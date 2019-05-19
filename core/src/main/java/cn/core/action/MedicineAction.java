package cn.core.action;

import cn.core.beans.MedicineDO;
import cn.core.req.PageReq;
import cn.core.resp.PageResp;
import cn.core.services.MedicineService;
import cn.core.utils.Result;
import cn.core.utils.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/medicine")
@RestController
//@CrossOrigin(origins = "http://localhost:9090")
public class MedicineAction {

    @Autowired
    private MedicineService service;

    @GetMapping("/list")
    public ResultBean<PageResp<MedicineDO>> list(PageReq param) {
        ResultBean<PageResp<MedicineDO>> a = new ResultBean<>(service.listPage(param.toPageable(), param.getKeyword()));
        return a;
    }

    @PostMapping("/edit")
    public Result addMedicine(@RequestBody MedicineDO param) {
        return service.add(param);
    }

    @PostMapping("/del")
    public Result delMedicine(Long id) {
        return service.delete(id);
    }
}
