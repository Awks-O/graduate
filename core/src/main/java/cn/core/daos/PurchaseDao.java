package cn.core.daos;

import cn.core.beans.PurchaseDO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface PurchaseDao extends CrudRepository<PurchaseDO, Long> {

    @Transactional
    @Modifying
    @Query(value = "update PurchaseDO t set t.amount=?2,t.purchaseDate=?3 where ?1=t.medicineNumber")
    void saveByMedicineNumber(String keyword, Integer keyword1, Date keyword2);

    @Query(value = "select t from PurchaseDO t where ?1=t.medicineNumber")
    PurchaseDO findByKeyword(String keyword);

    @Query(value = "select t from PurchaseDO t where ?1=t.supplier")
    List<PurchaseDO> findByKeyword1(String keyword);
}
