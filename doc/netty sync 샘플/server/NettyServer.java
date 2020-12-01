package com.openapigateway.netty.sync.server;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Component
public class NettyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);
    
    /** 
     *@Fields DELIMITER : Custom separators, server and client alignment
     */ 
    public static final String DELIMITER = "@@";
    
    /**
     * @Fields boss : boss Thread groups are used to handle connection work. By default, they are twice the number of CPU s in the system. They can also be specified according to the actual situation.
     */
    private EventLoopGroup boss = new NioEventLoopGroup();

    /**
     * @Fields work : work Thread groups are used for data processing. By default, they are twice the number of CPU s in the system. They can also be specified according to the actual situation.
     */
    private EventLoopGroup work = new NioEventLoopGroup();

    /**
     * @Fields port : Monitor port
     */
    private Integer port = 8888;
    
    @Autowired
    private NettyServerHandlerInitializer handlerInitializer;

    /**
     * @throws InterruptedException 
     * @Description: Start Netty Server
     * @Author:Yang pan
     * @Since: 2019 September 12, 4:21:35 p.m.
     */
    @PostConstruct
    public void start() throws InterruptedException {

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(boss, work)
                // Specify Channel
                .channel(NioServerSocketChannel.class)
                // Set the socket address using the specified port
                .localAddress(new InetSocketAddress(port))

                // The number of queues that the server can connect corresponds to the backlog parameter in the listen function of TCP/IP protocol
                .option(ChannelOption.SO_BACKLOG, 1024)

                // Setting up a long TCP connection, TCP will automatically send an active probe data message if there is no data communication within two hours.
                .childOption(ChannelOption.SO_KEEPALIVE, true)

                // Packing small data packets into larger frames for transmission increases the network load, i.e. TCP delay transmission.
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(handlerInitializer);

        ChannelFuture future = bootstrap.bind().sync();
        
        if (future.isSuccess()) {
            LOGGER.info("start-up Netty Server...");
        }
    }
    
    @PreDestroy
    public void destory() throws InterruptedException {
        boss.shutdownGracefully().sync();
        work.shutdownGracefully().sync();
        LOGGER.info("Close Netty...");
    }
}