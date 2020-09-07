package com.tuling.persistence;

import com.alibaba.csp.sentinel.command.handler.ModifyParamFlowRulesCommandHandler;
import com.alibaba.csp.sentinel.datasource.*;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by smlz on 2019/11/28.
 */
@Slf4j
public class PullModeByFileDataSource implements InitFunc {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    public void init() throws Exception {
        log.info("time:{}读取配置",sdf.format(new Date()));

        try {

            //创建文件存储目录
            RuleFileUtils.mkdirIfNotExits(PersistenceRuleConstant.storePath);

            //创建规则文件
            RuleFileUtils.createFileIfNotExits(PersistenceRuleConstant.rulesMap);

            dealFlowRules();

            dealDegradeRules();

            dealSystemRules();

            dealParamFlowRules();

            dealAuthRules();
        }catch (Exception e) {
            log.error("错误原因:{}",e);
        }

    }

    /**
     * 方法实现说明:处理流控规则逻辑
     * @author:smlz
     * @return: void
     * @exception: FileNotFoundException
     * @date:2019/11/29 13:26
     */
    private void dealFlowRules() throws FileNotFoundException {
        String ruleFilePath = PersistenceRuleConstant.rulesMap.get(PersistenceRuleConstant.FLOW_RULE_PATH).toString();

        //创建流控规则的可读数据源

/*        ReadableDataSource<String, List<FlowRule>> flowRuleRDS = new FileRefreshableDataSource(
                ruleFilePath,RuleListParserUtils.flowRuleListParser
        );*/

        ReadableDataSource<String, List<FlowRule>> flowRuleRDS = new FileRefreshableDataSource(
                new File(ruleFilePath),RuleListParserUtils.flowRuleListParser,20000,1024 * 1024, Charset.forName("utf-8")
        );

        // 将可读数据源注册至FlowRuleManager 这样当规则文件发生变化时，就会更新规则到内存
        FlowRuleManager.register2Property(flowRuleRDS.getProperty());


        WritableDataSource<List<FlowRule>> flowRuleWDS = new FileWritableDataSource<List<FlowRule>>(
                ruleFilePath, RuleListParserUtils.flowFuleEnCoding
        );

        // 将可写数据源注册至 transport 模块的 WritableDataSourceRegistry 中.
        // 这样收到控制台推送的规则时，Sentinel 会先更新到内存，然后将规则写入到文件中.
        WritableDataSourceRegistry.registerFlowDataSource(flowRuleWDS);
    }

    /**
     * 方法实现说明:处理降级规则
     * @author:smlz
     * @return:void
     * @exception: FileNotFoundException
     * @date:2019/11/29 13:42
     */
    private void dealDegradeRules() throws FileNotFoundException {
        //讲解规则文件路径
        String degradeRuleFilePath = PersistenceRuleConstant.rulesMap.get(PersistenceRuleConstant.DEGRAGE_RULE_PATH).toString();

        //创建流控规则的可读数据源
        ReadableDataSource<String, List<DegradeRule>> degradeRuleRDS = new FileRefreshableDataSource(
                degradeRuleFilePath,RuleListParserUtils.degradeRuleListParse
        );

        // 将可读数据源注册至FlowRuleManager 这样当规则文件发生变化时，就会更新规则到内存
        DegradeRuleManager.register2Property(degradeRuleRDS.getProperty());


        WritableDataSource<List<DegradeRule>> degradeRuleWDS = new FileWritableDataSource<>(
                degradeRuleFilePath, RuleListParserUtils.degradeRuleEnCoding
        );

        // 将可写数据源注册至 transport 模块的 WritableDataSourceRegistry 中.
        // 这样收到控制台推送的规则时，Sentinel 会先更新到内存，然后将规则写入到文件中.
        WritableDataSourceRegistry.registerDegradeDataSource(degradeRuleWDS);
    }

