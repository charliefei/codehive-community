package com.feirui.auth.domain.convert;

import com.feirui.auth.domain.bo.AuthRoleBO;
import com.feirui.auth.infra.basic.entity.AuthRole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 角色bo转换器
 */
@Mapper
public interface AuthRoleBOConverter {

    AuthRoleBOConverter INSTANCE = Mappers.getMapper(AuthRoleBOConverter.class);

    AuthRole convert(AuthRoleBO authRoleBO);

}
