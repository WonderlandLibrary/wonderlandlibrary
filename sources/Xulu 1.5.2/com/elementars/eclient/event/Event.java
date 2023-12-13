// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event;

import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.util.Wrapper;

public abstract class Event
{
    private State state;
    private boolean cancelled;
    private final float partialTicks;
    
    public Event() {
        this.state = State.PRE;
        this.partialTicks = Wrapper.getMinecraft().getRenderPartialTicks();
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public State getEventState() {
        return this.state;
    }
    
    public void setState(final State state) {
        this.state = state;
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
    
    private static void call(final Event event) {
        final ArrayHelper<Data> dataList = Xulu.EVENT_MANAGER.get(event.getClass());
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
        PRE, 
        POST;
    }
}
