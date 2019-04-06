package cn.core.common.daos;

import cn.core.common.rbac.Role;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 角色 DAO
 *
 * @author 肖文杰 https://github.com/xwjie/
 */
public interface RoleDao extends PagingAndSortingRepository<Role, Long> {

}