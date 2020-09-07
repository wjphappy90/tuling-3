package com.tuling.config.role.service.impl;


import com.tuling.config.role.entity.SysPermission;
import com.tuling.config.role.entity.SysRolePermission;
import com.tuling.config.role.entity.SysUserRole;
import com.tuling.config.role.mapper.SysPermissionMapper;
import com.tuling.config.role.mapper.SysRolePermissionMapper;
import com.tuling.config.role.mapper.SysUserRoleMapper;
import com.tuling.config.role.service.ISysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smlz on 2019/12/20.
 */
@Component
@Slf4j
public class SysPermissionServiceImpl implements ISysPermissionService {

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public List<SysPermission> findByUserId(Integer userId) {

        //根据用户ID查询出 用户角色映射列表
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.findByUserId(userId);
        if (CollectionUtils.isEmpty(sysUserRoleList)) {
            log.warn("根据用户ID:{}查询 用户角色为空",userId);
            return null;
        }

        //迭代循环获取roleIds
        List<Integer> roleIds = new ArrayList<>();
        for(SysUserRole sysUserRole :sysUserRoleList) {
            roleIds.add(sysUserRole.getRoleId());
        }

        //查询角色 资源关联集合
        List<SysRolePermission> sysRolePermissionList = sysRolePermissionMapper.findByRoleIds(roleIds);
        if(CollectionUtils.isEmpty(sysRolePermissionList)) {
            log.warn("根据RoleIds:{}查询SysRolePermission集合为空",roleIds);
            return null;
        }

        //迭代permissionId 加入集合
        List<Integer> permissionIds = new ArrayList<>();
        for (SysRolePermission sysRolePermission:sysRolePermissionList) {
            permissionIds.add(sysRolePermission.getPermissionId());
        }

        //查询用户的所有资源
        List<SysPermission> sysPermissionList = sysPermissionMapper.findByIds(permissionIds);
        if(CollectionUtils.isEmpty(sysPermissionList)) {
            log.warn("根据permissionIds:{} 查询SysPermission为空",permissionIds);
        }
        return sysPermissionList;
    }
}
