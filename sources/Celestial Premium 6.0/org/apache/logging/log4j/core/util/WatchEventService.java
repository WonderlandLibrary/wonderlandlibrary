/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.core.util.WatchManager;

public interface WatchEventService {
    public void subscribe(WatchManager var1);

    public void unsubscribe(WatchManager var1);
}

