/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.particles;

import java.util.Collection;
import java.util.LinkedList;

public final class EvictingList
extends LinkedList {
    private final int maxSize;

    @Override
    public boolean add(Object object) {
        if (this.size() >= this.maxSize) {
            this.removeFirst();
        }
        return super.add(object);
    }

    public EvictingList(int n) {
        this.maxSize = n;
    }

    public boolean isFull() {
        return this.size() >= this.maxSize;
    }

    public EvictingList(Collection collection, int n) {
        super(collection);
        this.maxSize = n;
    }
}

