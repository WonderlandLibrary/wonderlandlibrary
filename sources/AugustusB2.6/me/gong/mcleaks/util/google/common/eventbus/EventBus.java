// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.eventbus;

import java.lang.reflect.Method;
import me.gong.mcleaks.util.google.common.base.MoreObjects;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
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
            return Logger.getLogger(EventBus.class.getName() + "." + context.getEventBus().identifier());
        }
        
        private static String message(final SubscriberExceptionContext context) {
            final Method method = context.getSubscriberMethod();
            return "Exception thrown by subscriber method " + method.getName() + '(' + method.getParameterTypes()[0].getName() + ')' + " on subscriber " + context.getSubscriber() + " when dispatching event: " + context.getEvent();
        }
        
        static {
            INSTANCE = new LoggingHandler();
        }
    }
}
