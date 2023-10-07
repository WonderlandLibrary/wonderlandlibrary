/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.arithmo.event.Event;
import me.arithmo.event.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class EventSubscription<T extends Event> {
    private final T event;
    private final List<EventListener> subscribed = new CopyOnWriteArrayList<EventListener>();

    public EventSubscription(T event) {
        this.event = event;
    }

    public void fire(Event event) {
        for (EventListener module : this.subscribed) {
            if (Minecraft.getMinecraft().thePlayer == null) continue;
            module.onEvent(event);
        }
    }

    public void add(EventListener listener) {
        this.subscribed.add(listener);
    }

    public void remove(EventListener listener) {
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

