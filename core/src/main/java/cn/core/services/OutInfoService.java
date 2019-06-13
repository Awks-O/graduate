package cn.core.services;

import cn.core.beans.MedicineDO;
import cn.core.beans.OutInfoDO;
import cn.core.consts.ResultCode;
import cn.core.daos.MedicineDao;
import cn.core.daos.OutInfoDao;
import cn.core.resp.PageResp;
import cn.core.utils.ExportUtil;
import cn.core.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.*;

@Service
@Slf4j
public class OutInfoService {

    @Autowired
    private OutInfoDao detailDao;

    @Autowired
    private MedicineDao medicineDao;

    public PageResp<OutInfoDO> listPage(Pageable pageable, String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return new PageResp<>(detailDao.findAll(pageable));
        } else {
            // asd也可以用spring JPA 的 Specification 来实现查找
            return new PageResp<>(detailDao.findAllByKeyword(keyword, pageable));
        }
    }

    public Result add(OutInfoDO outInDetailDO) {
        Date date = new Date();
        outInDetailDO.setUpdateTime(date);
        detailDao.save(outInDetailDO);
        MedicineDO medicineDO = medicineDao.findByKeyword(outInDetailDO.getMedicineNumber());
        medicineDO.setStock(medicineDO.getStock() - outInDetailDO.getAmount().doubleValue());
        medicineDO.setUpdateTime(date);
        medicineDao.save(medicineDO);
        return Result.success();
    }

    public Result delete(Long id) {
        detailDao.deleteById(id);
        return Result.success();
    }

    public Result fileExport(HttpServletResponse response) {
        List<OutInfoDO> list = (List<OutInfoDO>) detailDao.findAll();

        if (list.size() == 0) {
            Result.failure(ResultCode.NOT_FOUND);
        }

        String sTitle = "本位码,药品名称,进货量,规格,退货单价,供应商,入库日期";
        String fName = "药品出库清单";
        String mapKey = "medicineNumber,medicineName,amount,stockUnit,unitPrice,supplier,outDate";
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> map;
        for (OutInfoDO order : list) {
            map = new HashMap<>();
            map.put("medicineNumber", order.getMedicineNumber());
            map.put("medicineName", order.getMedicineName());
            map.put("amount", order.getAmount());
            map.put("stockUnit", order.getStockUnit());
            map.put("unitPrice", order.getUnitPrice());
            map.put("supplier", order.getSupplier());
            map.put("outDate", DateFormat.getDateInstance(DateFormat.DEFAULT).format(order.getOutDate()));
            dataList.add(map);
        }
        try (final OutputStream os = response.getOutputStream()) {
            ExportUtil.setHeader(fName, response);
            ExportUtil.doExport(dataList, sTitle, mapKey, os);
            return null;
        } catch (Exception e) {
            log.error("生成csv文件失败", e);
        }
        return Result.failure(ResultCode.FAIL, "数据导出出错");
    }

    public static void main(String[] args) {
        for (int i = 1; i < 1000; i++) {
            System.out.print(i * 90 + ",");
        }
    }
}
