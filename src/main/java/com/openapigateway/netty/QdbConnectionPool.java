package com.openapigateway.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

public class QdbConnectionPool {
//    private ChannelPoolMap<InetSocketAddress, FixedChannelPool> poolMap;
//
//    public void init() {
//        EventLoopGroup group = new NioEventLoopGroup();
//        final Bootstrap bootstrap = new Bootstrap();
//        bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
//
//        poolMap = new AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool>() {
//            @Override
//            protected FixedChannelPool newPool(InetSocketAddress key) {
//                bootstrap.remoteAddress(key);
//                return new FixedChannelPool(bootstrap, new QdbPoolHandler(), 100);
//            }
//        };
//
//    }
//
//    public QdbResult query(InetSocketAddress address, String bkey) {
//        final QdbResult result = new QdbResult(bkey);
//        final CountDownLatch countDownLatch = new CountDownLatch(1);
//        final FixedChannelPool pool = poolMap.get(address);
//        Future<Channel> future = pool.acquire();
//        future.addListener(new FutureListener<Channel>() {
//            @Override
//            public void operationComplete(Future<Channel> future) {
//                if (future.isSuccess()) {
//                    Channel ch = future.getNow();
//                    System.out.println(ch.toString());
//                    ch.pipeline().addLast(new QdbClientHandler(result, countDownLatch));
//                    ch.pipeline().fireChannelActive();
//                }
//                else {
//                    System.out.println("future not succ");
//                }
//            }
//        });
//        try {
//            countDownLatch.await();
//        }
//        catch (InterruptedException ex) {
//
//        }
//        pool.release(future.getNow());
//        return result;
//    }
//
//    public static void main(String[] args) throws Exception {
//        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
//        QdbConnectionPool pool = new QdbConnectionPool();
//        pool.init();
//        QdbResult result = pool.query(address, "xxxxxx");
//    }

}
