package com.feirui.auth.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.auth.common.enums.IsDeletedFlagEnum;
import com.feirui.auth.infra.basic.dao.AuthRoleDao;
import com.feirui.auth.infra.basic.entity.AuthRole;
import com.feirui.auth.infra.basic.service.AuthRoleService;
import org.springframework.stereotype.Service;

/**
 * (AuthRole)表服务实现类
 */
@Service("authRoleService")
public class AuthRoleServiceImpl extends ServiceImpl<AuthRoleDao, AuthRole> implements AuthRoleService {

    @Override
    public AuthRole queryByRoleKey(String roleKey) {
        return lambdaQuery()
                .eq(AuthRole::getRoleKey, roleKey)
                .eq(AuthRole::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getCode())
                .one();
    }
}

