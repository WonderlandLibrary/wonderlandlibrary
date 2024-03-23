/*
qProtect is deobfed so easily XD
*/
package dev.zprestige.prestige.client.event;

import java.lang.reflect.Method;

public final class Listener {
    private final Method method;
    private final Object object;
    private final Class<?> event;
    public int priority;

    public Listener(final Method method, final Object object, final Class<?> event, final int priority) {
        this.method = method;
        this.object = object;
        this.event = event;
        this.priority = priority;
    }

    public Method getMethod() {
        return method;
    }

    public Object getObject() {
        return object;
    }

    public Class<?> getEvent() {
        return event;
    }

    public int getPriority() {
        return priority;
    }
}