package com.llye.externalizedconfig.externalizedconfigyaml.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductDto {
    private Long id;
    private String productName;
    private double productPrice;
}
