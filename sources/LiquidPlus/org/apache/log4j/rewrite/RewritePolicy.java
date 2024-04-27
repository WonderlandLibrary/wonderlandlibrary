/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.rewrite;

import org.apache.log4j.spi.LoggingEvent;

public interface RewritePolicy {
    public LoggingEvent rewrite(LoggingEvent var1);
}

