// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.ListIterator;
import com.viaversion.viaversion.libs.fastutil.BigArrays;
import java.util.Iterator;

public final class ObjectIterators
{
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private ObjectIterators() {
    }
    
    public static <K> ObjectIterator<K> emptyIterator() {
        return (ObjectIterator<K>)ObjectIterators.EMPTY_ITERATOR;
    }
    
    public static <K> ObjectListIterator<K> singleton(final K element) {
        return new SingletonIterator<K>(element);
    }
    
    public static <K> ObjectListIterator<K> wrap(final K[] array, final int offset, final int length) {
        ObjectArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator<K>(array, offset, length);
    }
    
    public static <K> ObjectListIterator<K> wrap(final K[] array) {
        return new ArrayIterator<K>(array, 0, array.length);
    }
    
    public static <K> int unwrap(final Iterator<? extends K> i, final K[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = (K)i.next();
        }
        return max - j - 1;
    }
    
    public static <K> int unwrap(final Iterator<? extends K> i, final K[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static <K> K[] unwrap(final Iterator<? extends K> i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        K[] array = (K[])new Object[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = ObjectArrays.grow(array, j + 1);
            }
            array[j++] = (K)i.next();
        }
        return ObjectArrays.trim(array, j);
    }
    
    public static <K> K[] unwrap(final Iterator<? extends K> i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static <K> long unwrap(final Iterator<? extends K> i, final K[][] array, long offset, final long max) {
        if (max < 0L) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0L || offset + max > BigArrays.length(array)) {
            throw new IllegalArgumentException();
        }
        long j = max;
        while (j-- != 0L && i.hasNext()) {
            BigArrays.set(array, offset++, (K)i.next());
        }
        return max - j - 1L;
    }
    
    public static <K> long unwrap(final Iterator<? extends K> i, final K[][] array) {
        return unwrap(i, array, 0L, BigArrays.length(array));
    }
    
    public static <K> int unwrap(final Iterator<K> i, final ObjectCollection<? super K> c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add((Object)i.next());
        }
        return max - j - 1;
    }
    
    public static <K> K[][] unwrapBig(final Iterator<? extends K> i, long max) {
        if (max < 0L) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        K[][] array = (K[][])ObjectBigArrays.newBigArray(16L);
        long j = 0L;
        while (max-- != 0L && i.hasNext()) {
            if (j == BigArrays.length(array)) {
                array = BigArrays.grow(array, j + 1L);
            }
            BigArrays.set(array, j++, (K)i.next());
        }
        return BigArrays.trim(array, j);
    }
    
    public static <K> K[][] unwrapBig(final Iterator<? extends K> i) {
        return unwrapBig(i, Long.MAX_VALUE);
    }
    
