package cn.core.req;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

/**
 * 分页请求参数
 */
@Data
@JSONType(ignores = "pageable") // 不加fastjson toJson的时候 报 StackOverflowError
public class PageReq {

    private int page = 1;

    private int pageSize = 10;

    private String sortField = "";

    private String sort = "";

    private String keyword = "";

    public PageReq() {
        super();
    }

    private PageReq(int page, int pageSize, String sortField, String sort,
                    String keyword) {
        super();
        this.page = page;
        this.pageSize = pageSize;
        this.sortField = sortField;
        this.sort = sort;
        this.keyword = keyword;
    }

    public PageReq getPageable() {
        return new PageReq(page, pageSize, sortField, sort, keyword);
    }

    public Pageable toPageable() {
        // pageable里面是从第0页开始的。
        Pageable pageable;

        if (StringUtils.isEmpty(sortField)) {
            pageable = PageRequest.of(page - 1, pageSize);
        } else {
            pageable = PageRequest.of(page - 1, pageSize,
                    sort.toLowerCase().startsWith("desc") ? Direction.DESC
                            : Direction.ASC,
                    sortField);
        }

        return pageable;
    }
}
