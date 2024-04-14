package com.feirui.auth.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.auth.common.enums.IsDeletedFlagEnum;
import com.feirui.auth.infra.basic.dao.AuthPermissionDao;
import com.feirui.auth.infra.basic.entity.AuthPermission;
import com.feirui.auth.infra.basic.service.AuthPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (AuthPermission)表服务实现类
 */
@Service("authPermissionService")
public class AuthPermissionServiceImpl extends ServiceImpl<AuthPermissionDao, AuthPermission> implements AuthPermissionService {
    @Resource
    private AuthPermissionDao authPermissionDao;

    @Override
    public int insert(AuthPermission authPermission) {
        return authPermissionDao.insert(authPermission);
    }

    @Override
    public List<AuthPermission> queryByIds(List<Long> permissionIds) {
        return lambdaQuery()
                .eq(AuthPermission::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getCode())
                .in(AuthPermission::getId, permissionIds)
                .list();
    }
}

