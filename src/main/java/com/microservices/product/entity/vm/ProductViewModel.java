package com.microservices.product.entity.vm;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductViewModel {
	@JsonProperty("id")
    private Integer id;
	@JsonProperty("name")
    private String name;
	@JsonProperty("description")
    private String description;
	@JsonProperty("price")
    private Integer price;
	@JsonProperty("product_category_id")
    private Integer productCategoryId;
}
