package cn.core.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
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
