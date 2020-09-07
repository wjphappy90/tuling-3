package com.tuling.netty;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.tbbpower.netty")
public class NettyServerProperties {

    private int port = 3000;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
