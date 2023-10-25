/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.input;

import net.java.games.input.LinuxAxisDescriptor;

final class LinuxEvent {
    private long nanos;
    private final LinuxAxisDescriptor descriptor = new LinuxAxisDescriptor();
    private int value;

    LinuxEvent() {
    }

    public final void set(long seconds, long microseconds, int type, int code, int value) {
        this.nanos = (seconds * 1000000L + microseconds) * 1000L;
        this.descriptor.set(type, code);
        this.value = value;
    }

    public final int getValue() {
        return this.value;
    }

    public final LinuxAxisDescriptor getDescriptor() {
        return this.descriptor;
    }

    public final long getNanos() {
        return this.nanos;
    }
}

