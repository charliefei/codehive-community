package com.feirui.auth.infra.basic.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (AuthPermission)表实体类
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthPermission {
    
    private Long id;
    
    private String name;
    
    private Long parentId;
    
    private Integer type;
    
    private String menuUrl;
    
    private Integer status;

    @TableField(value = "`show`")
    private Integer show;
    
    private String icon;
    
    private String permissionKey;
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

