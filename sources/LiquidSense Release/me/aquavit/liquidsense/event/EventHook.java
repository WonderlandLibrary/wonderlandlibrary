package me.aquavit.liquidsense.event;

import java.lang.reflect.Method;

public class EventHook {

    private final boolean isIgnoreCondition;
    private final Listenable eventClass;
    private final Method method;

    public EventHook(Listenable eventClass, Method method, EventTarget eventTarget) {
        this.eventClass = eventClass;
        this.method = method;
        this.isIgnoreCondition = eventTarget.ignoreCondition();
    }

    public boolean isIgnoreCondition() {
        return this.isIgnoreCondition;
    }

    public Listenable getEventClass() {
        return this.eventClass;
    }

    public Method getMethod() {
        return this.method;
    }
}
