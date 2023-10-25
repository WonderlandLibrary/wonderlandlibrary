package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public class TreeMultimap<K, V> extends AbstractSortedKeySortedSetMultimap<K, V> {
  private transient Comparator<? super K> keyComparator;
  
  private transient Comparator<? super V> valueComparator;
  
  @GwtIncompatible("not needed in emulated source")
  private static final long serialVersionUID = 0L;
  
  public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create() {
    return new TreeMultimap<K, V>(Ordering.natural(), Ordering.natural());
  }
  
  public static <K, V> TreeMultimap<K, V> create(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
    return new TreeMultimap<K, V>((Comparator<? super K>)Preconditions.checkNotNull(keyComparator), (Comparator<? super V>)Preconditions.checkNotNull(valueComparator));
  }
  
  public static <K extends Comparable, V extends Comparable> TreeMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
    return new TreeMultimap<K, V>(Ordering.natural(), Ordering.natural(), multimap);
  }
  
  TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator) {
    super(new TreeMap<K, Collection<V>>(keyComparator));
    this.keyComparator = keyComparator;
    this.valueComparator = valueComparator;
  }
  
  private TreeMultimap(Comparator<? super K> keyComparator, Comparator<? super V> valueComparator, Multimap<? extends K, ? extends V> multimap) {
    this(keyComparator, valueComparator);
    putAll(multimap);
  }
  
  SortedSet<V> createCollection() {
    return new TreeSet<V>(this.valueComparator);
  }
  
  Collection<V> createCollection(@Nullable K key) {
    if (key == null)
      keyComparator().compare(key, key); 
    return super.createCollection(key);
  }
  
  public Comparator<? super K> keyComparator() {
    return this.keyComparator;
  }
  
  public Comparator<? super V> valueComparator() {
    return this.valueComparator;
  }
  
  @GwtIncompatible("NavigableMap")
  NavigableMap<K, Collection<V>> backingMap() {
    return (NavigableMap<K, Collection<V>>)super.backingMap();
  }
  
  @GwtIncompatible("NavigableSet")
  public NavigableSet<V> get(@Nullable K key) {
    return (NavigableSet<V>)super.get(key);
  }
  
  @GwtIncompatible("NavigableSet")
  Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) {
    return Sets.unmodifiableNavigableSet((NavigableSet<V>)collection);
  }
  
  @GwtIncompatible("NavigableSet")
  Collection<V> wrapCollection(K key, Collection<V> collection) {
    return new AbstractMapBasedMultimap.WrappedNavigableSet(this, key, (NavigableSet<V>)collection, null);
  }
  
  @GwtIncompatible("NavigableSet")
  public NavigableSet<K> keySet() {
    return (NavigableSet<K>)super.keySet();
  }
  
  @GwtIncompatible("NavigableSet")
  NavigableSet<K> createKeySet() {
    return new AbstractMapBasedMultimap.NavigableKeySet(this, backingMap());
  }
  
  @GwtIncompatible("NavigableMap")
  public NavigableMap<K, Collection<V>> asMap() {
    return (NavigableMap<K, Collection<V>>)super.asMap();
  }
  
  @GwtIncompatible("NavigableMap")
  NavigableMap<K, Collection<V>> createAsMap() {
    return new AbstractMapBasedMultimap.NavigableAsMap(this, backingMap());
  }
  
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    stream.writeObject(keyComparator());
    stream.writeObject(valueComparator());
    Serialization.writeMultimap(this, stream);
  }
  
  @GwtIncompatible("java.io.ObjectInputStream")
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.keyComparator = (Comparator<? super K>)Preconditions.checkNotNull(stream.readObject());
    this.valueComparator = (Comparator<? super V>)Preconditions.checkNotNull(stream.readObject());
    setMap(new TreeMap<K, Collection<V>>(this.keyComparator));
    Serialization.populateMultimap(this, stream);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\TreeMultimap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */