package com.cognixia.jump.exception;
public class TokenExpiredException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TokenExpiredException(String expireTime) {
		super("Token expired at: " + expireTime + ". Please create user again.");
	}
}