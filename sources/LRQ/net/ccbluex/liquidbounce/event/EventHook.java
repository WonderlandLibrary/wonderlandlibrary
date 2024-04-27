/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import java.lang.reflect.Method;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;

public final class EventHook {
    private final boolean isIgnoreCondition;
    private final Listenable eventClass;
    private final Method method;

    public final boolean isIgnoreCondition() {
        return this.isIgnoreCondition;
    }

    public final Listenable getEventClass() {
        return this.eventClass;
    }

    public final Method getMethod() {
        return this.method;
    }

    public EventHook(Listenable eventClass, Method method, EventTarget eventTarget) {
        this.eventClass = eventClass;
        this.method = method;
        this.isIgnoreCondition = eventTarget.ignoreCondition();
    }
}

