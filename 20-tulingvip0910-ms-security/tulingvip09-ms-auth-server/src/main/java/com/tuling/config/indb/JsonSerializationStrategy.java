package com.tuling.config.indb;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * json序列化器
 * Created by smlz on 2020/3/6.
 */
@Component
public class JsonSerializationStrategy extends StandardStringSerializationStrategy {

    private GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer= new GenericJackson2JsonRedisSerializer();
    @Override
    protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(bytes,clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //return jackson2JsonRedisSerializer.deserialize(bytes,clazz);
    }

    @Override
    protected byte[] serializeInternal(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //return jackson2JsonRedisSerializer.serialize(object);
    }
}
