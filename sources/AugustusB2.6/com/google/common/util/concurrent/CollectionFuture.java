// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.Collections;
import com.google.common.collect.ImmutableCollection;
import javax.annotation.CheckForNull;
import java.util.List;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
abstract class CollectionFuture<V, C> extends AggregateFuture<V, C>
{
    @CheckForNull
    private List<Present<V>> values;
    
    CollectionFuture(final ImmutableCollection<? extends ListenableFuture<? extends V>> futures, final boolean allMustSucceed) {
        super(futures, allMustSucceed, true);
        final List<Present<V>> values = (List<Present<V>>)(futures.isEmpty() ? Collections.emptyList() : Lists.newArrayListWithCapacity(futures.size()));
        for (int i = 0; i < futures.size(); ++i) {
            values.add(null);
        }
        this.values = values;
    }
    
    @Override
    final void collectOneValue(final int index, @ParametricNullness final V returnValue) {
        final List<Present<V>> localValues = this.values;
        if (localValues != null) {
            localValues.set(index, new Present<V>(returnValue));
        }
    }
    
    @Override
    final void handleAllCompleted() {
        final List<Present<V>> localValues = this.values;
        if (localValues != null) {
            this.set(this.combine(localValues));
        }
    }
    
    @Override
    void releaseResources(final ReleaseResourcesReason reason) {
        super.releaseResources(reason);
        this.values = null;
    }
    
    abstract C combine(final List<Present<V>> p0);
    
    static final class ListFuture<V> extends CollectionFuture<V, List<V>>
    {
        ListFuture(final ImmutableCollection<? extends ListenableFuture<? extends V>> futures, final boolean allMustSucceed) {
            super(futures, allMustSucceed);
            this.init();
        }
        
        public List<V> combine(final List<Present<V>> values) {
            final List<V> result = (List<V>)Lists.newArrayListWithCapacity(values.size());
            for (final Present<V> element : values) {
                result.add((element != null) ? element.value : null);
            }
            return Collections.unmodifiableList((List<? extends V>)result);
        }
    }
    
    private static final class Present<V>
    {
        V value;
        
        Present(final V value) {
            this.value = value;
        }
    }
}
