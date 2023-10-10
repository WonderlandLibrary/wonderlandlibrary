// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Objects;
import java.util.AbstractCollection;
import java.util.function.Consumer;
import java.util.Spliterator;
import com.google.j2objc.annotations.Weak;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Comparator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NavigableSet;
import com.google.common.annotations.GwtIncompatible;
import javax.annotation.CheckForNull;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;
import java.util.Collections;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.SortedSet;
import java.util.Set;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import com.google.common.annotations.Beta;
import java.util.stream.Stream;
import java.util.stream.Collector;
import java.util.function.Supplier;
import java.util.function.Function;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class Multimaps
{
    private Multimaps() {
    }
    
    public static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> toMultimap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends V> valueFunction, final Supplier<M> multimapSupplier) {
        return CollectCollectors.toMultimap((Function<? super T, ?>)keyFunction, (Function<? super T, ?>)valueFunction, multimapSupplier);
    }
    
    @Beta
    public static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> flatteningToMultimap(final Function<? super T, ? extends K> keyFunction, final Function<? super T, ? extends Stream<? extends V>> valueFunction, final Supplier<M> multimapSupplier) {
        return CollectCollectors.flatteningToMultimap((Function<? super T, ?>)keyFunction, (Function<? super T, ? extends Stream<?>>)valueFunction, multimapSupplier);
    }
    
    public static <K, V> Multimap<K, V> newMultimap(final Map<K, Collection<V>> map, final com.google.common.base.Supplier<? extends Collection<V>> factory) {
        return new CustomMultimap<K, V>(map, factory);
    }
    
    public static <K, V> ListMultimap<K, V> newListMultimap(final Map<K, Collection<V>> map, final com.google.common.base.Supplier<? extends List<V>> factory) {
        return new CustomListMultimap<K, V>(map, factory);
    }
    
    public static <K, V> SetMultimap<K, V> newSetMultimap(final Map<K, Collection<V>> map, final com.google.common.base.Supplier<? extends Set<V>> factory) {
        return new CustomSetMultimap<K, V>(map, factory);
    }
    
    public static <K, V> SortedSetMultimap<K, V> newSortedSetMultimap(final Map<K, Collection<V>> map, final com.google.common.base.Supplier<? extends SortedSet<V>> factory) {
        return new CustomSortedSetMultimap<K, V>(map, factory);
    }
    
    @CanIgnoreReturnValue
    public static <K, V, M extends Multimap<K, V>> M invertFrom(final Multimap<? extends V, ? extends K> source, final M dest) {
        Preconditions.checkNotNull(dest);
        for (final Map.Entry<? extends V, ? extends K> entry : source.entries()) {
            dest.put((K)entry.getValue(), (V)entry.getKey());
        }
        return dest;
    }
    
    public static <K, V> Multimap<K, V> synchronizedMultimap(final Multimap<K, V> multimap) {
        return Synchronized.multimap(multimap, null);
    }
    
    public static <K, V> Multimap<K, V> unmodifiableMultimap(final Multimap<K, V> delegate) {
        if (delegate instanceof UnmodifiableMultimap || delegate instanceof ImmutableMultimap) {
            return delegate;
        }
        return new UnmodifiableMultimap<K, V>(delegate);
    }
    
    @Deprecated
    public static <K, V> Multimap<K, V> unmodifiableMultimap(final ImmutableMultimap<K, V> delegate) {
        return Preconditions.checkNotNull(delegate);
    }
    
    public static <K, V> SetMultimap<K, V> synchronizedSetMultimap(final SetMultimap<K, V> multimap) {
        return Synchronized.setMultimap(multimap, null);
    }
    
    public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(final SetMultimap<K, V> delegate) {
        if (delegate instanceof UnmodifiableSetMultimap || delegate instanceof ImmutableSetMultimap) {
            return delegate;
        }
        return new UnmodifiableSetMultimap<K, V>(delegate);
    }
    
    @Deprecated
    public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(final ImmutableSetMultimap<K, V> delegate) {
        return Preconditions.checkNotNull(delegate);
    }
    
    public static <K, V> SortedSetMultimap<K, V> synchronizedSortedSetMultimap(final SortedSetMultimap<K, V> multimap) {
        return Synchronized.sortedSetMultimap(multimap, null);
    }
    
    public static <K, V> SortedSetMultimap<K, V> unmodifiableSortedSetMultimap(final SortedSetMultimap<K, V> delegate) {
        if (delegate instanceof UnmodifiableSortedSetMultimap) {
            return delegate;
        }
        return new UnmodifiableSortedSetMultimap<K, V>(delegate);
    }
    
    public static <K, V> ListMultimap<K, V> synchronizedListMultimap(final ListMultimap<K, V> multimap) {
        return Synchronized.listMultimap(multimap, null);
    }
    
    public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(final ListMultimap<K, V> delegate) {
        if (delegate instanceof UnmodifiableListMultimap || delegate instanceof ImmutableListMultimap) {
            return delegate;
        }
        return new UnmodifiableListMultimap<K, V>(delegate);
    }
    
    @Deprecated
    public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(final ImmutableListMultimap<K, V> delegate) {
        return Preconditions.checkNotNull(delegate);
    }
    
    private static <V> Collection<V> unmodifiableValueCollection(final Collection<V> collection) {
        if (collection instanceof SortedSet) {
            return (Collection<V>)Collections.unmodifiableSortedSet((SortedSet<Object>)(SortedSet)collection);
        }
        if (collection instanceof Set) {
            return (Collection<V>)Collections.unmodifiableSet((Set<?>)(Set)collection);
        }
        if (collection instanceof List) {
            return (Collection<V>)Collections.unmodifiableList((List<?>)(List)collection);
        }
        return Collections.unmodifiableCollection((Collection<? extends V>)collection);
    }
    
    private static <K, V> Collection<Map.Entry<K, V>> unmodifiableEntries(final Collection<Map.Entry<K, V>> entries) {
        if (entries instanceof Set) {
            return (Collection<Map.Entry<K, V>>)Maps.unmodifiableEntrySet((Set<Map.Entry<Object, Object>>)(Set)entries);
        }
        return (Collection<Map.Entry<K, V>>)new Maps.UnmodifiableEntries(Collections.unmodifiableCollection((Collection<? extends Map.Entry<Object, Object>>)entries));
    }
    
    @Beta
    public static <K, V> Map<K, List<V>> asMap(final ListMultimap<K, V> multimap) {
        return (Map<K, List<V>>)multimap.asMap();
    }
    
    @Beta
    public static <K, V> Map<K, Set<V>> asMap(final SetMultimap<K, V> multimap) {
        return (Map<K, Set<V>>)multimap.asMap();
    }
    
    @Beta
    public static <K, V> Map<K, SortedSet<V>> asMap(final SortedSetMultimap<K, V> multimap) {
        return (Map<K, SortedSet<V>>)multimap.asMap();
    }
    
    @Beta
    public static <K, V> Map<K, Collection<V>> asMap(final Multimap<K, V> multimap) {
        return multimap.asMap();
    }
    
    public static <K, V> SetMultimap<K, V> forMap(final Map<K, V> map) {
        return new MapMultimap<K, V>(map);
    }
    
    public static <K, V1, V2> Multimap<K, V2> transformValues(final Multimap<K, V1> fromMultimap, final com.google.common.base.Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        final Maps.EntryTransformer<K, V1, V2> transformer = Maps.asEntryTransformer(function);
        return transformEntries(fromMultimap, transformer);
    }
    
    public static <K, V1, V2> ListMultimap<K, V2> transformValues(final ListMultimap<K, V1> fromMultimap, final com.google.common.base.Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        final Maps.EntryTransformer<K, V1, V2> transformer = Maps.asEntryTransformer(function);
        return transformEntries(fromMultimap, transformer);
    }
    
    public static <K, V1, V2> Multimap<K, V2> transformEntries(final Multimap<K, V1> fromMap, final Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
        return (Multimap<K, V2>)new TransformedEntriesMultimap((Multimap<Object, Object>)fromMap, (Maps.EntryTransformer<? super Object, ? super Object, Object>)transformer);
    }
    
    public static <K, V1, V2> ListMultimap<K, V2> transformEntries(final ListMultimap<K, V1> fromMap, final Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
        return new TransformedEntriesListMultimap<K, Object, V2>(fromMap, transformer);
    }
    
    public static <K, V> ImmutableListMultimap<K, V> index(final Iterable<V> values, final com.google.common.base.Function<? super V, K> keyFunction) {
        return index(values.iterator(), keyFunction);
    }
    
    public static <K, V> ImmutableListMultimap<K, V> index(final Iterator<V> values, final com.google.common.base.Function<? super V, K> keyFunction) {
        Preconditions.checkNotNull(keyFunction);
        final ImmutableListMultimap.Builder<K, V> builder = ImmutableListMultimap.builder();
        while (values.hasNext()) {
            final V value = values.next();
            Preconditions.checkNotNull(value, values);
            builder.put(keyFunction.apply((Object)value), value);
        }
        return builder.build();
    }
    
    public static <K, V> Multimap<K, V> filterKeys(final Multimap<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        if (unfiltered instanceof SetMultimap) {
            return (Multimap<K, V>)filterKeys((SetMultimap<Object, Object>)(SetMultimap)unfiltered, (Predicate<? super Object>)keyPredicate);
        }
        if (unfiltered instanceof ListMultimap) {
            return (Multimap<K, V>)filterKeys((ListMultimap<Object, Object>)(ListMultimap)unfiltered, (Predicate<? super Object>)keyPredicate);
        }
        if (unfiltered instanceof FilteredKeyMultimap) {
            final FilteredKeyMultimap<K, V> prev = (FilteredKeyMultimap<K, V>)(FilteredKeyMultimap)unfiltered;
            return new FilteredKeyMultimap<K, V>(prev.unfiltered, Predicates.and(prev.keyPredicate, keyPredicate));
        }
        if (unfiltered instanceof FilteredMultimap) {
            final FilteredMultimap<K, V> prev2 = (FilteredMultimap<K, V>)(FilteredMultimap)unfiltered;
            return filterFiltered(prev2, (Predicate<? super Map.Entry<K, V>>)Maps.keyPredicateOnEntries((Predicate<? super Object>)keyPredicate));
        }
        return new FilteredKeyMultimap<K, V>(unfiltered, keyPredicate);
    }
    
    public static <K, V> SetMultimap<K, V> filterKeys(final SetMultimap<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        if (unfiltered instanceof FilteredKeySetMultimap) {
            final FilteredKeySetMultimap<K, V> prev = (FilteredKeySetMultimap<K, V>)(FilteredKeySetMultimap)unfiltered;
            return new FilteredKeySetMultimap<K, V>(prev.unfiltered(), Predicates.and(prev.keyPredicate, keyPredicate));
        }
        if (unfiltered instanceof FilteredSetMultimap) {
            final FilteredSetMultimap<K, V> prev2 = (FilteredSetMultimap<K, V>)(FilteredSetMultimap)unfiltered;
            return filterFiltered(prev2, (Predicate<? super Map.Entry<K, V>>)Maps.keyPredicateOnEntries((Predicate<? super Object>)keyPredicate));
        }
        return new FilteredKeySetMultimap<K, V>(unfiltered, keyPredicate);
    }
    
    public static <K, V> ListMultimap<K, V> filterKeys(final ListMultimap<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        if (unfiltered instanceof FilteredKeyListMultimap) {
            final FilteredKeyListMultimap<K, V> prev = (FilteredKeyListMultimap<K, V>)(FilteredKeyListMultimap)unfiltered;
            return new FilteredKeyListMultimap<K, V>(prev.unfiltered(), Predicates.and(prev.keyPredicate, keyPredicate));
        }
        return new FilteredKeyListMultimap<K, V>(unfiltered, keyPredicate);
    }
    
    public static <K, V> Multimap<K, V> filterValues(final Multimap<K, V> unfiltered, final Predicate<? super V> valuePredicate) {
        return (Multimap<K, V>)filterEntries((Multimap<Object, Object>)unfiltered, Maps.valuePredicateOnEntries((Predicate<? super Object>)valuePredicate));
    }
    
    public static <K, V> SetMultimap<K, V> filterValues(final SetMultimap<K, V> unfiltered, final Predicate<? super V> valuePredicate) {
        return (SetMultimap<K, V>)filterEntries((SetMultimap<Object, Object>)unfiltered, Maps.valuePredicateOnEntries((Predicate<? super Object>)valuePredicate));
    }
    
    public static <K, V> Multimap<K, V> filterEntries(final Multimap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(entryPredicate);
        if (unfiltered instanceof SetMultimap) {
            return (Multimap<K, V>)filterEntries((SetMultimap<Object, Object>)(SetMultimap)unfiltered, (Predicate<? super Map.Entry<Object, Object>>)entryPredicate);
        }
        return (unfiltered instanceof FilteredMultimap) ? filterFiltered((FilteredMultimap<K, V>)(FilteredMultimap)unfiltered, entryPredicate) : new FilteredEntryMultimap<K, V>(Preconditions.checkNotNull(unfiltered), entryPredicate);
    }
    
    public static <K, V> SetMultimap<K, V> filterEntries(final SetMultimap<K, V> unfiltered, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(entryPredicate);
        return (unfiltered instanceof FilteredSetMultimap) ? filterFiltered((FilteredSetMultimap<K, V>)(FilteredSetMultimap)unfiltered, entryPredicate) : new FilteredEntrySetMultimap<K, V>(Preconditions.checkNotNull(unfiltered), entryPredicate);
    }
    
    private static <K, V> Multimap<K, V> filterFiltered(final FilteredMultimap<K, V> multimap, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        final Predicate<Map.Entry<K, V>> predicate = Predicates.and(multimap.entryPredicate(), entryPredicate);
        return new FilteredEntryMultimap<K, V>(multimap.unfiltered(), predicate);
    }
    
    private static <K, V> SetMultimap<K, V> filterFiltered(final FilteredSetMultimap<K, V> multimap, final Predicate<? super Map.Entry<K, V>> entryPredicate) {
        final Predicate<Map.Entry<K, V>> predicate = Predicates.and((Predicate<? super Map.Entry<K, V>>)multimap.entryPredicate(), entryPredicate);
        return new FilteredEntrySetMultimap<K, V>(multimap.unfiltered(), predicate);
    }
    
    static boolean equalsImpl(final Multimap<?, ?> multimap, @CheckForNull final Object object) {
        if (object == multimap) {
            return true;
        }
        if (object instanceof Multimap) {
            final Multimap<?, ?> that = (Multimap<?, ?>)object;
            return multimap.asMap().equals(that.asMap());
        }
        return false;
    }
    
    private static class CustomMultimap<K, V> extends AbstractMapBasedMultimap<K, V>
    {
        transient com.google.common.base.Supplier<? extends Collection<V>> factory;
        @GwtIncompatible
        private static final long serialVersionUID = 0L;
        
        CustomMultimap(final Map<K, Collection<V>> map, final com.google.common.base.Supplier<? extends Collection<V>> factory) {
            super(map);
            this.factory = Preconditions.checkNotNull(factory);
        }
        
        @Override
        Set<K> createKeySet() {
            return this.createMaybeNavigableKeySet();
        }
        
        @Override
        Map<K, Collection<V>> createAsMap() {
            return this.createMaybeNavigableAsMap();
        }
        
        protected Collection<V> createCollection() {
            return (Collection<V>)this.factory.get();
        }
        
        @Override
         <E> Collection<E> unmodifiableCollectionSubclass(final Collection<E> collection) {
            if (collection instanceof NavigableSet) {
                return (Collection<E>)Sets.unmodifiableNavigableSet((NavigableSet<Object>)(NavigableSet)collection);
            }
            if (collection instanceof SortedSet) {
                return (Collection<E>)Collections.unmodifiableSortedSet((SortedSet<Object>)(SortedSet)collection);
            }
            if (collection instanceof Set) {
                return (Collection<E>)Collections.unmodifiableSet((Set<?>)(Set)collection);
            }
            if (collection instanceof List) {
                return (Collection<E>)Collections.unmodifiableList((List<?>)(List)collection);
            }
            return Collections.unmodifiableCollection((Collection<? extends E>)collection);
        }
        
        @Override
        Collection<V> wrapCollection(@ParametricNullness final K key, final Collection<V> collection) {
            if (collection instanceof List) {
                return this.wrapList(key, (List)collection, null);
            }
            if (collection instanceof NavigableSet) {
                return (Collection<V>)new WrappedNavigableSet((K)key, (NavigableSet)collection, null);
            }
            if (collection instanceof SortedSet) {
                return (Collection<V>)new WrappedSortedSet((K)key, (SortedSet)collection, null);
            }
            if (collection instanceof Set) {
                return (Collection<V>)new WrappedSet((K)key, (Set)collection);
            }
            return (Collection<V>)new WrappedCollection((K)key, (Collection<V>)collection, null);
        }
        
        @GwtIncompatible
        private void writeObject(final ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(this.backingMap());
        }
        
        @GwtIncompatible
        private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (com.google.common.base.Supplier<? extends Collection<V>>)stream.readObject();
            final Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
            this.setMap(map);
        }
    }
    
    private static class CustomListMultimap<K, V> extends AbstractListMultimap<K, V>
    {
        transient com.google.common.base.Supplier<? extends List<V>> factory;
        @GwtIncompatible
        private static final long serialVersionUID = 0L;
        
        CustomListMultimap(final Map<K, Collection<V>> map, final com.google.common.base.Supplier<? extends List<V>> factory) {
            super(map);
            this.factory = Preconditions.checkNotNull(factory);
        }
        
        @Override
        Set<K> createKeySet() {
            return this.createMaybeNavigableKeySet();
        }
        
        @Override
        Map<K, Collection<V>> createAsMap() {
            return this.createMaybeNavigableAsMap();
        }
        
        protected List<V> createCollection() {
            return (List<V>)this.factory.get();
        }
        
        @GwtIncompatible
        private void writeObject(final ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(this.backingMap());
        }
        
        @GwtIncompatible
        private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (com.google.common.base.Supplier<? extends List<V>>)stream.readObject();
            final Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
            this.setMap(map);
        }
    }
    
    private static class CustomSetMultimap<K, V> extends AbstractSetMultimap<K, V>
    {
        transient com.google.common.base.Supplier<? extends Set<V>> factory;
        @GwtIncompatible
        private static final long serialVersionUID = 0L;
        
        CustomSetMultimap(final Map<K, Collection<V>> map, final com.google.common.base.Supplier<? extends Set<V>> factory) {
            super(map);
            this.factory = Preconditions.checkNotNull(factory);
        }
        
        @Override
        Set<K> createKeySet() {
            return this.createMaybeNavigableKeySet();
        }
        
        @Override
        Map<K, Collection<V>> createAsMap() {
            return this.createMaybeNavigableAsMap();
        }
        
        protected Set<V> createCollection() {
            return (Set<V>)this.factory.get();
        }
        
        @Override
         <E> Collection<E> unmodifiableCollectionSubclass(final Collection<E> collection) {
            if (collection instanceof NavigableSet) {
                return (Collection<E>)Sets.unmodifiableNavigableSet((NavigableSet<Object>)(NavigableSet)collection);
            }
            if (collection instanceof SortedSet) {
                return (Collection<E>)Collections.unmodifiableSortedSet((SortedSet<Object>)(SortedSet)collection);
            }
            return (Collection<E>)Collections.unmodifiableSet((Set<?>)(Set)collection);
        }
        
        @Override
        Collection<V> wrapCollection(@ParametricNullness final K key, final Collection<V> collection) {
            if (collection instanceof NavigableSet) {
                return (Collection<V>)new WrappedNavigableSet((K)key, (NavigableSet)collection, null);
            }
            if (collection instanceof SortedSet) {
                return (Collection<V>)new WrappedSortedSet((K)key, (SortedSet)collection, null);
            }
            return (Collection<V>)new WrappedSet((K)key, (Set)collection);
        }
        
        @GwtIncompatible
        private void writeObject(final ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(this.backingMap());
        }
        
        @GwtIncompatible
        private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (com.google.common.base.Supplier<? extends Set<V>>)stream.readObject();
            final Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
            this.setMap(map);
        }
    }
    
    private static class CustomSortedSetMultimap<K, V> extends AbstractSortedSetMultimap<K, V>
    {
        transient com.google.common.base.Supplier<? extends SortedSet<V>> factory;
        @CheckForNull
        transient Comparator<? super V> valueComparator;
        @GwtIncompatible
        private static final long serialVersionUID = 0L;
        
        CustomSortedSetMultimap(final Map<K, Collection<V>> map, final com.google.common.base.Supplier<? extends SortedSet<V>> factory) {
            super(map);
            this.factory = Preconditions.checkNotNull(factory);
            this.valueComparator = (Comparator<? super V>)((SortedSet)factory.get()).comparator();
        }
        
        @Override
        Set<K> createKeySet() {
            return this.createMaybeNavigableKeySet();
        }
        
        @Override
        Map<K, Collection<V>> createAsMap() {
            return this.createMaybeNavigableAsMap();
        }
        
        protected SortedSet<V> createCollection() {
            return (SortedSet<V>)this.factory.get();
        }
        
        @CheckForNull
        @Override
        public Comparator<? super V> valueComparator() {
            return this.valueComparator;
        }
        
        @GwtIncompatible
        private void writeObject(final ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(this.backingMap());
        }
        
        @GwtIncompatible
        private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (com.google.common.base.Supplier<? extends SortedSet<V>>)stream.readObject();
            this.valueComparator = (Comparator<? super V>)((SortedSet)this.factory.get()).comparator();
            final Map<K, Collection<V>> map = (Map<K, Collection<V>>)stream.readObject();
            this.setMap(map);
        }
    }
    
    private static class UnmodifiableMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable
    {
        final Multimap<K, V> delegate;
        @LazyInit
        @CheckForNull
        transient Collection<Map.Entry<K, V>> entries;
        @LazyInit
        @CheckForNull
        transient Multiset<K> keys;
        @LazyInit
        @CheckForNull
        transient Set<K> keySet;
        @LazyInit
        @CheckForNull
        transient Collection<V> values;
        @LazyInit
        @CheckForNull
        transient Map<K, Collection<V>> map;
        private static final long serialVersionUID = 0L;
        
        UnmodifiableMultimap(final Multimap<K, V> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate);
        }
        
        @Override
        protected Multimap<K, V> delegate() {
            return this.delegate;
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Map<K, Collection<V>> asMap() {
            Map<K, Collection<V>> result = this.map;
            if (result == null) {
                final Map<Object, Object> unmodifiableMap = Collections.unmodifiableMap((Map<?, ?>)Maps.transformValues((Map<? extends K, Collection<V>>)this.delegate.asMap(), (com.google.common.base.Function<? super Collection<V>, ? extends V>)new com.google.common.base.Function<Collection<V>, Collection<V>>(this) {
                    @Override
                    public Collection<V> apply(final Collection<V> collection) {
                        return (Collection<V>)unmodifiableValueCollection((Collection<Object>)collection);
                    }
                }));
                this.map = (Map<K, Collection<V>>)unmodifiableMap;
                result = (Map<K, Collection<V>>)unmodifiableMap;
            }
            return result;
        }
        
        @Override
        public Collection<Map.Entry<K, V>> entries() {
            Collection<Map.Entry<K, V>> result = this.entries;
            if (result == null) {
                result = (this.entries = (Collection<Map.Entry<K, V>>)unmodifiableEntries((Collection<Map.Entry<Object, Object>>)this.delegate.entries()));
            }
            return result;
        }
        
        @Override
        public void forEach(final BiConsumer<? super K, ? super V> consumer) {
            this.delegate.forEach(Preconditions.checkNotNull(consumer));
        }
        
        @Override
        public Collection<V> get(@ParametricNullness final K key) {
            return (Collection<V>)unmodifiableValueCollection((Collection<Object>)this.delegate.get(key));
        }
        
        @Override
        public Multiset<K> keys() {
            Multiset<K> result = this.keys;
            if (result == null) {
                result = (this.keys = Multisets.unmodifiableMultiset((Multiset<? extends K>)this.delegate.keys()));
            }
            return result;
        }
        
        @Override
        public Set<K> keySet() {
            Set<K> result = this.keySet;
            if (result == null) {
                result = (this.keySet = Collections.unmodifiableSet((Set<? extends K>)this.delegate.keySet()));
            }
            return result;
        }
        
        @Override
        public boolean put(@ParametricNullness final K key, @ParametricNullness final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean putAll(@ParametricNullness final K key, final Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean putAll(final Multimap<? extends K, ? extends V> multimap) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(@CheckForNull final Object key, @CheckForNull final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Collection<V> removeAll(@CheckForNull final Object key) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Collection<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Collection<V> values() {
            Collection<V> result = this.values;
            if (result == null) {
                result = (this.values = Collections.unmodifiableCollection((Collection<? extends V>)this.delegate.values()));
            }
            return result;
        }
    }
    
    private static class UnmodifiableListMultimap<K, V> extends UnmodifiableMultimap<K, V> implements ListMultimap<K, V>
    {
        private static final long serialVersionUID = 0L;
        
        UnmodifiableListMultimap(final ListMultimap<K, V> delegate) {
            super(delegate);
        }
        
        public ListMultimap<K, V> delegate() {
            return (ListMultimap<K, V>)(ListMultimap)super.delegate();
        }
        
        @Override
        public List<V> get(@ParametricNullness final K key) {
            return Collections.unmodifiableList((List<? extends V>)this.delegate().get(key));
        }
        
        @Override
        public List<V> removeAll(@CheckForNull final Object key) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public List<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }
    }
    
    private static class UnmodifiableSetMultimap<K, V> extends UnmodifiableMultimap<K, V> implements SetMultimap<K, V>
    {
        private static final long serialVersionUID = 0L;
        
        UnmodifiableSetMultimap(final SetMultimap<K, V> delegate) {
            super(delegate);
        }
        
        public SetMultimap<K, V> delegate() {
            return (SetMultimap<K, V>)(SetMultimap)super.delegate();
        }
        
        @Override
        public Set<V> get(@ParametricNullness final K key) {
            return Collections.unmodifiableSet((Set<? extends V>)this.delegate().get(key));
        }
        
        @Override
        public Set<Map.Entry<K, V>> entries() {
            return Maps.unmodifiableEntrySet(this.delegate().entries());
        }
        
        @Override
        public Set<V> removeAll(@CheckForNull final Object key) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Set<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }
    }
    
    private static class UnmodifiableSortedSetMultimap<K, V> extends UnmodifiableSetMultimap<K, V> implements SortedSetMultimap<K, V>
    {
        private static final long serialVersionUID = 0L;
        
        UnmodifiableSortedSetMultimap(final SortedSetMultimap<K, V> delegate) {
            super(delegate);
        }
        
        @Override
        public SortedSetMultimap<K, V> delegate() {
            return (SortedSetMultimap<K, V>)(SortedSetMultimap)super.delegate();
        }
        
        @Override
        public SortedSet<V> get(@ParametricNullness final K key) {
            return Collections.unmodifiableSortedSet(this.delegate().get(key));
        }
        
        @Override
        public SortedSet<V> removeAll(@CheckForNull final Object key) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public SortedSet<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }
        
        @CheckForNull
        @Override
        public Comparator<? super V> valueComparator() {
            return this.delegate().valueComparator();
        }
    }
    
    private static class MapMultimap<K, V> extends AbstractMultimap<K, V> implements SetMultimap<K, V>, Serializable
    {
        final Map<K, V> map;
        private static final long serialVersionUID = 7845222491160860175L;
        
        MapMultimap(final Map<K, V> map) {
            this.map = Preconditions.checkNotNull(map);
        }
        
        @Override
        public int size() {
            return this.map.size();
        }
        
        @Override
        public boolean containsKey(@CheckForNull final Object key) {
            return this.map.containsKey(key);
        }
        
        @Override
        public boolean containsValue(@CheckForNull final Object value) {
            return this.map.containsValue(value);
        }
        
        @Override
        public boolean containsEntry(@CheckForNull final Object key, @CheckForNull final Object value) {
            return this.map.entrySet().contains(Maps.immutableEntry(key, value));
        }
        
        @Override
        public Set<V> get(@ParametricNullness final K key) {
            return new Sets.ImprovedAbstractSet<V>() {
                @Override
                public Iterator<V> iterator() {
                    return new Iterator<V>() {
                        int i;
                        
                        @Override
                        public boolean hasNext() {
                            return this.i == 0 && MapMultimap.this.map.containsKey(key);
                        }
                        
                        @ParametricNullness
                        @Override
                        public V next() {
                            if (!this.hasNext()) {
                                throw new NoSuchElementException();
                            }
                            ++this.i;
                            return NullnessCasts.uncheckedCastNullableTToT(MapMultimap.this.map.get(key));
                        }
                        
                        @Override
                        public void remove() {
                            CollectPreconditions.checkRemove(this.i == 1);
                            this.i = -1;
                            MapMultimap.this.map.remove(key);
                        }
                    };
                }
                
                @Override
                public int size() {
                    return MapMultimap.this.map.containsKey(key) ? 1 : 0;
                }
            };
        }
        
        @Override
        public boolean put(@ParametricNullness final K key, @ParametricNullness final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean putAll(@ParametricNullness final K key, final Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean putAll(final Multimap<? extends K, ? extends V> multimap) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Set<V> replaceValues(@ParametricNullness final K key, final Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(@CheckForNull final Object key, @CheckForNull final Object value) {
            return this.map.entrySet().remove(Maps.immutableEntry(key, value));
        }
        
        @Override
        public Set<V> removeAll(@CheckForNull final Object key) {
            final Set<V> values = new HashSet<V>(2);
            if (!this.map.containsKey(key)) {
                return values;
            }
            values.add(this.map.remove(key));
            return values;
        }
        
        @Override
        public void clear() {
            this.map.clear();
        }
        
        @Override
        Set<K> createKeySet() {
            return this.map.keySet();
        }
        
        @Override
        Collection<V> createValues() {
            return this.map.values();
        }
        
        @Override
        public Set<Map.Entry<K, V>> entries() {
            return this.map.entrySet();
        }
        
        @Override
        Collection<Map.Entry<K, V>> createEntries() {
            throw new AssertionError((Object)"unreachable");
        }
        
        @Override
        Multiset<K> createKeys() {
            return (Multiset<K>)new Keys((Multimap<Object, Object>)this);
        }
        
        @Override
        Iterator<Map.Entry<K, V>> entryIterator() {
            return this.map.entrySet().iterator();
        }
        
        @Override
        Map<K, Collection<V>> createAsMap() {
            return (Map<K, Collection<V>>)new AsMap((Multimap<Object, Object>)this);
        }
        
        @Override
        public int hashCode() {
            return this.map.hashCode();
        }
    }
    
    private static class TransformedEntriesMultimap<K, V1, V2> extends AbstractMultimap<K, V2>
    {
        final Multimap<K, V1> fromMultimap;
        final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;
        
        TransformedEntriesMultimap(final Multimap<K, V1> fromMultimap, final Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
            this.fromMultimap = Preconditions.checkNotNull(fromMultimap);
            this.transformer = Preconditions.checkNotNull(transformer);
        }
        
        Collection<V2> transform(@ParametricNullness final K key, final Collection<V1> values) {
            final com.google.common.base.Function<? super V1, V2> function = Maps.asValueToValueFunction(this.transformer, key);
            if (values instanceof List) {
                return (Collection<V2>)Lists.transform((List<Object>)(List)values, (com.google.common.base.Function<? super Object, ?>)function);
            }
            return Collections2.transform(values, function);
        }
        
        @Override
        Map<K, Collection<V2>> createAsMap() {
            return Maps.transformEntries(this.fromMultimap.asMap(), (Maps.EntryTransformer<? super K, ? super Collection<V1>, Collection<V2>>)new Maps.EntryTransformer<K, Collection<V1>, Collection<V2>>() {
                @Override
                public Collection<V2> transformEntry(@ParametricNullness final K key, final Collection<V1> value) {
                    return TransformedEntriesMultimap.this.transform(key, value);
                }
            });
        }
        
        @Override
        public void clear() {
            this.fromMultimap.clear();
        }
        
        @Override
        public boolean containsKey(@CheckForNull final Object key) {
            return this.fromMultimap.containsKey(key);
        }
        
        @Override
        Collection<Map.Entry<K, V2>> createEntries() {
            return (Collection<Map.Entry<K, V2>>)new AbstractMultimap.Entries();
        }
        
        @Override
        Iterator<Map.Entry<K, V2>> entryIterator() {
            return Iterators.transform(this.fromMultimap.entries().iterator(), (com.google.common.base.Function<? super Map.Entry<K, V1>, ? extends Map.Entry<K, V2>>)Maps.asEntryToEntryFunction((Maps.EntryTransformer<? super Object, ? super Object, V2>)this.transformer));
        }
        
        @Override
        public Collection<V2> get(@ParametricNullness final K key) {
            return this.transform(key, this.fromMultimap.get(key));
        }
        
        @Override
        public boolean isEmpty() {
            return this.fromMultimap.isEmpty();
        }
        
        @Override
        Set<K> createKeySet() {
            return this.fromMultimap.keySet();
        }
        
        @Override
        Multiset<K> createKeys() {
            return this.fromMultimap.keys();
        }
        
        @Override
        public boolean put(@ParametricNullness final K key, @ParametricNullness final V2 value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean putAll(@ParametricNullness final K key, final Iterable<? extends V2> values) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean putAll(final Multimap<? extends K, ? extends V2> multimap) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(@CheckForNull final Object key, @CheckForNull final Object value) {
            return this.get(key).remove(value);
        }
        
        @Override
        public Collection<V2> removeAll(@CheckForNull final Object key) {
            return this.transform(key, this.fromMultimap.removeAll(key));
        }
        
        @Override
        public Collection<V2> replaceValues(@ParametricNullness final K key, final Iterable<? extends V2> values) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int size() {
            return this.fromMultimap.size();
        }
        
        @Override
        Collection<V2> createValues() {
            return Collections2.transform(this.fromMultimap.entries(), (com.google.common.base.Function<? super Map.Entry<K, V1>, V2>)Maps.asEntryToValueFunction((Maps.EntryTransformer<? super Object, ? super Object, T>)this.transformer));
        }
    }
    
    private static final class TransformedEntriesListMultimap<K, V1, V2> extends TransformedEntriesMultimap<K, V1, V2> implements ListMultimap<K, V2>
    {
        TransformedEntriesListMultimap(final ListMultimap<K, V1> fromMultimap, final Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
            super(fromMultimap, transformer);
        }
        
        @Override
        List<V2> transform(@ParametricNullness final K key, final Collection<V1> values) {
            return Lists.transform((List<Object>)(List)values, (com.google.common.base.Function<? super Object, ? extends V2>)Maps.asValueToValueFunction((Maps.EntryTransformer<? super K, ? super F, ? extends T>)this.transformer, key));
        }
        
        @Override
        public List<V2> get(@ParametricNullness final K key) {
            return this.transform(key, this.fromMultimap.get(key));
        }
        
        @Override
        public List<V2> removeAll(@CheckForNull final Object key) {
            return this.transform(key, this.fromMultimap.removeAll(key));
        }
        
        @Override
        public List<V2> replaceValues(@ParametricNullness final K key, final Iterable<? extends V2> values) {
            throw new UnsupportedOperationException();
        }
    }
    
    static class Keys<K, V> extends AbstractMultiset<K>
    {
        @Weak
        final Multimap<K, V> multimap;
        
        Keys(final Multimap<K, V> multimap) {
            this.multimap = multimap;
        }
        
        @Override
        Iterator<Multiset.Entry<K>> entryIterator() {
            return new TransformedIterator<Map.Entry<K, Collection<V>>, Multiset.Entry<K>>(this, this.multimap.asMap().entrySet().iterator()) {
                @Override
                Multiset.Entry<K> transform(final Map.Entry<K, Collection<V>> backingEntry) {
                    return new Multisets.AbstractEntry<K>(this) {
                        @ParametricNullness
                        @Override
                        public K getElement() {
                            return backingEntry.getKey();
                        }
                        
                        @Override
                        public int getCount() {
                            return backingEntry.getValue().size();
                        }
                    };
                }
            };
        }
        
        @Override
        public Spliterator<K> spliterator() {
            return CollectSpliterators.map(this.multimap.entries().spliterator(), (Function<? super Map.Entry<K, V>, ? extends K>)Map.Entry::getKey);
        }
        
        @Override
        public void forEach(final Consumer<? super K> consumer) {
            Preconditions.checkNotNull(consumer);
            this.multimap.entries().forEach(entry -> consumer.accept(entry.getKey()));
        }
        
        @Override
        int distinctElements() {
            return this.multimap.asMap().size();
        }
        
        @Override
        public int size() {
            return this.multimap.size();
        }
        
        @Override
        public boolean contains(@CheckForNull final Object element) {
            return this.multimap.containsKey(element);
        }
        
        @Override
        public Iterator<K> iterator() {
            return Maps.keyIterator(this.multimap.entries().iterator());
        }
        
        @Override
        public int count(@CheckForNull final Object element) {
            final Collection<V> values = Maps.safeGet(this.multimap.asMap(), element);
            return (values == null) ? 0 : values.size();
        }
        
        @Override
        public int remove(@CheckForNull final Object element, final int occurrences) {
            CollectPreconditions.checkNonnegative(occurrences, "occurrences");
            if (occurrences == 0) {
                return this.count(element);
            }
            final Collection<V> values = Maps.safeGet(this.multimap.asMap(), element);
            if (values == null) {
                return 0;
            }
            final int oldCount = values.size();
            if (occurrences >= oldCount) {
                values.clear();
            }
            else {
                final Iterator<V> iterator = values.iterator();
                for (int i = 0; i < occurrences; ++i) {
                    iterator.next();
                    iterator.remove();
                }
            }
            return oldCount;
        }
        
        @Override
        public void clear() {
            this.multimap.clear();
        }
        
        @Override
        public Set<K> elementSet() {
            return this.multimap.keySet();
        }
        
        @Override
        Iterator<K> elementIterator() {
            throw new AssertionError((Object)"should never be called");
        }
    }
    
    abstract static class Entries<K, V> extends AbstractCollection<Map.Entry<K, V>>
    {
        abstract Multimap<K, V> multimap();
        
        @Override
        public int size() {
            return this.multimap().size();
        }
        
        @Override
        public boolean contains(@CheckForNull final Object o) {
            if (o instanceof Map.Entry) {
                final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
                return this.multimap().containsEntry(entry.getKey(), entry.getValue());
            }
            return false;
        }
        
        @Override
        public boolean remove(@CheckForNull final Object o) {
            if (o instanceof Map.Entry) {
                final Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
                return this.multimap().remove(entry.getKey(), entry.getValue());
            }
            return false;
        }
        
        @Override
        public void clear() {
            this.multimap().clear();
        }
    }
    
    static final class AsMap<K, V> extends Maps.ViewCachingAbstractMap<K, Collection<V>>
    {
        @Weak
        private final Multimap<K, V> multimap;
        
        AsMap(final Multimap<K, V> multimap) {
            this.multimap = Preconditions.checkNotNull(multimap);
        }
        
        @Override
        public int size() {
            return this.multimap.keySet().size();
        }
        
        protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
            return (Set<Map.Entry<K, Collection<V>>>)new EntrySet();
        }
        
        void removeValuesForKey(@CheckForNull final Object key) {
            this.multimap.keySet().remove(key);
        }
        
        @CheckForNull
        @Override
        public Collection<V> get(@CheckForNull final Object key) {
            return this.containsKey(key) ? this.multimap.get((K)key) : null;
        }
        
        @CheckForNull
        @Override
        public Collection<V> remove(@CheckForNull final Object key) {
            return this.containsKey(key) ? this.multimap.removeAll(key) : null;
        }
        
        @Override
        public Set<K> keySet() {
            return this.multimap.keySet();
        }
        
        @Override
        public boolean isEmpty() {
            return this.multimap.isEmpty();
        }
        
        @Override
        public boolean containsKey(@CheckForNull final Object key) {
            return this.multimap.containsKey(key);
        }
        
        @Override
        public void clear() {
            this.multimap.clear();
        }
        
        class EntrySet extends Maps.EntrySet<K, Collection<V>>
        {
            @Override
            Map<K, Collection<V>> map() {
                return (Map<K, Collection<V>>)AsMap.this;
            }
            
            @Override
            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return Maps.asMapEntryIterator(AsMap.this.multimap.keySet(), (com.google.common.base.Function<? super K, Collection<V>>)new com.google.common.base.Function<K, Collection<V>>() {
                    @Override
                    public Collection<V> apply(@ParametricNullness final K key) {
                        return AsMap.this.multimap.get(key);
                    }
                });
            }
            
            @Override
            public boolean remove(@CheckForNull final Object o) {
                if (!this.contains(o)) {
                    return false;
                }
                final Map.Entry<?, ?> entry = Objects.requireNonNull(o);
                AsMap.this.removeValuesForKey(entry.getKey());
                return true;
            }
        }
    }
}
