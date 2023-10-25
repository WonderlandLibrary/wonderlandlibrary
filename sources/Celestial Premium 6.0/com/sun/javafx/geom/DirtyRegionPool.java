/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.DirtyRegionContainer;
import java.util.Deque;
import java.util.LinkedList;

public final class DirtyRegionPool {
    private static final int POOL_SIZE_MIN = 4;
    private static final int EXPIRATION_TIME = 3000;
    private static final int COUNT_BETWEEN_EXPIRATION_CHECK = 90;
    private final int containerSize;
    private int clearCounter = 90;
    private final Deque<DirtyRegionContainer> fixed;
    private final Deque<PoolItem> unlocked;
    private final Deque<PoolItem> locked;

    public DirtyRegionPool(int n) {
        this.containerSize = n;
        this.fixed = new LinkedList<DirtyRegionContainer>();
        this.unlocked = new LinkedList<PoolItem>();
        this.locked = new LinkedList<PoolItem>();
        for (int i = 0; i < 4; ++i) {
            this.fixed.add(new DirtyRegionContainer(n));
        }
    }

    public DirtyRegionContainer checkOut() {
        this.clearExpired();
        if (!this.fixed.isEmpty()) {
            return this.fixed.pop();
        }
        if (!this.unlocked.isEmpty()) {
            PoolItem poolItem = this.unlocked.pop();
            this.locked.push(poolItem);
            return poolItem.container;
        }
        DirtyRegionContainer dirtyRegionContainer = new DirtyRegionContainer(this.containerSize);
        this.locked.push(new PoolItem(null, -1L));
        return dirtyRegionContainer;
    }

    public void checkIn(DirtyRegionContainer dirtyRegionContainer) {
        dirtyRegionContainer.reset();
        if (this.locked.isEmpty()) {
            this.fixed.push(dirtyRegionContainer);
        } else {
            PoolItem poolItem = this.locked.pop();
            poolItem.container = dirtyRegionContainer;
            poolItem.timeStamp = System.currentTimeMillis();
            this.unlocked.push(poolItem);
        }
    }

    private void clearExpired() {
        if (this.unlocked.isEmpty()) {
            return;
        }
        if (this.clearCounter-- == 0) {
            this.clearCounter = 90;
            PoolItem poolItem = this.unlocked.peekLast();
            long l = System.currentTimeMillis();
            while (poolItem != null && poolItem.timeStamp + 3000L < l) {
                this.unlocked.removeLast();
                poolItem = this.unlocked.peekLast();
            }
        }
    }

    private class PoolItem {
        DirtyRegionContainer container;
        long timeStamp;

        public PoolItem(DirtyRegionContainer dirtyRegionContainer, long l) {
            this.container = dirtyRegionContainer;
            this.timeStamp = l;
        }
    }
}

