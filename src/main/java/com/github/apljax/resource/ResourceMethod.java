package com.github.apljax.resource;

import java.util.TreeMap;

import com.github.apljax.util.PathBuilder;
import com.github.apljax.util.ResourceIdExtractor;

import javassist.bytecode.MethodInfo;


public class ResourceMethod {

	// JAX-RS http request types
	public enum RequestMethod {GET,POST,PUT,DELETE,HEAD};

	private ResourceClass myClass=null;
	private MethodInfo methodInfo=null;
	private RequestMethod requestMethod=RequestMethod.GET;
	private String path=null;
	private String consumes=null;
	private String produces=null;
	private String comment=null;
	private String definedResourceId=null;
	private String defaultPath=null;
	private TreeMap<Integer, ResourceParameter> resourceParameters=null;
	private PathBuilder pathBuilder=null;
	private ResourceIdExtractor resourceIdExtractor=null;

	public ResourceMethod(ResourceClass cls, MethodInfo met) {
		super();
		this.myClass = cls;
		this.methodInfo = met;
		this.resourceParameters=new TreeMap<Integer, ResourceParameter>();
	}

	public String getComment() {
		return comment;
	}

	/**
	 * Delegate to class if not defined
	 *
	 * @return String comment
	 */
	public String getConsumes() {
		if (consumes == null)
			return(myClass.getConsumes());
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

	public MethodInfo getMethodInfo() {
		return methodInfo;
	}

	public ResourceClass getMyClass() {
		return myClass;
	}

	public String getPath() {
		return path;
	}

	private PathBuilder getPathBuilder() {
		if (pathBuilder == null) {
			pathBuilder = new PathBuilder().
					setRootPath(myClass.getResources().getRootUrl()).
					setClassDefaultPath(myClass.getDefaultPath()).
					setClassPath(myClass.getPath()).
					setMethodDefaultPath(defaultPath).
					setMethodPath(path);
		}
		return pathBuilder;
	}

	public String getProduces() {
		if (produces == null)
			return(myClass.getProduces());
		return produces;
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
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
	 * get or create a ResourceParameter for a given name
	 *
	 * @param path
	 * @return a ResourceMethod
	 */
	public ResourceParameter getResourceParameter(Integer index) {
		ResourceParameter ret=this.resourceParameters.get(index);
		if (ret == null) {
			ret=new ResourceParameter(this,index);
			this.resourceParameters.put(index, ret);
		}
		return ret;
	}

	public TreeMap<Integer, ResourceParameter> getResourceParameters() {
		return resourceParameters;
	}

	/**
	 * Get the relative path to this resource method
	 *
	 * @return
	 */
	public String getResourcePath() {
		return getPathBuilder().buildNoRoot();
	}

	public ResourceMethod setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public ResourceMethod setConsumes(String consumes) {
		this.consumes = consumes;
		return this;
	}

	public ResourceMethod setDefaultPath(String defaultPath) {
		this.defaultPath = defaultPath;
		return this;
	}

	public ResourceMethod setDefinedResourceId(String resourceId) {
		this.definedResourceId = resourceId;
		return this;
	}

	public ResourceMethod setPath(String path) {
		this.path = path;
		return this;
	}

	public ResourceMethod setProduces(String produces) {
		this.produces = produces;
		return this;
	}

	public ResourceMethod setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
		return this;
	}

}
