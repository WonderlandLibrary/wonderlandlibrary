/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.event;

import java.lang.reflect.InvocationTargetException;
import me.Tengoku.Terror.event.ArrayHelper;
import me.Tengoku.Terror.event.Data;
import me.Tengoku.Terror.event.EventDirection;
import me.Tengoku.Terror.event.EventManager;
import me.Tengoku.Terror.event.EventType;

public abstract class Event<T> {
    public EventDirection direction;
    public EventType type;
    private boolean cancelled;

    public EventDirection getDirection() {
        return this.direction;
    }

    public boolean isPre() {
        if (this.type == null) {
            return false;
        }
        return this.type == EventType.PRE;
    }

    public void setDirection(EventDirection eventDirection) {
        this.direction = eventDirection;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public Event call() {
        this.cancelled = false;
        Event.call(this);
        return this;
    }

    public boolean isIncoming() {
        if (this.direction == null) {
            return false;
        }
        return this.direction == EventDirection.INCOMING;
    }

    public EventType getType() {
        return this.type;
    }

    public boolean isOutgoing() {
        if (this.direction == null) {
            return false;
        }
        return this.direction == EventDirection.OUTGOING;
    }

    public boolean isPost() {
        if (this.type == null) {
            return false;
        }
        return this.type == EventType.POST;
    }

    private static final void call(Event event) {
        ArrayHelper<Data> arrayHelper = EventManager.get(event.getClass());
        if (arrayHelper != null) {
            for (Data data : arrayHelper) {
                try {
                    data.target.invoke(data.source, event);
                }
                catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
                catch (InvocationTargetException invocationTargetException) {
                    invocationTargetException.printStackTrace();
                }
            }
        }
    }

    public void setType(EventType eventType) {
        this.type = eventType;
    }

    public void setCancelled(boolean bl) {
        this.cancelled = bl;
    }
}

