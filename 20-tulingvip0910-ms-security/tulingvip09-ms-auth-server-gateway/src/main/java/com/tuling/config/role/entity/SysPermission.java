package com.tuling.config.role.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:资源表
* @author: smlz
* @createDate: 2019/12/20 14:02
* @version: 1.0
*/
@Data
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 4285835478693487481L;

    private Integer id;

    private Integer pid;

    private Integer type;

    private String name;

    private String code;

    private String uri;

    private Integer seq = 1;

    private String createUser;

    private Date createTime;

    private String updateUser;

    private Date updateTime;

}
