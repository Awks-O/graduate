package cn.core.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 药品消耗趋势
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TrendDO extends BaseEntity {

    /**
     * 时间，X轴
     */
    private List<List<Map<String, String>>> dataList;

}
