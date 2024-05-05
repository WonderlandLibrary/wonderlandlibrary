/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.event.eventbus;

import com.wallhacks.losebypass.event.eventbus.EventPriority;
import java.lang.reflect.Method;

public final class Listener {
    public final Method method;
    public final Object object;
    public final Class<?> event;
    public final EventPriority priority;

    public Listener(Method method, Object object, Class<?> event, EventPriority priority) {
        this.method = method;
        this.object = object;
        this.event = event;
        this.priority = priority;
    }
}

