/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins.validation.validators;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.validation.ConstraintValidator;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.ValidHost;
import org.apache.logging.log4j.status.StatusLogger;

public class ValidHostValidator
implements ConstraintValidator<ValidHost> {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private ValidHost annotation;

    @Override
    public void initialize(ValidHost annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(String name, Object value) {
        if (value == null) {
            LOGGER.error(this.annotation.message());
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
            LOGGER.error(this.annotation.message(), (Throwable)e);
            return false;
        }
    }
}

