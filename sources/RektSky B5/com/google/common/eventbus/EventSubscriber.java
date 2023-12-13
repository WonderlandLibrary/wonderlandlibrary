/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Nullable;

class EventSubscriber {
    private final Object target;
    private final Method method;

    EventSubscriber(Object target, Method method) {
        Preconditions.checkNotNull(target, "EventSubscriber target cannot be null.");
        Preconditions.checkNotNull(method, "EventSubscriber method cannot be null.");
        this.target = target;
        this.method = method;
        method.setAccessible(true);
    }

    public void handleEvent(Object event) throws InvocationTargetException {
        Preconditions.checkNotNull(event);
        try {
            this.method.invoke(this.target, event);
        }
        catch (IllegalArgumentException e2) {
            throw new Error("Method rejected target/argument: " + event, e2);
        }
        catch (IllegalAccessException e3) {
            throw new Error("Method became inaccessible: " + event, e3);
        }
        catch (InvocationTargetException e4) {
            if (e4.getCause() instanceof Error) {
                throw (Error)e4.getCause();
            }
            throw e4;
        }
    }

    public String toString() {
        return "[wrapper " + this.method + "]";
    }

    public int hashCode() {
        int PRIME = 31;
        return (31 + this.method.hashCode()) * 31 + System.identityHashCode(this.target);
    }

    public boolean equals(@Nullable Object obj) {
        if (obj instanceof EventSubscriber) {
            EventSubscriber that = (EventSubscriber)obj;
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

