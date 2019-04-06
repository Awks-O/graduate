package cn.core.action;

import cn.core.beans.Blog;
import cn.core.common.annotations.Log;
import cn.core.common.beans.PageReq;
import cn.core.common.beans.PageResp;
import cn.core.common.beans.ResultBean;
import cn.core.consts.LogConst;
import cn.core.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * blog
 *
 * @author 肖文杰 https://github.com/xwjie/
 */
@RequestMapping("/blog")
@RestController
@CrossOrigin
public class BlogController {

    @Autowired
    BlogService blogService;

    @GetMapping("/all")
    @Log(action = LogConst.ACTION_QUERY, itemType = LogConst.ITEM_TYPE_BLOG)
    public ResultBean<Collection<Blog>> getAll() {
        return new ResultBean<Collection<Blog>>(blogService.getAll());
    }

    @GetMapping(value = "/list")
    @Log(action = LogConst.ACTION_QUERY, itemType = LogConst.ITEM_TYPE_BLOG, param = "#param")
    public ResultBean<PageResp<Blog>> list(PageReq param) {
        return new ResultBean<>(blogService.listPage(param.toPageable(), param.getKeyword()));
    }

    /**
     * 新增配置
     *
     * @param blog
     * @return
     */
    @PostMapping("/add")
    @Log(action = LogConst.ACTION_ADD, itemType = LogConst.ITEM_TYPE_BLOG, itemId = "#blog.title")
    public ResultBean<Long> add(@RequestBody @Valid Blog blog) {
        return new ResultBean<Long>(blogService.add(blog));
    }

    @PostMapping("/delete")
    @Log(action = LogConst.ACTION_DELETE, itemType = LogConst.ITEM_TYPE_BLOG, itemId = "#id")
    public ResultBean<Boolean> delete(@RequestParam long id) {
        return new ResultBean<Boolean>(blogService.delete(id));
    }
}
