// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.EventListener;

public class LogEventListener implements EventListener
{
    protected static final StatusLogger LOGGER;
    private final LoggerContext context;
    
    protected LogEventListener() {
        this.context = LoggerContext.getContext(false);
    }
    
    public void log(final LogEvent event) {
        if (event == null) {
            return;
        }
        final Logger logger = this.context.getLogger(event.getLoggerName());
        if (logger.privateConfig.filter(event.getLevel(), event.getMarker(), event.getMessage(), event.getThrown())) {
            logger.privateConfig.logEvent(event);
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
