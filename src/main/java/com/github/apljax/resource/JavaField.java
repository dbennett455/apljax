package com.github.apljax.resource;

import com.github.apljax.util.Enum.RequestType;

import javassist.bytecode.FieldInfo;

public class JavaField {

	private JavaClass myClass=null;
	private FieldInfo fieldInfo=null;
	private String name=null;
	private String javaType = null;  // java data type
	private RequestType type=null;
	private String defaultValue=null;
	private String comment=null;

	public JavaField(JavaClass cls, FieldInfo fld) {
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

	public JavaClass getMyClass() {
		return myClass;
	}

	public String getName() {
		return name;
	}

	public RequestType getRequestType() {
		return type;
	}

	public JavaField setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public JavaField setJavaType(String javaType) {
		this.javaType = javaType;
		return this;
	}

	public JavaField setName(String name) {
		this.name = name;
		return this;
	}

	public JavaField setRequestType(RequestType type) {
		this.type = type;
		return this;
	}

}
