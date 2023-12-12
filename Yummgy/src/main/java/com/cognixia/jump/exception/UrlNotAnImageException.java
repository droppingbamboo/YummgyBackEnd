package com.cognixia.jump.exception;

public class UrlNotAnImageException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public UrlNotAnImageException(String url) {
		super(url + " is not a PNG or JPG link.");
	}
}
