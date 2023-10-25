/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface Plugin {
    public static final String EMPTY = "";

    public String name();

    public String category();

    public String elementType() default "";

    public boolean printObject() default false;

    public boolean deferChildren() default false;
}

