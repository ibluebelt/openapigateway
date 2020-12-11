package com.openapigateway.standalone.legacy.netty.server;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LegacyEchoServerHandler extends ChannelInboundHandlerAdapter {

    public static final int MESSAGE_SIZE = 256;

    private long id;

    private int port;

    public LegacyEchoServerHandler(long id, int port) {
        this.id = id;
        this.port = port;
    }

    // 채널을 읽을 때 동작할 코드를 정의 합니다.
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());
        // 3. 수신된 데이터를 가지고 있는 네티의 바이트 버퍼 객체로 부터 문자열 객체를 읽어온다.

        log.debug("[{}][{}]클라이언트에서 받은 데이터:[{}]", port, id, readMessage);

        Thread.sleep(1 * 1000);

        String sendMsg = "[" + port + "][" + id + "]" + readMessage;
        log.debug("[{}][{}]서버에서 보낸 데이터:[{}]", port, id, sendMsg);

        ctx.write(toByteBuf(sendMsg));
    }

    // 채널 읽는 것을 완료했을 때 동작할 코드를 정의 합니다.
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush(); // 컨텍스트의 내용을 플러쉬합니다.
    };

    // 예외가 발생할 때 동작할 코드를 정의 합니다.
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace(); // 쌓여있는 트레이스를 출력합니다.
        ctx.close(); // 컨텍스트를 종료시킵니다.
    }

    private ByteBuf toByteBuf(String temp1) {
        ByteBuf message = Unpooled.buffer(MESSAGE_SIZE);

        // 예제로 사용할 바이트 배열을 만듭니다.
        byte[] str = temp1.getBytes();

        // 예제 바이트 배열을 메시지에 씁니다.
        message.writeBytes(str);

        return message;
    }
}