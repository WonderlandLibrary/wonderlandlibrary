// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.spi;

import java.util.Set;
import org.apache.log4j.Priority;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Category;
import org.apache.log4j.bridge.LogEventAdapter;

public class LoggingEvent
{
    public final long timeStamp;
    
    public static long getStartTime() {
        return LogEventAdapter.getStartTime();
    }
    
    public LoggingEvent() {
        this.timeStamp = System.currentTimeMillis();
    }
    
    public LoggingEvent(final String fqnOfCategoryClass, final Category logger, final long timeStamp, final Level level, final Object message, final String threadName, final ThrowableInformation throwable, final String ndc, final LocationInfo info, final Map properties) {
        this.timeStamp = timeStamp;
    }
    
    public LoggingEvent(final String fqnOfCategoryClass, final Category logger, final long timeStamp, final Priority level, final Object message, final Throwable throwable) {
        this.timeStamp = timeStamp;
    }
    
    public LoggingEvent(final String fqnOfCategoryClass, final Category logger, final Priority level, final Object message, final Throwable throwable) {
        this.timeStamp = System.currentTimeMillis();
    }
    
    public String getFQNOfLoggerClass() {
        return null;
    }
    
    public Level getLevel() {
        return null;
    }
    
    public LocationInfo getLocationInformation() {
        return null;
    }
    
    public Category getLogger() {
        return null;
    }
    
    public String getLoggerName() {
        return null;
    }
    
    public Object getMDC(final String key) {
        return null;
    }
    
    public void getMDCCopy() {
    }
    
    public Object getMessage() {
        return null;
    }
    
    public String getNDC() {
        return null;
    }
    
    public Map getProperties() {
        return null;
    }
    
    public String getProperty(final String key) {
        return null;
    }
    
    public Set getPropertyKeySet() {
        return null;
    }
    
    public String getRenderedMessage() {
        return null;
    }
    
    public String getThreadName() {
        return null;
    }
    
    public ThrowableInformation getThrowableInformation() {
        return null;
    }
    
    public String[] getThrowableStrRep() {
        return null;
    }
    
    public long getTimeStamp() {
        return 0L;
    }
    
    public Object removeProperty(final String propName) {
        return null;
    }
    
    public void setProperty(final String propName, final String propValue) {
    }
}
