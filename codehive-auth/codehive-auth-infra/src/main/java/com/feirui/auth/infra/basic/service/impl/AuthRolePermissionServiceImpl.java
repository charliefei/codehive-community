package com.feirui.auth.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.auth.common.enums.IsDeletedFlagEnum;
import com.feirui.auth.infra.basic.dao.AuthRolePermissionDao;
import com.feirui.auth.infra.basic.entity.AuthRolePermission;
import com.feirui.auth.infra.basic.service.AuthRolePermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (AuthRolePermission)表服务实现类
 */
@Service("authRolePermissionService")
public class AuthRolePermissionServiceImpl extends ServiceImpl<AuthRolePermissionDao, AuthRolePermission> implements AuthRolePermissionService {

    @Override
    public List<AuthRolePermission> queryByRoleId(Long roleId) {
        return lambdaQuery()
                .eq(AuthRolePermission::getRoleId, roleId)
                .eq(AuthRolePermission::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getCode())
                .list();
    }
}

