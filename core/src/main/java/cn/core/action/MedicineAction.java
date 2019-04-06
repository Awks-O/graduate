package cn.core.action;

import cn.core.common.beans.PageReq;
import cn.core.common.beans.PageResp;
import cn.core.common.beans.ResultBean;
import cn.core.domain.MedicineDO;
import cn.core.services.MedicineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/medicine")
@RestController
public class MedicineAction {

    @Resource
    private MedicineService service;

    @GetMapping(value = "/list")
    public ResultBean<PageResp<MedicineDO>> list(PageReq param) {
        return new ResultBean<>(service.listPage(param.toPageable(), param.getKeyword()));
    }
}
