package com.github.apljax.util;

import java.util.List;

public class PathTools {

	String path="";
	List<String> aliases=null;

	public PathTools(String path) {
		super();
		setPath(path);
	}

	public String getPath() {
		return path;
	}

	public PathTools setPath(String pth) {
		if (pth != null) {
			// trim
			this.path = pth.replaceAll("(^[\\r\\n\\s\\/])|([\\r\\n\\s\\/]$)","");
		}
		// check for alias 
		
		
		


		this.path = path;
		return this;
	}



}
