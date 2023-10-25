/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.logging.log4j.core.config.plugins.PluginVisitorStrategy;
import org.apache.logging.log4j.core.config.plugins.visitors.PluginBuilderAttributeVisitor;

@Documented
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.PARAMETER, ElementType.FIELD})
@PluginVisitorStrategy(value=PluginBuilderAttributeVisitor.class)
public @interface PluginBuilderAttribute {
    public String value() default "";

    public boolean sensitive() default false;
}

