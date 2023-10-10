// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.MarkerManager;
import java.io.IOException;
import java.lang.reflect.Field;
import java.io.ObjectInputStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogBuilder;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.apache.logging.log4j.util.LambdaUtil;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;
import org.apache.logging.log4j.message.DefaultFlowMessageFactory;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.Constants;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.apache.logging.log4j.message.ReusableMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.internal.DefaultLogBuilder;
import org.apache.logging.log4j.message.MessageFactory2;
import org.apache.logging.log4j.message.FlowMessageFactory;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.Marker;
import java.io.Serializable;

public abstract class AbstractLogger implements ExtendedLogger, LocationAwareLogger, Serializable
{
    public static final Marker FLOW_MARKER;
    public static final Marker ENTRY_MARKER;
    public static final Marker EXIT_MARKER;
    public static final Marker EXCEPTION_MARKER;
    public static final Marker THROWING_MARKER;
    public static final Marker CATCHING_MARKER;
    public static final Class<? extends MessageFactory> DEFAULT_MESSAGE_FACTORY_CLASS;
    public static final Class<? extends FlowMessageFactory> DEFAULT_FLOW_MESSAGE_FACTORY_CLASS;
    private static final long serialVersionUID = 2L;
    private static final String FQCN;
    private static final String THROWING = "Throwing";
    private static final String CATCHING = "Catching";
    protected final String name;
    private final MessageFactory2 messageFactory;
    private final FlowMessageFactory flowMessageFactory;
    private static final ThreadLocal<int[]> recursionDepthHolder;
    protected final transient ThreadLocal<DefaultLogBuilder> logBuilder;
    
    public AbstractLogger() {
        this.name = this.getClass().getName();
        this.messageFactory = createDefaultMessageFactory();
        this.flowMessageFactory = createDefaultFlowMessageFactory();
        this.logBuilder = new LocalLogBuilder(this);
    }
    
    public AbstractLogger(final String name) {
        this(name, createDefaultMessageFactory());
    }
    
    public AbstractLogger(final String name, final MessageFactory messageFactory) {
        this.name = name;
        this.messageFactory = ((messageFactory == null) ? createDefaultMessageFactory() : narrow(messageFactory));
        this.flowMessageFactory = createDefaultFlowMessageFactory();
        this.logBuilder = new LocalLogBuilder(this);
    }
    
    public static void checkMessageFactory(final ExtendedLogger logger, final MessageFactory messageFactory) {
        final String name = logger.getName();
        final MessageFactory loggerMessageFactory = logger.getMessageFactory();
        if (messageFactory != null && !loggerMessageFactory.equals(messageFactory)) {
            StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with the message factory {}, which may create log events with unexpected formatting.", name, loggerMessageFactory, messageFactory);
        }
        else if (messageFactory == null && !loggerMessageFactory.getClass().equals(AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS)) {
            StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with a null message factory (defaults to {}), which may create log events with unexpected formatting.", name, loggerMessageFactory, AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS.getName());
        }
    }
    
    @Override
    public void catching(final Level level, final Throwable throwable) {
        this.catching(AbstractLogger.FQCN, level, throwable);
    }
    
    protected void catching(final String fqcn, final Level level, final Throwable throwable) {
        if (this.isEnabled(level, AbstractLogger.CATCHING_MARKER, (Object)null, null)) {
            this.logMessageSafely(fqcn, level, AbstractLogger.CATCHING_MARKER, this.catchingMsg(throwable), throwable);
        }
    }
    
    @Override
    public void catching(final Throwable throwable) {
        if (this.isEnabled(Level.ERROR, AbstractLogger.CATCHING_MARKER, (Object)null, null)) {
            this.logMessageSafely(AbstractLogger.FQCN, Level.ERROR, AbstractLogger.CATCHING_MARKER, this.catchingMsg(throwable), throwable);
        }
    }
    
    protected Message catchingMsg(final Throwable throwable) {
        return this.messageFactory.newMessage("Catching");
    }
    
    private static Class<? extends MessageFactory> createClassForProperty(final String property, final Class<ReusableMessageFactory> reusableParameterizedMessageFactoryClass, final Class<ParameterizedMessageFactory> parameterizedMessageFactoryClass) {
        try {
            final String fallback = Constants.ENABLE_THREADLOCALS ? reusableParameterizedMessageFactoryClass.getName() : parameterizedMessageFactoryClass.getName();
            final String clsName = PropertiesUtil.getProperties().getStringProperty(property, fallback);
            return LoaderUtil.loadClass(clsName).asSubclass(MessageFactory.class);
        }
        catch (Throwable throwable) {
            return parameterizedMessageFactoryClass;
        }
    }
    
    private static Class<? extends FlowMessageFactory> createFlowClassForProperty(final String property, final Class<DefaultFlowMessageFactory> defaultFlowMessageFactoryClass) {
        try {
            final String clsName = PropertiesUtil.getProperties().getStringProperty(property, defaultFlowMessageFactoryClass.getName());
            return LoaderUtil.loadClass(clsName).asSubclass(FlowMessageFactory.class);
        }
        catch (Throwable throwable) {
            return defaultFlowMessageFactoryClass;
        }
    }
    
    private static MessageFactory2 createDefaultMessageFactory() {
        try {
            final MessageFactory result = (MessageFactory)AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS.newInstance();
            return narrow(result);
        }
        catch (InstantiationException | IllegalAccessException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            throw new IllegalStateException(e);
        }
    }
    
    private static MessageFactory2 narrow(final MessageFactory result) {
        if (result instanceof MessageFactory2) {
            return (MessageFactory2)result;
        }
        return new MessageFactory2Adapter(result);
    }
    
    private static FlowMessageFactory createDefaultFlowMessageFactory() {
        try {
            return (FlowMessageFactory)AbstractLogger.DEFAULT_FLOW_MESSAGE_FACTORY_CLASS.newInstance();
        }
        catch (InstantiationException | IllegalAccessException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            throw new IllegalStateException(e);
        }
    }
    
