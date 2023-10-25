/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins.validation;

import java.lang.annotation.Annotation;

public interface ConstraintValidator<A extends Annotation> {
    public void initialize(A var1);

    public boolean isValid(String var1, Object var2);
}

