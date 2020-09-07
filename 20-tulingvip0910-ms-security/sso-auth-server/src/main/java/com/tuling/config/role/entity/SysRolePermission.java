package com.tuling.config.role.entity;

import lombok.Data;

import java.io.Serializable;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:角色资源映射表,一个角色可以访问多个资源,一个资源可以被多个角色访问
* @author: smlz
* @createDate: 2019/12/20 14:03
* @version: 1.0
*/
@Data
public class SysRolePermission implements Serializable {

    private static final long serialVersionUID = 7402412601579098788L;

    private Integer id;

    private Integer roleId;

    private Integer permissionId;
}
