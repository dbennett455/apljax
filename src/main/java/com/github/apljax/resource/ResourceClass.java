package com.github.apljax.resource;

import java.util.TreeMap;
import java.util.TreeSet;

import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;

/**
 * This class represents a JAX-RS resource class
 *
 * @author dbennett455@gmail.com
 *
 */
public class ResourceClass {

	private Resources resources=null;
	private ClassFile classFile=null;
	private String path=null;
	private String produces=null;
	private String consumes=null;
	private String comment=null;
	private String resourceId=null;
	private String defaultPath=null;
	private TreeMap<String,ResourceField> resourceFields=null;
	private TreeMap<String,ResourceMethod> resourceMethods=null;

	public ResourceClass(Resources r, ClassFile cf) {
		super();
		this.resources = r;
		this.classFile = cf;
		this.resourceFields = new TreeMap<String,ResourceField>();
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

	public String getDefaultPath() {
		return defaultPath;
	}

	public String getFullPath() {
		String ret=null;
		// todo: code it, see util.PathTools
		return ret;

	}

	public String getPath() {
		return path;
	}

	public String getProduces() {
		return produces;
	}

	/**
	 * get or create a ResourceField for a given path
	 *
	 * @param path
	 * @return a ResourceField
	 */
	public ResourceField getResourceField(FieldInfo fld) {
		String key=fld.getName()+fld.getDescriptor();
		ResourceField ret=this.resourceFields.get(key);
		if (ret == null) {
			ret=newResourceField(this,fld);
			this.resourceFields.put(key, ret);
		}
		return ret;
	}

	public TreeMap<String,ResourceField> getResourceFields() {
		return resourceFields;
	}

	public String getResourceId() {
		return resourceId;
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

	public Resources getResources() {
		return resources;
	}


	/**
	 * Create a new Resource field
	 *
	 */
	public ResourceField newResourceField(ResourceClass cls, FieldInfo fld) {
		return new ResourceField(cls,fld);
	}

	/**
	 * Create a new Resource method
	 *
	 */
	public ResourceMethod newResourceMethod(ResourceClass cls, MethodInfo met) {
		return new ResourceMethod(cls,met);
	}

	public ResourceClass setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public ResourceClass setConsumes(String consumes) {
		this.consumes = consumes;
		return this;
	}

	public ResourceClass setDefaultPath(String defaultPath) {
		this.defaultPath = defaultPath;
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

	public ResourceClass setResourceId(String resourceId) {
		this.resourceId = resourceId;
		return this;
	}

	public ResourceClass setResources(Resources resources) {
		this.resources = resources;
		return this;
	}

}
