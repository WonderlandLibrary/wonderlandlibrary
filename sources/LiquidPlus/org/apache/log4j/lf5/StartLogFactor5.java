/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.lf5;

import org.apache.log4j.lf5.LF5Appender;
import org.apache.log4j.lf5.LogLevel;
import org.apache.log4j.lf5.viewer.LogBrokerMonitor;

public class StartLogFactor5 {
    public static final void main(String[] args2) {
        LogBrokerMonitor monitor = new LogBrokerMonitor(LogLevel.getLog4JLevels());
        monitor.setFrameSize(LF5Appender.getDefaultMonitorWidth(), LF5Appender.getDefaultMonitorHeight());
        monitor.setFontSize(12);
        monitor.show();
    }
}

