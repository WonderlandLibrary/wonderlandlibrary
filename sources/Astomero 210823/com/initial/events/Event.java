package com.initial.events;

import com.initial.events.impl.*;
import java.lang.reflect.*;
import java.util.*;

public abstract class Event
{
    protected boolean pre;
    public EventType type;
    private boolean cancelled;
    
    public Event() {
        this.pre = true;
    }
    
    public boolean isPre() {
        return this.type != null && this.type == EventType.PRE;
    }
    
    public boolean isPost() {
        return this.type != null && this.type == EventType.POST;
    }
    
    public Event call() {
        this.cancelled = false;
        call(this);
        return this;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    private static final void call(final Event event) {
        final ArrayHelper<Data> dataList = EventManager.get(event.getClass());
        if (dataList != null) {
            for (final Data data : dataList) {
                try {
                    data.target.invoke(data.source, event);
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    public enum State
    {
        PRE("PRE", 0), 
        POST("POST", 1);
        
        private State(final String string, final int number) {
        }
    }
}
