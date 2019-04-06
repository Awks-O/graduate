package cn.core.common.utils;

import cn.core.daos.ConfigDao;
import cn.core.daos.MedicineDao;
import cn.core.jms.JMSTool;
import cn.core.jpa.JPAListener;
import cn.core.tool.ConfigUtil;
import cn.core.tool.MedicineUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;

/**
 * 工具类的注入
 *
 * @author 肖文杰
 */
//@Configuration
public class StaticFieldInjectionConfiguration {

    @Autowired
    MessageSource resources;

    @Autowired
    private MedicineDao medicineDao;

    @Autowired
    ConfigDao configDao;

    @Autowired
    JMSTool jmsTool;

    @PostConstruct
    private void init() {
        System.out.println("\n\n-----StaticFieldInjectionConfiguration----\n\n");

        CheckUtil.setResources(resources);
        ConfigUtil.setConfigDao(configDao);
        MedicineUtil.setMedicineDao(medicineDao);
        JPAListener.setJmsTool(jmsTool);
    }
}