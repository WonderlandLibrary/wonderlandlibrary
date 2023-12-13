// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import me.gong.mcleaks.util.google.common.math.IntMath;
import java.math.RoundingMode;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.util.Comparator;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
final class TopKSelector<T>
{
    private final int k;
    private final Comparator<? super T> comparator;
    private final T[] buffer;
    private int bufferSize;
    private T threshold;
    
    public static <T extends Comparable<? super T>> TopKSelector<T> least(final int k) {
        return least(k, (Comparator<? super T>)Ordering.natural());
    }
    
    public static <T extends Comparable<? super T>> TopKSelector<T> greatest(final int k) {
        return greatest(k, (Comparator<? super T>)Ordering.natural());
    }
    
    public static <T> TopKSelector<T> least(final int k, final Comparator<? super T> comparator) {
        return new TopKSelector<T>(comparator, k);
    }
    
    public static <T> TopKSelector<T> greatest(final int k, final Comparator<? super T> comparator) {
        return new TopKSelector<T>(Ordering.from(comparator).reverse(), k);
    }
    
    private TopKSelector(final Comparator<? super T> comparator, final int k) {
        this.comparator = Preconditions.checkNotNull(comparator, (Object)"comparator");
        this.k = k;
        Preconditions.checkArgument(k >= 0, "k must be nonnegative, was %s", k);
        this.buffer = (T[])new Object[k * 2];
        this.bufferSize = 0;
        this.threshold = null;
    }
    
    public void offer(@Nullable final T elem) {
        if (this.k == 0) {
            return;
        }
        if (this.bufferSize == 0) {
            this.buffer[0] = elem;
            this.threshold = elem;
            this.bufferSize = 1;
        }
        else if (this.bufferSize < this.k) {
            this.buffer[this.bufferSize++] = elem;
            if (this.comparator.compare((Object)elem, (Object)this.threshold) > 0) {
                this.threshold = elem;
            }
        }
        else if (this.comparator.compare((Object)elem, (Object)this.threshold) < 0) {
            this.buffer[this.bufferSize++] = elem;
            if (this.bufferSize == 2 * this.k) {
                this.trim();
            }
        }
    }
    
    private void trim() {
        int left = 0;
        int right = 2 * this.k - 1;
        int minThresholdPosition = 0;
        int iterations = 0;
        final int maxIterations = IntMath.log2(right - left, RoundingMode.CEILING) * 3;
        while (left < right) {
            final int pivotIndex = left + right + 1 >>> 1;
            final int pivotNewIndex = this.partition(left, right, pivotIndex);
            if (pivotNewIndex > this.k) {
                right = pivotNewIndex - 1;
            }
            else {
                if (pivotNewIndex >= this.k) {
                    break;
                }
                left = Math.max(pivotNewIndex, left + 1);
                minThresholdPosition = pivotNewIndex;
            }
            if (++iterations >= maxIterations) {
                Arrays.sort(this.buffer, left, right, this.comparator);
                break;
            }
        }
        this.bufferSize = this.k;
        this.threshold = this.buffer[minThresholdPosition];
        for (int i = minThresholdPosition + 1; i < this.k; ++i) {
            if (this.comparator.compare((Object)this.buffer[i], (Object)this.threshold) > 0) {
                this.threshold = this.buffer[i];
            }
        }
    }
    
    private int partition(final int left, final int right, final int pivotIndex) {
        final T pivotValue = this.buffer[pivotIndex];
        this.buffer[pivotIndex] = this.buffer[right];
        int pivotNewIndex = left;
        for (int i = left; i < right; ++i) {
            if (this.comparator.compare((Object)this.buffer[i], (Object)pivotValue) < 0) {
                this.swap(pivotNewIndex, i);
                ++pivotNewIndex;
            }
        }
        this.buffer[right] = this.buffer[pivotNewIndex];
        this.buffer[pivotNewIndex] = pivotValue;
        return pivotNewIndex;
    }
    
    private void swap(final int i, final int j) {
        final T tmp = this.buffer[i];
        this.buffer[i] = this.buffer[j];
        this.buffer[j] = tmp;
    }
    
    TopKSelector<T> combine(final TopKSelector<T> other) {
        for (int i = 0; i < other.bufferSize; ++i) {
            this.offer(other.buffer[i]);
        }
        return this;
    }
    
    public void offerAll(final Iterable<? extends T> elements) {
        this.offerAll(elements.iterator());
    }
    
    public void offerAll(final Iterator<? extends T> elements) {
        while (elements.hasNext()) {
            this.offer(elements.next());
        }
    }
    
    public List<T> topK() {
        Arrays.sort(this.buffer, 0, this.bufferSize, this.comparator);
        if (this.bufferSize > this.k) {
            Arrays.fill(this.buffer, this.k, this.buffer.length, null);
            this.bufferSize = this.k;
            this.threshold = this.buffer[this.k - 1];
        }
        return Collections.unmodifiableList((List<? extends T>)Arrays.asList((T[])Arrays.copyOf((T[])this.buffer, this.bufferSize)));
    }
}
