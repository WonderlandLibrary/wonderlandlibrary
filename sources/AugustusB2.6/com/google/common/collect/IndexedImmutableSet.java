// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Iterator;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.function.Consumer;
import java.util.Spliterator;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
abstract class IndexedImmutableSet<E> extends CachingAsList<E>
{
    abstract E get(final int p0);
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return this.asList().iterator();
    }
    
    @Override
    public Spliterator<E> spliterator() {
        return CollectSpliterators.indexed(this.size(), 1297, this::get);
    }
    
    @Override
    public void forEach(final Consumer<? super E> consumer) {
        Preconditions.checkNotNull(consumer);
        for (int n = this.size(), i = 0; i < n; ++i) {
            consumer.accept(this.get(i));
        }
    }
    
    @GwtIncompatible
    @Override
    int copyIntoArray(final Object[] dst, final int offset) {
        return this.asList().copyIntoArray(dst, offset);
    }
    
    @Override
    ImmutableList<E> createAsList() {
        return new ImmutableAsList<E>() {
            @Override
            public E get(final int index) {
                return IndexedImmutableSet.this.get(index);
            }
            
            @Override
            boolean isPartialView() {
                return IndexedImmutableSet.this.isPartialView();
            }
            
            @Override
            public int size() {
                return IndexedImmutableSet.this.size();
            }
            
            @Override
            ImmutableCollection<E> delegateCollection() {
                return (ImmutableCollection<E>)IndexedImmutableSet.this;
            }
        };
    }
}
