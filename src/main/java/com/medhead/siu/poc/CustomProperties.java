package com.medhead.siu.poc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.medhead.siu.poc")
@Data
public class CustomProperties {
    private Integer hospitalSearchDistance;
}
