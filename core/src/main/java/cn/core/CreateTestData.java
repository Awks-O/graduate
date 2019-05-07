package cn.core;

import cn.core.beans.Config;
import cn.core.beans.MedicineDO;
import cn.core.daos.MedicineDao;
import cn.core.daos.UserDao;
import cn.core.services.ConfigService;
import cn.core.services.UserService;
import cn.core.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 增加测试数据 （上线时候需要删除掉）
 */
@Component
@Slf4j
public class CreateTestData implements CommandLineRunner {

    @Autowired
    private MedicineDao medicineDao;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userSevice;

    @Override
    public void run(String... args) throws Exception {
        // 用户不存在则创建测试数据
        if (userDao.findByName("Awks-O") == null) {
            log.error("创建测试数据.....");

            createUsers();

            // FIXME
            // createConfigs();

            log.error("创建测试数据完毕");
        }
    }

    public void createUsers() {
        log.error("---addUser---");

        // role
        MedicineDO data;

        Date date = new Date();
        for (int i = 1; i <= 10; i++) {
            data = new MedicineDO();

            data.setAlarmValue(100D);
            data.setMedicineName("name" + i);
            data.setMedicineNumber("num" + i);
            data.setPurchaseDate(date);
            data.setStock(1000D);
            data.setStockUnit("mm");
            data.setUsableTime(date);
            data.setCreateTime(date);
            data.setUpdateTime(date);

            medicineDao.save(data);
        }
    }

    @Autowired
    SecurityManager securityManager;

    public void createConfigs() {
        log.error("---addTestData---");

        // 登陆
        userSevice.login("admin", "123456");

        //
        ThreadContext.bind(securityManager);

        for (int i = 1; i <= 20; i++) {

            Config config = new Config();

            config.setName("测试数据：" + i);
            System.out.println("测试数据：" + i);
            config.setValue("https://github.com/Awks-O");
            config.setDescription("admin：" + i);

            // 创建记录的用户
            config.setCreator(UserUtil.getUser());

            configService.add(config);
        }
    }
}

