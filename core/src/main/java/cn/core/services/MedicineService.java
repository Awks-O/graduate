package cn.core.services;

import cn.core.common.beans.PageResp;
import cn.core.daos.MedicineDao;
import cn.core.beans.Medicine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class MedicineService {

    @Autowired
    MedicineDao medicineDao;

    public Medicine findByName() {
        return medicineDao.findByMedicineName("*");
    }

    public PageResp<Medicine> listPage(Pageable pageable, String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return new PageResp<>(medicineDao.findAll(pageable));
        } else {
            // 也可以用spring JPA 的 Specification 来实现查找
            return new PageResp<>(medicineDao.findAllByKeyword(keyword, pageable));
        }
    }
}
