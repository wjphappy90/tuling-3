package com.tuling.config.role.mapper;



import com.tuling.config.role.entity.SysPermission;

import java.util.List;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:资源mapper
* @author: smlz
* @createDate: 2019/12/20 15:28
* @version: 1.0
*/
public interface SysPermissionMapper  {

    /**
    * @vlog: 高于生活，源于生活
    * @desc: 类的描述:根据资源id集合查询资源列表
    * @author: smlz
    * @createDate: 2019/12/20 15:28
    * @version: 1.0
    */
    List<SysPermission> findByIds(List<Integer> ids);

}
