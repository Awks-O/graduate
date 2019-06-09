package cn.core.action;

import cn.core.beans.UserDO;
import cn.core.consts.ResultCode;
import cn.core.services.UserService;
import cn.core.utils.Result;
import cn.core.utils.ResultBean;
import cn.core.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * app相关的controller，支持跨域
 */
@RequestMapping("/app")
@RestController
@CrossOrigin
public class AppAction {
    private static final Logger logger = LoggerFactory.getLogger(AppAction.class);

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public Result login(HttpSession session, @RequestParam String username, @RequestParam String
            password) {
        logger.info("login user:" + username);

        UserDO user;
        try {
            user = userService.login(username, password);
        } catch (Exception ignored) {
            return Result.failure(ResultCode.FAIL);
        }
        //FIXME
        session.setAttribute(UserUtil.KEY_USER, user);

        return Result.success(user);
    }

    @PostMapping(value = "/user")
    public ResultBean<UserDO> login(HttpSession session) {
        logger.info("get current user");
        return new ResultBean<>(UserUtil.getUserIfLogin());
    }

    @PostMapping(value = "/logout")
    public ResultBean<Boolean> logout(HttpSession session) {
        //session.invalidate();
        userService.logout();

        return new ResultBean<>(true);
    }
}
