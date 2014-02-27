package com.bensoft.apljax;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.MemberValue;

import com.bensoft.apljax.annotation.Comment;
import com.bensoft.apljax.discover.AJDiscoverer;
import com.bensoft.apljax.os.ResourceClass;
import com.bensoft.apljax.os.ResourceMethod;
import com.bensoft.apljax.os.ResourceParameter;
import com.bensoft.apljax.util.AJProperties;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationObjectDiscoveryListener;
import com.impetus.annovention.listener.MethodAnnotationObjectDiscoveryListener;
import com.impetus.annovention.listener.MethodParameterAnnotationObjectDiscoveryListener;
import com.impetus.annovention.util.MethodParameter;

public class Scanner {

    private final static Logger log=LoggerFactory.getLogger(Scanner.class);
    private static Pattern classNameMatching=null;
	private Resources resources=new Resources();

	public Scanner() {
		resources=new Resources();
        // create the classNameMatching property and build a regular expression pattern
        // in the format .*({className}|...).*
        if (classNameMatching == null && AJProperties.getProperties().getProperty("classNameMatching") != null) {
        	List<String> lCpm=Arrays.asList(
        			AJProperties.getProperties().getProperty("classNameMatching").split(",")
        	);
        	StringBuffer sbCpm=new StringBuffer("(");
        	int i=0;
        	for (String sCp : lCpm) {
        		if (i++ > 0)
        			sbCpm.append("|");
        		sbCpm.append(sCp.trim());
        	}
        	sbCpm.append(")");
        	try {
        		classNameMatching=Pattern.compile(sbCpm.toString());
        	} catch (PatternSyntaxException pse) {
        		log.error("Invalid match expression in classNameMatching: {}",sbCpm.toString());
        	}
        }

	}

	public Resources scan() {
		Discoverer discoverer = new AJDiscoverer();
		discoverer.addAnnotationListener(new MyClassAnnotationListener(resources));
		discoverer.addAnnotationListener(new MyMethodAnnotationListener(resources));
		discoverer.addAnnotationListener(new MyMethodParameterAnnotationListener(resources));
		discoverer.discover(true, true, true, true, true, true);
		return resources;
	}

	/**
	 * Do we have a class name match?
	 *
	 * @param clazz
	 * @return
	 */
	private static boolean classNameMatch(ClassFile clazz) {
		boolean ret=true;
		if (classNameMatching != null) {
			if (clazz == null)
				ret=false;
			else {
				String className=clazz.getName();
				Matcher m=classNameMatching.matcher(className);
				ret=m.find();
				if (ret)
					log.debug("class name match: {}",className);
				else
					log.debug("class name skipped: {}",className);
			}
		}
		return ret;
	}

	/**
	 * Listen for Class Annotations
	 */
	static class MyClassAnnotationListener implements ClassAnnotationObjectDiscoveryListener {

		private Resources resources;

		public MyClassAnnotationListener(Resources resources) {
			super();
			this.resources = resources;
		}

		public void discovered(ClassFile clazz, Annotation annotation) {

			if (classNameMatch(clazz)) {

				if (annotation != null && annotation.getTypeName() != null) {

					// get annotation value
					MemberValue myParamVal=annotation.getMemberValue("value");
					String myValue=null;
					if (myParamVal != null)
						myValue=myParamVal.toString();

					// get or add class
					ResourceClass cls=resources.getResourceClass(clazz);

					if ("javax.ws.rs.Path".equals(annotation.getTypeName())) {
						cls.setPath(myValue);
					} else if ("javax.ws.rs.Consumes".equals(annotation.getTypeName())) {
						cls.setConsumes(myValue);
					} else if ("javax.ws.rs.Produces".equals(annotation.getTypeName())) {
						cls.setProduces(myValue);
					} else if (annotation.getTypeName().indexOf(".apljax.annotation.Comment") > 0) {
						cls.setComment(myValue);
					}

					log.debug("Discovered Class[{}] with Annotation ({}) value={}",clazz.getName(),annotation.getTypeName(),myValue);

				}

			}

		}

		public String[] supportedAnnotations() {
			return new String[] {
                Path.class.getName(),
                Produces.class.getName(),
                Consumes.class.getName(),
                Comment.class.getName()
			};
		}
	}


	/**
	 * Report on method annotation with and without parameters
	 *
	 */
	static class MyMethodAnnotationListener implements MethodAnnotationObjectDiscoveryListener {

		private Resources resources;

		public MyMethodAnnotationListener(Resources resources) {
			super();
			this.resources = resources;
		}

