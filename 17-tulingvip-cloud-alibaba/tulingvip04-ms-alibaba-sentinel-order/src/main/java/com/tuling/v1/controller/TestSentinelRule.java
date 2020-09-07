package com.tuling.v1.controller;

import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * Created by smlz on 2019/11/24.
 */
public class TestSentinelRule {

    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for(int i=0;i<1000;i++) {
            restTemplate.postForObject("http://localhost:8080/saveOrder",null,String.class);
            Thread.sleep(10);
        }

        /*testWaiting();*/

    }

    public static void testWaiting() {
        RestTemplate restTemplate = new RestTemplate();
        for(int i=0;i<1000;i++) {
            restTemplate.postForObject("http://localhost:8080/findById/1",null,String.class);
            System.out.println(new Date());
        }
    }


}
