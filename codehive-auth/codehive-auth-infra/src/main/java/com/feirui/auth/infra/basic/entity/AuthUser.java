package com.feirui.auth.infra.basic.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * (AuthUser)表实体类
 *
 * @author makejava
 * @since 2024-01-24 15:23:43
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {
    
    private Long id;
    
    private String userName;
    
    private String nickName;
    
    private String email;
    
    private String phone;
    
    private String password;
    
    private Integer sex;
    
    private String avatar;
    
    private Integer status;
    
    private String introduce;
    
    private String extJson;
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

