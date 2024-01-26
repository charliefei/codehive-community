package com.feirui.auth.infra.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feirui.auth.infra.basic.entity.AuthPermission;

/**
 * (AuthPermission)表服务接口
 */
public interface AuthPermissionService extends IService<AuthPermission> {

    int insert(AuthPermission authPermission);
}

