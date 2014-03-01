/**
 * ResourceId defines the main resource
 * that a class of method is part of.
 * If not defined, be default the main
 * resource id is the first element in
 * the URL path.
 *
 */
package com.github.apljax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResourceId {
	String text() default "";
}
