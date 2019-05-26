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
public class OutInfoDO extends BaseEntity {

    /**
     * 药品编号
     */
    private String medicineNumber;

    /**
     * 药品名称
     */
    private String medicineName;

    /**
     * 退货单价
     */
    private String unitPrice;

    /**
     * 存储单位
     */
    private String stockUnit;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 出庫時間
     */
    private Date outDate;
}
