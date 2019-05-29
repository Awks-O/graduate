package cn.core.daos;

import cn.core.beans.OutInfoDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OutInfoDao extends PagingAndSortingRepository<OutInfoDO, Long> {

    @Query(value = "select t from OutInfoDO t order by outDate desc")
    Page<OutInfoDO> findAll(Pageable pageable);

    @Query(value = "select t from OutInfoDO t where t.medicineName like %?1% or t.medicineNumber like %?1% order by outDate desc")
    Page<OutInfoDO> findAllByKeyword(String keyword, Pageable pageable);
}
