package cn.core.action;

import cn.core.beans.PurchaseDO;
import cn.core.req.PageReq;
import cn.core.resp.PageResp;
import cn.core.services.PurchaseService;
import cn.core.utils.Result;
import cn.core.utils.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/medicine")
@RestController
public class PurchaseAction {

    @Autowired
    private PurchaseService service;

    @GetMapping("/purchase/forecast")
    public Result forecast(String num){
        return service.forecast(num);
    }
    @GetMapping("/purchase/list")
    public ResultBean<PageResp<PurchaseDO>> list(PageReq param) {
        ResultBean<PageResp<PurchaseDO>> a = new ResultBean<>(service.listPage(param.toPageable(), param.getKeyword(), param.getKeyword1()));
        return a;
    }

    @PostMapping("/purchase/edit")
    public Result addMedicine(@RequestBody PurchaseDO param) {
        return service.add(param);
    }

    @PostMapping("/purchase/del")
    public Result delMedicine(Long id) {
        return service.delete(id);
    }

    @GetMapping("/purchase/export")
    public Result export(HttpServletResponse response, PageReq param) {
        return service.fileExport(response, param.toPageable(), param.getKeyword(), param.getKeyword1());
    }
}
