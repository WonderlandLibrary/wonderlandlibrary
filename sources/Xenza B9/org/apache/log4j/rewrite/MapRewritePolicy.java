// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.rewrite;

import org.apache.log4j.spi.LocationInfo;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Iterator;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.Marker;
import org.apache.log4j.Priority;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.logging.log4j.util.StringMap;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.log4j.bridge.LogEventAdapter;
import org.apache.logging.log4j.message.SimpleMessage;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.log4j.spi.LoggingEvent;

public class MapRewritePolicy implements RewritePolicy
{
    @Override
    public LoggingEvent rewrite(final LoggingEvent source) {
        final Object msg = source.getMessage();
        if (!(msg instanceof MapMessage) && !(msg instanceof Map)) {
            return source;
        }
        final Map<String, String> props = (source.getProperties() != null) ? new HashMap<String, String>(source.getProperties()) : new HashMap<String, String>();
        final Map<String, Object> eventProps = (msg instanceof Map) ? ((Map)msg) : ((MapMessage)msg).getData();
        Message newMessage = null;
        final Object newMsg = eventProps.get("message");
        if (newMsg != null) {
            newMessage = new SimpleMessage(newMsg.toString());
            for (final Map.Entry<String, Object> entry : eventProps.entrySet()) {
                if (!"message".equals(entry.getKey())) {
                    props.put(entry.getKey(), entry.getValue().toString());
                }
            }
            LogEvent event;
            if (source instanceof LogEventAdapter) {
                event = new Log4jLogEvent.Builder(((LogEventAdapter)source).getEvent()).setMessage(newMessage).setContextData(new SortedArrayStringMap(props)).build();
            }
            else {
                final LocationInfo info = source.getLocationInformation();
                final StackTraceElement element = new StackTraceElement(info.getClassName(), info.getMethodName(), info.getFileName(), Integer.parseInt(info.getLineNumber()));
                final Thread thread = this.getThread(source.getThreadName());
                final long threadId = (thread != null) ? thread.getId() : 0L;
                final int threadPriority = (thread != null) ? thread.getPriority() : 0;
                event = Log4jLogEvent.newBuilder().setContextData(new SortedArrayStringMap(props)).setLevel(OptionConverter.convertLevel(source.getLevel())).setLoggerFqcn(source.getFQNOfLoggerClass()).setMarker(null).setMessage(newMessage).setSource(element).setLoggerName(source.getLoggerName()).setThreadName(source.getThreadName()).setThreadId(threadId).setThreadPriority(threadPriority).setThrown(source.getThrowableInformation().getThrowable()).setTimeMillis(source.getTimeStamp()).setNanoTime(0L).setThrownProxy(null).build();
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
