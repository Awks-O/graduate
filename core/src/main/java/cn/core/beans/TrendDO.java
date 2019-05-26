package cn.core.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Date;

/**
 * 药品消耗趋势
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TrendDO extends BaseEntity{

    /**
     * 统计日期
     */
    private Date statisticalDate;

    /**
     * 消耗量
     */
    private Integer consumption;

    /**
     * 药品编号
     */
    private String medicineNumber;

    /**
     * 药品名称
     */
    private String medicineName;

    /**
     * 供应商
     */
    private String supplier;

}
