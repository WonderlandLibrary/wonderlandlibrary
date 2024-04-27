/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.event;

import java.lang.reflect.Method;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;

public final class EventHook {
    private final Method method;
    private final boolean isIgnoreCondition;
    private final Listenable eventClass;

    public final boolean isIgnoreCondition() {
        return this.isIgnoreCondition;
    }

    public EventHook(Listenable listenable, Method method, EventTarget eventTarget) {
        this.eventClass = listenable;
        this.method = method;
        this.isIgnoreCondition = eventTarget.ignoreCondition();
    }

    public final Listenable getEventClass() {
        return this.eventClass;
    }

    public final Method getMethod() {
        return this.method;
    }
}

