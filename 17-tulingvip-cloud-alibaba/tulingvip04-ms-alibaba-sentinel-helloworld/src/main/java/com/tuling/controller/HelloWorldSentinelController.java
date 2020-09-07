package com.tuling.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.tuling.service.BusiServiceImpl;
import com.tuling.utils.BlockUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.management.relation.RoleUnresolved;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by smlz on 2020/2/13.
 */
@RestController
@Slf4j
public class HelloWorldSentinelController {

    @Autowired
    private BusiServiceImpl busiService;

    /**
     * 初始化流控规则
     */
    @PostConstruct
    public void init() {

        List<FlowRule> flowRules = new ArrayList<>();

        /**
         * 定义 helloSentinelV1 受保护的资源的规则
         */
        //创建流控规则对象
        FlowRule flowRule = new FlowRule();
        //设置流控规则 QPS
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //设置受保护的资源
        flowRule.setResource("helloSentinelV1");
        //设置受保护的资源的阈值
        flowRule.setCount(1);



        /**
         * 定义 helloSentinelV2 受保护的资源的规则
         */
        //创建流控规则对象
        FlowRule flowRule2 = new FlowRule();
        //设置流控规则 QPS
        flowRule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //设置受保护的资源
        flowRule2.setResource("helloSentinelV2");
        //设置受保护的资源的阈值
        flowRule2.setCount(1);


        /**
         * 定义 helloSentinelV3 受保护的资源的规则
         */
        //创建流控规则对象
        FlowRule flowRule3 = new FlowRule();
        //设置流控规则 QPS
        flowRule3.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //设置受保护的资源
        flowRule3.setResource("helloSentinelV3");
        //设置受保护的资源的阈值
        flowRule3.setCount(1);



        flowRules.add(flowRule);
        flowRules.add(flowRule2);
        flowRules.add(flowRule3);

        //加载配置好的规则
        FlowRuleManager.loadRules(flowRules);

        DegradeRule degradeRule = new DegradeRule();
        degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
        degradeRule.setCount(1);
        degradeRule.setTimeWindow(10);
        degradeRule.setResource("testRt");

        DegradeRuleManager.loadRules(Arrays.asList(degradeRule));
    }


    /**
     * 频繁请求接口 http://localhost:8080/helloSentinelV1
     * 这种做法的缺点:
     * 1)业务侵入性很大,需要在你的controoler中写入 非业务代码..
     * 2)配置不灵活 若需要添加新的受保护资源 需要手动添加 init方法来添加流控规则
     * @return
     */
    @RequestMapping("/helloSentinelV1")
    public String testHelloSentinelV1() {

        Entry entity =null;
        //关联受保护的资源
        try {
            entity = SphU.entry("helloSentinelV1");
            //开始执行 自己的业务方法
            busiService.doBusi();
            //结束执行自己的业务方法
        } catch (BlockException e) {
            log.info("testHelloSentinelV1方法被流控了");
            return "testHelloSentinelV1方法被流控了";
        }finally {
            if(entity!=null) {
                entity.exit();
            }
        }
        return "OK";
    }




    /**
     * 频繁请求接口 http://localhost:8080/helloSentinelV2
     * 优点: 需要配置aspectj的切面SentinelResourceAspect ,添加注解@SentinelResource
     *     解决了v1版本中 sentinel的业务侵入代码问题,通过blockHandler指定被流控后调用的方法.
     * 缺点: 若我们的controller中的方法逐步变多,那么受保护的方法也越来越多，会导致一个问题
     * blockHandler的方法也会越来越多   引起方法急剧膨胀 怎么解决
     *
     * 注意点:
     *   blockHandler 对应处理 BlockException 的函数名称，
     *   可选项。blockHandler 函数访问范围需要是 public，返回类型需要与原方法相匹配，
     *   参数类型需要和原方法相匹配并且最后加一个额外的参数，
     *   类型为 BlockException。blockHandler 函数默认需要和原方法在同一个类中
     * @return
     */
    @RequestMapping("/helloSentinelV2")
    @SentinelResource(value = "helloSentinelV2",blockHandler ="testHelloSentinelV2BlockMethod")
    public String testHelloSentinelV2() {
        busiService.doBusi();
        return "OK";
    }

    public String testHelloSentinelV2BlockMethod(BlockException e) {
        log.info("testRt流控");
        return "testRt降级 流控...."+e;
    }

    @RequestMapping("/testRt")
    @SentinelResource(value = "testRt",blockHandler ="testHelloSentinelV2BlockMethod"
    ,fallback ="testHelloSentinelV2FullBackMethod" )
    public String testRt() {
        throw new RuntimeException("异常");
    }

    public String testHelloSentinelV2FullBackMethod(Throwable e) {
        log.info("testRt降级");
        return "testRt降级...."+e;
    }















    /**
     * 我们看到了v2中的缺点,我们通过blockHandlerClass 来指定处理被流控的类
     * 通过testHelloSentinelV3BlockMethod 来指定blockHandlerClass 中的方法名称
     * ***这种方式 处理异常流控的方法必须要是static的
     * 频繁请求接口 http://localhost:8080/helloSentinelV3
     * @return
     */
    @RequestMapping("/helloSentinelV3")
    @SentinelResource(value = "helloSentinelV3",blockHandler = "testHelloSentinelV3BlockMethod",blockHandlerClass = BlockUtils.class)
    public String testHelloSentinelV3() {
        busiService.doBusi();
        return "OK";
    }

}
