/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractLogger
implements Logger {
    public static final Marker FLOW_MARKER = MarkerManager.getMarker("FLOW");
    public static final Marker ENTRY_MARKER = MarkerManager.getMarker("ENTRY", FLOW_MARKER);
    public static final Marker EXIT_MARKER = MarkerManager.getMarker("EXIT", FLOW_MARKER);
    public static final Marker EXCEPTION_MARKER = MarkerManager.getMarker("EXCEPTION");
    public static final Marker THROWING_MARKER = MarkerManager.getMarker("THROWING", EXCEPTION_MARKER);
    public static final Marker CATCHING_MARKER = MarkerManager.getMarker("CATCHING", EXCEPTION_MARKER);
    public static final Class<? extends MessageFactory> DEFAULT_MESSAGE_FACTORY_CLASS = ParameterizedMessageFactory.class;
    private static final String FQCN = AbstractLogger.class.getName();
    private static final String THROWING = "throwing";
    private static final String CATCHING = "catching";
    private final String name;
    private final MessageFactory messageFactory;

    public AbstractLogger() {
        this.name = this.getClass().getName();
        this.messageFactory = this.createDefaultMessageFactory();
    }

    public AbstractLogger(String name) {
        this.name = name;
        this.messageFactory = this.createDefaultMessageFactory();
    }

    public AbstractLogger(String name, MessageFactory messageFactory) {
        this.name = name;
        this.messageFactory = messageFactory == null ? this.createDefaultMessageFactory() : messageFactory;
    }

    public static void checkMessageFactory(Logger logger, MessageFactory messageFactory) {
        String name = logger.getName();
        MessageFactory loggerMessageFactory = logger.getMessageFactory();
        if (messageFactory != null && !loggerMessageFactory.equals(messageFactory)) {
            StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with the message factory {}, which may create log events with unexpected formatting.", name, loggerMessageFactory, messageFactory);
        } else if (messageFactory == null && !loggerMessageFactory.getClass().equals(DEFAULT_MESSAGE_FACTORY_CLASS)) {
            StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with a null message factory (defaults to {}), which may create log events with unexpected formatting.", name, loggerMessageFactory, DEFAULT_MESSAGE_FACTORY_CLASS.getName());
        }
    }

    @Override
    public void catching(Level level, Throwable t) {
        this.catching(FQCN, level, t);
    }

    @Override
    public void catching(Throwable t) {
        this.catching(FQCN, Level.ERROR, t);
    }

    protected void catching(String fqcn, Level level, Throwable t) {
        if (this.isEnabled(level, CATCHING_MARKER, (Object)null, null)) {
            this.log(CATCHING_MARKER, fqcn, level, this.messageFactory.newMessage(CATCHING), t);
        }
    }

    private MessageFactory createDefaultMessageFactory() {
        try {
            return DEFAULT_MESSAGE_FACTORY_CLASS.newInstance();
        }
        catch (InstantiationException e) {
            throw new IllegalStateException(e);
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void debug(Marker marker, Message msg) {
        if (this.isEnabled(Level.DEBUG, marker, msg, null)) {
            this.log(marker, FQCN, Level.DEBUG, msg, null);
        }
    }

    @Override
    public void debug(Marker marker, Message msg, Throwable t) {
        if (this.isEnabled(Level.DEBUG, marker, msg, t)) {
            this.log(marker, FQCN, Level.DEBUG, msg, t);
        }
    }

    @Override
    public void debug(Marker marker, Object message) {
        if (this.isEnabled(Level.DEBUG, marker, message, null)) {
            this.log(marker, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void debug(Marker marker, Object message, Throwable t) {
        if (this.isEnabled(Level.DEBUG, marker, message, t)) {
            this.log(marker, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void debug(Marker marker, String message) {
        if (this.isEnabled(Level.DEBUG, marker, message)) {
            this.log(marker, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void debug(Marker marker, String message, Object ... params) {
        if (this.isEnabled(Level.DEBUG, marker, message, params)) {
            Message msg = this.messageFactory.newMessage(message, params);
            this.log(marker, FQCN, Level.DEBUG, msg, msg.getThrowable());
        }
    }

    @Override
    public void debug(Marker marker, String message, Throwable t) {
        if (this.isEnabled(Level.DEBUG, marker, message, t)) {
            this.log(marker, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void debug(Message msg) {
        if (this.isEnabled(Level.DEBUG, null, msg, null)) {
            this.log(null, FQCN, Level.DEBUG, msg, null);
        }
    }

    @Override
    public void debug(Message msg, Throwable t) {
        if (this.isEnabled(Level.DEBUG, null, msg, t)) {
            this.log(null, FQCN, Level.DEBUG, msg, t);
        }
    }

    @Override
    public void debug(Object message) {
        if (this.isEnabled(Level.DEBUG, null, message, null)) {
            this.log(null, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void debug(Object message, Throwable t) {
        if (this.isEnabled(Level.DEBUG, null, message, t)) {
            this.log(null, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void debug(String message) {
        if (this.isEnabled(Level.DEBUG, null, message)) {
            this.log(null, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void debug(String message, Object ... params) {
        if (this.isEnabled(Level.DEBUG, null, message, params)) {
            Message msg = this.messageFactory.newMessage(message, params);
            this.log(null, FQCN, Level.DEBUG, msg, msg.getThrowable());
        }
    }

    @Override
    public void debug(String message, Throwable t) {
        if (this.isEnabled(Level.DEBUG, (Marker)null, message, t)) {
            this.log(null, FQCN, Level.DEBUG, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void entry() {
        this.entry(FQCN, new Object[0]);
    }

    @Override
    public void entry(Object ... params) {
        this.entry(FQCN, params);
    }

    protected void entry(String fqcn, Object ... params) {
        if (this.isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, null)) {
            this.log(ENTRY_MARKER, fqcn, Level.TRACE, this.entryMsg(params.length, params), null);
        }
    }

    private Message entryMsg(int count, Object ... params) {
        if (count == 0) {
            return this.messageFactory.newMessage("entry");
        }
        StringBuilder sb = new StringBuilder("entry params(");
        int i = 0;
        for (Object parm : params) {
            if (parm != null) {
                sb.append(parm.toString());
            } else {
                sb.append("null");
            }
            if (++i >= params.length) continue;
            sb.append(", ");
        }
        sb.append(")");
        return this.messageFactory.newMessage(sb.toString());
    }

    @Override
    public void error(Marker marker, Message msg) {
        if (this.isEnabled(Level.ERROR, marker, msg, null)) {
            this.log(marker, FQCN, Level.ERROR, msg, null);
        }
    }

    @Override
    public void error(Marker marker, Message msg, Throwable t) {
        if (this.isEnabled(Level.ERROR, marker, msg, t)) {
            this.log(marker, FQCN, Level.ERROR, msg, t);
        }
    }

    @Override
    public void error(Marker marker, Object message) {
        if (this.isEnabled(Level.ERROR, marker, message, null)) {
            this.log(marker, FQCN, Level.ERROR, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void error(Marker marker, Object message, Throwable t) {
        if (this.isEnabled(Level.ERROR, marker, message, t)) {
            this.log(marker, FQCN, Level.ERROR, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void error(Marker marker, String message) {
        if (this.isEnabled(Level.ERROR, marker, message)) {
            this.log(marker, FQCN, Level.ERROR, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void error(Marker marker, String message, Object ... params) {
        if (this.isEnabled(Level.ERROR, marker, message, params)) {
            Message msg = this.messageFactory.newMessage(message, params);
            this.log(marker, FQCN, Level.ERROR, msg, msg.getThrowable());
        }
    }

    @Override
    public void error(Marker marker, String message, Throwable t) {
        if (this.isEnabled(Level.ERROR, marker, message, t)) {
            this.log(marker, FQCN, Level.ERROR, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void error(Message msg) {
        if (this.isEnabled(Level.ERROR, null, msg, null)) {
            this.log(null, FQCN, Level.ERROR, msg, null);
        }
    }

    @Override
    public void error(Message msg, Throwable t) {
        if (this.isEnabled(Level.ERROR, null, msg, t)) {
            this.log(null, FQCN, Level.ERROR, msg, t);
        }
    }

    @Override
    public void error(Object message) {
        if (this.isEnabled(Level.ERROR, null, message, null)) {
            this.log(null, FQCN, Level.ERROR, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void error(Object message, Throwable t) {
        if (this.isEnabled(Level.ERROR, null, message, t)) {
            this.log(null, FQCN, Level.ERROR, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void error(String message) {
        if (this.isEnabled(Level.ERROR, null, message)) {
            this.log(null, FQCN, Level.ERROR, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void error(String message, Object ... params) {
        if (this.isEnabled(Level.ERROR, null, message, params)) {
            Message msg = this.messageFactory.newMessage(message, params);
            this.log(null, FQCN, Level.ERROR, msg, msg.getThrowable());
        }
    }

    @Override
    public void error(String message, Throwable t) {
        if (this.isEnabled(Level.ERROR, (Marker)null, message, t)) {
            this.log(null, FQCN, Level.ERROR, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void exit() {
        this.exit(FQCN, null);
    }

    @Override
    public <R> R exit(R result) {
        return this.exit(FQCN, result);
    }

    protected <R> R exit(String fqcn, R result) {
        if (this.isEnabled(Level.TRACE, EXIT_MARKER, (Object)null, null)) {
            this.log(EXIT_MARKER, fqcn, Level.TRACE, this.toExitMsg(result), null);
        }
        return result;
    }

    @Override
    public void fatal(Marker marker, Message msg) {
        if (this.isEnabled(Level.FATAL, marker, msg, null)) {
            this.log(marker, FQCN, Level.FATAL, msg, null);
        }
    }

    @Override
    public void fatal(Marker marker, Message msg, Throwable t) {
        if (this.isEnabled(Level.FATAL, marker, msg, t)) {
            this.log(marker, FQCN, Level.FATAL, msg, t);
        }
    }

    @Override
    public void fatal(Marker marker, Object message) {
        if (this.isEnabled(Level.FATAL, marker, message, null)) {
            this.log(marker, FQCN, Level.FATAL, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void fatal(Marker marker, Object message, Throwable t) {
        if (this.isEnabled(Level.FATAL, marker, message, t)) {
            this.log(marker, FQCN, Level.FATAL, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void fatal(Marker marker, String message) {
        if (this.isEnabled(Level.FATAL, marker, message)) {
            this.log(marker, FQCN, Level.FATAL, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void fatal(Marker marker, String message, Object ... params) {
        if (this.isEnabled(Level.FATAL, marker, message, params)) {
            Message msg = this.messageFactory.newMessage(message, params);
            this.log(marker, FQCN, Level.FATAL, msg, msg.getThrowable());
        }
    }

    @Override
    public void fatal(Marker marker, String message, Throwable t) {
        if (this.isEnabled(Level.FATAL, marker, message, t)) {
            this.log(marker, FQCN, Level.FATAL, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void fatal(Message msg) {
        if (this.isEnabled(Level.FATAL, null, msg, null)) {
            this.log(null, FQCN, Level.FATAL, msg, null);
        }
    }

    @Override
    public void fatal(Message msg, Throwable t) {
        if (this.isEnabled(Level.FATAL, null, msg, t)) {
            this.log(null, FQCN, Level.FATAL, msg, t);
        }
    }

    @Override
    public void fatal(Object message) {
        if (this.isEnabled(Level.FATAL, null, message, null)) {
            this.log(null, FQCN, Level.FATAL, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void fatal(Object message, Throwable t) {
        if (this.isEnabled(Level.FATAL, null, message, t)) {
            this.log(null, FQCN, Level.FATAL, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void fatal(String message) {
        if (this.isEnabled(Level.FATAL, null, message)) {
            this.log(null, FQCN, Level.FATAL, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void fatal(String message, Object ... params) {
        if (this.isEnabled(Level.FATAL, null, message, params)) {
            Message msg = this.messageFactory.newMessage(message, params);
            this.log(null, FQCN, Level.FATAL, msg, msg.getThrowable());
        }
    }

    @Override
    public void fatal(String message, Throwable t) {
        if (this.isEnabled(Level.FATAL, (Marker)null, message, t)) {
            this.log(null, FQCN, Level.FATAL, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public MessageFactory getMessageFactory() {
        return this.messageFactory;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void info(Marker marker, Message msg) {
        if (this.isEnabled(Level.INFO, marker, msg, null)) {
            this.log(marker, FQCN, Level.INFO, msg, null);
        }
    }

    @Override
    public void info(Marker marker, Message msg, Throwable t) {
        if (this.isEnabled(Level.INFO, marker, msg, t)) {
            this.log(marker, FQCN, Level.INFO, msg, t);
        }
    }

    @Override
    public void info(Marker marker, Object message) {
        if (this.isEnabled(Level.INFO, marker, message, null)) {
            this.log(marker, FQCN, Level.INFO, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void info(Marker marker, Object message, Throwable t) {
        if (this.isEnabled(Level.INFO, marker, message, t)) {
            this.log(marker, FQCN, Level.INFO, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void info(Marker marker, String message) {
        if (this.isEnabled(Level.INFO, marker, message)) {
            this.log(marker, FQCN, Level.INFO, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void info(Marker marker, String message, Object ... params) {
        if (this.isEnabled(Level.INFO, marker, message, params)) {
            Message msg = this.messageFactory.newMessage(message, params);
            this.log(marker, FQCN, Level.INFO, msg, msg.getThrowable());
        }
    }

    @Override
    public void info(Marker marker, String message, Throwable t) {
        if (this.isEnabled(Level.INFO, marker, message, t)) {
            this.log(marker, FQCN, Level.INFO, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void info(Message msg) {
        if (this.isEnabled(Level.INFO, null, msg, null)) {
            this.log(null, FQCN, Level.INFO, msg, null);
        }
    }

    @Override
    public void info(Message msg, Throwable t) {
        if (this.isEnabled(Level.INFO, null, msg, t)) {
            this.log(null, FQCN, Level.INFO, msg, t);
        }
    }

    @Override
    public void info(Object message) {
        if (this.isEnabled(Level.INFO, null, message, null)) {
            this.log(null, FQCN, Level.INFO, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void info(Object message, Throwable t) {
        if (this.isEnabled(Level.INFO, null, message, t)) {
            this.log(null, FQCN, Level.INFO, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void info(String message) {
        if (this.isEnabled(Level.INFO, null, message)) {
            this.log(null, FQCN, Level.INFO, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void info(String message, Object ... params) {
        if (this.isEnabled(Level.INFO, null, message, params)) {
            Message msg = this.messageFactory.newMessage(message, params);
            this.log(null, FQCN, Level.INFO, msg, msg.getThrowable());
        }
    }

    @Override
    public void info(String message, Throwable t) {
        if (this.isEnabled(Level.INFO, (Marker)null, message, t)) {
            this.log(null, FQCN, Level.INFO, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return this.isEnabled(Level.DEBUG, null, null);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.isEnabled(Level.DEBUG, marker, (Object)null, null);
    }

    @Override
    public boolean isEnabled(Level level) {
        return this.isEnabled(level, null, (Object)null, null);
    }

    protected abstract boolean isEnabled(Level var1, Marker var2, Message var3, Throwable var4);

    protected abstract boolean isEnabled(Level var1, Marker var2, Object var3, Throwable var4);

    protected abstract boolean isEnabled(Level var1, Marker var2, String var3);

    protected abstract boolean isEnabled(Level var1, Marker var2, String var3, Object ... var4);

    protected abstract boolean isEnabled(Level var1, Marker var2, String var3, Throwable var4);

    @Override
    public boolean isErrorEnabled() {
        return this.isEnabled(Level.ERROR, null, (Object)null, null);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.isEnabled(Level.ERROR, marker, (Object)null, null);
    }

    @Override
    public boolean isFatalEnabled() {
        return this.isEnabled(Level.FATAL, null, (Object)null, null);
    }

    @Override
    public boolean isFatalEnabled(Marker marker) {
        return this.isEnabled(Level.FATAL, marker, (Object)null, null);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.isEnabled(Level.INFO, null, (Object)null, null);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return this.isEnabled(Level.INFO, marker, (Object)null, null);
    }

    @Override
    public boolean isTraceEnabled() {
        return this.isEnabled(Level.TRACE, null, (Object)null, null);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.isEnabled(Level.TRACE, marker, (Object)null, null);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.isEnabled(Level.WARN, null, (Object)null, null);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.isEnabled(Level.WARN, marker, (Object)null, null);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker) {
        return this.isEnabled(level, marker, (Object)null, null);
    }

    @Override
    public void log(Level level, Marker marker, Message msg) {
        if (this.isEnabled(level, marker, msg, null)) {
            this.log(marker, FQCN, level, msg, null);
        }
    }

    @Override
    public void log(Level level, Marker marker, Message msg, Throwable t) {
        if (this.isEnabled(level, marker, msg, t)) {
            this.log(marker, FQCN, level, msg, t);
        }
    }

    @Override
    public void log(Level level, Marker marker, Object message) {
        if (this.isEnabled(level, marker, message, null)) {
            this.log(marker, FQCN, level, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void log(Level level, Marker marker, Object message, Throwable t) {
        if (this.isEnabled(level, marker, message, t)) {
            this.log(marker, FQCN, level, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void log(Level level, Marker marker, String message) {
        if (this.isEnabled(level, marker, message)) {
            this.log(marker, FQCN, level, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void log(Level level, Marker marker, String message, Object ... params) {
        if (this.isEnabled(level, marker, message, params)) {
            Message msg = this.messageFactory.newMessage(message, params);
            this.log(marker, FQCN, level, msg, msg.getThrowable());
        }
    }

    @Override
    public void log(Level level, Marker marker, String message, Throwable t) {
        if (this.isEnabled(level, marker, message, t)) {
            this.log(marker, FQCN, level, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void log(Level level, Message msg) {
        if (this.isEnabled(level, null, msg, null)) {
            this.log(null, FQCN, level, msg, null);
        }
    }

    @Override
    public void log(Level level, Message msg, Throwable t) {
        if (this.isEnabled(level, null, msg, t)) {
            this.log(null, FQCN, level, msg, t);
        }
    }

    @Override
    public void log(Level level, Object message) {
        if (this.isEnabled(level, null, message, null)) {
            this.log(null, FQCN, level, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void log(Level level, Object message, Throwable t) {
        if (this.isEnabled(level, null, message, t)) {
            this.log(null, FQCN, level, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void log(Level level, String message) {
        if (this.isEnabled(level, null, message)) {
            this.log(null, FQCN, level, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void log(Level level, String message, Object ... params) {
        if (this.isEnabled(level, null, message, params)) {
            Message msg = this.messageFactory.newMessage(message, params);
            this.log(null, FQCN, level, msg, msg.getThrowable());
        }
    }

    @Override
    public void log(Level level, String message, Throwable t) {
        if (this.isEnabled(level, (Marker)null, message, t)) {
            this.log(null, FQCN, level, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void printf(Level level, String format, Object ... params) {
        if (this.isEnabled(level, null, format, params)) {
            StringFormattedMessage msg = new StringFormattedMessage(format, params);
            this.log(null, FQCN, level, msg, msg.getThrowable());
        }
    }

    @Override
    public void printf(Level level, Marker marker, String format, Object ... params) {
        if (this.isEnabled(level, marker, format, params)) {
            StringFormattedMessage msg = new StringFormattedMessage(format, params);
            this.log(marker, FQCN, level, msg, msg.getThrowable());
        }
    }

    public abstract void log(Marker var1, String var2, Level var3, Message var4, Throwable var5);

    @Override
    public <T extends Throwable> T throwing(Level level, T t) {
        return this.throwing(FQCN, level, t);
    }

    @Override
    public <T extends Throwable> T throwing(T t) {
        return this.throwing(FQCN, Level.ERROR, t);
    }

    protected <T extends Throwable> T throwing(String fqcn, Level level, T t) {
        if (this.isEnabled(level, THROWING_MARKER, (Object)null, null)) {
            this.log(THROWING_MARKER, fqcn, level, this.messageFactory.newMessage("throwing"), t);
        }
        return t;
    }

    private Message toExitMsg(Object result) {
        if (result == null) {
            return this.messageFactory.newMessage("exit");
        }
        return this.messageFactory.newMessage("exit with(" + result + ")");
    }

    public String toString() {
        return this.name;
    }

    @Override
    public void trace(Marker marker, Message msg) {
        if (this.isEnabled(Level.TRACE, marker, msg, null)) {
            this.log(marker, FQCN, Level.TRACE, msg, null);
        }
    }

    @Override
    public void trace(Marker marker, Message msg, Throwable t) {
        if (this.isEnabled(Level.TRACE, marker, msg, t)) {
            this.log(marker, FQCN, Level.TRACE, msg, t);
        }
    }

    @Override
    public void trace(Marker marker, Object message) {
        if (this.isEnabled(Level.TRACE, marker, message, null)) {
            this.log(marker, FQCN, Level.TRACE, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void trace(Marker marker, Object message, Throwable t) {
        if (this.isEnabled(Level.TRACE, marker, message, t)) {
            this.log(marker, FQCN, Level.TRACE, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void trace(Marker marker, String message) {
        if (this.isEnabled(Level.TRACE, marker, message)) {
            this.log(marker, FQCN, Level.TRACE, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void trace(Marker marker, String message, Object ... params) {
        if (this.isEnabled(Level.TRACE, marker, message, params)) {
            Message msg = this.messageFactory.newMessage(message, params);
            this.log(marker, FQCN, Level.TRACE, msg, msg.getThrowable());
        }
    }

    @Override
    public void trace(Marker marker, String message, Throwable t) {
        if (this.isEnabled(Level.TRACE, marker, message, t)) {
            this.log(marker, FQCN, Level.TRACE, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void trace(Message msg) {
        if (this.isEnabled(Level.TRACE, null, msg, null)) {
            this.log(null, FQCN, Level.TRACE, msg, null);
        }
    }

    @Override
    public void trace(Message msg, Throwable t) {
        if (this.isEnabled(Level.TRACE, null, msg, t)) {
            this.log(null, FQCN, Level.TRACE, msg, t);
        }
    }

    @Override
    public void trace(Object message) {
        if (this.isEnabled(Level.TRACE, null, message, null)) {
            this.log(null, FQCN, Level.TRACE, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void trace(Object message, Throwable t) {
        if (this.isEnabled(Level.TRACE, null, message, t)) {
            this.log(null, FQCN, Level.TRACE, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void trace(String message) {
        if (this.isEnabled(Level.TRACE, null, message)) {
            this.log(null, FQCN, Level.TRACE, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void trace(String message, Object ... params) {
        if (this.isEnabled(Level.TRACE, null, message, params)) {
            Message msg = this.messageFactory.newMessage(message, params);
            this.log(null, FQCN, Level.TRACE, msg, msg.getThrowable());
        }
    }

    @Override
    public void trace(String message, Throwable t) {
        if (this.isEnabled(Level.TRACE, (Marker)null, message, t)) {
            this.log(null, FQCN, Level.TRACE, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void warn(Marker marker, Message msg) {
        if (this.isEnabled(Level.WARN, marker, msg, null)) {
            this.log(marker, FQCN, Level.WARN, msg, null);
        }
    }

    @Override
    public void warn(Marker marker, Message msg, Throwable t) {
        if (this.isEnabled(Level.WARN, marker, msg, t)) {
            this.log(marker, FQCN, Level.WARN, msg, t);
        }
    }

    @Override
    public void warn(Marker marker, Object message) {
        if (this.isEnabled(Level.WARN, marker, message, null)) {
            this.log(marker, FQCN, Level.WARN, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void warn(Marker marker, Object message, Throwable t) {
        if (this.isEnabled(Level.WARN, marker, message, t)) {
            this.log(marker, FQCN, Level.WARN, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void warn(Marker marker, String message) {
        if (this.isEnabled(Level.WARN, marker, message)) {
            this.log(marker, FQCN, Level.WARN, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void warn(Marker marker, String message, Object ... params) {
        if (this.isEnabled(Level.WARN, marker, message, params)) {
            Message msg = this.messageFactory.newMessage(message, params);
            this.log(marker, FQCN, Level.WARN, msg, msg.getThrowable());
        }
    }

    @Override
    public void warn(Marker marker, String message, Throwable t) {
        if (this.isEnabled(Level.WARN, marker, message, t)) {
            this.log(marker, FQCN, Level.WARN, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void warn(Message msg) {
        if (this.isEnabled(Level.WARN, null, msg, null)) {
            this.log(null, FQCN, Level.WARN, msg, null);
        }
    }

    @Override
    public void warn(Message msg, Throwable t) {
        if (this.isEnabled(Level.WARN, null, msg, t)) {
            this.log(null, FQCN, Level.WARN, msg, t);
        }
    }

    @Override
    public void warn(Object message) {
        if (this.isEnabled(Level.WARN, null, message, null)) {
            this.log(null, FQCN, Level.WARN, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void warn(Object message, Throwable t) {
        if (this.isEnabled(Level.WARN, null, message, t)) {
            this.log(null, FQCN, Level.WARN, this.messageFactory.newMessage(message), t);
        }
    }

    @Override
    public void warn(String message) {
        if (this.isEnabled(Level.WARN, null, message)) {
            this.log(null, FQCN, Level.WARN, this.messageFactory.newMessage(message), null);
        }
    }

    @Override
    public void warn(String message, Object ... params) {
        if (this.isEnabled(Level.WARN, null, message, params)) {
            Message msg = this.messageFactory.newMessage(message, params);
            this.log(null, FQCN, Level.WARN, msg, msg.getThrowable());
        }
    }

    @Override
    public void warn(String message, Throwable t) {
        if (this.isEnabled(Level.WARN, (Marker)null, message, t)) {
            this.log(null, FQCN, Level.WARN, this.messageFactory.newMessage(message), t);
        }
    }
}

