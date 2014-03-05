package com.github.apljax.resource;

import com.github.apljax.util.Enum.RequestType;
import com.impetus.annovention.util.MethodParameter;

public class JavaMethodParameter {

	private JavaMethod resourceMethod = null;
	private MethodParameter methodParameter = null;
	private Integer index = null;
	private String name = null;
	private RequestType type = null; // JAX-RS request type
	private String defaultValue = "";
	private String comment = null;
	private String javaType = null;

	public JavaMethodParameter(JavaMethod met, Integer index) {
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

	public String getJavaType() {
		return javaType;
	}

	public MethodParameter getMethodParameter() {
		return methodParameter;
	}


	public String getName() {
		return name;
	}

	public JavaMethod getResourceMethod() {
		return resourceMethod;
	}

	public RequestType getType() {
		return type;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public JavaMethodParameter setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public JavaMethodParameter setIndex(Integer index) {
		this.index = index;
		return this;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public void setMethodParameter(MethodParameter methodParameter) {
		this.methodParameter = methodParameter;
	}

	public JavaMethodParameter setName(String name) {
		this.name = name;
		return this;
	}

	public JavaMethodParameter setType(RequestType type) {
		this.type = type;
		return this;
	}

}
