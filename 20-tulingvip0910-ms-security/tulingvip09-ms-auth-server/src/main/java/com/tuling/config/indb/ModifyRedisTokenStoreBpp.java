package com.tuling.config.indb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Component;

/**
 * Created by smlz on 2020/3/6.
 */
@Slf4j
/*@Component*/
public class ModifyRedisTokenStoreBpp implements BeanPostProcessor {

    @Autowired
    private JsonSerializationStrategy strategy;

    @Nullable
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof TokenStore) {
            log.info("beanName:{}",beanName);
            RedisTokenStore redisTokenStore = (RedisTokenStore) bean;
            redisTokenStore.setSerializationStrategy(strategy);
            return redisTokenStore;
        }
        return bean;
    }
}
