// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.eventbus;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import com.google.common.base.Preconditions;
import java.lang.reflect.Method;

class EventSubscriber
{
    private final Object target;
    private final Method method;
    
    EventSubscriber(final Object target, final Method method) {
        Preconditions.checkNotNull(target, (Object)"EventSubscriber target cannot be null.");
        Preconditions.checkNotNull(method, (Object)"EventSubscriber method cannot be null.");
        this.target = target;
        (this.method = method).setAccessible(true);
    }
    
    public void handleEvent(final Object event) throws InvocationTargetException {
        Preconditions.checkNotNull(event);
        try {
            this.method.invoke(this.target, event);
        }
        catch (final IllegalArgumentException e) {
            final String value = String.valueOf(String.valueOf(event));
            throw new Error(new StringBuilder(33 + value.length()).append("Method rejected target/argument: ").append(value).toString(), e);
        }
        catch (final IllegalAccessException e2) {
            final String value2 = String.valueOf(String.valueOf(event));
            throw new Error(new StringBuilder(28 + value2.length()).append("Method became inaccessible: ").append(value2).toString(), e2);
        }
        catch (final InvocationTargetException e3) {
            if (e3.getCause() instanceof Error) {
                throw (Error)e3.getCause();
            }
            throw e3;
        }
    }
    
    @Override
    public String toString() {
        final String value = String.valueOf(String.valueOf(this.method));
        return new StringBuilder(10 + value.length()).append("[wrapper ").append(value).append("]").toString();
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 31;
        return (31 + this.method.hashCode()) * 31 + System.identityHashCode(this.target);
    }
    
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj instanceof EventSubscriber) {
            final EventSubscriber that = (EventSubscriber)obj;
            return this.target == that.target && this.method.equals(that.method);
        }
        return false;
    }
    
    public Object getSubscriber() {
        return this.target;
    }
    
    public Method getMethod() {
        return this.method;
    }
}
