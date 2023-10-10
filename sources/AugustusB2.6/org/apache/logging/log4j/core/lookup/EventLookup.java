// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "event", category = "Lookup")
public class EventLookup extends AbstractLookup
{
    @Override
    public String lookup(final LogEvent event, final String key) {
        switch (key) {
            case "Marker": {
                return (event.getMarker() != null) ? event.getMarker().getName() : null;
            }
            case "ThreadName": {
                return event.getThreadName();
            }
            case "Level": {
                return event.getLevel().toString();
            }
            case "ThreadId": {
                return Long.toString(event.getThreadId());
            }
            case "Timestamp": {
                return Long.toString(event.getTimeMillis());
            }
            case "Exception": {
                if (event.getThrown() != null) {
                    return event.getThrown().getClass().getSimpleName();
                }
                if (event.getThrownProxy() != null) {
                    return event.getThrownProxy().getName();
                }
                return null;
            }
            case "Logger": {
                return event.getLoggerName();
            }
            case "Message": {
                return event.getMessage().getFormattedMessage();
            }
            default: {
                return null;
            }
        }
    }
}
