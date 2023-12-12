package com.cognixia.jump.exception;

public class NoUserGivenException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public NoUserGivenException(String resource) {
		super("No user was given for " + resource + " creation.");
	}
}
