package com.tuling.rabbitmqwithspring;

import com.rabbitmq.client.Channel;
import com.tuling.conveter.TulingImageConverter;
import com.tuling.conveter.TulingWordConverter;
import com.tuling.entity.*;
import com.tuling.entity.Address;
import com.tuling.messagedelegate.TulingMsgDelegate;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * spring整合rabbitmq的整合版本
 * Created by smlz on 2019/10/4.
 */
@Configuration
public class RabbitmqConfig {


    /**
     * 创建连接工厂
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory () {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setAddresses("192.168.159.8:5672");
        cachingConnectionFactory.setVirtualHost("tuling");
        cachingConnectionFactory.setUsername("smlz");
        cachingConnectionFactory.setPassword("smlz");
        cachingConnectionFactory.setConnectionTimeout(100000);
        cachingConnectionFactory.setCloseTimeout(100000);
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        //spring容器启动加载该类
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    //=====================================申明三个交换机====================================================================
    @Bean
    public TopicExchange topicExchange() {
        TopicExchange topicExchange = new TopicExchange("tuling.topic.exchange",true,false);
        return topicExchange;
    }

    @Bean
    public DirectExchange directExchange() {
        DirectExchange directExchange = new DirectExchange("tuling.direct.exchange",true,false);
        return directExchange;
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        FanoutExchange fanoutExchange = new FanoutExchange("tuling.faout.exchange",true,false);
        return fanoutExchange;
    }

    //===========================================申明队列===========================================================
    @Bean
    public Queue testTopicQueue() {
        Queue queue = new Queue("testTopicQueue",true,false,false,null);
        return queue;
    }

    @Bean
    public Queue testTopicQueue2() {
        Queue queue = new Queue("testTopicQueue2",true,false,false,null);
        return queue;
    }

    @Bean
    public Queue testDirectQueue() {
        Queue queue = new Queue("testDirectQueue",true,false,false,null);
        return queue;
    }

    @Bean
    public Queue testFaoutQueue() {
        Queue queue = new Queue("testfaoutQueue",true,false,false,null);
        return queue;
    }

    @Bean
    public Queue orderQueue() {
        Queue queue = new Queue("orderQueue",true,false,false,null);
        return queue;
    }

    @Bean
    public Queue addressQueue() {
        Queue queue = new Queue("addressQueue",true,false,false,null);
        return queue;
    }

    @Bean
    public Queue fileQueue() {
        Queue queue = new Queue("fileQueue",true,false,false,null);
        return queue;
    }



    //========================================申明绑定==============================================================
    @Bean
    public Binding topicBingding() {
        return BindingBuilder.bind(testTopicQueue()).to(topicExchange()).with("topic.#");
    }

    @Bean
    public Binding topicBingding2() {
        return BindingBuilder.bind(testTopicQueue2()).to(topicExchange()).with("topic.key.#");
    }

    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(testDirectQueue()).to(directExchange()).with("direct.key");
    }

    @Bean
    public Binding orderQueueBinding() {
        return BindingBuilder.bind(orderQueue()).to(directExchange()).with("rabbitmq.order");
    }

    @Bean
    public Binding addressQueueBinding() {
        return BindingBuilder.bind(addressQueue()).to(directExchange()).with("rabbitmq.address");
    }

    @Bean
    public Binding fileQueueBinding() {
        return BindingBuilder.bind(fileQueue()).to(directExchange()).with("rabbitmq.file");
    }

    @Bean
    public Binding fanoutBinding() {
        return BindingBuilder.bind(testFaoutQueue()).to(fanoutExchange());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setReceiveTimeout(50000);
        return rabbitTemplate;
    }

    /**
     * 简单的消息监听容器
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory());
        //设置监听的队列
        simpleMessageListenerContainer.setQueues(testTopicQueue(),testDirectQueue(),testTopicQueue2(),orderQueue(),addressQueue(),fileQueue());
        //设置当前消费者1
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        //最大消费者个数5
        simpleMessageListenerContainer.setMaxConcurrentConsumers(10);
        //设置签收模式
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //设置拒绝重回队列
        simpleMessageListenerContainer.setDefaultRequeueRejected(false);
        //消费端的标签策略
        simpleMessageListenerContainer.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String s) {
                return UUID.randomUUID().toString()+"-"+s;
            }
        });


        //创建消息监听适配器对象
        //自己创建一个消息委托器对象
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new TulingMsgDelegate());

        //设置监听得方法
        // messageListenerAdapter.setDefaultListenerMethod("consumerMsg");
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);

        //指定具体某一个队列被某一个消费者消费
/*        Map<String,String> queueOrTagToMethodName = new HashMap<>();
        queueOrTagToMethodName.put("testTopicQueue","consumerTopicQueue");
        queueOrTagToMethodName.put("testTopicQueue2","consumerTopicQueue2");
        messageListenerAdapter.setQueueOrTagToMethodName(queueOrTagToMethodName);*/


        /**
         * 处理json
         * 1:需要创建一个消息转换器对象
         * 2:把消息转换器设置到消息监听适配器上
         * 3:把监听器设置到容器中

        messageListenerAdapter.setDefaultListenerMethod("consumerJsonMessage");
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        messageListenerAdapter.setMessageConverter(jackson2JsonMessageConverter);
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);
         */

        //===============================处理java对象得
        messageListenerAdapter.setDefaultListenerMethod("consumerJavaObjMessage");
        //消息转换器
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        //版本问题 需要设置信任的包
        javaTypeMapper.setTrustedPackages("com.tuling.entity");
        //json转java得
        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
        //设置消息转换器
        messageListenerAdapter.setMessageConverter(jackson2JsonMessageConverter);
        //设置监听




        /**
         * 处理pdf  image 等等
         */
        messageListenerAdapter.setDefaultListenerMethod("consumerFileMessage");
        //全局转换器
        ContentTypeDelegatingMessageConverter messageConverter = new ContentTypeDelegatingMessageConverter();
        messageConverter.addDelegate("img/png", new TulingImageConverter());
        messageConverter.addDelegate("img/jpg",new TulingImageConverter());
        messageConverter.addDelegate("application/word",new TulingWordConverter());
        messageConverter.addDelegate("word",new TulingWordConverter());


        messageListenerAdapter.setMessageConverter(messageConverter);
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);


        return simpleMessageListenerContainer;
    }
}
