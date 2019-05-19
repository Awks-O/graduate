package cn.core.services;

import cn.core.beans.UserDO;
import cn.core.consts.Roles;
import cn.core.daos.UserDao;
import cn.core.resp.PageResp;
import cn.core.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static cn.core.utils.CheckUtil.check;

/**
 * 用户相关处理类
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserDao userDao;

    public UserDO findUser(String username) {
        return userDao.findByName(username);
    }

    /**
     * FIXME
     *
     * @param username
     * @param password
     * @return
     */
    public UserDO login(String username, String password) {
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        subject.login(token);

        // 登陆成功，取出用户

        return (UserDO) subject.getPrincipal();
        //return findUser(username);
    }


    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    public PageResp<UserDO> list(Pageable pageable, String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return new PageResp<>(userDao.findAll(pageable));
        } else {
            // 也可以用springjpa 的 Specification 来实现查找
            return new PageResp<>(userDao.findAllByKeyword(keyword, pageable));
        }
    }

    /**
     * 修改密码
     *
     * @param id
     * @param password
     */
    //FIXME why not work？？！！
    @RequiresRoles(Roles.ADMIN)
    public void updatePwd(long id, String password) {
        UserDO user = userDao.findById(id).get();

        check(checkPwd(password), "password.invalid");

        // FIXME
        log.info("modify password, user id: " + id + ", password:" + password);

        // 生成新密码
        String hash = PasswordUtil.renewPassword(password, user.getSalt());

        user.setPassword(hash);

        userDao.save(user);
    }

    //TODO
    private boolean checkPwd(String password) {
        return password.length() >= 3;
    }
}
