package com.openapigateway.legacy0001;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.Data;

@Data
public class User {

    private String id;

    private String pw;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
