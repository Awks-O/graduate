package cn.core.action;

import cn.core.beans.Config;
import cn.core.req.PageReq;
import cn.core.resp.PageResp;
import cn.core.services.ConfigService;
import cn.core.utils.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * 配置相关的controller，支持跨域
 */
@RequestMapping("/config")
@RestController
@CrossOrigin
public class ConfigAction {

    @Autowired
    private ConfigService configService;

    @GetMapping("/all")
    public ResultBean<Collection<Config>> getAll() {
        return new ResultBean<>(configService.getAll());
    }

    @GetMapping(value = "/list")
    public ResultBean<PageResp<Config>> list(PageReq param) {
        return new ResultBean<>(configService.listPage(param.toPageable(), param.getKeyword()));
    }

    /**
     * 新增配置
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
        return new ResultBean<>(configService.delete(id));
    }
}
