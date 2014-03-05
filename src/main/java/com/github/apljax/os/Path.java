package com.github.apljax.os;

import java.util.TreeMap;

@SuppressWarnings("serial")
public class Path extends TreeMap<String, Object> {

	public Path assignURI(String uri) {
		if (uri != null)
			put("uri",uri);
		return this;
	}

	public Method retrieveMethod(String methodName) {
		Method ret=retrieveMethods().get(methodName);
		if (ret == null) {
			ret=new Method();
			retrieveMethods().put(methodName,ret);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public TreeMap<String, Method> retrieveMethods() {
		if (get("method")==null)
			put("method",new TreeMap<String,Method>());
		return (TreeMap<String,Method>)get("method");
	}

	public String retrieveURI() {
		return (String)get("uri");
	}

}
