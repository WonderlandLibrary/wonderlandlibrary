// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.eventbus;

import javax.annotation.CheckForNull;
import java.lang.reflect.InvocationTargetException;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.lang.reflect.Method;
import com.google.common.annotations.VisibleForTesting;
import com.google.j2objc.annotations.Weak;

@ElementTypesAreNonnullByDefault
class Subscriber
{
    @Weak
    private EventBus bus;
    @VisibleForTesting
    final Object target;
    private final Method method;
    private final Executor executor;
    
    static Subscriber create(final EventBus bus, final Object listener, final Method method) {
        return isDeclaredThreadSafe(method) ? new Subscriber(bus, listener, method) : new SynchronizedSubscriber(bus, listener, method);
    }
    
    private Subscriber(final EventBus bus, final Object target, final Method method) {
        this.bus = bus;
        this.target = Preconditions.checkNotNull(target);
        (this.method = method).setAccessible(true);
        this.executor = bus.executor();
    }
    
    final void dispatchEvent(final Object event) {
        this.executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Subscriber.this.invokeSubscriberMethod(event);
                }
                catch (InvocationTargetException e) {
                    Subscriber.this.bus.handleSubscriberException(e.getCause(), Subscriber.this.context(event));
                }
            }
        });
    }
    
    @VisibleForTesting
    void invokeSubscriberMethod(final Object event) throws InvocationTargetException {
        try {
            this.method.invoke(this.target, Preconditions.checkNotNull(event));
        }
        catch (IllegalArgumentException e) {
            final String value = String.valueOf(event);
            throw new Error(new StringBuilder(33 + String.valueOf(value).length()).append("Method rejected target/argument: ").append(value).toString(), e);
        }
        catch (IllegalAccessException e2) {
            final String value2 = String.valueOf(event);
            throw new Error(new StringBuilder(28 + String.valueOf(value2).length()).append("Method became inaccessible: ").append(value2).toString(), e2);
        }
        catch (InvocationTargetException e3) {
            if (e3.getCause() instanceof Error) {
                throw (Error)e3.getCause();
            }
            throw e3;
        }
    }
    
    private SubscriberExceptionContext context(final Object event) {
        return new SubscriberExceptionContext(this.bus, event, this.target, this.method);
    }
    
    @Override
    public final int hashCode() {
        return (31 + this.method.hashCode()) * 31 + System.identityHashCode(this.target);
    }
    
    @Override
    public final boolean equals(@CheckForNull final Object obj) {
        if (obj instanceof Subscriber) {
            final Subscriber that = (Subscriber)obj;
            return this.target == that.target && this.method.equals(that.method);
        }
        return false;
    }
    
    private static boolean isDeclaredThreadSafe(final Method method) {
        return method.getAnnotation(AllowConcurrentEvents.class) != null;
    }
    
    @VisibleForTesting
    static final class SynchronizedSubscriber extends Subscriber
    {
        private SynchronizedSubscriber(final EventBus bus, final Object target, final Method method) {
            super(bus, target, method, null);
        }
        
        @Override
        void invokeSubscriberMethod(final Object event) throws InvocationTargetException {
            synchronized (this) {
                super.invokeSubscriberMethod(event);
            }
        }
    }
}
