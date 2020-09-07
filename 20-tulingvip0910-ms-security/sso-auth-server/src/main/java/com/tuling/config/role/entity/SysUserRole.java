package com.tuling.config.role.entity;

import lombok.Data;

import java.io.Serializable;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:用户角色关联表(一个用户可以有多个角色，一个角色可以分配给多个用户)
* @author: smlz
* @createDate: 2019/12/20 13:49
* @version: 1.0
*/
@Data
public class SysUserRole implements Serializable {
    private static final long serialVersionUID = -1810195806444298544L;

    private Integer id;

    private Integer userId;

    private Integer roleId;
}