    public static <K> long unwrap(final Iterator<K> i, final ObjectCollection<? super K> c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add((Object)i.next());
            ++n;
        }
        return n;
    }
    
    public static <K> int pour(final Iterator<K> i, final ObjectCollection<? super K> s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add((Object)i.next());
        }
        return max - j - 1;
    }
    
    public static <K> int pour(final Iterator<K> i, final ObjectCollection<? super K> s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static <K> ObjectList<K> pour(final Iterator<K> i, final int max) {
        final ObjectArrayList<K> l = new ObjectArrayList<K>();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static <K> ObjectList<K> pour(final Iterator<K> i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static <K> ObjectIterator<K> asObjectIterator(final Iterator<K> i) {
        if (i instanceof ObjectIterator) {
            return (ObjectIterator<K>)(ObjectIterator)i;
        }
        return new IteratorWrapper<K>(i);
    }
    
    public static <K> ObjectListIterator<K> asObjectIterator(final ListIterator<K> i) {
        if (i instanceof ObjectListIterator) {
            return (ObjectListIterator<K>)(ObjectListIterator)i;
        }
        return new ListIteratorWrapper<K>(i);
    }
    
    public static <K> boolean any(final Iterator<K> iterator, final Predicate<? super K> predicate) {
        return indexOf(iterator, predicate) != -1;
    }
    
    public static <K> boolean all(final Iterator<K> iterator, final Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate);
        while (iterator.hasNext()) {
            if (!predicate.test((Object)iterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    public static <K> int indexOf(final Iterator<K> iterator, final Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate);
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test((Object)iterator.next())) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    @SafeVarargs
    public static <K> ObjectIterator<K> concat(final ObjectIterator<? extends K>... a) {
        return concat(a, 0, a.length);
    }
    
    public static <K> ObjectIterator<K> concat(final ObjectIterator<? extends K>[] a, final int offset, final int length) {
        return new IteratorConcatenator<K>(a, offset, length);
    }
    
    public static <K> ObjectIterator<K> unmodifiable(final ObjectIterator<? extends K> i) {
        return (ObjectIterator<K>)new ObjectIterators.UnmodifiableIterator((ObjectIterator)i);
    }
    
    public static <K> ObjectBidirectionalIterator<K> unmodifiable(final ObjectBidirectionalIterator<? extends K> i) {
        return (ObjectBidirectionalIterator<K>)new ObjectIterators.UnmodifiableBidirectionalIterator((ObjectBidirectionalIterator)i);
    }
    
    public static <K> ObjectListIterator<K> unmodifiable(final ObjectListIterator<? extends K> i) {
        return (ObjectListIterator<K>)new ObjectIterators.UnmodifiableListIterator((ObjectListIterator)i);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator<K> implements ObjectListIterator<K>, Serializable, Cloneable
    {
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
        public int skip(final int n) {
            return 0;
        }
        
        @Override
        public int back(final int n) {
            return 0;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super K> action) {
        }
        
        public Object clone() {
            return ObjectIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return ObjectIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator<K> implements ObjectListIterator<K>
    {
        private final K element;
        private byte curr;
        
        public SingletonIterator(final K element) {
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
        public void forEachRemaining(final Consumer<? super K> action) {
            Objects.requireNonNull(action);
            if (this.curr == 0) {
                action.accept((Object)this.element);
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
        public int back(final int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (n == 0 || this.curr < 1) {
                return 0;
            }
            this.curr = 1;
            return 1;
        }
        
        @Override
        public int skip(final int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (n == 0 || this.curr > 0) {
                return 0;
            }
            this.curr = 0;
            return 1;
        }
    }
    
    private static class ArrayIterator<K> implements ObjectListIterator<K>
    {
        private final K[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final K[] array, final int offset, final int length) {
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
            final K[] array = this.array;
            final int offset = this.offset;
            final int curr = this.curr - 1;
            this.curr = curr;
            return array[offset + curr];
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super K> action) {
            Objects.requireNonNull(action);
            while (this.curr < this.length) {
                action.accept((Object)this.array[this.offset + this.curr]);
                ++this.curr;
            }
        }
        
        @Override
        public int skip(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (n <= this.length - this.curr) {
                this.curr += n;
                return n;
            }
            n = this.length - this.curr;
            this.curr = this.length;
            return n;
        }
        
        @Override
        public int back(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (n <= this.curr) {
                this.curr -= n;
                return n;
            }
            n = this.curr;
            this.curr = 0;
            return n;
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
    
    private static class IteratorWrapper<K> implements ObjectIterator<K>
    {
        final Iterator<K> i;
        
        public IteratorWrapper(final Iterator<K> i) {
            this.i = i;
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
        public void forEachRemaining(final Consumer<? super K> action) {
            this.i.forEachRemaining(action);
        }
    }
    
    private static class ListIteratorWrapper<K> implements ObjectListIterator<K>
    {
        final ListIterator<K> i;
        
        public ListIteratorWrapper(final ListIterator<K> i) {
            this.i = i;
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
        public void set(final K k) {
            this.i.set(k);
        }
        
        @Override
        public void add(final K k) {
            this.i.add(k);
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
        public void forEachRemaining(final Consumer<? super K> action) {
            this.i.forEachRemaining(action);
        }
    }
    
    public abstract static class AbstractIndexBasedIterator<K> extends AbstractObjectIterator<K>
    {
        protected final int minPos;
        protected int pos;
        protected int lastReturned;
        
        protected AbstractIndexBasedIterator(final int minPos, final int initialPos) {
            this.minPos = minPos;
            this.pos = initialPos;
        }
        
        protected abstract K get(final int p0);
        
        protected abstract void remove(final int p0);
        
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
            final int lastReturned = this.pos++;
            this.lastReturned = lastReturned;
            return this.get(lastReturned);
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
        public void forEachRemaining(final Consumer<? super K> action) {
            while (this.pos < this.getMaxPos()) {
                final int lastReturned = this.pos++;
                this.lastReturned = lastReturned;
                action.accept((Object)this.get(lastReturned));
            }
        }
        
        @Override
        public int skip(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            final int max = this.getMaxPos();
            final int remaining = max - this.pos;
            if (n < remaining) {
                this.pos += n;
            }
            else {
                n = remaining;
                this.pos = max;
            }
            this.lastReturned = this.pos - 1;
            return n;
        }
    }
    
    public abstract static class AbstractIndexBasedListIterator<K> extends AbstractIndexBasedIterator<K> implements ObjectListIterator<K>
    {
        protected AbstractIndexBasedListIterator(final int minPos, final int initialPos) {
            super(minPos, initialPos);
        }
        
        protected abstract void add(final int p0, final K p1);
        
        protected abstract void set(final int p0, final K p1);
        
        @Override
        public boolean hasPrevious() {
            return this.pos > this.minPos;
        }
        
        @Override
        public K previous() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final int n = this.pos - 1;
            this.pos = n;
            this.lastReturned = n;
            return this.get(n);
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
        public void add(final K k) {
            this.add(this.pos++, k);
            this.lastReturned = -1;
        }
        
        @Override
        public void set(final K k) {
            if (this.lastReturned == -1) {
                throw new IllegalStateException();
            }
            this.set(this.lastReturned, k);
        }
        
        @Override
        public int back(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            final int remaining = this.pos - this.minPos;
            if (n < remaining) {
                this.pos -= n;
            }
            else {
                n = remaining;
                this.pos = this.minPos;
            }
            this.lastReturned = this.pos;
            return n;
        }
    }
    
    private static class IteratorConcatenator<K> implements ObjectIterator<K>
    {
        final ObjectIterator<? extends K>[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final ObjectIterator<? extends K>[] a, final int offset, final int length) {
            this.lastOffset = -1;
            this.a = a;
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
            final ObjectIterator<? extends K>[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final K next = (K)a[offset].next();
            this.advance();
            return next;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super K> action) {
            while (this.length > 0) {
                final ObjectIterator<? extends K>[] a = this.a;
                final int offset = this.offset;
                this.lastOffset = offset;
                a[offset].forEachRemaining(action);
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
        public int skip(final int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            this.lastOffset = -1;
            int skipped = 0;
            while (skipped < n && this.length != 0) {
                skipped += this.a[this.offset].skip(n - skipped);
                if (this.a[this.offset].hasNext()) {
                    break;
                }
                --this.length;
                ++this.offset;
            }
            return skipped;
        }
    }
}
