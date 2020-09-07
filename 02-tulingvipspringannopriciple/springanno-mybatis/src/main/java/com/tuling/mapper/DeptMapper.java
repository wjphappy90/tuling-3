package com.tuling.mapper;


import com.tuling.entity.Dept;

import java.util.List;

/**
 * Created by smlz on 2019/3/22.
 */

public interface DeptMapper {


    Dept findOne(Integer id);

    List<Dept> list();

    int save(Dept dept);

}
