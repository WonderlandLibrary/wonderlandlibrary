// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.rewrite;

import org.apache.log4j.spi.LocationInfo;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Iterator;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.Marker;
import org.apache.log4j.Priority;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.logging.log4j.util.StringMap;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.log4j.bridge.LogEventAdapter;
import org.apache.log4j.spi.LoggingEvent;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Collections;
import java.util.Map;

public class PropertyRewritePolicy implements RewritePolicy
{
    private Map<String, String> properties;
    
    public PropertyRewritePolicy() {
        this.properties = Collections.EMPTY_MAP;
    }
    
    public void setProperties(final String properties) {
        final Map<String, String> newMap = new HashMap<String, String>();
        final StringTokenizer pairs = new StringTokenizer(properties, ",");
        while (pairs.hasMoreTokens()) {
            final StringTokenizer entry = new StringTokenizer(pairs.nextToken(), "=");
            newMap.put(entry.nextElement().toString().trim(), entry.nextElement().toString().trim());
        }
        synchronized (this) {
            this.properties = newMap;
        }
    }
    
    @Override
    public LoggingEvent rewrite(final LoggingEvent source) {
        if (!this.properties.isEmpty()) {
            final Map<String, String> rewriteProps = (source.getProperties() != null) ? new HashMap<String, String>(source.getProperties()) : new HashMap<String, String>();
            for (final Map.Entry<String, String> entry : this.properties.entrySet()) {
                if (!rewriteProps.containsKey(entry.getKey())) {
                    rewriteProps.put(entry.getKey(), entry.getValue());
                }
            }
            LogEvent event;
            if (source instanceof LogEventAdapter) {
                event = new Log4jLogEvent.Builder(((LogEventAdapter)source).getEvent()).setContextData(new SortedArrayStringMap(rewriteProps)).build();
            }
            else {
                final LocationInfo info = source.getLocationInformation();
                final StackTraceElement element = new StackTraceElement(info.getClassName(), info.getMethodName(), info.getFileName(), Integer.parseInt(info.getLineNumber()));
                final Thread thread = this.getThread(source.getThreadName());
                final long threadId = (thread != null) ? thread.getId() : 0L;
                final int threadPriority = (thread != null) ? thread.getPriority() : 0;
                event = Log4jLogEvent.newBuilder().setContextData(new SortedArrayStringMap(rewriteProps)).setLevel(OptionConverter.convertLevel(source.getLevel())).setLoggerFqcn(source.getFQNOfLoggerClass()).setMarker(null).setMessage(new SimpleMessage(source.getRenderedMessage())).setSource(element).setLoggerName(source.getLoggerName()).setThreadName(source.getThreadName()).setThreadId(threadId).setThreadPriority(threadPriority).setThrown(source.getThrowableInformation().getThrowable()).setTimeMillis(source.getTimeStamp()).setNanoTime(0L).setThrownProxy(null).build();
            }
            return new LogEventAdapter(event);
        }
        return source;
    }
    
    private Thread getThread(final String name) {
        for (final Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getName().equals(name)) {
                return thread;
            }
        }
        return null;
    }
}
