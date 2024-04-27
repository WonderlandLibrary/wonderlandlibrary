/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.markers.KMappedMarker
 */
package cc.paimon.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.jvm.internal.markers.KMappedMarker;

public final class RingBuffer
implements KMappedMarker,
Iterable {
    private int tail;
    private final int maxCapacity;
    private int size;
    private final Object[] array;

    public static final void access$setSize$p(RingBuffer ringBuffer, int n) {
        ringBuffer.size = n;
    }

    public static final int access$getSize$p(RingBuffer ringBuffer) {
        return ringBuffer.size;
    }

    public final void add(Object object) {
        this.tail = (this.tail + 1) % this.maxCapacity;
        this.array[this.tail] = object;
        if (this.size < this.maxCapacity) {
            int n = this.size;
            this.size = n + 1;
        }
    }

    public RingBuffer(int n) {
        Object[] objectArray;
        int n2 = this.maxCapacity = n;
        RingBuffer ringBuffer = this;
        Object[] objectArray2 = new Object[n2];
        int n3 = 0;
        while (n3 < n2) {
            int n4 = n3;
            int n5 = n3++;
            objectArray = objectArray2;
            boolean bl = false;
            Object var10_10 = null;
            objectArray[n5] = var10_10;
        }
        objectArray = objectArray2;
        ringBuffer.array = objectArray;
    }

    public final int getSize() {
        return this.size;
    }

    public final Object get(int n) {
        Object object;
        if (!((this.size == 0 || n >= this.size || n < 0 ? null : (object = this.size == this.maxCapacity ? this.array[(this.getHead() + n) % this.maxCapacity] : this.array[n])) instanceof Object)) {
            object = null;
        }
        return object;
    }

    public Iterator iterator() {
        return new Iterator(this){
            private final AtomicInteger index;
            final RingBuffer this$0;

            public Object next() {
                Object object = this.this$0.get(this.index.getAndIncrement());
                if (object == null) {
                    throw (Throwable)new NoSuchElementException();
                }
                return object;
            }

            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            public boolean hasNext() {
                return this.index.get() < this.this$0.getSize();
            }

            static {
            }
            {
                this.this$0 = ringBuffer;
                this.index = new AtomicInteger(0);
            }
        };
    }

    private final int getHead() {
        return this.size == this.maxCapacity ? (this.tail + 1) % this.size : 0;
    }

    public final int getMaxCapacity() {
        return this.maxCapacity;
    }
}

