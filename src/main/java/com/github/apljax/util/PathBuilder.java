package com.github.apljax.util;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.apljax.Scanner;

public class PathBuilder extends Object {

    private final static Logger log=LoggerFactory.getLogger(PathBuilder.class);
	private static final Pattern trimPath=Pattern.compile("(^[/\\s\"]+)|([/\\s\"]+$)");
	private static final Pattern deQuote=Pattern.compile("(^[\\s\"]+)|([\\s\"]+$)");

	/**
	 * Remove quotes from an annotation value
	 *
	 * @param val annotation value
	 * @return String dequoted
	 */
	public static String deQuote(String val) {
		String ret=null;
		if (val != null) {
			ret = deQuote.matcher(val).replaceAll("");
		}
		return ret;
	}
	/**
	 * Remove spaces, quotes and slashes from a spec
	 *
	 * @param pth the path component from an annotation
	 * @return cleaned string
	 */
	public static String fixPath(String pth) {
		String ret=null;
		if (pth != null) {
			ret = trimPath.matcher(pth).replaceAll("");
		}
		return ret;
	}
	String rootPath=null;
	String classDefaultPath=null;
	String classPath=null;

	String methodDefaultPath=null;

	String methodPath=null;

	public PathBuilder() {
		super();
	}

	/**
	 * Build full path
	 *
	 * @return String full path
	 */
	public String build() {
		StringBuffer ret=new StringBuffer();
		// root path
		if (rootPath != null)
			ret.append(rootPath);
		// buildNoRoot
		String remainder=this.buildNoRoot();
		if (remainder != null && remainder.length() > 0) {
			ret.append('/');
			ret.append(remainder);
		}
		return ret.toString();
	}

	/**
	 * Build without the root component
	 *
	 * @return full path without root
	 */
	public String buildNoRoot() {
		StringBuffer ret=new StringBuffer();
		// class path
		if (classDefaultPath != null && classDefaultPath.length() > 0) {
			ret.append(classDefaultPath);
		} else if (classPath != null && classPath.length() > 0) {
			if (ret.length() > 0) ret.append('/');
			ret.append(classPath);
		}
		// method path
		if (methodDefaultPath != null && methodDefaultPath.length() > 0) {
			if (ret.length() > 0) ret.append('/');
			ret.append(methodDefaultPath);
		} else if (methodPath != null && methodPath.length() > 0) {
			if (ret.length() > 0) ret.append('/');
			ret.append(methodPath);
		}
		return ret.toString();
	}

	public String getClassDefaultPath() {
		return classDefaultPath;
	}

	public String getClassPath() {
		return classPath;
	}

	public String getMethodDefaultPath() {
		return methodDefaultPath;
	}

	public String getMethodPath() {
		return methodPath;
	}

	public String getRootPath() {
		return rootPath;
	}

	public PathBuilder setClassDefaultPath(String classDefaultPath) {
		this.classDefaultPath = fixPath(classDefaultPath);
		return this;
	}

	public PathBuilder setClassPath(String classPath) {
		this.classPath = fixPath(classPath);
		return this;
	}

	public PathBuilder setMethodDefaultPath(String methodDefaultPath) {
		this.methodDefaultPath = fixPath(methodDefaultPath);
		return this;
	}

	public PathBuilder setMethodPath(String methodPath) {
		this.methodPath = fixPath(methodPath);
		return this;
	}

	public PathBuilder setRootPath(String rootPath) {
		this.rootPath = fixPath(rootPath);
		return this;
	}

}
