// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.eventbus;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.base.Objects;
import java.util.Arrays;
import me.gong.mcleaks.util.google.common.cache.CacheLoader;
import me.gong.mcleaks.util.google.common.cache.CacheBuilder;
import me.gong.mcleaks.util.google.common.util.concurrent.UncheckedExecutionException;
import me.gong.mcleaks.util.google.common.base.Throwables;
import java.lang.annotation.Annotation;
import me.gong.mcleaks.util.google.common.reflect.TypeToken;
import me.gong.mcleaks.util.google.common.collect.HashMultimap;
import me.gong.mcleaks.util.google.common.collect.UnmodifiableIterator;
import java.util.List;
import me.gong.mcleaks.util.google.common.collect.Iterators;
import me.gong.mcleaks.util.google.common.collect.Lists;
import me.gong.mcleaks.util.google.common.annotations.VisibleForTesting;
import java.util.Set;
import java.util.Iterator;
import me.gong.mcleaks.util.google.common.collect.Multimap;
import me.gong.mcleaks.util.google.common.base.MoreObjects;
import java.util.Collection;
import java.util.Map;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.collect.Maps;
import me.gong.mcleaks.util.google.common.collect.ImmutableSet;
import java.lang.reflect.Method;
import me.gong.mcleaks.util.google.common.collect.ImmutableList;
import me.gong.mcleaks.util.google.common.cache.LoadingCache;
import me.gong.mcleaks.util.google.j2objc.annotations.Weak;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ConcurrentMap;

final class SubscriberRegistry
{
    private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers;
    @Weak
    private final EventBus bus;
    private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache;
    private static final LoadingCache<Class<?>, ImmutableSet<Class<?>>> flattenHierarchyCache;
    
    SubscriberRegistry(final EventBus bus) {
        this.subscribers = Maps.newConcurrentMap();
        this.bus = Preconditions.checkNotNull(bus);
    }
    
    void register(final Object listener) {
        final Multimap<Class<?>, Subscriber> listenerMethods = this.findAllSubscribers(listener);
        for (final Map.Entry<Class<?>, Collection<Subscriber>> entry : listenerMethods.asMap().entrySet()) {
            final Class<?> eventType = entry.getKey();
            final Collection<Subscriber> eventMethodsInListener = entry.getValue();
            CopyOnWriteArraySet<Subscriber> eventSubscribers = this.subscribers.get(eventType);
            if (eventSubscribers == null) {
                final CopyOnWriteArraySet<Subscriber> newSet = new CopyOnWriteArraySet<Subscriber>();
                eventSubscribers = MoreObjects.firstNonNull(this.subscribers.putIfAbsent(eventType, newSet), newSet);
            }
            eventSubscribers.addAll(eventMethodsInListener);
        }
    }
    
    void unregister(final Object listener) {
        final Multimap<Class<?>, Subscriber> listenerMethods = this.findAllSubscribers(listener);
        for (final Map.Entry<Class<?>, Collection<Subscriber>> entry : listenerMethods.asMap().entrySet()) {
            final Class<?> eventType = entry.getKey();
            final Collection<Subscriber> listenerMethodsForType = entry.getValue();
            final CopyOnWriteArraySet<Subscriber> currentSubscribers = this.subscribers.get(eventType);
            if (currentSubscribers == null || !currentSubscribers.removeAll(listenerMethodsForType)) {
                throw new IllegalArgumentException("missing event subscriber for an annotated method. Is " + listener + " registered?");
            }
        }
    }
    
    @VisibleForTesting
    Set<Subscriber> getSubscribersForTesting(final Class<?> eventType) {
        return MoreObjects.firstNonNull(this.subscribers.get(eventType), (Set<Subscriber>)ImmutableSet.of());
    }
    
