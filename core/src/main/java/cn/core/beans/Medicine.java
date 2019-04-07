package cn.core.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class Medicine extends BaseEntity {

    private String medicineNumber;

    private String medicineName;

    private String stockUnit;

    private Double stock;

    private Double alarmValue;

    private Date usableTime;

    private Date purchaseDate;

}
