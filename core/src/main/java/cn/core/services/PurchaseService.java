package cn.core.services;

import cn.core.beans.InInfoDO;
import cn.core.beans.PurchaseDO;
import cn.core.consts.ResultCode;
import cn.core.daos.InInfoDao;
import cn.core.daos.PurchaseDao;
import cn.core.daos.PurchasePageDao;
import cn.core.req.PageReq;
import cn.core.resp.PageResp;
import cn.core.utils.ExportUtil;
import cn.core.utils.Result;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PurchaseService {

    @Autowired
    private PurchasePageDao purchasePageDao;

    @Autowired
    private PurchaseDao purchaseDao;

    @Autowired
    private InInfoDao infoDao;

    public Result forecast(String num) {
        PageReq pageReq = new PageReq();
        pageReq.setPageSize(30);
        pageReq.setPage(1);
        List<InInfoDO> list = infoDao.findAllByKeyword1
                (num, pageReq.toPageable()).getContent();
        List<Map<String, String>> tempList = new ArrayList<>();
        IdentityHashMap<String, String> map;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        for (InInfoDO inInfoDO : list) {
            map = new IdentityHashMap<>();
            map.put(new String("日期"), dateFormat.format(inInfoDO.getInDate()));
            map.put(new String("数量"), inInfoDO.getAmount().toString());
            tempList.add(map);
        }
        return Result.success(tempList, "success");
    }

    public PageResp<PurchaseDO> listPage(Pageable pageable, String keyword, String keyword1) {
        if (StringUtils.isEmpty(keyword1)) {
            return new PageResp<>(query(pageable, keyword));
        } else {
            PageResp<PurchaseDO> resp = new PageResp<>(query(pageable, keyword));
            List<PurchaseDO> list = resp.getRows();
            Date date;
            try {
                date = DateFormat.getDateInstance().parse(keyword1);
            } catch (ParseException e) {
                return null;
            }
            list = list.stream().filter(a -> a.getPurchaseDate() == null || a.getPurchaseDate().getTime() < date.getTime()).collect(Collectors.toList());
            resp.setRows(list);
            return resp;
        }
    }

    public Result add(PurchaseDO purchaseDO) {
        Date date = new Date();
        purchaseDO.setUpdateTime(date);
        purchasePageDao.save(purchaseDO);
        return Result.success();
    }

    public Result delete(Long id) {
        purchasePageDao.deleteById(id);
        return Result.success();
    }

    public Result fileExport(HttpServletResponse response, String keyword1, String keyword) {
        List<PurchaseDO> list;
        if (StringUtils.isEmpty(keyword)) {
            list = Lists.newArrayList(purchaseDao.findAll());
        } else {
            list = purchaseDao.findByKeyword1(keyword);
        }
        Date date;
        try {
            date = DateFormat.getDateInstance().parse(keyword1);
        } catch (ParseException e) {
            return Result.failure(ResultCode.FAIL);
        }
        list = list.stream().filter(a -> a.getPurchaseDate() == null
                || a.getPurchaseDate().getTime() < date.getTime()).collect(Collectors.toList());
        if (list.size() == 0) {
            Result.failure(ResultCode.NOT_FOUND);
        }

        String sTitle = "本位码,药品名称,预计采购量,规格,供应商,预订购日期";
        String fName = "药品采购清单 ";
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
            map.put("purchaseDate", order.getPurchaseDate()!=null? DateFormat.getDateInstance(DateFormat.DEFAULT).format(order.getPurchaseDate()): null);
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
        if (StringUtils.isEmpty(keyword)) {
            return purchasePageDao.findAll(pageable);
        } else {
            return purchasePageDao.findAllByKeyword(keyword, pageable);
        }
    }

}
