package com.llye.externalizedconfig.externalizedconfigyaml.service;

import com.llye.externalizedconfig.externalizedconfigyaml.config.AppConfig;
import com.llye.externalizedconfig.externalizedconfigyaml.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final AppConfig appConfig;

    public ProductService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public List<ProductDto> getAllProducts(int pageNumber, int pageSize) {
        Map<String, AppConfig.FeatureFlag> featureFlags = appConfig.getFeatureFlags();
        Optional<Map.Entry<String, AppConfig.FeatureFlag>> maybeFeatureFlag = featureFlags.entrySet()
                                                                                          .stream()
                                                                                          .filter(entry -> entry.getValue()
                                                                                                                   .isEnabled())
                                                                                          .findFirst();

        List<ProductDto> productDtos = Collections.emptyList();
        if (maybeFeatureFlag.isPresent()) {
            Map.Entry<String, AppConfig.FeatureFlag> featureFlag = maybeFeatureFlag.get();
            if ("featureA".equals(featureFlag.getKey())) {
                productDtos = buildFeatureAProducts();
            } else if ("featureB".equals(featureFlag.getKey())) {
                productDtos = buildFeatureBProducts();
            }
            logBetaTestingInfo(featureFlag);
            logRolloutInfo(featureFlag);
        }

        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, productDtos.size());
        return productDtos.subList(startIndex, endIndex);
    }

    private List<ProductDto> buildFeatureAProducts() {
        return List.of(
                ProductDto.builder()
                          .id(1L)
                          .productName("Type A - LG TV")
                          .productPrice(3800.50)
                          .build(),
                ProductDto.builder()
                          .id(2L)
                          .productName("Type A - Samsung TV")
                          .productPrice(3500.50)
                          .build(),
                ProductDto.builder()
                          .id(3L)
                          .productName("Type A - Philips TV")
                          .productPrice(3000.50)
                          .build(),
                ProductDto.builder()
                          .id(4L)
                          .productName("Type A - Sony TV")
                          .productPrice(2800)
                          .build(),
                ProductDto.builder()
                          .id(5L)
                          .productName("Type A - Sharp TV")
                          .productPrice(2500)
                          .build());
    }

    private List<ProductDto> buildFeatureBProducts() {
        return List.of(
                ProductDto.builder()
                          .id(6L)
                          .productName("Type B - LG TV")
                          .productPrice(2800.50)
                          .build(),
                ProductDto.builder()
                          .id(7L)
                          .productName("Type B - Samsung TV")
                          .productPrice(2500.50)
                          .build(),
                ProductDto.builder()
                          .id(8L)
                          .productName("Type B - Philips TV")
                          .productPrice(2000.50)
                          .build(),
                ProductDto.builder()
                          .id(9L)
                          .productName("Type B - Sony TV")
                          .productPrice(1800)
                          .build(),
                ProductDto.builder()
                          .id(10L)
                          .productName("Type B - Sharp TV")
                          .productPrice(1500)
                          .build());
    }

    private void logBetaTestingInfo(Map.Entry<String, AppConfig.FeatureFlag> featureFlag) {
        AppConfig.BetaTesting betaTesting = Optional.ofNullable(featureFlag.getValue())
                                                    .map(AppConfig.FeatureFlag::getBetaTesting)
                                                    .orElse(null);
        if (Objects.nonNull(betaTesting)) {
            boolean isBetaTestingEnabled = betaTesting.isEnabled();
            if (isBetaTestingEnabled) {
                String format = String.format("Testers: %s", betaTesting.getTesters());
                LOG.info(format);
            }
        }
    }

    private void logRolloutInfo(Map.Entry<String, AppConfig.FeatureFlag> featureFlag) {
        AppConfig.Rollout rollout = Optional.ofNullable(featureFlag.getValue())
                                            .map(AppConfig.FeatureFlag::getRollout)
                                            .orElse(null);
        if (Objects.nonNull(rollout)) {
            String format = String.format("Rollout strategy: %s, percentage: %d", rollout.getStrategy(), rollout.getPercentage());
            LOG.info(format);
        }
    }
}
