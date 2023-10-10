// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.validation.validators;

import org.apache.logging.log4j.status.StatusLogger;
import java.lang.annotation.Annotation;
import org.apache.logging.log4j.core.config.plugins.convert.TypeConverters;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidPort;
import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;

public class ValidPortValidator implements ConstraintValidator<ValidPort>
{
    private static final Logger LOGGER;
    private ValidPort annotation;
    
    @Override
    public void initialize(final ValidPort annotation) {
        this.annotation = annotation;
    }
    
    @Override
    public boolean isValid(final String name, final Object value) {
        if (value instanceof CharSequence) {
            return this.isValid(name, TypeConverters.convert(value.toString(), (Class<?>)Integer.class, (Object)(-1)));
        }
        if (!Integer.class.isInstance(value)) {
            ValidPortValidator.LOGGER.error(this.annotation.message());
            return false;
        }
        final int port = (int)value;
        if (port < 0 || port > 65535) {
            ValidPortValidator.LOGGER.error(this.annotation.message());
            return false;
        }
        return true;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
