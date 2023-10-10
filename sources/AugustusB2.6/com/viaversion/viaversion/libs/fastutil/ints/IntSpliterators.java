// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.NoSuchElementException;
import java.util.Arrays;
import com.viaversion.viaversion.libs.fastutil.SafeMath;
import java.util.Comparator;
import java.util.function.Consumer;
import java.io.Serializable;
import com.viaversion.viaversion.libs.fastutil.chars.CharSpliterator;
import com.viaversion.viaversion.libs.fastutil.shorts.ShortSpliterator;
import com.viaversion.viaversion.libs.fastutil.bytes.ByteSpliterator;
import java.util.Objects;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.Spliterator;

public final class IntSpliterators
{
    static final int BASE_SPLITERATOR_CHARACTERISTICS = 256;
    public static final int COLLECTION_SPLITERATOR_CHARACTERISTICS = 320;
    public static final int LIST_SPLITERATOR_CHARACTERISTICS = 16720;
    public static final int SET_SPLITERATOR_CHARACTERISTICS = 321;
    private static final int SORTED_CHARACTERISTICS = 20;
    public static final int SORTED_SET_SPLITERATOR_CHARACTERISTICS = 341;
    public static final EmptySpliterator EMPTY_SPLITERATOR;
    
    private IntSpliterators() {
    }
    
    public static IntSpliterator singleton(final int element) {
        return new SingletonSpliterator(element);
    }
    
    public static IntSpliterator singleton(final int element, final IntComparator comparator) {
        return new SingletonSpliterator(element, comparator);
    }
    
    public static IntSpliterator wrap(final int[] array, final int offset, final int length) {
        IntArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliterator(array, offset, length, 0);
    }
    
    public static IntSpliterator wrap(final int[] array) {
        return new ArraySpliterator(array, 0, array.length, 0);
    }
    
    public static IntSpliterator wrap(final int[] array, final int offset, final int length, final int additionalCharacteristics) {
        IntArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliterator(array, offset, length, additionalCharacteristics);
    }
    
    public static IntSpliterator wrapPreSorted(final int[] array, final int offset, final int length, final int additionalCharacteristics, final IntComparator comparator) {
        IntArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliteratorWithComparator(array, offset, length, additionalCharacteristics, comparator);
    }
    
    public static IntSpliterator wrapPreSorted(final int[] array, final int offset, final int length, final IntComparator comparator) {
        return wrapPreSorted(array, offset, length, 0, comparator);
    }
    
    public static IntSpliterator wrapPreSorted(final int[] array, final IntComparator comparator) {
        return wrapPreSorted(array, 0, array.length, comparator);
    }
    
    public static IntSpliterator asIntSpliterator(final Spliterator i) {
        if (i instanceof IntSpliterator) {
            return (IntSpliterator)i;
        }
        if (i instanceof Spliterator.OfInt) {
            return new PrimitiveSpliteratorWrapper((Spliterator.OfInt)i);
        }
        return new SpliteratorWrapper(i);
    }
    
    public static IntSpliterator asIntSpliterator(final Spliterator i, final IntComparator comparatorOverride) {
        if (i instanceof IntSpliterator) {
            throw new IllegalArgumentException("Cannot override comparator on instance that is already a " + IntSpliterator.class.getSimpleName());
        }
        if (i instanceof Spliterator.OfInt) {
            return new PrimitiveSpliteratorWrapperWithComparator((Spliterator.OfInt)i, comparatorOverride);
        }
        return new SpliteratorWrapperWithComparator(i, comparatorOverride);
    }
    
