package com.cognixia.jump.project.one;

public class YesNoException extends Exception {
	
	private static final long serialVersionUID = 9098578360011113179L;

	public YesNoException(){
		super("Invalid entry! Enter Y or N!");
	}

}
