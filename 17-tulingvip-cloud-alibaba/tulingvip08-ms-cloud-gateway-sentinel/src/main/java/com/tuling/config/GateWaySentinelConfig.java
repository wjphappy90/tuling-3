package com.tuling.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.WebExceptionHandler;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 网关 +防卫兵配置
 * Created by smlz on 2019/12/17.
 */
@Configuration
public class GateWaySentinelConfig {


    @PostConstruct
    public void init() {
        System.out.println("==============================");
        //initCustomizedApis();
        //initGatewayRules();
    }

    /**
     * 方法实现说明:1.6.3版本之前 需要配置
     * @author:smlz
     * @return:
     * @exception:
     * @date:2019/12/18 21:56
     */
    private void initCustomizedApis() {
        //定义一个所有的api的集合
        Set<ApiDefinition> definitions = new HashSet<>();

        //定义api资源集合
        ApiDefinition someCustomizedApis = new ApiDefinition("some_customized_api");
        //添加批量路径
        Set<ApiPathPredicateItem> someApis = new HashSet<>();
        ApiPathPredicateItem apiPathPredicateItem = new ApiPathPredicateItem()
                                                        .setPattern("/product/selectProductInfoById/**")
                                                        .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX);
        someApis.add(apiPathPredicateItem);

        //定义api资源集合
        ApiDefinition anotherCustomizedApis= new ApiDefinition("another_customized_api");
        //添加批量路径
        Set<ApiPathPredicateItem> otherApis = new HashSet<>();
        ApiPathPredicateItem otherApi1 = new ApiPathPredicateItem()
                                            .setPattern("/product/testLimit")
                                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_EXACT);

        otherApis.add(otherApi1);


        definitions.add(someCustomizedApis);
        definitions.add(anotherCustomizedApis);
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

    /** 方法实现说明:1.6.3版本之前 需要配置
     *
     * resource：资源名称，可以是网关中的 route 名称或者用户自定义的 API 分组名称 比如(product_center,order_center)
     * ResourceMode:规则是针对 API Gateway 的 route（RESOURCE_MODE_ROUTE_ID）还是用户在 Sentinel 中定义的 API 分组（RESOURCE_MODE_CUSTOM_API_NAME），默认是 route
     * grade：限流指标维度，同限流规则的 grade 字段
     * count：限流阈值
     * intervalSec：统计时间窗口，单位是秒，默认是 1 秒
     * controlBehavior：流量整形的控制效果，同限流规则的 controlBehavior 字段，目前支持快速失败和匀速排队两种模式，默认是快速失败
     * burst：应对突发请求时额外允许的请求数目。
     * paramItem：参数限流配置。若不提供，则代表不针对参数进行限流，该网关规则将会被转换成普通流控规则；
     *   否则会转换成热点规则。其中的字段：
            parseStrategy：从请求中提取参数的策略，目前支持提取来源
                IP（PARAM_PARSE_STRATEGY_CLIENT_IP）、
                Host（PARAM_PARSE_STRATEGY_HOST）、
                任意 Header（PARAM_PARSE_STRATEGY_HEADER）
                任意 URL 参数（PARAM_PARSE_STRATEGY_URL_PARAM）四种模式。
        fieldName：若提取策略选择 Header 模式或 URL 参数模式，则需要指定对应的 header 名称或 URL 参数名称。
        pattern：参数值的匹配模式，只有匹配该模式的请求属性值会纳入统计和流控；若为空则统计该请求属性的所有值。（1.6.2 版本开始支持）
        matchStrategy：参数值的匹配策略，
            目前支持精确匹配（PARAM_MATCH_STRATEGY_EXACT）、
            子串匹配（PARAM_MATCH_STRATEGY_CONTAINS）和正则匹配（PARAM_MATCH_STRATEGY_REGEX）。（1.6.2 版本开始支持）
            用户可以通过 GatewayRuleManager.loadRules(rules) 手动加载网关规则，
            或通过 GatewayRuleManager.register2Property(property) 注册动态规则源动态推送（推荐方式）

     -Djava.net.preferIPv4Stack=true -Dcsp.sentinel.dashboard.server=localhost:8080 -Dcsp.sentinel.api.port=8666 -Dproject.name=MYAPP -Dcsp.sentinel.app.type=1
     说明：-Dcsp.sentinel.app.type=1 这个参数是Gateway接入Sentinel才需要配置，配置上之后dashboard会展示出为gateway特别定制的界面。而且这个功能在1.6.3版本才有，于2019.07合入的新功能。
     * @author:smlz
     * @return:
     * @exception:
     * @date:2019/12/17 16:52
     */
    private void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();

        rules.add(new GatewayFlowRule("product_center")
                .setCount(1)
                .setIntervalSec(1)

        );

        rules.add(new GatewayFlowRule("order_center")
                .setCount(1)
                .setIntervalSec(1)

        );


/*        rules.add(new GatewayFlowRule("some_customized_api")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                .setCount(1)
                .setIntervalSec(5)
                .setParamItem(new GatewayParamFlowItem()
                        .setParseStrategy(SentinelGatewayConstants.PARAM_PARSE_STRATEGY_URL_PARAM)
                        .setFieldName("pn")
                )
        );

        rules.add(new GatewayFlowRule("another_customized_api")
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
                .setCount(20)
                .setIntervalSec(10));*/



       GatewayRuleManager.loadRules(rules);
    }

    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0Iiwic2NvcGUiOlsicmVhZCJdLCJhZGRpdGlvbmFsSW5mbyI6eyJuaWNrTmFtZSI6IndpbmRpciIsImludGVncmF0aW9uIjo1MDAwLCJtZW1iZXJJZCI6MX0sImV4cCI6MTU4NjY3Njk2MiwiYXV0aG9yaXRpZXMiOlsiVEVTVCJdLCJqdGkiOiIzNjc0N2NhMS1lM2I0LTQzYjQtOGU3MS1iMjFiODc4YTlkNjciLCJjbGllbnRfaWQiOiJtZW1iZXItc2VydmljZSJ9.kYGvFXBlTYJIqNmVoDN4nHyUD9K5uEgQ1Evt8Ky6kYem56YFK10oKiCFtWQAgQjKnrk9fFtbYY5DhMcxaeIcrH2nc1_YGWKtsU0PTyBlK4APx-53Be1lrwmqwl7XhtZuHPXKYkofBGqcthcdEuqSN2QvNSygxBAwTS0YrTIa-5uI_8__X0HUlIvoaCngyd7Q6vdpSTRIj67DMXV_uBjkMOpZdB-YZg9ZgtOsjkL2jwJqtu7DRbOqO8aTkBXoa4JA38SN_6Pc6A6lYFTVKbveRqIqOQFGmI0tNAcayUKDKjO-rUd0EpdEOJonXrHNs4Aa0QKg1nKp0SPSn6oHQfKt5A");

        HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<>(null,httpHeaders);


        for (int i=0;i<100;i++) {
            Thread.sleep(500);
            restTemplate.exchange("http://localhost:8888/member/center/loadUmsMember?username=test", HttpMethod.GET,
                    httpEntity,Object.class);
        }
    }
}
