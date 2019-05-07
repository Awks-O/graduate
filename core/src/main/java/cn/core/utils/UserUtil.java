package cn.core.utils;

import cn.core.beans.RoleDO;
import cn.core.beans.UserDO;
import cn.core.consts.Roles;
import cn.core.exception.UnloginException;
import org.apache.log4j.MDC;

import java.util.List;
import java.util.Locale;

public class UserUtil {

    private final static ThreadLocal<UserDO> tlUser = new ThreadLocal<>();

    private final static ThreadLocal<Locale> tlLocale = ThreadLocal.withInitial(() -> {
        // 语言的默认值
        return Locale.CHINESE;
    });

    public static final String KEY_LANG = "lang";

    public static final String KEY_USER = "user";

    public static void setUser(UserDO user) {
        tlUser.set(user);

        // 把用户信息放到log4j
        MDC.put(KEY_USER, user.getName());
    }

    /**
     * 如果没有登录，返回null
     *
     * @return
     */
    public static UserDO getUserIfLogin() {
        return tlUser.get();
    }

    /**
     * 如果没有登录会抛出异常
     *
     * @return
     */
    public static UserDO getUser() {
        UserDO user = tlUser.get();

        if (user == null) {
            throw new UnloginException();
        }

        return user;
    }

    public static long getUserId() {
        return getUser().getId();
    }

    public static void setLocale(String locale) {
        setLocale(new Locale(locale));
    }

    public static void setLocale(Locale locale) {
        tlLocale.set(locale);
    }

    public static Locale getLocale() {
        return tlLocale.get();
    }

    public static void clearAllUserInfo() {
        tlUser.remove();
        tlLocale.remove();

        MDC.remove(KEY_USER);
    }

    /**
     * 是否管理员
     *
     * @return
     */
    public static boolean isAdmin() {
        List<RoleDO> roles = getUser().getRoles();

        for (RoleDO role : roles) {
            if (Roles.ADMIN.equals(role.getName())) {
                return true;
            }
        }

        return false;

    }
}
