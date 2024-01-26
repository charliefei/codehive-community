package com.feirui.auth.domain.convert;

import com.feirui.auth.domain.bo.AuthPermissionBO;
import com.feirui.auth.infra.basic.entity.AuthPermission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 权限bo转换器
 */
@Mapper
public interface AuthPermissionBOConverter {

    AuthPermissionBOConverter INSTANCE = Mappers.getMapper(AuthPermissionBOConverter.class);

    AuthPermission convert(AuthPermissionBO authPermissionBO);

}
