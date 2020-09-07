package com.tuling.seata.service;


import com.tuling.seata.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * <p>
 * 仓储服务 服务实现类
 * </p>
 *
 * @author lihaodong
 * @since 2019-11-25
 */
@Slf4j
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Transactional
    @Override
    public boolean reduceCount(Integer productId, Integer amount) throws Exception {
        log.info("商品Id:{},商品数量:{}",productId,amount);
        // 检查库存
        checkStock(productId, amount);
        log.info("开始扣减 {} 库存", productId);
        //Integer record = productMapper.reduceCount(productId,amount);
        //Integer record = productMapper.reduceCountByBatch(Arrays.asList(1,2));
        Integer record2 = productMapper.reduceCount(productId,amount);

        log.info("结束扣减 {} 库存结果:{}", productId, record2 > 0 ? "操作成功" : "扣减库存失败");
        return false;
    }







    private void checkStock(Integer productId, Integer requiredAmount) throws Exception {
        log.info("检查 {} 库存", productId);
        int countInDb = productMapper.selectCountById(productId);
        log.info("数据库库存:{}",countInDb);
        if (countInDb < requiredAmount) {
            log.warn("{} 库存不足，当前库存:{}", productId, countInDb);
            throw new Exception("库存不足");
        }
    }
}
