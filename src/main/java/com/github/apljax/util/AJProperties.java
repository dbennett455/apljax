package com.github.apljax.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * You can optionally use -Dapljax.properties="{file}" to define
 * system properties
 *
 * @author dbennett
 *
 */
public class AJProperties {

	private final String propertiesFile =
			System.getProperty("apljax.properties") != null ?
					System.getProperty("apljax.properties") :
					"/apljax.properties";
	private final Logger log=LoggerFactory.getLogger(AJProperties.class);
	private static AJProperties instance = null;
	private Properties props=null;

	private AJProperties() {
	    try {
	    	props = new Properties();
	    	InputStream is = this.getClass().getResourceAsStream("/apljax.properties");
   			props.load(is);
	    } catch (IOException ioe) {
	    	log.info("{} file not found.",propertiesFile);
	    }
	}

	public static synchronized AJProperties instance() {
	    if (instance == null) {
	        instance = new AJProperties();
	    }
	    return instance;
	}

	public static Properties getProperties(){
		return(AJProperties.instance().props);
	}

}
