package cn.core.jpa;

import cn.core.beans.BaseEntity;
import cn.core.jms.JMSTool;
import cn.core.jms.JMSType;
import lombok.Setter;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;


public class JPAListener {

    @Setter
    static JMSTool jmsTool;


    @PostPersist
    public void createObject(BaseEntity o) {
        //System.out.println("create object :"+ o);
        jmsTool.sendMessage(JMSType.CREATE, o);
    }


    @PostRemove
    public void removeObject(BaseEntity o) {
        //System.out.println("create object :"+ o);
        jmsTool.sendMessage(JMSType.DELETE, o);
    }
}
