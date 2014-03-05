package com.github.apljax.os;

import java.util.TreeMap;

/**
 * The Bowl of O's is the container the holds the resources built from the
 * classes, fields, methods and parameters.  This should be dynamically
 * created for the request based on the permissions of the requester.
 *
 * @author dbennett455
 *
 */

@SuppressWarnings("serial")
public class Bowl extends TreeMap<String, Object> {

	public Bowl addResource(String id, Resource r) {
		if (r != null)   // don't add null resources
			put(id, r);
		return this;
	}

	public Bowl assignRootUrl(String url) {
		if (url != null)  // don't add null
			put("_rootUrl", url);
		return this;
	}

}
