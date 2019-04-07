package cn.core.action;

import cn.core.beans.Medicine;
import cn.core.common.beans.PageReq;
import cn.core.common.beans.PageResp;
import cn.core.common.beans.ResultBean;
import cn.core.services.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/medicine")
@RestController
public class MedicineAction {

    @Autowired
    MedicineService service;

    @GetMapping(value = "/list")
    public ResultBean<PageResp<Medicine>> list(PageReq param) {
        ResultBean<PageResp<Medicine>> a = new ResultBean<>(service.listPage(param.toPageable(), param.getKeyword()));
        return a;
    }
}
