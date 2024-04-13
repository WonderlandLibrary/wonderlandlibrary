package com.canon.majik.api.event.eventBus;

import java.lang.reflect.Method;


public final class Listener {
    private final Method method;
    private final Object parent;
    private final Class<?> event;
    private final int priority;

    public Listener(Method method, Object parent, Class<?> event, int priority){
        this.method = method;
        this.parent = parent;
        this.event = event;
        this.priority = priority;
    }


    public Method getMethod() {
        return method;
    }

    public Object getParent() {
        return parent;
    }

    public Class<?> getEvent() {
        return event;
    }

    public int getPriority() {
        return priority;
    }
}
