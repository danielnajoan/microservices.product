package com.microservices.product.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OutletRequest {
	@JsonProperty("id")
    private Integer id;
	@JsonProperty("name")
    private String name;
	@JsonProperty("total_category")
    private Integer totalCategory;
}