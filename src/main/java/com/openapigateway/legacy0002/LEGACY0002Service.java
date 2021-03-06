package com.openapigateway.legacy0002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openapigateway.netty.SocketClientHandler;
import com.openapigateway.netty.SocketClient;

import lombok.extern.slf4j.Slf4j;

@Service
// @Transactional
@Slf4j
public class LEGACY0002Service {

    private final String LEGACY_ID = "LEGACY0002";

    @Autowired
    private SocketClient socketClient;

    public String getBalances() {
        log.debug("getBalances {}", "start");
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

    public String getBalanceByPk(String userId) {
        log.debug("getBalanceByPk {} {}", "start", userId);

        return "LEGACY0002Service.getBalanceByPk";
    }
}
