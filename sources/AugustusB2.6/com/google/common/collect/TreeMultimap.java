// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.function.BiConsumer;
import java.util.Set;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtIncompatible;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
public class TreeMultimap<K, V> extends AbstractSortedKeySortedSetMultimap<K, V>
{
    private transient Comparator<? super K> keyComparator;
    private transient Comparator<? super V> valueComparator;
    @GwtIncompatible
    private static final long serialVersionUID = 0L;
    
    public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create() {
        return new TreeMultimap<K, V>(Ordering.natural(), Ordering.natural());
    }
    
    public static <K, V> TreeMultimap<K, V> create(final Comparator<? super K> keyComparator, final Comparator<? super V> valueComparator) {
        return new TreeMultimap<K, V>(Preconditions.checkNotNull(keyComparator), Preconditions.checkNotNull(valueComparator));
    }
    
    public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create(final Multimap<? extends K, ? extends V> multimap) {
        return new TreeMultimap<K, V>(Ordering.natural(), Ordering.natural(), multimap);
    }
    
    TreeMultimap(final Comparator<? super K> keyComparator, final Comparator<? super V> valueComparator) {
        super(new TreeMap(keyComparator));
        this.keyComparator = keyComparator;
        this.valueComparator = valueComparator;
    }
    
    private TreeMultimap(final Comparator<? super K> keyComparator, final Comparator<? super V> valueComparator, final Multimap<? extends K, ? extends V> multimap) {
        this(keyComparator, valueComparator);
        this.putAll(multimap);
    }
    
    @Override
    Map<K, Collection<V>> createAsMap() {
        return this.createMaybeNavigableAsMap();
    }
    
    @Override
    SortedSet<V> createCollection() {
        return new TreeSet<V>(this.valueComparator);
    }
    
    @Override
    Collection<V> createCollection(@ParametricNullness final K key) {
        if (key == null) {
            this.keyComparator().compare((Object)key, (Object)key);
        }
        return super.createCollection(key);
    }
    
    @Deprecated
    public Comparator<? super K> keyComparator() {
        return this.keyComparator;
    }
    
    @Override
    public Comparator<? super V> valueComparator() {
        return this.valueComparator;
    }
    
    @GwtIncompatible
    @Override
    public NavigableSet<V> get(@ParametricNullness final K key) {
        return (NavigableSet<V>)(NavigableSet)super.get(key);
    }
    
    @Override
    public NavigableSet<K> keySet() {
        return (NavigableSet<K>)(NavigableSet)super.keySet();
    }
    
    @Override
    public NavigableMap<K, Collection<V>> asMap() {
        return (NavigableMap<K, Collection<V>>)(NavigableMap)super.asMap();
    }
    
    @GwtIncompatible
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(this.keyComparator());
        stream.writeObject(this.valueComparator());
        Serialization.writeMultimap((Multimap<Object, Object>)this, stream);
    }
    
    @GwtIncompatible
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.keyComparator = Preconditions.checkNotNull(stream.readObject());
        this.valueComparator = Preconditions.checkNotNull(stream.readObject());
        this.setMap(new TreeMap<K, Collection<V>>(this.keyComparator));
        Serialization.populateMultimap((Multimap<Object, Object>)this, stream);
    }
}
