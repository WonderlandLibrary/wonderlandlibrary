/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.BaseMpscLinkedArrayQueueColdProducerFields;
import com.github.benmanes.caffeine.cache.BaseMpscLinkedArrayQueueConsumerFields;
import com.github.benmanes.caffeine.cache.BaseMpscLinkedArrayQueueProducerFields;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.UnsafeAccess;
import com.github.benmanes.caffeine.cache.UnsafeRefArrayAccess;
import java.lang.reflect.Field;
import java.util.Iterator;

abstract class BaseMpscLinkedArrayQueue<E>
extends BaseMpscLinkedArrayQueueColdProducerFields<E> {
    private static final long P_INDEX_OFFSET;
    private static final long C_INDEX_OFFSET;
    private static final long P_LIMIT_OFFSET;
    private static final Object JUMP;

    BaseMpscLinkedArrayQueue(int initialCapacity) {
        if (initialCapacity < 2) {
            throw new IllegalArgumentException("Initial capacity must be 2 or more");
        }
        int p2capacity = Caffeine.ceilingPowerOfTwo(initialCapacity);
        long mask = (long)p2capacity - 1L << 1;
        E[] buffer = BaseMpscLinkedArrayQueue.allocate(p2capacity + 1);
        this.producerBuffer = buffer;
        this.producerMask = mask;
        this.consumerBuffer = buffer;
        this.consumerMask = mask;
        this.soProducerLimit(mask);
    }

    @Override
    public final Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "@" + Integer.toHexString(this.hashCode());
    }

    @Override
    public boolean offer(E e) {
        Object[] buffer;
        long mask;
        long pIndex;
        if (null == e) {
            throw new NullPointerException();
        }
        block6: while (true) {
            long producerLimit = this.lvProducerLimit();
            pIndex = this.lvProducerIndex();
            if ((pIndex & 1L) == 1L) continue;
            mask = this.producerMask;
            buffer = this.producerBuffer;
            if (producerLimit <= pIndex) {
                int result = this.offerSlowPath(mask, pIndex, producerLimit);
                switch (result) {
                    case 0: {
                        break;
                    }
                    case 1: {
                        continue block6;
                    }
                    case 2: {
                        return false;
                    }
                    case 3: {
                        this.resize(mask, buffer, pIndex, e);
                        return true;
                    }
                }
            }
            if (this.casProducerIndex(pIndex, pIndex + 2L)) break;
        }
        long offset = BaseMpscLinkedArrayQueue.modifiedCalcElementOffset(pIndex, mask);
        UnsafeRefArrayAccess.soElement(buffer, offset, e);
        return true;
    }

    private int offerSlowPath(long mask, long pIndex, long producerLimit) {
        long cIndex = this.lvConsumerIndex();
        long bufferCapacity = this.getCurrentBufferCapacity(mask);
        int result = 0;
        if (cIndex + bufferCapacity > pIndex) {
            if (!this.casProducerLimit(producerLimit, cIndex + bufferCapacity)) {
                result = 1;
            }
        } else {
            result = this.availableInQueue(pIndex, cIndex) <= 0L ? 2 : (this.casProducerIndex(pIndex, pIndex + 1L) ? 3 : 1);
        }
        return result;
    }

    protected abstract long availableInQueue(long var1, long var3);

    private static long modifiedCalcElementOffset(long index, long mask) {
        return UnsafeRefArrayAccess.REF_ARRAY_BASE + ((index & mask) << UnsafeRefArrayAccess.REF_ELEMENT_SHIFT - 1);
    }

    @Override
    public E poll() {
        Object[] buffer = this.consumerBuffer;
        long index = this.consumerIndex;
        long mask = this.consumerMask;
        long offset = BaseMpscLinkedArrayQueue.modifiedCalcElementOffset(index, mask);
        Object e = UnsafeRefArrayAccess.lvElement(buffer, offset);
        if (e == null) {
            if (index != this.lvProducerIndex()) {
                while ((e = UnsafeRefArrayAccess.lvElement(buffer, offset)) == null) {
                }
            } else {
                return null;
            }
        }
        if (e == JUMP) {
            Object[] nextBuffer = this.getNextBuffer(buffer, mask);
            return (E)this.newBufferPoll(nextBuffer, index);
        }
        UnsafeRefArrayAccess.soElement(buffer, offset, null);
        this.soConsumerIndex(index + 2L);
        return (E)e;
    }

    @Override
    public E peek() {
        Object[] buffer = this.consumerBuffer;
        long index = this.consumerIndex;
        long mask = this.consumerMask;
        long offset = BaseMpscLinkedArrayQueue.modifiedCalcElementOffset(index, mask);
        Object e = UnsafeRefArrayAccess.lvElement(buffer, offset);
        if (e == null && index != this.lvProducerIndex()) {
            while ((e = UnsafeRefArrayAccess.lvElement(buffer, offset)) == null) {
            }
        }
        if (e == JUMP) {
            return (E)this.newBufferPeek(this.getNextBuffer(buffer, mask), index);
        }
        return (E)e;
    }

    private E[] getNextBuffer(E[] buffer, long mask) {
        long nextArrayOffset = BaseMpscLinkedArrayQueue.nextArrayOffset(mask);
        Object[] nextBuffer = (Object[])UnsafeRefArrayAccess.lvElement(buffer, nextArrayOffset);
        UnsafeRefArrayAccess.soElement(buffer, nextArrayOffset, null);
        return nextBuffer;
    }

    private static long nextArrayOffset(long mask) {
        return BaseMpscLinkedArrayQueue.modifiedCalcElementOffset(mask + 2L, Long.MAX_VALUE);
    }

    private E newBufferPoll(E[] nextBuffer, long index) {
        long offsetInNew = this.newBufferAndOffset(nextBuffer, index);
        E n = UnsafeRefArrayAccess.lvElement(nextBuffer, offsetInNew);
        if (n == null) {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        UnsafeRefArrayAccess.soElement(nextBuffer, offsetInNew, null);
        this.soConsumerIndex(index + 2L);
        return n;
    }

    private E newBufferPeek(E[] nextBuffer, long index) {
        long offsetInNew = this.newBufferAndOffset(nextBuffer, index);
        E n = UnsafeRefArrayAccess.lvElement(nextBuffer, offsetInNew);
        if (null == n) {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        return n;
    }

    private long newBufferAndOffset(E[] nextBuffer, long index) {
        this.consumerBuffer = nextBuffer;
        this.consumerMask = (long)nextBuffer.length - 2L << 1;
        long offsetInNew = BaseMpscLinkedArrayQueue.modifiedCalcElementOffset(index, this.consumerMask);
        return offsetInNew;
    }

    @Override
    public final int size() {
        long currentProducerIndex;
        long before;
        long after = this.lvConsumerIndex();
        do {
            before = after;
            currentProducerIndex = this.lvProducerIndex();
        } while (before != (after = this.lvConsumerIndex()));
        long size = currentProducerIndex - after >> 1;
        if (size > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int)size;
    }

    @Override
    public final boolean isEmpty() {
        return this.lvConsumerIndex() == this.lvProducerIndex();
    }

    private long lvProducerIndex() {
        return UnsafeAccess.UNSAFE.getLongVolatile(this, P_INDEX_OFFSET);
    }

    private long lvConsumerIndex() {
        return UnsafeAccess.UNSAFE.getLongVolatile(this, C_INDEX_OFFSET);
    }

    private void soProducerIndex(long v) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, P_INDEX_OFFSET, v);
    }

    private boolean casProducerIndex(long expect, long newValue) {
        return UnsafeAccess.UNSAFE.compareAndSwapLong(this, P_INDEX_OFFSET, expect, newValue);
    }

    private void soConsumerIndex(long v) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, C_INDEX_OFFSET, v);
    }

    private long lvProducerLimit() {
        return this.producerLimit;
    }

    private boolean casProducerLimit(long expect, long newValue) {
        return UnsafeAccess.UNSAFE.compareAndSwapLong(this, P_LIMIT_OFFSET, expect, newValue);
    }

    private void soProducerLimit(long v) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, P_LIMIT_OFFSET, v);
    }

    public long currentProducerIndex() {
        return this.lvProducerIndex() / 2L;
    }

    public long currentConsumerIndex() {
        return this.lvConsumerIndex() / 2L;
    }

    public abstract int capacity();

    public boolean relaxedOffer(E e) {
        return this.offer(e);
    }

    public E relaxedPoll() {
        Object[] buffer = this.consumerBuffer;
        long index = this.consumerIndex;
        long mask = this.consumerMask;
        long offset = BaseMpscLinkedArrayQueue.modifiedCalcElementOffset(index, mask);
        Object e = UnsafeRefArrayAccess.lvElement(buffer, offset);
        if (e == null) {
            return null;
        }
        if (e == JUMP) {
            Object[] nextBuffer = this.getNextBuffer(buffer, mask);
            return (E)this.newBufferPoll(nextBuffer, index);
        }
        UnsafeRefArrayAccess.soElement(buffer, offset, null);
        this.soConsumerIndex(index + 2L);
        return (E)e;
    }

    public E relaxedPeek() {
        Object[] buffer = this.consumerBuffer;
        long index = this.consumerIndex;
        long mask = this.consumerMask;
        long offset = BaseMpscLinkedArrayQueue.modifiedCalcElementOffset(index, mask);
        Object e = UnsafeRefArrayAccess.lvElement(buffer, offset);
        if (e == JUMP) {
            return (E)this.newBufferPeek(this.getNextBuffer(buffer, mask), index);
        }
        return (E)e;
    }

    private void resize(long oldMask, E[] oldBuffer, long pIndex, E e) {
        int newBufferLength = this.getNextBufferSize(oldBuffer);
        E[] newBuffer = BaseMpscLinkedArrayQueue.allocate(newBufferLength);
        this.producerBuffer = newBuffer;
        int newMask = newBufferLength - 2 << 1;
        this.producerMask = newMask;
        long offsetInOld = BaseMpscLinkedArrayQueue.modifiedCalcElementOffset(pIndex, oldMask);
        long offsetInNew = BaseMpscLinkedArrayQueue.modifiedCalcElementOffset(pIndex, newMask);
        UnsafeRefArrayAccess.soElement(newBuffer, offsetInNew, e);
        UnsafeRefArrayAccess.soElement(oldBuffer, BaseMpscLinkedArrayQueue.nextArrayOffset(oldMask), newBuffer);
        long cIndex = this.lvConsumerIndex();
        long availableInQueue = this.availableInQueue(pIndex, cIndex);
        if (availableInQueue <= 0L) {
            throw new IllegalStateException();
        }
        this.soProducerLimit(pIndex + Math.min((long)newMask, availableInQueue));
        this.soProducerIndex(pIndex + 2L);
        UnsafeRefArrayAccess.soElement(oldBuffer, offsetInOld, JUMP);
    }

    public static <E> E[] allocate(int capacity) {
        return new Object[capacity];
    }

    protected abstract int getNextBufferSize(E[] var1);

    protected abstract long getCurrentBufferCapacity(long var1);

    static {
        Field iField;
        try {
            iField = BaseMpscLinkedArrayQueueProducerFields.class.getDeclaredField("producerIndex");
            P_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(iField);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        try {
            iField = BaseMpscLinkedArrayQueueConsumerFields.class.getDeclaredField("consumerIndex");
            C_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(iField);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        try {
            iField = BaseMpscLinkedArrayQueueColdProducerFields.class.getDeclaredField("producerLimit");
            P_LIMIT_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(iField);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        JUMP = new Object();
    }
}

