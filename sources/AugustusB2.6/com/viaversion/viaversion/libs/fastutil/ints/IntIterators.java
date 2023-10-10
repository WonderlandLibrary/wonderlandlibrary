// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.NoSuchElementException;
import java.io.Serializable;
import com.viaversion.viaversion.libs.fastutil.chars.CharIterator;
import com.viaversion.viaversion.libs.fastutil.shorts.ShortIterator;
import com.viaversion.viaversion.libs.fastutil.bytes.ByteIterator;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.ListIterator;
import java.util.PrimitiveIterator;
import java.util.Iterator;
import com.viaversion.viaversion.libs.fastutil.BigArrays;

public final class IntIterators
{
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private IntIterators() {
    }
    
    public static IntListIterator singleton(final int element) {
        return new SingletonIterator(element);
    }
    
    public static IntListIterator wrap(final int[] array, final int offset, final int length) {
        IntArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static IntListIterator wrap(final int[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final IntIterator i, final int[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextInt();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final IntIterator i, final int[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static int[] unwrap(final IntIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int[] array = new int[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = IntArrays.grow(array, j + 1);
            }
            array[j++] = i.nextInt();
        }
        return IntArrays.trim(array, j);
    }
    
    public static int[] unwrap(final IntIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static long unwrap(final IntIterator i, final int[][] array, long offset, final long max) {
        if (max < 0L) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0L || offset + max > BigArrays.length(array)) {
            throw new IllegalArgumentException();
        }
        long j = max;
        while (j-- != 0L && i.hasNext()) {
            BigArrays.set(array, offset++, i.nextInt());
        }
        return max - j - 1L;
    }
    
    public static long unwrap(final IntIterator i, final int[][] array) {
        return unwrap(i, array, 0L, BigArrays.length(array));
    }
    
    public static int unwrap(final IntIterator i, final IntCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextInt());
        }
        return max - j - 1;
    }
    
    public static int[][] unwrapBig(final IntIterator i, long max) {
        if (max < 0L) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int[][] array = IntBigArrays.newBigArray(16L);
        long j = 0L;
        while (max-- != 0L && i.hasNext()) {
            if (j == BigArrays.length(array)) {
                array = BigArrays.grow(array, j + 1L);
            }
            BigArrays.set(array, j++, i.nextInt());
        }
        return BigArrays.trim(array, j);
    }
    
    public static int[][] unwrapBig(final IntIterator i) {
        return unwrapBig(i, Long.MAX_VALUE);
    }
    
    public static long unwrap(final IntIterator i, final IntCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextInt());
            ++n;
        }
        return n;
    }
    
