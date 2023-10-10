// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.eventbus;

import java.lang.reflect.Method;
import com.google.common.base.MoreObjects;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

@ElementTypesAreNonnullByDefault
public class EventBus
{
    private static final Logger logger;
    private final String identifier;
    private final Executor executor;
    private final SubscriberExceptionHandler exceptionHandler;
    private final SubscriberRegistry subscribers;
    private final Dispatcher dispatcher;
    
    public EventBus() {
        this("default");
    }
    
    public EventBus(final String identifier) {
        this(identifier, MoreExecutors.directExecutor(), Dispatcher.perThreadDispatchQueue(), LoggingHandler.INSTANCE);
    }
    
    public EventBus(final SubscriberExceptionHandler exceptionHandler) {
        this("default", MoreExecutors.directExecutor(), Dispatcher.perThreadDispatchQueue(), exceptionHandler);
    }
    
    EventBus(final String identifier, final Executor executor, final Dispatcher dispatcher, final SubscriberExceptionHandler exceptionHandler) {
        this.subscribers = new SubscriberRegistry(this);
        this.identifier = Preconditions.checkNotNull(identifier);
        this.executor = Preconditions.checkNotNull(executor);
        this.dispatcher = Preconditions.checkNotNull(dispatcher);
        this.exceptionHandler = Preconditions.checkNotNull(exceptionHandler);
    }
    
    public final String identifier() {
        return this.identifier;
    }
    
    final Executor executor() {
        return this.executor;
    }
    
    void handleSubscriberException(final Throwable e, final SubscriberExceptionContext context) {
        Preconditions.checkNotNull(e);
        Preconditions.checkNotNull(context);
        try {
            this.exceptionHandler.handleException(e, context);
        }
        catch (Throwable e2) {
            EventBus.logger.log(Level.SEVERE, String.format(Locale.ROOT, "Exception %s thrown while handling exception: %s", e2, e), e2);
        }
    }
    
    public void register(final Object object) {
        this.subscribers.register(object);
    }
    
    public void unregister(final Object object) {
        this.subscribers.unregister(object);
    }
    
    public void post(final Object event) {
        final Iterator<Subscriber> eventSubscribers = this.subscribers.getSubscribers(event);
        if (eventSubscribers.hasNext()) {
            this.dispatcher.dispatch(event, eventSubscribers);
        }
        else if (!(event instanceof DeadEvent)) {
            this.post(new DeadEvent(this, event));
        }
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(this.identifier).toString();
    }
    
    static {
        logger = Logger.getLogger(EventBus.class.getName());
    }
    
    static final class LoggingHandler implements SubscriberExceptionHandler
    {
        static final LoggingHandler INSTANCE;
        
        @Override
        public void handleException(final Throwable exception, final SubscriberExceptionContext context) {
            final Logger logger = logger(context);
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, message(context), exception);
            }
        }
        
        private static Logger logger(final SubscriberExceptionContext context) {
            final String name = EventBus.class.getName();
            final String identifier = context.getEventBus().identifier();
            return Logger.getLogger(new StringBuilder(1 + String.valueOf(name).length() + String.valueOf(identifier).length()).append(name).append(".").append(identifier).toString());
        }
        
        private static String message(final SubscriberExceptionContext context) {
            final Method method = context.getSubscriberMethod();
            final String name = method.getName();
            final String name2 = method.getParameterTypes()[0].getName();
            final String value = String.valueOf(context.getSubscriber());
            final String value2 = String.valueOf(context.getEvent());
            return new StringBuilder(80 + String.valueOf(name).length() + String.valueOf(name2).length() + String.valueOf(value).length() + String.valueOf(value2).length()).append("Exception thrown by subscriber method ").append(name).append('(').append(name2).append(')').append(" on subscriber ").append(value).append(" when dispatching event: ").append(value2).toString();
        }
        
        static {
            INSTANCE = new LoggingHandler();
        }
    }
}
