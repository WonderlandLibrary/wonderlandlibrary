// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import javax.annotation.CheckForNull;
import java.util.Set;
import com.google.common.base.Predicate;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class FilteredKeySetMultimap<K, V> extends FilteredKeyMultimap<K, V> implements FilteredSetMultimap<K, V>
{
    FilteredKeySetMultimap(final SetMultimap<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        super(unfiltered, keyPredicate);
    }
    
    @Override
    public SetMultimap<K, V> unfiltered() {
        return (SetMultimap<K, V>)(SetMultimap)this.unfiltered;
    }
    
    @Override
    public Set<V> get(@ParametricNullness final K key) {
        return (Set<V>)(Set)super.get(key);
    }
    
    @Override
    public Set<V> removeAll(@CheckForNull final Object key) {
        return (Set<V>)(Set)super.removeAll(key);
    }
    
    @Override
    public Set<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
        return (Set<V>)(Set)super.replaceValues(key, values);
    }
    
    @Override
    public Set<Map.Entry<K, V>> entries() {
        return (Set<Map.Entry<K, V>>)(Set)super.entries();
    }
    
    @Override
    Set<Map.Entry<K, V>> createEntries() {
        return new EntrySet();
    }
    
    class EntrySet extends Entries implements Set<Map.Entry<K, V>>
    {
        EntrySet(final FilteredKeySetMultimap this$0) {
            this$0.super();
        }
        
        @Override
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
        
        @Override
        public boolean equals(@CheckForNull final Object o) {
            return Sets.equalsImpl(this, o);
        }
    }
}
