// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config.plugins;

import java.lang.annotation.Annotation;
import org.apache.logging.log4j.core.config.plugins.visitors.PluginVisitor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface PluginVisitorStrategy {
    Class<? extends PluginVisitor<? extends Annotation>> value();
}