    Iterator<Subscriber> getSubscribers(final Object event) {
        final ImmutableSet<Class<?>> eventTypes = flattenHierarchy(event.getClass());
        final List<Iterator<Subscriber>> subscriberIterators = (List<Iterator<Subscriber>>)Lists.newArrayListWithCapacity(eventTypes.size());
        for (final Class<?> eventType : eventTypes) {
            final CopyOnWriteArraySet<Subscriber> eventSubscribers = this.subscribers.get(eventType);
            if (eventSubscribers != null) {
                subscriberIterators.add(eventSubscribers.iterator());
            }
        }
        return Iterators.concat((Iterator<? extends Iterator<? extends Subscriber>>)subscriberIterators.iterator());
    }
    
    private Multimap<Class<?>, Subscriber> findAllSubscribers(final Object listener) {
        final Multimap<Class<?>, Subscriber> methodsInListener = (Multimap<Class<?>, Subscriber>)HashMultimap.create();
        final Class<?> clazz = listener.getClass();
        for (final Method method : getAnnotatedMethods(clazz)) {
            final Class<?>[] parameterTypes = method.getParameterTypes();
            final Class<?> eventType = parameterTypes[0];
            methodsInListener.put(eventType, Subscriber.create(this.bus, listener, method));
        }
        return methodsInListener;
    }
    
    private static ImmutableList<Method> getAnnotatedMethods(final Class<?> clazz) {
        return SubscriberRegistry.subscriberMethodsCache.getUnchecked(clazz);
    }
    
    private static ImmutableList<Method> getAnnotatedMethodsNotCached(final Class<?> clazz) {
        final Set<? extends Class<?>> supertypes = TypeToken.of(clazz).getTypes().rawTypes();
        final Map<MethodIdentifier, Method> identifiers = (Map<MethodIdentifier, Method>)Maps.newHashMap();
        for (final Class<?> supertype : supertypes) {
            for (final Method method : supertype.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Subscribe.class) && !method.isSynthetic()) {
                    final Class<?>[] parameterTypes = method.getParameterTypes();
                    Preconditions.checkArgument(parameterTypes.length == 1, "Method %s has @Subscribe annotation but has %s parameters.Subscriber methods must have exactly 1 parameter.", method, parameterTypes.length);
                    final MethodIdentifier ident = new MethodIdentifier(method);
                    if (!identifiers.containsKey(ident)) {
                        identifiers.put(ident, method);
                    }
                }
            }
        }
        return ImmutableList.copyOf((Collection<? extends Method>)identifiers.values());
    }
    
    @VisibleForTesting
    static ImmutableSet<Class<?>> flattenHierarchy(final Class<?> concreteClass) {
        try {
            return SubscriberRegistry.flattenHierarchyCache.getUnchecked(concreteClass);
        }
        catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }
    
    static {
        subscriberMethodsCache = CacheBuilder.newBuilder().weakKeys().build((CacheLoader<? super Class<?>, ImmutableList<Method>>)new CacheLoader<Class<?>, ImmutableList<Method>>() {
            @Override
            public ImmutableList<Method> load(final Class<?> concreteClass) throws Exception {
                return getAnnotatedMethodsNotCached(concreteClass);
            }
        });
        flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys().build((CacheLoader<? super Class<?>, ImmutableSet<Class<?>>>)new CacheLoader<Class<?>, ImmutableSet<Class<?>>>() {
            @Override
            public ImmutableSet<Class<?>> load(final Class<?> concreteClass) {
                return ImmutableSet.copyOf((Collection<? extends Class<?>>)TypeToken.of(concreteClass).getTypes().rawTypes());
            }
        });
    }
    
    private static final class MethodIdentifier
    {
        private final String name;
        private final List<Class<?>> parameterTypes;
        
        MethodIdentifier(final Method method) {
            this.name = method.getName();
            this.parameterTypes = Arrays.asList(method.getParameterTypes());
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(this.name, this.parameterTypes);
        }
        
        @Override
        public boolean equals(@Nullable final Object o) {
            if (o instanceof MethodIdentifier) {
                final MethodIdentifier ident = (MethodIdentifier)o;
                return this.name.equals(ident.name) && this.parameterTypes.equals(ident.parameterTypes);
            }
            return false;
        }
    }
}
