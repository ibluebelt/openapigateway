package com.openapigateway.legacy0003;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openapigateway.netty.SocketClientHandler;
import com.openapigateway.netty.SocketClient;

import lombok.extern.slf4j.Slf4j;

@Service
// @Transactional
@Slf4j
public class LEGACY0003Service {

    private final String LEGACY_ID = "LEGACY0003";

    @Autowired
    private SocketClient socketClient;

    public String getCredits() {
        log.debug("getCredits {}", "start");
        String sendMsg = null;
        String recvMsg = null;

        sendMsg = "[" + LEGACY_ID + "]" + "[" + Thread.currentThread().getId() + "]";
        log.debug(sendMsg);

        SocketClientHandler clientHandler = new SocketClientHandler(LEGACY_ID, sendMsg);
        socketClient.communicate(clientHandler);
        try {
            recvMsg = clientHandler.getRecvMessage();
            log.debug(recvMsg + "[" + Thread.currentThread().getId() + "]");
        }
        catch (InterruptedException e) {
            // TODO 적당한 TimeoutException 만들어야 함
            throw new NullPointerException("서버 응답시간 초과하였습니다.");
        }

        return recvMsg;
    }

    public String getCreditByPk(String userId) {
        log.debug("getCreditByPk {} {}", "start", userId);

        return "LEGACY0003Service.getCreditByPk";
    }
}
