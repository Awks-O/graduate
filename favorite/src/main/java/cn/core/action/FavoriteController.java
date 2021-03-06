package cn.core.action;

import cn.core.beans.Favorite;
import cn.core.common.beans.ResultBean;
import cn.core.services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * 收藏相关的controller，支持跨域
 *
 * @author 肖文杰 https://github.com/xwjie/
 */
@RequestMapping("/favorite")
@RestController
@CrossOrigin
public class FavoriteController {

    @Autowired
    FavoriteService favoriteService;

    @GetMapping("/all")
    public ResultBean<Collection<Favorite>> getAll(@RequestParam int type) {
        return new ResultBean<Collection<Favorite>>(favoriteService.getAll(type));
    }

    /**
     * 新增配置
     * <p>
     * FIXME 同时支持json格式和表单格式
     *
     * @param favorite
     * @return
     */
    @PostMapping("/add")
    public ResultBean<Long> add(@RequestBody @Valid Favorite favorite) {
        return new ResultBean<Long>(favoriteService.add(favorite));
    }

    @PostMapping("/delete")
    public ResultBean<Boolean> delete(@RequestParam long id) {
        return new ResultBean<Boolean>(favoriteService.delete(id));
    }
}
