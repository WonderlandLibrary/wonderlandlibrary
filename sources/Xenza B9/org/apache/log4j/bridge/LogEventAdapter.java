// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.bridge;

import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.util.Throwables;
import org.apache.log4j.spi.ThrowableInformation;
import org.apache.log4j.Category;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LocationInfo;
import java.lang.reflect.Method;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.log4j.spi.LoggingEvent;

public class LogEventAdapter extends LoggingEvent
{
    private static final long JVM_START_TIME;
    private final LogEvent event;
    
    public LogEventAdapter(final LogEvent event) {
        this.event = event;
    }
    
    public static long getStartTime() {
        return LogEventAdapter.JVM_START_TIME;
    }
    
    private static long initStartTime() {
        try {
            final Class<?> factoryClass = Loader.loadSystemClass("java.lang.management.ManagementFactory");
            final Method getRuntimeMXBean = factoryClass.getMethod("getRuntimeMXBean", (Class<?>[])new Class[0]);
            final Object runtimeMXBean = getRuntimeMXBean.invoke(null, new Object[0]);
            final Class<?> runtimeMXBeanClass = Loader.loadSystemClass("java.lang.management.RuntimeMXBean");
            final Method getStartTime = runtimeMXBeanClass.getMethod("getStartTime", (Class<?>[])new Class[0]);
            return (long)getStartTime.invoke(runtimeMXBean, new Object[0]);
        }
        catch (final Throwable t) {
            StatusLogger.getLogger().error("Unable to call ManagementFactory.getRuntimeMXBean().getStartTime(), using system time for OnStartupTriggeringPolicy", t);
            return System.currentTimeMillis();
        }
    }
    
    public LogEvent getEvent() {
        return this.event;
    }
    
    @Override
    public LocationInfo getLocationInformation() {
        return new LocationInfo(this.event.getSource());
    }
    
    @Override
    public Level getLevel() {
        return OptionConverter.convertLevel(this.event.getLevel());
    }
    
    @Override
    public String getLoggerName() {
        return this.event.getLoggerName();
    }
    
    @Override
    public long getTimeStamp() {
        return this.event.getTimeMillis();
    }
    
    @Override
    public Category getLogger() {
        return Category.getInstance(this.event.getLoggerName());
    }
    
    @Override
    public Object getMessage() {
        return this.event.getMessage();
    }
    
    @Override
    public String getNDC() {
        return this.event.getContextStack().toString();
    }
    
    @Override
    public Object getMDC(final String key) {
        if (this.event.getContextData() != null) {
            return this.event.getContextData().getValue(key);
        }
        return null;
    }
    
    @Override
    public void getMDCCopy() {
    }
    
    @Override
    public String getRenderedMessage() {
        return this.event.getMessage().getFormattedMessage();
    }
    
    @Override
    public String getThreadName() {
        return this.event.getThreadName();
    }
    
    @Override
    public ThrowableInformation getThrowableInformation() {
        if (this.event.getThrown() != null) {
            return new ThrowableInformation(this.event.getThrown());
        }
        return null;
    }
    
    @Override
    public String[] getThrowableStrRep() {
        if (this.event.getThrown() != null) {
            return Throwables.toStringList(this.event.getThrown()).toArray(Strings.EMPTY_ARRAY);
        }
        return null;
    }
    
    @Override
    public String getProperty(final String key) {
        return this.event.getContextData().getValue(key);
    }
    
    @Override
    public Set getPropertyKeySet() {
        return this.event.getContextData().toMap().keySet();
    }
    
    @Override
    public Map getProperties() {
        return this.event.getContextData().toMap();
    }
    
    static {
        JVM_START_TIME = initStartTime();
    }
}
