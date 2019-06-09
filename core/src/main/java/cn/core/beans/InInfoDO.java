package cn.core.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@DynamicUpdate
@Table(indexes = {
        @Index(name = "medicine_number_unique", columnList = "medicineNumber"),
        @Index(name = "medicine_name_unique", columnList = "medicineName")
})
public class InInfoDO extends BaseEntity {

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
     * 数量
     */
    private Integer amount;

    /**
     * 单价
     */
    private String unitPrice;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 保质期
     */
    private String expirationDate;

    /**
     * 入库日期
     */
    private Date inDate;

    /**
     * 生产日期
     */
    private Date productionDate;
}
