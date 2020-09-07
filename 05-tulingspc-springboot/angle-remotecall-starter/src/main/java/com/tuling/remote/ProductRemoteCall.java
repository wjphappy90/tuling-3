package com.tuling.remote;

import com.tuling.anno.RemoteCall;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by smlz on 2020/4/2.
 */
@RemoteCall(serviceName = "product-server",path = "/product")
public interface ProductRemoteCall {

    @RequestMapping("/getProduct/{productId}")
    Map<String,String> qryProductMap(@PathVariable long productId);
}
