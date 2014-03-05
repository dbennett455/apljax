package com.github.apljax.os;

import java.util.TreeMap;

@SuppressWarnings("serial")
public class Resource extends TreeMap<String, Object> {

	public Resource assignComment(String comment) {
		if (comment != null)
			put("comment",comment);
		return this;
	}

	public String retrieveComment() {
		return (String)get("comment");
	}

	public Path retrievePath(String uri) {
		Path ret=retrievePaths().get(uri);
		if (ret == null) {
			ret=new Path();
			retrievePaths().put(uri, ret);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public TreeMap<String, Path> retrievePaths() {
		if (get("path") == null)
			put("path",new TreeMap<String,Path>());
		return (TreeMap<String,Path>)get("path");
	}

}
