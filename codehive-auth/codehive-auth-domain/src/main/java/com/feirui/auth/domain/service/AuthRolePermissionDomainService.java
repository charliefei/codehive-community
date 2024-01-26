package com.feirui.auth.domain.service;

import com.feirui.auth.common.enums.IsDeletedFlagEnum;
import com.feirui.auth.domain.bo.AuthRolePermissionBO;
import com.feirui.auth.infra.basic.entity.AuthRolePermission;
import com.feirui.auth.infra.basic.service.AuthRolePermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthRolePermissionDomainService {
    @Resource
    private AuthRolePermissionService authRolePermissionService;

    public Boolean add(AuthRolePermissionBO authRolePermissionBO) {
        Long roleId = authRolePermissionBO.getRoleId();
        List<AuthRolePermission> rolePermissionList = authRolePermissionBO.getPermissionIdList()
                .stream()
                .map(permissionId -> {
                    AuthRolePermission authRolePermission = new AuthRolePermission();
                    authRolePermission.setRoleId(roleId);
                    authRolePermission.setPermissionId(permissionId);
                    authRolePermission.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
                    return authRolePermission;
                })
                .collect(Collectors.toList());
        return authRolePermissionService.saveBatch(rolePermissionList);
    }
}
