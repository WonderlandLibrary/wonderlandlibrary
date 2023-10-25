/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rewrite;

import org.apache.logging.log4j.core.LogEvent;

public interface RewritePolicy {
    public LogEvent rewrite(LogEvent var1);
}

