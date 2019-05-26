package cn.core.action;

import cn.core.beans.InInfoDO;
import cn.core.beans.OutInfoDO;
import cn.core.req.PageReq;
import cn.core.resp.PageResp;
import cn.core.services.InInfoService;
import cn.core.services.OutInfoService;
import cn.core.utils.Result;
import cn.core.utils.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/medicine")
@RestController
public class InAction {

    @Autowired
    private InInfoService service;

    @GetMapping("/inInfo/list")
    public ResultBean<PageResp<InInfoDO>> list(PageReq param) {
        ResultBean<PageResp<InInfoDO>> a = new ResultBean<>(service.listPage(param.toPageable(), param.getKeyword()));
        return a;
    }

    @PostMapping("/inInfo/edit")
    public Result addMedicine(@RequestBody InInfoDO param) {
        return service.add(param);
    }

    @PostMapping("/inInfo/del")
    public Result delMedicine(Long id) {
        return service.delete(id);
    }
}
