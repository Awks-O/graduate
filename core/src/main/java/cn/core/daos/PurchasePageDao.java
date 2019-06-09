package cn.core.daos;

import cn.core.beans.PurchaseDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PurchasePageDao extends PagingAndSortingRepository<PurchaseDO, Long> {

    @Query(value = "select t from PurchaseDO t order by t.purchaseDate asc")
    Page<PurchaseDO> findAll(Pageable pageable);

    @Query(value = "select t from PurchaseDO t where t.medicineName like %?1% or t.medicineNumber like %?1% or t.supplier like %?1% order by t.purchaseDate asc")
    Page<PurchaseDO> findAllByKeyword(String keyword, Pageable pageable);
}
