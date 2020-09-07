package com.tuling.compent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.tuling.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * 消息接受组件
 * Created by smlz on 2019/10/10.
 */
@Component
@Slf4j
public class TulingMsgReceiver {


    @RabbitListener(queues = {"tulingBootQueue"})
    @RabbitHandler
    public void consumerMsg(Message message, Channel channel) throws IOException {

        log.info("消费消息:{}",message.getPayload());
        //手工签收
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        log.info("接受deliveryTag:{}",deliveryTag);
        channel.basicAck(deliveryTag,false);
    }

    @RabbitListener(queues = {"tulingBootDelayQueue"})
    @RabbitHandler
    public void consumerDelayMsg(org.springframework.amqp.core.Message message, Channel channel) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ObjectMapper objectMapper = new ObjectMapper();
        Order order = objectMapper.readValue(message.getBody(),Order.class);
        log.info("在{},签收:{}",sdf.format(new Date()),order);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }


    /**
     * todo:   强烈不推荐这种用法 我们再开发中需要把队列 交换机  绑定配置到我们专门的配置类中
     * @param message
     * @param channel
     */
    @RabbitListener(bindings =
         @QueueBinding(
                 value = @Queue(
                         name = "tulingBootQueue2",
                         durable = "true",
                         autoDelete = "false",
                         exclusive = "false"
                 ),
                 exchange = @Exchange(
                         name = "springboot.direct.exchange",
                         type = "direct",
                         durable = "true",
                         autoDelete = "fasle"),
                 key = {"springboot.key2"}
         )
    )

    @RabbitHandler
    public void consumerMsg2(Message message,Channel channel) throws IOException {
        log.info("consumerMsg2===消费消息:{}",message.getPayload());
        //手工签收
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        log.info("consumerMsg2===接受deliveryTag:{}",deliveryTag);
        channel.basicAck(deliveryTag,false);
    }



    /**
     *   tuling.rabbitmq.queue.queueName=tulingBootQueue3
         tuling.rabbitmq.queue.durable=true
         tuling.rabbitmq.queue.autoDelete=false
         tuling.rabbitmq.queue.exclusive=false

         tuling.rabbitmq.exchange.name=springboot.direct.exchange
         tuling.rabbitmq.exchange.type=direct
         tuling.rabbitmq.exchange.autoDelete=false
         tuling.rabbitmq.exchange.durable=true
         tuling.rabbitmq.routing.key=springboot.key3


    @RabbitListener(bindings =
    @QueueBinding(
            value = @Queue(
                    name = "${tuling.rabbitmq.queue.queueName}",
                    durable = "${tuling.rabbitmq.queue.durable}",
                    autoDelete = "${tuling.rabbitmq.queue.autoDelete}",
                    exclusive = "${tuling.rabbitmq.queue.exclusive}"
            ),
            exchange = @Exchange(
                    name = "${tuling.rabbitmq.exchange.name}",
                    type = "${tuling.rabbitmq.exchange.type}",
                    durable = "${tuling.rabbitmq.exchange.durable}",
                    autoDelete = "${tuling.rabbitmq.exchange.autoDelete}"),
            key = {"${tuling.rabbitmq.routing.key}"}
    )
    )
    @RabbitHandler
    public void conusmerOrder(@Payload Order order, @Headers Map<String,Object> headers, Channel channel) throws IOException {
        log.info("消费消息order:{}",order.toString());
        log.info("headers:{}",headers);
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag,false);
    }
     */


    /**
     * 接受客户端发送的core包下的message
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = {"tulingBootQueue3"})
    @RabbitHandler
    public void consumerOrder(org.springframework.amqp.core.Message message,Channel channel) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Order order = objectMapper.readValue(message.getBody(),Order.class);
        log.info("order:{}",order.toString());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }


/*    @RabbitListener(queues = {"tulingBootQueue3"})
    @RabbitHandler
    public void consumerOrder(Message message,Channel channel) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Order order = objectMapper.readValue(message.getPayload().toString(),Order.class);
        log.info("消费消息:{}",order);
        //手工签收
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        log.info("接受deliveryTag:{}",deliveryTag);
        channel.basicAck(deliveryTag,false);
    }*/
}