		public void discovered(ClassFile clazz, MethodInfo method, Annotation annotation) {

			if (classNameMatch(clazz)) {

				if (annotation != null && annotation.getTypeName() != null) {

					// get annotation value
					MemberValue myParamVal=annotation.getMemberValue("value");
					String myValue=null;
					if (myParamVal != null)
						myValue=myParamVal.toString();

					// get or add class
					ResourceClass cls=resources.getResourceClass(clazz);
					ResourceMethod met=cls.getResourceMethod(method);

					if ("javax.ws.rs.Path".equals(annotation.getTypeName())) {
						met.setPath(myValue);
					} else if ("javax.ws.rs.Consumes".equals(annotation.getTypeName())) {
						met.setConsumes(myValue);
					} else if ("javax.ws.rs.Produces".equals(annotation.getTypeName())) {
						met.setProduces(myValue);
					} else if ("javax.ws.rs.GET".equals(annotation.getTypeName())) {
						met.setRequestMethod(ResourceMethod.RequestMethod.GET);
					} else if ("javax.ws.rs.POST".equals(annotation.getTypeName())) {
						met.setRequestMethod(ResourceMethod.RequestMethod.POST);
					} else if ("javax.ws.rs.PUT".equals(annotation.getTypeName())) {
						met.setRequestMethod(ResourceMethod.RequestMethod.PUT);
					} else if ("javax.ws.rs.DELETE".equals(annotation.getTypeName())) {
						met.setRequestMethod(ResourceMethod.RequestMethod.DELETE);
					} else if (annotation.getTypeName().indexOf(".apljax.annotation.Comment") > 0) {
						met.setComment(myValue);
					}

    				log.debug("Discovered Method[{}.{} ({})] with Annotation({} value={})",
    						clazz.getName(),method.getName(),method.getDescriptor(),annotation.getTypeName(),myValue);
				}

			}

		}

		public String[] supportedAnnotations() {
			return new String[] {
                Path.class.getName(),
                Produces.class.getName(),
                Consumes.class.getName(),
                GET.class.getName(),
                POST.class.getName(),
                PUT.class.getName(),
                DELETE.class.getName(),
                Comment.class.getName()
			};
		}
	}

	/**
	 *
	 * Report on method parameter annotations with and without parameters using the new Javassist object listener style
	 *
	 */
	static class MyMethodParameterAnnotationListener implements MethodParameterAnnotationObjectDiscoveryListener {

		private Resources resources;

		public MyMethodParameterAnnotationListener(Resources resources) {
			super();
			this.resources = resources;
		}

		public void discovered(ClassFile clazz, MethodInfo method, MethodParameter methodParameter, Annotation annotation) {

			if (classNameMatch(clazz)) {

				if (annotation != null && annotation.getTypeName() != null) {

					// get annotation value
					MemberValue myParamVal=annotation.getMemberValue("value");
					String myValue=null;
					if (myParamVal != null)
						myValue=myParamVal.toString();

					// get or add method parameter
					ResourceClass cls=resources.getResourceClass(clazz);
					ResourceMethod met=cls.getResourceMethod(method);
					ResourceParameter param=met.getResourceParameter(new Integer(methodParameter.getIndex()));
					if (param.getMethodParameter() == null)
						param.setMethodParameter(methodParameter);

					if ("javax.ws.rs.PathParam".equals(annotation.getTypeName())) {
						param.setType(ResourceParameter.ParameterType.PATH);
						param.setName(myValue);
					} else if ("javax.ws.rs.QueryParam".equals(annotation.getTypeName())) {
						param.setType(ResourceParameter.ParameterType.QUERY);
						param.setName(myValue);
					} else if ("javax.ws.rs.MatrixParam".equals(annotation.getTypeName())) {
						param.setType(ResourceParameter.ParameterType.MATRIX);
						param.setName(myValue);
					} else if ("javax.ws.rs.HeaderParam".equals(annotation.getTypeName())) {
						param.setType(ResourceParameter.ParameterType.HEADER);
						param.setName(myValue);
					} else if ("javax.ws.rs.CookieParam".equals(annotation.getTypeName())) {
						param.setType(ResourceParameter.ParameterType.COOKIE);
						param.setName(myValue);
					} else if ("javax.ws.rs.FormParam".equals(annotation.getTypeName())) {
						param.setType(ResourceParameter.ParameterType.FORM);
						param.setName(myValue);
					} else if ("javax.ws.rs.DefaultValue".equals(annotation.getTypeName())) {
						param.setDefaultValue(myValue);
					} else if (annotation.getTypeName().indexOf(".apljax.annotation.Comment") > 0) {
						param.setComment(myValue);
					}

    				log.debug("Discovered Parameter on Method[{}.{}({})] index:{} type:{} with Annotation({} value={})",
    						clazz.getName(),method.getName(),method.getDescriptor(),methodParameter.getIndex(),
    						methodParameter.getType(),annotation.getTypeName(),myValue);
				}
			}
		}

		public String[] supportedAnnotations() {
			return new String[] {
				PathParam.class.getName(),
				QueryParam.class.getName(),
				MatrixParam.class.getName(),
				HeaderParam.class.getName(),
				CookieParam.class.getName(),
				FormParam.class.getName(),
				DefaultValue.class.getName(),
				Comment.class.getName()
			};
		}
	}
}
