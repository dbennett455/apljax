package com.github.apljax.resource;

import java.util.TreeMap;

import javassist.bytecode.ClassFile;

public class Resources {

	TreeMap<String,ResourceClass> resources=null;
	String rootUrl="";

	public Resources() {
		resources=new TreeMap<String, ResourceClass>();
	}

	/**
	 * get or create a ResourceClass for a given path
	 *
	 * @param path
	 * @return a ResourceClass
	 */
	public ResourceClass getResourceClass(ClassFile cls) {
		String key=cls.getName();
		ResourceClass ret=this.resources.get(key);
		if (ret == null) {
			ret=newResourceClass(cls);
			this.resources.put(key, ret);
		}
		return ret;
	}

	public TreeMap<String, ResourceClass> getResources() {
		return resources;
	}

	public String getRootUrl() {
		return rootUrl;
	}

	/**
	 * This method returns a new ResourceClass so you can overload
	 *
	 * @param cf
	 * @param path
	 * @return
	 */
	public ResourceClass newResourceClass(ClassFile cf) {
		return new ResourceClass(this,cf);
	}

	public Resources setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
		return this;
	}

}
