// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.validation.validators;

import org.apache.logging.log4j.status.StatusLogger;
import java.lang.annotation.Annotation;
import java.net.UnknownHostException;
import java.net.InetAddress;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidHost;
import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;

public class ValidHostValidator implements ConstraintValidator<ValidHost>
{
    private static final Logger LOGGER;
    private ValidHost annotation;
    
    @Override
    public void initialize(final ValidHost annotation) {
        this.annotation = annotation;
    }
    
    @Override
    public boolean isValid(final String name, final Object value) {
        if (value == null) {
            ValidHostValidator.LOGGER.error(this.annotation.message());
            return false;
        }
        if (value instanceof InetAddress) {
            return true;
        }
        try {
            InetAddress.getByName(value.toString());
            return true;
        }
        catch (UnknownHostException e) {
            ValidHostValidator.LOGGER.error(this.annotation.message(), e);
            return false;
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
