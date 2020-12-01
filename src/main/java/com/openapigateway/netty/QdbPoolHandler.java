package com.openapigateway.netty;


import io.netty.channel.Channel;
import io.netty.channel.pool.AbstractChannelPoolHandler;

public class QdbPoolHandler extends AbstractChannelPoolHandler {

    @Override
    public void channelCreated(Channel ch) throws Exception {
//        ch.pipeline().addLast(new QdbDecoder());
    }
}