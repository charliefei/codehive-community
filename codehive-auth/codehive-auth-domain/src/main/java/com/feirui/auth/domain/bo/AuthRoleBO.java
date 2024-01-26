package com.feirui.auth.domain.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色bo
 */
@Data
public class AuthRoleBO implements Serializable {

    private Long id;
    
    private String roleName;
    
    private String roleKey;

}

