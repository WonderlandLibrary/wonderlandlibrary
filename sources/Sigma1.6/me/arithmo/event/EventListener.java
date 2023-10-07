/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.event;

import me.arithmo.event.Event;

public interface EventListener<E extends Event> {
    public void onEvent(E var1);
}

