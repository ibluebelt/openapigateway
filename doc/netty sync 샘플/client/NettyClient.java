package com.openapigateway.netty.sync.client;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * @Description: Netty Client
 * @Author:Yang pan
 * @Since:2019 September 26, 8:54:59 p.m.
 */
@Component
public class NettyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    private EventLoopGroup group = new NioEventLoopGroup();

    /**
     * @Fields DELIMITER : Custom separators, server and client alignment
     */
    public static final String DELIMITER = "@@";

    /**
     * @Fields hostIp : Server ip
     */
    private String hostIp = "192.168.90.96";

    /**
     * @Fields port : Server Port
     */
    private int port = 8888;

    /**
     * @Fields socketChannel : passageway
     */
    private SocketChannel socketChannel;

    /**
     * @Fields clientHandlerInitilizer : Initialization
     */
    @Autowired
    private NettyClientHandlerInitilizer clientHandlerInitilizer;

    /**
     * @Description: Start the client
     * @Author:Yang pan
     * @Since: 2019 September 12, 4:43:21 p.m.
     */
    @SuppressWarnings("unchecked")
    @PostConstruct
    public void start() {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                // Specify Channel
                .channel(NioSocketChannel.class)
                // Server address
                .remoteAddress(hostIp, port)

                // Packing small data packets into larger frames for transmission increases the network load, i.e. TCP delay transmission.
                .option(ChannelOption.SO_KEEPALIVE, true)
                // Packing small data packets into larger frames for transmission increases the network load, i.e. TCP delay transmission.
                .option(ChannelOption.TCP_NODELAY, true).handler(clientHandlerInitilizer);

        // Connect
        ChannelFuture channelFuture = bootstrap.connect();
        // Client disconnection reconnection logic
        channelFuture.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    LOGGER.info("Connect Netty Server Success...");
                }
                else {

                    LOGGER.info("Connect Netty Server failed, disconnected and reconnected...");
                    final EventLoop loop = future.channel().eventLoop();
                    loop.schedule(new Runnable() {
                        @Override
                        public void run() {
                            LOGGER.info("Connection is being retried...");
                            start();
                        }
                    }, 20, TimeUnit.SECONDS);
                }
            }

        });

        socketChannel = (SocketChannel) channelFuture.channel();
    }

    /**
     * @Description: message sending
     * @Author:Yang pan
     * @Since: 2019 September 12, 5:08:47 p.m.
     * @param message
     */
    public void sendMsg(String message) {

        String msg = message.concat(NettyClient.DELIMITER);

        ByteBuf byteBuf = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);
        ChannelFuture future = socketChannel.writeAndFlush(byteBuf);

        future.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

                if (future.isSuccess()) {
                    System.out.println("===========Send successfully");
                }
                else {
                    System.out.println("------------------fail in send");
                }
            }
        });
    }

    /**
     * @Description: Send Synchronized Messages
     * @Author:Yang pan
     * @Since: 2019 September 12, 5:08:47 p.m.
     * @param message
     */
    public String sendSyncMsg(String message, SyncFuture<String> syncFuture) {

        String result = "";

        String msg = message.concat(NettyClient.DELIMITER);

        ByteBuf byteBuf = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);

        try {

            ChannelFuture future = socketChannel.writeAndFlush(byteBuf);
            future.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {

                    if (future.isSuccess()) {
                        System.out.println("===========Send successfully");
                    }
                    else {
                        System.out.println("------------------fail in send");
                    }
                }
            });

            // Wait for 8 seconds.
            result = syncFuture.get(8, TimeUnit.SECONDS);

        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}