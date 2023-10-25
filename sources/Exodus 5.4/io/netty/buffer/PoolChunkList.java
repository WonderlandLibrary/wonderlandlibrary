/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.PoolArena;
import io.netty.buffer.PoolChunk;
import io.netty.buffer.PooledByteBuf;
import io.netty.util.internal.StringUtil;

final class PoolChunkList<T> {
    private PoolChunk<T> head;
    private final PoolChunkList<T> nextList;
    private final int maxUsage;
    PoolChunkList<T> prevList;
    private final PoolArena<T> arena;
    private final int minUsage;

    void free(PoolChunk<T> poolChunk, long l) {
        poolChunk.free(l);
        if (poolChunk.usage() < this.minUsage) {
            this.remove(poolChunk);
            if (this.prevList == null) {
                assert (poolChunk.usage() == 0);
                this.arena.destroyChunk(poolChunk);
            } else {
                this.prevList.add(poolChunk);
            }
        }
    }

    void add(PoolChunk<T> poolChunk) {
        if (poolChunk.usage() >= this.maxUsage) {
            this.nextList.add(poolChunk);
            return;
        }
        poolChunk.parent = this;
        if (this.head == null) {
            this.head = poolChunk;
            poolChunk.prev = null;
            poolChunk.next = null;
        } else {
            poolChunk.prev = null;
            poolChunk.next = this.head;
            this.head.prev = poolChunk;
            this.head = poolChunk;
        }
    }

    boolean allocate(PooledByteBuf<T> pooledByteBuf, int n, int n2) {
        long l;
        if (this.head == null) {
            return false;
        }
        PoolChunk<T> poolChunk = this.head;
        while ((l = poolChunk.allocate(n2)) < 0L) {
            poolChunk = poolChunk.next;
            if (poolChunk != null) continue;
            return false;
        }
        poolChunk.initBuf(pooledByteBuf, l, n);
        if (poolChunk.usage() >= this.maxUsage) {
            this.remove(poolChunk);
            this.nextList.add(poolChunk);
        }
        return true;
    }

    private void remove(PoolChunk<T> poolChunk) {
        if (poolChunk == this.head) {
            this.head = poolChunk.next;
            if (this.head != null) {
                this.head.prev = null;
            }
        } else {
            PoolChunk poolChunk2;
            poolChunk.prev.next = poolChunk2 = poolChunk.next;
            if (poolChunk2 != null) {
                poolChunk2.prev = poolChunk.prev;
            }
        }
    }

    PoolChunkList(PoolArena<T> poolArena, PoolChunkList<T> poolChunkList, int n, int n2) {
        this.arena = poolArena;
        this.nextList = poolChunkList;
        this.minUsage = n;
        this.maxUsage = n2;
    }

    public String toString() {
        if (this.head == null) {
            return "none";
        }
        StringBuilder stringBuilder = new StringBuilder();
        PoolChunk<T> poolChunk = this.head;
        while (true) {
            stringBuilder.append(poolChunk);
            poolChunk = poolChunk.next;
            if (poolChunk == null) break;
            stringBuilder.append(StringUtil.NEWLINE);
        }
        return stringBuilder.toString();
    }
}

