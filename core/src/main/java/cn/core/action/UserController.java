package cn.core.action;

import cn.core.beans.UserDO;
import cn.core.services.UserService;
import cn.core.utils.PageReq;
import cn.core.utils.PageResp;
import cn.core.utils.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 测试数据
     *
     * @param keyword
     * @return
     */
    @GetMapping("/search")
    public ResultBean<List<UserDO>> search(@RequestParam String keyword) {
        System.out.println("UserController.search()" + keyword);

        List<UserDO> nodes = new ArrayList<>();

        return new ResultBean<>(nodes);
    }

    @GetMapping("/list")
    public ResultBean<PageResp<UserDO>> list(PageReq page) {
        return new ResultBean<>(userService.list(page.toPageable(), page.getKeyword()));
    }

    /**
     * 修改密码
     *
     * @param id
     * @param password
     * @return
     */
    @PostMapping("updatepwd")
    public ResultBean<Boolean> updatePwd(long id, String password) {
        System.out.println(userService.getClass());//FIXME DELETE
        userService.updatePwd(id, password.trim());
        return new ResultBean<>(true);
    }
}
