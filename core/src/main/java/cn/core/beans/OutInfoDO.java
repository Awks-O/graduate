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
@Table(indexes = {
        @Index(name = "medicine_number_unique", columnList = "medicineNumber", unique = true),
        @Index(name = "medicine_name_unique", columnList = "medicineName")
})
@DynamicUpdate
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
