/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.eventbus;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventSubscriber;
import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.SubscriberFindingStrategy;
import com.google.common.eventbus.SynchronizedEventSubscriber;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

class AnnotatedSubscriberFinder
implements SubscriberFindingStrategy {
    private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class<?>, ImmutableList<Method>>(){

        @Override
        public ImmutableList<Method> load(Class<?> concreteClass) throws Exception {
            return AnnotatedSubscriberFinder.getAnnotatedMethodsInternal(concreteClass);
        }
    });

    AnnotatedSubscriberFinder() {
    }

    @Override
    public Multimap<Class<?>, EventSubscriber> findAllSubscribers(Object listener) {
        HashMultimap<Class<?>, EventSubscriber> methodsInListener = HashMultimap.create();
        Class<?> clazz = listener.getClass();
        for (Method method : AnnotatedSubscriberFinder.getAnnotatedMethods(clazz)) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> eventType = parameterTypes[0];
            EventSubscriber subscriber = AnnotatedSubscriberFinder.makeSubscriber(listener, method);
            methodsInListener.put(eventType, subscriber);
        }
        return methodsInListener;
    }

    private static ImmutableList<Method> getAnnotatedMethods(Class<?> clazz) {
        try {
            return subscriberMethodsCache.getUnchecked(clazz);
        }
        catch (UncheckedExecutionException e2) {
            throw Throwables.propagate(e2.getCause());
        }
    }

    private static ImmutableList<Method> getAnnotatedMethodsInternal(Class<?> clazz) {
        Set supers = TypeToken.of(clazz).getTypes().rawTypes();
        HashMap<MethodIdentifier, Method> identifiers = Maps.newHashMap();
        for (Class superClazz : supers) {
            for (Method superClazzMethod : superClazz.getMethods()) {
                if (!superClazzMethod.isAnnotationPresent(Subscribe.class)) continue;
                Class<?>[] parameterTypes = superClazzMethod.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException("Method " + superClazzMethod + " has @Subscribe annotation, but requires " + parameterTypes.length + " arguments.  Event subscriber methods must require a single argument.");
                }
                MethodIdentifier ident = new MethodIdentifier(superClazzMethod);
                if (identifiers.containsKey(ident)) continue;
                identifiers.put(ident, superClazzMethod);
            }
        }
        return ImmutableList.copyOf(identifiers.values());
    }

    private static EventSubscriber makeSubscriber(Object listener, Method method) {
        EventSubscriber wrapper = AnnotatedSubscriberFinder.methodIsDeclaredThreadSafe(method) ? new EventSubscriber(listener, method) : new SynchronizedEventSubscriber(listener, method);
        return wrapper;
    }

    private static boolean methodIsDeclaredThreadSafe(Method method) {
        return method.getAnnotation(AllowConcurrentEvents.class) != null;
    }

    private static final class MethodIdentifier {
        private final String name;
        private final List<Class<?>> parameterTypes;

        MethodIdentifier(Method method) {
            this.name = method.getName();
            this.parameterTypes = Arrays.asList(method.getParameterTypes());
        }

        public int hashCode() {
            return Objects.hashCode(this.name, this.parameterTypes);
        }

        public boolean equals(@Nullable Object o2) {
            if (o2 instanceof MethodIdentifier) {
                MethodIdentifier ident = (MethodIdentifier)o2;
                return this.name.equals(ident.name) && this.parameterTypes.equals(ident.parameterTypes);
            }
            return false;
        }
    }
}

