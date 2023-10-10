// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import java.util.function.BiConsumer;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import java.util.Map;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(serializable = true, emulated = true)
public final class ArrayListMultimap<K, V> extends ArrayListMultimapGwtSerializationDependencies<K, V>
{
    private static final int DEFAULT_VALUES_PER_KEY = 3;
    @VisibleForTesting
    transient int expectedValuesPerKey;
    @GwtIncompatible
    private static final long serialVersionUID = 0L;
    
    public static <K, V> ArrayListMultimap<K, V> create() {
        return new ArrayListMultimap<K, V>();
    }
    
    public static <K, V> ArrayListMultimap<K, V> create(final int expectedKeys, final int expectedValuesPerKey) {
        return new ArrayListMultimap<K, V>(expectedKeys, expectedValuesPerKey);
    }
    
    public static <K, V> ArrayListMultimap<K, V> create(final Multimap<? extends K, ? extends V> multimap) {
        return new ArrayListMultimap<K, V>(multimap);
    }
    
    private ArrayListMultimap() {
        this(12, 3);
    }
    
    private ArrayListMultimap(final int expectedKeys, final int expectedValuesPerKey) {
        super(Platform.newHashMapWithExpectedSize(expectedKeys));
        CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
        this.expectedValuesPerKey = expectedValuesPerKey;
    }
    
    private ArrayListMultimap(final Multimap<? extends K, ? extends V> multimap) {
        this(multimap.keySet().size(), (multimap instanceof ArrayListMultimap) ? ((ArrayListMultimap)multimap).expectedValuesPerKey : 3);
        this.putAll(multimap);
    }
    
    @Override
    List<V> createCollection() {
        return new ArrayList<V>(this.expectedValuesPerKey);
    }
    
    @Deprecated
    public void trimToSize() {
        for (final Collection<V> collection : this.backingMap().values()) {
            final ArrayList<V> arrayList = (ArrayList<V>)(ArrayList)collection;
            arrayList.trimToSize();
        }
    }
    
    @GwtIncompatible
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        Serialization.writeMultimap((Multimap<Object, Object>)this, stream);
    }
    
    @GwtIncompatible
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.expectedValuesPerKey = 3;
        final int distinctKeys = Serialization.readCount(stream);
        final Map<K, Collection<V>> map = (Map<K, Collection<V>>)Maps.newHashMap();
        this.setMap(map);
        Serialization.populateMultimap((Multimap<Object, Object>)this, stream, distinctKeys);
    }
}