    @Override
    public void debug(final Marker marker, final CharSequence message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, null);
    }
    
    @Override
    public void debug(final Marker marker, final CharSequence message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, throwable);
    }
    
    @Override
    public void debug(final Marker marker, final Message message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, (message != null) ? message.getThrowable() : null);
    }
    
    @Override
    public void debug(final Marker marker, final Message message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, throwable);
    }
    
    @Override
    public void debug(final Marker marker, final Object message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, null);
    }
    
    @Override
    public void debug(final Marker marker, final Object message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, throwable);
    }
    
    @Override
    public void debug(final Marker marker, final String message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, (Throwable)null);
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Object... params) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, params);
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, throwable);
    }
    
    @Override
    public void debug(final Message message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, (message != null) ? message.getThrowable() : null);
    }
    
    @Override
    public void debug(final Message message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, throwable);
    }
    
    @Override
    public void debug(final CharSequence message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, null);
    }
    
    @Override
    public void debug(final CharSequence message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, throwable);
    }
    
    @Override
    public void debug(final Object message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, null);
    }
    
    @Override
    public void debug(final Object message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, throwable);
    }
    
    @Override
    public void debug(final String message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, (Throwable)null);
    }
    
    @Override
    public void debug(final String message, final Object... params) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, params);
    }
    
    @Override
    public void debug(final String message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, throwable);
    }
    
    @Override
    public void debug(final Supplier<?> messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, messageSupplier, null);
    }
    
    @Override
    public void debug(final Supplier<?> messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, messageSupplier, throwable);
    }
    
    @Override
    public void debug(final Marker marker, final Supplier<?> messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, messageSupplier, null);
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, paramSuppliers);
    }
    
    @Override
    public void debug(final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, messageSupplier, throwable);
    }
    
    @Override
    public void debug(final String message, final Supplier<?>... paramSuppliers) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, paramSuppliers);
    }
    
    @Override
    public void debug(final Marker marker, final MessageSupplier messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, messageSupplier, null);
    }
    
    @Override
    public void debug(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, messageSupplier, throwable);
    }
    
    @Override
    public void debug(final MessageSupplier messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, messageSupplier, null);
    }
    
    @Override
    public void debug(final MessageSupplier messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, messageSupplier, throwable);
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Object p0) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, p0);
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Object p0, final Object p1) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, p0, p1);
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, p0, p1, p2);
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3);
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    @Override
    public void debug(final String message, final Object p0) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, p0);
    }
    
    @Override
    public void debug(final String message, final Object p0, final Object p1) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, p0, p1);
    }
    
    @Override
    public void debug(final String message, final Object p0, final Object p1, final Object p2) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, p0, p1, p2);
    }
    
    @Override
    public void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, p0, p1, p2, p3);
    }
    
    @Override
    public void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.DEBUG, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    protected EntryMessage enter(final String fqcn, final String format, final Supplier<?>... paramSuppliers) {
        EntryMessage entryMsg = null;
        if (this.isEnabled(Level.TRACE, AbstractLogger.ENTRY_MARKER, (Object)null, null)) {
            this.logMessageSafely(fqcn, Level.TRACE, AbstractLogger.ENTRY_MARKER, entryMsg = this.entryMsg(format, paramSuppliers), null);
        }
        return entryMsg;
    }
    
    @Deprecated
    protected EntryMessage enter(final String fqcn, final String format, final MessageSupplier... paramSuppliers) {
        EntryMessage entryMsg = null;
        if (this.isEnabled(Level.TRACE, AbstractLogger.ENTRY_MARKER, (Object)null, null)) {
            this.logMessageSafely(fqcn, Level.TRACE, AbstractLogger.ENTRY_MARKER, entryMsg = this.entryMsg(format, paramSuppliers), null);
        }
        return entryMsg;
    }
    
    protected EntryMessage enter(final String fqcn, final String format, final Object... params) {
        EntryMessage entryMsg = null;
        if (this.isEnabled(Level.TRACE, AbstractLogger.ENTRY_MARKER, (Object)null, null)) {
            this.logMessageSafely(fqcn, Level.TRACE, AbstractLogger.ENTRY_MARKER, entryMsg = this.entryMsg(format, params), null);
        }
        return entryMsg;
    }
    
    @Deprecated
    protected EntryMessage enter(final String fqcn, final MessageSupplier messageSupplier) {
        EntryMessage message = null;
        if (this.isEnabled(Level.TRACE, AbstractLogger.ENTRY_MARKER, (Object)null, null)) {
            this.logMessageSafely(fqcn, Level.TRACE, AbstractLogger.ENTRY_MARKER, message = this.flowMessageFactory.newEntryMessage(messageSupplier.get()), null);
        }
        return message;
    }
    
    protected EntryMessage enter(final String fqcn, final Message message) {
        EntryMessage flowMessage = null;
        if (this.isEnabled(Level.TRACE, AbstractLogger.ENTRY_MARKER, (Object)null, null)) {
            this.logMessageSafely(fqcn, Level.TRACE, AbstractLogger.ENTRY_MARKER, flowMessage = this.flowMessageFactory.newEntryMessage(message), null);
        }
        return flowMessage;
    }
    
    @Deprecated
    @Override
    public void entry() {
        this.entry(AbstractLogger.FQCN, (Object[])null);
    }
    
    @Override
    public void entry(final Object... params) {
        this.entry(AbstractLogger.FQCN, params);
    }
    
    protected void entry(final String fqcn, final Object... params) {
        if (this.isEnabled(Level.TRACE, AbstractLogger.ENTRY_MARKER, (Object)null, null)) {
            if (params == null) {
                this.logMessageSafely(fqcn, Level.TRACE, AbstractLogger.ENTRY_MARKER, this.entryMsg(null, (Supplier<?>[])null), null);
            }
            else {
                this.logMessageSafely(fqcn, Level.TRACE, AbstractLogger.ENTRY_MARKER, this.entryMsg(null, params), null);
            }
        }
    }
    
    protected EntryMessage entryMsg(final String format, final Object... params) {
        final int count = (params == null) ? 0 : params.length;
        if (count == 0) {
            if (Strings.isEmpty(format)) {
                return this.flowMessageFactory.newEntryMessage(null);
            }
            return this.flowMessageFactory.newEntryMessage(new SimpleMessage(format));
        }
        else {
            if (format != null) {
                return this.flowMessageFactory.newEntryMessage(new ParameterizedMessage(format, params));
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("params(");
            for (int i = 0; i < count; ++i) {
                if (i > 0) {
                    sb.append(", ");
                }
                final Object parm = params[i];
                sb.append((parm instanceof Message) ? ((Message)parm).getFormattedMessage() : String.valueOf(parm));
            }
            sb.append(')');
            return this.flowMessageFactory.newEntryMessage(new SimpleMessage(sb));
        }
    }
    
    protected EntryMessage entryMsg(final String format, final MessageSupplier... paramSuppliers) {
        final int count = (paramSuppliers == null) ? 0 : paramSuppliers.length;
        final Object[] params = new Object[count];
        for (int i = 0; i < count; ++i) {
            params[i] = paramSuppliers[i].get();
            params[i] = ((params[i] != null) ? ((Message)params[i]).getFormattedMessage() : null);
        }
        return this.entryMsg(format, params);
    }
    
    protected EntryMessage entryMsg(final String format, final Supplier<?>... paramSuppliers) {
        final int count = (paramSuppliers == null) ? 0 : paramSuppliers.length;
        final Object[] params = new Object[count];
        for (int i = 0; i < count; ++i) {
            params[i] = paramSuppliers[i].get();
            if (params[i] instanceof Message) {
                params[i] = ((Message)params[i]).getFormattedMessage();
            }
        }
        return this.entryMsg(format, params);
    }
    
    @Override
    public void error(final Marker marker, final Message message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, (message != null) ? message.getThrowable() : null);
    }
    
    @Override
    public void error(final Marker marker, final Message message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, throwable);
    }
    
    @Override
    public void error(final Marker marker, final CharSequence message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, null);
    }
    
    @Override
    public void error(final Marker marker, final CharSequence message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, throwable);
    }
    
    @Override
    public void error(final Marker marker, final Object message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, null);
    }
    
    @Override
    public void error(final Marker marker, final Object message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, throwable);
    }
    
    @Override
    public void error(final Marker marker, final String message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, (Throwable)null);
    }
    
    @Override
    public void error(final Marker marker, final String message, final Object... params) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, params);
    }
    
    @Override
    public void error(final Marker marker, final String message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, throwable);
    }
    
    @Override
    public void error(final Message message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, (message != null) ? message.getThrowable() : null);
    }
    
    @Override
    public void error(final Message message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, throwable);
    }
    
    @Override
    public void error(final CharSequence message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, null);
    }
    
    @Override
    public void error(final CharSequence message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, throwable);
    }
    
    @Override
    public void error(final Object message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, null);
    }
    
    @Override
    public void error(final Object message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, throwable);
    }
    
    @Override
    public void error(final String message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, (Throwable)null);
    }
    
    @Override
    public void error(final String message, final Object... params) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, params);
    }
    
    @Override
    public void error(final String message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, throwable);
    }
    
    @Override
    public void error(final Supplier<?> messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, messageSupplier, null);
    }
    
    @Override
    public void error(final Supplier<?> messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, messageSupplier, throwable);
    }
    
    @Override
    public void error(final Marker marker, final Supplier<?> messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, messageSupplier, null);
    }
    
    @Override
    public void error(final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, paramSuppliers);
    }
    
    @Override
    public void error(final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, messageSupplier, throwable);
    }
    
    @Override
    public void error(final String message, final Supplier<?>... paramSuppliers) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, paramSuppliers);
    }
    
    @Override
    public void error(final Marker marker, final MessageSupplier messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, messageSupplier, null);
    }
    
    @Override
    public void error(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, messageSupplier, throwable);
    }
    
    @Override
    public void error(final MessageSupplier messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, messageSupplier, null);
    }
    
    @Override
    public void error(final MessageSupplier messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, messageSupplier, throwable);
    }
    
    @Override
    public void error(final Marker marker, final String message, final Object p0) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, p0);
    }
    
    @Override
    public void error(final Marker marker, final String message, final Object p0, final Object p1) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, p0, p1);
    }
    
    @Override
    public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, p0, p1, p2);
    }
    
    @Override
    public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, p0, p1, p2, p3);
    }
    
    @Override
    public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    @Override
    public void error(final String message, final Object p0) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, p0);
    }
    
    @Override
    public void error(final String message, final Object p0, final Object p1) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, p0, p1);
    }
    
    @Override
    public void error(final String message, final Object p0, final Object p1, final Object p2) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, p0, p1, p2);
    }
    
    @Override
    public void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, p0, p1, p2, p3);
    }
    
    @Override
    public void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.ERROR, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    @Deprecated
    @Override
    public void exit() {
        this.exit(AbstractLogger.FQCN, (Object)null);
    }
    
    @Deprecated
    @Override
    public <R> R exit(final R result) {
        return this.exit(AbstractLogger.FQCN, result);
    }
    
    protected <R> R exit(final String fqcn, final R result) {
        if (this.isEnabled(Level.TRACE, AbstractLogger.EXIT_MARKER, (CharSequence)null, null)) {
            this.logMessageSafely(fqcn, Level.TRACE, AbstractLogger.EXIT_MARKER, this.exitMsg(null, result), null);
        }
        return result;
    }
    
    protected <R> R exit(final String fqcn, final String format, final R result) {
        if (this.isEnabled(Level.TRACE, AbstractLogger.EXIT_MARKER, (CharSequence)null, null)) {
            this.logMessageSafely(fqcn, Level.TRACE, AbstractLogger.EXIT_MARKER, this.exitMsg(format, result), null);
        }
        return result;
    }
    
    protected Message exitMsg(final String format, final Object result) {
        if (result == null) {
            if (format == null) {
                return this.messageFactory.newMessage("Exit");
            }
            return this.messageFactory.newMessage("Exit: " + format);
        }
        else {
            if (format == null) {
                return this.messageFactory.newMessage("Exit with(" + result + ')');
            }
            return this.messageFactory.newMessage("Exit: " + format, result);
        }
    }
    
    @Override
    public void fatal(final Marker marker, final Message message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, (message != null) ? message.getThrowable() : null);
    }
    
    @Override
    public void fatal(final Marker marker, final Message message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, throwable);
    }
    
    @Override
    public void fatal(final Marker marker, final CharSequence message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, null);
    }
    
    @Override
    public void fatal(final Marker marker, final CharSequence message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, throwable);
    }
    
    @Override
    public void fatal(final Marker marker, final Object message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, null);
    }
    
    @Override
    public void fatal(final Marker marker, final Object message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, throwable);
    }
    
    @Override
    public void fatal(final Marker marker, final String message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, (Throwable)null);
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Object... params) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, params);
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, throwable);
    }
    
    @Override
    public void fatal(final Message message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, (message != null) ? message.getThrowable() : null);
    }
    
    @Override
    public void fatal(final Message message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, throwable);
    }
    
    @Override
    public void fatal(final CharSequence message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, null);
    }
    
    @Override
    public void fatal(final CharSequence message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, throwable);
    }
    
    @Override
    public void fatal(final Object message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, null);
    }
    
    @Override
    public void fatal(final Object message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, throwable);
    }
    
    @Override
    public void fatal(final String message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, (Throwable)null);
    }
    
    @Override
    public void fatal(final String message, final Object... params) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, params);
    }
    
    @Override
    public void fatal(final String message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, throwable);
    }
    
    @Override
    public void fatal(final Supplier<?> messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, messageSupplier, null);
    }
    
    @Override
    public void fatal(final Supplier<?> messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, messageSupplier, throwable);
    }
    
    @Override
    public void fatal(final Marker marker, final Supplier<?> messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, messageSupplier, null);
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, paramSuppliers);
    }
    
    @Override
    public void fatal(final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, messageSupplier, throwable);
    }
    
    @Override
    public void fatal(final String message, final Supplier<?>... paramSuppliers) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, paramSuppliers);
    }
    
    @Override
    public void fatal(final Marker marker, final MessageSupplier messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, messageSupplier, null);
    }
    
    @Override
    public void fatal(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, messageSupplier, throwable);
    }
    
    @Override
    public void fatal(final MessageSupplier messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, messageSupplier, null);
    }
    
    @Override
    public void fatal(final MessageSupplier messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, messageSupplier, throwable);
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Object p0) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, p0);
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Object p0, final Object p1) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, p0, p1);
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, p0, p1, p2);
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, p0, p1, p2, p3);
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    @Override
    public void fatal(final String message, final Object p0) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, p0);
    }
    
    @Override
    public void fatal(final String message, final Object p0, final Object p1) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, p0, p1);
    }
    
    @Override
    public void fatal(final String message, final Object p0, final Object p1, final Object p2) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, p0, p1, p2);
    }
    
    @Override
    public void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, p0, p1, p2, p3);
    }
    
    @Override
    public void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.FATAL, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    @Override
    public <MF extends MessageFactory> MF getMessageFactory() {
        return (MF)this.messageFactory;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void info(final Marker marker, final Message message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, (message != null) ? message.getThrowable() : null);
    }
    
    @Override
    public void info(final Marker marker, final Message message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, throwable);
    }
    
    @Override
    public void info(final Marker marker, final CharSequence message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, null);
    }
    
    @Override
    public void info(final Marker marker, final CharSequence message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, throwable);
    }
    
    @Override
    public void info(final Marker marker, final Object message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, null);
    }
    
    @Override
    public void info(final Marker marker, final Object message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, throwable);
    }
    
    @Override
    public void info(final Marker marker, final String message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, (Throwable)null);
    }
    
    @Override
    public void info(final Marker marker, final String message, final Object... params) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, params);
    }
    
    @Override
    public void info(final Marker marker, final String message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, throwable);
    }
    
    @Override
    public void info(final Message message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, (message != null) ? message.getThrowable() : null);
    }
    
    @Override
    public void info(final Message message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, throwable);
    }
    
    @Override
    public void info(final CharSequence message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, null);
    }
    
    @Override
    public void info(final CharSequence message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, throwable);
    }
    
    @Override
    public void info(final Object message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, null);
    }
    
    @Override
    public void info(final Object message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, throwable);
    }
    
    @Override
    public void info(final String message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, (Throwable)null);
    }
    
    @Override
    public void info(final String message, final Object... params) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, params);
    }
    
    @Override
    public void info(final String message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, throwable);
    }
    
    @Override
    public void info(final Supplier<?> messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, messageSupplier, null);
    }
    
    @Override
    public void info(final Supplier<?> messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, messageSupplier, throwable);
    }
    
    @Override
    public void info(final Marker marker, final Supplier<?> messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, messageSupplier, null);
    }
    
    @Override
    public void info(final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, paramSuppliers);
    }
    
    @Override
    public void info(final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, messageSupplier, throwable);
    }
    
    @Override
    public void info(final String message, final Supplier<?>... paramSuppliers) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, paramSuppliers);
    }
    
    @Override
    public void info(final Marker marker, final MessageSupplier messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, messageSupplier, null);
    }
    
    @Override
    public void info(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, messageSupplier, throwable);
    }
    
    @Override
    public void info(final MessageSupplier messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, messageSupplier, null);
    }
    
    @Override
    public void info(final MessageSupplier messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, messageSupplier, throwable);
    }
    
    @Override
    public void info(final Marker marker, final String message, final Object p0) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, p0);
    }
    
    @Override
    public void info(final Marker marker, final String message, final Object p0, final Object p1) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, p0, p1);
    }
    
    @Override
    public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, p0, p1, p2);
    }
    
    @Override
    public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, p0, p1, p2, p3);
    }
    
    @Override
    public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    @Override
    public void info(final String message, final Object p0) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, p0);
    }
    
    @Override
    public void info(final String message, final Object p0, final Object p1) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, p0, p1);
    }
    
    @Override
    public void info(final String message, final Object p0, final Object p1, final Object p2) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, p0, p1, p2);
    }
    
    @Override
    public void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, p0, p1, p2, p3);
    }
    
    @Override
    public void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.INFO, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.isEnabled(Level.DEBUG, null, null);
    }
    
    @Override
    public boolean isDebugEnabled(final Marker marker) {
        return this.isEnabled(Level.DEBUG, marker, (Object)null, null);
    }
    
    @Override
    public boolean isEnabled(final Level level) {
        return this.isEnabled(level, null, (Object)null, null);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker) {
        return this.isEnabled(level, marker, (Object)null, null);
    }
    
    @Override
    public boolean isErrorEnabled() {
        return this.isEnabled(Level.ERROR, null, (Object)null, null);
    }
    
    @Override
    public boolean isErrorEnabled(final Marker marker) {
        return this.isEnabled(Level.ERROR, marker, (Object)null, null);
    }
    
    @Override
    public boolean isFatalEnabled() {
        return this.isEnabled(Level.FATAL, null, (Object)null, null);
    }
    
    @Override
    public boolean isFatalEnabled(final Marker marker) {
        return this.isEnabled(Level.FATAL, marker, (Object)null, null);
    }
    
    @Override
    public boolean isInfoEnabled() {
        return this.isEnabled(Level.INFO, null, (Object)null, null);
    }
    
    @Override
    public boolean isInfoEnabled(final Marker marker) {
        return this.isEnabled(Level.INFO, marker, (Object)null, null);
    }
    
    @Override
    public boolean isTraceEnabled() {
        return this.isEnabled(Level.TRACE, null, (Object)null, null);
    }
    
    @Override
    public boolean isTraceEnabled(final Marker marker) {
        return this.isEnabled(Level.TRACE, marker, (Object)null, null);
    }
    
    @Override
    public boolean isWarnEnabled() {
        return this.isEnabled(Level.WARN, null, (Object)null, null);
    }
    
    @Override
    public boolean isWarnEnabled(final Marker marker) {
        return this.isEnabled(Level.WARN, marker, (Object)null, null);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final Message message) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, (message != null) ? message.getThrowable() : null);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final Message message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, throwable);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final CharSequence message) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, null);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final CharSequence message, final Throwable throwable) {
        if (this.isEnabled(level, marker, message, throwable)) {
            this.logMessage(AbstractLogger.FQCN, level, marker, message, throwable);
        }
    }
    
    @Override
    public void log(final Level level, final Marker marker, final Object message) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, null);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final Object message, final Throwable throwable) {
        if (this.isEnabled(level, marker, message, throwable)) {
            this.logMessage(AbstractLogger.FQCN, level, marker, message, throwable);
        }
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, (Throwable)null);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Object... params) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, params);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, throwable);
    }
    
    @Override
    public void log(final Level level, final Message message) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, (message != null) ? message.getThrowable() : null);
    }
    
    @Override
    public void log(final Level level, final Message message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, throwable);
    }
    
    @Override
    public void log(final Level level, final CharSequence message) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, null);
    }
    
    @Override
    public void log(final Level level, final CharSequence message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, throwable);
    }
    
    @Override
    public void log(final Level level, final Object message) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, null);
    }
    
    @Override
    public void log(final Level level, final Object message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, throwable);
    }
    
    @Override
    public void log(final Level level, final String message) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, (Throwable)null);
    }
    
    @Override
    public void log(final Level level, final String message, final Object... params) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, params);
    }
    
    @Override
    public void log(final Level level, final String message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, throwable);
    }
    
    @Override
    public void log(final Level level, final Supplier<?> messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, messageSupplier, null);
    }
    
    @Override
    public void log(final Level level, final Supplier<?> messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, messageSupplier, throwable);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final Supplier<?> messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, messageSupplier, null);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, paramSuppliers);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, messageSupplier, throwable);
    }
    
    @Override
    public void log(final Level level, final String message, final Supplier<?>... paramSuppliers) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, paramSuppliers);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final MessageSupplier messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, messageSupplier, null);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, messageSupplier, throwable);
    }
    
    @Override
    public void log(final Level level, final MessageSupplier messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, messageSupplier, null);
    }
    
    @Override
    public void log(final Level level, final MessageSupplier messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, messageSupplier, throwable);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Object p0) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, p0);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, p0, p1);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, p0, p1, p2);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, p0, p1, p2, p3);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.logIfEnabled(AbstractLogger.FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    @Override
    public void log(final Level level, final String message, final Object p0) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, p0);
    }
    
    @Override
    public void log(final Level level, final String message, final Object p0, final Object p1) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, p0, p1);
    }
    
    @Override
    public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, p0, p1, p2);
    }
    
    @Override
    public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, p0, p1, p2, p3);
    }
    
    @Override
    public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.logIfEnabled(AbstractLogger.FQCN, level, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable throwable) {
        if (this.isEnabled(level, marker, message, throwable)) {
            this.logMessageSafely(fqcn, level, marker, message, throwable);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable) {
        if (this.isEnabled(level, marker, messageSupplier, throwable)) {
            this.logMessage(fqcn, level, marker, messageSupplier, throwable);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final Object message, final Throwable throwable) {
        if (this.isEnabled(level, marker, message, throwable)) {
            this.logMessage(fqcn, level, marker, message, throwable);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final CharSequence message, final Throwable throwable) {
        if (this.isEnabled(level, marker, message, throwable)) {
            this.logMessage(fqcn, level, marker, message, throwable);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable) {
        if (this.isEnabled(level, marker, messageSupplier, throwable)) {
            this.logMessage(fqcn, level, marker, messageSupplier, throwable);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message) {
        if (this.isEnabled(level, marker, message)) {
            this.logMessage(fqcn, level, marker, message);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
        if (this.isEnabled(level, marker, message)) {
            this.logMessage(fqcn, level, marker, message, paramSuppliers);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object... params) {
        if (this.isEnabled(level, marker, message, params)) {
            this.logMessage(fqcn, level, marker, message, params);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0) {
        if (this.isEnabled(level, marker, message, p0)) {
            this.logMessage(fqcn, level, marker, message, p0);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1) {
        if (this.isEnabled(level, marker, message, p0, p1)) {
            this.logMessage(fqcn, level, marker, message, p0, p1);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        if (this.isEnabled(level, marker, message, p0, p1, p2)) {
            this.logMessage(fqcn, level, marker, message, p0, p1, p2);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        if (this.isEnabled(level, marker, message, p0, p1, p2, p3)) {
            this.logMessage(fqcn, level, marker, message, p0, p1, p2, p3);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        if (this.isEnabled(level, marker, message, p0, p1, p2, p3, p4)) {
            this.logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        if (this.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5)) {
            this.logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        if (this.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6)) {
            this.logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        if (this.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7)) {
            this.logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        if (this.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8)) {
            this.logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        if (this.isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9)) {
            this.logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
        }
    }
    
    @Override
    public void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String message, final Throwable throwable) {
        if (this.isEnabled(level, marker, message, throwable)) {
            this.logMessage(fqcn, level, marker, message, throwable);
        }
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final CharSequence message, final Throwable throwable) {
        this.logMessageSafely(fqcn, level, marker, this.messageFactory.newMessage(message), throwable);
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final Object message, final Throwable throwable) {
        this.logMessageSafely(fqcn, level, marker, this.messageFactory.newMessage(message), throwable);
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable) {
        final Message message = LambdaUtil.get(messageSupplier);
        final Throwable effectiveThrowable = (throwable == null && message != null) ? message.getThrowable() : throwable;
        this.logMessageSafely(fqcn, level, marker, message, effectiveThrowable);
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable) {
        final Message message = LambdaUtil.getMessage(messageSupplier, this.messageFactory);
        final Throwable effectiveThrowable = (throwable == null && message != null) ? message.getThrowable() : throwable;
        this.logMessageSafely(fqcn, level, marker, message, effectiveThrowable);
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final String message, final Throwable throwable) {
        this.logMessageSafely(fqcn, level, marker, this.messageFactory.newMessage(message), throwable);
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final String message) {
        final Message msg = this.messageFactory.newMessage(message);
        this.logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final String message, final Object... params) {
        final Message msg = this.messageFactory.newMessage(message, params);
        this.logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final String message, final Object p0) {
        final Message msg = this.messageFactory.newMessage(message, p0);
        this.logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1) {
        final Message msg = this.messageFactory.newMessage(message, p0, p1);
        this.logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        final Message msg = this.messageFactory.newMessage(message, p0, p1, p2);
        this.logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        final Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3);
        this.logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        final Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4);
        this.logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        final Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5);
        this.logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        final Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6);
        this.logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        final Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7);
        this.logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        final Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
        this.logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        final Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
        this.logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
    }
    
    protected void logMessage(final String fqcn, final Level level, final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
        final Message msg = this.messageFactory.newMessage(message, LambdaUtil.getAll(paramSuppliers));
        this.logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
    }
    
    @Override
    public void logMessage(final Level level, final Marker marker, final String fqcn, final StackTraceElement location, final Message message, final Throwable throwable) {
        try {
            incrementRecursionDepth();
            this.log(level, marker, fqcn, location, message, throwable);
        }
        catch (Exception ex) {
            this.handleLogMessageException(ex, fqcn, message);
        }
        finally {
            decrementRecursionDepth();
            ReusableMessageFactory.release(message);
        }
    }
    
    protected void log(final Level level, final Marker marker, final String fqcn, final StackTraceElement location, final Message message, final Throwable throwable) {
        this.logMessage(fqcn, level, marker, message, throwable);
    }
    
    @Override
    public void printf(final Level level, final Marker marker, final String format, final Object... params) {
        if (this.isEnabled(level, marker, format, params)) {
            final Message message = new StringFormattedMessage(format, params);
            this.logMessageSafely(AbstractLogger.FQCN, level, marker, message, message.getThrowable());
        }
    }
    
    @Override
    public void printf(final Level level, final String format, final Object... params) {
        if (this.isEnabled(level, null, format, params)) {
            final Message message = new StringFormattedMessage(format, params);
            this.logMessageSafely(AbstractLogger.FQCN, level, null, message, message.getThrowable());
        }
    }
    
    @PerformanceSensitive
    private void logMessageSafely(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable throwable) {
        try {
            this.logMessageTrackRecursion(fqcn, level, marker, message, throwable);
        }
        finally {
            ReusableMessageFactory.release(message);
        }
    }
    
    @PerformanceSensitive
    private void logMessageTrackRecursion(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable throwable) {
        try {
            incrementRecursionDepth();
            this.tryLogMessage(fqcn, this.getLocation(fqcn), level, marker, message, throwable);
        }
        finally {
            decrementRecursionDepth();
        }
    }
    
    private static int[] getRecursionDepthHolder() {
        int[] result = AbstractLogger.recursionDepthHolder.get();
        if (result == null) {
            result = new int[] { 0 };
            AbstractLogger.recursionDepthHolder.set(result);
        }
        return result;
    }
    
    private static void incrementRecursionDepth() {
        final int[] recursionDepthHolder = getRecursionDepthHolder();
        final int n = 0;
        ++recursionDepthHolder[n];
    }
    
    private static void decrementRecursionDepth() {
        final int[] recursionDepthHolder = getRecursionDepthHolder();
        final int n = 0;
        final int n2 = recursionDepthHolder[n] - 1;
        recursionDepthHolder[n] = n2;
        final int newDepth = n2;
        if (newDepth < 0) {
            throw new IllegalStateException("Recursion depth became negative: " + newDepth);
        }
    }
    
    public static int getRecursionDepth() {
        return getRecursionDepthHolder()[0];
    }
    
    @PerformanceSensitive
    private void tryLogMessage(final String fqcn, final StackTraceElement location, final Level level, final Marker marker, final Message message, final Throwable throwable) {
        try {
            this.log(level, marker, fqcn, location, message, throwable);
        }
        catch (Exception e) {
            this.handleLogMessageException(e, fqcn, message);
        }
    }
    
    @PerformanceSensitive
    private StackTraceElement getLocation(final String fqcn) {
        return this.requiresLocation() ? StackLocatorUtil.calcLocation(fqcn) : null;
    }
    
    private void handleLogMessageException(final Exception exception, final String fqcn, final Message message) {
        if (exception instanceof LoggingException) {
            throw (LoggingException)exception;
        }
        StatusLogger.getLogger().warn("{} caught {} logging {}: {}", fqcn, exception.getClass().getName(), message.getClass().getSimpleName(), message.getFormat(), exception);
    }
    
    @Override
    public <T extends Throwable> T throwing(final T throwable) {
        return this.throwing(AbstractLogger.FQCN, Level.ERROR, throwable);
    }
    
    @Override
    public <T extends Throwable> T throwing(final Level level, final T throwable) {
        return this.throwing(AbstractLogger.FQCN, level, throwable);
    }
    
    protected <T extends Throwable> T throwing(final String fqcn, final Level level, final T throwable) {
        if (this.isEnabled(level, AbstractLogger.THROWING_MARKER, (Object)null, null)) {
            this.logMessageSafely(fqcn, level, AbstractLogger.THROWING_MARKER, this.throwingMsg(throwable), throwable);
        }
        return throwable;
    }
    
    protected Message throwingMsg(final Throwable throwable) {
        return this.messageFactory.newMessage("Throwing");
    }
    
    @Override
    public void trace(final Marker marker, final Message message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, (message != null) ? message.getThrowable() : null);
    }
    
    @Override
    public void trace(final Marker marker, final Message message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, throwable);
    }
    
    @Override
    public void trace(final Marker marker, final CharSequence message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, null);
    }
    
    @Override
    public void trace(final Marker marker, final CharSequence message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, throwable);
    }
    
    @Override
    public void trace(final Marker marker, final Object message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, null);
    }
    
    @Override
    public void trace(final Marker marker, final Object message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, throwable);
    }
    
    @Override
    public void trace(final Marker marker, final String message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, (Throwable)null);
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Object... params) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, params);
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, throwable);
    }
    
    @Override
    public void trace(final Message message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, (message != null) ? message.getThrowable() : null);
    }
    
    @Override
    public void trace(final Message message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, throwable);
    }
    
    @Override
    public void trace(final CharSequence message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, null);
    }
    
    @Override
    public void trace(final CharSequence message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, throwable);
    }
    
    @Override
    public void trace(final Object message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, null);
    }
    
    @Override
    public void trace(final Object message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, throwable);
    }
    
    @Override
    public void trace(final String message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, (Throwable)null);
    }
    
    @Override
    public void trace(final String message, final Object... params) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, params);
    }
    
    @Override
    public void trace(final String message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, throwable);
    }
    
    @Override
    public void trace(final Supplier<?> messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, messageSupplier, null);
    }
    
    @Override
    public void trace(final Supplier<?> messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, messageSupplier, throwable);
    }
    
    @Override
    public void trace(final Marker marker, final Supplier<?> messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, messageSupplier, null);
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, paramSuppliers);
    }
    
    @Override
    public void trace(final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, messageSupplier, throwable);
    }
    
    @Override
    public void trace(final String message, final Supplier<?>... paramSuppliers) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, paramSuppliers);
    }
    
    @Override
    public void trace(final Marker marker, final MessageSupplier messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, messageSupplier, null);
    }
    
    @Override
    public void trace(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, messageSupplier, throwable);
    }
    
    @Override
    public void trace(final MessageSupplier messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, messageSupplier, null);
    }
    
    @Override
    public void trace(final MessageSupplier messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, messageSupplier, throwable);
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Object p0) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, p0);
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Object p0, final Object p1) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, p0, p1);
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, p0, p1, p2);
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, p0, p1, p2, p3);
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    @Override
    public void trace(final String message, final Object p0) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, p0);
    }
    
    @Override
    public void trace(final String message, final Object p0, final Object p1) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, p0, p1);
    }
    
    @Override
    public void trace(final String message, final Object p0, final Object p1, final Object p2) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, p0, p1, p2);
    }
    
    @Override
    public void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, p0, p1, p2, p3);
    }
    
    @Override
    public void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.TRACE, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    @Override
    public EntryMessage traceEntry() {
        return this.enter(AbstractLogger.FQCN, null, (Object[])null);
    }
    
    @Override
    public EntryMessage traceEntry(final String format, final Object... params) {
        return this.enter(AbstractLogger.FQCN, format, params);
    }
    
    @Override
    public EntryMessage traceEntry(final Supplier<?>... paramSuppliers) {
        return this.enter(AbstractLogger.FQCN, null, paramSuppliers);
    }
    
    @Override
    public EntryMessage traceEntry(final String format, final Supplier<?>... paramSuppliers) {
        return this.enter(AbstractLogger.FQCN, format, paramSuppliers);
    }
    
    @Override
    public EntryMessage traceEntry(final Message message) {
        return this.enter(AbstractLogger.FQCN, message);
    }
    
    @Override
    public void traceExit() {
        this.exit(AbstractLogger.FQCN, null, (Object)null);
    }
    
    @Override
    public <R> R traceExit(final R result) {
        return this.exit(AbstractLogger.FQCN, null, result);
    }
    
    @Override
    public <R> R traceExit(final String format, final R result) {
        return this.exit(AbstractLogger.FQCN, format, result);
    }
    
    @Override
    public void traceExit(final EntryMessage message) {
        if (message != null && this.isEnabled(Level.TRACE, AbstractLogger.EXIT_MARKER, message, null)) {
            this.logMessageSafely(AbstractLogger.FQCN, Level.TRACE, AbstractLogger.EXIT_MARKER, this.flowMessageFactory.newExitMessage(message), null);
        }
    }
    
    @Override
    public <R> R traceExit(final EntryMessage message, final R result) {
        if (message != null && this.isEnabled(Level.TRACE, AbstractLogger.EXIT_MARKER, message, null)) {
            this.logMessageSafely(AbstractLogger.FQCN, Level.TRACE, AbstractLogger.EXIT_MARKER, this.flowMessageFactory.newExitMessage(result, message), null);
        }
        return result;
    }
    
    @Override
    public <R> R traceExit(final Message message, final R result) {
        if (message != null && this.isEnabled(Level.TRACE, AbstractLogger.EXIT_MARKER, message, null)) {
            this.logMessageSafely(AbstractLogger.FQCN, Level.TRACE, AbstractLogger.EXIT_MARKER, this.flowMessageFactory.newExitMessage(result, message), null);
        }
        return result;
    }
    
    @Override
    public void warn(final Marker marker, final Message message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, (message != null) ? message.getThrowable() : null);
    }
    
    @Override
    public void warn(final Marker marker, final Message message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, throwable);
    }
    
    @Override
    public void warn(final Marker marker, final CharSequence message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, null);
    }
    
    @Override
    public void warn(final Marker marker, final CharSequence message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, throwable);
    }
    
    @Override
    public void warn(final Marker marker, final Object message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, null);
    }
    
    @Override
    public void warn(final Marker marker, final Object message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, throwable);
    }
    
    @Override
    public void warn(final Marker marker, final String message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, (Throwable)null);
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Object... params) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, params);
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, throwable);
    }
    
    @Override
    public void warn(final Message message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, (message != null) ? message.getThrowable() : null);
    }
    
    @Override
    public void warn(final Message message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, throwable);
    }
    
    @Override
    public void warn(final CharSequence message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, null);
    }
    
    @Override
    public void warn(final CharSequence message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, throwable);
    }
    
    @Override
    public void warn(final Object message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, null);
    }
    
    @Override
    public void warn(final Object message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, throwable);
    }
    
    @Override
    public void warn(final String message) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, (Throwable)null);
    }
    
    @Override
    public void warn(final String message, final Object... params) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, params);
    }
    
    @Override
    public void warn(final String message, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, throwable);
    }
    
    @Override
    public void warn(final Supplier<?> messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, messageSupplier, null);
    }
    
    @Override
    public void warn(final Supplier<?> messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, messageSupplier, throwable);
    }
    
    @Override
    public void warn(final Marker marker, final Supplier<?> messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, messageSupplier, null);
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Supplier<?>... paramSuppliers) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, paramSuppliers);
    }
    
    @Override
    public void warn(final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, messageSupplier, throwable);
    }
    
    @Override
    public void warn(final String message, final Supplier<?>... paramSuppliers) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, paramSuppliers);
    }
    
    @Override
    public void warn(final Marker marker, final MessageSupplier messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, messageSupplier, null);
    }
    
    @Override
    public void warn(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, messageSupplier, throwable);
    }
    
    @Override
    public void warn(final MessageSupplier messageSupplier) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, messageSupplier, null);
    }
    
    @Override
    public void warn(final MessageSupplier messageSupplier, final Throwable throwable) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, messageSupplier, throwable);
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Object p0) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, p0);
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Object p0, final Object p1) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, p0, p1);
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, p0, p1, p2);
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, p0, p1, p2, p3);
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    @Override
    public void warn(final String message, final Object p0) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, p0);
    }
    
    @Override
    public void warn(final String message, final Object p0, final Object p1) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, p0, p1);
    }
    
    @Override
    public void warn(final String message, final Object p0, final Object p1, final Object p2) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, p0, p1, p2);
    }
    
    @Override
    public void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, p0, p1, p2, p3);
    }
    
    @Override
    public void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, p0, p1, p2, p3, p4);
    }
    
    @Override
    public void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, p0, p1, p2, p3, p4, p5);
    }
    
    @Override
    public void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, p0, p1, p2, p3, p4, p5, p6);
    }
    
    @Override
    public void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }
    
    @Override
    public void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }
    
    @Override
    public void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.logIfEnabled(AbstractLogger.FQCN, Level.WARN, null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
    
    protected boolean requiresLocation() {
        return false;
    }
    
    @Override
    public LogBuilder atTrace() {
        return this.atLevel(Level.TRACE);
    }
    
    @Override
    public LogBuilder atDebug() {
        return this.atLevel(Level.DEBUG);
    }
    
    @Override
    public LogBuilder atInfo() {
        return this.atLevel(Level.INFO);
    }
    
    @Override
    public LogBuilder atWarn() {
        return this.atLevel(Level.WARN);
    }
    
    @Override
    public LogBuilder atError() {
        return this.atLevel(Level.ERROR);
    }
    
    @Override
    public LogBuilder atFatal() {
        return this.atLevel(Level.FATAL);
    }
    
    @Override
    public LogBuilder always() {
        final DefaultLogBuilder builder = this.logBuilder.get();
        if (builder.isInUse()) {
            return new DefaultLogBuilder(this);
        }
        return builder.reset(Level.OFF);
    }
    
    @Override
    public LogBuilder atLevel(final Level level) {
        if (this.isEnabled(level)) {
            return this.getLogBuilder(level).reset(level);
        }
        return LogBuilder.NOOP;
    }
    
    private DefaultLogBuilder getLogBuilder(final Level level) {
        final DefaultLogBuilder builder = this.logBuilder.get();
        return (Constants.ENABLE_THREADLOCALS && !builder.isInUse()) ? builder : new DefaultLogBuilder(this, level);
    }
    
    private void readObject(final ObjectInputStream s) throws ClassNotFoundException, IOException {
        s.defaultReadObject();
        try {
            final Field f = this.getClass().getDeclaredField("logBuilder");
            f.setAccessible(true);
            f.set(this, new LocalLogBuilder(this));
        }
        catch (NoSuchFieldException | IllegalAccessException ex3) {
            final ReflectiveOperationException ex2;
            final ReflectiveOperationException ex = ex2;
            StatusLogger.getLogger().warn("Unable to initialize LogBuilder");
        }
    }
    
    static {
        FLOW_MARKER = MarkerManager.getMarker("FLOW");
        ENTRY_MARKER = MarkerManager.getMarker("ENTER").setParents(AbstractLogger.FLOW_MARKER);
        EXIT_MARKER = MarkerManager.getMarker("EXIT").setParents(AbstractLogger.FLOW_MARKER);
        EXCEPTION_MARKER = MarkerManager.getMarker("EXCEPTION");
        THROWING_MARKER = MarkerManager.getMarker("THROWING").setParents(AbstractLogger.EXCEPTION_MARKER);
        CATCHING_MARKER = MarkerManager.getMarker("CATCHING").setParents(AbstractLogger.EXCEPTION_MARKER);
        DEFAULT_MESSAGE_FACTORY_CLASS = createClassForProperty("log4j2.messageFactory", ReusableMessageFactory.class, ParameterizedMessageFactory.class);
        DEFAULT_FLOW_MESSAGE_FACTORY_CLASS = createFlowClassForProperty("log4j2.flowMessageFactory", DefaultFlowMessageFactory.class);
        FQCN = AbstractLogger.class.getName();
        recursionDepthHolder = new ThreadLocal<int[]>();
    }
    
    private class LocalLogBuilder extends ThreadLocal<DefaultLogBuilder>
    {
        private AbstractLogger logger;
        
        LocalLogBuilder(final AbstractLogger logger) {
            this.logger = logger;
        }
        
        @Override
        protected DefaultLogBuilder initialValue() {
            return new DefaultLogBuilder(this.logger);
        }
    }
}
