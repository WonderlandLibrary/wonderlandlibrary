/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal.logging;

import io.netty.util.internal.logging.InternalLogLevel;

public interface InternalLogger {
    public void info(String var1);

    public void log(InternalLogLevel var1, String var2);

    public void trace(String var1, Object var2);

    public void info(String var1, Object var2, Object var3);

    public void warn(String var1, Object var2, Object var3);

    public void log(InternalLogLevel var1, String var2, Object var3);

    public void info(String var1, Object var2);

    public void error(String var1, Object ... var2);

    public void warn(String var1, Object ... var2);

    public void warn(String var1, Object var2);

    public void info(String var1, Object ... var2);

    public void info(String var1, Throwable var2);

    public void log(InternalLogLevel var1, String var2, Object var3, Object var4);

    public void trace(String var1);

    public void log(InternalLogLevel var1, String var2, Object ... var3);

    public boolean isTraceEnabled();

    public void debug(String var1, Object var2);

    public void log(InternalLogLevel var1, String var2, Throwable var3);

    public void error(String var1, Object var2, Object var3);

    public boolean isDebugEnabled();

    public void trace(String var1, Object ... var2);

    public void error(String var1, Throwable var2);

    public void debug(String var1);

    public void error(String var1, Object var2);

    public void debug(String var1, Throwable var2);

    public String name();

    public boolean isEnabled(InternalLogLevel var1);

    public void trace(String var1, Throwable var2);

    public boolean isErrorEnabled();

    public boolean isInfoEnabled();

    public void debug(String var1, Object ... var2);

    public void warn(String var1);

    public void warn(String var1, Throwable var2);

    public void debug(String var1, Object var2, Object var3);

    public void error(String var1);

    public void trace(String var1, Object var2, Object var3);

    public boolean isWarnEnabled();
}

