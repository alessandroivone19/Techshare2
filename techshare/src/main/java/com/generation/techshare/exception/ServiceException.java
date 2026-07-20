package com.generation.techshare.exception;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceException(String message) {
		super(message);
	
	}

	public Map<String,String> toMap(String operation)
	{
		Map<String,String> errorData = new LinkedHashMap<String,String>();
		errorData.put("cause", this.getMessage());
		errorData.put("operation", operation);
		
		return errorData;
	}
	
	
	
}

