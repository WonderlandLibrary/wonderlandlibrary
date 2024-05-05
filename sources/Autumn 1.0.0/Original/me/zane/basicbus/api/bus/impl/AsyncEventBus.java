package me.zane.basicbus.api.bus.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zane.basicbus.api.annotations.Listener;
import me.zane.basicbus.api.bus.Bus;
import me.zane.basicbus.api.bus.CallLocation;
import me.zane.basicbus.api.invocation.Invoker;

public final class AsyncEventBus<T> implements Bus<T> {
    private final Map<Class<?>, List<CallLocation>> eventClassMethodMap = new HashMap<>();

    private final Invoker invoker;

    public AsyncEventBus(Invoker invoker) {
        this.invoker = invoker;
    }

    public Invoker invoker() {
        return this.invoker;
    }

    public Map<Class<?>, List<CallLocation>> map() {
        return this.eventClassMethodMap;
    }

    public void subscribe(Object subscriber) {
        Method[] methods = subscriber.getClass().getDeclaredMethods();
        Map<Class<?>, List<CallLocation>> eventClassMethodMapRef = this.eventClassMethodMap;
        for (int i = methods.length - 1; i >= 0; i--) {
            Method method = methods[i];
            Listener listener = method.<Listener>getAnnotation(Listener.class);
            if (listener != null) {
                Class<?>[] params = method.getParameterTypes();
                int paramsLength = params.length;
                if (paramsLength <= 1) {
                    Class<?> eventClass = listener.value();
                    if (paramsLength != 1 || eventClass == params[0]) {
                        CallLocation callLoc = new CallLocation(subscriber, method);
                        if (eventClassMethodMapRef.containsKey(eventClass)) {
                            ((List<CallLocation>)eventClassMethodMapRef.get(eventClass)).add(callLoc);
                        } else {
                            eventClassMethodMapRef.put(eventClass, Arrays.asList(new CallLocation[] { callLoc }));
                        }
                    }
                }
            }
        }
    }
}
