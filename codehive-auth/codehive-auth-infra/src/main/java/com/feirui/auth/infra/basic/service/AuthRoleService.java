package com.feirui.auth.infra.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feirui.auth.infra.basic.entity.AuthRole;

/**
 * (AuthRole)表服务接口
 */
public interface AuthRoleService extends IService<AuthRole> {

    AuthRole queryByRoleKey(String roleKey);
}

