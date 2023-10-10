// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import java.util.Iterator;
import java.util.Collections;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.collect.Lists;
import me.gong.mcleaks.util.google.common.collect.ImmutableList;
import me.gong.mcleaks.util.google.common.collect.ImmutableCollection;
import me.gong.mcleaks.util.google.common.base.Optional;
import java.util.List;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
abstract class CollectionFuture<V, C> extends AggregateFuture<V, C>
{
    abstract class CollectionFutureRunningState extends RunningState
    {
        private List<Optional<V>> values;
        
        CollectionFutureRunningState(final ImmutableCollection<? extends ListenableFuture<? extends V>> futures, final boolean allMustSucceed) {
            super((ImmutableCollection<? extends ListenableFuture<? extends InputT>>)futures, allMustSucceed, true);
            this.values = (List<Optional<V>>)(futures.isEmpty() ? ImmutableList.of() : Lists.newArrayListWithCapacity(futures.size()));
            for (int i = 0; i < futures.size(); ++i) {
                this.values.add(null);
            }
        }
        
        @Override
        final void collectOneValue(final boolean allMustSucceed, final int index, @Nullable final V returnValue) {
            final List<Optional<V>> localValues = this.values;
            if (localValues != null) {
                localValues.set(index, Optional.fromNullable(returnValue));
            }
            else {
                Preconditions.checkState(allMustSucceed || CollectionFuture.this.isCancelled(), (Object)"Future was done before all dependencies completed");
            }
        }
        
        @Override
        final void handleAllCompleted() {
            final List<Optional<V>> localValues = this.values;
            if (localValues != null) {
                CollectionFuture.this.set(this.combine(localValues));
            }
            else {
                Preconditions.checkState(CollectionFuture.this.isDone());
            }
        }
        
        @Override
        void releaseResourcesAfterFailure() {
            super.releaseResourcesAfterFailure();
            this.values = null;
        }
        
        abstract C combine(final List<Optional<V>> p0);
    }
    
    static final class ListFuture<V> extends CollectionFuture<V, List<V>>
    {
        ListFuture(final ImmutableCollection<? extends ListenableFuture<? extends V>> futures, final boolean allMustSucceed) {
            this.init(new ListFutureRunningState(futures, allMustSucceed));
        }
        
        private final class ListFutureRunningState extends CollectionFutureRunningState
        {
            ListFutureRunningState(final ImmutableCollection<? extends ListenableFuture<? extends V>> futures, final boolean allMustSucceed) {
                super((ImmutableCollection<? extends ListenableFuture<? extends V>>)futures, allMustSucceed);
            }
            
            public List<V> combine(final List<Optional<V>> values) {
                final List<V> result = (List<V>)Lists.newArrayListWithCapacity(values.size());
                for (final Optional<V> element : values) {
                    result.add((element != null) ? element.orNull() : null);
                }
                return Collections.unmodifiableList((List<? extends V>)result);
            }
        }
    }
}
