package com.spring.security.demo.vo;

public class AuthorizationResponse {

	private int status;
	private String message;

	public AuthorizationResponse(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "ApiResponse [statusCode=" + status + ", message=" + message + "]";
	}

}