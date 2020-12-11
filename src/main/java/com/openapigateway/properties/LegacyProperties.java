package com.openapigateway.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource("classpath:legacy.properties")
@ConfigurationProperties("")
@Data
public class LegacyProperties {

    private Map<String, Map<String, String>> legacies = new HashMap<String, Map<String, String>>();
}
