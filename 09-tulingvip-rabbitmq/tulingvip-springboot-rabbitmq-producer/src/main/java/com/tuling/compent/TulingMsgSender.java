package com.tuling.compent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuling.entity.Order;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 消息发送组件
 * Created by smlz on 2019/10/9.
 */
@Component
public class TulingMsgSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试发送我们的消息
     * @param msg 消息内容
     * @param msgProp 消息属性
     */
    public void sendMsg(Object msg,Map<String,Object> msgProp) {

        MessageHeaders messageHeaders = new MessageHeaders(msgProp);

        //构建消息对象
        Message message = MessageBuilder.createMessage(msg,messageHeaders);


        //构建correlationData 用于做可靠性投递得,ID:必须为全局唯一的 根据业务规则
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //开启确认模式
        rabbitTemplate.setConfirmCallback(new TulingConfirmCallBack());

        //开启消息可达监听
        rabbitTemplate.setReturnCallback(new TulingRetrunCallBack());

        rabbitTemplate.convertAndSend("springboot.direct.exchange","springboot.key",message,correlationData);

        rabbitTemplate.convertAndSend("springboot.direct.exchange","springboot.key2",message,correlationData);

    }

    public void sendOrderMsg(Order order) throws Exception {

        //构建correlationData 用于做可靠性投递得,ID:必须为全局唯一的 根据业务规则
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //开启确认模式
        rabbitTemplate.setConfirmCallback(new TulingConfirmCallBack());

        //开启消息可达监听
        rabbitTemplate.setReturnCallback(new TulingRetrunCallBack());





        /*
            发送core下的message对象*/
        ObjectMapper objectMapper = new ObjectMapper();
        String orderJson = objectMapper.writeValueAsString(order);
        org.springframework.amqp.core.MessageProperties messageProperties = new MessageProperties();
        org.springframework.amqp.core.Message message = new org.springframework.amqp.core.Message(orderJson.getBytes(),messageProperties);
        rabbitTemplate.convertAndSend("springboot.direct.exchange","springboot.key3",message,correlationData);


        /**
         * 发送Messaging包下的message对象

        Map<String,Object> map = new HashMap<>();
        map.put("company","tuling");
        MessageHeaders messageHeaders = new MessageHeaders(map);
        ObjectMapper objectMapper = new ObjectMapper();
        String orderJson = objectMapper.writeValueAsString(order);
        Message message = MessageBuilder.createMessage(orderJson,messageHeaders);
        System.out.println("orderJson"+orderJson);
        rabbitTemplate.convertAndSend("springboot.direct.exchange","springboot.key3",message,correlationData);
         */

        //直接发送对象
        //rabbitTemplate.convertAndSend("springboot.direct.exchange","springboot.key3",order,correlationData);
    }


    public void sendDelayMessage(Order order) {
        //构建correlationData 用于做可靠性投递得,ID:必须为全局唯一的 根据业务规则
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //开启确认模式
        rabbitTemplate.setConfirmCallback(new TulingConfirmCallBack());

        //开启消息可达监听
       // rabbitTemplate.setReturnCallback(new TulingRetrunCallBack());

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        rabbitTemplate.convertAndSend("delayExchange", "springboot.delay.key", order, new MessagePostProcessor() {
            @Override
            public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message) throws AmqpException {
                message.getMessageProperties().setHeader("x-delay", 10000);//设置延迟时间
                return message;
            }
        }, correlationData);
    }
}
