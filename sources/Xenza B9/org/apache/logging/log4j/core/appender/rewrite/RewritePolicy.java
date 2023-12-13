// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.rewrite;

import org.apache.logging.log4j.core.LogEvent;

public interface RewritePolicy
{
    LogEvent rewrite(final LogEvent source);
}
