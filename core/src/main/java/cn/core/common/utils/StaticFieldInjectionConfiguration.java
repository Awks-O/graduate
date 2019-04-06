package cn.core.common.utils;

import javax.annotation.PostConstruct;

import cn.core.daos.ConfigDao;
import cn.core.jms.JMSTool;
import cn.core.jpa.JPAListener;
import cn.core.tool.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

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
    ConfigDao configDao;

    @Autowired
    JMSTool jmsTool;

    @PostConstruct
    private void init() {
        System.out.println("\n\n-----StaticFieldInjectionConfiguration----\n\n");

        CheckUtil.setResources(resources);
        ConfigUtil.setConfigDao(configDao);

        JPAListener.setJmsTool(jmsTool);
    }
}