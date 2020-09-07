package com.tuling.seata.controller;

import com.tuling.seata.service.IProductService;
import com.tuling.seata.vo.ResultVo;
import io.seata.spring.annotation.GlobalLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 仓储服务 前端控制器
 * </p>
 *
 * @author lihaodong
 * @since 2019-11-25
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    /**
     * 扣减库存
     */
    @RequestMapping("/reduceCount")
    public ResultVo decrease(@RequestParam Integer count, @RequestParam Integer productId) throws Exception {
        boolean reductFlag = productService.reduceCount(count,productId);

        ResultVo resultVo = new ResultVo();
        resultVo.setSuccess(reductFlag);
        resultVo.setMsg("扣除库存成功");
        resultVo.setData("扣除库存成功");

        return resultVo;
    }


}

