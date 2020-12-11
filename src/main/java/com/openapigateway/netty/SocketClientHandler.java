package com.openapigateway.netty;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class SocketClientHandler extends ChannelInboundHandlerAdapter {

    // 메시지 사이즈를 결정합니다.
    private final int MESSAGE_SIZE = 256;

    private final ByteBuf sendMessage;

    private int countDownLatchTimeout;

    private TimeUnit countDownLatchUnit;

    private byte[] recvByteMessage;

    // Because the request and response are one-to-one, the CountDownLatch value is initialized to 1.
    private CountDownLatch latch = new CountDownLatch(1);

    private String serverId;

    // 초기화
    public SocketClientHandler(String serverId, String msg) {
        this.serverId = serverId;

        this.sendMessage = Unpooled.buffer(MESSAGE_SIZE);

        // 예제로 사용할 바이트 배열을 만듭니다.
        byte[] str = msg.getBytes();

        // 예제 바이트 배열을 메시지에 씁니다.
        this.sendMessage.writeBytes(str);
    }

    // 채널이 활성화 되면 동작할 코드를 정의합니다.
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 메시지를 쓴 후 플러쉬합니다.
        ctx.writeAndFlush(sendMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 받은 메시지를 ByteBuf형으로 캐스팅합니다.
        ByteBuf byteBufMessage = (ByteBuf) msg;

        // 읽을 수 있는 바이트의 길이를 가져옵니다.
        int size = byteBufMessage.readableBytes();

        // 읽을 수 있는 바이트의 길이만큼 바이트 배열을 초기화합니다.
        recvByteMessage = new byte[size];

        // for문을 돌며 가져온 바이트 값을 연결합니다.
        for (int i = 0; i < size; i++) {
            recvByteMessage[i] = byteBufMessage.getByte(i);
        }

        // 결과를 콘솔에 출력합니다.
        System.out.println("서버에서 받음");

        // 그후 컨텍스트를 종료합니다.
        ctx.close();
    }

    public String getRecvMessage() throws InterruptedException {
        if (latch.await(countDownLatchTimeout, countDownLatchUnit)) {
            log.debug("timeout 내에 응답");

            // 바이트를 String 형으로 변환합니다.
            String recvMessage = new String(recvByteMessage);

            return recvMessage;
        }
        else {
            throw new InterruptedException("timeout 발생");
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        latch.countDown();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // cause.printStackTrace();

        log.debug("exception 발생");

        ctx.close();
    }
}