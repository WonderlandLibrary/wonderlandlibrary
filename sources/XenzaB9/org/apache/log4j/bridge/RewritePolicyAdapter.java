// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.bridge;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;

public class RewritePolicyAdapter implements RewritePolicy
{
    private final org.apache.log4j.rewrite.RewritePolicy policy;
    
    public RewritePolicyAdapter(final org.apache.log4j.rewrite.RewritePolicy policy) {
        this.policy = policy;
    }
    
    @Override
    public LogEvent rewrite(final LogEvent source) {
        final LoggingEvent event = this.policy.rewrite(new LogEventAdapter(source));
        return (event instanceof LogEventAdapter) ? ((LogEventAdapter)event).getEvent() : new LogEventWrapper(event);
    }
    
    public org.apache.log4j.rewrite.RewritePolicy getPolicy() {
        return this.policy;
    }
}
