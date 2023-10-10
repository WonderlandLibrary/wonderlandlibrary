/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.log.Hierarchy
 *  org.apache.log.Logger
 */
package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

public class LogKitLogger
implements Log,
Serializable {
    private static final long serialVersionUID = 3768538055836059519L;
    protected volatile transient Logger logger = null;
    protected String name = null;

    public LogKitLogger(String name) {
        this.name = name;
        this.logger = this.getLogger();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Logger getLogger() {
        Logger result = this.logger;
        if (result == null) {
            LogKitLogger logKitLogger = this;
            synchronized (logKitLogger) {
                result = this.logger;
                if (result == null) {
                    this.logger = result = Hierarchy.getDefaultHierarchy().getLoggerFor(this.name);
                }
            }
        }
        return result;
    }

    public void trace(Object message) {
        this.debug(message);
    }

    public void trace(Object message, Throwable t2) {
        this.debug(message, t2);
    }

    public void debug(Object message) {
        if (message != null) {
            this.getLogger().debug(String.valueOf(message));
        }
    }

    public void debug(Object message, Throwable t2) {
        if (message != null) {
            this.getLogger().debug(String.valueOf(message), t2);
        }
    }

    public void info(Object message) {
        if (message != null) {
            this.getLogger().info(String.valueOf(message));
        }
    }

    public void info(Object message, Throwable t2) {
        if (message != null) {
            this.getLogger().info(String.valueOf(message), t2);
        }
    }

    public void warn(Object message) {
        if (message != null) {
            this.getLogger().warn(String.valueOf(message));
        }
    }

    public void warn(Object message, Throwable t2) {
        if (message != null) {
            this.getLogger().warn(String.valueOf(message), t2);
        }
    }

    public void error(Object message) {
        if (message != null) {
            this.getLogger().error(String.valueOf(message));
        }
    }

    public void error(Object message, Throwable t2) {
        if (message != null) {
            this.getLogger().error(String.valueOf(message), t2);
        }
    }

    public void fatal(Object message) {
        if (message != null) {
            this.getLogger().fatalError(String.valueOf(message));
        }
    }

    public void fatal(Object message, Throwable t2) {
        if (message != null) {
            this.getLogger().fatalError(String.valueOf(message), t2);
        }
    }

    public boolean isDebugEnabled() {
        return this.getLogger().isDebugEnabled();
    }

    public boolean isErrorEnabled() {
        return this.getLogger().isErrorEnabled();
    }

    public boolean isFatalEnabled() {
        return this.getLogger().isFatalErrorEnabled();
    }

    public boolean isInfoEnabled() {
        return this.getLogger().isInfoEnabled();
    }

    public boolean isTraceEnabled() {
        return this.getLogger().isDebugEnabled();
    }

    public boolean isWarnEnabled() {
        return this.getLogger().isWarnEnabled();
    }
}

