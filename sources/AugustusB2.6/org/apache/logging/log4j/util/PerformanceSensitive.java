// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.CLASS)
public @interface PerformanceSensitive {
    String[] value() default { "" };
}
