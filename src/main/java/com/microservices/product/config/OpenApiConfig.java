package com.microservices.product.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig implements WebMvcConfigurer {
	
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
      registry.addRedirectViewController("/", "/swagger-ui.html");
      registry.addRedirectViewController("/swagger-ui", "/swagger-ui.html");
    }
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                		.title("PRODUCT REST API")
                		.description("REST API for Product")
                		.version("1.0.0")
                		.termsOfService("Terms of service")
                		.license(new License()
                				.name("Apache License Version 2.0")
                				.url("https://www.apache.org/licenses/LICENSE-2.0"))
                		.contact(new Contact()
                				.name("Daniel Stephanus Najoan")
                				.email("danielnajoan@gmail.com"))
                );
    }
    
    @Bean
    GroupedOpenApi allApis() {
      return GroupedOpenApi.builder().group("*").pathsToMatch("/**").build();
    }
    
    @Bean
    GroupedOpenApi outletListApis() {
      return GroupedOpenApi.builder().group("Outlet").pathsToMatch("/**/outlet/**").build();
    }
    
    @Bean
    GroupedOpenApi productCategoryListApis() {
      return GroupedOpenApi.builder().group("Product Category").pathsToMatch("/**/product-category/**").build();
    }
    
    @Bean
    GroupedOpenApi productListApis() {
      return GroupedOpenApi.builder().group("Product").pathsToMatch("/**/product/**").build();
    }
}
