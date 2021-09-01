package com.cognixia.jump.project.one;

public class IDExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3894890100607995608L;
	
	public IDExistsException(int id) {
		super("An employee with ID: "+id+" already exists.");
	}

}
