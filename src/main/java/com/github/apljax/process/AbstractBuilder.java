package com.github.apljax.process;

import java.util.ArrayList;
import java.util.List;

import com.github.apljax.os.Bowl;
import com.github.apljax.os.Method;
import com.github.apljax.os.Parameter;
import com.github.apljax.os.Path;
import com.github.apljax.os.Resource;
import com.github.apljax.resource.JavaClass;
import com.github.apljax.resource.JavaField;
import com.github.apljax.resource.JavaMethod;
import com.github.apljax.resource.JavaMethodParameter;
import com.github.apljax.resource.JavaResources;
import com.github.apljax.util.Enum;
import com.github.apljax.util.JavaDataToJavascriptData;
import com.github.apljax.util.Enum.RequestMethod;

public abstract class AbstractBuilder {

	private Bowl bowl;
	private static JavaResources javaResources=null;

	/**
	 * Scan the resources if they haven't been
	 * scanned yet.
	 *
	 */
	public AbstractBuilder() {
		if (javaResources == null) {
			javaResources=new Scanner().scan();
		}
		bowl=new Bowl();
	}

	/**
	 * This is for dynamic loading environments like DCEVM or JRebel
	 */
	public static void reset() {
		javaResources=null;
	}

	/**
	 * This filter is defined in the applications implementation of a Builder.
	 * It defines whether or not the principal requester can access the
	 * JAX-RS annotated Java Class.
	 *
	 * Format: package.Class
	 *
	 * @param className class name
	 * @return true if principal requester has access
	 */
	public abstract boolean hasAccessToJavaClass(String className);

	/**
	 * This filter is defined in the applications implementation of a Builder.
	 * It defines whether or not the principal requester can access the
	 * JAX-RS annotated Java Field.
	 *
	 * Format: package.Class.field
	 *
	 * @param fieldName field name
	 * @return true if principal requester has access
	 */
	public abstract boolean hasAccessToJavaField(String fieldName);

	/**
	 * This filter is defined in the applications implementation of a Builder.
	 * It defines whether or not the principal requester can access the
	 * JAX-RS annotated Java Method.
	 *
	 * Format: package.Class.method
	 *
	 * @param methodName method name
	 * @return true if principal requester has access
	 */
	public abstract boolean hasAccessToJavaMethod(String methodName);

	/**
	 * This filter is defined in the applications implementation of a Builder.
	 * It defines whether or not the principal requester can access the
	 * JAX-RS annotated Method parameter.
	 *
	 * Format: name from @xxxParam() definition
	 *
	 * @param resourceId the resourceId this parameter is defined in
	 * @param paramName method parameter
	 * @return true if principal requester has access
	 */
	public abstract boolean hasAccessToParameter(String resourceId, String paramName);

	/**
	 * This filter is defined in the applications implementation of a Builder.
	 * It defines whether or not the principal requester can access the
	 * JAX-RS annotated ResourceId.  The resource id is generally the first
	 * component in the JAX-RS path but can also be defined using the
	 * AplJAX ResourceId annotation.
	 *
	 * @param resourceId
	 * @return true if principal requester has access
	 */
	public abstract boolean hasAccessToResource(String resourceId);


	/**
	 * This filter is defined in the applications implementation of a Builder.
	 * It defines whether or not the principal requester can access the
	 * JAX-RS URI and Method.  The URI is the relative address to the application.
	 * If the method parameter is null then the check is global for the URI compared
	 * to any method.
	 *
	 * @param uri Address URI to check
	 * @param resourceId the defined resourceId of the method
	 * @param method request method (null == all methods)
	 * @return true if principal requester has access
	 */
	public abstract boolean hasAccessToURI(String uri, String resourceId, RequestMethod method);


	/**
	 * This filter is defined in the applications implementation of a Builder.
	 * It defines whether or not the principal requester can produce content
	 * of the media type that a method will consume.  If no media types
	 * can be consumed by the requester then the consumes element will not
	 * be included in the payload.
	 *
	 * @param mediaType media type that a method will consume from a requester
	 * @return true if principal requester can send media/type
	 */
	public abstract boolean canConsumeMediaType(String mediaType);

	/**
	 * This filter is defined in the applications implementation of a Builder.
	 * It defines whether or not the principal requester can receive content
	 * of the media type that a method will produce.  If no media types
	 * can be produced for the requester then the produces element will not
	 * be included in the payload.
	 *
	 * @param mediaType media type that a method can produce for a requester
	 * @return true if principal requester can send media/type
	 */
	public abstract boolean canProduceMediaType(String mediaType);

	/**
	 * This is the main builder method.  It will return a bowl of apljax O's
	 * based on the access criteria defined in the implementation of this
	 * class (hasAccessToxxx() and canXxx() methods.
	 *
	 * @return Bowl of apljax O's
	 */
	public Bowl pour() {
		if (javaResources != null) {
			// add root URL if defined
			if (javaResources.getRootUrl() != null) {
				bowl.assignRootUrl(javaResources.getRootUrl());
			}
			// iterate through our applications resources
			for (String className : javaResources.getResources().keySet()) {
				pourClass(className);
			}
		}
		return bowl;
	}


