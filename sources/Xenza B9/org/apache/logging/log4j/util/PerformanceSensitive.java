// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Retention(RetentionPolicy.CLASS)
public @interface PerformanceSensitive {
    String[] value() default { "" };
}
