package com.feirui.auth.application.convert;

import com.feirui.auth.application.dto.AuthRolePermissionDTO;
import com.feirui.auth.domain.bo.AuthRolePermissionBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 权限dto转换器
 */
@Mapper
public interface AuthRolePermissionDTOConverter {

    AuthRolePermissionDTOConverter INSTANCE = Mappers.getMapper(AuthRolePermissionDTOConverter.class);

    AuthRolePermissionBO convert(AuthRolePermissionDTO authRolePermissionDTO);

}
