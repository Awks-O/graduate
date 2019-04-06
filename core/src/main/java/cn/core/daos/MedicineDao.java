package cn.core.daos;

import cn.core.beans.Config;
import cn.core.domain.MedicineDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MedicineDao extends PagingAndSortingRepository<MedicineDO, Long> {

    MedicineDO findByName(String name);

    @Query(value = "select t from Config t where t.name like %?1% or t.value like %?1% or t.description like %?1%")
    Page<MedicineDO> findAllByKeyword(String keyword, Pageable pageable);
}
