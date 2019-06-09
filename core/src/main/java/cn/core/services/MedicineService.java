package cn.core.services;

import cn.core.beans.MedicineDO;
import cn.core.consts.ResultCode;
import cn.core.daos.MedicinePageDao;
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
import java.util.*;

@Service
@Slf4j
public class MedicineService {

    @Autowired
    private MedicinePageDao medicinePageDao;

    public PageResp<MedicineDO> listPage(Pageable pageable, String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return new PageResp<>(medicinePageDao.findAll(pageable));
        } else {
            // asd也可以用spring JPA 的 Specification 来实现查找
            return new PageResp<>(medicinePageDao.findAllByKeyword(keyword, pageable));
        }
    }

    public Result add(MedicineDO medicineDO) {
        Date date = new Date();
        medicineDO.setUpdateTime(date);
        medicinePageDao.save(medicineDO);
        return Result.success();
    }

    public Result delete(Long id) {
        medicinePageDao.deleteById(id);
        return Result.success();
    }

    public Result fileExport(HttpServletResponse response) {
        List<MedicineDO> list = (List<MedicineDO>) medicinePageDao.findAll();

        if (list.size() == 0) {
            Result.failure(ResultCode.NOT_FOUND);
        }

        String sTitle = "本位码,药品名称,库存量,规格,供应商";
        String fName = "药品库存清单";
        String mapKey = "medicineNumber,medicineName,stock,stockUnit,supplier";
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> map;
        for (MedicineDO order : list) {
            map = new HashMap<>();
            map.put("medicineNumber", order.getMedicineNumber());
            map.put("medicineName", order.getMedicineName());
            map.put("stock", order.getStock());
            map.put("stockUnit", order.getStockUnit());
            map.put("supplier", order.getSupplier());
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
