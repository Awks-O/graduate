package cn.core.services;

import cn.core.beans.Config;
import cn.core.common.beans.PageResp;
import cn.core.daos.MedicineDao;
import cn.core.domain.MedicineDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class MedicineService {

    @Resource
    private MedicineDao medicineDao;

    public MedicineDO findByName() {
        return medicineDao.findByName("*");
    }

    public PageResp<MedicineDO> listPage(Pageable pageable, String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return new PageResp<>(medicineDao.findAll(pageable));
        } else {
            // 也可以用spring JPA 的 Specification 来实现查找
            return new PageResp<>(medicineDao.findAllByKeyword(keyword, pageable));
        }
    }
}
