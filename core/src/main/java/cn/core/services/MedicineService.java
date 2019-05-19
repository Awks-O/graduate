package cn.core.services;

import cn.core.beans.MedicineDO;
import cn.core.daos.MedicineDao;
import cn.core.resp.PageResp;
import cn.core.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class MedicineService {

    @Autowired
    private MedicineDao medicineDao;

    public MedicineDO findByName() {
        return medicineDao.findByMedicineName("*");
    }

    public PageResp<MedicineDO> listPage(Pageable pageable, String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return new PageResp<>(medicineDao.findAll(pageable));
        } else {
            // asd也可以用spring JPA 的 Specification 来实现查找
            return new PageResp<>(medicineDao.findAllByKeyword(keyword, pageable));
        }
    }

    public Result add(MedicineDO medicineDO) {
        medicineDao.save(medicineDO);
        return Result.success();
    }

    public Result delete(Long id) {
        medicineDao.deleteById(id);
        return Result.success();
    }
}
