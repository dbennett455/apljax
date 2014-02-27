package com.bensoft.apljax.os;

import com.impetus.annovention.util.MethodParameter;

import javassist.bytecode.FieldInfo;

public class ResourceParameter {

	// JAX-RS parameter types
	public enum ParameterType {
		PATH, QUERY, MATRIX, HEADER, COOKIE, FORM
	};

	private ResourceMethod resourceMethod = null;
	private MethodParameter methodParameter = null;
	private Integer index = null;
	private String name = null;
	private ParameterType type = null;
	private String defaultValue = "";
	private String comment = null;

	public ResourceParameter(ResourceMethod met, Integer index) {
		super();
		this.resourceMethod = met;
		this.index = index;
	}

	public String getComment() {
		return comment;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public Integer getIndex() {
		return index;
	}

	public MethodParameter getMethodParameter() {
		return methodParameter;
	}

	public String getName() {
		return name;
	}

	public ResourceMethod getResourceMethod() {
		return resourceMethod;
	}

	public ParameterType getType() {
		return type;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ResourceParameter setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public ResourceParameter setIndex(Integer index) {
		this.index = index;
		return this;
	}

	public void setMethodParameter(MethodParameter methodParameter) {
		this.methodParameter = methodParameter;
	}

	public ResourceParameter setName(String name) {
		this.name = name;
		return this;
	}

	public ResourceParameter setType(ParameterType type) {
		this.type = type;
		return this;
	}

}
