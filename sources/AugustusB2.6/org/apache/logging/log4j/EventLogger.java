// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.StructuredDataMessage;
import org.apache.logging.log4j.spi.ExtendedLogger;

public final class EventLogger
{
    public static final Marker EVENT_MARKER;
    private static final String NAME = "EventLogger";
    private static final String FQCN;
    private static final ExtendedLogger LOGGER;
    
    private EventLogger() {
    }
    
    public static void logEvent(final StructuredDataMessage msg) {
        EventLogger.LOGGER.logIfEnabled(EventLogger.FQCN, Level.OFF, EventLogger.EVENT_MARKER, msg, null);
    }
    
    public static void logEvent(final StructuredDataMessage msg, final Level level) {
        EventLogger.LOGGER.logIfEnabled(EventLogger.FQCN, level, EventLogger.EVENT_MARKER, msg, null);
    }
    
    static {
        EVENT_MARKER = MarkerManager.getMarker("EVENT");
        FQCN = EventLogger.class.getName();
        LOGGER = LogManager.getContext(false).getLogger("EventLogger");
    }
}
