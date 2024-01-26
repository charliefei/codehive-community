package com.feirui.auth.application.convert;

import com.feirui.auth.application.dto.AuthRoleDTO;
import com.feirui.auth.domain.bo.AuthRoleBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 角色dto转换器
 */
@Mapper
public interface AuthRoleDTOConverter {

    AuthRoleDTOConverter INSTANCE = Mappers.getMapper(AuthRoleDTOConverter.class);

    AuthRoleBO convert(AuthRoleDTO authRoleDTO);

}
