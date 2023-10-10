// 
// Decompiled by Procyon v0.5.36
// 

package net.lenni0451.eventapi.reflection;

import net.lenni0451.eventapi.events.IEvent;
import java.lang.reflect.Method;
import net.lenni0451.eventapi.listener.IEventListener;

public class ReflectedEventListener implements IEventListener
{
    private final Object callInstance;
    private final Class<?> eventClass;
    private final Method method;
    
    public ReflectedEventListener(final Object callInstance, final Class<?> eventClass, final Method method) {
        this.callInstance = callInstance;
        this.eventClass = eventClass;
        this.method = method;
    }
    
    public Object getCallInstance() {
        return this.callInstance;
    }
    
    @Override
    public void onEvent(final IEvent event) {
        try {
            this.method.invoke(this.callInstance, this.eventClass.cast(event));
        }
        catch (Throwable e) {
            throw new RuntimeException("Error invoking reflected method", e);
        }
    }
}
