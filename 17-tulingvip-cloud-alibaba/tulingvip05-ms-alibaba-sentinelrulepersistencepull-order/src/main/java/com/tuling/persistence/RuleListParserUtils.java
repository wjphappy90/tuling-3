package com.tuling.persistence;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.lang.Nullable;

import java.util.List;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:规则列表解析工具类
* @author: smlz
* @createDate: 2019/11/28 16:55
* @version: 1.0
*/
public class RuleListParserUtils {


    /**
     * 流控列表解析器
     */
    public static final Converter<String, List<FlowRule>> flowRuleListParser = new Converter<String, List<FlowRule>>() {
        @Nullable
        @Override
        public List<FlowRule> convert(String source) {
            return JSON.parseObject(source, new TypeReference<List<FlowRule>>() {});
        }
    };

    public static final Converter<String,List<DegradeRule>> degradeRuleListParse = new Converter<String, List<DegradeRule>>() {
        @Nullable
        @Override
        public List<DegradeRule> convert(String source) {
            return JSON.parseObject(source,new TypeReference<List<DegradeRule>>(){});
        }
    };

    public static final Converter<String,List<SystemRule>> sysRuleListParse = new Converter<String, List<SystemRule>>() {
        @Nullable
        @Override
        public List<SystemRule> convert(String source) {
            return JSON.parseObject(source,new TypeReference<List<SystemRule>>(){});
        }
    };

    public static final Converter<String,List<ParamFlowRule>> paramFlowRuleListParse = new Converter<String, List<ParamFlowRule>>() {
        @Nullable
        @Override
        public List<ParamFlowRule> convert(String source) {
            return JSON.parseObject(source,new TypeReference<List<ParamFlowRule>>(){});
        }
    };

    public static final Converter<String,List<AuthorityRule>> authorityRuleParse = new Converter<String, List<AuthorityRule>>() {
        @Nullable
        @Override
        public List<AuthorityRule> convert(String source) {
            return JSON.parseObject(source,new TypeReference<List<AuthorityRule>>(){});
        }
    };




    public static final Converter<List<FlowRule>,String>  flowFuleEnCoding= new Converter<List<FlowRule>,String>() {

        @Nullable
        @Override
        public String convert(List<FlowRule> source) {
            return JSON.toJSONString(source);
        }
    };


    public static final Converter<List<SystemRule>,String>  sysRuleEnCoding= new Converter<List<SystemRule>,String>() {

        @Nullable
        @Override
        public String convert(List<SystemRule> source) {
            return JSON.toJSONString(source);
        }
    };

    public static final Converter<List<DegradeRule>,String>  degradeRuleEnCoding= new Converter<List<DegradeRule>,String>() {

        @Nullable
        @Override
        public String convert(List<DegradeRule> source) {
            return JSON.toJSONString(source);
        }
    };

    public static final Converter<List<ParamFlowRule>,String>  paramRuleEnCoding= new Converter<List<ParamFlowRule>,String>() {

        @Nullable
        @Override
        public String convert(List<ParamFlowRule> source) {
            return JSON.toJSONString(source);
        }
    };

    public static final Converter<List<AuthorityRule>,String>  authorityEncoding= new Converter<List<AuthorityRule>,String>() {

        @Nullable
        @Override
        public String convert(List<AuthorityRule> source) {
            return JSON.toJSONString(source);
        }
    };
}
