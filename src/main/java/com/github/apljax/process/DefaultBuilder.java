package com.github.apljax.process;

/**
 * This is the default builder.  It is completely permissive return
 * true for all access checks.
 *
 */

import com.github.apljax.util.Enum.RequestMethod;

public class DefaultBuilder extends AbstractBuilder {

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

	public boolean hasAccessToResource(String resourceId) {
		return true;
	}

	public boolean hasAccessToURI(String uri, String resourceId,
			RequestMethod method) {
		return true;
	}

	public boolean canConsumeMediaType(String mediaType) {
		return true;
	}

	public boolean canProduceMediaType(String mediaType) {
		return true;
	}



}
