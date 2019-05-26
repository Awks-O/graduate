package cn.core.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class MedicineDO extends BaseEntity {

    /**
     * 药品编号
     */
    private String medicineNumber;

    /**
     * 药品名称
     */
    private String medicineName;

    /**
     * 存储单位
     */
    private String stockUnit;

    /**
     * 库存量
     */
    private Double stock;

    /**
     * 警报值
     */
    private Double alarmValue;

    /**
     * 剩余可用日期
     */
    private Date usableTime;

    /**
     * 预订购日期
     */
    private Date purchaseDate;

    /**
     * 是否预测
     */
    private Integer forecast;

    /**
     * 供应商
     */
    private String supplier;
}
