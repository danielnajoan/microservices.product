package com.microservices.product.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema
public class ErrorDetail {
    @JsonProperty("status")
	@Schema(example = "200", description = "status code")
	private int status;
    @JsonProperty("message")
	@Schema(example = "Success", description = "response message")
	private String message;
    @JsonProperty("detail_message")
	@Schema(example = "Success get data", description = "detailed response message")
	private Object detailmessage;
    @JsonProperty("execution_time")
	@Schema(example = "00:00:10.523", description = "time execution per request (in format time)")
	private String executiontime;
    @JsonProperty("detail_info")
	@Schema(example = "OK", description = "detail info")
	private Object detailInfo;
}
