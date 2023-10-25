/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl.client;

import org.apache.http.impl.client.Clock;

class SystemClock
implements Clock {
    SystemClock() {
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}

