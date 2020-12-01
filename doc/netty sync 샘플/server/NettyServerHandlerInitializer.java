package com.openapigateway.netty.sync.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {

    /** 
     *@Fields serverHandler : Service processing
     */ 
    @Autowired
    private NettyServerHandler serverHandler;
    
    
    @Override
    protected void initChannel(Channel ch) throws Exception {

        // Get the corresponding pipeline through socket Channel
        ChannelPipeline channelPipeline = ch.pipeline();
        
        /*
         * channelPipeline There will be many handler classes (also known as interceptor classes)
         * Once you get pipeline, you can add handler directly to. addLast
         */
        ByteBuf buf = Unpooled.copiedBuffer(NettyServer.DELIMITER.getBytes());
        channelPipeline.addLast("framer", new DelimiterBasedFrameDecoder(1024*1024*2, buf));
        //channelPipeline.addLast("decoder",new StringDecoder(CharsetUtil.UTF_8));
        //channelPipeline.addLast("encoder",new StringEncoder(CharsetUtil.UTF_8));
        
        // Custom decoder, paste/unpack/unpack
        
        channelPipeline.addLast(serverHandler);
    }

}