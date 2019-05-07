package cn.core.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * 基类
 *
 *
 */
@Data
@MappedSuperclass
@EqualsAndHashCode(of = "id")
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date updateTime;

}
