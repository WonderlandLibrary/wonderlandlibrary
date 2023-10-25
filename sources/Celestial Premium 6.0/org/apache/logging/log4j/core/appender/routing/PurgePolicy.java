/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.routing;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.routing.RoutingAppender;

public interface PurgePolicy {
    public void purge();

    public void update(String var1, LogEvent var2);

    public void initialize(RoutingAppender var1);
}

