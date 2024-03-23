package xyz.northclient.features;

import java.lang.reflect.Method;

public class EventListener {
    private final Object target;
    private final Method method;

    public EventListener(final Object target, final Method method) {
        this.target = target;
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }
}
