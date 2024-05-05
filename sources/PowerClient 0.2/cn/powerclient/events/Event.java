/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.events;

public abstract class Event {
    protected boolean cancelled;
    private boolean onGround;

    public void fire() {
        this.cancelled = false;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}

