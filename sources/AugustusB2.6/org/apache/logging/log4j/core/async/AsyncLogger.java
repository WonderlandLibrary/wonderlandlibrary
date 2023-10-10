// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
import org.apache.logging.log4j.core.util.ClockFactory;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.core.impl.ContextDataFactory;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.ReliabilityStrategy;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.util.Supplier;
import org.apache.logging.log4j.core.config.Property;
import java.util.List;
import org.apache.logging.log4j.util.StringMap;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.message.ReusableMessage;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.status.StatusLogger;
import com.lmax.disruptor.EventTranslatorVararg;
import org.apache.logging.log4j.core.Logger;

public class AsyncLogger extends Logger implements EventTranslatorVararg<RingBufferLogEvent>
{
    private static final StatusLogger LOGGER;
    private static final Clock CLOCK;
    private static final ContextDataInjector CONTEXT_DATA_INJECTOR;
    private static final ThreadNameCachingStrategy THREAD_NAME_CACHING_STRATEGY;
    private final ThreadLocal<RingBufferLogEventTranslator> threadLocalTranslator;
    private final AsyncLoggerDisruptor loggerDisruptor;
    private volatile boolean includeLocation;
    private volatile NanoClock nanoClock;
    private final TranslatorType threadLocalTranslatorType;
    private final TranslatorType varargTranslatorType;
    
    public AsyncLogger(final LoggerContext context, final String name, final MessageFactory messageFactory, final AsyncLoggerDisruptor loggerDisruptor) {
        super(context, name, messageFactory);
        this.threadLocalTranslator = new ThreadLocal<RingBufferLogEventTranslator>();
        this.threadLocalTranslatorType = new TranslatorType() {
            @Override
            void log(final String fqcn, final StackTraceElement location, final Level level, final Marker marker, final Message message, final Throwable thrown) {
                AsyncLogger.this.logWithThreadLocalTranslator(fqcn, location, level, marker, message, thrown);
            }
            
            @Override
            void log(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
                AsyncLogger.this.logWithThreadLocalTranslator(fqcn, level, marker, message, thrown);
            }
        };
        this.varargTranslatorType = new TranslatorType() {
            @Override
            void log(final String fqcn, final StackTraceElement location, final Level level, final Marker marker, final Message message, final Throwable thrown) {
                AsyncLogger.this.logWithVarargTranslator(fqcn, location, level, marker, message, thrown);
            }
            
            @Override
            void log(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
                AsyncLogger.this.logWithVarargTranslator(fqcn, level, marker, message, thrown);
            }
        };
        this.loggerDisruptor = loggerDisruptor;
        this.includeLocation = this.privateConfig.loggerConfig.isIncludeLocation();
        this.nanoClock = context.getConfiguration().getNanoClock();
    }
    
    @Override
    protected void updateConfiguration(final Configuration newConfig) {
        this.nanoClock = newConfig.getNanoClock();
        this.includeLocation = newConfig.getLoggerConfig(this.name).isIncludeLocation();
        super.updateConfiguration(newConfig);
    }
    
    NanoClock getNanoClock() {
        return this.nanoClock;
    }
    
    private RingBufferLogEventTranslator getCachedTranslator() {
        RingBufferLogEventTranslator result = this.threadLocalTranslator.get();
        if (result == null) {
            result = new RingBufferLogEventTranslator();
            this.threadLocalTranslator.set(result);
        }
        return result;
    }
    
    @Override
    public void logMessage(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        this.getTranslatorType().log(fqcn, level, marker, message, thrown);
    }
    
    public void log(final Level level, final Marker marker, final String fqcn, final StackTraceElement location, final Message message, final Throwable throwable) {
        this.getTranslatorType().log(fqcn, location, level, marker, message, throwable);
    }
    
    private TranslatorType getTranslatorType() {
        return this.loggerDisruptor.isUseThreadLocals() ? this.threadLocalTranslatorType : this.varargTranslatorType;
    }
    
    private boolean isReused(final Message message) {
        return message instanceof ReusableMessage;
    }
    
    private void logWithThreadLocalTranslator(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        final RingBufferLogEventTranslator translator = this.getCachedTranslator();
        this.initTranslator(translator, fqcn, level, marker, message, thrown);
        this.initTranslatorThreadValues(translator);
        this.publish(translator);
    }
    
