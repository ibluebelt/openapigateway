package com.openapigateway.legacy0001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openapigateway.netty.ClientHandler;
import com.openapigateway.netty.SocketConnectionPool;
import com.openapigateway.properties.LegacyProperties;

import lombok.extern.slf4j.Slf4j;

@Service
// @Transactional
@Slf4j
public class LEGACY0001Service {

    @Autowired
    LegacyProperties legacyProperties;

    @Autowired
    private SocketConnectionPool socketConnectionPool;

    public String getCredits() {
        log.debug("getCredits {}", "start");
        
        String ip = legacyProperties.getLegacy0001Ip();
        int port = legacyProperties.getLegacy0001Port();
        
        ClientHandler clientHandler = new ClientHandler("abcefg");
        socketConnectionPool.communicate(ip, port, clientHandler);
        
        
        String recvMessage = null;
        
        try {
            recvMessage = clientHandler.getRecvMessage();
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return recvMessage;
    }

    public String getCreditByPk(String userId) {
        log.debug("getCreditByPk {} {}", "start", userId);

        return "LEGACY0001Service.getCreditByPk";
    }
}
