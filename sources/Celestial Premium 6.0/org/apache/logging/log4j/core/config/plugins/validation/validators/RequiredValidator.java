/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins.validation.validators;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.apache.logging.log4j.core.util.Assert;
import org.apache.logging.log4j.status.StatusLogger;

public class RequiredValidator
implements ConstraintValidator<Required> {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private Required annotation;

    @Override
    public void initialize(Required anAnnotation) {
        this.annotation = anAnnotation;
    }

    @Override
    public boolean isValid(String name, Object value) {
        return Assert.isNonEmpty(value) || this.err(name);
    }

    private boolean err(String name) {
        LOGGER.error(this.annotation.message() + ": " + name);
        return false;
    }
}

