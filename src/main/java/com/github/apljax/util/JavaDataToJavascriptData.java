package com.github.apljax.util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Really basic type conversion.
 *
 * TODO: add numeric sizes and precision
 * TODO: date support
 *
 * @author dbennett455
 *
 */

public class JavaDataToJavascriptData {

	@SuppressWarnings("serial")
	private static ArrayList<String> booleanTypes = new ArrayList<String>(){{
		add("boolean");
		add("java.lang.Boolean");
	}};

	@SuppressWarnings("serial")
	private static ArrayList<String> numberTypes = new ArrayList<String>(){{
		 add("byte");
		 add("double");
		 add("float");
		 add("int");
		 add("long");
		 add("short");
		 add("java.lang.Integer");
		 add("java.lang.Long");
		 add("java.lang.Float");
		 add("java.lang.Double");
		 add("java.lang.Byte");
		 add("java.lang.Short");
		 add("java.math.BigDecimal");
		 add("java.math.BigInteger");
	}};

	public static void addBooleanType(String type) {
		if (!Arrays.asList(booleanTypes.toArray()).contains(type))
			booleanTypes.add(type);
	}

	public static void addNumberType(String type) {
		if (!Arrays.asList(numberTypes.toArray()).contains(type))
			numberTypes.add(type);
	}

	public static String convert(String javaType) {
		// boolean
		if (Arrays.asList(booleanTypes.toArray()).contains(javaType))
			return("boolean");
		// number
		else if (Arrays.asList(numberTypes.toArray()).contains(javaType))
			return("number");
		// String is catch all
		else
			return "String";
	}

}
