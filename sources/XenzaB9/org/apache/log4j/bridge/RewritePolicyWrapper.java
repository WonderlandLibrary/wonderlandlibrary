// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.bridge;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.rewrite.RewritePolicy;

public class RewritePolicyWrapper implements RewritePolicy
{
    private final org.apache.logging.log4j.core.appender.rewrite.RewritePolicy policy;
    
    public RewritePolicyWrapper(final org.apache.logging.log4j.core.appender.rewrite.RewritePolicy policy) {
        this.policy = policy;
    }
    
    @Override
    public LoggingEvent rewrite(final LoggingEvent source) {
        final LogEvent event = (source instanceof LogEventAdapter) ? ((LogEventAdapter)source).getEvent() : new LogEventWrapper(source);
        return new LogEventAdapter(this.policy.rewrite(event));
    }
    
    public org.apache.logging.log4j.core.appender.rewrite.RewritePolicy getPolicy() {
        return this.policy;
    }
}
