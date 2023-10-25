/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;

public interface Logger {
    public void catching(Level var1, Throwable var2);

    public void catching(Throwable var1);

    public void debug(Marker var1, Message var2);

    public void debug(Marker var1, Message var2, Throwable var3);

    public void debug(Marker var1, Object var2);

    public void debug(Marker var1, Object var2, Throwable var3);

    public void debug(Marker var1, String var2);

    public void debug(Marker var1, String var2, Object ... var3);

    public void debug(Marker var1, String var2, Throwable var3);

    public void debug(Message var1);

    public void debug(Message var1, Throwable var2);

    public void debug(Object var1);

    public void debug(Object var1, Throwable var2);

    public void debug(String var1);

    public void debug(String var1, Object ... var2);

    public void debug(String var1, Throwable var2);

    public void entry();

    public void entry(Object ... var1);

    public void error(Marker var1, Message var2);

    public void error(Marker var1, Message var2, Throwable var3);

    public void error(Marker var1, Object var2);

    public void error(Marker var1, Object var2, Throwable var3);

    public void error(Marker var1, String var2);

    public void error(Marker var1, String var2, Object ... var3);

    public void error(Marker var1, String var2, Throwable var3);

    public void error(Message var1);

    public void error(Message var1, Throwable var2);

    public void error(Object var1);

    public void error(Object var1, Throwable var2);

    public void error(String var1);

    public void error(String var1, Object ... var2);

    public void error(String var1, Throwable var2);

    public void exit();

    public <R> R exit(R var1);

    public void fatal(Marker var1, Message var2);

    public void fatal(Marker var1, Message var2, Throwable var3);

    public void fatal(Marker var1, Object var2);

    public void fatal(Marker var1, Object var2, Throwable var3);

    public void fatal(Marker var1, String var2);

    public void fatal(Marker var1, String var2, Object ... var3);

    public void fatal(Marker var1, String var2, Throwable var3);

    public void fatal(Message var1);

    public void fatal(Message var1, Throwable var2);

    public void fatal(Object var1);

    public void fatal(Object var1, Throwable var2);

    public void fatal(String var1);

    public void fatal(String var1, Object ... var2);

    public void fatal(String var1, Throwable var2);

    public MessageFactory getMessageFactory();

    public String getName();

    public void info(Marker var1, Message var2);

    public void info(Marker var1, Message var2, Throwable var3);

    public void info(Marker var1, Object var2);

    public void info(Marker var1, Object var2, Throwable var3);

    public void info(Marker var1, String var2);

    public void info(Marker var1, String var2, Object ... var3);

    public void info(Marker var1, String var2, Throwable var3);

    public void info(Message var1);

    public void info(Message var1, Throwable var2);

    public void info(Object var1);

    public void info(Object var1, Throwable var2);

    public void info(String var1);

    public void info(String var1, Object ... var2);

    public void info(String var1, Throwable var2);

    public boolean isDebugEnabled();

    public boolean isDebugEnabled(Marker var1);

    public boolean isEnabled(Level var1);

    public boolean isEnabled(Level var1, Marker var2);

    public boolean isErrorEnabled();

    public boolean isErrorEnabled(Marker var1);

    public boolean isFatalEnabled();

    public boolean isFatalEnabled(Marker var1);

    public boolean isInfoEnabled();

    public boolean isInfoEnabled(Marker var1);

    public boolean isTraceEnabled();

    public boolean isTraceEnabled(Marker var1);

    public boolean isWarnEnabled();

    public boolean isWarnEnabled(Marker var1);

    public void log(Level var1, Marker var2, Message var3);

    public void log(Level var1, Marker var2, Message var3, Throwable var4);

    public void log(Level var1, Marker var2, Object var3);

    public void log(Level var1, Marker var2, Object var3, Throwable var4);

    public void log(Level var1, Marker var2, String var3);

    public void log(Level var1, Marker var2, String var3, Object ... var4);

    public void log(Level var1, Marker var2, String var3, Throwable var4);

    public void log(Level var1, Message var2);

    public void log(Level var1, Message var2, Throwable var3);

    public void log(Level var1, Object var2);

    public void log(Level var1, Object var2, Throwable var3);

    public void log(Level var1, String var2);

    public void log(Level var1, String var2, Object ... var3);

    public void log(Level var1, String var2, Throwable var3);

    public void printf(Level var1, Marker var2, String var3, Object ... var4);

    public void printf(Level var1, String var2, Object ... var3);

    public <T extends Throwable> T throwing(Level var1, T var2);

    public <T extends Throwable> T throwing(T var1);

    public void trace(Marker var1, Message var2);

    public void trace(Marker var1, Message var2, Throwable var3);

    public void trace(Marker var1, Object var2);

    public void trace(Marker var1, Object var2, Throwable var3);

    public void trace(Marker var1, String var2);

    public void trace(Marker var1, String var2, Object ... var3);

    public void trace(Marker var1, String var2, Throwable var3);

    public void trace(Message var1);

    public void trace(Message var1, Throwable var2);

    public void trace(Object var1);

    public void trace(Object var1, Throwable var2);

    public void trace(String var1);

    public void trace(String var1, Object ... var2);

    public void trace(String var1, Throwable var2);

    public void warn(Marker var1, Message var2);

    public void warn(Marker var1, Message var2, Throwable var3);

    public void warn(Marker var1, Object var2);

    public void warn(Marker var1, Object var2, Throwable var3);

    public void warn(Marker var1, String var2);

    public void warn(Marker var1, String var2, Object ... var3);

    public void warn(Marker var1, String var2, Throwable var3);

    public void warn(Message var1);

    public void warn(Message var1, Throwable var2);

    public void warn(Object var1);

    public void warn(Object var1, Throwable var2);

    public void warn(String var1);

    public void warn(String var1, Object ... var2);

    public void warn(String var1, Throwable var2);
}

