package com.github.apljax.resource;

import javassist.bytecode.FieldInfo;

public class ResourceField {

	// JAX-RS parameter types
	public enum RequestType {
		PATH, QUERY, MATRIX, HEADER, COOKIE, FORM
	};

	private ResourceClass myClass=null;
	private FieldInfo fieldInfo=null;
	private String name=null;
	private String javaType = null;  // java data type
	private RequestType type=null;
	private String defaultValue=null;
	private String comment=null;

	public ResourceField(ResourceClass cls, FieldInfo fld) {
		super();
		this.myClass = cls;
		this.fieldInfo = fld;
	}

	public String getComment() {
		return comment;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public FieldInfo getFieldInfo() {
		return fieldInfo;
	}

	public String getJavaType() {
		return javaType;
	}

	public ResourceClass getMyClass() {
		return myClass;
	}

	public String getName() {
		return name;
	}

	public RequestType getRequestType() {
		return type;
	}

	public ResourceField setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public ResourceField setJavaType(String javaType) {
		this.javaType = javaType;
		return this;
	}

	public ResourceField setName(String name) {
		this.name = name;
		return this;
	}

	public ResourceField setRequestType(RequestType type) {
		this.type = type;
		return this;
	}

}
