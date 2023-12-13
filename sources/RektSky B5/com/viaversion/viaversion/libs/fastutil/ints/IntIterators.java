/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.libs.fastutil.bytes.ByteIterator
 *  com.viaversion.viaversion.libs.fastutil.chars.CharIterator
 *  com.viaversion.viaversion.libs.fastutil.ints.IntBigArrays
 *  com.viaversion.viaversion.libs.fastutil.ints.IntIterators$ByteIteratorWrapper
 *  com.viaversion.viaversion.libs.fastutil.ints.IntIterators$CharIteratorWrapper
 *  com.viaversion.viaversion.libs.fastutil.ints.IntIterators$ShortIteratorWrapper
 *  com.viaversion.viaversion.libs.fastutil.ints.IntIterators$UnmodifiableBidirectionalIterator
 *  com.viaversion.viaversion.libs.fastutil.ints.IntIterators$UnmodifiableIterator
 *  com.viaversion.viaversion.libs.fastutil.ints.IntIterators$UnmodifiableListIterator
 *  com.viaversion.viaversion.libs.fastutil.shorts.ShortIterator
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.BigArrays;
import com.viaversion.viaversion.libs.fastutil.bytes.ByteIterator;
import com.viaversion.viaversion.libs.fastutil.chars.CharIterator;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractIntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrayList;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntBidirectionalIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntBigArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntCollection;
import com.viaversion.viaversion.libs.fastutil.ints.IntConsumer;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterator;
import com.viaversion.viaversion.libs.fastutil.ints.IntIterators;
import com.viaversion.viaversion.libs.fastutil.ints.IntList;
import com.viaversion.viaversion.libs.fastutil.ints.IntListIterator;
import com.viaversion.viaversion.libs.fastutil.shorts.ShortIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.IntPredicate;

public final class IntIterators {
    public static final EmptyIterator EMPTY_ITERATOR = new EmptyIterator();

    private IntIterators() {
    }

    public static IntListIterator singleton(int element) {
        return new SingletonIterator(element);
    }

    public static IntListIterator wrap(int[] array, int offset, int length) {
        IntArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }

    public static IntListIterator wrap(int[] array) {
        return new ArrayIterator(array, 0, array.length);
    }

    public static int unwrap(IntIterator i2, int[] array, int offset, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j2 = max;
        while (j2-- != 0 && i2.hasNext()) {
            array[offset++] = i2.nextInt();
        }
        return max - j2 - 1;
    }

    public static int unwrap(IntIterator i2, int[] array) {
        return IntIterators.unwrap(i2, array, 0, array.length);
    }

    public static int[] unwrap(IntIterator i2, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int[] array = new int[16];
        int j2 = 0;
        while (max-- != 0 && i2.hasNext()) {
            if (j2 == array.length) {
                array = IntArrays.grow(array, j2 + 1);
            }
            array[j2++] = i2.nextInt();
        }
        return IntArrays.trim(array, j2);
    }

    public static int[] unwrap(IntIterator i2) {
        return IntIterators.unwrap(i2, Integer.MAX_VALUE);
    }

