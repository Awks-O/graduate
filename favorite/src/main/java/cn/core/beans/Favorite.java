package cn.core.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;

/**
 * 收藏类
 *
 * @author 晓风轻
 * <p>
 * （用户id+业务对象类型+业务对象id）唯一索引为了防止并发操作数据错误
 */
@Entity
@Data
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"userId", "objType", "objId"})
)
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class Favorite extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private long userId;

    @Min(1)
    private int objType;

    @Min(1)
    private long objId;

}
