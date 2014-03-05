package com.github.apljax.os;

import java.util.TreeMap;

@SuppressWarnings("serial")
public class Method extends TreeMap<String, Object> {

	public Method assignComment(String comment) {
		if (comment != null)
			put("comment",comment);
		return this;
	}


	public Method assignConsumes(String[] consumes) {
		if (consumes != null && consumes.length > 0)
			put("consumes",consumes);
		return this;
	}

	public Method assignProduces(String[] produces) {
		if (produces != null && produces.length > 0)
			put("produces",produces);
		return this;
	}

	public String retrieveComment() {
		return (String)get("comment");
	}

	public String[] retrieveConsumes() {
		return (String[])get("consumes");
	}

	public Parameter retrieveParameter(String parameterName) {
		Parameter ret=retrieveParameters().get(parameterName);
		if (ret == null) {
			ret=new Parameter();
			retrieveParameters().put(parameterName,ret);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public TreeMap<String, Parameter> retrieveParameters() {
		if (get("parameter")==null)
			put("parameter",new TreeMap<String,Parameter>());
		return (TreeMap<String,Parameter>)get("parameter");
	}

	public String[] retrieveProduces() {
		return (String[])get("produces");
	}

}
