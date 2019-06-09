package cn.core.daos;


import cn.core.beans.RoleDO;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 角色 DAO
 */
public interface RoleDao extends PagingAndSortingRepository<RoleDO, Long> {

}