    private void logWithThreadLocalTranslator(final String fqcn, final StackTraceElement location, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        final RingBufferLogEventTranslator translator = this.getCachedTranslator();
        this.initTranslator(translator, fqcn, location, level, marker, message, thrown);
        this.initTranslatorThreadValues(translator);
        this.publish(translator);
    }
    
    private void publish(final RingBufferLogEventTranslator translator) {
        if (!this.loggerDisruptor.tryPublish(translator)) {
            this.handleRingBufferFull(translator);
        }
    }
    
    private void handleRingBufferFull(final RingBufferLogEventTranslator translator) {
        if (AbstractLogger.getRecursionDepth() > 1) {
            AsyncQueueFullMessageUtil.logWarningToStatusLogger();
            this.logMessageInCurrentThread(translator.fqcn, translator.level, translator.marker, translator.message, translator.thrown);
            translator.clear();
            return;
        }
        final EventRoute eventRoute = this.loggerDisruptor.getEventRoute(translator.level);
        switch (eventRoute) {
            case ENQUEUE: {
                this.loggerDisruptor.enqueueLogMessageWhenQueueFull(translator);
                break;
            }
            case SYNCHRONOUS: {
                this.logMessageInCurrentThread(translator.fqcn, translator.level, translator.marker, translator.message, translator.thrown);
                translator.clear();
                break;
            }
            case DISCARD: {
                translator.clear();
                break;
            }
            default: {
                throw new IllegalStateException("Unknown EventRoute " + eventRoute);
            }
        }
    }
    
    private void initTranslator(final RingBufferLogEventTranslator translator, final String fqcn, final StackTraceElement location, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        translator.setBasicValues(this, this.name, marker, fqcn, level, message, thrown, ThreadContext.getImmutableStack(), location, AsyncLogger.CLOCK, this.nanoClock);
    }
    
    private void initTranslator(final RingBufferLogEventTranslator translator, final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        translator.setBasicValues(this, this.name, marker, fqcn, level, message, thrown, ThreadContext.getImmutableStack(), this.calcLocationIfRequested(fqcn), AsyncLogger.CLOCK, this.nanoClock);
    }
    
    private void initTranslatorThreadValues(final RingBufferLogEventTranslator translator) {
        if (AsyncLogger.THREAD_NAME_CACHING_STRATEGY == ThreadNameCachingStrategy.UNCACHED) {
            translator.updateThreadValues();
        }
    }
    
    private StackTraceElement calcLocationIfRequested(final String fqcn) {
        return this.includeLocation ? StackLocatorUtil.calcLocation(fqcn) : null;
    }
    
    private void logWithVarargTranslator(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        final Disruptor<RingBufferLogEvent> disruptor = this.loggerDisruptor.getDisruptor();
        if (disruptor == null) {
            AsyncLogger.LOGGER.error("Ignoring log event after Log4j has been shut down.");
            return;
        }
        if (!this.isReused(message)) {
            InternalAsyncUtil.makeMessageImmutable(message);
        }
        StackTraceElement location = null;
        final RingBuffer ringBuffer = disruptor.getRingBuffer();
        final Object[] array = new Object[7];
        array[0] = this;
        location = (StackTraceElement)(array[1] = this.calcLocationIfRequested(fqcn));
        array[2] = fqcn;
        array[3] = level;
        array[4] = marker;
        array[5] = message;
        array[6] = thrown;
        if (!ringBuffer.tryPublishEvent((EventTranslatorVararg)this, array)) {
            this.handleRingBufferFull(location, fqcn, level, marker, message, thrown);
        }
    }
    
    private void logWithVarargTranslator(final String fqcn, final StackTraceElement location, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        final Disruptor<RingBufferLogEvent> disruptor = this.loggerDisruptor.getDisruptor();
        if (disruptor == null) {
            AsyncLogger.LOGGER.error("Ignoring log event after Log4j has been shut down.");
            return;
        }
        if (!this.isReused(message)) {
            InternalAsyncUtil.makeMessageImmutable(message);
        }
        if (!disruptor.getRingBuffer().tryPublishEvent((EventTranslatorVararg)this, new Object[] { this, location, fqcn, level, marker, message, thrown })) {
            this.handleRingBufferFull(location, fqcn, level, marker, message, thrown);
        }
    }
    
