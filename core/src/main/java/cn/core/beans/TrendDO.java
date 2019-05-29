package cn.core.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Date;
import java.util.List;

/**
 * 药品消耗趋势
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TrendDO extends BaseEntity{

    /**
     * 时间，X轴
     */
    private List<Date> dateList;

    /**
     * 消耗量，Y轴
     */
    private List<Integer> amountList;

}
