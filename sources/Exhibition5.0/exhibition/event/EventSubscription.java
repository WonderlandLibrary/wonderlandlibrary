// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event;

import exhibition.util.MinecraftUtil;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class EventSubscription<T extends Event>
{
    private final T event;
    private final List<EventListener> subscribed;
    
    public EventSubscription(final T event) {
        this.subscribed = new CopyOnWriteArrayList<EventListener>();
        this.event = event;
    }
    
    public void fire(final Event event) {
        if (MinecraftUtil.mc.thePlayer == null) {
            return;
        }
        this.subscribed.forEach(listener -> listener.onEvent(event));
    }
    
    public void add(final EventListener listener) {
        this.subscribed.add(listener);
    }
    
    public void remove(final EventListener listener) {
        if (this.subscribed.contains(listener)) {
            this.subscribed.remove(listener);
        }
    }
    
    public List<EventListener> getSubscribed() {
        return this.subscribed;
    }
    
    public Event getEvent() {
        return this.event;
    }
}
