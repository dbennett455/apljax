package com.github.apljax.resource;

import java.util.Arrays;
import java.util.TreeMap;

import javax.ws.rs.core.MediaType;

import com.github.apljax.util.PathBuilder;
import com.github.apljax.util.ResourceIdExtractor;

import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;

/**
 * This class represents a JAX-RS resource class
 *
 * @author dbennett455@gmail.com
 *
 */
public class JavaClass {

	private JavaResources resources=null;
	private ClassFile classFile=null;
	private String path=null;
	private String[] produces=null;
	private String[] consumes=null;
	private String comment=null;
	private String definedResourceId=null;
	private String defaultPath=null;
	private TreeMap<String,JavaField> resourceFields=null;
	private TreeMap<String,JavaMethod> resourceMethods=null;
	private PathBuilder pathBuilder=null;
	private ResourceIdExtractor resourceIdExtractor=null;

	public JavaClass(JavaResources r, ClassFile cf) {
		super();
		this.resources = r;
		this.classFile = cf;
		this.resourceFields = new TreeMap<String,JavaField>();
		this.resourceMethods = new TreeMap<String,JavaMethod>();
	}

	public ClassFile getClassFile() {
		return classFile;
	}

	public String getComment() {
		return comment;
	}

	/**
	 * Delegate to default if not provided
	 *
	 * @return String consumes media type
	 */
	public String[] getConsumes() {
		if (consumes == null)
			return(
				Arrays.asList(MediaType.WILDCARD).toArray(new String[1])
			);
		return consumes;
	}

	public String getDefaultPath() {
		return defaultPath;
	}

	public String getDefinedResourceId() {
		return definedResourceId;
	}

	/**
	 * Return the full path from PathBuilder
	 *
	 * @return full path to this resource as a String
	 */
	public String getFullPath() {
		return getPathBuilder().build();
	}

	public String getPath() {
		return path;
	}

	public PathBuilder getPathBuilder() {
		if (pathBuilder == null) {
			pathBuilder=new PathBuilder().
					setRootPath(resources.getRootUrl()).
					setClassDefaultPath(defaultPath).
					setClassPath(path);
		}
		return pathBuilder;
	}

	public String[] getProduces() {
		if (produces == null)
			return(
					Arrays.asList(MediaType.WILDCARD).toArray(new String[1])
				);
		return produces;
	}

	/**
	 * get or create a JavaField for a given path
	 *
	 * @param path
	 * @return a JavaField
	 */
	public JavaField getResourceField(FieldInfo fld) {
		String key=fld.getName()+fld.getDescriptor();
		JavaField ret=this.resourceFields.get(key);
		if (ret == null) {
			ret=newResourceField(this,fld);
			this.resourceFields.put(key, ret);
		}
		return ret;
	}

	public TreeMap<String,JavaField> getResourceFields() {
		return resourceFields;
	}

	public String getResourceId() {
		return getResourceIdExtractor().extract();
	}

	private ResourceIdExtractor getResourceIdExtractor() {
		if (resourceIdExtractor == null) {
			resourceIdExtractor=new ResourceIdExtractor(
					getDefinedResourceId(),
					getPathBuilder()
				);
		}
		return resourceIdExtractor;
	}

	/**
	 * get or create a JavaMethod for a given path
	 *
	 * @param path
	 * @return a JavaMethod
	 */
	public JavaMethod getResourceMethod(MethodInfo met) {
		String key=met.getName()+met.getDescriptor();
		JavaMethod ret=this.resourceMethods.get(key);
		if (ret == null) {
			ret=newResourceMethod(this,met);
			this.resourceMethods.put(key, ret);
		}
		return ret;
	}

	public TreeMap<String,JavaMethod> getResourceMethods() {
		return resourceMethods;
	}

	/**
	 * App relative path
	 *
	 * @return String path
	 */
	public String getResourcePath() {
		return getPathBuilder().buildNoRoot();
	}


	public JavaResources getResources() {
		return resources;
	}

	/**
	 * Create a new Resource field
	 *
	 */
	public JavaField newResourceField(JavaClass cls, FieldInfo fld) {
		return new JavaField(cls,fld);
	}

	/**
	 * Create a new Resource method
	 *
	 */
	public JavaMethod newResourceMethod(JavaClass cls, MethodInfo met) {
		return new JavaMethod(cls,met);
	}

	public JavaClass setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public JavaClass setConsumes(String[] consumes) {
		this.consumes = consumes;
		return this;
	}

	public JavaClass setDefaultPath(String defaultPath) {
		this.defaultPath = defaultPath;
		return this;
	}

	public JavaClass setDefinedResourceId(String resourceId) {
		this.definedResourceId = resourceId;
		return this;
	}

	public JavaClass setPath(String path) {
		this.path = path;
		return this;
	}

	public JavaClass setProduces(String[] produces) {
		this.produces = produces;
		return this;
	}

	public JavaClass setResources(JavaResources resources) {
		this.resources = resources;
		return this;
	}

}
