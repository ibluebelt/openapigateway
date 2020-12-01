package com.openapigateway.netty.sync.client;

import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.topinfo.ci.netty.service.NettyClientService;
import com.topinfo.ci.netty.utils.ExceptionUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;

@Component
@ChannelHandler.Sharable // Annotating a channel handler can be safely shared by multiple channels
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientHandler.class);

    @Autowired
    private NettyClientService service;

    @Autowired
    private NettyClient nettyClient;

    /**
     * @Description: When the server sends a message to the client, it triggers the method to receive the message.
     * @Author:Yang pan
     * @Since: 2019 September 12, 5:03:31 p.m.
     * @param ctx
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {

        String msg = byteBuf.toString(CharsetUtil.UTF_8);

        LOGGER.info("The client receives a message:{}", msg);

        //service.ackMsg(msg);
        service.ackSyncMsg(msg); // Synchronized message return
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Successful request connection...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        LOGGER.info("Connection disconnected...");

        // Disconnecting and reconnecting during use
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {

            @Override
            public void run() {
                // Reconnect
                nettyClient.start();
            }
        }, 20, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

    /**
     * Handling exceptions, typically putting Andler, which implements exception handling logic, at the end of Channel Pipeline
     * This ensures that all inbound messages are always processed, regardless of where they occur. Here's just a simple way to close Channel and print exception messages
     * 
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

        // Output to log
        ExceptionUtil.getStackTrace(cause);

        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }

}