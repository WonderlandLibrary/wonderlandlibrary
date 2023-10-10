// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.validation;

import java.lang.annotation.Annotation;

public interface ConstraintValidator<A extends Annotation>
{
    void initialize(final A annotation);
    
    boolean isValid(final String name, final Object value);
}
