package com.microservices.product.util;

import org.springframework.http.HttpStatus;

public class ErrorDetailInfoList {
	// 200 OK
	public ErrorDetail getInfoOk(String msg) {
		ErrorDetail info = new ErrorDetail();
		info.setStatus(HttpStatus.OK.value());
		info.setMessage(HttpStatus.OK.name());
		info.setDetailmessage(msg);
		info.setDetailInfo(HttpStatus.OK);

		return info;
	}

	// 201 Created
	public ErrorDetail getInfoCreated(String msg) {
		ErrorDetail info = new ErrorDetail();
		info.setStatus(HttpStatus.CREATED.value());
		info.setMessage(HttpStatus.CREATED.name());
		info.setDetailmessage(msg);
		info.setDetailInfo(HttpStatus.CREATED);

		return info;
	}

	// 204 No Content
	public ErrorDetail getInfoNoContent() {
		ErrorDetail info = new ErrorDetail();
		info.setStatus(HttpStatus.NO_CONTENT.value());
		info.setMessage(HttpStatus.NO_CONTENT.name());
		info.setDetailmessage("Data not found!");
		info.setDetailInfo(HttpStatus.NO_CONTENT);

		return info;
	}

	// 204 No Content
	public ErrorDetail getInfoNoContent(String msg) {
		ErrorDetail info = new ErrorDetail();
		info.setStatus(HttpStatus.NO_CONTENT.value());
		info.setMessage(HttpStatus.NO_CONTENT.name());
		info.setDetailmessage(msg);
		info.setDetailInfo(HttpStatus.NO_CONTENT);

		return info;
	}

	// 400 Bad Request
	public ErrorDetail getInfoBadRequest(String msg) {
		ErrorDetail info = new ErrorDetail();
		info.setStatus(HttpStatus.BAD_REQUEST.value());
		info.setMessage(HttpStatus.BAD_REQUEST.name());
		info.setDetailmessage(msg);
		info.setDetailInfo(HttpStatus.BAD_REQUEST);

		return info;
	}
	
	// 401 Unauthorized
	public ErrorDetail getInfoUnauthorized(String msg) {
		ErrorDetail info = new ErrorDetail();
		info.setStatus(HttpStatus.UNAUTHORIZED.value());
		info.setMessage(HttpStatus.UNAUTHORIZED.name());
		info.setDetailmessage(msg);
		info.setDetailInfo(HttpStatus.UNAUTHORIZED);

		return info;
	}
	
	// 403 Forbidden
	public ErrorDetail getInfoForbidden(String msg) {
		ErrorDetail info = new ErrorDetail();
		info.setStatus(HttpStatus.FORBIDDEN.value());
		info.setMessage(HttpStatus.FORBIDDEN.name());
		info.setDetailmessage(msg);
		info.setDetailInfo(HttpStatus.FORBIDDEN);

		return info;
	}

	// 404 Not Found
	public ErrorDetail getInfoNotFound() {
		ErrorDetail info = new ErrorDetail();
		info.setStatus(HttpStatus.NOT_FOUND.value());
		info.setMessage(HttpStatus.NOT_FOUND.name());
		info.setDetailmessage("Data not found!");
		info.setDetailInfo(HttpStatus.NOT_FOUND);

		return info;
	}

	// 404 Not Found
	public ErrorDetail getInfoNotFound(String msg) {
		ErrorDetail info = new ErrorDetail();
		info.setStatus(HttpStatus.NOT_FOUND.value());
		info.setMessage(HttpStatus.NOT_FOUND.name());
		info.setDetailmessage(msg);
		info.setDetailInfo(HttpStatus.NOT_FOUND);

		return info;
	}

	// 408 Request Timeout
	public ErrorDetail getInfoRequestTimeout(String errMsg) {
		ErrorDetail info = new ErrorDetail();
		info.setStatus(HttpStatus.REQUEST_TIMEOUT.value());
		info.setMessage(HttpStatus.REQUEST_TIMEOUT.name());
		info.setDetailmessage(errMsg);
		info.setDetailInfo(HttpStatus.REQUEST_TIMEOUT);

		return info;
	}

	// 409 Conflict
	public ErrorDetail getInfoConflict(String errMsg) {
		ErrorDetail info = new ErrorDetail();
		info.setStatus(HttpStatus.CONFLICT.value());
		info.setMessage(HttpStatus.CONFLICT.name());
		info.setDetailmessage(errMsg);
		info.setDetailInfo(HttpStatus.CONFLICT);

		return info;
	}

	// 500 Internal Server Error
	public ErrorDetail getInfoInternalServerError() {
		ErrorDetail info = new ErrorDetail();
		info.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		info.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.name());
		info.setDetailInfo(HttpStatus.INTERNAL_SERVER_ERROR);

		return info;
	}

	// 501 Not Implemented
	public ErrorDetail getInfoNotImplemented(String errMsg) {
		ErrorDetail info = new ErrorDetail();
		info.setStatus(HttpStatus.NOT_IMPLEMENTED.value());
		info.setMessage(HttpStatus.NOT_IMPLEMENTED.name());
		info.setDetailmessage(errMsg);
		info.setDetailInfo(HttpStatus.NOT_IMPLEMENTED);

		return info;
	}
	
	// 504 Gateway Timeout
	public ErrorDetail getInfoGatewayTimeout(String errMsg) {
		ErrorDetail info = new ErrorDetail();
		info.setStatus(HttpStatus.GATEWAY_TIMEOUT.value());
		info.setMessage(HttpStatus.GATEWAY_TIMEOUT.name());
		info.setDetailmessage(errMsg);
		info.setDetailInfo(HttpStatus.GATEWAY_TIMEOUT);

		return info;
	}
}
