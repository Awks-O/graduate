package cn.core;

import cn.core.beans.Config;
import cn.core.beans.InInfoDO;
import cn.core.beans.MedicineDO;
import cn.core.beans.OutInfoDO;
import cn.core.daos.InInfoDao;
import cn.core.daos.MedicineDao;
import cn.core.daos.OutInfoDao;
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
import java.util.Random;

/**
 * 增加测试数据 （上线时候需要删除掉）
 */
//@Component
@Slf4j
public class CreateTestData implements CommandLineRunner {

    @Autowired
    private MedicineDao medicineDao;

    @Autowired
    private OutInfoDao detailDao;

    @Autowired
    private InInfoDao inInfoDao;

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
            log.info("创建测试数据.....");

            createDetail();
//            createMedicine();

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

        for (int i=0; i <= 10; ++i){
            inInfoDO = new InInfoDO();
            inInfoDO.setExpirationDate("36个月");
            inInfoDO.setProductionDate(new Date());
            inInfoDO.setUnitPrice("17.2");
            Random random = new Random();
            inInfoDO.setAmount(random.nextInt(10000)%(10000-1001) + 1000);
            inInfoDO.setMedicineName("美沙拉嗪肠溶片");
            inInfoDO.setMedicineNumber("86903810000546");
            inInfoDO.setSupplier("葵花药业集团佳木斯鹿灵制药有限公司");
            inInfoDO.setInDate(new Date());
            inInfoDO.setStockUnit("片");
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
            data.setPurchaseDate(date);
            data.setStock(1000D);
            data.setStockUnit("盒");
            data.setForecast(1);
            data.setSupplier("供应商");
            data.setUsableTime(date);
            data.setCreateTime(date);
            data.setUpdateTime(date);
            medicineDao.save(data);
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
}

