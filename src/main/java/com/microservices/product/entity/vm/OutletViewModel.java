package com.microservices.product.entity.vm;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OutletViewModel {
	@JsonProperty("id")
    private Integer id;
	@JsonProperty("name")
    private String name;
	@JsonProperty("total_category")
    private Integer totalCategory;
	@JsonProperty("product_categories")
    private List<ProductCategoryViewModel> productCategories;
}
