package com.microservices.product.controller;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.product.entity.request.ProductCategoryRequest;
import com.microservices.product.entity.vm.ManagerViewModel;
import com.microservices.product.entity.vm.ProductCategoryViewModel;
import com.microservices.product.manager.ProductCategoryManager;
import com.microservices.product.util.Utils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "ProductCategory", description = "Operations pertaining to ProductCategory")
@RequestMapping("/product-category")
public class ProductCategoryController {
	private static final Logger logger = LogManager.getLogger(ProductCategoryController.class);
	
	@Autowired
    private ProductCategoryManager productCategoryManager;

	private long hashing;

	private void setCurrentHashing() {
		this.hashing = new Date().getTime();
	}
	
    @Operation(summary = "Get All ProductCategory", description = "Get All ProductCategory", tags = "ProductCategory")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
	@GetMapping("/")
	public ResponseEntity<ManagerViewModel<List<ProductCategoryViewModel>>> getAllProductCategorys(
			@Parameter(description = "Page")
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@Parameter(description = "Size")
			@RequestParam(value = "size", required = false, defaultValue = "10") int size,
			@Parameter(description = "Order By")
			@RequestParam(value = "orderby", required = false, defaultValue = "id") String orderby){
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Get All ProductCategory");
		ManagerViewModel<List<ProductCategoryViewModel>> mvm = productCategoryManager.getAllProductCategory(hashing, page, size, orderby);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<List<ProductCategoryViewModel>>>(mvm, HttpStatus.OK);
	}
    
    @Operation(summary = "Get ProductCategory By Id", description = "Get ProductCategory By Id", tags = "ProductCategory")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<ManagerViewModel<List<ProductCategoryViewModel>>> getProductCategoryById(
    		@Parameter(description = "Id Value", required = true) @PathVariable("id") int id) {
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Get ProductCategory By " + id);
		ManagerViewModel<List<ProductCategoryViewModel>> mvm = productCategoryManager.getProductCategoryById(hashing, id);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<List<ProductCategoryViewModel>>>(mvm, HttpStatus.OK);
    }
    
    @Operation(summary = "Create ProductCategory", description = "Create ProductCategory", tags = "ProductCategory")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
    @PostMapping("/")
    public ResponseEntity<ManagerViewModel<ProductCategoryViewModel>> createProductCategory(@RequestBody ProductCategoryRequest request) {
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Create ProductCategory");
		ManagerViewModel<ProductCategoryViewModel> mvm = productCategoryManager.insertProductCategory(hashing, request);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<ProductCategoryViewModel>>(mvm, HttpStatus.OK);
    }
    
    @Operation(summary = "Update ProductCategory", description = "Update ProductCategory", tags = "ProductCategory")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
    @PatchMapping("/update")
    public ResponseEntity<ManagerViewModel<ProductCategoryViewModel>> updateProductCategory(@RequestBody ProductCategoryRequest request) {
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Update ProductCategory");
		ManagerViewModel<ProductCategoryViewModel> mvm = productCategoryManager.updateProductCategory(hashing, request);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<ProductCategoryViewModel>>(mvm, HttpStatus.OK);
    }
    
    @Operation(summary = "Delete ProductCategory", description = "Delete ProductCategory", tags = "ProductCategory")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ManagerViewModel<ProductCategoryViewModel>> deleteProductCategoryById(
    		@Parameter(description = "Id Value", required = true) @PathVariable("id") int id) {
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Delete ProductCategory By Id " + id);
		ManagerViewModel<ProductCategoryViewModel> mvm = productCategoryManager.deleteProductCategoryById(hashing, id);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<ProductCategoryViewModel>>(mvm, HttpStatus.OK);
    }
}

