/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.report.liquidware.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001c\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010(\n\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0013\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0015J\u0018\u0010\u0016\u001a\u0004\u0018\u00018\u00002\u0006\u0010\u0017\u001a\u00020\u0004H\u0086\u0002\u00a2\u0006\u0002\u0010\u0018J\u000f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00028\u00000\u001aH\u0096\u0002R\u0018\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0007X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\tR\u0014\u0010\n\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u001e\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u0004@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\fR\u000e\u0010\u0011\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lme/report/liquidware/utils/RingBuffer;", "T", "", "maxCapacity", "", "(I)V", "array", "", "", "[Ljava/lang/Object;", "head", "getHead", "()I", "getMaxCapacity", "<set-?>", "size", "getSize", "tail", "add", "", "element", "(Ljava/lang/Object;)V", "get", "index", "(I)Ljava/lang/Object;", "iterator", "", "KyinoClient"})
public final class RingBuffer<T>
implements Iterable<T>,
KMappedMarker {
    private final Object[] array;
    private int size;
    private int tail;
    private final int maxCapacity;

    public final int getSize() {
        return this.size;
    }

    private final int getHead() {
        return this.size == this.maxCapacity ? (this.tail + 1) % this.size : 0;
    }

    public final void add(T element) {
        this.tail = (this.tail + 1) % this.maxCapacity;
        this.array[this.tail] = element;
        if (this.size < this.maxCapacity) {
            int n = this.size;
            this.size = n + 1;
        }
    }

    @Nullable
    public final T get(int index) {
        Object object;
        if (!((this.size == 0 || index >= this.size || index < 0 ? null : (object = this.size == this.maxCapacity ? this.array[(this.getHead() + index) % this.maxCapacity] : this.array[index])) instanceof Object)) {
            object = null;
        }
        return (T)object;
    }

    @Override
    @NotNull
    public Iterator<T> iterator() {
        return new Iterator<T>(this){
            private final AtomicInteger index;
            final /* synthetic */ RingBuffer this$0;

            public boolean hasNext() {
                return this.index.get() < this.this$0.getSize();
            }

            public T next() {
                T t = this.this$0.get(this.index.getAndIncrement());
                if (t == null) {
                    throw (Throwable)new NoSuchElementException();
                }
                return t;
            }
            {
                this.this$0 = $outer;
                this.index = new AtomicInteger(0);
            }

            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }
        };
    }

    public final int getMaxCapacity() {
        return this.maxCapacity;
    }

    public RingBuffer(int maxCapacity) {
        Object[] objectArray;
        int n = this.maxCapacity = maxCapacity;
        RingBuffer ringBuffer = this;
        Object[] objectArray2 = new Object[n];
        int n2 = 0;
        while (n2 < n) {
            int n3 = n2;
            int n4 = n2++;
            objectArray = objectArray2;
            boolean bl = false;
            Object var10_10 = null;
            objectArray[n4] = var10_10;
        }
        objectArray = objectArray2;
        ringBuffer.array = objectArray;
    }

    public static final /* synthetic */ int access$getSize$p(RingBuffer $this) {
        return $this.size;
    }

    public static final /* synthetic */ void access$setSize$p(RingBuffer $this, int n) {
        $this.size = n;
    }
}

