package cn.core.beans;

import cn.core.common.rbac.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class Config extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name, description, value;

    /**
     * 创建者
     */
    @CreatedBy
    @ManyToOne
    private User creator;
}
