package com.bensoft.apljax.os;

import java.util.TreeMap;

import javassist.bytecode.FieldInfo;
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
	private TreeMap<Integer, ResourceParameter> resourceParameters=null;

	public ResourceMethod(ResourceClass cls, MethodInfo met) {
		super();
		this.myClass = cls;
		this.methodInfo = met;
		this.resourceParameters=new TreeMap<Integer, ResourceParameter>();
	}

	public String getComment() {
		return comment;
	}

	public String getConsumes() {
		return consumes;
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

	public String getProduces() {
		return produces;
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
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

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ResourceMethod setConsumes(String consumes) {
		this.consumes = consumes;
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
