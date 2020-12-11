package com.openapigateway.netty;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.openapigateway.properties.LegacyProperties;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
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

// TODO Component annotation 은 나중에 config server 구축하면... OpenapigatewayApplication 에서 NettyClient 부분 지우고 annotation 살려 처리
//@Component
@Slf4j
public class SocketClient {

    private final static int MAX_CONNECTION = 1;

    Map<String, Map<String, String>> legacies;

    private ChannelPoolMap<InetSocketAddress, FixedChannelPool> poolMap;

    public SocketClient(LegacyProperties legacyProperties) {
        log.debug("create socket connection pool");
        this.legacies = legacyProperties.getLegacies();
        createPools();
        connectPools();
    }

    private void createPools() {
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

    private void connectPools() {
        for (String legacyId : legacies.keySet()) {
            String sendMsg = "live check interface";
            SocketClientHandler clientHandler = new SocketClientHandler(legacyId, sendMsg);
            communicate(clientHandler);
            String recvMsg = null;
            try {
                recvMsg = clientHandler.getRecvMessage();
                log.debug(recvMsg);
            }
            catch (InterruptedException e) {
                log.debug("exception 발생", e);

                // TODO 적당한 TimeoutException 만들어야 함
                throw new NullPointerException("서버 연결 실패하였습니다.");
            }
        }
    }

    public void communicate(SocketClientHandler clientHandler) {
        // depending on when you use addr1 or addr2 you will get different pools.
        // final SimpleChannelPool pool = poolMap.get(addr1);
        final FixedChannelPool channelPool = getChannelPool(clientHandler.getServerId());
        Future<Channel> future = channelPool.acquire();

        setTimeoutIn(clientHandler);

        future.addListener(new FutureListener<Channel>() {
            @Override
            public void operationComplete(Future<Channel> f) {
                if (f.isSuccess()) {
                    log.info("Connect Netty Server Success...");

                    Channel ch = f.getNow();

                    // Do somethings
                    // System.out.println(ch.toString());
                    ch.pipeline().addLast(clientHandler);
                    ch.pipeline().fireChannelActive();
                    // ch.close();

                    // Release back to pool
                    channelPool.release(ch);
                }
                else {
                    log.info("Connect Netty Server failed, disconnected and reconnected...");
                }
            }
        });
    }

    private FixedChannelPool getChannelPool(String serverId) {
        Map<String, String> serverInfo = getServerInfo(serverId);
        String serverIp = serverInfo.get("ip");
        int serverPort = Integer.parseInt(serverInfo.get("port"));

        InetSocketAddress addr1 = new InetSocketAddress(serverIp, serverPort);

        // depending on when you use addr1 or addr2 you will get different pools.
        // final SimpleChannelPool pool = poolMap.get(addr1);
        return poolMap.get(addr1);
    }

    private void setTimeoutIn(SocketClientHandler clientHandler) {
        String serverId = clientHandler.getServerId();
        Map<String, String> serverInfo = getServerInfo(serverId);

        int serverTimeout = Integer.parseInt(serverInfo.get("timeout"));
        int countDownLatchTimeout = Integer.parseInt(serverInfo.get("countdownlatchtimeout"));
        String countDownLatchTimeunit = serverInfo.get("countdownlatchtimeunit");

        TimeUnit countDownLatchTimeUnit = null;
        if ("SECONDS".equals(countDownLatchTimeunit)) {
            countDownLatchTimeUnit = TimeUnit.SECONDS;
        }
        else if ("MILLISECONDS".equals(countDownLatchTimeunit)) {
            countDownLatchTimeUnit = TimeUnit.MILLISECONDS;
        }
        else {
            // TODO 적당한 NotFoundException 만들어야 함
            throw new NullPointerException("서버 time unit 을 찾을 수 없습니다.");
        }

        clientHandler.setCountDownLatchTimeout(countDownLatchTimeout);
        clientHandler.setCountDownLatchUnit(countDownLatchTimeUnit);
    }

    private Map<String, String> getServerInfo(String serverId) {
        return legacies.get(serverId);
    }

}
