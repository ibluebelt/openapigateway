package com.openapigateway.netty.sync.server;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.topinfo.ju.ccon.netty.bean.Message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@Component
@ChannelHandler.Sharable //Annotating a channel handler can be safely shared by multiple channels
public class NettyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerHandler.class);
    
    public static AtomicInteger nConnection = new AtomicInteger(0);
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        
        String txt = msg.toString(CharsetUtil.UTF_8);
        
        LOGGER.info("Receive a message from the client:{}", txt);
    
        
        
        ackMessage(ctx, txt);
    }

    /**
     *@Description: confirmation message 
     *@Author:Yang pan
     *@Since: 2019 September 17, 11:22:27 a.m.
     *@param ctx
     *@param message
     */
    public void ackMessage(ChannelHandlerContext ctx, String message) {
        
        //custom delimiter
        String msg = message+NettyServer.DELIMITER;
        
        ByteBuf byteBuf = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);
        
        //Response Client
        ctx.writeAndFlush(byteBuf);
    }
    
    

    /**
     *@Description: Each time a new connection comes, add one to the number of connections
     *@Author:Yang pan
     *@Since: 2019 September 16, 3:04:42 p.m.
     *@param ctx
     *@throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        nConnection.incrementAndGet();
        
        LOGGER.info("Request connection...{}，Current number of connections: : {}",  ctx.channel().id(),nConnection.get());
    }
    
    /**
     *@Description: Each time you disconnect from the server, reduce the number of connections by one
     *@Author:Yang pan
     *@Since: 2019 September 16, 3:06:10 p.m.
     *@param ctx
     *@throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        nConnection.decrementAndGet();
        LOGGER.info("Disconnect...Current number of connections: : {}",  nConnection.get());
    }
    
    
    /**
     *@Description: Callback when connection is abnormal 
     *@Author:Yang pan
     *@Since: 2019 September 16, 3:06:55 p.m.
     *@param ctx
     *@param cause
     *@throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        
        // Print error log
        cause.printStackTrace();
        
        Channel channel = ctx.channel();
        
        if(channel.isActive()){
            ctx.close();
        }
        
    }
    
}