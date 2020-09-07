package com.tuling.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.client.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

/**
 * 根据RestTemplate特性自己改造
 * Created by smlz on 2019/11/19.
 */
@Slf4j
public class TulingRestTemplate extends RestTemplate {

    private DiscoveryClient discoveryClient;

    public TulingRestTemplate (DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    protected <T> T doExecute(URI url, @Nullable HttpMethod method, @Nullable RequestCallback requestCallback,
                              @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {

        Assert.notNull(url, "URI is required");
        Assert.notNull(method, "HttpMethod is required");
        ClientHttpResponse response = null;
        try {

            log.info("请求的url路径为:{}",url);
            //把服务名 替换成我们的IP
            url = replaceUrl(url);

            log.info("替换后的路径:{}",url);

            ClientHttpRequest request = createRequest(url, method);
            if (requestCallback != null) {
                requestCallback.doWithRequest(request);
            }
            response = request.execute();
            handleResponse(url, method, response);
            return (responseExtractor != null ? responseExtractor.extractData(response) : null);
        }
        catch (IOException ex) {
            String resource = url.toString();
            String query = url.getRawQuery();
            resource = (query != null ? resource.substring(0, resource.indexOf('?')) : resource);
            throw new ResourceAccessException("I/O error on " + method.name() +
                    " request for \"" + resource + "\": " + ex.getMessage(), ex);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }


    /**
     * 方法实现说明:把微服务名称  去注册中心拉取对应IP进行调用
     * http://product-center/selectProductInfoById/1
     * @author:smlz
     * @param url:请求的url
     * @return:
     * @exception:
     * @date:2020/2/6 13:11
     */
    private URI replaceUrl(URI url){

        //1:从URI中解析调用的调用的serviceName=product-center
        String serviceName = url.getHost();
        log.info("调用微服务的名称:{}",serviceName);

        //2:解析我们的请求路径 reqPath= /selectProductInfoById/1
        String reqPath = url.getPath();
        log.info("请求path:{}",reqPath);


        //通过微服务的名称去nacos服务端获取 对应的实例列表
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(serviceName);
        if(serviceInstanceList.isEmpty()) {
            throw new RuntimeException("没有可用的微服务实例列表:"+serviceName);
        }

        String serviceIp = chooseTargetIp(serviceInstanceList);

        String source = serviceIp+reqPath;
        try {
            return new URI(source);
        } catch (URISyntaxException e) {
            log.error("根据source:{}构建URI异常",source);
        }
        return url;
    }

    /**
     * 方法实现说明:从服务列表中 随机选举一个ip
     * @author:smlz
     * @param serviceInstanceList 服务列表
     * @return: 调用的ip
     * @exception:
     * @date:2020/2/6 13:15
     */
    private String chooseTargetIp(List<ServiceInstance> serviceInstanceList) {
        //采取随机的获取一个
        Random random = new Random();
        Integer randomIndex = random.nextInt(serviceInstanceList.size());
        String serviceIp = serviceInstanceList.get(randomIndex).getUri().toString();
        log.info("随机选举的服务IP:{}",serviceIp);
        return serviceIp;
    }



}
