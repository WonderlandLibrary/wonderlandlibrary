/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.collections.AbstractIterator;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.ranges.RangesKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000>\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010(\n\u0002\b\b\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B\u000f\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007B\u001d\u0012\u000e\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\t\u0012\u0006\u0010\u000b\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\fJ\u0013\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0016J\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0006\u0010\u0018\u001a\u00020\u0006J\u0016\u0010\u0019\u001a\u00028\u00002\u0006\u0010\u001a\u001a\u00020\u0006H\u0096\u0002\u00a2\u0006\u0002\u0010\u001bJ\u0006\u0010\u001c\u001a\u00020\u001dJ\u000f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00028\u00000\u001fH\u0096\u0002J\u000e\u0010 \u001a\u00020\u00142\u0006\u0010!\u001a\u00020\u0006J\u0015\u0010\"\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tH\u0014\u00a2\u0006\u0002\u0010#J'\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00010\t\"\u0004\b\u0001\u0010\u00012\f\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00010\tH\u0014\u00a2\u0006\u0002\u0010%J\u0015\u0010&\u001a\u00020\u0006*\u00020\u00062\u0006\u0010!\u001a\u00020\u0006H\u0082\bR\u0018\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\rR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0006@RX\u0096\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006'"}, d2={"Lkotlin/collections/RingBuffer;", "T", "Lkotlin/collections/AbstractList;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "capacity", "", "(I)V", "buffer", "", "", "filledSize", "([Ljava/lang/Object;I)V", "[Ljava/lang/Object;", "<set-?>", "size", "getSize", "()I", "startIndex", "add", "", "element", "(Ljava/lang/Object;)V", "expanded", "maxCapacity", "get", "index", "(I)Ljava/lang/Object;", "isFull", "", "iterator", "", "removeFirst", "n", "toArray", "()[Ljava/lang/Object;", "array", "([Ljava/lang/Object;)[Ljava/lang/Object;", "forward", "kotlin-stdlib"})
final class RingBuffer<T>
extends AbstractList<T>
implements RandomAccess {
    @NotNull
    private final Object[] buffer;
    private final int capacity;
    private int startIndex;
    private int size;

    public RingBuffer(@NotNull Object[] buffer, int filledSize) {
        boolean bl;
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        this.buffer = buffer;
        boolean bl2 = bl = filledSize >= 0;
        if (!bl) {
            boolean $i$a$-require-RingBuffer$32 = false;
            String $i$a$-require-RingBuffer$32 = Intrinsics.stringPlus("ring buffer filled size should not be negative but it is ", filledSize);
            throw new IllegalArgumentException($i$a$-require-RingBuffer$32.toString());
        }
        boolean bl3 = bl = filledSize <= this.buffer.length;
        if (!bl) {
            boolean bl4 = false;
            String string = "ring buffer filled size: " + filledSize + " cannot be larger than the buffer size: " + this.buffer.length;
            throw new IllegalArgumentException(string.toString());
        }
        this.capacity = this.buffer.length;
        this.size = filledSize;
    }

    public RingBuffer(int capacity) {
        this(new Object[capacity], 0);
    }

    @Override
    public int getSize() {
        return this.size;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public T get(int index) {
        void this_$iv;
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(index, this.size());
        RingBuffer ringBuffer = this;
        int $this$forward$iv = this.startIndex;
        boolean $i$f$forward = false;
        return (T)this.buffer[($this$forward$iv + index) % ((RingBuffer)this_$iv).capacity];
    }

    public final boolean isFull() {
        return this.size() == this.capacity;
    }

    @Override
    @NotNull
    public Iterator<T> iterator() {
        return new AbstractIterator<T>(this){
            private int count;
            private int index;
            final /* synthetic */ RingBuffer<T> this$0;
            {
                this.this$0 = $receiver;
                this.count = this.this$0.size();
                this.index = RingBuffer.access$getStartIndex$p(this.this$0);
            }

            /*
             * WARNING - void declaration
             */
            protected void computeNext() {
                if (this.count == 0) {
                    this.done();
                } else {
                    void this_$iv;
                    void $this$forward$iv;
                    this.setNext(RingBuffer.access$getBuffer$p(this.this$0)[this.index]);
                    KMappedMarker kMappedMarker = this.this$0;
                    int n = this.index;
                    boolean n$iv = true;
                    boolean $i$f$forward = false;
                    this.index = ($this$forward$iv + n$iv) % RingBuffer.access$getCapacity$p((RingBuffer)this_$iv);
                    kMappedMarker = this;
                    n = kMappedMarker.count;
                    kMappedMarker.count = n + -1;
                }
            }
        };
    }

    @Override
    @NotNull
    public <T> T[] toArray(@NotNull T[] array) {
        int n;
        T[] TArray;
        Intrinsics.checkNotNullParameter(array, "array");
        if (array.length < this.size()) {
            T[] TArray2 = Arrays.copyOf(array, this.size());
            Intrinsics.checkNotNullExpressionValue(TArray2, "copyOf(this, newSize)");
            TArray = TArray2;
        } else {
            TArray = array;
        }
        T[] result = TArray;
        int size = this.size();
        int widx = 0;
        int idx = this.startIndex;
        while (widx < size && idx < this.capacity) {
            result[widx] = this.buffer[idx];
            n = widx;
            widx = n + 1;
            n = idx;
            idx = n + 1;
        }
        idx = 0;
        while (widx < size) {
            result[widx] = this.buffer[idx];
            n = widx;
            widx = n + 1;
            n = idx;
            idx = n + 1;
        }
        if (result.length > this.size()) {
            result[this.size()] = null;
        }
        return result;
    }

    @Override
    @NotNull
    public Object[] toArray() {
        return this.toArray(new Object[this.size()]);
    }

    @NotNull
    public final RingBuffer<T> expanded(int maxCapacity) {
        Object[] objectArray;
        int newCapacity = RangesKt.coerceAtMost(this.capacity + (this.capacity >> 1) + 1, maxCapacity);
        if (this.startIndex == 0) {
            Object[] objectArray2 = this.buffer;
            Object[] objectArray3 = Arrays.copyOf(objectArray2, newCapacity);
            Intrinsics.checkNotNullExpressionValue(objectArray3, "copyOf(this, newSize)");
            objectArray = objectArray3;
        } else {
            objectArray = this.toArray(new Object[newCapacity]);
        }
        Object[] newBuffer = objectArray;
        return new RingBuffer<T>(newBuffer, this.size());
    }

    public final void add(T element) {
        if (this.isFull()) {
            throw new IllegalStateException("ring buffer is full");
        }
        RingBuffer ringBuffer = this;
        int n = this.startIndex;
        int n$iv = this.size();
        boolean $i$f$forward = false;
        this.buffer[($this$forward$iv + n$iv) % ((RingBuffer)this_$iv).capacity] = element;
        ringBuffer = this;
        n = ringBuffer.size();
        ringBuffer.size = n + 1;
    }

    /*
     * WARNING - void declaration
     */
    public final void removeFirst(int n) {
        boolean bl;
        boolean bl2 = bl = n >= 0;
        if (!bl) {
            boolean $i$a$-require-RingBuffer$removeFirst$32 = false;
            String $i$a$-require-RingBuffer$removeFirst$32 = Intrinsics.stringPlus("n shouldn't be negative but it is ", n);
            throw new IllegalArgumentException($i$a$-require-RingBuffer$removeFirst$32.toString());
        }
        boolean bl3 = bl = n <= this.size();
        if (!bl) {
            boolean $i$a$-require-RingBuffer$removeFirst$42 = false;
            String $i$a$-require-RingBuffer$removeFirst$42 = "n shouldn't be greater than the buffer size: n = " + n + ", size = " + this.size();
            throw new IllegalArgumentException($i$a$-require-RingBuffer$removeFirst$42.toString());
        }
        if (n > 0) {
            void this_$iv;
            int start = this.startIndex;
            RingBuffer ringBuffer = this;
            int $this$forward$iv = start;
            boolean $i$f$forward = false;
            int end = ($this$forward$iv + n) % ((RingBuffer)this_$iv).capacity;
            if (start > end) {
                ArraysKt.fill(this.buffer, null, start, this.capacity);
                ArraysKt.fill(this.buffer, null, 0, end);
            } else {
                ArraysKt.fill(this.buffer, null, start, end);
            }
            this.startIndex = end;
            ringBuffer = this;
            ringBuffer.size = ringBuffer.size() - n;
        }
    }

    private final int forward(int $this$forward, int n) {
        boolean $i$f$forward = false;
        return ($this$forward + n) % this.capacity;
    }

    public static final /* synthetic */ int access$getStartIndex$p(RingBuffer $this) {
        return $this.startIndex;
    }

    public static final /* synthetic */ Object[] access$getBuffer$p(RingBuffer $this) {
        return $this.buffer;
    }
}

