package me.zane.basicbus.api.bus;

import java.lang.reflect.Method;

public final class CallLocation {
    public final Object subscriber;

    public final Method method;

    public final boolean noParams;

    public CallLocation(Object subscriber, Method method) {
        this.subscriber = subscriber;
        method.setAccessible(true);
        this.method = method;
        this.noParams = (method.getParameterCount() == 0);
    }

    public String toString() {
        return "CallLocation: {\n   class: " + this.subscriber
                .getClass().getCanonicalName() + "\n   method: " + this.method
                .getName() + "\n};";
    }
}
