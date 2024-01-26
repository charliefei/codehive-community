package com.feirui.auth.domain.service;

import com.feirui.auth.common.enums.IsDeletedFlagEnum;
import com.feirui.auth.domain.bo.AuthRoleBO;
import com.feirui.auth.domain.convert.AuthRoleBOConverter;
import com.feirui.auth.infra.basic.entity.AuthRole;
import com.feirui.auth.infra.basic.service.AuthRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthRoleDomainService {
    @Resource
    private AuthRoleService authRoleService;

    public Boolean add(AuthRoleBO authRoleBO) {
        AuthRole authRole = AuthRoleBOConverter.INSTANCE.convert(authRoleBO);
        authRole.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        return authRoleService.save(authRole);
    }

    public Boolean update(AuthRoleBO authRoleBO) {
        AuthRole authRole = AuthRoleBOConverter.INSTANCE.convert(authRoleBO);
        return authRoleService.updateById(authRole);
    }

    public Boolean delete(AuthRoleBO authRoleBO) {
        AuthRole authRole = AuthRoleBOConverter.INSTANCE.convert(authRoleBO);
        authRole.setIsDeleted(IsDeletedFlagEnum.DELETED.getCode());
        return authRoleService.updateById(authRole);
    }
}
