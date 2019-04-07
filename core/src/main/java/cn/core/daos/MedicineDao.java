package cn.core.daos;

import cn.core.beans.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MedicineDao extends PagingAndSortingRepository<Medicine, Long> {

    Medicine findByMedicineName(String medicineName);

    @Query(value = "select t from Medicine t where t.medicineName like %?1%")
    Page<Medicine> findAllByKeyword(String keyword, Pageable pageable);
}
