package com.github.apljax.resource;

import java.util.TreeMap;

import javassist.bytecode.ClassFile;

public class JavaResources {

	TreeMap<String,JavaClass> resources=null;
	String rootUrl="";

	public JavaResources() {
		resources=new TreeMap<String, JavaClass>();
	}

	/**
	 * get or create a JavaClass for a given path
	 *
	 * @param path
	 * @return a JavaClass
	 */
	public JavaClass getResourceClass(ClassFile cls) {
		String key=cls.getName();
		JavaClass ret=this.resources.get(key);
		if (ret == null) {
			ret=newResourceClass(cls);
			this.resources.put(key, ret);
		}
		return ret;
	}

	public TreeMap<String, JavaClass> getResources() {
		return resources;
	}

	public String getRootUrl() {
		return rootUrl;
	}

	/**
	 * This method returns a new JavaClass so you can overload
	 *
	 * @param cf
	 * @param path
	 * @return
	 */
	public JavaClass newResourceClass(ClassFile cf) {
		return new JavaClass(this,cf);
	}

	public JavaResources setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
		return this;
	}

}
