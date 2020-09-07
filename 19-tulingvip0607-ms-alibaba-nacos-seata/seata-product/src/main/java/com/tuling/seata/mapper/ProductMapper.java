package com.tuling.seata.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by smlz on 2019/12/9.
 */
public interface ProductMapper {

    Integer reduceCount(@Param("productId") Integer productId, @Param("amount") Integer amount);

    Integer reduceCountByBatch(List<Integer> productIds);

    Integer selectCountById(@Param("productId") Integer productId);
}
