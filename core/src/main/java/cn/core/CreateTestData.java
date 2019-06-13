package cn.core;

import cn.core.beans.*;
import cn.core.consts.Roles;
import cn.core.daos.*;
import cn.core.services.ConfigService;
import cn.core.services.UserService;
import cn.core.utils.PasswordUtil;
import cn.core.utils.UserUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * 增加测试数据 （上线时候需要删除掉）
 */
@Component
@Slf4j
public class CreateTestData implements CommandLineRunner {

    @Autowired
    private MedicinePageDao medicinePageDao;

    @Autowired
    private OutInfoDao detailDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private InInfoDao inInfoDao;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userSevice;

    @Override
    public void run(String... args) {
        // 用户不存在则创建测试数据
        if (userDao.findByName("admin") == null) {
            log.info("创建测试数据.....");

            createDetail();
//            createMedicine();
//            createUsers();

            log.info("创建测试数据完毕");
        }
    }

    private void createDetail() {
        log.info("---addDetail--");

        OutInfoDO detailDO;

//        for (int i=0; i <= 10; ++i){
//            detailDO = new OutInfoDO();
//            detailDO.setUnitPrice("1.1");
//            detailDO.setAmount(1000);
//            detailDO.setMedicineName("药品"+i);
//            detailDO.setMedicineNumber("药品编号"+i);
//            detailDO.setSupplier("供应商"+i);
//            detailDO.setOutDate(new Date());
//            detailDO.setStockUnit("盒");
//            detailDO.setCreateTime(new Date());
//            detailDao.save(detailDO);
//        }

        InInfoDO inInfoDO;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        for (int i = 0; i <= 10; ++i) {
            inInfoDO = new InInfoDO();
            inInfoDO.setExpirationDate("24个月");
            inInfoDO.setProductionDate(new Date());
            inInfoDO.setUnitPrice("17.2");
            Random random = new Random();
            inInfoDO.setAmount(random.nextInt(10000) % (10000 - 1001) + 1000);
            inInfoDO.setMedicineName("琥乙红霉素片");
            inInfoDO.setMedicineNumber("86904036000105");
            inInfoDO.setSupplier("青岛正大海尔制药有限公司");
            inInfoDO.setInDate(calendar.getTime());
            calendar.add(Calendar.MONTH, 3);
            inInfoDO.setStockUnit("12片/板*2板/盒");
            inInfoDO.setCreateTime(new Date());
            inInfoDao.save(inInfoDO);
        }

    }

    private void createMedicine() {
        log.info("---addUser---");

        MedicineDO data;

        Date date = new Date();
        for (int i = 1; i <= 10; i++) {
            data = new MedicineDO();
            data.setAlarmValue(100D);
            data.setMedicineName("药品" + i);
            data.setMedicineNumber("药品编号" + i);
            data.setPeriod(3);
            data.setStock(1000D);
            data.setStockUnit("盒");
            data.setForecast(1);
            data.setSupplier("供应商");
            data.setUsableTime(date);
            data.setCreateTime(date);
            data.setUpdateTime(date);
            medicinePageDao.save(data);
        }
    }

    @Autowired
    private SecurityManager securityManager;

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

    public void createUsers() {
        log.error("---addUser---");

        // role
        RoleDO normalRole = new RoleDO();

        normalRole.setName(Roles.NORMAL_USER);
        normalRole.setComment("普通用户");

        normalRole = roleDao.save(normalRole);


        RoleDO adminRole = new RoleDO();

        adminRole.setName(Roles.ADMIN);
        adminRole.setComment("管理员");

        adminRole = roleDao.save(adminRole);

        // amdin
        UserDO admin = new UserDO();

        admin.setName("admin");
        admin.setNick("管理員");

        // 盐和密码
        admin.setSalt("admin");
        String password = PasswordUtil.renewPassword("123456", admin.getSalt());

        // 计算后密码
        admin.setPassword(password);

        // 角色
        admin.setRoles(Lists.newArrayList(adminRole, normalRole));

        userDao.save(admin);

        for (int i = 1; i <= 10; i++) {
            UserDO user = new UserDO();

            user.setName("user" + i);
            user.setNick("测试用户" + i);

            // 盐和密码
            user.setSalt(user.getName());
            String password2 = PasswordUtil.renewPassword("123456", user.getSalt());

            // 计算后密码
            user.setPassword(password2);

            user.setRoles(Lists.newArrayList(normalRole));

            userDao.save(user);
        }
    }
}

