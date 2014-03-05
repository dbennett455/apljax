package com.github.apljax.process;

import com.github.apljax.util.Enum.RequestMethod;

public class DefaultBuilder extends AbstractBuilder {

	public boolean canConsumeMediaType(String mediaType) {
		return true;
	}

	public boolean canProduceMediaType(String mediaType) {
		return true;
	}

	public boolean hasAccessToJavaClass(String className) {
		return true;
	}

	public boolean hasAccessToJavaField(String fieldName) {
		return true;
	}

	public boolean hasAccessToJavaMethod(String methodName) {
		return true;
	}

	public boolean hasAccessToParameter(String resourceId, String paramName) {
		return true;
	}

	public boolean hasAccessToRequestMethod(RequestMethod requestMethod) {
		return true;
	}

	public boolean hasAccessToRequestParameterName(String parameterName) {
		return true;
	}

	public boolean hasAccessToResource(String resourceId) {
		return true;
	}

	public boolean hasAccessToURI(String uri, RequestMethod method) {
		return true;
	}


}
