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

import com.microservices.product.entity.request.OutletRequest;
import com.microservices.product.entity.vm.ManagerViewModel;
import com.microservices.product.entity.vm.OutletViewModel;
import com.microservices.product.manager.OutletManager;
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
@Tag(name = "Outlet", description = "Operations pertaining to Outlet")
@RequestMapping("/outlet")
public class OutletController {
	private static final Logger logger = LogManager.getLogger(OutletController.class);
	
	@Autowired
    private OutletManager outletManager;

	private long hashing;

	private void setCurrentHashing() {
		this.hashing = new Date().getTime();
	}
	
    @Operation(summary = "Get All Outlet", description = "Get All Outlet", tags = "Outlet")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
	@GetMapping("/")
	public ResponseEntity<ManagerViewModel<List<OutletViewModel>>> getAllOutlets(
			@Parameter(description = "Page")
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@Parameter(description = "Size")
			@RequestParam(value = "size", required = false, defaultValue = "10") int size,
			@Parameter(description = "Order By")
			@RequestParam(value = "orderby", required = false, defaultValue = "id") String orderby){
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Get All Outlet");
		ManagerViewModel<List<OutletViewModel>> mvm = outletManager.getAllOutlet(hashing, page, size, orderby);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<List<OutletViewModel>>>(mvm, HttpStatus.OK);
	}
    
    @Operation(summary = "Get Outlet By Id", description = "Get Outlet By Id", tags = "Outlet")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<ManagerViewModel<List<OutletViewModel>>> getOutletById(
    		@Parameter(description = "Id Value", required = true) @PathVariable("id") int id) {
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Get Outlet By " + id);
		ManagerViewModel<List<OutletViewModel>> mvm = outletManager.getOutletById(hashing, id);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<List<OutletViewModel>>>(mvm, HttpStatus.OK);
    }
    
    @Operation(summary = "Create Outlet", description = "Create Outlet", tags = "Outlet")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
    @PostMapping("/")
    public ResponseEntity<ManagerViewModel<OutletViewModel>> createOutlet(@RequestBody OutletRequest request) {
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Create Outlet");
		ManagerViewModel<OutletViewModel> mvm = outletManager.insertOutlet(hashing, request);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<OutletViewModel>>(mvm, HttpStatus.OK);
    }
    
    @Operation(summary = "Update Outlet", description = "Update Outlet", tags = "Outlet")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
    @PatchMapping("/update")
    public ResponseEntity<ManagerViewModel<OutletViewModel>> updateOutlet(@RequestBody OutletRequest request) {
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Update Outlet");
		ManagerViewModel<OutletViewModel> mvm = outletManager.updateOutlet(hashing, request);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<OutletViewModel>>(mvm, HttpStatus.OK);
    }
    
    @Operation(summary = "Delete Outlet", description = "Delete Outlet", tags = "Outlet")
	@ApiResponses(value = {
	           @ApiResponse(responseCode = "200", description = "Successful Operation", 
	                   content = @Content(schema = @Schema(implementation = ManagerViewModel.class)))})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ManagerViewModel<OutletViewModel>> deleteOutletById(
    		@Parameter(description = "Id Value", required = true) @PathVariable("id") int id) {
    	setCurrentHashing();
		long startTime = System.currentTimeMillis();
		logger.info("("+hashing+") Delete Outlet By Id " + id);
		ManagerViewModel<OutletViewModel> mvm = outletManager.deleteOutletById(hashing, id);
		long endTime = System.currentTimeMillis();
		mvm.getInfo().setExecutiontime(Utils.executiontime(startTime, endTime));
		return new ResponseEntity<ManagerViewModel<OutletViewModel>>(mvm, HttpStatus.OK);
    }
}
