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
    public static final String FEATURE_A = "featureA";
    public static final String FEATURE_B = "featureB";

    Map<String, FeatureFlag> featureFlags;

    public record FeatureFlag(boolean enabled, BetaTesting betaTesting, Rollout rollout) {}

    public record BetaTesting(boolean enabled, List<String> testers) {}

    public record Rollout(String strategy, int percentage) {}
}