    public void translateTo(final RingBufferLogEvent event, final long sequence, final Object... args) {
        final AsyncLogger asyncLogger = (AsyncLogger)args[0];
        final StackTraceElement location = (StackTraceElement)args[1];
        final String fqcn = (String)args[2];
        final Level level = (Level)args[3];
        final Marker marker = (Marker)args[4];
        final Message message = (Message)args[5];
        final Throwable thrown = (Throwable)args[6];
        final ThreadContext.ContextStack contextStack = ThreadContext.getImmutableStack();
        final Thread currentThread = Thread.currentThread();
        final String threadName = AsyncLogger.THREAD_NAME_CACHING_STRATEGY.getThreadName();
        event.setValues(asyncLogger, asyncLogger.getName(), marker, fqcn, level, message, thrown, AsyncLogger.CONTEXT_DATA_INJECTOR.injectContextData(null, (StringMap)event.getContextData()), contextStack, currentThread.getId(), threadName, currentThread.getPriority(), location, AsyncLogger.CLOCK, this.nanoClock);
    }
    
    void logMessageInCurrentThread(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown) {
        final ReliabilityStrategy strategy = this.privateConfig.loggerConfig.getReliabilityStrategy();
        strategy.log(this, this.getName(), fqcn, marker, level, message, thrown);
    }
    
    private void handleRingBufferFull(final StackTraceElement location, final String fqcn, final Level level, final Marker marker, final Message msg, final Throwable thrown) {
        if (AbstractLogger.getRecursionDepth() > 1) {
            AsyncQueueFullMessageUtil.logWarningToStatusLogger();
            this.logMessageInCurrentThread(fqcn, level, marker, msg, thrown);
            return;
        }
        final EventRoute eventRoute = this.loggerDisruptor.getEventRoute(level);
        switch (eventRoute) {
            case ENQUEUE: {
                this.loggerDisruptor.enqueueLogMessageWhenQueueFull((EventTranslatorVararg<RingBufferLogEvent>)this, this, location, fqcn, level, marker, msg, thrown);
                break;
            }
            case SYNCHRONOUS: {
                this.logMessageInCurrentThread(fqcn, level, marker, msg, thrown);
                break;
            }
            case DISCARD: {
                break;
            }
            default: {
                throw new IllegalStateException("Unknown EventRoute " + eventRoute);
            }
        }
    }
    
    public void actualAsyncLog(final RingBufferLogEvent event) {
        final LoggerConfig privateConfigLoggerConfig = this.privateConfig.loggerConfig;
        final List<Property> properties = privateConfigLoggerConfig.getPropertyList();
        if (properties != null) {
            this.onPropertiesPresent(event, properties);
        }
        privateConfigLoggerConfig.getReliabilityStrategy().log(this, event);
    }
    
    private void onPropertiesPresent(final RingBufferLogEvent event, final List<Property> properties) {
        final StringMap contextData = getContextData(event);
        for (int i = 0, size = properties.size(); i < size; ++i) {
            final Property prop = properties.get(i);
            if (contextData.getValue(prop.getName()) == null) {
                final String value = prop.isValueNeedsLookup() ? this.privateConfig.config.getStrSubstitutor().replace(event, prop.getValue()) : prop.getValue();
                contextData.putValue(prop.getName(), value);
            }
        }
        event.setContextData(contextData);
    }
    
    private static StringMap getContextData(final RingBufferLogEvent event) {
        final StringMap contextData = (StringMap)event.getContextData();
        if (contextData.isFrozen()) {
            final StringMap temp = ContextDataFactory.createContextData();
            temp.putAll(contextData);
            return temp;
        }
        return contextData;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        CLOCK = ClockFactory.getClock();
        CONTEXT_DATA_INJECTOR = ContextDataInjectorFactory.createInjector();
        THREAD_NAME_CACHING_STRATEGY = ThreadNameCachingStrategy.create();
    }
    
    abstract class TranslatorType
    {
        abstract void log(final String fqcn, final StackTraceElement location, final Level level, final Marker marker, final Message message, final Throwable thrown);
        
        abstract void log(final String fqcn, final Level level, final Marker marker, final Message message, final Throwable thrown);
    }
}
