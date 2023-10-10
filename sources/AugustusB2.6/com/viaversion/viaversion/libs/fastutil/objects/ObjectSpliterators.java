// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.objects;

import java.util.NoSuchElementException;
import java.util.Arrays;
import com.viaversion.viaversion.libs.fastutil.SafeMath;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.Spliterator;
import java.util.Comparator;

public final class ObjectSpliterators
{
    static final int BASE_SPLITERATOR_CHARACTERISTICS = 0;
    public static final int COLLECTION_SPLITERATOR_CHARACTERISTICS = 64;
    public static final int LIST_SPLITERATOR_CHARACTERISTICS = 16464;
    public static final int SET_SPLITERATOR_CHARACTERISTICS = 65;
    private static final int SORTED_CHARACTERISTICS = 20;
    public static final int SORTED_SET_SPLITERATOR_CHARACTERISTICS = 85;
    public static final EmptySpliterator EMPTY_SPLITERATOR;
    
    private ObjectSpliterators() {
    }
    
    public static <K> ObjectSpliterator<K> emptySpliterator() {
        return (ObjectSpliterator<K>)ObjectSpliterators.EMPTY_SPLITERATOR;
    }
    
    public static <K> ObjectSpliterator<K> singleton(final K element) {
        return new SingletonSpliterator<K>(element);
    }
    
    public static <K> ObjectSpliterator<K> singleton(final K element, final Comparator<? super K> comparator) {
        return new SingletonSpliterator<K>(element, comparator);
    }
    
    public static <K> ObjectSpliterator<K> wrap(final K[] array, final int offset, final int length) {
        ObjectArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliterator<K>(array, offset, length, 0);
    }
    
    public static <K> ObjectSpliterator<K> wrap(final K[] array) {
        return new ArraySpliterator<K>(array, 0, array.length, 0);
    }
    
    public static <K> ObjectSpliterator<K> wrap(final K[] array, final int offset, final int length, final int additionalCharacteristics) {
        ObjectArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliterator<K>(array, offset, length, additionalCharacteristics);
    }
    
    public static <K> ObjectSpliterator<K> wrapPreSorted(final K[] array, final int offset, final int length, final int additionalCharacteristics, final Comparator<? super K> comparator) {
        ObjectArrays.ensureOffsetLength(array, offset, length);
        return new ArraySpliteratorWithComparator<K>(array, offset, length, additionalCharacteristics, comparator);
    }
    
    public static <K> ObjectSpliterator<K> wrapPreSorted(final K[] array, final int offset, final int length, final Comparator<? super K> comparator) {
        return wrapPreSorted(array, offset, length, 0, comparator);
    }
    
    public static <K> ObjectSpliterator<K> wrapPreSorted(final K[] array, final Comparator<? super K> comparator) {
        return wrapPreSorted(array, 0, array.length, comparator);
    }
    
    public static <K> ObjectSpliterator<K> asObjectSpliterator(final Spliterator<K> i) {
        if (i instanceof ObjectSpliterator) {
            return (ObjectSpliterator<K>)(ObjectSpliterator)i;
        }
        return new SpliteratorWrapper<K>(i);
    }
    
    public static <K> ObjectSpliterator<K> asObjectSpliterator(final Spliterator<K> i, final Comparator<? super K> comparatorOverride) {
        if (i instanceof ObjectSpliterator) {
            throw new IllegalArgumentException("Cannot override comparator on instance that is already a " + ObjectSpliterator.class.getSimpleName());
        }
        return new SpliteratorWrapperWithComparator<K>(i, comparatorOverride);
    }
    
