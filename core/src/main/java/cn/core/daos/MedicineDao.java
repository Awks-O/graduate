package cn.core.daos;

import cn.core.beans.MedicineDO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Date;

public interface MedicineDao extends CrudRepository<MedicineDO, Long> {

    @Transactional
    @Modifying
    @Query(value = "update MedicineDO t set t.stock=?2,t.updateTime=?3 where ?1=t.medicineNumber")
    void saveByKeyword(String keyword, Double amount, Date date);

    @Query(value = "select t from MedicineDO t where t.medicineNumber like %?1%")
    MedicineDO findByKeyword(String keyword);
}
