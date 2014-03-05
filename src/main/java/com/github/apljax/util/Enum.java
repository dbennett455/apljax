package com.github.apljax.util;

public class Enum {

	// JAX-RS parameter types
	public enum RequestType {
		PATH, QUERY, MATRIX, HEADER, COOKIE, FORM
	};

	// JAX-RS http request types
	public enum RequestMethod {
		GET, POST, PUT, DELETE, HEAD
	};

	public static String requestMethodToString(RequestMethod requestMethod) {
		switch (requestMethod) {
			case GET:
				return("GET");
			case POST:
				return("POST");
			case PUT:
				return("PUT");
			case DELETE:
				return("DELETE");
			case HEAD:
				return("HEAD");
			default:
				return("UNKNOWN");
		}
	}

	public static String requestTypeToString(RequestType requestType) {
		switch (requestType) {
		case PATH:
			return("PATH");
		case QUERY:
			return("QUERY");
		case MATRIX:
			return("MATRIX");
		case HEADER:
			return("HEADER");
		case COOKIE:
			return("COOKIE");
		case FORM:
			return("FORM");
		default:
			return("UNKNOWN");
		}
	}

}
