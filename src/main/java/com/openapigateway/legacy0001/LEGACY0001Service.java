package com.openapigateway.legacy0001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openapigateway.netty.SocketClient;
import com.openapigateway.netty.SocketClientHandler;

import lombok.extern.slf4j.Slf4j;

@Service
// @Transactional
@Slf4j
public class LEGACY0001Service {

    private final String LEGACY_ID = "LEGACY0001";

    @Autowired
    private SocketClient socketClient;

    public String getAccounts(String interfaceId, User user) {
        log.debug("getAccounts {} {}", "start", user.toString());
        String sendMsg = null;
        String recvMsg = null;

        // TODO parameter object 를 전문 string 으로 변경하는 간단한 방법 필요. charset 고려 필요
        
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
        
        // TODO 전문 string 을 return object 로 변경하는 간단한 방법 필요. charset 고려 필요

        
        
        
        return recvMsg;
    }

    public String getAccountsByPk(String userId) {
        log.debug("getAccountsByPk {} {}", "start", userId);

        String interfaceId = "LEGACY0001IL0002";

        return "LEGACY0001Service.getAccountsByPk";
    }
}
