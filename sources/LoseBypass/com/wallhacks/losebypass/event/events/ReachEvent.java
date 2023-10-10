/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.event.events;

import com.wallhacks.losebypass.event.eventbus.Event;

public class ReachEvent
extends Event {
    double reach;
    double hitbox;

    public double getReach() {
        return this.reach;
    }

    public double getHitbox() {
        return this.hitbox;
    }
}

