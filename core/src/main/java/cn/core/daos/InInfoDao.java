package cn.core.daos;

import cn.core.beans.InInfoDO;
import cn.core.beans.OutInfoDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InInfoDao extends PagingAndSortingRepository<InInfoDO, Long> {

    @Query(value = "select t from InInfoDO t order by inDate desc")
    Page<InInfoDO> findAll(Pageable pageable);

    @Query(value = "select t from InInfoDO t where t.medicineName like %?1% or t.medicineNumber like %?1% order by outInTime desc")
    Page<InInfoDO> findAllByKeyword(String keyword, Pageable pageable);
}
