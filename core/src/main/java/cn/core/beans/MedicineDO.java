package cn.core.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {
        @Index(name = "medicine_number_unique", columnList = "medicineNumber", unique = true),
        @Index(name = "medicine_name_unique", columnList = "medicineName")
})
@DynamicUpdate
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
     * 阶乘因子
     */
    private String factorial;


    /**
     * 预订购日期
     */
    //private Date purchaseDate;

    /**
     * 是否预测
     */
    private Integer forecast;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 订货周期
     */
    private Integer period;
}
