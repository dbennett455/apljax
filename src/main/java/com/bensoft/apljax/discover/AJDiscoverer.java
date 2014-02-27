/*
 * Copyright 2010 Impetus Infotech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bensoft.apljax.discover;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bensoft.apljax.util.AJProperties;
import com.impetus.annovention.Discoverer;

/**
 * The Class ClasspathReader.
 *
 * @author animesh.kumar
 */
public class AJDiscoverer extends Discoverer {

    private final Logger log=LoggerFactory.getLogger(AJDiscoverer.class);
    private static Pattern classPathMatching=null;
    private AJFilter filter;

    /**
     * Instantiates a new classpath reader.
     */
    public AJDiscoverer() {
    	super();
        filter = new AJFilter();
        // create the classPathMatching property and build a regular expression pattern
        // in the format .*({cp}|...).*
        if (classPathMatching == null && AJProperties.getProperties().getProperty("classPathMatching") != null) {
        	List<String> lCpm=Arrays.asList(
        			AJProperties.getProperties().getProperty("classPathMatching").split(",")
        	);
        	StringBuffer sbCpm=new StringBuffer(".*(");
        	int i=0;
        	for (String sCp : lCpm) {
        		if (i++ > 0)
        			sbCpm.append("|");
        		sbCpm.append(sCp.trim());
        	}
        	sbCpm.append(").*");
        	try {
        		classPathMatching=Pattern.compile(sbCpm.toString());
        	} catch (PatternSyntaxException pse) {
        		log.error("Invalid match expression in classPathMatching: {}",sbCpm.toString());
        	}
        }
    }

    /**
     * Uses java.class.path system-property to fetch URLs
     *
     * @return the URL[]
     */
    public final URL[] findResources() {
    	List<URL> ret=new ArrayList<URL>();
        URL[] cp = getUrlsForCurrentClasspath();
        if (cp.length == 0) cp = getUrlsForSystemClasspath();
        if (classPathMatching != null) {
        	for (URL p : cp) {
            	Matcher m=classPathMatching.matcher(p.getPath());
        		if (m.find()) {
    				ret.add(p);
    			}
    		}
            return ret.toArray(new URL[ret.size()]);
        }
    	return cp;
    }

    /* @see com.impetus.annovention.Discoverer#getFilter() */
    public final AJFilter getFilter() {
        return filter;
    }

    /**
     * @param filter
     */
    public final void setFilter(AJFilter filter) {
        this.filter = filter;
    }

    //-------------------------------------------------------------------------

    // See http://code.google.com/p/reflections/source/browse/trunk/reflections/src/main/java/org/reflections/util/ClasspathHelper.java?r=103
    private URL[] getUrlsForCurrentClasspath() {
        List<URL> list = new ArrayList<URL>();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        while (loader != null) {
            if (loader instanceof URLClassLoader) {
                URL[]     urlArray = ((URLClassLoader) loader).getURLs();
                List<URL> urlList  = Arrays.asList(urlArray);
                list.addAll(urlList);
            }
            loader = loader.getParent();
        }
        return list.toArray(new URL[list.size()]);
    }

    private URL[] getUrlsForSystemClasspath() {
        List<URL> list = new ArrayList<URL>();
        String classpath = System.getProperty("java.class.path");
        StringTokenizer tokenizer = new StringTokenizer(classpath,
                File.pathSeparator);

        while (tokenizer.hasMoreTokens()) {
            String path = tokenizer.nextToken();

            File fp = new File(path);
            if (!fp.exists())
                throw new RuntimeException(
                        "File in java.class.path does not exist: " + fp);
            try {
                list.add(fp.toURI().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return list.toArray(new URL[list.size()]);
    }
}