    public static int pour(final IntIterator i, final IntCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextInt());
        }
        return max - j - 1;
    }
    
    public static int pour(final IntIterator i, final IntCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static IntList pour(final IntIterator i, final int max) {
        final IntArrayList l = new IntArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static IntList pour(final IntIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static IntIterator asIntIterator(final Iterator i) {
        if (i instanceof IntIterator) {
            return (IntIterator)i;
        }
        if (i instanceof PrimitiveIterator.OfInt) {
            return new PrimitiveIteratorWrapper((PrimitiveIterator.OfInt)i);
        }
        return new IteratorWrapper(i);
    }
    
    public static IntListIterator asIntIterator(final ListIterator i) {
        if (i instanceof IntListIterator) {
            return (IntListIterator)i;
        }
        return new ListIteratorWrapper(i);
    }
    
    public static boolean any(final IntIterator iterator, final IntPredicate predicate) {
        return indexOf(iterator, predicate) != -1;
    }
    
    public static boolean all(final IntIterator iterator, final IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.nextInt())) {
                return false;
            }
        }
        return true;
    }
    
    public static int indexOf(final IntIterator iterator, final IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextInt())) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static IntListIterator fromTo(final int from, final int to) {
        return new IntervalIterator(from, to);
    }
    
    public static IntIterator concat(final IntIterator... a) {
        return concat(a, 0, a.length);
    }
    
    public static IntIterator concat(final IntIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static IntIterator unmodifiable(final IntIterator i) {
        return (IntIterator)new IntIterators.UnmodifiableIterator(i);
    }
    
    public static IntBidirectionalIterator unmodifiable(final IntBidirectionalIterator i) {
        return (IntBidirectionalIterator)new IntIterators.UnmodifiableBidirectionalIterator(i);
    }
    
    public static IntListIterator unmodifiable(final IntListIterator i) {
        return (IntListIterator)new IntIterators.UnmodifiableListIterator(i);
    }
    
    public static IntIterator wrap(final ByteIterator iterator) {
        return (IntIterator)new IntIterators.ByteIteratorWrapper(iterator);
    }
    
    public static IntIterator wrap(final ShortIterator iterator) {
        return (IntIterator)new IntIterators.ShortIteratorWrapper(iterator);
    }
    
    public static IntIterator wrap(final CharIterator iterator) {
        return (IntIterator)new IntIterators.CharIteratorWrapper(iterator);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator implements IntListIterator, Serializable, Cloneable
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
        public int skip(final int n) {
            return 0;
        }
        
        @Override
        public int back(final int n) {
            return 0;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
        }
        
        @Deprecated
        @Override
        public void forEachRemaining(final Consumer<? super Integer> action) {
        }
        
        public Object clone() {
            return IntIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return IntIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator implements IntListIterator
    {
        private final int element;
        private byte curr;
        
        public SingletonIterator(final int element) {
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
        public void forEachRemaining(final IntConsumer action) {
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
    
    private static class ArrayIterator implements IntListIterator
    {
        private final int[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final int[] array, final int offset, final int length) {
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
            final int[] array = this.array;
            final int offset = this.offset;
            final int curr = this.curr - 1;
            this.curr = curr;
            return array[offset + curr];
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            Objects.requireNonNull(action);
            while (this.curr < this.length) {
                action.accept(this.array[this.offset + this.curr]);
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
    
    private static class IteratorWrapper implements IntIterator
    {
        final Iterator<Integer> i;
        
        public IteratorWrapper(final Iterator<Integer> i) {
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
        public int nextInt() {
            return this.i.next();
        }
        
        @Override
        public void forEachRemaining(final com.viaversion.viaversion.libs.fastutil.ints.IntConsumer action) {
            this.i.forEachRemaining(action);
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            Objects.requireNonNull(action);
            final Iterator<Integer> i = this.i;
            Consumer<Integer> action2;
            if (action instanceof Consumer) {
                action2 = (Consumer<Integer>)action;
            }
            else {
                Objects.requireNonNull(action);
                action2 = action::accept;
            }
            i.forEachRemaining(action2);
        }
        
        @Deprecated
        @Override
        public void forEachRemaining(final Consumer<? super Integer> action) {
            this.i.forEachRemaining(action);
        }
    }
    
    private static class PrimitiveIteratorWrapper implements IntIterator
    {
        final PrimitiveIterator.OfInt i;
        
        public PrimitiveIteratorWrapper(final PrimitiveIterator.OfInt i) {
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
        public int nextInt() {
            return this.i.nextInt();
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            this.i.forEachRemaining(action);
        }
    }
    
    private static class ListIteratorWrapper implements IntListIterator
    {
        final ListIterator<Integer> i;
        
        public ListIteratorWrapper(final ListIterator<Integer> i) {
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
        public void set(final int k) {
            this.i.set(k);
        }
        
        @Override
        public void add(final int k) {
            this.i.add(k);
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
        public void forEachRemaining(final com.viaversion.viaversion.libs.fastutil.ints.IntConsumer action) {
            this.i.forEachRemaining(action);
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            Objects.requireNonNull(action);
            final ListIterator<Integer> i = this.i;
            Consumer<Integer> action2;
            if (action instanceof Consumer) {
                action2 = (Consumer<Integer>)action;
            }
            else {
                Objects.requireNonNull(action);
                action2 = action::accept;
            }
            i.forEachRemaining(action2);
        }
        
        @Deprecated
        @Override
        public void forEachRemaining(final Consumer<? super Integer> action) {
            this.i.forEachRemaining(action);
        }
    }
    
    public abstract static class AbstractIndexBasedIterator extends AbstractIntIterator
    {
        protected final int minPos;
        protected int pos;
        protected int lastReturned;
        
        protected AbstractIndexBasedIterator(final int minPos, final int initialPos) {
            this.minPos = minPos;
            this.pos = initialPos;
        }
        
        protected abstract int get(final int p0);
        
        protected abstract void remove(final int p0);
        
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
        public void forEachRemaining(final IntConsumer action) {
            while (this.pos < this.getMaxPos()) {
                final int lastReturned = this.pos++;
                this.lastReturned = lastReturned;
                action.accept(this.get(lastReturned));
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
    
    public abstract static class AbstractIndexBasedListIterator extends AbstractIndexBasedIterator implements IntListIterator
    {
        protected AbstractIndexBasedListIterator(final int minPos, final int initialPos) {
            super(minPos, initialPos);
        }
        
        protected abstract void add(final int p0, final int p1);
        
        protected abstract void set(final int p0, final int p1);
        
        @Override
        public boolean hasPrevious() {
            return this.pos > this.minPos;
        }
        
        @Override
        public int previousInt() {
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
        public void add(final int k) {
            this.add(this.pos++, k);
            this.lastReturned = -1;
        }
        
        @Override
        public void set(final int k) {
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
    
    private static class IntervalIterator implements IntListIterator
    {
        private final int from;
        private final int to;
        int curr;
        
        public IntervalIterator(final int from, final int to) {
            this.curr = from;
            this.from = from;
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
        public void forEachRemaining(final IntConsumer action) {
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
        public int skip(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (this.curr + n <= this.to) {
                this.curr += n;
                return n;
            }
            n = this.to - this.curr;
            this.curr = this.to;
            return n;
        }
        
        @Override
        public int back(int n) {
            if (this.curr - n >= this.from) {
                this.curr -= n;
                return n;
            }
            n = this.curr - this.from;
            this.curr = this.from;
            return n;
        }
    }
    
    private static class IteratorConcatenator implements IntIterator
    {
        final IntIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final IntIterator[] a, final int offset, final int length) {
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
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final IntIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final int next = a[offset].nextInt();
            this.advance();
            return next;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            while (this.length > 0) {
                final IntIterator[] a = this.a;
                final int offset = this.offset;
                this.lastOffset = offset;
                a[offset].forEachRemaining(action);
                this.advance();
            }
        }
        
        @Deprecated
        @Override
        public void forEachRemaining(final Consumer<? super Integer> action) {
            while (this.length > 0) {
                final IntIterator[] a = this.a;
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
