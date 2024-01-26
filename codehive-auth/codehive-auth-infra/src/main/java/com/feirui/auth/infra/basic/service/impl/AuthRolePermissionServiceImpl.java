package com.feirui.auth.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.auth.infra.basic.dao.AuthRolePermissionDao;
import com.feirui.auth.infra.basic.entity.AuthRolePermission;
import com.feirui.auth.infra.basic.service.AuthRolePermissionService;
import org.springframework.stereotype.Service;

/**
 * (AuthRolePermission)表服务实现类
 */
@Service("authRolePermissionService")
public class AuthRolePermissionServiceImpl extends ServiceImpl<AuthRolePermissionDao, AuthRolePermission> implements AuthRolePermissionService {

}

