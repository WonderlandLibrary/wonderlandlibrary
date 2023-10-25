/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins.validation.validators;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidPort;
import org.apache.logging.log4j.status.StatusLogger;

public class ValidPortValidator
implements ConstraintValidator<ValidPort> {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private ValidPort annotation;

    @Override
    public void initialize(ValidPort annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(String name, Object value) {
        if (value instanceof CharSequence) {
            return this.isValid(name, TypeConverters.convert(value.toString(), Integer.class, -1));
        }
        if (!Integer.class.isInstance(value)) {
            LOGGER.error(this.annotation.message());
            return false;
        }
        int port = (Integer)value;
        if (port < 0 || port > 65535) {
            LOGGER.error(this.annotation.message());
            return false;
        }
        return true;
    }
}

