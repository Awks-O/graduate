package cn.core.action;

import cn.core.common.beans.ResultBean;
import cn.core.common.rbac.User;
import cn.core.common.utils.UserUtil;
import cn.core.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * app相关的controller，支持跨域
 *
 * @author 肖文杰 https://github.com/xwjie/
 */
@RequestMapping("/app")
@RestController
@CrossOrigin
public class AppController {
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public ResultBean<User> login(HttpSession session, @RequestParam String username, @RequestParam String
            password) {
        logger.info("login user:" + username);

        User user = userService.login(username, password);

        //FIXME
        session.setAttribute(UserUtil.KEY_USER, user);

        return new ResultBean<User>(user);
    }

    @PostMapping(value = "/user")
    public ResultBean<User> login(HttpSession session) {
        logger.info("get current user");
        return new ResultBean<User>(UserUtil.getUserIfLogin());
    }

    @PostMapping(value = "/logout")
    public ResultBean<Boolean> logout(HttpSession session) {
        //session.invalidate();
        userService.logout();

        return new ResultBean<Boolean>(true);
    }
}
