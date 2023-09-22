package com.microservices.product.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.microservices.product.entity.request.ProductRequest;
import com.microservices.product.entity.vm.ManagerViewModel;
import com.microservices.product.entity.vm.ProductViewModel;
import com.microservices.product.manager.ProductManager;
import com.microservices.product.util.Utils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "Product", description = "Operations pertaining to Product")
@RequestMapping("/product")
public class ProductController {
	private static final Logger logger = LogManager.getLogger(ProductController.class);
	
	@Autowired
    private ProductManager productManager;

	private long hashing;

	private void setCurrentHashing() {
		this.hashing = new Date().getTime();
	}
	
    @Operation(summary = "Get All Product", description = "Get All Product", tags = "Product")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
	@GetMapping("/")
	public ResponseEntity<ManagerViewModel<List<ProductViewModel>>> getAllProducts(
			@Parameter(description = "Page")
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@Parameter(description = "Size")
			@RequestParam(value = "size", required = false, defaultValue = "10") int size,
			@Parameter(description = "Order By")
			@RequestParam(value = "orderby", required = false, defaultValue = "id") String orderby){
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Get All Product");
		ManagerViewModel<List<ProductViewModel>> mvm = productManager.getAllProduct(hashing, page, size, orderby);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<List<ProductViewModel>>>(mvm, HttpStatus.OK);
	}
    
    @Operation(summary = "Get Product By Id", description = "Get Product By Id", tags = "Product")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<ManagerViewModel<List<ProductViewModel>>> getProductById(
    		@Parameter(description = "Id Value", required = true) @PathVariable("id") int id) {
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Get Product By " + id);
		ManagerViewModel<List<ProductViewModel>> mvm = productManager.getProductById(hashing, id);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<List<ProductViewModel>>>(mvm, HttpStatus.OK);
    }
    
    @Operation(summary = "Create Product", description = "Create Product", tags = "Product")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
    @PostMapping("/")
    public ResponseEntity<ManagerViewModel<ProductViewModel>> createProduct(@RequestBody ProductRequest request) {
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Create Product");
		ManagerViewModel<ProductViewModel> mvm = productManager.insertProduct(hashing, request);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<ProductViewModel>>(mvm, HttpStatus.OK);
    }
    
    @Operation(summary = "Update Product", description = "Update Product", tags = "Product")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
    @PatchMapping("/update")
    public ResponseEntity<ManagerViewModel<ProductViewModel>> updateProduct(@RequestBody ProductRequest request) {
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Update Product");
		ManagerViewModel<ProductViewModel> mvm = productManager.updateProduct(hashing, request);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<ProductViewModel>>(mvm, HttpStatus.OK);
    }
    
    @Operation(summary = "Delete Product", description = "Delete Product", tags = "Product")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ManagerViewModel<ProductViewModel>> deleteProductById(
    		@Parameter(description = "Id Value", required = true) @PathVariable("id") int id) {
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Delete Product By Id " + id);
		ManagerViewModel<ProductViewModel> mvm = productManager.deleteProductById(hashing, id);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<ProductViewModel>>(mvm, HttpStatus.OK);
    }
}
