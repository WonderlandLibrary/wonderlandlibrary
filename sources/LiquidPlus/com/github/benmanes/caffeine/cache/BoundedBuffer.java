/*
 * Decompiled with CFR 0.152.
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.BBHeader;
import com.github.benmanes.caffeine.cache.Buffer;
import com.github.benmanes.caffeine.cache.StripedBuffer;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Consumer;

final class BoundedBuffer<E>
extends StripedBuffer<E> {
    static final int BUFFER_SIZE = 16;
    static final int MASK = 15;

    BoundedBuffer() {
    }

    @Override
    protected Buffer<E> create(E e) {
        return new RingBuffer<E>(e);
    }

    static final class RingBuffer<E>
    extends BBHeader.ReadAndWriteCounterRef
    implements Buffer<E> {
        final AtomicReferenceArray<E> buffer = new AtomicReferenceArray(16);

        public RingBuffer(E e) {
            this.buffer.lazySet(0, e);
        }

        @Override
        public int offer(E e) {
            long head = this.readCounter;
            long tail = this.relaxedWriteCounter();
            long size = tail - head;
            if (size >= 16L) {
                return 1;
            }
            if (this.casWriteCounter(tail, tail + 1L)) {
                int index = (int)(tail & 0xFL);
                this.buffer.lazySet(index, e);
                return 0;
            }
            return -1;
        }

        @Override
        public void drainTo(Consumer<E> consumer) {
            int index;
            E e;
            long head = this.readCounter;
            long tail = this.relaxedWriteCounter();
            long size = tail - head;
            if (size == 0L) {
                return;
            }
            while ((e = this.buffer.get(index = (int)(head & 0xFL))) != null) {
                this.buffer.lazySet(index, null);
                consumer.accept(e);
                if (++head != tail) continue;
            }
            this.lazySetReadCounter(head);
        }

        @Override
        public int reads() {
            return (int)this.readCounter;
        }

        @Override
        public int writes() {
            return (int)this.writeCounter;
        }
    }
}

