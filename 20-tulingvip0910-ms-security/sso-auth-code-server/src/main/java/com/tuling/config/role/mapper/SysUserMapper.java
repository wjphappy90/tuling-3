package com.tuling.config.role.mapper;


import com.tuling.config.role.entity.SysUser;

/**
 * Created by smlz on 2019/12/20.
 */
public interface SysUserMapper {

    SysUser findByUserName(String userName);
}
