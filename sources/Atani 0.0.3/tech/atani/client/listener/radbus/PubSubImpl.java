package tech.atani.client.listener.radbus;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author nevalackin
 * @since 1.0.0
 */
final class PubSubImpl<Event> implements PubSub<Event> {

    private static final Object GLOBAL_LISTENERS_BACKING_OBJECT = new Object();
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    // the type of Listener#invoke
    private static final MethodType LISTENER_INVOKE_TYPE = MethodType.methodType(void.class, Object.class);
    private final Map<Class<?>, List<Listener<Event>>> eventTypeListenerCache = new HashMap<>();
    private final Map<Object, List<TypedListener>> subscriberMap = new HashMap<>();
    private final Consumer<String> logger;

    PubSubImpl(Consumer<String> logger) {
        this.logger = logger;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> void subscribe(
        Class<T> event,
        Listener<T> listener
    ) {
        // add cache entry
        this.eventTypeListenerCache.computeIfAbsent(event, key -> new ArrayList<>()).add((Listener<Event>) listener);
        // add subscribe (so cache entry isn't removed when unsubscribe is called)
        this.subscriberMap.computeIfAbsent(GLOBAL_LISTENERS_BACKING_OBJECT, key -> new ArrayList<>())
            .add(new TypedListener(event, (Listener<Event>) listener));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void subscribe(Object subscriber) {
        // the type of the LMF factory
        MethodType factoryType = MethodType.methodType(Listener.class, subscriber.getClass());
        // iterate over all methods (to catch programmer errors)
        for (Method method : subscriber.getClass().getDeclaredMethods()) {
            // check for @Listen
            if (method.isAnnotationPresent(Listen.class)) {
                int mm = method.getModifiers();
                // check is public
                if (!Modifier.isPublic(mm)) {
                    this.error(String.format("ERROR: %s#%s: Methods marked with `@Listen` must be public.",
                                             method.getDeclaringClass().getSimpleName(), method.getName()));
                    continue;
                }
                // check is non-static
                if (Modifier.isStatic(mm)) {
                    this.error(String.format("ERROR: %s#%s: Methods marked with `@Listen` must be non-static.",
                                             method.getDeclaringClass().getSimpleName(), method.getName()));
                    continue;
                }
                // check is non-abstract
                if (Modifier.isAbstract(mm)) {
                    this.error(String.format("ERROR: %s#%s: Methods marked with `@Listen` must be non-abstract.",
                                             method.getDeclaringClass().getSimpleName(), method.getName()));
                    continue;
                }
                /*
                // check is non-native
                if (Modifier.isNative(mm)) {
                    this.error(String.format("ERROR: %s#%s: Methods marked with `@Listen` must be non-native.",
                                             method.getDeclaringClass().getSimpleName(), method.getName()));
                    continue;
                }
                */
                // check method signature
                if (method.getReturnType() != void.class) {
                    this.error(String.format("ERROR: %s#%s: Methods marked with `@Listen` must return `void`.",
                                             method.getDeclaringClass().getSimpleName(), method.getName()));
                    continue;
                }

                // check parameters length
                Class<?>[] paramTypes = method.getParameterTypes();
                if (paramTypes.length != 1) {
                    this.error(String.format("ERROR: %s#%s: methods marked with `@Listen` must have only a single parameter.",
                                             method.getDeclaringClass().getSimpleName(), method.getName()));
                    continue;
                }

                // store type of first parameter
                Class<?> event = paramTypes[0];

                try {
                    // get method handle
                    MethodHandle handle = LOOKUP.unreflect(method);
                    // create Listener from handle
                    CallSite site = LambdaMetafactory.metafactory(LOOKUP, "invoke",
                                                                  factoryType,
                                                                  LISTENER_INVOKE_TYPE,
                                                                  handle,
                                                                  MethodType.methodType(void.class, event));
                    Listener<Event> listener = (Listener<Event>) site.getTarget().invoke(subscriber);
                    // add cache entry
                    this.eventTypeListenerCache.computeIfAbsent(event, key -> new ArrayList<>()).add(listener);
                    // add subscriber (don't rebuild cache)
                    this.subscriberMap.computeIfAbsent(subscriber, key -> new ArrayList<>()).add(new TypedListener(event, listener));
                } catch (IllegalAccessException ignored) {
                    // this should not happen
                } catch (ClassCastException e) {
                    this.error(String.format("ERROR: %s#%s: the parameter of methods marked `@Listen` must be a subclass of the PubSub<Event> `Event` type parameter.",
                                             method.getDeclaringClass().getSimpleName(), method.getName()));
                } catch (Throwable e) {
                    // error log exceptions from inside the event listener
                    this.error(String.format("ERROR: %s", e.getCause()));
                }
            }
        }
    }

    @Override
    public void unsubscribe(Object subscriber) {
        // remove subscriber
        this.subscriberMap.remove(subscriber);
        // clear and ...
        this.eventTypeListenerCache.clear();
        // rebuild cache from subscribers
        this.subscriberMap.values().forEach(typedListeners ->
                                                typedListeners.forEach(typedListener ->
                                                                           this.eventTypeListenerCache.computeIfAbsent(typedListener.type, key -> new ArrayList<>())
                                                                               .add(typedListener.listener)));
    }

    @Override
    public void publish(Event event) {
        final List<Listener<Event>> listeners = this.eventTypeListenerCache.get(event.getClass());

        if (listeners == null) return;

        int listenersSize = listeners.size();

        while (listenersSize > 0) {
            listeners.get(--listenersSize).invoke(event);
        }
    }

    @Override
    public void clear() {
        // clear subscriber map and cache
        this.subscriberMap.clear();
        this.eventTypeListenerCache.clear();
    }

    private void error(String msg) {
        this.logger.accept(msg);
    }

    private class TypedListener {
        private final Class<?> type;
        private final Listener<Event> listener;

        public TypedListener(Class<?> type, Listener<Event> listener) {
            this.type = type;
            this.listener = listener;
        }

        @Override
        public String toString() {
            return String.format("TypedListener { type: %s, listener: %s }", this.type.getSimpleName(), this.listener);
        }
    }
}