package com.enjoytheban.api;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Purity
 */

/**
 * Basically makes an event run when this annotation is called
 */

@Retention(RUNTIME)
@Target(METHOD)
public @interface EventHandler {
	/*
	 The priority of this EventHandler
	 @return
	 */
	byte priority() default Priority.NORMAL;
}
