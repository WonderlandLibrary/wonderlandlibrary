/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 */
package io.netty.util.internal.logging;

import io.netty.util.internal.logging.AbstractInternalLogger;
import org.slf4j.Logger;

class Slf4JLogger
extends AbstractInternalLogger {
    private static final long serialVersionUID = 108038972685130825L;
    private final transient Logger logger;

    @Override
    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }

    @Override
    public void debug(String string) {
        this.logger.debug(string);
    }

    @Override
    public void error(String string, Object ... objectArray) {
        this.logger.error(string, objectArray);
    }

    @Override
    public void warn(String string, Object object, Object object2) {
        this.logger.warn(string, object, object2);
    }

    @Override
    public void debug(String string, Object object, Object object2) {
        this.logger.debug(string, object, object2);
    }

    @Override
    public void error(String string, Object object, Object object2) {
        this.logger.error(string, object, object2);
    }

    @Override
    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }

    @Override
    public void trace(String string, Object object, Object object2) {
        this.logger.trace(string, object, object2);
    }

    Slf4JLogger(Logger logger) {
        super(logger.getName());
        this.logger = logger;
    }

    @Override
    public void warn(String string, Object object) {
        this.logger.warn(string, object);
    }

    @Override
    public void error(String string, Object object) {
        this.logger.error(string, object);
    }

    @Override
    public void debug(String string, Object object) {
        this.logger.debug(string, object);
    }

    @Override
    public void info(String string, Throwable throwable) {
        this.logger.info(string, throwable);
    }

    @Override
    public void warn(String string, Object ... objectArray) {
        this.logger.warn(string, objectArray);
    }

    @Override
    public void trace(String string) {
        this.logger.trace(string);
    }

    @Override
    public void error(String string) {
        this.logger.error(string);
    }

    @Override
    public void warn(String string, Throwable throwable) {
        this.logger.warn(string, throwable);
    }

    @Override
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }

    @Override
    public void debug(String string, Throwable throwable) {
        this.logger.debug(string, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    @Override
    public void info(String string, Object object) {
        this.logger.info(string, object);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    @Override
    public void warn(String string) {
        this.logger.warn(string);
    }

    @Override
    public void debug(String string, Object ... objectArray) {
        this.logger.debug(string, objectArray);
    }

    @Override
    public void info(String string, Object object, Object object2) {
        this.logger.info(string, object, object2);
    }

    @Override
    public void trace(String string, Throwable throwable) {
        this.logger.trace(string, throwable);
    }

    @Override
    public void trace(String string, Object ... objectArray) {
        this.logger.trace(string, objectArray);
    }

    @Override
    public void info(String string) {
        this.logger.info(string);
    }

    @Override
    public void error(String string, Throwable throwable) {
        this.logger.error(string, throwable);
    }

    @Override
    public void trace(String string, Object object) {
        this.logger.trace(string, object);
    }

    @Override
    public void info(String string, Object ... objectArray) {
        this.logger.info(string, objectArray);
    }
}

