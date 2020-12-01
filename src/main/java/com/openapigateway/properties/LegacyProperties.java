package com.openapigateway.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource("classpath:legacy.properties")
@Data
public class LegacyProperties {

    @Value("${legacy0001.ip}")
    private String legacy0001Ip;

    @Value("${legacy0001.port}")
    private int legacy0001Port;

    @Value("${legacy0002.ip}")
    private String legacy0002Ip;

    @Value("${legacy0002.port}")
    private int legacy0002Port;

}
