package cn.core.action;

import cn.core.beans.MedicineDO;
import cn.core.services.MedicineService;
import cn.core.utils.PageReq;
import cn.core.utils.PageResp;
import cn.core.utils.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/medicine")
@RestController
//@CrossOrigin(origins = "http://localhost:9090")
public class MedicineAction {

    @Autowired
    MedicineService service;

    @GetMapping("/list")
    public ResultBean<PageResp<MedicineDO>> list(PageReq param) {
        ResultBean<PageResp<MedicineDO>> a = new ResultBean<>(service.listPage(param.toPageable(), param.getKeyword()));
        return a;
    }

    @PostMapping("add")
    public ResultBean<PageResp<MedicineDO>> addMedicine(PageReq param) {
        return null;
    }

    @PostMapping("edit")
    public ResultBean<PageResp<MedicineDO>> editMedicine(PageReq param) {
        return null;
    }

    @PostMapping("del")
    public ResultBean<PageResp<MedicineDO>> delMedicine(Integer id) {
        return null;
    }
}