    public static void onEachMatching(final IntSpliterator spliterator, final IntPredicate predicate, final IntConsumer action) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(action);
        spliterator.forEachRemaining(value -> {
            if (predicate.test(value)) {
                action.accept(value);
            }
        });
    }
    
    public static IntSpliterator fromTo(final int from, final int to) {
        return new IntervalSpliterator(from, to);
    }
    
    public static IntSpliterator concat(final IntSpliterator... a) {
        return concat(a, 0, a.length);
    }
    
    public static IntSpliterator concat(final IntSpliterator[] a, final int offset, final int length) {
        return new SpliteratorConcatenator(a, offset, length);
    }
    
    public static IntSpliterator asSpliterator(final IntIterator iter, final long size, final int additionalCharacterisitcs) {
        return new SpliteratorFromIterator(iter, size, additionalCharacterisitcs);
    }
    
    public static IntSpliterator asSpliteratorFromSorted(final IntIterator iter, final long size, final int additionalCharacterisitcs, final IntComparator comparator) {
        return new SpliteratorFromIteratorWithComparator(iter, size, additionalCharacterisitcs, comparator);
    }
    
    public static IntSpliterator asSpliteratorUnknownSize(final IntIterator iter, final int characterisitcs) {
        return new SpliteratorFromIterator(iter, characterisitcs);
    }
    
    public static IntSpliterator asSpliteratorFromSortedUnknownSize(final IntIterator iter, final int additionalCharacterisitcs, final IntComparator comparator) {
        return new SpliteratorFromIteratorWithComparator(iter, additionalCharacterisitcs, comparator);
    }
    
    public static IntIterator asIterator(final IntSpliterator spliterator) {
        return new IteratorFromSpliterator(spliterator);
    }
    
    public static IntSpliterator wrap(final ByteSpliterator spliterator) {
        return (IntSpliterator)new IntSpliterators.ByteSpliteratorWrapper(spliterator);
    }
    
    public static IntSpliterator wrap(final ShortSpliterator spliterator) {
        return (IntSpliterator)new IntSpliterators.ShortSpliteratorWrapper(spliterator);
    }
    
    public static IntSpliterator wrap(final CharSpliterator spliterator) {
        return (IntSpliterator)new IntSpliterators.CharSpliteratorWrapper(spliterator);
    }
    
    static {
        EMPTY_SPLITERATOR = new EmptySpliterator();
    }
    
    public static class EmptySpliterator implements IntSpliterator, Serializable, Cloneable
    {
        private static final long serialVersionUID = 8379247926738230492L;
        private static final int CHARACTERISTICS = 16448;
        
        protected EmptySpliterator() {
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer action) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean tryAdvance(final Consumer<? super Integer> action) {
            return false;
        }
        
        @Override
        public IntSpliterator trySplit() {
            return null;
        }
        
        @Override
        public long estimateSize() {
            return 0L;
        }
        
        @Override
        public int characteristics() {
            return 16448;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
        }
        
        @Deprecated
        @Override
        public void forEachRemaining(final Consumer<? super Integer> action) {
        }
        
        public Object clone() {
            return IntSpliterators.EMPTY_SPLITERATOR;
        }
        
        private Object readResolve() {
            return IntSpliterators.EMPTY_SPLITERATOR;
        }
    }
    
    private static class SingletonSpliterator implements IntSpliterator
    {
        private final int element;
        private final IntComparator comparator;
        private boolean consumed;
        private static final int CHARACTERISTICS = 17749;
        
        public SingletonSpliterator(final int element) {
            this(element, null);
        }
        
        public SingletonSpliterator(final int element, final IntComparator comparator) {
            this.consumed = false;
            this.element = element;
            this.comparator = comparator;
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer action) {
            Objects.requireNonNull(action);
            if (this.consumed) {
                return false;
            }
            this.consumed = true;
            action.accept(this.element);
            return true;
        }
        
        @Override
        public IntSpliterator trySplit() {
            return null;
        }
        
        @Override
        public long estimateSize() {
            return this.consumed ? 0 : 1;
        }
        
        @Override
        public int characteristics() {
            return 17749;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            Objects.requireNonNull(action);
            if (!this.consumed) {
                this.consumed = true;
                action.accept(this.element);
            }
        }
        
        @Override
        public IntComparator getComparator() {
            return this.comparator;
        }
        
        @Override
        public long skip(final long n) {
            if (n < 0L) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (n == 0L || this.consumed) {
                return 0L;
            }
            this.consumed = true;
            return 1L;
        }
    }
    
    private static class ArraySpliterator implements IntSpliterator
    {
        private static final int BASE_CHARACTERISTICS = 16720;
        final int[] array;
        private final int offset;
        private int length;
        private int curr;
        final int characteristics;
        
        public ArraySpliterator(final int[] array, final int offset, final int length, final int additionalCharacteristics) {
            this.array = array;
            this.offset = offset;
            this.length = length;
            this.characteristics = (0x4150 | additionalCharacteristics);
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer action) {
            if (this.curr >= this.length) {
                return false;
            }
            Objects.requireNonNull(action);
            action.accept(this.array[this.offset + this.curr++]);
            return true;
        }
        
        @Override
        public long estimateSize() {
            return this.length - this.curr;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        protected ArraySpliterator makeForSplit(final int newOffset, final int newLength) {
            return new ArraySpliterator(this.array, newOffset, newLength, this.characteristics);
        }
        
        @Override
        public IntSpliterator trySplit() {
            final int retLength = this.length - this.curr >> 1;
            if (retLength <= 1) {
                return null;
            }
            final int myNewCurr = this.curr + retLength;
            final int retOffset = this.offset + this.curr;
            this.curr = myNewCurr;
            return this.makeForSplit(retOffset, retLength);
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
        public long skip(long n) {
            if (n < 0L) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (this.curr >= this.length) {
                return 0L;
            }
            final int remaining = this.length - this.curr;
            if (n < remaining) {
                this.curr = SafeMath.safeLongToInt(this.curr + n);
                return n;
            }
            n = remaining;
            this.curr = this.length;
            return n;
        }
    }
    
    private static class ArraySpliteratorWithComparator extends ArraySpliterator
    {
        private final IntComparator comparator;
        
        public ArraySpliteratorWithComparator(final int[] array, final int offset, final int length, final int additionalCharacteristics, final IntComparator comparator) {
            super(array, offset, length, additionalCharacteristics | 0x14);
            this.comparator = comparator;
        }
        
        @Override
        protected ArraySpliteratorWithComparator makeForSplit(final int newOffset, final int newLength) {
            return new ArraySpliteratorWithComparator(this.array, newOffset, newLength, this.characteristics, this.comparator);
        }
        
        @Override
        public IntComparator getComparator() {
            return this.comparator;
        }
    }
    
    private static class SpliteratorWrapper implements IntSpliterator
    {
        final Spliterator<Integer> i;
        
        public SpliteratorWrapper(final Spliterator<Integer> i) {
            this.i = i;
        }
        
        @Override
        public boolean tryAdvance(final com.viaversion.viaversion.libs.fastutil.ints.IntConsumer action) {
            return this.i.tryAdvance(action);
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer action) {
            Objects.requireNonNull(action);
            final Spliterator<Integer> i = this.i;
            Consumer<Integer> consumer;
            if (action instanceof Consumer) {
                consumer = (Consumer<Integer>)action;
            }
            else {
                Objects.requireNonNull(action);
                consumer = action::accept;
            }
            return i.tryAdvance(consumer);
        }
        
        @Deprecated
        @Override
        public boolean tryAdvance(final Consumer<? super Integer> action) {
            return this.i.tryAdvance(action);
        }
        
        @Override
        public void forEachRemaining(final com.viaversion.viaversion.libs.fastutil.ints.IntConsumer action) {
            this.i.forEachRemaining(action);
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            Objects.requireNonNull(action);
            final Spliterator<Integer> i = this.i;
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
        
        @Override
        public long estimateSize() {
            return this.i.estimateSize();
        }
        
        @Override
        public int characteristics() {
            return this.i.characteristics();
        }
        
        @Override
        public IntComparator getComparator() {
            return IntComparators.asIntComparator(this.i.getComparator());
        }
        
        @Override
        public IntSpliterator trySplit() {
            final Spliterator<Integer> innerSplit = this.i.trySplit();
            if (innerSplit == null) {
                return null;
            }
            return new SpliteratorWrapper(innerSplit);
        }
    }
    
    private static class SpliteratorWrapperWithComparator extends SpliteratorWrapper
    {
        final IntComparator comparator;
        
        public SpliteratorWrapperWithComparator(final Spliterator<Integer> i, final IntComparator comparator) {
            super(i);
            this.comparator = comparator;
        }
        
        @Override
        public IntComparator getComparator() {
            return this.comparator;
        }
        
        @Override
        public IntSpliterator trySplit() {
            final Spliterator<Integer> innerSplit = this.i.trySplit();
            if (innerSplit == null) {
                return null;
            }
            return new SpliteratorWrapperWithComparator(innerSplit, this.comparator);
        }
    }
    
    private static class PrimitiveSpliteratorWrapper implements IntSpliterator
    {
        final Spliterator.OfInt i;
        
        public PrimitiveSpliteratorWrapper(final Spliterator.OfInt i) {
            this.i = i;
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer action) {
            return this.i.tryAdvance(action);
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            this.i.forEachRemaining(action);
        }
        
        @Override
        public long estimateSize() {
            return this.i.estimateSize();
        }
        
        @Override
        public int characteristics() {
            return this.i.characteristics();
        }
        
        @Override
        public IntComparator getComparator() {
            return IntComparators.asIntComparator(this.i.getComparator());
        }
        
        @Override
        public IntSpliterator trySplit() {
            final Spliterator.OfInt innerSplit = this.i.trySplit();
            if (innerSplit == null) {
                return null;
            }
            return new PrimitiveSpliteratorWrapper(innerSplit);
        }
    }
    
    private static class PrimitiveSpliteratorWrapperWithComparator extends PrimitiveSpliteratorWrapper
    {
        final IntComparator comparator;
        
        public PrimitiveSpliteratorWrapperWithComparator(final Spliterator.OfInt i, final IntComparator comparator) {
            super(i);
            this.comparator = comparator;
        }
        
        @Override
        public IntComparator getComparator() {
            return this.comparator;
        }
        
        @Override
        public IntSpliterator trySplit() {
            final Spliterator.OfInt innerSplit = this.i.trySplit();
            if (innerSplit == null) {
                return null;
            }
            return new PrimitiveSpliteratorWrapperWithComparator(innerSplit, this.comparator);
        }
    }
    
    public abstract static class AbstractIndexBasedSpliterator extends AbstractIntSpliterator
    {
        protected int pos;
        
        protected AbstractIndexBasedSpliterator(final int initialPos) {
            this.pos = initialPos;
        }
        
        protected abstract int get(final int p0);
        
        protected abstract int getMaxPos();
        
        protected abstract IntSpliterator makeForSplit(final int p0, final int p1);
        
        protected int computeSplitPoint() {
            return this.pos + (this.getMaxPos() - this.pos) / 2;
        }
        
        private void splitPointCheck(final int splitPoint, final int observedMax) {
            if (splitPoint < this.pos || splitPoint > observedMax) {
                throw new IndexOutOfBoundsException("splitPoint " + splitPoint + " outside of range of current position " + this.pos + " and range end " + observedMax);
            }
        }
        
        @Override
        public int characteristics() {
            return 16720;
        }
        
        @Override
        public long estimateSize() {
            return this.getMaxPos() - (long)this.pos;
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer action) {
            if (this.pos >= this.getMaxPos()) {
                return false;
            }
            action.accept(this.get(this.pos++));
            return true;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            final int max = this.getMaxPos();
            while (this.pos < max) {
                action.accept(this.get(this.pos));
                ++this.pos;
            }
        }
        
        @Override
        public long skip(long n) {
            if (n < 0L) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            final int max = this.getMaxPos();
            if (this.pos >= max) {
                return 0L;
            }
            final int remaining = max - this.pos;
            if (n < remaining) {
                this.pos = SafeMath.safeLongToInt(this.pos + n);
                return n;
            }
            n = remaining;
            this.pos = max;
            return n;
        }
        
        @Override
        public IntSpliterator trySplit() {
            final int max = this.getMaxPos();
            final int splitPoint = this.computeSplitPoint();
            if (splitPoint == this.pos || splitPoint == max) {
                return null;
            }
            this.splitPointCheck(splitPoint, max);
            final int oldPos = this.pos;
            final IntSpliterator maybeSplit = this.makeForSplit(oldPos, splitPoint);
            if (maybeSplit != null) {
                this.pos = splitPoint;
            }
            return maybeSplit;
        }
    }
    
    public abstract static class EarlyBindingSizeIndexBasedSpliterator extends AbstractIndexBasedSpliterator
    {
        protected final int maxPos;
        
        protected EarlyBindingSizeIndexBasedSpliterator(final int initialPos, final int maxPos) {
            super(initialPos);
            this.maxPos = maxPos;
        }
        
        @Override
        protected final int getMaxPos() {
            return this.maxPos;
        }
    }
    
    public abstract static class LateBindingSizeIndexBasedSpliterator extends AbstractIndexBasedSpliterator
    {
        protected int maxPos;
        private boolean maxPosFixed;
        
        protected LateBindingSizeIndexBasedSpliterator(final int initialPos) {
            super(initialPos);
            this.maxPos = -1;
            this.maxPosFixed = false;
        }
        
        protected LateBindingSizeIndexBasedSpliterator(final int initialPos, final int fixedMaxPos) {
            super(initialPos);
            this.maxPos = -1;
            this.maxPos = fixedMaxPos;
            this.maxPosFixed = true;
        }
        
        protected abstract int getMaxPosFromBackingStore();
        
        @Override
        protected final int getMaxPos() {
            return this.maxPosFixed ? this.maxPos : this.getMaxPosFromBackingStore();
        }
        
        @Override
        public IntSpliterator trySplit() {
            final IntSpliterator maybeSplit = super.trySplit();
            if (!this.maxPosFixed && maybeSplit != null) {
                this.maxPos = this.getMaxPosFromBackingStore();
                this.maxPosFixed = true;
            }
            return maybeSplit;
        }
    }
    
    private static class IntervalSpliterator implements IntSpliterator
    {
        private static final int DONT_SPLIT_THRESHOLD = 2;
        private static final int CHARACTERISTICS = 17749;
        private int curr;
        private int to;
        
        public IntervalSpliterator(final int from, final int to) {
            this.curr = from;
            this.to = to;
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer action) {
            if (this.curr >= this.to) {
                return false;
            }
            action.accept(this.curr++);
            return true;
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
        public long estimateSize() {
            return this.to - (long)this.curr;
        }
        
        @Override
        public int characteristics() {
            return 17749;
        }
        
        @Override
        public IntComparator getComparator() {
            return null;
        }
        
        @Override
        public IntSpliterator trySplit() {
            final long remaining = this.to - this.curr;
            final int mid = (int)(this.curr + (remaining >> 1));
            if (remaining >= 0L && remaining <= 2L) {
                return null;
            }
            final int old_curr = this.curr;
            this.curr = mid;
            return new IntervalSpliterator(old_curr, mid);
        }
        
        @Override
        public long skip(long n) {
            if (n < 0L) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (this.curr >= this.to) {
                return 0L;
            }
            final long newCurr = this.curr + n;
            if (newCurr <= this.to && newCurr >= this.curr) {
                this.curr = SafeMath.safeLongToInt(newCurr);
                return n;
            }
            n = this.to - this.curr;
            this.curr = this.to;
            return n;
        }
    }
    
    private static class SpliteratorConcatenator implements IntSpliterator
    {
        private static final int EMPTY_CHARACTERISTICS = 16448;
        private static final int CHARACTERISTICS_NOT_SUPPORTED_WHILE_MULTIPLE = 5;
        final IntSpliterator[] a;
        int offset;
        int length;
        long remainingEstimatedExceptCurrent;
        int characteristics;
        
        public SpliteratorConcatenator(final IntSpliterator[] a, final int offset, final int length) {
            this.remainingEstimatedExceptCurrent = Long.MAX_VALUE;
            this.characteristics = 0;
            this.a = a;
            this.offset = offset;
            this.length = length;
            this.remainingEstimatedExceptCurrent = this.recomputeRemaining();
            this.characteristics = this.computeCharacteristics();
        }
        
        private long recomputeRemaining() {
            int curLength = this.length - 1;
            int curOffset = this.offset + 1;
            long result = 0L;
            while (curLength > 0) {
                final long cur = this.a[curOffset++].estimateSize();
                --curLength;
                if (cur == Long.MAX_VALUE) {
                    return Long.MAX_VALUE;
                }
                result += cur;
                if (result == Long.MAX_VALUE || result < 0L) {
                    return Long.MAX_VALUE;
                }
            }
            return result;
        }
        
        private int computeCharacteristics() {
            if (this.length <= 0) {
                return 16448;
            }
            int current = -1;
            int curLength = this.length;
            int curOffset = this.offset;
            if (curLength > 1) {
                current &= 0xFFFFFFFA;
            }
            while (curLength > 0) {
                current &= this.a[curOffset++].characteristics();
                --curLength;
            }
            return current;
        }
        
        private void advanceNextSpliterator() {
            if (this.length <= 0) {
                throw new AssertionError((Object)"advanceNextSpliterator() called with none remaining");
            }
            ++this.offset;
            --this.length;
            this.remainingEstimatedExceptCurrent = this.recomputeRemaining();
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer action) {
            boolean any = false;
            while (this.length > 0) {
                if (this.a[this.offset].tryAdvance(action)) {
                    any = true;
                    break;
                }
                this.advanceNextSpliterator();
            }
            return any;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            while (this.length > 0) {
                this.a[this.offset].forEachRemaining(action);
                this.advanceNextSpliterator();
            }
        }
        
        @Deprecated
        @Override
        public void forEachRemaining(final Consumer<? super Integer> action) {
            while (this.length > 0) {
                this.a[this.offset].forEachRemaining(action);
                this.advanceNextSpliterator();
            }
        }
        
        @Override
        public long estimateSize() {
            if (this.length <= 0) {
                return 0L;
            }
            final long est = this.a[this.offset].estimateSize() + this.remainingEstimatedExceptCurrent;
            if (est < 0L) {
                return Long.MAX_VALUE;
            }
            return est;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        @Override
        public IntComparator getComparator() {
            if (this.length == 1 && (this.characteristics & 0x4) != 0x0) {
                return this.a[this.offset].getComparator();
            }
            throw new IllegalStateException();
        }
        
        @Override
        public IntSpliterator trySplit() {
            switch (this.length) {
                case 0: {
                    return null;
                }
                case 1: {
                    final IntSpliterator split = this.a[this.offset].trySplit();
                    this.characteristics = this.a[this.offset].characteristics();
                    return split;
                }
                case 2: {
                    final IntSpliterator split = this.a[this.offset++];
                    --this.length;
                    this.characteristics = this.a[this.offset].characteristics();
                    this.remainingEstimatedExceptCurrent = 0L;
                    return split;
                }
                default: {
                    final int mid = this.length >> 1;
                    final int ret_offset = this.offset;
                    final int new_offset = this.offset + mid;
                    final int ret_length = mid;
                    final int new_length = this.length - mid;
                    this.offset = new_offset;
                    this.length = new_length;
                    this.remainingEstimatedExceptCurrent = this.recomputeRemaining();
                    this.characteristics = this.computeCharacteristics();
                    return new SpliteratorConcatenator(this.a, ret_offset, ret_length);
                }
            }
        }
        
        @Override
        public long skip(final long n) {
            long skipped = 0L;
            if (this.length <= 0) {
                return 0L;
            }
            while (skipped < n && this.length >= 0) {
                final long curSkipped = this.a[this.offset].skip(n - skipped);
                skipped += curSkipped;
                if (skipped < n) {
                    this.advanceNextSpliterator();
                }
            }
            return skipped;
        }
    }
    
    private static class SpliteratorFromIterator implements IntSpliterator
    {
        private static final int BATCH_INCREMENT_SIZE = 1024;
        private static final int BATCH_MAX_SIZE = 33554432;
        private final IntIterator iter;
        final int characteristics;
        private final boolean knownSize;
        private long size;
        private int nextBatchSize;
        private IntSpliterator delegate;
        
        SpliteratorFromIterator(final IntIterator iter, final int characteristics) {
            this.size = Long.MAX_VALUE;
            this.nextBatchSize = 1024;
            this.delegate = null;
            this.iter = iter;
            this.characteristics = (0x100 | characteristics);
            this.knownSize = false;
        }
        
        SpliteratorFromIterator(final IntIterator iter, final long size, final int additionalCharacteristics) {
            this.size = Long.MAX_VALUE;
            this.nextBatchSize = 1024;
            this.delegate = null;
            this.iter = iter;
            this.knownSize = true;
            this.size = size;
            if ((additionalCharacteristics & 0x1000) != 0x0) {
                this.characteristics = (0x100 | additionalCharacteristics);
            }
            else {
                this.characteristics = (0x4140 | additionalCharacteristics);
            }
        }
        
        @Override
        public boolean tryAdvance(final IntConsumer action) {
            if (this.delegate != null) {
                final boolean hadRemaining = this.delegate.tryAdvance(action);
                if (!hadRemaining) {
                    this.delegate = null;
                }
                return hadRemaining;
            }
            if (!this.iter.hasNext()) {
                return false;
            }
            --this.size;
            action.accept(this.iter.nextInt());
            return true;
        }
        
        @Override
        public void forEachRemaining(final IntConsumer action) {
            if (this.delegate != null) {
                this.delegate.forEachRemaining(action);
                this.delegate = null;
            }
            this.iter.forEachRemaining(action);
            this.size = 0L;
        }
        
        @Override
        public long estimateSize() {
            if (this.delegate != null) {
                return this.delegate.estimateSize();
            }
            if (!this.iter.hasNext()) {
                return 0L;
            }
            return (this.knownSize && this.size >= 0L) ? this.size : Long.MAX_VALUE;
        }
        
        @Override
        public int characteristics() {
            return this.characteristics;
        }
        
        protected IntSpliterator makeForSplit(final int[] batch, final int len) {
            return IntSpliterators.wrap(batch, 0, len, this.characteristics);
        }
        
        @Override
        public IntSpliterator trySplit() {
            if (!this.iter.hasNext()) {
                return null;
            }
            int batchSizeEst;
            int[] batch;
            int actualSeen;
            for (batchSizeEst = ((this.knownSize && this.size > 0L) ? ((int)Math.min(this.nextBatchSize, this.size)) : this.nextBatchSize), batch = new int[batchSizeEst], actualSeen = 0; actualSeen < batchSizeEst && this.iter.hasNext(); batch[actualSeen++] = this.iter.nextInt(), --this.size) {}
            if (batchSizeEst < this.nextBatchSize && this.iter.hasNext()) {
                for (batch = Arrays.copyOf(batch, this.nextBatchSize); this.iter.hasNext() && actualSeen < this.nextBatchSize; batch[actualSeen++] = this.iter.nextInt(), --this.size) {}
            }
            this.nextBatchSize = Math.min(33554432, this.nextBatchSize + 1024);
            final IntSpliterator split = this.makeForSplit(batch, actualSeen);
            if (!this.iter.hasNext()) {
                this.delegate = split;
                return split.trySplit();
            }
            return split;
        }
        
        @Override
        public long skip(final long n) {
            if (n < 0L) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            if (this.iter instanceof IntBigListIterator) {
                final long skipped = ((IntBigListIterator)this.iter).skip(n);
                this.size -= skipped;
                return skipped;
            }
            long skippedSoFar;
            int skipped2;
            for (skippedSoFar = 0L; skippedSoFar < n && this.iter.hasNext(); skippedSoFar += skipped2) {
                skipped2 = this.iter.skip(SafeMath.safeLongToInt(Math.min(n, 2147483647L)));
                this.size -= skipped2;
            }
            return skippedSoFar;
        }
    }
    
    private static class SpliteratorFromIteratorWithComparator extends SpliteratorFromIterator
    {
        private final IntComparator comparator;
        
        SpliteratorFromIteratorWithComparator(final IntIterator iter, final int additionalCharacteristics, final IntComparator comparator) {
            super(iter, additionalCharacteristics | 0x14);
            this.comparator = comparator;
        }
        
        SpliteratorFromIteratorWithComparator(final IntIterator iter, final long size, final int additionalCharacteristics, final IntComparator comparator) {
            super(iter, size, additionalCharacteristics | 0x14);
            this.comparator = comparator;
        }
        
        @Override
        public IntComparator getComparator() {
            return this.comparator;
        }
        
        @Override
        protected IntSpliterator makeForSplit(final int[] array, final int len) {
            return IntSpliterators.wrapPreSorted(array, 0, len, this.characteristics, this.comparator);
        }
    }
    
    private static final class IteratorFromSpliterator implements IntIterator, IntConsumer
    {
        private final IntSpliterator spliterator;
        private int holder;
        private boolean hasPeeked;
        
        IteratorFromSpliterator(final IntSpliterator spliterator) {
            this.holder = 0;
            this.hasPeeked = false;
            this.spliterator = spliterator;
        }
        
        @Override
        public void accept(final int item) {
            this.holder = item;
        }
        
        @Override
        public boolean hasNext() {
            if (this.hasPeeked) {
                return true;
            }
            final boolean hadElement = this.spliterator.tryAdvance(this);
            return hadElement && (this.hasPeeked = true);
        }
        
        @Override
        public int nextInt() {
            if (this.hasPeeked) {
                this.hasPeeked = false;
                return this.holder;
            }
            final boolean hadElement = this.spliterator.tryAdvance(this);
            if (!hadElement) {
                throw new NoSuchElementException();
            }
            return this.holder;
        }
        
        @Override
        public void forEachRemaining(final java.util.function.IntConsumer action) {
            if (this.hasPeeked) {
                this.hasPeeked = false;
                action.accept(this.holder);
            }
            this.spliterator.forEachRemaining(action);
        }
        
        @Override
        public int skip(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            }
            int skipped = 0;
            if (this.hasPeeked) {
                this.hasPeeked = false;
                this.spliterator.skip(1L);
                ++skipped;
                --n;
            }
            if (n > 0) {
                skipped += SafeMath.safeLongToInt(this.spliterator.skip(n));
            }
            return skipped;
        }
    }
}
