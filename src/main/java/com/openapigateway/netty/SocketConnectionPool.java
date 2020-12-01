package com.openapigateway.netty;

import java.net.InetSocketAddress;

import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SocketConnectionPool {

    private final static int MAX_CONNECTION = 1;

    private ChannelPoolMap<InetSocketAddress, FixedChannelPool> poolMap;

    public SocketConnectionPool() throws InterruptedException {
        log.debug("create socket connection pool");

        EventLoopGroup group = new NioEventLoopGroup();
        final Bootstrap cb = new Bootstrap();
        cb.group(group)
                // Specify Channel
                .channel(NioSocketChannel.class)
                // Packing small data packets into larger frames for transmission increases the network load, i.e. TCP delay transmission.
                .option(ChannelOption.SO_KEEPALIVE, true)
                // Packing small data packets into larger frames for transmission increases the network load, i.e. TCP delay transmission.
                .option(ChannelOption.TCP_NODELAY, true);

        // SimpleChannelPool
        // ChannelPoolMap<InetSocketAddress, SimpleChannelPool> poolMap = new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {
        // @Override
        // protected SimpleChannelPool newPool(InetSocketAddress key) {
        // //return new SimpleChannelPool(cb.remoteAddress(key), new TestChannelPoolHandler());
        // return new SimpleChannelPool(cb.remoteAddress(key), new SocketConnectionChannelPoolHandler());
        // }
        // };

        // FixedChannelPool
        poolMap = new AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool>() {
            @Override
            protected FixedChannelPool newPool(InetSocketAddress key) {
                return new FixedChannelPool(cb.remoteAddress(key), new AbstractChannelPoolHandler() {
                    @Override
                    public void channelCreated(Channel ch) throws Exception {

                    }
                }, MAX_CONNECTION);
            }
        };

    }

    public void communicate(String ip, int port, ChannelInboundHandlerAdapter handler) {

        InetSocketAddress addr1 = new InetSocketAddress(ip, port);

        // depending on when you use addr1 or addr2 you will get different pools.
        // final SimpleChannelPool pool = poolMap.get(addr1);
        final FixedChannelPool pool = poolMap.get(addr1);
        Future<Channel> f = pool.acquire();

        f.addListener(new FutureListener<Channel>() {
            @Override
            public void operationComplete(Future<Channel> f) {
                if (f.isSuccess()) {
                    log.info("Connect Netty Server Success...");

                    Channel ch = f.getNow();
                    
                    // Do somethings
                    // ...
                    System.out.println(ch.toString());
                    ch.pipeline().addLast(handler);
                    ch.pipeline().fireChannelActive();
                    // ...
                    
                    // Release back to pool
                    pool.release(ch);
                }
                else {
                    log.info("Connect Netty Server failed, disconnected and reconnected...");
                }
            }
        });
    }
    
}
