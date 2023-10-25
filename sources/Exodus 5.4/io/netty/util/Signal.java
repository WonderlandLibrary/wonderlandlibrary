/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import io.netty.util.UniqueName;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ConcurrentMap;

public final class Signal
extends Error {
    private final UniqueName uname;
    private static final ConcurrentMap<String, Boolean> map = PlatformDependent.newConcurrentHashMap();
    private static final long serialVersionUID = -221145131122459977L;

    public static Signal valueOf(String string) {
        return new Signal(string);
    }

    public void expect(Signal signal) {
        if (this != signal) {
            throw new IllegalStateException("unexpected signal: " + signal);
        }
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public String toString() {
        return this.uname.name();
    }

    @Override
    public Throwable initCause(Throwable throwable) {
        return this;
    }

    @Deprecated
    public Signal(String string) {
        super(string);
        this.uname = new UniqueName(map, string, new Object[0]);
    }
}

