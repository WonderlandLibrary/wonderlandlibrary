/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.libs.fastutil.objects.ObjectBigArrays
 *  com.viaversion.viaversion.libs.fastutil.objects.ObjectIterators$UnmodifiableBidirectionalIterator
 *  com.viaversion.viaversion.libs.fastutil.objects.ObjectIterators$UnmodifiableIterator
 *  com.viaversion.viaversion.libs.fastutil.objects.ObjectIterators$UnmodifiableListIterator
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.BigArrays;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrayList;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectBidirectionalIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectBigArrays;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectCollection;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectIterators;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectList;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectListIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class ObjectIterators {
    public static final EmptyIterator EMPTY_ITERATOR = new EmptyIterator();

    private ObjectIterators() {
    }

    public static <K> ObjectIterator<K> emptyIterator() {
        return EMPTY_ITERATOR;
    }

    public static <K> ObjectListIterator<K> singleton(K element) {
        return new SingletonIterator<K>(element);
    }

    public static <K> ObjectListIterator<K> wrap(K[] array, int offset, int length) {
        ObjectArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator<K>(array, offset, length);
    }

    public static <K> ObjectListIterator<K> wrap(K[] array) {
        return new ArrayIterator<K>(array, 0, array.length);
    }

    public static <K> int unwrap(Iterator<? extends K> i2, K[] array, int offset, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j2 = max;
        while (j2-- != 0 && i2.hasNext()) {
            array[offset++] = i2.next();
        }
        return max - j2 - 1;
    }

    public static <K> int unwrap(Iterator<? extends K> i2, K[] array) {
        return ObjectIterators.unwrap(i2, array, 0, array.length);
    }

    public static <K> K[] unwrap(Iterator<? extends K> i2, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        Object[] array = new Object[16];
        int j2 = 0;
        while (max-- != 0 && i2.hasNext()) {
            if (j2 == array.length) {
                array = ObjectArrays.grow(array, j2 + 1);
            }
            array[j2++] = i2.next();
        }
        return ObjectArrays.trim(array, j2);
    }

    public static <K> K[] unwrap(Iterator<? extends K> i2) {
        return ObjectIterators.unwrap(i2, Integer.MAX_VALUE);
    }

    public static <K> long unwrap(Iterator<? extends K> i2, K[][] array, long offset, long max) {
        if (max < 0L) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0L || offset + max > BigArrays.length(array)) {
            throw new IllegalArgumentException();
        }
        long j2 = max;
        while (j2-- != 0L && i2.hasNext()) {
            BigArrays.set(array, offset++, i2.next());
        }
        return max - j2 - 1L;
    }

    public static <K> long unwrap(Iterator<? extends K> i2, K[][] array) {
        return ObjectIterators.unwrap(i2, array, 0L, BigArrays.length(array));
    }

    public static <K> int unwrap(Iterator<K> i2, ObjectCollection<? super K> c2, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j2 = max;
        while (j2-- != 0 && i2.hasNext()) {
            c2.add(i2.next());
        }
        return max - j2 - 1;
    }

    public static <K> K[][] unwrapBig(Iterator<? extends K> i2, long max) {
        if (max < 0L) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        Object[][] array = ObjectBigArrays.newBigArray((long)16L);
        long j2 = 0L;
        while (max-- != 0L && i2.hasNext()) {
            if (j2 == BigArrays.length(array)) {
                array = BigArrays.grow(array, j2 + 1L);
            }
            BigArrays.set(array, j2++, i2.next());
        }
        return BigArrays.trim(array, j2);
    }

    public static <K> K[][] unwrapBig(Iterator<? extends K> i2) {
        return ObjectIterators.unwrapBig(i2, Long.MAX_VALUE);
    }

    public static <K> long unwrap(Iterator<K> i2, ObjectCollection<? super K> c2) {
        long n2 = 0L;
        while (i2.hasNext()) {
            c2.add(i2.next());
            ++n2;
        }
        return n2;
    }

    public static <K> int pour(Iterator<K> i2, ObjectCollection<? super K> s2, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j2 = max;
        while (j2-- != 0 && i2.hasNext()) {
            s2.add(i2.next());
        }
        return max - j2 - 1;
    }

    public static <K> int pour(Iterator<K> i2, ObjectCollection<? super K> s2) {
        return ObjectIterators.pour(i2, s2, Integer.MAX_VALUE);
    }

    public static <K> ObjectList<K> pour(Iterator<K> i2, int max) {
        ObjectArrayList l2 = new ObjectArrayList();
        ObjectIterators.pour(i2, l2, max);
        l2.trim();
        return l2;
    }

    public static <K> ObjectList<K> pour(Iterator<K> i2) {
        return ObjectIterators.pour(i2, Integer.MAX_VALUE);
    }

    public static <K> ObjectIterator<K> asObjectIterator(Iterator<K> i2) {
        if (i2 instanceof ObjectIterator) {
            return (ObjectIterator)i2;
        }
        return new IteratorWrapper<K>(i2);
    }

    public static <K> ObjectListIterator<K> asObjectIterator(ListIterator<K> i2) {
        if (i2 instanceof ObjectListIterator) {
            return (ObjectListIterator)i2;
        }
        return new ListIteratorWrapper<K>(i2);
    }

    public static <K> boolean any(Iterator<K> iterator, Predicate<? super K> predicate) {
        return ObjectIterators.indexOf(iterator, predicate) != -1;
    }

    public static <K> boolean all(Iterator<K> iterator, Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate);
        do {
            if (iterator.hasNext()) continue;
            return true;
        } while (predicate.test(iterator.next()));
        return false;
    }

    public static <K> int indexOf(Iterator<K> iterator, Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate);
        int i2 = 0;
        while (iterator.hasNext()) {
            if (predicate.test(iterator.next())) {
                return i2;
            }
            ++i2;
        }
        return -1;
    }

    @SafeVarargs
    public static <K> ObjectIterator<K> concat(ObjectIterator<? extends K> ... a2) {
        return ObjectIterators.concat(a2, 0, a2.length);
    }

    public static <K> ObjectIterator<K> concat(ObjectIterator<? extends K>[] a2, int offset, int length) {
        return new IteratorConcatenator<K>(a2, offset, length);
    }

    public static <K> ObjectIterator<K> unmodifiable(ObjectIterator<? extends K> i2) {
        return new UnmodifiableIterator(i2);
    }

    public static <K> ObjectBidirectionalIterator<K> unmodifiable(ObjectBidirectionalIterator<? extends K> i2) {
        return new UnmodifiableBidirectionalIterator(i2);
    }

    public static <K> ObjectListIterator<K> unmodifiable(ObjectListIterator<? extends K> i2) {
        return new UnmodifiableListIterator(i2);
    }

    public static class EmptyIterator<K>
    implements ObjectListIterator<K>,
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
        public K next() {
            throw new NoSuchElementException();
        }

        @Override
        public K previous() {
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
        public void forEachRemaining(Consumer<? super K> action) {
        }

        public Object clone() {
            return EMPTY_ITERATOR;
        }

        private Object readResolve() {
            return EMPTY_ITERATOR;
        }
    }

    private static class SingletonIterator<K>
    implements ObjectListIterator<K> {
        private final K element;
        private byte curr;

        public SingletonIterator(K element) {
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
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }

        @Override
        public K previous() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = 0;
            return this.element;
        }

        @Override
        public void forEachRemaining(Consumer<? super K> action) {
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

    private static class ArrayIterator<K>
    implements ObjectListIterator<K> {
        private final K[] array;
        private final int offset;
        private final int length;
        private int curr;

        public ArrayIterator(K[] array, int offset, int length) {
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
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }

        @Override
        public K previous() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + --this.curr];
        }

        @Override
        public void forEachRemaining(Consumer<? super K> action) {
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

    private static class IteratorWrapper<K>
    implements ObjectIterator<K> {
        final Iterator<K> i;

        public IteratorWrapper(Iterator<K> i2) {
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
        public K next() {
            return this.i.next();
        }

        @Override
        public void forEachRemaining(Consumer<? super K> action) {
            this.i.forEachRemaining(action);
        }
    }

    private static class ListIteratorWrapper<K>
    implements ObjectListIterator<K> {
        final ListIterator<K> i;

        public ListIteratorWrapper(ListIterator<K> i2) {
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
        public void set(K k2) {
            this.i.set(k2);
        }

        @Override
        public void add(K k2) {
            this.i.add(k2);
        }

        @Override
        public void remove() {
            this.i.remove();
        }

        @Override
        public K next() {
            return this.i.next();
        }

        @Override
        public K previous() {
            return this.i.previous();
        }

        @Override
        public void forEachRemaining(Consumer<? super K> action) {
            this.i.forEachRemaining(action);
        }
    }

    private static class IteratorConcatenator<K>
    implements ObjectIterator<K> {
        final ObjectIterator<? extends K>[] a;
        int offset;
        int length;
        int lastOffset = -1;

        public IteratorConcatenator(ObjectIterator<? extends K>[] a2, int offset, int length) {
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
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.lastOffset = this.offset;
            Object next = this.a[this.lastOffset].next();
            this.advance();
            return (K)next;
        }

        @Override
        public void forEachRemaining(Consumer<? super K> action) {
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

    public static abstract class AbstractIndexBasedListIterator<K>
    extends AbstractIndexBasedIterator<K>
    implements ObjectListIterator<K> {
        protected AbstractIndexBasedListIterator(int minPos, int initialPos) {
            super(minPos, initialPos);
        }

        protected abstract void add(int var1, K var2);

        protected abstract void set(int var1, K var2);

        @Override
        public boolean hasPrevious() {
            return this.pos > this.minPos;
        }

        @Override
        public K previous() {
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
        public void add(K k2) {
            this.add(this.pos++, k2);
            this.lastReturned = -1;
        }

        @Override
        public void set(K k2) {
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

    public static abstract class AbstractIndexBasedIterator<K>
    extends AbstractObjectIterator<K> {
        protected final int minPos;
        protected int pos;
        protected int lastReturned;

        protected AbstractIndexBasedIterator(int minPos, int initialPos) {
            this.minPos = minPos;
            this.pos = initialPos;
        }

        protected abstract K get(int var1);

        protected abstract void remove(int var1);

        protected abstract int getMaxPos();

        @Override
        public boolean hasNext() {
            return this.pos < this.getMaxPos();
        }

        @Override
        public K next() {
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
        public void forEachRemaining(Consumer<? super K> action) {
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

