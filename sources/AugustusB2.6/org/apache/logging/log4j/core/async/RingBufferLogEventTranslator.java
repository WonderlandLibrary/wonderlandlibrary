// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
import org.apache.logging.log4j.core.config.Property;
import java.util.List;
import org.apache.logging.log4j.util.StringMap;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.ContextDataInjector;
import com.lmax.disruptor.EventTranslator;

public class RingBufferLogEventTranslator implements EventTranslator<RingBufferLogEvent>
{
    private static final ContextDataInjector INJECTOR;
    private AsyncLogger asyncLogger;
    String loggerName;
    protected Marker marker;
    protected String fqcn;
    protected Level level;
    protected Message message;
    protected Throwable thrown;
    private ThreadContext.ContextStack contextStack;
    private long threadId;
    private String threadName;
    private int threadPriority;
    private StackTraceElement location;
    private Clock clock;
    private NanoClock nanoClock;
    
    public RingBufferLogEventTranslator() {
        this.threadId = Thread.currentThread().getId();
        this.threadName = Thread.currentThread().getName();
        this.threadPriority = Thread.currentThread().getPriority();
    }
    
    public void translateTo(final RingBufferLogEvent event, final long sequence) {
        try {
            event.setValues(this.asyncLogger, this.loggerName, this.marker, this.fqcn, this.level, this.message, this.thrown, RingBufferLogEventTranslator.INJECTOR.injectContextData(null, (StringMap)event.getContextData()), this.contextStack, this.threadId, this.threadName, this.threadPriority, this.location, this.clock, this.nanoClock);
        }
        finally {
            this.clear();
        }
    }
    
    void clear() {
        this.setBasicValues(null, null, null, null, null, null, null, null, null, null, null);
    }
    
    public void setBasicValues(final AsyncLogger anAsyncLogger, final String aLoggerName, final Marker aMarker, final String theFqcn, final Level aLevel, final Message msg, final Throwable aThrowable, final ThreadContext.ContextStack aContextStack, final StackTraceElement aLocation, final Clock aClock, final NanoClock aNanoClock) {
        this.asyncLogger = anAsyncLogger;
        this.loggerName = aLoggerName;
        this.marker = aMarker;
        this.fqcn = theFqcn;
        this.level = aLevel;
        this.message = msg;
        this.thrown = aThrowable;
        this.contextStack = aContextStack;
        this.location = aLocation;
        this.clock = aClock;
        this.nanoClock = aNanoClock;
    }
    
    public void updateThreadValues() {
        final Thread currentThread = Thread.currentThread();
        this.threadId = currentThread.getId();
        this.threadName = currentThread.getName();
        this.threadPriority = currentThread.getPriority();
    }
    
    static {
        INJECTOR = ContextDataInjectorFactory.createInjector();
    }
}
