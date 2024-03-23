package xyz.northclient.features;

import xyz.northclient.InstanceAccess;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class EventBus implements InstanceAccess {
    private final Map<Class<? extends Event>, Set<EventListener>> listeners = new ConcurrentHashMap<>();
    private final Map<Class<?>, Map<String, Method>> listenerMethods = new HashMap<>();

    public void post(final Event event) {
        final Set<EventListener> eventListeners = listeners.get(event.getClass());
        if (eventListeners == null) {
            return;
        }
        for (final EventListener listener : eventListeners) {
            try {
                final Method method = getListenerMethod(listener.getTarget().getClass(), listener.getMethod().getName(), event.getClass());
                method.invoke(listener.getTarget(), event);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void register(final Object object) {
        final Map<String, Method> methods = new HashMap<>();
        for (final Method method : object.getClass().getMethods()) {
            if (method.isAnnotationPresent(EventLink.class)) {
                final Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1 || !Event.class.isAssignableFrom(parameterTypes[0])) {
                    throw new IllegalArgumentException("Method " + method + " has @Listener annotation but has wrong parameters");
                }
                final Class<? extends Event> eventType = parameterTypes[0].asSubclass(Event.class);
                final Set<EventListener> eventListeners = listeners.computeIfAbsent(eventType, k -> ConcurrentHashMap.newKeySet());
                eventListeners.add(new EventListener(object, method));
                methods.put(method.getName(), method);
            }
        }
        listenerMethods.put(object.getClass(), methods);
    }

    public void unregister(final Object object) {
        for (final Set<EventListener> eventListeners : listeners.values()) {
            eventListeners.removeIf(listener -> listener.getTarget() == object);
        }
        listenerMethods.remove(object.getClass());
    }

    public void onKey(int key) {
        for(AbstractModule mod : north.getModules().getModules()) {
            if(mod.getKeyCode() == key) {
                mod.toggle();
            }
        }
    }

    private Method getListenerMethod(final Class<?> clazz, final String methodName, final Class<?> eventType) {
        final Map<String, Method> methods = listenerMethods.get(clazz);
        if (methods == null) {
            throw new IllegalStateException("No @Listen methods found in class " + clazz);
        }
        final Method method = methods.get(methodName);
        if (method == null) {
            throw new IllegalStateException("No @Listen method found with name " + methodName + " in class " + clazz);
        }
        if (!method.getParameterTypes()[0].isAssignableFrom(eventType)) {
            throw new IllegalStateException("Method " + method + " has @Listen annotation but has wrong parameters");
        }
        return method;
    }

}