package com.misys.async_rest.model;

import javax.validation.constraints.NotNull;

public class Build {

	private String id;
    @NotNull(message = "name is required field")
    private String name;
    
	public String getId() {
		return this.id;
	}
	
	public void setId(String aId) {
		this.id = aId;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String aName) {
		this.name = aName;
	}
}
