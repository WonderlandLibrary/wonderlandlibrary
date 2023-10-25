/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.impl;

import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.message.Message;

public interface LocationAwareLogEventFactory {
    public LogEvent createEvent(String var1, Marker var2, String var3, StackTraceElement var4, Level var5, Message var6, List<Property> var7, Throwable var8);
}

