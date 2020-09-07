package com.tuling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuling.compent.TulingMsgSender;
import com.tuling.entity.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TulingvipSpringbootRabbitmqProducerApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private TulingMsgSender tulingMsgSender;

	@Test
	public void testMsgSender() throws JsonProcessingException {
		Map<String,Object> msgProp = new HashMap<>();
		msgProp.put("company","tuling");
		msgProp.put("name","smlz");
/*		String msgBody = "hello tuling ";*/
		Order order = new Order();
		order.setOrderNo(UUID.randomUUID().toString());
		order.setUserName("smlz");
		order.setPayMoney(10000.00);
		order.setCreateDt(new Date());
		ObjectMapper objectMapper = new ObjectMapper();


		tulingMsgSender.sendMsg(objectMapper.writeValueAsString(order),msgProp);
	}

	@Test
	public void testSenderOrder() throws Exception {
		Order order = new Order();
		order.setOrderNo(UUID.randomUUID().toString());
		order.setUserName("smlz");
		order.setPayMoney(10000.00);
		order.setCreateDt(new Date());

		tulingMsgSender.sendOrderMsg(order);
	}

	@Test
	public void testSenderDelay() {
		Order order = new Order();
		order.setOrderNo(UUID.randomUUID().toString());
		order.setUserName("smlz");
		order.setPayMoney(10000.00);
		order.setCreateDt(new Date());

		tulingMsgSender.sendDelayMessage(order);
	}



}
