/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import io.netty.util.Timer;
import io.netty.util.TimerTask;

public interface Timeout {
    public boolean cancel();

    public boolean isExpired();

    public boolean isCancelled();

    public Timer timer();

    public TimerTask task();
}

