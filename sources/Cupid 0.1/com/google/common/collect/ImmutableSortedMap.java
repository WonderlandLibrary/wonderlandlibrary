package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements NavigableMap<K, V> {
  private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
  
  private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new EmptyImmutableSortedMap<Comparable, Object>(NATURAL_ORDER);
  
  private transient ImmutableSortedMap<K, V> descendingMap;
  
  private static final long serialVersionUID = 0L;
  
  static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
    if (Ordering.<Comparable>natural().equals(comparator))
      return of(); 
    return new EmptyImmutableSortedMap<K, V>(comparator);
  }
  
  static <K, V> ImmutableSortedMap<K, V> fromSortedEntries(Comparator<? super K> comparator, int size, Map.Entry<K, V>[] entries) {
    if (size == 0)
      return emptyMap(comparator); 
    ImmutableList.Builder<K> keyBuilder = ImmutableList.builder();
    ImmutableList.Builder<V> valueBuilder = ImmutableList.builder();
    for (int i = 0; i < size; i++) {
      Map.Entry<K, V> entry = entries[i];
      keyBuilder.add(entry.getKey());
      valueBuilder.add(entry.getValue());
    } 
    return new RegularImmutableSortedMap<K, V>(new RegularImmutableSortedSet<K>(keyBuilder.build(), comparator), valueBuilder.build());
  }
  
  static <K, V> ImmutableSortedMap<K, V> from(ImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
    if (keySet.isEmpty())
      return emptyMap(keySet.comparator()); 
    return new RegularImmutableSortedMap<K, V>((RegularImmutableSortedSet<K>)keySet, valueList);
  }
  
  public static <K, V> ImmutableSortedMap<K, V> of() {
    return (ImmutableSortedMap)NATURAL_EMPTY_MAP;
  }
  
  public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1) {
    return from(ImmutableSortedSet.of(k1), ImmutableList.of(v1));
  }
  
  public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2) {
    return fromEntries(Ordering.natural(), false, 2, (Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
  }
  
  public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
    return fromEntries(Ordering.natural(), false, 3, (Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
  }
  
  public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
    return fromEntries(Ordering.natural(), false, 4, (Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) });
  }
  
  public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
    return fromEntries(Ordering.natural(), false, 5, (Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5) });
  }
  
  public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
    Ordering<K> naturalOrder = Ordering.natural();
    return copyOfInternal(map, naturalOrder);
  }
  
  public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
    return copyOfInternal(map, (Comparator<? super K>)Preconditions.checkNotNull(comparator));
  }
  
  public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> map) {
    Comparator<Comparable> comparator1;
    Comparator<? super K> comparator = map.comparator();
    if (comparator == null)
      comparator1 = NATURAL_ORDER; 
    return copyOfInternal(map, (Comparator)comparator1);
  }
  
  private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
    boolean sameComparator = false;
    if (map instanceof SortedMap) {
      SortedMap<?, ?> sortedMap = (SortedMap<?, ?>)map;
      Comparator<?> comparator2 = sortedMap.comparator();
      sameComparator = (comparator2 == null) ? ((comparator == NATURAL_ORDER)) : comparator.equals(comparator2);
    } 
    if (sameComparator && map instanceof ImmutableSortedMap) {
      ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap)map;
      if (!kvMap.isPartialView())
        return kvMap; 
    } 
    Map.Entry[] arrayOfEntry = (Map.Entry[])map.entrySet().toArray((Object[])new Map.Entry[0]);
    return fromEntries(comparator, sameComparator, arrayOfEntry.length, (Map.Entry<K, V>[])arrayOfEntry);
  }
  
  static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean sameComparator, int size, Map.Entry<K, V>... entries) {
    for (int i = 0; i < size; i++) {
      Map.Entry<K, V> entry = entries[i];
      entries[i] = entryOf(entry.getKey(), entry.getValue());
    } 
    if (!sameComparator) {
      sortEntries(comparator, size, entries);
      validateEntries(size, entries, comparator);
    } 
    return fromSortedEntries(comparator, size, entries);
  }
  
  private static <K, V> void sortEntries(Comparator<? super K> comparator, int size, Map.Entry<K, V>[] entries) {
    Arrays.sort(entries, 0, size, Ordering.<K>from(comparator).onKeys());
  }
  
  private static <K, V> void validateEntries(int size, Map.Entry<K, V>[] entries, Comparator<? super K> comparator) {
    for (int i = 1; i < size; i++)
      checkNoConflict((comparator.compare(entries[i - 1].getKey(), entries[i].getKey()) != 0), "key", entries[i - 1], entries[i]); 
  }
  
  public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
    return new Builder<K, V>(Ordering.natural());
  }
  
  public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
    return new Builder<K, V>(comparator);
  }
  
  public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
    return new Builder<K, V>(Ordering.<Comparable>natural().reverse());
  }
  
  public static class Builder<K, V> extends ImmutableMap.Builder<K, V> {
    private final Comparator<? super K> comparator;
    
    public Builder(Comparator<? super K> comparator) {
      this.comparator = (Comparator<? super K>)Preconditions.checkNotNull(comparator);
    }
    
    public Builder<K, V> put(K key, V value) {
      super.put(key, value);
      return this;
    }
    
    public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
      super.put(entry);
      return this;
    }
    
    public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
      super.putAll(map);
      return this;
    }
    
    public ImmutableSortedMap<K, V> build() {
      return ImmutableSortedMap.fromEntries(this.comparator, false, this.size, (Map.Entry<K, V>[])this.entries);
    }
  }
  
  ImmutableSortedMap() {}
  
  ImmutableSortedMap(ImmutableSortedMap<K, V> descendingMap) {
    this.descendingMap = descendingMap;
  }
  
  public int size() {
    return values().size();
  }
  
  public boolean containsValue(@Nullable Object value) {
    return values().contains(value);
  }
  
  boolean isPartialView() {
    return (keySet().isPartialView() || values().isPartialView());
  }
  
  public ImmutableSet<Map.Entry<K, V>> entrySet() {
    return super.entrySet();
  }
  
  public Comparator<? super K> comparator() {
    return keySet().comparator();
  }
  
  public K firstKey() {
    return keySet().first();
  }
  
  public K lastKey() {
    return keySet().last();
  }
  
  public ImmutableSortedMap<K, V> headMap(K toKey) {
    return headMap(toKey, false);
  }
  
  public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
    return subMap(fromKey, true, toKey, false);
  }
  
  public ImmutableSortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
    Preconditions.checkNotNull(fromKey);
    Preconditions.checkNotNull(toKey);
    Preconditions.checkArgument((comparator().compare(fromKey, toKey) <= 0), "expected fromKey <= toKey but %s > %s", new Object[] { fromKey, toKey });
    return headMap(toKey, toInclusive).tailMap(fromKey, fromInclusive);
  }
  
  public ImmutableSortedMap<K, V> tailMap(K fromKey) {
    return tailMap(fromKey, true);
  }
  
  public Map.Entry<K, V> lowerEntry(K key) {
    return headMap(key, false).lastEntry();
  }
  
  public K lowerKey(K key) {
    return Maps.keyOrNull(lowerEntry(key));
  }
  
  public Map.Entry<K, V> floorEntry(K key) {
    return headMap(key, true).lastEntry();
  }
  
  public K floorKey(K key) {
    return Maps.keyOrNull(floorEntry(key));
  }
  
  public Map.Entry<K, V> ceilingEntry(K key) {
    return tailMap(key, true).firstEntry();
  }
  
  public K ceilingKey(K key) {
    return Maps.keyOrNull(ceilingEntry(key));
  }
  
  public Map.Entry<K, V> higherEntry(K key) {
    return tailMap(key, false).firstEntry();
  }
  
  public K higherKey(K key) {
    return Maps.keyOrNull(higherEntry(key));
  }
  
  public Map.Entry<K, V> firstEntry() {
    return isEmpty() ? null : entrySet().asList().get(0);
  }
  
  public Map.Entry<K, V> lastEntry() {
    return isEmpty() ? null : entrySet().asList().get(size() - 1);
  }
  
  @Deprecated
  public final Map.Entry<K, V> pollFirstEntry() {
    throw new UnsupportedOperationException();
  }
  
  @Deprecated
  public final Map.Entry<K, V> pollLastEntry() {
    throw new UnsupportedOperationException();
  }
  
  public ImmutableSortedMap<K, V> descendingMap() {
    ImmutableSortedMap<K, V> result = this.descendingMap;
    if (result == null)
      result = this.descendingMap = createDescendingMap(); 
    return result;
  }
  
  public ImmutableSortedSet<K> navigableKeySet() {
    return keySet();
  }
  
  public ImmutableSortedSet<K> descendingKeySet() {
    return keySet().descendingSet();
  }
  
  private static class SerializedForm extends ImmutableMap.SerializedForm {
    private final Comparator<Object> comparator;
    
    private static final long serialVersionUID = 0L;
    
    SerializedForm(ImmutableSortedMap<?, ?> sortedMap) {
      super(sortedMap);
      this.comparator = (Comparator)sortedMap.comparator();
    }
    
    Object readResolve() {
      ImmutableSortedMap.Builder<Object, Object> builder = new ImmutableSortedMap.Builder<Object, Object>(this.comparator);
      return createMap(builder);
    }
  }
  
  Object writeReplace() {
    return new SerializedForm(this);
  }
  
  public abstract ImmutableSortedSet<K> keySet();
  
  public abstract ImmutableCollection<V> values();
  
  public abstract ImmutableSortedMap<K, V> headMap(K paramK, boolean paramBoolean);
  
  public abstract ImmutableSortedMap<K, V> tailMap(K paramK, boolean paramBoolean);
  
  abstract ImmutableSortedMap<K, V> createDescendingMap();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\ImmutableSortedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */