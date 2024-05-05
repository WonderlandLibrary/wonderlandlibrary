package me.zane.basicbus.api.bus;

import me.zane.basicbus.api.annotations.Listener;
import me.zane.basicbus.api.invocation.Invoker;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Bus<T> {
    Invoker invoker();

    Map<Class<?>, List<CallLocation>> map();

    default void subscribe(Object subscriber) {
        Method[] methods = subscriber.getClass().getDeclaredMethods();
        Map<Class<?>, List<CallLocation>> eventClassMethodMapRef = map();
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
                            eventClassMethodMapRef.put(eventClass, new CopyOnWriteArrayList<>(Arrays.asList(new CallLocation[] { callLoc })));
                        }
                    }
                }
            }
        }
    }

    default void unsubscribe(Object subscriber) {
        Collection<List<CallLocation>> callLocationsRef = map().values();
        for (List<CallLocation> callLocations : callLocationsRef) {
            for (CallLocation callLocation : callLocations) {
                if (callLocation.subscriber == subscriber)
                    callLocations.remove(callLocation);
            }
        }
    }

    default void post(T event) {
        List<CallLocation> callLocations = map().get(event.getClass());
        if (callLocations != null)
            for (CallLocation callLocation : callLocations) {
                Method method = callLocation.method;
                Object sub = callLocation.subscriber;
                if (callLocation.noParams) {
                    invoker().invoke(sub, method, new Object[0]);
                    continue;
                }
                invoker().invoke(sub, method, new Object[] { event });
            }
    }
}
