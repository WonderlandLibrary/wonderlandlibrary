// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.eventbus;

import java.util.concurrent.ConcurrentLinkedQueue;
import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Queue;
import java.util.Iterator;

@ElementTypesAreNonnullByDefault
abstract class Dispatcher
{
    static Dispatcher perThreadDispatchQueue() {
        return new PerThreadQueuedDispatcher();
    }
    
    static Dispatcher legacyAsync() {
        return new LegacyAsyncDispatcher();
    }
    
    static Dispatcher immediate() {
        return ImmediateDispatcher.INSTANCE;
    }
    
    abstract void dispatch(final Object p0, final Iterator<Subscriber> p1);
    
    private static final class PerThreadQueuedDispatcher extends Dispatcher
    {
        private final ThreadLocal<Queue<Event>> queue;
        private final ThreadLocal<Boolean> dispatching;
        
        private PerThreadQueuedDispatcher() {
            this.queue = new ThreadLocal<Queue<Event>>() {
                @Override
                protected Queue<Event> initialValue() {
                    return (Queue<Event>)Queues.newArrayDeque();
                }
            };
            this.dispatching = new ThreadLocal<Boolean>() {
                @Override
                protected Boolean initialValue() {
                    return false;
                }
            };
        }
        
        @Override
        void dispatch(final Object event, final Iterator<Subscriber> subscribers) {
            Preconditions.checkNotNull(event);
            Preconditions.checkNotNull(subscribers);
            final Queue<Event> queueForThread = this.queue.get();
            queueForThread.offer(new Event(event, (Iterator)subscribers));
            if (!this.dispatching.get()) {
                this.dispatching.set(true);
                try {
                    Event nextEvent;
                    while ((nextEvent = queueForThread.poll()) != null) {
                        while (nextEvent.subscribers.hasNext()) {
                            nextEvent.subscribers.next().dispatchEvent(nextEvent.event);
                        }
                    }
                }
                finally {
                    this.dispatching.remove();
                    this.queue.remove();
                }
            }
        }
        
        private static final class Event
        {
            private final Object event;
            private final Iterator<Subscriber> subscribers;
            
            private Event(final Object event, final Iterator<Subscriber> subscribers) {
                this.event = event;
                this.subscribers = subscribers;
            }
        }
    }
    
    private static final class LegacyAsyncDispatcher extends Dispatcher
    {
        private final ConcurrentLinkedQueue<EventWithSubscriber> queue;
        
        private LegacyAsyncDispatcher() {
            this.queue = Queues.newConcurrentLinkedQueue();
        }
        
        @Override
        void dispatch(final Object event, final Iterator<Subscriber> subscribers) {
            Preconditions.checkNotNull(event);
            while (subscribers.hasNext()) {
                this.queue.add(new EventWithSubscriber(event, (Subscriber)subscribers.next()));
            }
            EventWithSubscriber e;
            while ((e = this.queue.poll()) != null) {
                e.subscriber.dispatchEvent(e.event);
            }
        }
        
        private static final class EventWithSubscriber
        {
            private final Object event;
            private final Subscriber subscriber;
            
            private EventWithSubscriber(final Object event, final Subscriber subscriber) {
                this.event = event;
                this.subscriber = subscriber;
            }
        }
    }
    
    private static final class ImmediateDispatcher extends Dispatcher
    {
        private static final ImmediateDispatcher INSTANCE;
        
        @Override
        void dispatch(final Object event, final Iterator<Subscriber> subscribers) {
            Preconditions.checkNotNull(event);
            while (subscribers.hasNext()) {
                subscribers.next().dispatchEvent(event);
            }
        }
        
        static {
            INSTANCE = new ImmediateDispatcher();
        }
    }
}
