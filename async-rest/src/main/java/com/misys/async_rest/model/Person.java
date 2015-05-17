package com.misys.async_rest.model;

import java.util.HashMap;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(("id"))
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

	private String id;
	@NotNull(message="name is required field")
	private String name;
	
	private HashMap<String, Object> extras = new HashMap<String, Object>();
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonAnyGetter
	public HashMap<String, Object> getExtras() {
		return extras;
	}
	
	@JsonAnyGetter
	public void setExtras(String key, Object value) {
		this.extras.put(key, value);
	}
	
}
