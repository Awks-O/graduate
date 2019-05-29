package cn.core.services;

import cn.core.beans.InInfoDO;
import cn.core.beans.PurchaseDO;
import cn.core.beans.TrendDO;
import cn.core.consts.ResultCode;
import cn.core.daos.InInfoDao;
import cn.core.daos.PurchaseDao;
import cn.core.req.PageReq;
import cn.core.resp.PageResp;
import cn.core.utils.ExportUtil;
import cn.core.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PurchaseService {

    @Autowired
    private PurchaseDao purchaseDao;

    @Autowired
    private InInfoDao infoDao;

    public Result<TrendDO> forecast(String num) {
        List<InInfoDO> list = infoDao.findAllByKeyword1
                (num, new PageReq().toPageable()).getContent();
        List<Date> xList = list.stream().map(InInfoDO::getInDate).collect(Collectors.toList());
        List<Integer> yList = list.stream().map(InInfoDO::getAmount).collect(Collectors.toList());
        TrendDO trendDO = new TrendDO();
        trendDO.setAmountList(yList);
        trendDO.setDateList(xList);
        return Result.success(trendDO);
    }

    public PageResp<PurchaseDO> listPage(Pageable pageable, String keyword, String keyword1) {
        if (StringUtils.isEmpty(keyword1)) {
            return new PageResp<>(query(pageable, keyword));
        } else {
            PageResp<PurchaseDO> resp = new PageResp<>(query(pageable, keyword));
            List<PurchaseDO> list = resp.getRows();
            list = list.stream().filter(a -> a.getSupplier().contains(keyword1)).collect(Collectors.toList());
            resp.setRows(list);
            return resp;
        }
    }

    public Result add(PurchaseDO purchaseDO) {
        purchaseDao.save(purchaseDO);
        return Result.success();
    }

    public Result delete(Long id) {
        purchaseDao.deleteById(id);
        return Result.success();
    }

    public Result fileExport(HttpServletResponse response, Pageable pageable, String keyword, String keyword1) {
        List<PurchaseDO> list = query(pageable, keyword).getContent();
        if (!StringUtils.isEmpty(keyword1)) {
            list = list.stream().filter(a -> a.getSupplier().contains(keyword1)).collect(Collectors.toList());
        }

        if (list.size() == 0) {
            Result.failure(ResultCode.NOT_FOUND);
        }

        String sTitle = "本位码,药品名称,预计采购量,规格,供应商,预订购日期";
        String fName = "药品采购清单";
        String mapKey = "medicineNumber,medicineName,amount,unit,supplier,purchaseDate";
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> map;
        for (PurchaseDO order : list) {
            map = new HashMap<>();
            map.put("medicineNumber", order.getMedicineNumber());
            map.put("medicineName", order.getMedicineName());
            map.put("amount", order.getAmount());
            map.put("unit", order.getUnit());
            map.put("supplier", order.getSupplier());
            map.put("purchaseDate", DateFormat.getDateInstance(DateFormat.DEFAULT).format(order.getPurchaseDate()));
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


    private Page<PurchaseDO> query(Pageable pageable, String keyword) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 15);
        date = calendar.getTime();
        if (StringUtils.isEmpty(keyword)) {
            return purchaseDao.findAll(date, pageable);
        } else {
            return purchaseDao.findAllByKeyword(date, keyword, pageable);
        }
    }

    public static void main(String[] args) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -7100);
        date = calendar.getTime();
        System.out.println(date);
    }
}
