package cn.core.services;

import cn.core.beans.InInfoDO;
import cn.core.daos.InInfoDao;
import cn.core.resp.PageResp;
import cn.core.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class InInfoService {

    @Autowired
    private InInfoDao inInfoDao;

    public PageResp<InInfoDO> listPage(Pageable pageable, String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return new PageResp<>(inInfoDao.findAll(pageable));
        } else {
            // asd也可以用spring JPA 的 Specification 来实现查找
            return new PageResp<>(inInfoDao.findAllByKeyword(keyword, pageable));
        }
    }

    public Result add(InInfoDO outInDetailDO) {
        inInfoDao.save(outInDetailDO);
        return Result.success();
    }

    public Result delete(Long id) {
        inInfoDao.deleteById(id);
        return Result.success();
    }
}
