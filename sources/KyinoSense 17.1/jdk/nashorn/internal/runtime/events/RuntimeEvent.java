/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.events;

import java.util.logging.Level;
import jdk.nashorn.internal.runtime.options.Options;

public class RuntimeEvent<T> {
    public static final int RUNTIME_EVENT_QUEUE_SIZE = Options.getIntProperty("nashorn.runtime.event.queue.size", 1024);
    private final Level level;
    private final T value;

    public RuntimeEvent(Level level, T object) {
        this.level = level;
        this.value = object;
    }

    public final T getValue() {
        return this.value;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(this.level).append("] ").append(this.value == null ? "null" : this.getValueClass().getSimpleName()).append(" value=").append(this.value);
        return sb.toString();
    }

    public final Class<?> getValueClass() {
        return this.value.getClass();
    }
}