    /**
     * 方法实现说明:处理系统规则
     * @author:smlz
     * @return:void
     * @exception: FileNotFoundException
     * @date:2019/11/29 13:42
     */
    private void dealSystemRules() throws FileNotFoundException {
        //讲解规则文件路径
        String systemRuleFilePath = PersistenceRuleConstant.rulesMap.get(PersistenceRuleConstant.SYSTEM_RULE_PATH).toString();

        //创建流控规则的可读数据源
        ReadableDataSource<String, List<SystemRule>> systemRuleRDS = new FileRefreshableDataSource(
                systemRuleFilePath,RuleListParserUtils.sysRuleListParse
        );

        // 将可读数据源注册至FlowRuleManager 这样当规则文件发生变化时，就会更新规则到内存
        SystemRuleManager.register2Property(systemRuleRDS.getProperty());


        WritableDataSource<List<SystemRule>> systemRuleWDS = new FileWritableDataSource<>(
                systemRuleFilePath, RuleListParserUtils.sysRuleEnCoding
        );

        // 将可写数据源注册至 transport 模块的 WritableDataSourceRegistry 中.
        // 这样收到控制台推送的规则时，Sentinel 会先更新到内存，然后将规则写入到文件中.
        WritableDataSourceRegistry.registerSystemDataSource(systemRuleWDS);
    }

    /**
     * 方法实现说明:热点参数规则
     * @author:smlz
     * @return:
     * @exception:
     * @date:2019/11/29 13:50
     */
    private void dealParamFlowRules() throws FileNotFoundException {
        //讲解规则文件路径
        String paramFlowRuleFilePath = PersistenceRuleConstant.rulesMap.get(PersistenceRuleConstant.HOT_PARAM_RULE).toString();

        //创建流控规则的可读数据源
        ReadableDataSource<String, List<ParamFlowRule>> paramFlowRuleRDS = new FileRefreshableDataSource(
                paramFlowRuleFilePath,RuleListParserUtils.paramFlowRuleListParse
        );

        // 将可读数据源注册至FlowRuleManager 这样当规则文件发生变化时，就会更新规则到内存
        ParamFlowRuleManager.register2Property(paramFlowRuleRDS.getProperty());


        WritableDataSource<List<ParamFlowRule>> paramFlowRuleWDS = new FileWritableDataSource<>(
                paramFlowRuleFilePath, RuleListParserUtils.paramRuleEnCoding
        );

        // 将可写数据源注册至 transport 模块的 WritableDataSourceRegistry 中.
        // 这样收到控制台推送的规则时，Sentinel 会先更新到内存，然后将规则写入到文件中.
        ModifyParamFlowRulesCommandHandler.setWritableDataSource(paramFlowRuleWDS);
    }

    /**
     * 方法实现说明:授权规则
     * @author:smlz
     * @return:
     * @exception:
     * @date:2019/11/29 13:56
     */
    private void dealAuthRules() throws FileNotFoundException {
        //讲解规则文件路径
        String authFilePath = PersistenceRuleConstant.rulesMap.get(PersistenceRuleConstant.AUTH_RULE_PATH).toString();

        //创建流控规则的可读数据源
        ReadableDataSource<String, List<AuthorityRule>> authRuleRDS = new FileRefreshableDataSource(
                authFilePath,RuleListParserUtils.authorityRuleParse
        );

        // 将可读数据源注册至FlowRuleManager 这样当规则文件发生变化时，就会更新规则到内存
        AuthorityRuleManager.register2Property(authRuleRDS.getProperty());


        WritableDataSource<List<AuthorityRule>> authRuleWDS = new FileWritableDataSource<>(
                authFilePath, RuleListParserUtils.authorityEncoding
        );

        // 将可写数据源注册至 transport 模块的 WritableDataSourceRegistry 中.
        // 这样收到控制台推送的规则时，Sentinel 会先更新到内存，然后将规则写入到文件中.
        WritableDataSourceRegistry.registerAuthorityDataSource(authRuleWDS);
    }
}
