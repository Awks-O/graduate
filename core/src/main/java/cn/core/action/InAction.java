package cn.core.action;

import cn.core.beans.InInfoDO;
import cn.core.req.PageReq;
import cn.core.resp.PageResp;
import cn.core.services.InInfoService;
import cn.core.utils.Result;
import cn.core.utils.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/medicine")
@RestController
public class InAction {

    @Autowired
    private InInfoService service;

    @GetMapping("/inInfo/list")
    public ResultBean<PageResp<InInfoDO>> list(PageReq param) {
        PageResp<InInfoDO> a = service.listPage(param.toPageable(), param.getKeyword());
        a.setPagesize(param.getPageSize());
        return new ResultBean<>(a);
    }

    @PostMapping("/inInfo/edit")
    public Result addMedicine(@RequestBody InInfoDO param) {
        return service.add(param);
    }

    @PostMapping("/inInfo/del")
    public Result delMedicine(Long id) {
        return service.delete(id);
    }

    @GetMapping("/inInfo/export")
    public Result export(HttpServletResponse response) {
        return service.fileExport(response);
    }
}
