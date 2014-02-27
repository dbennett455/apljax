package com.bensoft.apljax.os;

import java.util.TreeMap;
import java.util.TreeSet;

import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;

/**
 * This class represents a JAX-RS resource class
 *
 * @author dbennett455@gmail.com
 *
 */
public class ResourceClass {

	private ClassFile classFile;
	private String path=null;
	private String produces=null;
	private String consumes=null;
	private String comment=null;
	private TreeMap<String,ResourceMethod> resourceMethods=null;

	public ResourceClass(ClassFile cf) {
		super();
		this.classFile = cf;
		this.resourceMethods = new TreeMap<String,ResourceMethod>();
	}

	public ClassFile getClassFile() {
		return classFile;
	}

	public String getComment() {
		return comment;
	}

	public String getConsumes() {
		return consumes;
	}

	public String getPath() {
		return path;
	}

	public String getProduces() {
		return produces;
	}

	/**
	 * get or create a ResourceMethod for a given path
	 *
	 * @param path
	 * @return a ResourceMethod
	 */
	public ResourceMethod getResourceMethod(MethodInfo met) {
		String key=met.getName()+met.getDescriptor();
		ResourceMethod ret=this.resourceMethods.get(key);
		if (ret == null) {
			ret=newResourceMethod(this,met);
			this.resourceMethods.put(key, ret);
		}
		return ret;
	}

	public TreeMap<String,ResourceMethod> getResourceMethods() {
		return resourceMethods;
	}


	/**
	 * Create a new Resource method
	 *
	 */
	public ResourceMethod newResourceMethod(ResourceClass cls, MethodInfo met) {
		return new ResourceMethod(cls,met);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ResourceClass setConsumes(String consumes) {
		this.consumes = consumes;
		return this;
	}

	public ResourceClass setPath(String path) {
		this.path = path;
		return this;
	}

	public ResourceClass setProduces(String produces) {
		this.produces = produces;
		return this;
	}

}
