/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import net.java.games.input.Event;

public final class EventQueue {
    private final Event[] queue;
    private int head;
    private int tail;

    public EventQueue(int size) {
        this.queue = new Event[size + 1];
        for (int i2 = 0; i2 < this.queue.length; ++i2) {
            this.queue[i2] = new Event();
        }
    }

    final synchronized void add(Event event) {
        this.queue[this.tail].set(event);
        this.tail = this.increase(this.tail);
    }

    final synchronized boolean isFull() {
        return this.increase(this.tail) == this.head;
    }

    private final int increase(int x2) {
        return (x2 + 1) % this.queue.length;
    }

    public final synchronized boolean getNextEvent(Event event) {
        if (this.head == this.tail) {
            return false;
        }
        event.set(this.queue[this.head]);
        this.head = this.increase(this.head);
        return true;
    }
}

