package cn.core.SpringbootCodeTemplate;

import cn.core.SpringbootCodeTemplateApplication;
import cn.core.beans.Config;
import cn.core.beans.UserDO;
import cn.core.daos.UserDao;
import cn.core.exception.CheckException;
import cn.core.services.ConfigService;
import cn.core.utils.UserUtil;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootCodeTemplateApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigTest {

    @Autowired
    ConfigService configService;

    @Autowired
    UserDao userDao;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static long randomNum = 0;
    private static String userName = null;

    @Before
    public void random() {
        // 整个方法会执行多次
        if (randomNum == 0) {
            randomNum = System.currentTimeMillis();
            userName = "junituser" + randomNum;
        }
    }

    @Test
    public void test_medicine() {
        System.out.println("\n\n---addUser: " + userName);

        // 新增用户
        {
            UserDO user = new UserDO();

            user.setName(userName);
            user.setNick("测试用户");

            // user.setRole(Roles.USER);

            user = userDao.save(user);

            assertTrue(user.getId() > 0L);
        }

        // 测试新增数据是否成功
        {
            UserDO user = userDao.findByName(userName);
            assertNotNull(user);
        }

    }

    @Test
    public void test_in_storage() {
        System.out.println("\n\n---addConfig---\n\n");

        // 设置当前登陆用户
        UserDO user = userDao.findByName("admin");
        UserUtil.setUser(user);

        // 创建config数据
//        {
//            Config config = new Config();
//
//            config.setName("测试数据：" + randomNum);
//            System.out.println("测试数据：" + randomNum);
//            config.setValue("https://github.com/Awks-O");
//            config.setDescription("admin：" + randomNum);
//
//            long newId = configService.add(config);
//            assertTrue(newId > 0L);
//        }
    }

    @Test
    public void test_out_storage() {
        System.out.println("\n\n---testGetConfig---\n\n");

        Collection<Config> all = configService.getAll();

        // 有数据
//        assertTrue(all.size() > 0);
    }

    @Test
    public void test_purchase() {
        System.out.println("\n\n---addTestData---\n\n");

        UserDO user = userDao.findByName(userName);

        // 设置当前登陆用户
        UserUtil.setUser(user);

        // 创建config数据
        {
            System.out.println("\n\n--测试[参数为空]---\n\n");

//            thrown.expect(CheckException.class);
//            thrown.expectMessage("参数为空");

            //configService.add(null);
        }

    }

    @Test
    public void test_user() {
        System.out.println("\n\n---addTestData---\n\n");

        UserDO user = userDao.findByName(userName);

        // 设置当前登陆用户
        UserUtil.setUser(user);

        // 创建config数据
        {
            System.out.println("\n\n--测试[取值为空]---\n\n");
//
//            thrown.expect(CheckException.class);
//            thrown.expectMessage("取值为空");

            Config config = new Config();

            config.setName("测试数据：" + randomNum);
            config.setValue(null);

//            configService.add(config);
        }
    }

    @Test
    public void test_forecast() {
        System.out.println("\n\n---addTestData---\n\n");

        UserDO user = userDao.findByName(userName);

        // 设置当前登陆用户
        UserUtil.setUser(user);

        // 创建config数据
        {
            System.out.println("\n\n--测试[名称已经存在]---\n\n");

//            thrown.expect(CheckException.class);
//            thrown.expectMessage("名称已经存在");

            Config config = new Config();

            config.setName("测试数据：" + randomNum);
            config.setValue("https://github.com/Awks-O");
            config.setDescription("admin：" + randomNum);

//            configService.add(config);
        }
    }

    @Test
    public void test_Config() {
        System.out.println("\n\n---deleteConfig---\n\n");

        UserDO user = userDao.findByName(userName);

        // 设置当前登陆用户
        UserUtil.setUser(user);

        Config config = configService.getByName("测试数据：" + randomNum);
//        assertTrue(config != null);
//        assertTrue(configService.delete(config.getId()));
    }

}
