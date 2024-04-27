/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.particles;

import java.util.Collection;
import java.util.LinkedList;

public final class EvictingList<T>
extends LinkedList<T> {
    private final int maxSize;

    public EvictingList(int maxSize) {
        this.maxSize = maxSize;
    }

    public EvictingList(Collection<? extends T> c, int maxSize) {
        super(c);
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(T t2) {
        if (this.size() >= this.maxSize) {
            this.removeFirst();
        }
        return super.add(t2);
    }

    public boolean isFull() {
        return this.size() >= this.maxSize;
    }
}

