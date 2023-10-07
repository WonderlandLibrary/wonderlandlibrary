// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.routing;

import org.apache.logging.log4j.core.LogEvent;

public interface PurgePolicy
{
    void purge();
    
    void update(final String key, final LogEvent event);
    
    void initialize(final RoutingAppender routingAppender);
}
