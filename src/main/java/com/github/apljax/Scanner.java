package com.github.apljax;

import java.util.ArrayList;
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
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import com.github.apljax.annotation.Comment;
import com.github.apljax.annotation.DefaultPath;
import com.github.apljax.annotation.ResourceId;
import com.github.apljax.discover.AJDiscoverer;
import com.github.apljax.resource.ResourceClass;
import com.github.apljax.resource.ResourceField;
import com.github.apljax.resource.ResourceMethod;
import com.github.apljax.resource.ResourceParameter;
import com.github.apljax.resource.Resources;
import com.github.apljax.util.AJProperties;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationObjectDiscoveryListener;
import com.impetus.annovention.listener.FieldAnnotationObjectDiscoveryListener;
import com.impetus.annovention.listener.MethodAnnotationObjectDiscoveryListener;
import com.impetus.annovention.listener.MethodParameterAnnotationObjectDiscoveryListener;
import com.impetus.annovention.util.MethodParameter;
import com.impetus.annovention.util.MethodParameters;

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
		discoverer.addAnnotationListener(new MyFieldAnnotationListener(resources));
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

					AnnotationValue av=new AnnotationValue(annotation);
					String myValue=av.myValue;
					String[] myArrayValue=av.myArrayValue;

					// get or add class
					ResourceClass cls=resources.getResourceClass(clazz);

					if (Path.class.getName().equals(annotation.getTypeName())) {
						cls.setPath(myValue);
					} else if (Consumes.class.getName().equals(annotation.getTypeName())) {
						cls.setConsumes(myArrayValue);
					} else if (Produces.class.getName().equals(annotation.getTypeName())) {
						cls.setProduces(myArrayValue);
					} else if (Comment.class.getName().equals(annotation.getTypeName())) {
						cls.setComment(myValue);
					} else if (ResourceId.class.getName().equals(annotation.getTypeName())) {
						cls.setDefinedResourceId(myValue);
					} else if (DefaultPath.class.getName().equals(annotation.getTypeName())) {
						cls.setDefaultPath(myValue);
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
                Comment.class.getName(),
                ResourceId.class.getName(),
                DefaultPath.class.getName()
			};
		}
	}

	/**
	 *
	 * Report on method parameter annotations with and without parameters using the new Javassist object listener style
	 *
	 */
	static class MyFieldAnnotationListener implements FieldAnnotationObjectDiscoveryListener {

		private Resources resources;

		public MyFieldAnnotationListener(Resources resources) {
			super();
			this.resources = resources;
		}

		public void discovered(ClassFile clazz, FieldInfo field, Annotation annotation) {

			if (classNameMatch(clazz)) {

				if (annotation != null && annotation.getTypeName() != null) {

					// get annotation value
					AnnotationValue av=new AnnotationValue(annotation);
					String myValue=av.myValue;

					// get or add method parameter
					ResourceClass cls=resources.getResourceClass(clazz);
					ResourceField fld=cls.getResourceField(field);

					if (PathParam.class.getName().equals(annotation.getTypeName())) {
						fld.setRequestType(ResourceField.RequestType.PATH);
						fld.setName(myValue);
					} else if (QueryParam.class.getName().equals(annotation.getTypeName())) {
						fld.setRequestType(ResourceField.RequestType.QUERY);
						fld.setName(myValue);
					} else if (MatrixParam.class.getName().equals(annotation.getTypeName())) {
						fld.setRequestType(ResourceField.RequestType.MATRIX);
						fld.setName(myValue);
					} else if (HeaderParam.class.getName().equals(annotation.getTypeName())) {
						fld.setRequestType(ResourceField.RequestType.HEADER);
						fld.setName(myValue);
					} else if (CookieParam.class.getName().equals(annotation.getTypeName())) {
						fld.setRequestType(ResourceField.RequestType.COOKIE);
						fld.setName(myValue);
					} else if (FormParam.class.getName().equals(annotation.getTypeName())) {
						fld.setRequestType(ResourceField.RequestType.FORM);
						fld.setName(myValue);
					} else if (DefaultValue.class.getName().equals(annotation.getTypeName())) {
						fld.setDefaultValue(myValue);
					} else if (Comment.class.getName().equals(annotation.getTypeName())) {
						fld.setComment(myValue);
					}

					// Set the javaType from the Descriptor
					if (fld.getJavaType() == null) {
						String desc=field.getDescriptor();
						List<String> lJt=MethodParameters.parameterTypes(desc);
						if (lJt != null && lJt.size() > 0) {
							fld.setJavaType(lJt.get(0));
						}
					}

       				log.debug("Discovered Field in Class {} name: {} type:{} with Annotation({} value={})",
       						clazz.getName(), field.getName(), fld.getJavaType(), annotation.getTypeName(), myValue);
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
					AnnotationValue av=new AnnotationValue(annotation);
					String myValue=av.myValue;
					String[] myArrayValue=av.myArrayValue;

					// get or add class
					ResourceClass cls=resources.getResourceClass(clazz);
					ResourceMethod met=cls.getResourceMethod(method);

					if (Path.class.getName().equals(annotation.getTypeName())) {
						met.setPath(myValue);
					} else if (Consumes.class.getName().equals(annotation.getTypeName())) {
						met.setConsumes(myArrayValue);
					} else if (Produces.class.getName().equals(annotation.getTypeName())) {
						met.setProduces(myArrayValue);
					} else if (GET.class.getName().equals(annotation.getTypeName())) {
						met.setRequestMethod(ResourceMethod.RequestMethod.GET);
					} else if (POST.class.getName().equals(annotation.getTypeName())) {
						met.setRequestMethod(ResourceMethod.RequestMethod.POST);
					} else if (PUT.class.getName().equals(annotation.getTypeName())) {
						met.setRequestMethod(ResourceMethod.RequestMethod.PUT);
					} else if (DELETE.class.getName().equals(annotation.getTypeName())) {
						met.setRequestMethod(ResourceMethod.RequestMethod.DELETE);
					} else if (Comment.class.getName().equals(annotation.getTypeName())) {
						met.setComment(myValue);
					} else if (ResourceId.class.getName().equals(annotation.getTypeName())) {
						met.setDefinedResourceId(myValue);
					} else if (DefaultPath.class.getName().equals(annotation.getTypeName())) {
						met.setDefaultPath(myValue);
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
                Comment.class.getName(),
                DefaultPath.class.getName(),
                ResourceId.class.getName()
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
					AnnotationValue av=new AnnotationValue(annotation);
					String myValue=av.myValue;

					// get or add method parameter
					ResourceClass cls=resources.getResourceClass(clazz);
					ResourceMethod met=cls.getResourceMethod(method);
					ResourceParameter param=met.getResourceParameter(new Integer(methodParameter.getIndex()));
					if (param.getMethodParameter() == null)
						param.setMethodParameter(methodParameter);

					if (PathParam.class.getName().equals(annotation.getTypeName())) {
						param.setType(ResourceParameter.ParameterType.PATH);
						param.setName(myValue);
					} else if (QueryParam.class.getName().equals(annotation.getTypeName())) {
						param.setType(ResourceParameter.ParameterType.QUERY);
						param.setName(myValue);
					} else if (MatrixParam.class.getName().equals(annotation.getTypeName())) {
						param.setType(ResourceParameter.ParameterType.MATRIX);
						param.setName(myValue);
					} else if (HeaderParam.class.getName().equals(annotation.getTypeName())) {
						param.setType(ResourceParameter.ParameterType.HEADER);
						param.setName(myValue);
					} else if (CookieParam.class.getName().equals(annotation.getTypeName())) {
						param.setType(ResourceParameter.ParameterType.COOKIE);
						param.setName(myValue);
					} else if (FormParam.class.getName().equals(annotation.getTypeName())) {
						param.setType(ResourceParameter.ParameterType.FORM);
						param.setName(myValue);
					} else if (DefaultValue.class.getName().equals(annotation.getTypeName())) {
						param.setDefaultValue(myValue);
					} else if (Comment.class.getName().equals(annotation.getTypeName())) {
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

	/**
	 * Decode annotation value
	 *
	 * @author root
	 */
	static class AnnotationValue {

		String myValue=null;
		String[] myArrayValue=null;

		AnnotationValue(Annotation annotation) {
			// get annotation value
			MemberValue myParamVal=annotation.getMemberValue("value");
			if (myParamVal != null) {
				if (myParamVal.getClass().getName().equals(StringMemberValue.class.getName())) {
					StringMemberValue smv=(StringMemberValue)myParamVal;
					myValue=smv.getValue();
				}
				else if (myParamVal != null && myParamVal.getClass().getName().equals(ArrayMemberValue.class.getName())) {
					ArrayMemberValue amv=(ArrayMemberValue)myParamVal;
					MemberValue[] mva=amv.getValue();
					ArrayList<String> als=new ArrayList<String>();
					for (MemberValue mv : mva) {
						if (mv.getClass().getName().equals(StringMemberValue.class.getName())) {
							StringMemberValue smv=(StringMemberValue)mv;
							als.add(smv.getValue());
						} else {
							als.add(mv.toString());
						}
					}
					myArrayValue=als.toArray(new String[als.size()]);
				} else {
					myValue=myParamVal.toString();
				}
			}
		}

	}

}
