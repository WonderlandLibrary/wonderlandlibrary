/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.eventbus.AnnotatedSubscriberFinder;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventSubscriber;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.google.common.eventbus.SubscriberFindingStrategy;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Beta
public class EventBus {
    private static final LoadingCache<Class<?>, Set<Class<?>>> flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class<?>, Set<Class<?>>>(){

        @Override
        public Set<Class<?>> load(Class<?> concreteClass) {
            return TypeToken.of(concreteClass).getTypes().rawTypes();
        }
    });
    private final SetMultimap<Class<?>, EventSubscriber> subscribersByType = HashMultimap.create();
    private final ReadWriteLock subscribersByTypeLock = new ReentrantReadWriteLock();
    private final SubscriberFindingStrategy finder = new AnnotatedSubscriberFinder();
    private final ThreadLocal<Queue<EventWithSubscriber>> eventsToDispatch = new ThreadLocal<Queue<EventWithSubscriber>>(){

        @Override
        protected Queue<EventWithSubscriber> initialValue() {
            return new LinkedList<EventWithSubscriber>();
        }
    };
    private final ThreadLocal<Boolean> isDispatching = new ThreadLocal<Boolean>(){

        @Override
        protected Boolean initialValue() {
            return false;
        }
    };
    private SubscriberExceptionHandler subscriberExceptionHandler;

    public EventBus() {
        this("default");
    }

    public EventBus(String identifier) {
        this(new LoggingSubscriberExceptionHandler(identifier));
    }

    public EventBus(SubscriberExceptionHandler subscriberExceptionHandler) {
        this.subscriberExceptionHandler = Preconditions.checkNotNull(subscriberExceptionHandler);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void register(Object object) {
        Multimap<Class<?>, EventSubscriber> methodsInListener = this.finder.findAllSubscribers(object);
        this.subscribersByTypeLock.writeLock().lock();
        try {
            this.subscribersByType.putAll(methodsInListener);
        }
        finally {
            this.subscribersByTypeLock.writeLock().unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void unregister(Object object) {
        Multimap<Class<?>, EventSubscriber> methodsInListener = this.finder.findAllSubscribers(object);
        for (Map.Entry<Class<?>, Collection<EventSubscriber>> entry : methodsInListener.asMap().entrySet()) {
            Class<?> eventType = entry.getKey();
            Collection<EventSubscriber> eventMethodsInListener = entry.getValue();
            this.subscribersByTypeLock.writeLock().lock();
            try {
                Set<EventSubscriber> currentSubscribers = this.subscribersByType.get(eventType);
                if (!currentSubscribers.containsAll(eventMethodsInListener)) {
                    throw new IllegalArgumentException("missing event subscriber for an annotated method. Is " + object + " registered?");
                }
                currentSubscribers.removeAll(eventMethodsInListener);
            }
            finally {
                this.subscribersByTypeLock.writeLock().unlock();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void post(Object event) {
        Set<Class<?>> dispatchTypes = this.flattenHierarchy(event.getClass());
        boolean dispatched = false;
        for (Class<?> eventType : dispatchTypes) {
            this.subscribersByTypeLock.readLock().lock();
            try {
                Set<EventSubscriber> wrappers = this.subscribersByType.get(eventType);
                if (wrappers.isEmpty()) continue;
                dispatched = true;
                for (EventSubscriber wrapper : wrappers) {
                    this.enqueueEvent(event, wrapper);
                }
            }
            finally {
                this.subscribersByTypeLock.readLock().unlock();
            }
        }
        if (!dispatched && !(event instanceof DeadEvent)) {
            this.post(new DeadEvent(this, event));
        }
        this.dispatchQueuedEvents();
    }

    void enqueueEvent(Object event, EventSubscriber subscriber) {
        this.eventsToDispatch.get().offer(new EventWithSubscriber(event, subscriber));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void dispatchQueuedEvents() {
        if (this.isDispatching.get().booleanValue()) {
            return;
        }
        this.isDispatching.set(true);
        try {
            EventWithSubscriber eventWithSubscriber;
            Queue<EventWithSubscriber> events = this.eventsToDispatch.get();
            while ((eventWithSubscriber = events.poll()) != null) {
                this.dispatch(eventWithSubscriber.event, eventWithSubscriber.subscriber);
            }
        }
        finally {
            this.isDispatching.remove();
            this.eventsToDispatch.remove();
        }
    }

    void dispatch(Object event, EventSubscriber wrapper) {
        try {
            wrapper.handleEvent(event);
        }
        catch (InvocationTargetException e2) {
            try {
                this.subscriberExceptionHandler.handleException(e2.getCause(), new SubscriberExceptionContext(this, event, wrapper.getSubscriber(), wrapper.getMethod()));
            }
            catch (Throwable t2) {
                Logger.getLogger(EventBus.class.getName()).log(Level.SEVERE, String.format("Exception %s thrown while handling exception: %s", t2, e2.getCause()), t2);
            }
        }
    }

    @VisibleForTesting
    Set<Class<?>> flattenHierarchy(Class<?> concreteClass) {
        try {
            return flattenHierarchyCache.getUnchecked(concreteClass);
        }
        catch (UncheckedExecutionException e2) {
            throw Throwables.propagate(e2.getCause());
        }
    }

    static class EventWithSubscriber {
        final Object event;
        final EventSubscriber subscriber;

        public EventWithSubscriber(Object event, EventSubscriber subscriber) {
            this.event = Preconditions.checkNotNull(event);
            this.subscriber = Preconditions.checkNotNull(subscriber);
        }
    }

    private static final class LoggingSubscriberExceptionHandler
    implements SubscriberExceptionHandler {
        private final Logger logger;

        public LoggingSubscriberExceptionHandler(String identifier) {
            this.logger = Logger.getLogger(EventBus.class.getName() + "." + Preconditions.checkNotNull(identifier));
        }

        @Override
        public void handleException(Throwable exception, SubscriberExceptionContext context) {
            this.logger.log(Level.SEVERE, "Could not dispatch event: " + context.getSubscriber() + " to " + context.getSubscriberMethod(), exception.getCause());
        }
    }
}

