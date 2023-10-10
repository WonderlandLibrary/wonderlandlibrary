/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.Constraint;
import com.google.common.collect.Constraints;
import com.google.common.collect.ForwardingCollection;
import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingMapEntry;
import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MapConstraint;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.SortedSetMultimap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public final class MapConstraints {
    private MapConstraints() {
    }

    public static MapConstraint<Object, Object> notNull() {
        return NotNullMapConstraint.INSTANCE;
    }

    public static <K, V> Map<K, V> constrainedMap(Map<K, V> map, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedMap<K, V>(map, constraint);
    }

    public static <K, V> Multimap<K, V> constrainedMultimap(Multimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedMultimap<K, V>(multimap, constraint);
    }

    public static <K, V> ListMultimap<K, V> constrainedListMultimap(ListMultimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedListMultimap<K, V>(multimap, constraint);
    }

    public static <K, V> SetMultimap<K, V> constrainedSetMultimap(SetMultimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedSetMultimap<K, V>(multimap, constraint);
    }

    public static <K, V> SortedSetMultimap<K, V> constrainedSortedSetMultimap(SortedSetMultimap<K, V> multimap, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedSortedSetMultimap<K, V>(multimap, constraint);
    }

    private static <K, V> Map.Entry<K, V> constrainedEntry(final Map.Entry<K, V> entry, final MapConstraint<? super K, ? super V> constraint) {
        Preconditions.checkNotNull(entry);
        Preconditions.checkNotNull(constraint);
        return new ForwardingMapEntry<K, V>(){

            @Override
            protected Map.Entry<K, V> delegate() {
                return entry;
            }

            @Override
            public V setValue(V value) {
                constraint.checkKeyValue(this.getKey(), value);
                return entry.setValue(value);
            }
        };
    }

    private static <K, V> Map.Entry<K, Collection<V>> constrainedAsMapEntry(final Map.Entry<K, Collection<V>> entry, final MapConstraint<? super K, ? super V> constraint) {
        Preconditions.checkNotNull(entry);
        Preconditions.checkNotNull(constraint);
        return new ForwardingMapEntry<K, Collection<V>>(){

            @Override
            protected Map.Entry<K, Collection<V>> delegate() {
                return entry;
            }

            @Override
            public Collection<V> getValue() {
                return Constraints.constrainedTypePreservingCollection((Collection)entry.getValue(), new Constraint<V>(){

                    @Override
                    public V checkElement(V value) {
                        constraint.checkKeyValue(this.getKey(), value);
                        return value;
                    }
                });
            }
        };
    }

    private static <K, V> Set<Map.Entry<K, Collection<V>>> constrainedAsMapEntries(Set<Map.Entry<K, Collection<V>>> entries, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedAsMapEntries<K, V>(entries, constraint);
    }

    private static <K, V> Collection<Map.Entry<K, V>> constrainedEntries(Collection<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
        if (entries instanceof Set) {
            return MapConstraints.constrainedEntrySet((Set)entries, constraint);
        }
        return new ConstrainedEntries<K, V>(entries, constraint);
    }

    private static <K, V> Set<Map.Entry<K, V>> constrainedEntrySet(Set<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedEntrySet<K, V>(entries, constraint);
    }

    public static <K, V> BiMap<K, V> constrainedBiMap(BiMap<K, V> map, MapConstraint<? super K, ? super V> constraint) {
        return new ConstrainedBiMap<K, V>(map, null, constraint);
    }

    private static <K, V> Collection<V> checkValues(K key, Iterable<? extends V> values, MapConstraint<? super K, ? super V> constraint) {
        ArrayList copy = Lists.newArrayList(values);
        for (Object value : copy) {
            constraint.checkKeyValue(key, value);
        }
        return copy;
    }

    private static <K, V> Map<K, V> checkMap(Map<? extends K, ? extends V> map, MapConstraint<? super K, ? super V> constraint) {
        LinkedHashMap<K, V> copy = new LinkedHashMap<K, V>(map);
        for (Map.Entry entry : copy.entrySet()) {
            constraint.checkKeyValue(entry.getKey(), entry.getValue());
        }
        return copy;
    }

    private static class ConstrainedSortedSetMultimap<K, V>
    extends ConstrainedSetMultimap<K, V>
    implements SortedSetMultimap<K, V> {
        ConstrainedSortedSetMultimap(SortedSetMultimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
            super(delegate, constraint);
        }

        @Override
        public SortedSet<V> get(K key) {
            return (SortedSet)super.get((Object)key);
        }

        @Override
        public SortedSet<V> removeAll(Object key) {
            return (SortedSet)super.removeAll(key);
        }

        @Override
        public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
            return (SortedSet)super.replaceValues((Object)key, (Iterable)values);
        }

        @Override
        public Comparator<? super V> valueComparator() {
            return ((SortedSetMultimap)this.delegate()).valueComparator();
        }
    }

    private static class ConstrainedSetMultimap<K, V>
    extends ConstrainedMultimap<K, V>
    implements SetMultimap<K, V> {
        ConstrainedSetMultimap(SetMultimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
            super(delegate, constraint);
        }

        @Override
        public Set<V> get(K key) {
            return (Set)super.get(key);
        }

        @Override
        public Set<Map.Entry<K, V>> entries() {
            return (Set)super.entries();
        }

        @Override
        public Set<V> removeAll(Object key) {
            return (Set)super.removeAll(key);
        }

        @Override
        public Set<V> replaceValues(K key, Iterable<? extends V> values) {
            return (Set)super.replaceValues(key, values);
        }
    }

    private static class ConstrainedListMultimap<K, V>
    extends ConstrainedMultimap<K, V>
    implements ListMultimap<K, V> {
        ConstrainedListMultimap(ListMultimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
            super(delegate, constraint);
        }

        @Override
        public List<V> get(K key) {
            return (List)super.get(key);
        }

        @Override
        public List<V> removeAll(Object key) {
            return (List)super.removeAll(key);
        }

        @Override
        public List<V> replaceValues(K key, Iterable<? extends V> values) {
            return (List)super.replaceValues(key, values);
        }
    }

    static class ConstrainedAsMapEntries<K, V>
    extends ForwardingSet<Map.Entry<K, Collection<V>>> {
        private final MapConstraint<? super K, ? super V> constraint;
        private final Set<Map.Entry<K, Collection<V>>> entries;

        ConstrainedAsMapEntries(Set<Map.Entry<K, Collection<V>>> entries, MapConstraint<? super K, ? super V> constraint) {
            this.entries = entries;
            this.constraint = constraint;
        }

        @Override
        protected Set<Map.Entry<K, Collection<V>>> delegate() {
            return this.entries;
        }

        @Override
        public Iterator<Map.Entry<K, Collection<V>>> iterator() {
            final Iterator<Map.Entry<K, Collection<V>>> iterator = this.entries.iterator();
            return new ForwardingIterator<Map.Entry<K, Collection<V>>>(){

                @Override
                public Map.Entry<K, Collection<V>> next() {
                    return MapConstraints.constrainedAsMapEntry((Map.Entry)iterator.next(), ConstrainedAsMapEntries.this.constraint);
                }

                @Override
                protected Iterator<Map.Entry<K, Collection<V>>> delegate() {
                    return iterator;
                }
            };
        }

        @Override
        public Object[] toArray() {
            return this.standardToArray();
        }

        @Override
        public <T> T[] toArray(T[] array) {
            return this.standardToArray(array);
        }

        @Override
        public boolean contains(Object o2) {
            return Maps.containsEntryImpl(this.delegate(), o2);
        }

        @Override
        public boolean containsAll(Collection<?> c2) {
            return this.standardContainsAll(c2);
        }

        @Override
        public boolean equals(@Nullable Object object) {
            return this.standardEquals(object);
        }

        @Override
        public int hashCode() {
            return this.standardHashCode();
        }

        @Override
        public boolean remove(Object o2) {
            return Maps.removeEntryImpl(this.delegate(), o2);
        }

        @Override
        public boolean removeAll(Collection<?> c2) {
            return this.standardRemoveAll(c2);
        }

        @Override
        public boolean retainAll(Collection<?> c2) {
            return this.standardRetainAll(c2);
        }
    }

    static class ConstrainedEntrySet<K, V>
    extends ConstrainedEntries<K, V>
    implements Set<Map.Entry<K, V>> {
        ConstrainedEntrySet(Set<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
            super(entries, constraint);
        }

        @Override
        public boolean equals(@Nullable Object object) {
            return Sets.equalsImpl(this, object);
        }

        @Override
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    private static class ConstrainedEntries<K, V>
    extends ForwardingCollection<Map.Entry<K, V>> {
        final MapConstraint<? super K, ? super V> constraint;
        final Collection<Map.Entry<K, V>> entries;

        ConstrainedEntries(Collection<Map.Entry<K, V>> entries, MapConstraint<? super K, ? super V> constraint) {
            this.entries = entries;
            this.constraint = constraint;
        }

        @Override
        protected Collection<Map.Entry<K, V>> delegate() {
            return this.entries;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            final Iterator<Map.Entry<K, V>> iterator = this.entries.iterator();
            return new ForwardingIterator<Map.Entry<K, V>>(){

                @Override
                public Map.Entry<K, V> next() {
                    return MapConstraints.constrainedEntry((Map.Entry)iterator.next(), ConstrainedEntries.this.constraint);
                }

                @Override
                protected Iterator<Map.Entry<K, V>> delegate() {
                    return iterator;
                }
            };
        }

        @Override
        public Object[] toArray() {
            return this.standardToArray();
        }

        @Override
        public <T> T[] toArray(T[] array) {
            return this.standardToArray(array);
        }

        @Override
        public boolean contains(Object o2) {
            return Maps.containsEntryImpl(this.delegate(), o2);
        }

        @Override
        public boolean containsAll(Collection<?> c2) {
            return this.standardContainsAll(c2);
        }

        @Override
        public boolean remove(Object o2) {
            return Maps.removeEntryImpl(this.delegate(), o2);
        }

        @Override
        public boolean removeAll(Collection<?> c2) {
            return this.standardRemoveAll(c2);
        }

        @Override
        public boolean retainAll(Collection<?> c2) {
            return this.standardRetainAll(c2);
        }
    }

    private static class ConstrainedAsMapValues<K, V>
    extends ForwardingCollection<Collection<V>> {
        final Collection<Collection<V>> delegate;
        final Set<Map.Entry<K, Collection<V>>> entrySet;

        ConstrainedAsMapValues(Collection<Collection<V>> delegate, Set<Map.Entry<K, Collection<V>>> entrySet) {
            this.delegate = delegate;
            this.entrySet = entrySet;
        }

        @Override
        protected Collection<Collection<V>> delegate() {
            return this.delegate;
        }

        @Override
        public Iterator<Collection<V>> iterator() {
            final Iterator<Map.Entry<K, Collection<V>>> iterator = this.entrySet.iterator();
            return new Iterator<Collection<V>>(){

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public Collection<V> next() {
                    return (Collection)((Map.Entry)iterator.next()).getValue();
                }

                @Override
                public void remove() {
                    iterator.remove();
                }
            };
        }

        @Override
        public Object[] toArray() {
            return this.standardToArray();
        }

        @Override
        public <T> T[] toArray(T[] array) {
            return this.standardToArray(array);
        }

        @Override
        public boolean contains(Object o2) {
            return this.standardContains(o2);
        }

        @Override
        public boolean containsAll(Collection<?> c2) {
            return this.standardContainsAll(c2);
        }

        @Override
        public boolean remove(Object o2) {
            return this.standardRemove(o2);
        }

        @Override
        public boolean removeAll(Collection<?> c2) {
            return this.standardRemoveAll(c2);
        }

        @Override
        public boolean retainAll(Collection<?> c2) {
            return this.standardRetainAll(c2);
        }
    }

    private static class ConstrainedMultimap<K, V>
    extends ForwardingMultimap<K, V>
    implements Serializable {
        final MapConstraint<? super K, ? super V> constraint;
        final Multimap<K, V> delegate;
        transient Collection<Map.Entry<K, V>> entries;
        transient Map<K, Collection<V>> asMap;

        public ConstrainedMultimap(Multimap<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.constraint = Preconditions.checkNotNull(constraint);
        }

        @Override
        protected Multimap<K, V> delegate() {
            return this.delegate;
        }

        @Override
        public Map<K, Collection<V>> asMap() {
            ForwardingMap result = this.asMap;
            if (result == null) {
                final Map<K, Collection<V>> asMapDelegate = this.delegate.asMap();
                this.asMap = result = new ForwardingMap<K, Collection<V>>(){
                    Set<Map.Entry<K, Collection<V>>> entrySet;
                    Collection<Collection<V>> values;

                    @Override
                    protected Map<K, Collection<V>> delegate() {
                        return asMapDelegate;
                    }

                    @Override
                    public Set<Map.Entry<K, Collection<V>>> entrySet() {
                        Set result = this.entrySet;
                        if (result == null) {
                            this.entrySet = result = MapConstraints.constrainedAsMapEntries(asMapDelegate.entrySet(), ConstrainedMultimap.this.constraint);
                        }
                        return result;
                    }

                    @Override
                    public Collection<V> get(Object key) {
                        try {
                            Collection collection = ConstrainedMultimap.this.get(key);
                            return collection.isEmpty() ? null : collection;
                        }
                        catch (ClassCastException e2) {
                            return null;
                        }
                    }

                    @Override
                    public Collection<Collection<V>> values() {
                        Collection result = this.values;
                        if (result == null) {
                            this.values = result = new ConstrainedAsMapValues(this.delegate().values(), this.entrySet());
                        }
                        return result;
                    }

                    @Override
                    public boolean containsValue(Object o2) {
                        return this.values().contains(o2);
                    }
                };
            }
            return result;
        }

        @Override
        public Collection<Map.Entry<K, V>> entries() {
            Collection result = this.entries;
            if (result == null) {
                this.entries = result = MapConstraints.constrainedEntries(this.delegate.entries(), this.constraint);
            }
            return result;
        }

        @Override
        public Collection<V> get(final K key) {
            return Constraints.constrainedTypePreservingCollection(this.delegate.get(key), new Constraint<V>(){

                @Override
                public V checkElement(V value) {
                    ConstrainedMultimap.this.constraint.checkKeyValue(key, value);
                    return value;
                }
            });
        }

        @Override
        public boolean put(K key, V value) {
            this.constraint.checkKeyValue(key, value);
            return this.delegate.put(key, value);
        }

        @Override
        public boolean putAll(K key, Iterable<? extends V> values) {
            return this.delegate.putAll(key, MapConstraints.checkValues(key, values, this.constraint));
        }

        @Override
        public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
            boolean changed = false;
            for (Map.Entry<K, V> entry : multimap.entries()) {
                changed |= this.put(entry.getKey(), entry.getValue());
            }
            return changed;
        }

        @Override
        public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
            return this.delegate.replaceValues(key, MapConstraints.checkValues(key, values, this.constraint));
        }
    }

    private static class InverseConstraint<K, V>
    implements MapConstraint<K, V> {
        final MapConstraint<? super V, ? super K> constraint;

        public InverseConstraint(MapConstraint<? super V, ? super K> constraint) {
            this.constraint = Preconditions.checkNotNull(constraint);
        }

        @Override
        public void checkKeyValue(K key, V value) {
            this.constraint.checkKeyValue(value, key);
        }
    }

    private static class ConstrainedBiMap<K, V>
    extends ConstrainedMap<K, V>
    implements BiMap<K, V> {
        volatile BiMap<V, K> inverse;

        ConstrainedBiMap(BiMap<K, V> delegate, @Nullable BiMap<V, K> inverse, MapConstraint<? super K, ? super V> constraint) {
            super(delegate, constraint);
            this.inverse = inverse;
        }

        @Override
        protected BiMap<K, V> delegate() {
            return (BiMap)super.delegate();
        }

        @Override
        public V forcePut(K key, V value) {
            this.constraint.checkKeyValue(key, value);
            return this.delegate().forcePut(key, value);
        }

        @Override
        public BiMap<V, K> inverse() {
            if (this.inverse == null) {
                this.inverse = new ConstrainedBiMap<K, V>(this.delegate().inverse(), this, new InverseConstraint(this.constraint));
            }
            return this.inverse;
        }

        @Override
        public Set<V> values() {
            return this.delegate().values();
        }
    }

    static class ConstrainedMap<K, V>
    extends ForwardingMap<K, V> {
        private final Map<K, V> delegate;
        final MapConstraint<? super K, ? super V> constraint;
        private transient Set<Map.Entry<K, V>> entrySet;

        ConstrainedMap(Map<K, V> delegate, MapConstraint<? super K, ? super V> constraint) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.constraint = Preconditions.checkNotNull(constraint);
        }

        @Override
        protected Map<K, V> delegate() {
            return this.delegate;
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            Set result = this.entrySet;
            if (result == null) {
                this.entrySet = result = MapConstraints.constrainedEntrySet(this.delegate.entrySet(), this.constraint);
            }
            return result;
        }

        @Override
        public V put(K key, V value) {
            this.constraint.checkKeyValue(key, value);
            return this.delegate.put(key, value);
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> map) {
            this.delegate.putAll(MapConstraints.checkMap(map, this.constraint));
        }
    }

    private static enum NotNullMapConstraint implements MapConstraint<Object, Object>
    {
        INSTANCE;


        @Override
        public void checkKeyValue(Object key, Object value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
        }

        @Override
        public String toString() {
            return "Not null";
        }
    }
}

