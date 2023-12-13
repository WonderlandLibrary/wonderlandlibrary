// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.config.plugins.validation.validators;

import org.apache.logging.log4j.status.StatusLogger;
import java.lang.annotation.Annotation;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;

public class NotBlankValidator implements ConstraintValidator<NotBlank>
{
    private static final Logger LOGGER;
    private NotBlank annotation;
    
    @Override
    public void initialize(final NotBlank anAnnotation) {
        this.annotation = anAnnotation;
    }
    
    @Override
    public boolean isValid(final String name, final Object value) {
        return Strings.isNotBlank(name) || this.err(name);
    }
    
    private boolean err(final String name) {
        NotBlankValidator.LOGGER.error(this.annotation.message(), name);
        return false;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
