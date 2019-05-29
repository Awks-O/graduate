package cn.core.daos;

import cn.core.beans.PurchaseDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

public interface PurchaseDao extends PagingAndSortingRepository<PurchaseDO, Long> {

    @Query(value = "select t from PurchaseDO t where ?1>=t.purchaseDate")
    Page<PurchaseDO> findAll(Date date, Pageable pageable);

    @Query(value = "select t from PurchaseDO t where ?1>=t.purchaseDate and t.medicineName like %?2% or t.medicineNumber like %?2%")
    Page<PurchaseDO> findAllByKeyword(Date date, String keyword, Pageable pageable);

    @Query(value = "update PurchaseDO t set t.amount=?2,t.purchaseDate=?3 where ?1=t.medicineNumber")
    Page saveByMedicineNumber(String keyword, Double keyword1, Date keyword2, Pageable pageable);
}
