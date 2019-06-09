package cn.core.services;

import cn.core.beans.InInfoDO;
import cn.core.beans.MedicineDO;
import cn.core.consts.ResultCode;
import cn.core.daos.InInfoDao;
import cn.core.daos.MedicineDao;
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
public class InInfoService {

    @Autowired
    private InInfoDao inInfoDao;

    @Autowired
    private MedicineDao medicineDao;

    public PageResp<InInfoDO> listPage(Pageable pageable, String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return new PageResp<>(inInfoDao.findAll(pageable));
        } else {
            // asd也可以用spring JPA 的 Specification 来实现查找
            return new PageResp<>(inInfoDao.findAllByKeyword(keyword, pageable));
        }
    }

    public Result add(InInfoDO infoDO) {
        Date date = new Date();
        infoDO.setUpdateTime(date);
        inInfoDao.save(infoDO);
        MedicineDO medicineDO = medicineDao.findByKeyword(infoDO.getMedicineNumber());
        medicineDO.setStock(medicineDO.getStock()+infoDO.getAmount().doubleValue());
        medicineDO.setUpdateTime(date);
        medicineDao.save(medicineDO);
        return Result.success();
    }

    public Result delete(Long id) {
        inInfoDao.deleteById(id);
        return Result.success();
    }

    public Result fileExport(HttpServletResponse response) {
        List<InInfoDO> list = (List<InInfoDO>) inInfoDao.findAll();

        if (list.size() == 0) {
            Result.failure(ResultCode.NOT_FOUND);
        }

        String sTitle = "本位码,药品名称,进货量量,规格,单价,生产日期,保质期,供应商,入库日期";
        String fName = "药品入库清单";
        String mapKey = "medicineNumber,medicineName,amount,stockUnit,unitPrice,productionDate,expirationDate,supplier,inDate";
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> map;
        for (InInfoDO order : list) {
            map = new HashMap<>();
            map.put("medicineNumber", order.getMedicineNumber());
            map.put("medicineName", order.getMedicineName());
            map.put("amount", order.getAmount());
            map.put("stockUnit", order.getStockUnit());
            map.put("unitPrice", order.getUnitPrice());
            map.put("productionDate", DateFormat.getDateInstance(DateFormat.DEFAULT).format(order.getProductionDate()));
            map.put("expirationDate", order.getExpirationDate());
            map.put("supplier", order.getSupplier());
            map.put("inDate", DateFormat.getDateInstance(DateFormat.DEFAULT).format(order.getInDate()));
            dataList.add(map);
        }
        try (final OutputStream os = response.getOutputStream()) {
            ExportUtil.responseSetProperties(fName, response);
            ExportUtil.doExport(dataList, sTitle, mapKey, os);
            return null;
        } catch (Exception e) {
            log.error("生成csv文件失败", e);
        }
        return Result.failure(ResultCode.FAIL, "数据导出出错");
    }
}
