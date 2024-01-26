package com.feirui.auth.infra.basic.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * (AuthRolePermission)表实体类
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRolePermission {
    
    private Long id;
    
    private Long roleId;
    
    private Long permissionId;
    //创建人
    private String createdBy;
    //创建时间
    private Date createdTime;
    //更新人
    private String updateBy;
    //更新时间
    private Date updateTime;
    
    private Integer isDeleted;
}

