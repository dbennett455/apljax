package com.github.apljax.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceIdExtractor {

	private static final Pattern resourceFromPath=Pattern.compile("^([^/]+)/*.*$");

	private String resourceId=null;
	private PathBuilder pathBuilder=null;

	public ResourceIdExtractor() {
		this(null);
	}

	public ResourceIdExtractor(String resourceId) {
		super();
		setResourceId(resourceId);
		this.pathBuilder = new PathBuilder();
	}

	public ResourceIdExtractor(String resourceId, PathBuilder pathBuilder) {
		super();
		setResourceId(resourceId);
		this.pathBuilder = pathBuilder;
	}

	/**
	 * If a default ResourceId() annotation is found then return that.  Otherwise,  use the
	 * path builder building without root and return the first component in the path as the
	 * resource.
	 *
	 * @return String resourceId
	 */
	public String extract() {
		String ret=null;
		if (resourceId != null && resourceId.length() > 0) {
			ret=resourceId;
		} else {
			String pth=getPathBuilder().buildNoRoot();
			if (pth != null && pth.length() > 0 && !pth.equals("/")) {
				Matcher m=resourceFromPath.matcher(pth);
				if (m.find())
					ret=m.group(1);
				else
					ret=pth;
			}
		}
		return ret;
	}

	public PathBuilder getPathBuilder() {
		return pathBuilder;
	}

	public String getResourceId() {
		return resourceId;
	}

	public ResourceIdExtractor setClassDefaultPath(String classDefaultPath) {
		pathBuilder.setClassDefaultPath(classDefaultPath);
		return this;
	}

	public ResourceIdExtractor setClassPath(String classPath) {
		pathBuilder.setClassPath(classPath);
		return this;
	}

	public ResourceIdExtractor setMethodDefaultPath(String methodDefaultPath) {
		pathBuilder.setMethodDefaultPath(methodDefaultPath);
		return this;
	}

	public ResourceIdExtractor setMethodPath(String methodPath) {
		pathBuilder.setMethodPath(methodPath);
		return this;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = PathBuilder.deQuote(resourceId);
	}
}
