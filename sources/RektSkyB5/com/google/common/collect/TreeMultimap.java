/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMapBasedMultimap;
import com.google.common.collect.AbstractSortedKeySortedSetMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.Serialization;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.Nullable;

@GwtCompatible(serializable=true, emulated=true)
public class TreeMultimap<K, V>
extends AbstractSortedKeySortedSetMultimap<K, V> {
    private transient Comparator<? super K> keyComparator;
    private transient Comparator<? super V> valueComparator;
    @GwtIncompatible(value="not needed in emulated source")
    private static final long serialVersionUID = 0L;

    public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create() {
        return new TreeMultimap(Ordering.natural(), Ordering.natural());
    }

    public static <K, V> TreeMultimap<K, V> create(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
        return new TreeMultimap<K, V>(Preconditions.checkNotNull(keyComparator), Preconditions.checkNotNull(valueComparator));
    }

    public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
        return new TreeMultimap<K, V>(Ordering.natural(), Ordering.natural(), multimap);
    }

    TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
        super(new TreeMap(keyComparator));
        this.keyComparator = keyComparator;
        this.valueComparator = valueComparator;
    }

    private TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator, Multimap<? extends K, ? extends V> multimap) {
        this(keyComparator, valueComparator);
        this.putAll((Multimap)multimap);
    }

    @Override
    SortedSet<V> createCollection() {
        return new TreeSet<V>(this.valueComparator);
    }

    @Override
    Collection<V> createCollection(@Nullable K key) {
        if (key == null) {
            this.keyComparator().compare(key, key);
        }
        return super.createCollection(key);
    }

    public Comparator<? super K> keyComparator() {
        return this.keyComparator;
    }

    @Override
    public Comparator<? super V> valueComparator() {
        return this.valueComparator;
    }

    @Override
    @GwtIncompatible(value="NavigableMap")
    NavigableMap<K, Collection<V>> backingMap() {
        return (NavigableMap)super.backingMap();
    }

    @Override
    @GwtIncompatible(value="NavigableSet")
    public NavigableSet<V> get(@Nullable K key) {
        return (NavigableSet)super.get((Object)key);
    }

    @Override
    @GwtIncompatible(value="NavigableSet")
    Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) {
        return Sets.unmodifiableNavigableSet((NavigableSet)collection);
    }

    @Override
    @GwtIncompatible(value="NavigableSet")
    Collection<V> wrapCollection(K key, Collection<V> collection) {
        return (AbstractMapBasedMultimap)this.new AbstractMapBasedMultimap.WrappedNavigableSet(key, (NavigableSet)collection, null);
    }

    @Override
    @GwtIncompatible(value="NavigableSet")
    public NavigableSet<K> keySet() {
        return (NavigableSet)super.keySet();
    }

    @Override
    @GwtIncompatible(value="NavigableSet")
    NavigableSet<K> createKeySet() {
        return (AbstractMapBasedMultimap)this.new AbstractMapBasedMultimap.NavigableKeySet(this.backingMap());
    }

    @Override
    @GwtIncompatible(value="NavigableMap")
    public NavigableMap<K, Collection<V>> asMap() {
        return (NavigableMap)super.asMap();
    }

    @Override
    @GwtIncompatible(value="NavigableMap")
    NavigableMap<K, Collection<V>> createAsMap() {
        return (AbstractMapBasedMultimap)this.new AbstractMapBasedMultimap.NavigableAsMap(this.backingMap());
    }

    @GwtIncompatible(value="java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.keyComparator());
        stream.writeObject(this.valueComparator());
        Serialization.writeMultimap(this, stream);
    }

    @GwtIncompatible(value="java.io.ObjectInputStream")
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.keyComparator = Preconditions.checkNotNull((Comparator)stream.readObject());
        this.valueComparator = Preconditions.checkNotNull((Comparator)stream.readObject());
        this.setMap(new TreeMap(this.keyComparator));
        Serialization.populateMultimap(this, stream);
    }
}

