// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.StreamSupport;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import com.viaversion.viaversion.libs.fastutil.Size64;
import java.util.Collection;

public interface IntCollection extends Collection<Integer>, IntIterable
{
    IntIterator iterator();
    
    default IntIterator intIterator() {
        return this.iterator();
    }
    
    default IntSpliterator spliterator() {
        return IntSpliterators.asSpliterator(this.iterator(), Size64.sizeOf(this), 320);
    }
    
    default IntSpliterator intSpliterator() {
        return this.spliterator();
    }
    
    boolean add(final int p0);
    
    boolean contains(final int p0);
    
    boolean rem(final int p0);
    
    @Deprecated
    default boolean add(final Integer key) {
        return this.add((int)key);
    }
    
    @Deprecated
    default boolean contains(final Object key) {
        return key != null && this.contains((int)key);
    }
    
    @Deprecated
    default boolean remove(final Object key) {
        return key != null && this.rem((int)key);
    }
    
    int[] toIntArray();
    
    @Deprecated
    default int[] toIntArray(final int[] a) {
        return this.toArray(a);
    }
    
    int[] toArray(final int[] p0);
    
    boolean addAll(final IntCollection p0);
    
    boolean containsAll(final IntCollection p0);
    
    boolean removeAll(final IntCollection p0);
    
    @Deprecated
    default boolean removeIf(final Predicate<? super Integer> filter) {
        return this.removeIf((filter instanceof IntPredicate) ? ((IntPredicate)filter) : (key -> filter.test(key)));
    }
    
    default boolean removeIf(final IntPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final IntIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextInt())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
    
    default boolean removeIf(final com.viaversion.viaversion.libs.fastutil.ints.IntPredicate filter) {
        return this.removeIf((IntPredicate)filter);
    }
    
    boolean retainAll(final IntCollection p0);
    
    @Deprecated
    default Stream<Integer> stream() {
        return super.stream();
    }
    
    default IntStream intStream() {
        return StreamSupport.intStream(this.intSpliterator(), false);
    }
    
    @Deprecated
    default Stream<Integer> parallelStream() {
        return super.parallelStream();
    }
    
    default IntStream intParallelStream() {
        return StreamSupport.intStream(this.intSpliterator(), true);
    }
}
