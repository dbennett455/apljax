/**
 * Default path is for AplJax directory
 * output and overrides the Path value when
 * complex ABNF notation is used.
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
public @interface DefaultPath {
	String text() default "";
}
