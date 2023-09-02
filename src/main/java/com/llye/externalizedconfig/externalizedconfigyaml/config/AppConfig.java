package com.llye.externalizedconfig.externalizedconfigyaml.config;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Value
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    Map<String, FeatureFlag> featureFlags;

    @Value
    public static class FeatureFlag {
        boolean enabled;
        BetaTesting betaTesting;
        Rollout rollout;
    }

    @Value
    public static class BetaTesting {
        boolean enabled;
        List<String> testers;
    }

    @Value
    public static class Rollout {
        String strategy;
        int percentage;
    }
}