    public static <K> void onEachMatching(final Spliterator<K> spliterator, final Predicate<? super K> predicate, final Consumer<? super K> action) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(action);
        spliterator.forEachRemaining(value -> {
            if (predicate.test((Object)value)) {
                action.accept((Object)value);
            }
        });
    }
    
    @SafeVarargs
    public static <K> ObjectSpliterator<K> concat(final ObjectSpliterator<? extends K>... a) {
        return concat(a, 0, a.length);
    }
    
    public static <K> ObjectSpliterator<K> concat(final ObjectSpliterator<? extends K>[] a, final int offset, final int length) {
        return new SpliteratorConcatenator<K>(a, offset, length);
    }
    
    public static <K> ObjectSpliterator<K> asSpliterator(final ObjectIterator<? extends K> iter, final long size, final int additionalCharacterisitcs) {
        return new SpliteratorFromIterator<K>(iter, size, additionalCharacterisitcs);
    }
    
    public static <K> ObjectSpliterator<K> asSpliteratorFromSorted(final ObjectIterator<? extends K> iter, final long size, final int additionalCharacterisitcs, final Comparator<? super K> comparator) {
        return new SpliteratorFromIteratorWithComparator<K>(iter, size, additionalCharacterisitcs, comparator);
    }
    
    public static <K> ObjectSpliterator<K> asSpliteratorUnknownSize(final ObjectIterator<? extends K> iter, final int characterisitcs) {
        return new SpliteratorFromIterator<K>(iter, characterisitcs);
    }
    
    public static <K> ObjectSpliterator<K> asSpliteratorFromSortedUnknownSize(final ObjectIterator<? extends K> iter, final int additionalCharacterisitcs, final Comparator<? super K> comparator) {
        return new SpliteratorFromIteratorWithComparator<K>(iter, additionalCharacterisitcs, comparator);
    }
    
    public static <K> ObjectIterator<K> asIterator(final ObjectSpliterator<? extends K> spliterator) {
        return new IteratorFromSpliterator<K>(spliterator);
    }
    
    static {
        EMPTY_SPLITERATOR = new EmptySpliterator();
    }
    
    public static class EmptySpliterator<K> implements ObjectSpliterator<K>, Serializable, Cloneable
    {
        private static final long serialVersionUID = 8379247926738230492L;
        private static final int CHARACTERISTICS = 16448;
        
        protected EmptySpliterator() {
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super K> action) {
            return false;
        }
        
        @Override
        public ObjectSpliterator<K> trySplit() {
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
        public void forEachRemaining(final Consumer<? super K> action) {
        }
        
        public Object clone() {
            return ObjectSpliterators.EMPTY_SPLITERATOR;
        }
        
        private Object readResolve() {
            return ObjectSpliterators.EMPTY_SPLITERATOR;
        }
    }
    
    private static class SingletonSpliterator<K> implements ObjectSpliterator<K>
    {
        private final K element;
        private final Comparator<? super K> comparator;
        private boolean consumed;
        private static final int CHARACTERISTICS = 17493;
        
        public SingletonSpliterator(final K element) {
            this(element, null);
        }
        
        public SingletonSpliterator(final K element, final Comparator<? super K> comparator) {
            this.consumed = false;
            this.element = element;
            this.comparator = comparator;
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super K> action) {
            Objects.requireNonNull(action);
            if (this.consumed) {
                return false;
            }
            this.consumed = true;
            action.accept((Object)this.element);
            return true;
        }
        
        @Override
        public ObjectSpliterator<K> trySplit() {
            return null;
        }
        
        @Override
        public long estimateSize() {
            return this.consumed ? 0 : 1;
        }
        
        @Override
        public int characteristics() {
            return 0x4455 | ((this.element != null) ? 256 : 0);
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super K> action) {
            Objects.requireNonNull(action);
            if (!this.consumed) {
                this.consumed = true;
                action.accept((Object)this.element);
            }
        }
        
        @Override
        public Comparator<? super K> getComparator() {
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
    
    private static class ArraySpliterator<K> implements ObjectSpliterator<K>
    {
        private static final int BASE_CHARACTERISTICS = 16464;
        final K[] array;
        private final int offset;
        private int length;
        private int curr;
        final int characteristics;
        
        public ArraySpliterator(final K[] array, final int offset, final int length, final int additionalCharacteristics) {
            this.array = array;
            this.offset = offset;
            this.length = length;
            this.characteristics = (0x4050 | additionalCharacteristics);
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super K> action) {
            if (this.curr >= this.length) {
                return false;
            }
            Objects.requireNonNull(action);
            action.accept((Object)this.array[this.offset + this.curr++]);
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
        
        protected ArraySpliterator<K> makeForSplit(final int newOffset, final int newLength) {
            return new ArraySpliterator<K>(this.array, newOffset, newLength, this.characteristics);
        }
        
        @Override
        public ObjectSpliterator<K> trySplit() {
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
        public void forEachRemaining(final Consumer<? super K> action) {
            Objects.requireNonNull(action);
            while (this.curr < this.length) {
                action.accept((Object)this.array[this.offset + this.curr]);
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
    
    private static class ArraySpliteratorWithComparator<K> extends ArraySpliterator<K>
    {
        private final Comparator<? super K> comparator;
        
        public ArraySpliteratorWithComparator(final K[] array, final int offset, final int length, final int additionalCharacteristics, final Comparator<? super K> comparator) {
            super(array, offset, length, additionalCharacteristics | 0x14);
            this.comparator = comparator;
        }
        
        @Override
        protected ArraySpliteratorWithComparator<K> makeForSplit(final int newOffset, final int newLength) {
            return new ArraySpliteratorWithComparator<K>(this.array, newOffset, newLength, this.characteristics, this.comparator);
        }
        
        @Override
        public Comparator<? super K> getComparator() {
            return this.comparator;
        }
    }
    
    private static class SpliteratorWrapper<K> implements ObjectSpliterator<K>
    {
        final Spliterator<K> i;
        
        public SpliteratorWrapper(final Spliterator<K> i) {
            this.i = i;
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super K> action) {
            return this.i.tryAdvance(action);
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super K> action) {
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
        public Comparator<? super K> getComparator() {
            return ObjectComparators.asObjectComparator(this.i.getComparator());
        }
        
        @Override
        public ObjectSpliterator<K> trySplit() {
            final Spliterator<K> innerSplit = this.i.trySplit();
            if (innerSplit == null) {
                return null;
            }
            return new SpliteratorWrapper((Spliterator<Object>)innerSplit);
        }
    }
    
    private static class SpliteratorWrapperWithComparator<K> extends SpliteratorWrapper<K>
    {
        final Comparator<? super K> comparator;
        
        public SpliteratorWrapperWithComparator(final Spliterator<K> i, final Comparator<? super K> comparator) {
            super(i);
            this.comparator = comparator;
        }
        
        @Override
        public Comparator<? super K> getComparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSpliterator<K> trySplit() {
            final Spliterator<K> innerSplit = this.i.trySplit();
            if (innerSplit == null) {
                return null;
            }
            return new SpliteratorWrapperWithComparator((Spliterator<Object>)innerSplit, (Comparator<? super Object>)this.comparator);
        }
    }
    
    public abstract static class AbstractIndexBasedSpliterator<K> extends AbstractObjectSpliterator<K>
    {
        protected int pos;
        
        protected AbstractIndexBasedSpliterator(final int initialPos) {
            this.pos = initialPos;
        }
        
        protected abstract K get(final int p0);
        
        protected abstract int getMaxPos();
        
        protected abstract ObjectSpliterator<K> makeForSplit(final int p0, final int p1);
        
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
            return 16464;
        }
        
        @Override
        public long estimateSize() {
            return this.getMaxPos() - (long)this.pos;
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super K> action) {
            if (this.pos >= this.getMaxPos()) {
                return false;
            }
            action.accept(this.get(this.pos++));
            return true;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super K> action) {
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
        public ObjectSpliterator<K> trySplit() {
            final int max = this.getMaxPos();
            final int splitPoint = this.computeSplitPoint();
            if (splitPoint == this.pos || splitPoint == max) {
                return null;
            }
            this.splitPointCheck(splitPoint, max);
            final int oldPos = this.pos;
            final ObjectSpliterator<K> maybeSplit = this.makeForSplit(oldPos, splitPoint);
            if (maybeSplit != null) {
                this.pos = splitPoint;
            }
            return maybeSplit;
        }
    }
    
    public abstract static class EarlyBindingSizeIndexBasedSpliterator<K> extends AbstractIndexBasedSpliterator<K>
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
    
    public abstract static class LateBindingSizeIndexBasedSpliterator<K> extends AbstractIndexBasedSpliterator<K>
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
        public ObjectSpliterator<K> trySplit() {
            final ObjectSpliterator<K> maybeSplit = super.trySplit();
            if (!this.maxPosFixed && maybeSplit != null) {
                this.maxPos = this.getMaxPosFromBackingStore();
                this.maxPosFixed = true;
            }
            return maybeSplit;
        }
    }
    
    private static class SpliteratorConcatenator<K> implements ObjectSpliterator<K>
    {
        private static final int EMPTY_CHARACTERISTICS = 16448;
        private static final int CHARACTERISTICS_NOT_SUPPORTED_WHILE_MULTIPLE = 5;
        final ObjectSpliterator<? extends K>[] a;
        int offset;
        int length;
        long remainingEstimatedExceptCurrent;
        int characteristics;
        
        public SpliteratorConcatenator(final ObjectSpliterator<? extends K>[] a, final int offset, final int length) {
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
        public boolean tryAdvance(final Consumer<? super K> action) {
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
        public void forEachRemaining(final Consumer<? super K> action) {
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
        public Comparator<? super K> getComparator() {
            if (this.length == 1 && (this.characteristics & 0x4) != 0x0) {
                return this.a[this.offset].getComparator();
            }
            throw new IllegalStateException();
        }
        
        @Override
        public ObjectSpliterator<K> trySplit() {
            switch (this.length) {
                case 0: {
                    return null;
                }
                case 1: {
                    final ObjectSpliterator<K> split = (ObjectSpliterator<K>)this.a[this.offset].trySplit();
                    this.characteristics = this.a[this.offset].characteristics();
                    return split;
                }
                case 2: {
                    final ObjectSpliterator<K> split = (ObjectSpliterator<K>)this.a[this.offset++];
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
    
    private static class SpliteratorFromIterator<K> implements ObjectSpliterator<K>
    {
        private static final int BATCH_INCREMENT_SIZE = 1024;
        private static final int BATCH_MAX_SIZE = 33554432;
        private final ObjectIterator<? extends K> iter;
        final int characteristics;
        private final boolean knownSize;
        private long size;
        private int nextBatchSize;
        private ObjectSpliterator<K> delegate;
        
        SpliteratorFromIterator(final ObjectIterator<? extends K> iter, final int characteristics) {
            this.size = Long.MAX_VALUE;
            this.nextBatchSize = 1024;
            this.delegate = null;
            this.iter = iter;
            this.characteristics = (0x0 | characteristics);
            this.knownSize = false;
        }
        
        SpliteratorFromIterator(final ObjectIterator<? extends K> iter, final long size, final int additionalCharacteristics) {
            this.size = Long.MAX_VALUE;
            this.nextBatchSize = 1024;
            this.delegate = null;
            this.iter = iter;
            this.knownSize = true;
            this.size = size;
            if ((additionalCharacteristics & 0x1000) != 0x0) {
                this.characteristics = (0x0 | additionalCharacteristics);
            }
            else {
                this.characteristics = (0x4040 | additionalCharacteristics);
            }
        }
        
        @Override
        public boolean tryAdvance(final Consumer<? super K> action) {
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
            action.accept((Object)this.iter.next());
            return true;
        }
        
        @Override
        public void forEachRemaining(final Consumer<? super K> action) {
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
        
        protected ObjectSpliterator<K> makeForSplit(final K[] batch, final int len) {
            return ObjectSpliterators.wrap(batch, 0, len, this.characteristics);
        }
        
        @Override
        public ObjectSpliterator<K> trySplit() {
            if (!this.iter.hasNext()) {
                return null;
            }
            int batchSizeEst;
            K[] batch;
            int actualSeen;
            for (batchSizeEst = ((this.knownSize && this.size > 0L) ? ((int)Math.min(this.nextBatchSize, this.size)) : this.nextBatchSize), batch = (K[])new Object[batchSizeEst], actualSeen = 0; actualSeen < batchSizeEst && this.iter.hasNext(); batch[actualSeen++] = (K)this.iter.next(), --this.size) {}
            if (batchSizeEst < this.nextBatchSize && this.iter.hasNext()) {
                for (batch = Arrays.copyOf(batch, this.nextBatchSize); this.iter.hasNext() && actualSeen < this.nextBatchSize; batch[actualSeen++] = (K)this.iter.next(), --this.size) {}
            }
            this.nextBatchSize = Math.min(33554432, this.nextBatchSize + 1024);
            final ObjectSpliterator<K> split = this.makeForSplit(batch, actualSeen);
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
            if (this.iter instanceof ObjectBigListIterator) {
                final long skipped = ((ObjectBigListIterator)this.iter).skip(n);
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
    
    private static class SpliteratorFromIteratorWithComparator<K> extends SpliteratorFromIterator<K>
    {
        private final Comparator<? super K> comparator;
        
        SpliteratorFromIteratorWithComparator(final ObjectIterator<? extends K> iter, final int additionalCharacteristics, final Comparator<? super K> comparator) {
            super(iter, additionalCharacteristics | 0x14);
            this.comparator = comparator;
        }
        
        SpliteratorFromIteratorWithComparator(final ObjectIterator<? extends K> iter, final long size, final int additionalCharacteristics, final Comparator<? super K> comparator) {
            super(iter, size, additionalCharacteristics | 0x14);
            this.comparator = comparator;
        }
        
        @Override
        public Comparator<? super K> getComparator() {
            return this.comparator;
        }
        
        @Override
        protected ObjectSpliterator<K> makeForSplit(final K[] array, final int len) {
            return ObjectSpliterators.wrapPreSorted(array, 0, len, this.characteristics, this.comparator);
        }
    }
    
    private static final class IteratorFromSpliterator<K> implements ObjectIterator<K>, Consumer<K>
    {
        private final ObjectSpliterator<? extends K> spliterator;
        private K holder;
        private boolean hasPeeked;
        
        IteratorFromSpliterator(final ObjectSpliterator<? extends K> spliterator) {
            this.holder = null;
            this.hasPeeked = false;
            this.spliterator = spliterator;
        }
        
        @Override
        public void accept(final K item) {
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
        public K next() {
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
        public void forEachRemaining(final Consumer<? super K> action) {
            if (this.hasPeeked) {
                this.hasPeeked = false;
                action.accept((Object)this.holder);
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
