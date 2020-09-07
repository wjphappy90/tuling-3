package com.tuling.v1.controller;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 用代码添加流控规则
 * Created by smlz on 2019/11/27.
 */
@RestController
public class AddFlowLimitController {

    @RequestMapping("/addFlowLimit")
    public String addFlowLimit() {
        List<FlowRule> flowRuleList = new ArrayList<>();

        FlowRule flowRule = new FlowRule("/testAddFlowLimitRule");

        //设置QPS阈值
        flowRule.setCount(1);

        //设置流控模型为QPS模型
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);

        flowRuleList.add(flowRule);

        FlowRuleManager.loadRules(flowRuleList);

        return "success";

    }

    @RequestMapping("/testAddFlowLimitRule")
    public String testAddFlowLimitRule() {
        return "testAddFlowLimitRule";
    }
}
