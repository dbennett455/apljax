package com.github.apljax.resource;

import java.util.TreeMap;

import com.github.apljax.util.Enum.RequestMethod;
import com.github.apljax.util.PathBuilder;
import com.github.apljax.util.ResourceIdExtractor;

import javassist.bytecode.MethodInfo;


public class JavaMethod {

	private JavaClass myClass=null;
	private MethodInfo methodInfo=null;
	private RequestMethod requestMethod=RequestMethod.GET;
	private String path=null;
	private String[] consumes=null;
	private String[] produces=null;
	private String comment=null;
	private String definedResourceId=null;
	private String defaultPath=null;
	private TreeMap<Integer, JavaMethodParameter> resourceParameters=null;
	private PathBuilder pathBuilder=null;
	private ResourceIdExtractor resourceIdExtractor=null;

	public JavaMethod(JavaClass cls, MethodInfo met) {
		super();
		this.myClass = cls;
		this.methodInfo = met;
		this.resourceParameters=new TreeMap<Integer, JavaMethodParameter>();
	}

	public String getComment() {
		return comment;
	}

	/**
	 * Delegate to class if not defined
	 *
	 * @return String comment
	 */
	public String[] getConsumes() {
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

	public JavaClass getMyClass() {
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

	public String[] getProduces() {
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
	 * get or create a JavaMethodParameter for a given name
	 *
	 * @param path
	 * @return a JavaMethod
	 */
	public JavaMethodParameter getResourceParameter(Integer index) {
		JavaMethodParameter ret=this.resourceParameters.get(index);
		if (ret == null) {
			ret=new JavaMethodParameter(this,index);
			this.resourceParameters.put(index, ret);
		}
		return ret;
	}

	public TreeMap<Integer, JavaMethodParameter> getResourceParameters() {
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

	public JavaMethod setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public JavaMethod setConsumes(String[] consumes) {
		this.consumes = consumes;
		return this;
	}

	public JavaMethod setDefaultPath(String defaultPath) {
		this.defaultPath = defaultPath;
		return this;
	}

	public JavaMethod setDefinedResourceId(String resourceId) {
		this.definedResourceId = resourceId;
		return this;
	}

	public JavaMethod setPath(String path) {
		this.path = path;
		return this;
	}

	public JavaMethod setProduces(String[] produces) {
		this.produces = produces;
		return this;
	}

	public JavaMethod setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
		return this;
	}

}
