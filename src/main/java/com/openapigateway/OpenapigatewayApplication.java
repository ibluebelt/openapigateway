package com.openapigateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.openapigateway.filter.ErrorFilter;
import com.openapigateway.filter.PostFilter;
import com.openapigateway.filter.PreFilter;
import com.openapigateway.filter.RouteFilter;
import com.openapigateway.netty.SocketClient;
import com.openapigateway.properties.LegacyProperties;

@SpringBootApplication
@EnableZuulProxy
public class OpenapigatewayApplication {

    @Autowired
    LegacyProperties legacyProperties;

    public static void main(String[] args) {
        SpringApplication.run(OpenapigatewayApplication.class, args);
    }

    @Bean
    public PreFilter preFilter() {
        return new PreFilter();
    }

    @Bean
    public PostFilter postFilter() {
        return new PostFilter();
    }

    @Bean
    public ErrorFilter errorFilter() {
        return new ErrorFilter();
    }

    @Bean
    public RouteFilter routeFilter() {
        return new RouteFilter();
    }

    @Bean
    public SocketClient socketClient() {
        return new SocketClient(legacyProperties);
    }
}