    public static long unwrap(IntIterator i2, int[][] array, long offset, long max) {
        if (max < 0L) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0L || offset + max > BigArrays.length(array)) {
            throw new IllegalArgumentException();
        }
        long j2 = max;
        while (j2-- != 0L && i2.hasNext()) {
            BigArrays.set(array, offset++, i2.nextInt());
        }
        return max - j2 - 1L;
    }

    public static long unwrap(IntIterator i2, int[][] array) {
        return IntIterators.unwrap(i2, array, 0L, BigArrays.length(array));
    }

    public static int unwrap(IntIterator i2, IntCollection c2, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j2 = max;
        while (j2-- != 0 && i2.hasNext()) {
            c2.add(i2.nextInt());
        }
        return max - j2 - 1;
    }

    public static int[][] unwrapBig(IntIterator i2, long max) {
        if (max < 0L) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int[][] array = IntBigArrays.newBigArray((long)16L);
        long j2 = 0L;
        while (max-- != 0L && i2.hasNext()) {
            if (j2 == BigArrays.length(array)) {
                array = BigArrays.grow(array, j2 + 1L);
            }
            BigArrays.set(array, j2++, i2.nextInt());
        }
        return BigArrays.trim(array, j2);
    }

    public static int[][] unwrapBig(IntIterator i2) {
        return IntIterators.unwrapBig(i2, Long.MAX_VALUE);
    }

    public static long unwrap(IntIterator i2, IntCollection c2) {
        long n2 = 0L;
        while (i2.hasNext()) {
            c2.add(i2.nextInt());
            ++n2;
        }
        return n2;
    }

    public static int pour(IntIterator i2, IntCollection s2, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j2 = max;
        while (j2-- != 0 && i2.hasNext()) {
            s2.add(i2.nextInt());
        }
        return max - j2 - 1;
    }

    public static int pour(IntIterator i2, IntCollection s2) {
        return IntIterators.pour(i2, s2, Integer.MAX_VALUE);
    }

    public static IntList pour(IntIterator i2, int max) {
        IntArrayList l2 = new IntArrayList();
        IntIterators.pour(i2, l2, max);
        l2.trim();
        return l2;
    }

    public static IntList pour(IntIterator i2) {
        return IntIterators.pour(i2, Integer.MAX_VALUE);
    }

    public static IntIterator asIntIterator(Iterator i2) {
        if (i2 instanceof IntIterator) {
            return (IntIterator)i2;
        }
        if (i2 instanceof PrimitiveIterator.OfInt) {
            return new PrimitiveIteratorWrapper((PrimitiveIterator.OfInt)i2);
        }
        return new IteratorWrapper(i2);
    }

    public static IntListIterator asIntIterator(ListIterator i2) {
        if (i2 instanceof IntListIterator) {
            return (IntListIterator)i2;
        }
        return new ListIteratorWrapper(i2);
    }

    public static boolean any(IntIterator iterator, IntPredicate predicate) {
        return IntIterators.indexOf(iterator, predicate) != -1;
    }

    public static boolean all(IntIterator iterator, IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        do {
            if (iterator.hasNext()) continue;
            return true;
        } while (predicate.test(iterator.nextInt()));
        return false;
    }

    public static int indexOf(IntIterator iterator, IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        int i2 = 0;
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextInt())) {
                return i2;
            }
            ++i2;
        }
        return -1;
    }

    public static IntListIterator fromTo(int from, int to) {
        return new IntervalIterator(from, to);
    }

    public static IntIterator concat(IntIterator ... a2) {
        return IntIterators.concat(a2, 0, a2.length);
    }

    public static IntIterator concat(IntIterator[] a2, int offset, int length) {
        return new IteratorConcatenator(a2, offset, length);
    }

    public static IntIterator unmodifiable(IntIterator i2) {
        return new UnmodifiableIterator(i2);
    }

    public static IntBidirectionalIterator unmodifiable(IntBidirectionalIterator i2) {
        return new UnmodifiableBidirectionalIterator(i2);
    }

    public static IntListIterator unmodifiable(IntListIterator i2) {
        return new UnmodifiableListIterator(i2);
    }

    public static IntIterator wrap(ByteIterator iterator) {
        return new ByteIteratorWrapper(iterator);
    }

    public static IntIterator wrap(ShortIterator iterator) {
        return new ShortIteratorWrapper(iterator);
    }

    public static IntIterator wrap(CharIterator iterator) {
        return new CharIteratorWrapper(iterator);
    }

    private static class SingletonIterator
    implements IntListIterator {
        private final int element;
        private byte curr;

        public SingletonIterator(int element) {
            this.element = element;
        }

        @Override
        public boolean hasNext() {
            return this.curr == 0;
        }

        @Override
        public boolean hasPrevious() {
            return this.curr == 1;
        }

        @Override
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }

        @Override
        public int previousInt() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = 0;
            return this.element;
        }

        @Override
        public void forEachRemaining(java.util.function.IntConsumer action) {
            Objects.requireNonNull(action);
            if (this.curr == 0) {
                action.accept(this.element);
                this.curr = 1;
            }
        }

        @Override
        public int nextIndex() {
            return this.curr;
        }

        @Override
        public int previousIndex() {
            return this.curr - 1;
        }

        @Override
        public int back(int n2) {
            if (n2 < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
            }
            if (n2 == 0 || this.curr < 1) {
                return 0;
            }
            this.curr = 1;
            return 1;
        }

        @Override
        public int skip(int n2) {
            if (n2 < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
            }
            if (n2 == 0 || this.curr > 0) {
                return 0;
            }
            this.curr = 0;
            return 1;
        }
    }

    private static class ArrayIterator
    implements IntListIterator {
        private final int[] array;
        private final int offset;
        private final int length;
        private int curr;

        public ArrayIterator(int[] array, int offset, int length) {
            this.array = array;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public boolean hasNext() {
            return this.curr < this.length;
        }

        @Override
        public boolean hasPrevious() {
            return this.curr > 0;
        }

        @Override
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }

        @Override
        public int previousInt() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + --this.curr];
        }

        @Override
        public void forEachRemaining(java.util.function.IntConsumer action) {
            Objects.requireNonNull(action);
            while (this.curr < this.length) {
                action.accept(this.array[this.offset + this.curr]);
                ++this.curr;
            }
        }

        @Override
        public int skip(int n2) {
            if (n2 < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
            }
            if (n2 <= this.length - this.curr) {
                this.curr += n2;
                return n2;
            }
            n2 = this.length - this.curr;
            this.curr = this.length;
            return n2;
        }

        @Override
        public int back(int n2) {
            if (n2 < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
            }
            if (n2 <= this.curr) {
                this.curr -= n2;
                return n2;
            }
            n2 = this.curr;
            this.curr = 0;
            return n2;
        }

        @Override
        public int nextIndex() {
            return this.curr;
        }

        @Override
        public int previousIndex() {
            return this.curr - 1;
        }
    }

    private static class PrimitiveIteratorWrapper
    implements IntIterator {
        final PrimitiveIterator.OfInt i;

        public PrimitiveIteratorWrapper(PrimitiveIterator.OfInt i2) {
            this.i = i2;
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override
        public void remove() {
            this.i.remove();
        }

        @Override
        public int nextInt() {
            return this.i.nextInt();
        }

        @Override
        public void forEachRemaining(java.util.function.IntConsumer action) {
            this.i.forEachRemaining(action);
        }
    }

    private static class IteratorWrapper
    implements IntIterator {
        final Iterator<Integer> i;

        public IteratorWrapper(Iterator<Integer> i2) {
            this.i = i2;
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override
        public void remove() {
            this.i.remove();
        }

        @Override
        public int nextInt() {
            return this.i.next();
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            this.i.forEachRemaining(action);
        }

        @Override
        public void forEachRemaining(java.util.function.IntConsumer action) {
            Objects.requireNonNull(action);
            this.i.forEachRemaining(action instanceof Consumer ? (Consumer<Integer>)((Object)action) : action::accept);
        }

        @Override
        @Deprecated
        public void forEachRemaining(Consumer<? super Integer> action) {
            this.i.forEachRemaining(action);
        }
    }

    private static class ListIteratorWrapper
    implements IntListIterator {
        final ListIterator<Integer> i;

        public ListIteratorWrapper(ListIterator<Integer> i2) {
            this.i = i2;
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }

        @Override
        public int nextIndex() {
            return this.i.nextIndex();
        }

        @Override
        public int previousIndex() {
            return this.i.previousIndex();
        }

        @Override
        public void set(int k2) {
            this.i.set(k2);
        }

        @Override
        public void add(int k2) {
            this.i.add(k2);
        }

        @Override
        public void remove() {
            this.i.remove();
        }

        @Override
        public int nextInt() {
            return this.i.next();
        }

        @Override
        public int previousInt() {
            return this.i.previous();
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            this.i.forEachRemaining(action);
        }

        @Override
        public void forEachRemaining(java.util.function.IntConsumer action) {
            Objects.requireNonNull(action);
            this.i.forEachRemaining(action instanceof Consumer ? (Consumer<Integer>)((Object)action) : action::accept);
        }

        @Override
        @Deprecated
        public void forEachRemaining(Consumer<? super Integer> action) {
            this.i.forEachRemaining(action);
        }
    }

    private static class IntervalIterator
    implements IntListIterator {
        private final int from;
        private final int to;
        int curr;

        public IntervalIterator(int from, int to) {
            this.from = this.curr = from;
            this.to = to;
        }

        @Override
        public boolean hasNext() {
            return this.curr < this.to;
        }

        @Override
        public boolean hasPrevious() {
            return this.curr > this.from;
        }

        @Override
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.curr++;
        }

        @Override
        public int previousInt() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            return --this.curr;
        }

        @Override
        public void forEachRemaining(java.util.function.IntConsumer action) {
            Objects.requireNonNull(action);
            while (this.curr < this.to) {
                action.accept(this.curr);
                ++this.curr;
            }
        }

        @Override
        public int nextIndex() {
            return this.curr - this.from;
        }

        @Override
        public int previousIndex() {
            return this.curr - this.from - 1;
        }

        @Override
        public int skip(int n2) {
            if (n2 < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
            }
            if (this.curr + n2 <= this.to) {
                this.curr += n2;
                return n2;
            }
            n2 = this.to - this.curr;
            this.curr = this.to;
            return n2;
        }

        @Override
        public int back(int n2) {
            if (this.curr - n2 >= this.from) {
                this.curr -= n2;
                return n2;
            }
            n2 = this.curr - this.from;
            this.curr = this.from;
            return n2;
        }
    }

    private static class IteratorConcatenator
    implements IntIterator {
        final IntIterator[] a;
        int offset;
        int length;
        int lastOffset = -1;

        public IteratorConcatenator(IntIterator[] a2, int offset, int length) {
            this.a = a2;
            this.offset = offset;
            this.length = length;
            this.advance();
        }

        private void advance() {
            while (this.length != 0 && !this.a[this.offset].hasNext()) {
                --this.length;
                ++this.offset;
            }
        }

        @Override
        public boolean hasNext() {
            return this.length > 0;
        }

        @Override
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.lastOffset = this.offset;
            int next = this.a[this.lastOffset].nextInt();
            this.advance();
            return next;
        }

        @Override
        public void forEachRemaining(java.util.function.IntConsumer action) {
            while (this.length > 0) {
                this.lastOffset = this.offset;
                this.a[this.lastOffset].forEachRemaining(action);
                this.advance();
            }
        }

        @Override
        @Deprecated
        public void forEachRemaining(Consumer<? super Integer> action) {
            while (this.length > 0) {
                this.lastOffset = this.offset;
                this.a[this.lastOffset].forEachRemaining(action);
                this.advance();
            }
        }

        @Override
        public void remove() {
            if (this.lastOffset == -1) {
                throw new IllegalStateException();
            }
            this.a[this.lastOffset].remove();
        }

        @Override
        public int skip(int n2) {
            if (n2 < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
            }
            this.lastOffset = -1;
            int skipped = 0;
            while (skipped < n2 && this.length != 0) {
                skipped += this.a[this.offset].skip(n2 - skipped);
                if (this.a[this.offset].hasNext()) break;
                --this.length;
                ++this.offset;
            }
            return skipped;
        }
    }

    public static class EmptyIterator
    implements IntListIterator,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyIterator() {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public int nextInt() {
            throw new NoSuchElementException();
        }

        @Override
        public int previousInt() {
            throw new NoSuchElementException();
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return -1;
        }

        @Override
        public int skip(int n2) {
            return 0;
        }

        @Override
        public int back(int n2) {
            return 0;
        }

        @Override
        public void forEachRemaining(java.util.function.IntConsumer action) {
        }

        @Override
        @Deprecated
        public void forEachRemaining(Consumer<? super Integer> action) {
        }

        public Object clone() {
            return EMPTY_ITERATOR;
        }

        private Object readResolve() {
            return EMPTY_ITERATOR;
        }
    }

    public static abstract class AbstractIndexBasedListIterator
    extends AbstractIndexBasedIterator
    implements IntListIterator {
        protected AbstractIndexBasedListIterator(int minPos, int initialPos) {
            super(minPos, initialPos);
        }

        protected abstract void add(int var1, int var2);

        protected abstract void set(int var1, int var2);

        @Override
        public boolean hasPrevious() {
            return this.pos > this.minPos;
        }

        @Override
        public int previousInt() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.lastReturned = --this.pos;
            return this.get(this.pos);
        }

        @Override
        public int nextIndex() {
            return this.pos;
        }

        @Override
        public int previousIndex() {
            return this.pos - 1;
        }

        @Override
        public void add(int k2) {
            this.add(this.pos++, k2);
            this.lastReturned = -1;
        }

        @Override
        public void set(int k2) {
            if (this.lastReturned == -1) {
                throw new IllegalStateException();
            }
            this.set(this.lastReturned, k2);
        }

        @Override
        public int back(int n2) {
            if (n2 < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
            }
            int remaining = this.pos - this.minPos;
            if (n2 < remaining) {
                this.pos -= n2;
            } else {
                n2 = remaining;
                this.pos = this.minPos;
            }
            this.lastReturned = this.pos;
            return n2;
        }
    }

    public static abstract class AbstractIndexBasedIterator
    extends AbstractIntIterator {
        protected final int minPos;
        protected int pos;
        protected int lastReturned;

        protected AbstractIndexBasedIterator(int minPos, int initialPos) {
            this.minPos = minPos;
            this.pos = initialPos;
        }

        protected abstract int get(int var1);

        protected abstract void remove(int var1);

        protected abstract int getMaxPos();

        @Override
        public boolean hasNext() {
            return this.pos < this.getMaxPos();
        }

        @Override
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.pos++;
            return this.get(this.lastReturned);
        }

        @Override
        public void remove() {
            if (this.lastReturned == -1) {
                throw new IllegalStateException();
            }
            this.remove(this.lastReturned);
            if (this.lastReturned < this.pos) {
                --this.pos;
            }
            this.lastReturned = -1;
        }

        @Override
        public void forEachRemaining(java.util.function.IntConsumer action) {
            while (this.pos < this.getMaxPos()) {
                ++this.pos;
                this.lastReturned = this.lastReturned;
                action.accept(this.get(this.lastReturned));
            }
        }

        @Override
        public int skip(int n2) {
            if (n2 < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n2);
            }
            int max = this.getMaxPos();
            int remaining = max - this.pos;
            if (n2 < remaining) {
                this.pos += n2;
            } else {
                n2 = remaining;
                this.pos = max;
            }
            this.lastReturned = this.pos - 1;
            return n2;
        }
    }
}

