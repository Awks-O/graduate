package cn.core.daos;

import cn.core.beans.MedicineDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MedicineDao extends PagingAndSortingRepository<MedicineDO, Long> {

    MedicineDO findByMedicineName(String medicineName);

    @Query(value = "select t from MedicineDO t where t.medicineName like %?1% or t.medicineNumber like %?1%")
    Page<MedicineDO> findAllByKeyword(String keyword, Pageable pageable);
}
