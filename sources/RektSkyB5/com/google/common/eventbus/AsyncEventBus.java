/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.EventSubscriber;
import com.google.common.eventbus.SubscriberExceptionHandler;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

@Beta
public class AsyncEventBus
extends EventBus {
    private final Executor executor;
    private final ConcurrentLinkedQueue<EventBus.EventWithSubscriber> eventsToDispatch = new ConcurrentLinkedQueue();

    public AsyncEventBus(String identifier, Executor executor) {
        super(identifier);
        this.executor = Preconditions.checkNotNull(executor);
    }

    public AsyncEventBus(Executor executor, SubscriberExceptionHandler subscriberExceptionHandler) {
        super(subscriberExceptionHandler);
        this.executor = Preconditions.checkNotNull(executor);
    }

    public AsyncEventBus(Executor executor) {
        super("default");
        this.executor = Preconditions.checkNotNull(executor);
    }

    @Override
    void enqueueEvent(Object event, EventSubscriber subscriber) {
        this.eventsToDispatch.offer(new EventBus.EventWithSubscriber(event, subscriber));
    }

    @Override
    protected void dispatchQueuedEvents() {
        EventBus.EventWithSubscriber eventWithSubscriber;
        while ((eventWithSubscriber = this.eventsToDispatch.poll()) != null) {
            this.dispatch(eventWithSubscriber.event, eventWithSubscriber.subscriber);
        }
    }

    @Override
    void dispatch(final Object event, final EventSubscriber subscriber) {
        Preconditions.checkNotNull(event);
        Preconditions.checkNotNull(subscriber);
        this.executor.execute(new Runnable(){

            @Override
            public void run() {
                AsyncEventBus.super.dispatch(event, subscriber);
            }
        });
    }
}

