// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class EventProtocol
{
    private final List<EventListener> listeners;
    
    public EventProtocol() {
        this.listeners = new CopyOnWriteArrayList<EventListener>();
    }
    
    public void register(final Object listener) {
        if (!(listener instanceof EventListener)) {
            throw new IllegalArgumentException("Listener must implement EventListener");
        }
        this.listeners.add((EventListener)listener);
    }
    
    public void unregister(final Object listener) {
        if (!(listener instanceof EventListener)) {
            throw new IllegalArgumentException("Listener must implement EventListener");
        }
        this.listeners.remove(listener);
    }
    
    public void handleEvent(final Event event) {
        for (final EventListener listener : this.listeners) {
            try {
                listener.onEvent(event);
            }
            catch (Exception exception) {
                exception.printStackTrace();
                System.err.println("Error handling event " + event.getClass().getSimpleName() + " for listener " + listener.getClass().getSimpleName());
            }
        }
    }
}
