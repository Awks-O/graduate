package cn.core.daos;

import cn.core.beans.UserDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 用户DAO
 */
public interface UserDao extends PagingAndSortingRepository<UserDO, Long> {

    UserDO findByName(String username);

    @Query(value = "select t from UserDO t where t.name like %?1%", nativeQuery = false)
    Page<UserDO> findAllByKeyword(String keyword, Pageable pageable);
}
