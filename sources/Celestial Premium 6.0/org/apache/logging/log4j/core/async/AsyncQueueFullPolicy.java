/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.async.EventRoute;

public interface AsyncQueueFullPolicy {
    public EventRoute getRoute(long var1, Level var3);
}

