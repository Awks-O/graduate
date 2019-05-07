package cn.core.action;

import cn.core.beans.Config;
import cn.core.utils.PageReq;
import cn.core.utils.PageResp;
import cn.core.utils.ResultBean;
import cn.core.services.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * 配置相关的controller，支持跨域
 *
 */
@RequestMapping("/config")
@RestController
@CrossOrigin
public class ConfigController {

    @Autowired
    ConfigService configService;

    @GetMapping("/all")
    public ResultBean<Collection<Config>> getAll() {
        return new ResultBean<Collection<Config>>(configService.getAll());
    }

    @GetMapping(value = "/list")
    public ResultBean<PageResp<Config>> list(PageReq param) {
        return new ResultBean<>(configService.listPage(param.toPageable(), param.getKeyword()));
    }

    /**
     * 新增配置
     * <p>
     * FIXME 同时支持json格式和表单格式
     *
     * @param config
     * @return
     */
    @PostMapping("/add")
    public ResultBean<Long> add(@RequestBody Config config) {
        System.out.println(configService.getClass());
        return new ResultBean<Long>(configService.add(config));
    }

    @PostMapping("/delete")
    public ResultBean<Boolean> delete(long id) {
        return new ResultBean<Boolean>(configService.delete(id));
    }
}
