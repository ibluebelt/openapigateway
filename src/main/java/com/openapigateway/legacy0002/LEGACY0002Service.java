package com.openapigateway.legacy0002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openapigateway.netty.SocketConnectionPool;
import com.openapigateway.properties.LegacyProperties;

import lombok.extern.slf4j.Slf4j;

@Service
// @Transactional
@Slf4j
public class LEGACY0002Service {
    
    @Autowired
    LegacyProperties legacyProperties;

    @Autowired
    private SocketConnectionPool socketConnectionPool;

    public String getBalances() {
        log.debug("getBalances {}", "start");

        return "LEGACY0002Service.getBalances";
    }

    public String getBalanceByPk(String userId) {
        log.debug("getBalanceByPk {} {}", "start", userId);

        return "LEGACY0002Service.getBalanceByPk";
    }
}
