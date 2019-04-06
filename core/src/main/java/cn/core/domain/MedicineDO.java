package cn.core.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
public class MedicineDO implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date updateTime;

    private String medicineName;

    private String stockUnit;

    private Double stock;

    private Double alarmValue;

    private Date usableTime;

    private Date purchaseDate;


}
