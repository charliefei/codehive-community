package com.feirui.auth.infra.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feirui.auth.infra.basic.entity.AuthRolePermission;

import java.util.List;

/**
 * (AuthRolePermission)表服务接口
 */
public interface AuthRolePermissionService extends IService<AuthRolePermission> {

    List<AuthRolePermission> queryByRoleId(Long roleId);
}

