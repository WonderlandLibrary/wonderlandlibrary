/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal.logging;

import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.ObjectStreamException;
import java.io.Serializable;

public abstract class AbstractInternalLogger
implements InternalLogger,
Serializable {
    private static final long serialVersionUID = -6382972526573193470L;
    private final String name;

    @Override
    public void log(InternalLogLevel internalLogLevel, String string) {
        switch (internalLogLevel) {
            case TRACE: {
                this.trace(string);
                break;
            }
            case DEBUG: {
                this.debug(string);
                break;
            }
            case INFO: {
                this.info(string);
                break;
            }
            case WARN: {
                this.warn(string);
                break;
            }
            case ERROR: {
                this.error(string);
                break;
            }
            default: {
                throw new Error();
            }
        }
    }

    protected Object readResolve() throws ObjectStreamException {
        return InternalLoggerFactory.getInstance(this.name());
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + this.name() + ')';
    }

    @Override
    public void log(InternalLogLevel internalLogLevel, String string, Throwable throwable) {
        switch (internalLogLevel) {
            case TRACE: {
                this.trace(string, throwable);
                break;
            }
            case DEBUG: {
                this.debug(string, throwable);
                break;
            }
            case INFO: {
                this.info(string, throwable);
                break;
            }
            case WARN: {
                this.warn(string, throwable);
                break;
            }
            case ERROR: {
                this.error(string, throwable);
                break;
            }
            default: {
                throw new Error();
            }
        }
    }

    @Override
    public void log(InternalLogLevel internalLogLevel, String string, Object object, Object object2) {
        switch (internalLogLevel) {
            case TRACE: {
                this.trace(string, object, object2);
                break;
            }
            case DEBUG: {
                this.debug(string, object, object2);
                break;
            }
            case INFO: {
                this.info(string, object, object2);
                break;
            }
            case WARN: {
                this.warn(string, object, object2);
                break;
            }
            case ERROR: {
                this.error(string, object, object2);
                break;
            }
            default: {
                throw new Error();
            }
        }
    }

    @Override
    public boolean isEnabled(InternalLogLevel internalLogLevel) {
        switch (internalLogLevel) {
            case TRACE: {
                return this.isTraceEnabled();
            }
            case DEBUG: {
                return this.isDebugEnabled();
            }
            case INFO: {
                return this.isInfoEnabled();
            }
            case WARN: {
                return this.isWarnEnabled();
            }
            case ERROR: {
                return this.isErrorEnabled();
            }
        }
        throw new Error();
    }

    @Override
    public void log(InternalLogLevel internalLogLevel, String string, Object ... objectArray) {
        switch (internalLogLevel) {
            case TRACE: {
                this.trace(string, objectArray);
                break;
            }
            case DEBUG: {
                this.debug(string, objectArray);
                break;
            }
            case INFO: {
                this.info(string, objectArray);
                break;
            }
            case WARN: {
                this.warn(string, objectArray);
                break;
            }
            case ERROR: {
                this.error(string, objectArray);
                break;
            }
            default: {
                throw new Error();
            }
        }
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public void log(InternalLogLevel internalLogLevel, String string, Object object) {
        switch (internalLogLevel) {
            case TRACE: {
                this.trace(string, object);
                break;
            }
            case DEBUG: {
                this.debug(string, object);
                break;
            }
            case INFO: {
                this.info(string, object);
                break;
            }
            case WARN: {
                this.warn(string, object);
                break;
            }
            case ERROR: {
                this.error(string, object);
                break;
            }
            default: {
                throw new Error();
            }
        }
    }

    protected AbstractInternalLogger(String string) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        this.name = string;
    }
}

