package com.tuling.compent.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * Created by smlz on 2019/12/16.
 */
@Slf4j
@Component
public class TimeMonitorGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {

    private static final String COUNT_START_TIME = "countStartTime";


    @Override
    public GatewayFilter apply(NameValueConfig config) {

/*        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

                String name = config.getName();
                String value = config.getValue();
                log.info("name:{},value:{}",name,value);
                if(value.equals("false")) {
                    return chain.filter(exchange);
                }
                exchange.getAttributes().put(COUNT_START_TIME, System.currentTimeMillis());

                //then方法相当于aop的后置通知一样
                return chain.filter(exchange).then(Mono.fromRunnable(new Runnable() {
                    @Override
                    public void run() {
                        Long startTime = exchange.getAttribute(COUNT_START_TIME);
                        if (startTime != null) {
                            StringBuilder sb = new StringBuilder(exchange.getRequest().getURI().getRawPath())
                                    .append(": ")
                                    .append(System.currentTimeMillis() - startTime)
                                    .append("ms");
                            sb.append(" params:").append(exchange.getRequest().getQueryParams());
                            log.info(sb.toString());
                        }
                    }
                }));
            }
        };*/
        return new TimeMonitorGatewayFilter(config);
    }

    /**
     * 我们自己写一个静态内部类 实现GatewayFilter,Ordered  通过Orderd可以实现顺序的控制
     */
    public static class TimeMonitorGatewayFilter implements GatewayFilter,Ordered{

        private NameValueConfig nameValueConfig;

        public TimeMonitorGatewayFilter(NameValueConfig nameValueConfig) {
            this.nameValueConfig = nameValueConfig;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            String name = nameValueConfig.getName();
            String value = nameValueConfig.getValue();
            log.info("name:{},value:{}",name,value);
            if(value.equals("false")) {
                return chain.filter(exchange);
            }
            exchange.getAttributes().put(COUNT_START_TIME, System.currentTimeMillis());

            //then方法相当于aop的后置通知一样
            return chain.filter(exchange).then(Mono.fromRunnable(new Runnable() {
                @Override
                public void run() {
                    Long startTime = exchange.getAttribute(COUNT_START_TIME);
                    if (startTime != null) {
                        StringBuilder sb = new StringBuilder(exchange.getRequest().getURI().getRawPath())
                                .append(": ")
                                .append(System.currentTimeMillis() - startTime)
                                .append("ms");
                        sb.append(" params:").append(exchange.getRequest().getQueryParams());
                        log.info(sb.toString());
                    }
                }
            }));
        }

        @Override
        public int getOrder() {
            return -100;
        }
    }
}
