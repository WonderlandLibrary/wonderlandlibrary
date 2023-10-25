/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core;

import java.util.EventListener;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.status.StatusLogger;

public class LogEventListener
implements EventListener {
    protected static final StatusLogger LOGGER = StatusLogger.getLogger();
    private final LoggerContext context = LoggerContext.getContext((boolean)false);

    protected LogEventListener() {
    }

    public void log(LogEvent event) {
        if (event == null) {
            return;
        }
        Logger logger = this.context.getLogger(event.getLoggerName());
        if (logger.privateConfig.filter(event.getLevel(), event.getMarker(), event.getMessage(), event.getThrown())) {
            logger.privateConfig.logEvent(event);
        }
    }
}

