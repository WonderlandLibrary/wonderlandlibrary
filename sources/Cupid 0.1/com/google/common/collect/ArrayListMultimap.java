package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@GwtCompatible(serializable = true, emulated = true)
public final class ArrayListMultimap<K, V> extends AbstractListMultimap<K, V> {
  private static final int DEFAULT_VALUES_PER_KEY = 3;
  
  @VisibleForTesting
  transient int expectedValuesPerKey;
  
  @GwtIncompatible("Not needed in emulated source.")
  private static final long serialVersionUID = 0L;
  
  public static <K, V> ArrayListMultimap<K, V> create() {
    return new ArrayListMultimap<K, V>();
  }
  
  public static <K, V> ArrayListMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
    return new ArrayListMultimap<K, V>(expectedKeys, expectedValuesPerKey);
  }
  
  public static <K, V> ArrayListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
    return new ArrayListMultimap<K, V>(multimap);
  }
  
  private ArrayListMultimap() {
    super(new HashMap<K, Collection<V>>());
    this.expectedValuesPerKey = 3;
  }
  
  private ArrayListMultimap(int expectedKeys, int expectedValuesPerKey) {
    super(Maps.newHashMapWithExpectedSize(expectedKeys));
    CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
    this.expectedValuesPerKey = expectedValuesPerKey;
  }
  
  private ArrayListMultimap(Multimap<? extends K, ? extends V> multimap) {
    this(multimap.keySet().size(), (multimap instanceof ArrayListMultimap) ? ((ArrayListMultimap)multimap).expectedValuesPerKey : 3);
    putAll(multimap);
  }
  
  List<V> createCollection() {
    return new ArrayList<V>(this.expectedValuesPerKey);
  }
  
  public void trimToSize() {
    for (Collection<V> collection : backingMap().values()) {
      ArrayList<V> arrayList = (ArrayList<V>)collection;
      arrayList.trimToSize();
    } 
  }
  
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    stream.writeInt(this.expectedValuesPerKey);
    Serialization.writeMultimap(this, stream);
  }
  
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.expectedValuesPerKey = stream.readInt();
    int distinctKeys = Serialization.readCount(stream);
    Map<K, Collection<V>> map = Maps.newHashMapWithExpectedSize(distinctKeys);
    setMap(map);
    Serialization.populateMultimap(this, stream, distinctKeys);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ArrayListMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */