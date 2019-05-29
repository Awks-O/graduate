package cn.core.action;

import cn.core.beans.OutInfoDO;
import cn.core.req.PageReq;
import cn.core.resp.PageResp;
import cn.core.services.OutInfoService;
import cn.core.utils.Result;
import cn.core.utils.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/medicine")
@RestController
public class OutAction {

    @Autowired
    private OutInfoService service;

    @GetMapping("/outInfo/list")
    public ResultBean<PageResp<OutInfoDO>> list(PageReq param) {
        ResultBean<PageResp<OutInfoDO>> a = new ResultBean<>(service.listPage(param.toPageable(), param.getKeyword()));
        return a;
    }

    @PostMapping("/outInfo/edit")
    public Result addMedicine(@RequestBody OutInfoDO param) {
        return service.add(param);
    }

    @PostMapping("/outInfo/del")
    public Result delMedicine(Long id) {
        return service.delete(id);
    }

    @GetMapping("/outInfo/export")
    public Result export(HttpServletResponse response) {
        return service.fileExport(response);
    }
}
