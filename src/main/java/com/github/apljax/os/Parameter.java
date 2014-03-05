package com.github.apljax.os;

import java.util.TreeMap;

@SuppressWarnings("serial")
public class Parameter extends TreeMap<String,String> {

	public Parameter assignComment(String comment) {
		if (comment != null)
			put("comment",comment);
		return this;
	}
	public Parameter assignDataType(String dataType) {
		if (dataType != null)
			put("dataType",dataType);
		return this;
	}
	public Parameter assignDefaultValue(String defaultValue) {
		if (defaultValue != null)
			put("defaultValue",defaultValue);
		return this;
	}
	public Parameter assignName(String name) {
		if (name != null)
			put("name",name);
		return this;
	}
	public Parameter assignRequestType(String requestType) {
		if (requestType != null)
			put("requestType",requestType);
		return this;
	}
	public String retrieveComment() {
		return get("comment");
	}
	public String retrieveDataType() {
		return get("dataType");
	}
	public String retrieveDefaultValue() {
		return get("defaultValue");
	}
	public String retrieveName() {
		return get("name");
	}
	public String retrieveRequestType() {
		return get("requestType");
	}

}
