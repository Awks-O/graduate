package cn.core.daos;

import cn.core.beans.InInfoDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InInfoDao extends PagingAndSortingRepository<InInfoDO, Long> {

    @Query(value = "select t from InInfoDO t order by t.inDate desc")
    Page<InInfoDO> findAll(Pageable pageable);

    @Query(value = "select t from InInfoDO t where t.supplier like %?1% order by t.inDate asc")
    Page<InInfoDO> findAllByKeyword(String keyword, Pageable pageable);

    @Query(value = "select t from InInfoDO t where t.medicineNumber=?1 order by t.inDate asc")
    Page<InInfoDO> findAllByKeyword1(String keyword, Pageable pageable);

}
