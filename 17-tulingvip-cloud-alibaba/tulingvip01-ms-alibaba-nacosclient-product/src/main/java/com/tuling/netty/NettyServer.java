package com.tuling.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class NettyServer {

    @Autowired
    private NettyServerProperties nettyServerProperties;

    /**
     * 监听线程组，监听客户端请求
     */
    private EventLoopGroup bossGroup;
    /**
     * 处理客户端相关操作线程组，负责处理与客户端的数据通讯
     */
    private EventLoopGroup workerGroup;

    // 创建 服务器端的启动对象，配置参数
    ServerBootstrap bootstrap;

    @PostConstruct
    public void start(){
        System.out.println("start");
        init();
    }

    private void init() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();
        //设置缓冲区大小
        bootstrap.option(ChannelOption.SO_BACKLOG,1024);
        bootstrap.group(bossGroup, workerGroup)
                //使用NioServerSocketChannel作为服务器的通道实现
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_SNDBUF,16*1024)
                .option(ChannelOption.SO_RCVBUF,16*1024)
                //设置线程队列得到连接个数
                .option(ChannelOption.SO_BACKLOG, 128)
                //设置保持活动连接状态
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //给我们的workGroup的EventLoop对应的管道设置处理器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    //创建一个通道初始化对象（匿名对象）
                    //给pipeline 设置处理器
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyServerHandler());
                    }
                });
        ChannelFuture cf = null;
        try {
            cf = bootstrap.bind(nettyServerProperties.getPort()).sync();

        } catch (InterruptedException e) {
            System.out.println(e);
            e.printStackTrace();
        }finally {
            //对关闭通道进行监听
/*            try {
                if(cf!=null){
                    cf.channel().closeFuture().sync();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
