package cn.core.services;

import cn.core.beans.OutInfoDO;
import cn.core.daos.OutInfoDao;
import cn.core.resp.PageResp;
import cn.core.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class OutInfoService {

    @Autowired
    private OutInfoDao detailDao;

    public PageResp<OutInfoDO> listPage(Pageable pageable, String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return new PageResp<>(detailDao.findAll(pageable));
        } else {
            // asd也可以用spring JPA 的 Specification 来实现查找
            return new PageResp<>(detailDao.findAllByKeyword(keyword, pageable));
        }
    }

    public Result add(OutInfoDO outInDetailDO) {
        detailDao.save(outInDetailDO);
        return Result.success();
    }

    public Result delete(Long id) {
        detailDao.deleteById(id);
        return Result.success();
    }
}