	/**
	 * Pour a class into the bowl
	 *
	 * @param className
	 */
	protected Resource pourClass(String className) {
		Resource resource=null;
		// if we have access to the java class
		if (hasAccessToJavaClass(className)) {
			JavaClass javaClass=javaResources.getResources().get(className);
			// if we have access to the resource
			if (hasAccessToResource(javaClass.getResourceId())) {
				resource=new Resource();
				bowl.addResource(javaClass.getResourceId(), resource);
				if (javaClass.getComment() != null)
					resource.assignComment(javaClass.getComment());
				// iterate through class methods
				for (String methodName : javaClass.getResourceMethods().keySet()) {
					pourMethod(methodName,javaClass,resource);
				}
			}
		}
		return resource;
	}

	/**
	 * Pour a method into the class
	 *
	 * @param methodName
	 * @param javaClass
	 * @param resource
	 */
	protected Method pourMethod(String methodName, JavaClass javaClass, Resource resource) {
		Method method=null;
		JavaMethod javaMethod=javaClass.getResourceMethods().get(methodName);
		// check that we have access to the method
		if (hasAccessToResource(javaMethod.getResourceId()) &&
				hasAccessToURI(javaMethod.getResourcePath(),
								javaMethod.getResourceId(),
								javaMethod.getRequestMethod())
				) {
			// handle the path
			Path p=resource.retrievePath(javaMethod.getResourcePath());
			p.assignURI(javaMethod.getFullPath());
			// get the method
			method=p.retrieveMethod(Enum.requestMethodToString(javaMethod.getRequestMethod()));
			method.assignComment(javaMethod.getComment());
			// consumes
			String[] consumes=getMediaTypes(javaMethod.getConsumes(), 'C');
			if (consumes != null && consumes.length > 0)
				method.assignConsumes(consumes);
			// produces
			String[] produces=getMediaTypes(javaMethod.getProduces(), 'P');
			if (produces != null && produces.length > 0)
				method.assignProduces(produces);
			// pour fields
			for (String fieldName : javaClass.getResourceFields().keySet()) {
				pourField(fieldName,javaClass,javaMethod,method);
			}
			// pour method parameters
			for (Integer parameterIndex : javaMethod.getResourceParameters().keySet()) {
				pourMethodParameter(parameterIndex,javaClass,javaMethod,method);
			}
		}
		return method;
	}

	/**
	 * pour method parameters into java method
	 *
	 */
	protected Parameter pourMethodParameter(Integer parameterIndex,JavaClass javaClass,JavaMethod javaMethod,Method method) {
		Parameter parameter=null;
		JavaMethodParameter javaMethodParameter=javaMethod.getResourceParameters().get(parameterIndex);
		if (hasAccessToParameter(javaClass.getResourceId(),javaMethodParameter.getName())) {
			// get/create the field
			parameter=method.retrieveParameter(javaMethodParameter.getName());
			parameter.assignName(javaMethodParameter.getName());
			parameter.assignComment(javaMethodParameter.getComment());
			parameter.assignDefaultValue(javaMethodParameter.getDefaultValue());
			parameter.assignRequestType(Enum.requestTypeToString(javaMethodParameter.getType()));
			parameter.assignDataType(JavaDataToJavascriptData.convert(javaMethodParameter.getJavaType()));
		}
		return parameter;
	}

	/**
	 * pour field parameters into java method
	 *
	 */
	protected Parameter pourField(String fieldName,JavaClass javaClass,JavaMethod javaMethod,Method method) {
		Parameter parameter=null;
		JavaField javaField=javaClass.getResourceFields().get(fieldName);
		// check that we have access
		if (hasAccessToJavaField(fieldName) &&
				hasAccessToParameter(javaClass.getResourceId(),javaField.getName())) {
			// get/create the field
			parameter=method.retrieveParameter(javaField.getName());
			parameter.assignName(javaField.getName());
			parameter.assignComment(javaField.getComment());
			parameter.assignDefaultValue(javaField.getDefaultValue());
			parameter.assignRequestType(Enum.requestTypeToString(javaField.getRequestType()));
			parameter.assignDataType(JavaDataToJavascriptData.convert(javaField.getJavaType()));
		}
		return parameter;
	}

	/**
	 * Return list of media types that the principal requester can
	 * can send (consume) and retrieve (produce)
	 *
	 * @param mediaTypes
	 * @param cOrp  'C' or 'P' only.
	 * @return
	 */
	protected String[] getMediaTypes(String[] mediaTypes, char cOrp) {
		List<String>ret =new ArrayList<String>();
		for (String mediaType : mediaTypes) {
			boolean can;
			switch (cOrp) {
			case 'C':
				can=canConsumeMediaType(mediaType);
				break;
			case 'P':
				can=canProduceMediaType(mediaType);
				break;
			default:
				// lazy, not creating a new exception for this edge case
				throw(new IllegalArgumentException("Only 'C' or 'P' allowed for cOrp."));
			}
			if (can)
				ret.add(mediaType);
		}
		return (ret.toArray(new String[ret.size()]));
	}


}
