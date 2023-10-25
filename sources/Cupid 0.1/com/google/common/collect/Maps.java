package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Converter;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Maps {
  private enum EntryFunction implements Function<Map.Entry<?, ?>, Object> {
    KEY {
      @Nullable
      public Object apply(Map.Entry<?, ?> entry) {
        return entry.getKey();
      }
    },
    VALUE {
      @Nullable
      public Object apply(Map.Entry<?, ?> entry) {
        return entry.getValue();
      }
    };
  }
  
  static <K> Function<Map.Entry<K, ?>, K> keyFunction() {
    return EntryFunction.KEY;
  }
  
  static <V> Function<Map.Entry<?, V>, V> valueFunction() {
    return EntryFunction.VALUE;
  }
  
  static <K, V> Iterator<K> keyIterator(Iterator<Map.Entry<K, V>> entryIterator) {
    return Iterators.transform(entryIterator, (Function)keyFunction());
  }
  
  static <K, V> Iterator<V> valueIterator(Iterator<Map.Entry<K, V>> entryIterator) {
    return Iterators.transform(entryIterator, (Function)valueFunction());
  }
  
  static <K, V> UnmodifiableIterator<V> valueIterator(final UnmodifiableIterator<Map.Entry<K, V>> entryIterator) {
    return new UnmodifiableIterator<V>() {
        public boolean hasNext() {
          return entryIterator.hasNext();
        }
        
        public V next() {
          return (V)((Map.Entry)entryIterator.next()).getValue();
        }
      };
  }
  
  @GwtCompatible(serializable = true)
  @Beta
  public static <K extends Enum<K>, V> ImmutableMap<K, V> immutableEnumMap(Map<K, ? extends V> map) {
    if (map instanceof ImmutableEnumMap) {
      ImmutableEnumMap<K, V> result = (ImmutableEnumMap)map;
      return result;
    } 
    if (map.isEmpty())
      return ImmutableMap.of(); 
    for (Map.Entry<K, ? extends V> entry : map.entrySet()) {
      Preconditions.checkNotNull(entry.getKey());
      Preconditions.checkNotNull(entry.getValue());
    } 
    return ImmutableEnumMap.asImmutable(new EnumMap<K, V>(map));
  }
  
  public static <K, V> HashMap<K, V> newHashMap() {
    return new HashMap<K, V>();
  }
  
  public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
    return new HashMap<K, V>(capacity(expectedSize));
  }
  
  static int capacity(int expectedSize) {
    if (expectedSize < 3) {
      CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
      return expectedSize + 1;
    } 
    if (expectedSize < 1073741824)
      return expectedSize + expectedSize / 3; 
    return Integer.MAX_VALUE;
  }
  
  public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
    return new HashMap<K, V>(map);
  }
  
  public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
    return new LinkedHashMap<K, V>();
  }
  
  public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
    return new LinkedHashMap<K, V>(map);
  }
  
  public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
    return (new MapMaker()).makeMap();
  }
  
  public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
    return new TreeMap<K, V>();
  }
  
  public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> map) {
    return new TreeMap<K, V>(map);
  }
  
  public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@Nullable Comparator<C> comparator) {
    return new TreeMap<K, V>(comparator);
  }
  
  public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
    return new EnumMap<K, V>((Class<K>)Preconditions.checkNotNull(type));
  }
  
  public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> map) {
    return new EnumMap<K, V>(map);
  }
  
  public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
    return new IdentityHashMap<K, V>();
  }
  
  public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right) {
    if (left instanceof SortedMap) {
      SortedMap<K, ? extends V> sortedLeft = (SortedMap)left;
      SortedMapDifference<K, V> result = difference(sortedLeft, right);
      return result;
    } 
    return difference(left, right, Equivalence.equals());
  }
  
  @Beta
  public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence) {
    Preconditions.checkNotNull(valueEquivalence);
    Map<K, V> onlyOnLeft = newHashMap();
    Map<K, V> onlyOnRight = new HashMap<K, V>(right);
    Map<K, V> onBoth = newHashMap();
    Map<K, MapDifference.ValueDifference<V>> differences = newHashMap();
    doDifference(left, right, valueEquivalence, onlyOnLeft, onlyOnRight, onBoth, differences);
    return new MapDifferenceImpl<K, V>(onlyOnLeft, onlyOnRight, onBoth, differences);
  }
  
  private static <K, V> void doDifference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence, Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
    for (Map.Entry<? extends K, ? extends V> entry : left.entrySet()) {
      K leftKey = entry.getKey();
      V leftValue = entry.getValue();
      if (right.containsKey(leftKey)) {
        V rightValue = onlyOnRight.remove(leftKey);
        if (valueEquivalence.equivalent(leftValue, rightValue)) {
          onBoth.put(leftKey, leftValue);
          continue;
        } 
        differences.put(leftKey, ValueDifferenceImpl.create(leftValue, rightValue));
        continue;
      } 
      onlyOnLeft.put(leftKey, leftValue);
    } 
  }
  
  private static <K, V> Map<K, V> unmodifiableMap(Map<K, V> map) {
    if (map instanceof SortedMap)
      return Collections.unmodifiableSortedMap((SortedMap<K, ? extends V>)map); 
    return Collections.unmodifiableMap(map);
  }
  
  static class MapDifferenceImpl<K, V> implements MapDifference<K, V> {
    final Map<K, V> onlyOnLeft;
    
    final Map<K, V> onlyOnRight;
    
    final Map<K, V> onBoth;
    
    final Map<K, MapDifference.ValueDifference<V>> differences;
    
    MapDifferenceImpl(Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
      this.onlyOnLeft = Maps.unmodifiableMap(onlyOnLeft);
      this.onlyOnRight = Maps.unmodifiableMap(onlyOnRight);
      this.onBoth = Maps.unmodifiableMap(onBoth);
      this.differences = (Map)Maps.unmodifiableMap((Map)differences);
    }
    
    public boolean areEqual() {
      return (this.onlyOnLeft.isEmpty() && this.onlyOnRight.isEmpty() && this.differences.isEmpty());
    }
    
    public Map<K, V> entriesOnlyOnLeft() {
      return this.onlyOnLeft;
    }
    
    public Map<K, V> entriesOnlyOnRight() {
      return this.onlyOnRight;
    }
    
    public Map<K, V> entriesInCommon() {
      return this.onBoth;
    }
    
    public Map<K, MapDifference.ValueDifference<V>> entriesDiffering() {
      return this.differences;
    }
    
    public boolean equals(Object object) {
      if (object == this)
        return true; 
      if (object instanceof MapDifference) {
        MapDifference<?, ?> other = (MapDifference<?, ?>)object;
        return (entriesOnlyOnLeft().equals(other.entriesOnlyOnLeft()) && entriesOnlyOnRight().equals(other.entriesOnlyOnRight()) && entriesInCommon().equals(other.entriesInCommon()) && entriesDiffering().equals(other.entriesDiffering()));
      } 
      return false;
    }
    
    public int hashCode() {
      return Objects.hashCode(new Object[] { entriesOnlyOnLeft(), entriesOnlyOnRight(), entriesInCommon(), entriesDiffering() });
    }
    
    public String toString() {
      if (areEqual())
        return "equal"; 
      StringBuilder result = new StringBuilder("not equal");
      if (!this.onlyOnLeft.isEmpty())
        result.append(": only on left=").append(this.onlyOnLeft); 
      if (!this.onlyOnRight.isEmpty())
        result.append(": only on right=").append(this.onlyOnRight); 
      if (!this.differences.isEmpty())
        result.append(": value differences=").append(this.differences); 
      return result.toString();
    }
  }
  
  static class ValueDifferenceImpl<V> implements MapDifference.ValueDifference<V> {
    private final V left;
    
    private final V right;
    
    static <V> MapDifference.ValueDifference<V> create(@Nullable V left, @Nullable V right) {
      return new ValueDifferenceImpl<V>(left, right);
    }
    
    private ValueDifferenceImpl(@Nullable V left, @Nullable V right) {
      this.left = left;
      this.right = right;
    }
    
    public V leftValue() {
      return this.left;
    }
    
    public V rightValue() {
      return this.right;
    }
    
    public boolean equals(@Nullable Object object) {
      if (object instanceof MapDifference.ValueDifference) {
        MapDifference.ValueDifference<?> that = (MapDifference.ValueDifference)object;
        return (Objects.equal(this.left, that.leftValue()) && Objects.equal(this.right, that.rightValue()));
      } 
      return false;
    }
    
    public int hashCode() {
      return Objects.hashCode(new Object[] { this.left, this.right });
    }
    
    public String toString() {
      return "(" + this.left + ", " + this.right + ")";
    }
  }
  
  public static <K, V> SortedMapDifference<K, V> difference(SortedMap<K, ? extends V> left, Map<? extends K, ? extends V> right) {
    Preconditions.checkNotNull(left);
    Preconditions.checkNotNull(right);
    Comparator<? super K> comparator = orNaturalOrder(left.comparator());
    SortedMap<K, V> onlyOnLeft = newTreeMap(comparator);
    SortedMap<K, V> onlyOnRight = newTreeMap(comparator);
    onlyOnRight.putAll(right);
    SortedMap<K, V> onBoth = newTreeMap(comparator);
    SortedMap<K, MapDifference.ValueDifference<V>> differences = newTreeMap(comparator);
    doDifference(left, right, Equivalence.equals(), onlyOnLeft, onlyOnRight, onBoth, differences);
    return new SortedMapDifferenceImpl<K, V>(onlyOnLeft, onlyOnRight, onBoth, differences);
  }
  
  static class SortedMapDifferenceImpl<K, V> extends MapDifferenceImpl<K, V> implements SortedMapDifference<K, V> {
    SortedMapDifferenceImpl(SortedMap<K, V> onlyOnLeft, SortedMap<K, V> onlyOnRight, SortedMap<K, V> onBoth, SortedMap<K, MapDifference.ValueDifference<V>> differences) {
      super(onlyOnLeft, onlyOnRight, onBoth, differences);
    }
    
    public SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering() {
      return (SortedMap<K, MapDifference.ValueDifference<V>>)super.entriesDiffering();
    }
    
    public SortedMap<K, V> entriesInCommon() {
      return (SortedMap<K, V>)super.entriesInCommon();
    }
    
    public SortedMap<K, V> entriesOnlyOnLeft() {
      return (SortedMap<K, V>)super.entriesOnlyOnLeft();
    }
    
    public SortedMap<K, V> entriesOnlyOnRight() {
      return (SortedMap<K, V>)super.entriesOnlyOnRight();
    }
  }
  
  static <E> Comparator<? super E> orNaturalOrder(@Nullable Comparator<? super E> comparator) {
    if (comparator != null)
      return comparator; 
    return Ordering.natural();
  }
  
  @Beta
  public static <K, V> Map<K, V> asMap(Set<K> set, Function<? super K, V> function) {
    if (set instanceof SortedSet)
      return asMap((SortedSet<K>)set, function); 
    return new AsMapView<K, V>(set, function);
  }
  
  @Beta
  public static <K, V> SortedMap<K, V> asMap(SortedSet<K> set, Function<? super K, V> function) {
    return Platform.mapsAsMapSortedSet(set, function);
  }
  
  static <K, V> SortedMap<K, V> asMapSortedIgnoreNavigable(SortedSet<K> set, Function<? super K, V> function) {
    return new SortedAsMapView<K, V>(set, function);
  }
  
  @Beta
  @GwtIncompatible("NavigableMap")
  public static <K, V> NavigableMap<K, V> asMap(NavigableSet<K> set, Function<? super K, V> function) {
    return new NavigableAsMapView<K, V>(set, function);
  }
  
  private static class AsMapView<K, V> extends ImprovedAbstractMap<K, V> {
    private final Set<K> set;
    
    final Function<? super K, V> function;
    
    Set<K> backingSet() {
      return this.set;
    }
    
    AsMapView(Set<K> set, Function<? super K, V> function) {
      this.set = (Set<K>)Preconditions.checkNotNull(set);
      this.function = (Function<? super K, V>)Preconditions.checkNotNull(function);
    }
    
    public Set<K> createKeySet() {
      return Maps.removeOnlySet(backingSet());
    }
    
    Collection<V> createValues() {
      return Collections2.transform(this.set, this.function);
    }
    
    public int size() {
      return backingSet().size();
    }
    
    public boolean containsKey(@Nullable Object key) {
      return backingSet().contains(key);
    }
    
    public V get(@Nullable Object key) {
      if (Collections2.safeContains(backingSet(), key)) {
        K k = (K)key;
        return (V)this.function.apply(k);
      } 
      return null;
    }
    
    public V remove(@Nullable Object key) {
      if (backingSet().remove(key)) {
        K k = (K)key;
        return (V)this.function.apply(k);
      } 
      return null;
    }
    
    public void clear() {
      backingSet().clear();
    }
    
    protected Set<Map.Entry<K, V>> createEntrySet() {
      return new Maps.EntrySet<K, V>() {
          Map<K, V> map() {
            return Maps.AsMapView.this;
          }
          
          public Iterator<Map.Entry<K, V>> iterator() {
            return Maps.asMapEntryIterator(Maps.AsMapView.this.backingSet(), Maps.AsMapView.this.function);
          }
        };
    }
  }
  
  static <K, V> Iterator<Map.Entry<K, V>> asMapEntryIterator(Set<K> set, final Function<? super K, V> function) {
    return new TransformedIterator<K, Map.Entry<K, V>>(set.iterator()) {
        Map.Entry<K, V> transform(K key) {
          return Maps.immutableEntry(key, (V)function.apply(key));
        }
      };
  }
  
  private static class SortedAsMapView<K, V> extends AsMapView<K, V> implements SortedMap<K, V> {
    SortedAsMapView(SortedSet<K> set, Function<? super K, V> function) {
      super(set, function);
    }
    
    SortedSet<K> backingSet() {
      return (SortedSet<K>)super.backingSet();
    }
    
    public Comparator<? super K> comparator() {
      return backingSet().comparator();
    }
    
    public Set<K> keySet() {
      return Maps.removeOnlySortedSet(backingSet());
    }
    
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
      return Maps.asMap(backingSet().subSet(fromKey, toKey), this.function);
    }
    
    public SortedMap<K, V> headMap(K toKey) {
      return Maps.asMap(backingSet().headSet(toKey), this.function);
    }
    
    public SortedMap<K, V> tailMap(K fromKey) {
      return Maps.asMap(backingSet().tailSet(fromKey), this.function);
    }
    
    public K firstKey() {
      return backingSet().first();
    }
    
    public K lastKey() {
      return backingSet().last();
    }
  }
  
  @GwtIncompatible("NavigableMap")
  private static final class NavigableAsMapView<K, V> extends AbstractNavigableMap<K, V> {
    private final NavigableSet<K> set;
    
    private final Function<? super K, V> function;
    
    NavigableAsMapView(NavigableSet<K> ks, Function<? super K, V> vFunction) {
      this.set = (NavigableSet<K>)Preconditions.checkNotNull(ks);
      this.function = (Function<? super K, V>)Preconditions.checkNotNull(vFunction);
    }
    
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
      return Maps.asMap(this.set.subSet(fromKey, fromInclusive, toKey, toInclusive), this.function);
    }
    
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
      return Maps.asMap(this.set.headSet(toKey, inclusive), this.function);
    }
    
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
      return Maps.asMap(this.set.tailSet(fromKey, inclusive), this.function);
    }
    
    public Comparator<? super K> comparator() {
      return this.set.comparator();
    }
    
    @Nullable
    public V get(@Nullable Object key) {
      if (Collections2.safeContains(this.set, key)) {
        K k = (K)key;
        return (V)this.function.apply(k);
      } 
      return null;
    }
    
    public void clear() {
      this.set.clear();
    }
    
    Iterator<Map.Entry<K, V>> entryIterator() {
      return Maps.asMapEntryIterator(this.set, this.function);
    }
    
    Iterator<Map.Entry<K, V>> descendingEntryIterator() {
      return descendingMap().entrySet().iterator();
    }
    
    public NavigableSet<K> navigableKeySet() {
      return Maps.removeOnlyNavigableSet(this.set);
    }
    
    public int size() {
      return this.set.size();
    }
    
    public NavigableMap<K, V> descendingMap() {
      return Maps.asMap(this.set.descendingSet(), this.function);
    }
  }
  
  private static <E> Set<E> removeOnlySet(final Set<E> set) {
    return new ForwardingSet<E>() {
        protected Set<E> delegate() {
          return set;
        }
        
        public boolean add(E element) {
          throw new UnsupportedOperationException();
        }
        
        public boolean addAll(Collection<? extends E> es) {
          throw new UnsupportedOperationException();
        }
      };
  }
  
  private static <E> SortedSet<E> removeOnlySortedSet(final SortedSet<E> set) {
    return new ForwardingSortedSet<E>() {
        protected SortedSet<E> delegate() {
          return set;
        }
        
        public boolean add(E element) {
          throw new UnsupportedOperationException();
        }
        
        public boolean addAll(Collection<? extends E> es) {
          throw new UnsupportedOperationException();
        }
        
        public SortedSet<E> headSet(E toElement) {
          return Maps.removeOnlySortedSet(super.headSet(toElement));
        }
        
        public SortedSet<E> subSet(E fromElement, E toElement) {
          return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement));
        }
        
        public SortedSet<E> tailSet(E fromElement) {
          return Maps.removeOnlySortedSet(super.tailSet(fromElement));
        }
      };
  }
  
  @GwtIncompatible("NavigableSet")
  private static <E> NavigableSet<E> removeOnlyNavigableSet(final NavigableSet<E> set) {
    return new ForwardingNavigableSet<E>() {
        protected NavigableSet<E> delegate() {
          return set;
        }
        
        public boolean add(E element) {
          throw new UnsupportedOperationException();
        }
        
        public boolean addAll(Collection<? extends E> es) {
          throw new UnsupportedOperationException();
        }
        
        public SortedSet<E> headSet(E toElement) {
          return Maps.removeOnlySortedSet(super.headSet(toElement));
        }
        
        public SortedSet<E> subSet(E fromElement, E toElement) {
          return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement));
        }
        
        public SortedSet<E> tailSet(E fromElement) {
          return Maps.removeOnlySortedSet(super.tailSet(fromElement));
        }
        
        public NavigableSet<E> headSet(E toElement, boolean inclusive) {
          return Maps.removeOnlyNavigableSet(super.headSet(toElement, inclusive));
        }
        
        public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
          return Maps.removeOnlyNavigableSet(super.tailSet(fromElement, inclusive));
        }
        
        public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
          return Maps.removeOnlyNavigableSet(super.subSet(fromElement, fromInclusive, toElement, toInclusive));
        }
        
        public NavigableSet<E> descendingSet() {
          return Maps.removeOnlyNavigableSet(super.descendingSet());
        }
      };
  }
  
  @Beta
  public static <K, V> ImmutableMap<K, V> toMap(Iterable<K> keys, Function<? super K, V> valueFunction) {
    return toMap(keys.iterator(), valueFunction);
  }
  
  @Beta
  public static <K, V> ImmutableMap<K, V> toMap(Iterator<K> keys, Function<? super K, V> valueFunction) {
    Preconditions.checkNotNull(valueFunction);
    Map<K, V> builder = newLinkedHashMap();
    while (keys.hasNext()) {
      K key = keys.next();
      builder.put(key, (V)valueFunction.apply(key));
    } 
    return ImmutableMap.copyOf(builder);
  }
  
  public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> values, Function<? super V, K> keyFunction) {
    return uniqueIndex(values.iterator(), keyFunction);
  }
  
  public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> values, Function<? super V, K> keyFunction) {
    Preconditions.checkNotNull(keyFunction);
    ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
    while (values.hasNext()) {
      V value = values.next();
      builder.put((K)keyFunction.apply(value), value);
    } 
    return builder.build();
  }
  
  @GwtIncompatible("java.util.Properties")
  public static ImmutableMap<String, String> fromProperties(Properties properties) {
    ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
    for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); ) {
      String key = (String)e.nextElement();
      builder.put(key, properties.getProperty(key));
    } 
    return builder.build();
  }
  
  @GwtCompatible(serializable = true)
  public static <K, V> Map.Entry<K, V> immutableEntry(@Nullable K key, @Nullable V value) {
    return new ImmutableEntry<K, V>(key, value);
  }
  
  static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(Set<Map.Entry<K, V>> entrySet) {
    return new UnmodifiableEntrySet<K, V>(Collections.unmodifiableSet(entrySet));
  }
  
  static <K, V> Map.Entry<K, V> unmodifiableEntry(final Map.Entry<? extends K, ? extends V> entry) {
    Preconditions.checkNotNull(entry);
    return new AbstractMapEntry<K, V>() {
        public K getKey() {
          return (K)entry.getKey();
        }
        
        public V getValue() {
          return (V)entry.getValue();
        }
      };
  }
  
  static class UnmodifiableEntries<K, V> extends ForwardingCollection<Map.Entry<K, V>> {
    private final Collection<Map.Entry<K, V>> entries;
    
    UnmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
      this.entries = entries;
    }
    
    protected Collection<Map.Entry<K, V>> delegate() {
      return this.entries;
    }
    
    public Iterator<Map.Entry<K, V>> iterator() {
      final Iterator<Map.Entry<K, V>> delegate = super.iterator();
      return new UnmodifiableIterator<Map.Entry<K, V>>() {
          public boolean hasNext() {
            return delegate.hasNext();
          }
          
          public Map.Entry<K, V> next() {
            return Maps.unmodifiableEntry(delegate.next());
          }
        };
    }
    
    public Object[] toArray() {
      return standardToArray();
    }
    
    public <T> T[] toArray(T[] array) {
      return (T[])standardToArray((Object[])array);
    }
  }
  
  static class UnmodifiableEntrySet<K, V> extends UnmodifiableEntries<K, V> implements Set<Map.Entry<K, V>> {
    UnmodifiableEntrySet(Set<Map.Entry<K, V>> entries) {
      super(entries);
    }
    
    public boolean equals(@Nullable Object object) {
      return Sets.equalsImpl(this, object);
    }
    
    public int hashCode() {
      return Sets.hashCodeImpl(this);
    }
  }
  
  @Beta
  public static <A, B> Converter<A, B> asConverter(BiMap<A, B> bimap) {
    return new BiMapConverter<A, B>(bimap);
  }
  
  private static final class BiMapConverter<A, B> extends Converter<A, B> implements Serializable {
    private final BiMap<A, B> bimap;
    
    private static final long serialVersionUID = 0L;
    
    BiMapConverter(BiMap<A, B> bimap) {
      this.bimap = (BiMap<A, B>)Preconditions.checkNotNull(bimap);
    }
    
    protected B doForward(A a) {
      return convert(this.bimap, a);
    }
    
    protected A doBackward(B b) {
      return convert(this.bimap.inverse(), b);
    }
    
    private static <X, Y> Y convert(BiMap<X, Y> bimap, X input) {
      Y output = bimap.get(input);
      Preconditions.checkArgument((output != null), "No non-null mapping present for input: %s", new Object[] { input });
      return output;
    }
    
    public boolean equals(@Nullable Object object) {
      if (object instanceof BiMapConverter) {
        BiMapConverter<?, ?> that = (BiMapConverter<?, ?>)object;
        return this.bimap.equals(that.bimap);
      } 
      return false;
    }
    
    public int hashCode() {
      return this.bimap.hashCode();
    }
    
    public String toString() {
      return "Maps.asConverter(" + this.bimap + ")";
    }
  }
  
  public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> bimap) {
    return Synchronized.biMap(bimap, null);
  }
  
  public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> bimap) {
    return new UnmodifiableBiMap<K, V>(bimap, null);
  }
  
  private static class UnmodifiableBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
    final Map<K, V> unmodifiableMap;
    
    final BiMap<? extends K, ? extends V> delegate;
    
    BiMap<V, K> inverse;
    
    transient Set<V> values;
    
    private static final long serialVersionUID = 0L;
    
    UnmodifiableBiMap(BiMap<? extends K, ? extends V> delegate, @Nullable BiMap<V, K> inverse) {
      this.unmodifiableMap = Collections.unmodifiableMap(delegate);
      this.delegate = delegate;
      this.inverse = inverse;
    }
    
    protected Map<K, V> delegate() {
      return this.unmodifiableMap;
    }
    
    public V forcePut(K key, V value) {
      throw new UnsupportedOperationException();
    }
    
    public BiMap<V, K> inverse() {
      BiMap<V, K> result = this.inverse;
      return (result == null) ? (this.inverse = new UnmodifiableBiMap(this.delegate.inverse(), this)) : result;
    }
    
    public Set<V> values() {
      Set<V> result = this.values;
      return (result == null) ? (this.values = Collections.unmodifiableSet(this.delegate.values())) : result;
    }
  }
  
  public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> fromMap, Function<? super V1, V2> function) {
    return transformEntries(fromMap, asEntryTransformer(function));
  }
  
  public static <K, V1, V2> SortedMap<K, V2> transformValues(SortedMap<K, V1> fromMap, Function<? super V1, V2> function) {
    return transformEntries(fromMap, asEntryTransformer(function));
  }
  
  @GwtIncompatible("NavigableMap")
  public static <K, V1, V2> NavigableMap<K, V2> transformValues(NavigableMap<K, V1> fromMap, Function<? super V1, V2> function) {
    return transformEntries(fromMap, asEntryTransformer(function));
  }
  
  public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
    if (fromMap instanceof SortedMap)
      return transformEntries((SortedMap<K, V1>)fromMap, transformer); 
    return new TransformedEntriesMap<K, V1, V2>(fromMap, transformer);
  }
  
  public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
    return Platform.mapsTransformEntriesSortedMap(fromMap, transformer);
  }
  
  @GwtIncompatible("NavigableMap")
  public static <K, V1, V2> NavigableMap<K, V2> transformEntries(NavigableMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
    return new TransformedEntriesNavigableMap<K, V1, V2>(fromMap, transformer);
  }
  
  static <K, V1, V2> SortedMap<K, V2> transformEntriesIgnoreNavigable(SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
    return new TransformedEntriesSortedMap<K, V1, V2>(fromMap, transformer);
  }
  
  static <K, V1, V2> EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> function) {
    Preconditions.checkNotNull(function);
    return new EntryTransformer<K, V1, V2>() {
        public V2 transformEntry(K key, V1 value) {
          return (V2)function.apply(value);
        }
      };
  }
  
  static <K, V1, V2> Function<V1, V2> asValueToValueFunction(final EntryTransformer<? super K, V1, V2> transformer, final K key) {
    Preconditions.checkNotNull(transformer);
    return new Function<V1, V2>() {
        public V2 apply(@Nullable V1 v1) {
          return transformer.transformEntry(key, v1);
        }
      };
  }
  
  static <K, V1, V2> Function<Map.Entry<K, V1>, V2> asEntryToValueFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
    Preconditions.checkNotNull(transformer);
    return new Function<Map.Entry<K, V1>, V2>() {
        public V2 apply(Map.Entry<K, V1> entry) {
          return (V2)transformer.transformEntry(entry.getKey(), entry.getValue());
        }
      };
  }
  
  static <V2, K, V1> Map.Entry<K, V2> transformEntry(final EntryTransformer<? super K, ? super V1, V2> transformer, final Map.Entry<K, V1> entry) {
    Preconditions.checkNotNull(transformer);
    Preconditions.checkNotNull(entry);
    return new AbstractMapEntry<K, V2>() {
        public K getKey() {
          return (K)entry.getKey();
        }
        
        public V2 getValue() {
          return (V2)transformer.transformEntry(entry.getKey(), entry.getValue());
        }
      };
  }
  
  static <K, V1, V2> Function<Map.Entry<K, V1>, Map.Entry<K, V2>> asEntryToEntryFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
    Preconditions.checkNotNull(transformer);
    return new Function<Map.Entry<K, V1>, Map.Entry<K, V2>>() {
        public Map.Entry<K, V2> apply(Map.Entry<K, V1> entry) {
          return Maps.transformEntry(transformer, entry);
        }
      };
  }
  
  static class TransformedEntriesMap<K, V1, V2> extends ImprovedAbstractMap<K, V2> {
    final Map<K, V1> fromMap;
    
    final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;
    
    TransformedEntriesMap(Map<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
      this.fromMap = (Map<K, V1>)Preconditions.checkNotNull(fromMap);
      this.transformer = (Maps.EntryTransformer<? super K, ? super V1, V2>)Preconditions.checkNotNull(transformer);
    }
    
    public int size() {
      return this.fromMap.size();
    }
    
    public boolean containsKey(Object key) {
      return this.fromMap.containsKey(key);
    }
    
    public V2 get(Object key) {
      V1 value = this.fromMap.get(key);
      return (value != null || this.fromMap.containsKey(key)) ? this.transformer.transformEntry((K)key, value) : null;
    }
    
    public V2 remove(Object key) {
      return this.fromMap.containsKey(key) ? this.transformer.transformEntry((K)key, this.fromMap.remove(key)) : null;
    }
    
    public void clear() {
      this.fromMap.clear();
    }
    
    public Set<K> keySet() {
      return this.fromMap.keySet();
    }
    
    protected Set<Map.Entry<K, V2>> createEntrySet() {
      return new Maps.EntrySet<K, V2>() {
          Map<K, V2> map() {
            return Maps.TransformedEntriesMap.this;
          }
          
          public Iterator<Map.Entry<K, V2>> iterator() {
            return Iterators.transform(Maps.TransformedEntriesMap.this.fromMap.entrySet().iterator(), (Function)Maps.asEntryToEntryFunction(Maps.TransformedEntriesMap.this.transformer));
          }
        };
    }
  }
  
  static class TransformedEntriesSortedMap<K, V1, V2> extends TransformedEntriesMap<K, V1, V2> implements SortedMap<K, V2> {
    protected SortedMap<K, V1> fromMap() {
      return (SortedMap<K, V1>)this.fromMap;
    }
    
    TransformedEntriesSortedMap(SortedMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
      super(fromMap, transformer);
    }
    
    public Comparator<? super K> comparator() {
      return fromMap().comparator();
    }
    
    public K firstKey() {
      return fromMap().firstKey();
    }
    
    public SortedMap<K, V2> headMap(K toKey) {
      return Maps.transformEntries(fromMap().headMap(toKey), this.transformer);
    }
    
    public K lastKey() {
      return fromMap().lastKey();
    }
    
    public SortedMap<K, V2> subMap(K fromKey, K toKey) {
      return Maps.transformEntries(fromMap().subMap(fromKey, toKey), this.transformer);
    }
    
    public SortedMap<K, V2> tailMap(K fromKey) {
      return Maps.transformEntries(fromMap().tailMap(fromKey), this.transformer);
    }
  }
  
  @GwtIncompatible("NavigableMap")
  private static class TransformedEntriesNavigableMap<K, V1, V2> extends TransformedEntriesSortedMap<K, V1, V2> implements NavigableMap<K, V2> {
    TransformedEntriesNavigableMap(NavigableMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
      super(fromMap, transformer);
    }
    
    public Map.Entry<K, V2> ceilingEntry(K key) {
      return transformEntry(fromMap().ceilingEntry(key));
    }
    
    public K ceilingKey(K key) {
      return fromMap().ceilingKey(key);
    }
    
    public NavigableSet<K> descendingKeySet() {
      return fromMap().descendingKeySet();
    }
    
    public NavigableMap<K, V2> descendingMap() {
      return Maps.transformEntries(fromMap().descendingMap(), this.transformer);
    }
    
    public Map.Entry<K, V2> firstEntry() {
      return transformEntry(fromMap().firstEntry());
    }
    
    public Map.Entry<K, V2> floorEntry(K key) {
      return transformEntry(fromMap().floorEntry(key));
    }
    
    public K floorKey(K key) {
      return fromMap().floorKey(key);
    }
    
    public NavigableMap<K, V2> headMap(K toKey) {
      return headMap(toKey, false);
    }
    
    public NavigableMap<K, V2> headMap(K toKey, boolean inclusive) {
      return Maps.transformEntries(fromMap().headMap(toKey, inclusive), this.transformer);
    }
    
    public Map.Entry<K, V2> higherEntry(K key) {
      return transformEntry(fromMap().higherEntry(key));
    }
    
    public K higherKey(K key) {
      return fromMap().higherKey(key);
    }
    
    public Map.Entry<K, V2> lastEntry() {
      return transformEntry(fromMap().lastEntry());
    }
    
    public Map.Entry<K, V2> lowerEntry(K key) {
      return transformEntry(fromMap().lowerEntry(key));
    }
    
    public K lowerKey(K key) {
      return fromMap().lowerKey(key);
    }
    
    public NavigableSet<K> navigableKeySet() {
      return fromMap().navigableKeySet();
    }
    
    public Map.Entry<K, V2> pollFirstEntry() {
      return transformEntry(fromMap().pollFirstEntry());
    }
    
    public Map.Entry<K, V2> pollLastEntry() {
      return transformEntry(fromMap().pollLastEntry());
    }
    
    public NavigableMap<K, V2> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
      return Maps.transformEntries(fromMap().subMap(fromKey, fromInclusive, toKey, toInclusive), this.transformer);
    }
    
    public NavigableMap<K, V2> subMap(K fromKey, K toKey) {
      return subMap(fromKey, true, toKey, false);
    }
    
    public NavigableMap<K, V2> tailMap(K fromKey) {
      return tailMap(fromKey, true);
    }
    
    public NavigableMap<K, V2> tailMap(K fromKey, boolean inclusive) {
      return Maps.transformEntries(fromMap().tailMap(fromKey, inclusive), this.transformer);
    }
    
    @Nullable
    private Map.Entry<K, V2> transformEntry(@Nullable Map.Entry<K, V1> entry) {
      return (entry == null) ? null : Maps.<V2, K, V1>transformEntry(this.transformer, entry);
    }
    
    protected NavigableMap<K, V1> fromMap() {
      return (NavigableMap<K, V1>)super.fromMap();
    }
  }
  
  static <K> Predicate<Map.Entry<K, ?>> keyPredicateOnEntries(Predicate<? super K> keyPredicate) {
    return Predicates.compose(keyPredicate, keyFunction());
  }
  
  static <V> Predicate<Map.Entry<?, V>> valuePredicateOnEntries(Predicate<? super V> valuePredicate) {
    return Predicates.compose(valuePredicate, valueFunction());
  }
  
  public static <K, V> Map<K, V> filterKeys(Map<K, V> unfiltered, Predicate<? super K> keyPredicate) {
    if (unfiltered instanceof SortedMap)
      return filterKeys((SortedMap<K, V>)unfiltered, keyPredicate); 
    if (unfiltered instanceof BiMap)
      return filterKeys((BiMap<K, V>)unfiltered, keyPredicate); 
    Preconditions.checkNotNull(keyPredicate);
    Predicate<Map.Entry<K, ?>> entryPredicate = keyPredicateOnEntries(keyPredicate);
    return (unfiltered instanceof AbstractFilteredMap) ? filterFiltered((AbstractFilteredMap<K, V>)unfiltered, (Predicate)entryPredicate) : new FilteredKeyMap<K, V>((Map<K, V>)Preconditions.checkNotNull(unfiltered), keyPredicate, (Predicate)entryPredicate);
  }
  
  public static <K, V> SortedMap<K, V> filterKeys(SortedMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
    return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
  }
  
  @GwtIncompatible("NavigableMap")
  public static <K, V> NavigableMap<K, V> filterKeys(NavigableMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
    return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
  }
  
  public static <K, V> BiMap<K, V> filterKeys(BiMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
    Preconditions.checkNotNull(keyPredicate);
    return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
  }
  
  public static <K, V> Map<K, V> filterValues(Map<K, V> unfiltered, Predicate<? super V> valuePredicate) {
    if (unfiltered instanceof SortedMap)
      return filterValues((SortedMap<K, V>)unfiltered, valuePredicate); 
    if (unfiltered instanceof BiMap)
      return filterValues((BiMap<K, V>)unfiltered, valuePredicate); 
    return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
  }
  
  public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
    return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
  }
  
  @GwtIncompatible("NavigableMap")
  public static <K, V> NavigableMap<K, V> filterValues(NavigableMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
    return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
  }
  
  public static <K, V> BiMap<K, V> filterValues(BiMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
    return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
  }
  
  public static <K, V> Map<K, V> filterEntries(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
    if (unfiltered instanceof SortedMap)
      return filterEntries((SortedMap<K, V>)unfiltered, entryPredicate); 
    if (unfiltered instanceof BiMap)
      return filterEntries((BiMap<K, V>)unfiltered, entryPredicate); 
    Preconditions.checkNotNull(entryPredicate);
    return (unfiltered instanceof AbstractFilteredMap) ? filterFiltered((AbstractFilteredMap<K, V>)unfiltered, entryPredicate) : new FilteredEntryMap<K, V>((Map<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
  }
  
  public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
    return Platform.mapsFilterSortedMap(unfiltered, entryPredicate);
  }
  
  static <K, V> SortedMap<K, V> filterSortedIgnoreNavigable(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
    Preconditions.checkNotNull(entryPredicate);
    return (unfiltered instanceof FilteredEntrySortedMap) ? filterFiltered((FilteredEntrySortedMap<K, V>)unfiltered, entryPredicate) : new FilteredEntrySortedMap<K, V>((SortedMap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
  }
  
  @GwtIncompatible("NavigableMap")
  public static <K, V> NavigableMap<K, V> filterEntries(NavigableMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
    Preconditions.checkNotNull(entryPredicate);
    return (unfiltered instanceof FilteredEntryNavigableMap) ? filterFiltered((FilteredEntryNavigableMap<K, V>)unfiltered, entryPredicate) : new FilteredEntryNavigableMap<K, V>((NavigableMap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
  }
  
  public static <K, V> BiMap<K, V> filterEntries(BiMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
    Preconditions.checkNotNull(unfiltered);
    Preconditions.checkNotNull(entryPredicate);
    return (unfiltered instanceof FilteredEntryBiMap) ? filterFiltered((FilteredEntryBiMap<K, V>)unfiltered, entryPredicate) : new FilteredEntryBiMap<K, V>(unfiltered, entryPredicate);
  }
  
  private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
    return new FilteredEntryMap<K, V>(map.unfiltered, Predicates.and(map.predicate, entryPredicate));
  }
  
  private static abstract class AbstractFilteredMap<K, V> extends ImprovedAbstractMap<K, V> {
    final Map<K, V> unfiltered;
    
    final Predicate<? super Map.Entry<K, V>> predicate;
    
    AbstractFilteredMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
      this.unfiltered = unfiltered;
      this.predicate = predicate;
    }
    
    boolean apply(@Nullable Object key, @Nullable V value) {
      K k = (K)key;
      return this.predicate.apply(Maps.immutableEntry(k, value));
    }
    
    public V put(K key, V value) {
      Preconditions.checkArgument(apply(key, value));
      return this.unfiltered.put(key, value);
    }
    
    public void putAll(Map<? extends K, ? extends V> map) {
      for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
        Preconditions.checkArgument(apply(entry.getKey(), entry.getValue())); 
      this.unfiltered.putAll(map);
    }
    
    public boolean containsKey(Object key) {
      return (this.unfiltered.containsKey(key) && apply(key, this.unfiltered.get(key)));
    }
    
    public V get(Object key) {
      V value = this.unfiltered.get(key);
      return (value != null && apply(key, value)) ? value : null;
    }
    
    public boolean isEmpty() {
      return entrySet().isEmpty();
    }
    
    public V remove(Object key) {
      return containsKey(key) ? this.unfiltered.remove(key) : null;
    }
    
    Collection<V> createValues() {
      return new Maps.FilteredMapValues<K, V>(this, this.unfiltered, this.predicate);
    }
  }
  
  private static final class FilteredMapValues<K, V> extends Values<K, V> {
    Map<K, V> unfiltered;
    
    Predicate<? super Map.Entry<K, V>> predicate;
    
    FilteredMapValues(Map<K, V> filteredMap, Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
      super(filteredMap);
      this.unfiltered = unfiltered;
      this.predicate = predicate;
    }
    
    public boolean remove(Object o) {
      return (Iterables.removeFirstMatching(this.unfiltered.entrySet(), Predicates.and(this.predicate, Maps.valuePredicateOnEntries(Predicates.equalTo(o)))) != null);
    }
    
    private boolean removeIf(Predicate<? super V> valuePredicate) {
      return Iterables.removeIf(this.unfiltered.entrySet(), Predicates.and(this.predicate, Maps.valuePredicateOnEntries(valuePredicate)));
    }
    
    public boolean removeAll(Collection<?> collection) {
      return removeIf(Predicates.in(collection));
    }
    
    public boolean retainAll(Collection<?> collection) {
      return removeIf(Predicates.not(Predicates.in(collection)));
    }
    
    public Object[] toArray() {
      return Lists.<V>newArrayList(iterator()).toArray();
    }
    
    public <T> T[] toArray(T[] array) {
      return (T[])Lists.<V>newArrayList(iterator()).toArray((Object[])array);
    }
  }
  
  private static class FilteredKeyMap<K, V> extends AbstractFilteredMap<K, V> {
    Predicate<? super K> keyPredicate;
    
    FilteredKeyMap(Map<K, V> unfiltered, Predicate<? super K> keyPredicate, Predicate<? super Map.Entry<K, V>> entryPredicate) {
      super(unfiltered, entryPredicate);
      this.keyPredicate = keyPredicate;
    }
    
    protected Set<Map.Entry<K, V>> createEntrySet() {
      return Sets.filter(this.unfiltered.entrySet(), this.predicate);
    }
    
    Set<K> createKeySet() {
      return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
    }
    
    public boolean containsKey(Object key) {
      return (this.unfiltered.containsKey(key) && this.keyPredicate.apply(key));
    }
  }
  
  static class FilteredEntryMap<K, V> extends AbstractFilteredMap<K, V> {
    final Set<Map.Entry<K, V>> filteredEntrySet;
    
    FilteredEntryMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
      super(unfiltered, entryPredicate);
      this.filteredEntrySet = Sets.filter(unfiltered.entrySet(), this.predicate);
    }
    
    protected Set<Map.Entry<K, V>> createEntrySet() {
      return new EntrySet();
    }
    
    private class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
      private EntrySet() {}
      
      protected Set<Map.Entry<K, V>> delegate() {
        return Maps.FilteredEntryMap.this.filteredEntrySet;
      }
      
      public Iterator<Map.Entry<K, V>> iterator() {
        return new TransformedIterator<Map.Entry<K, V>, Map.Entry<K, V>>(Maps.FilteredEntryMap.this.filteredEntrySet.iterator()) {
            Map.Entry<K, V> transform(final Map.Entry<K, V> entry) {
              return new ForwardingMapEntry<K, V>() {
                  protected Map.Entry<K, V> delegate() {
                    return entry;
                  }
                  
                  public V setValue(V newValue) {
                    Preconditions.checkArgument(Maps.FilteredEntryMap.this.apply(getKey(), newValue));
                    return super.setValue(newValue);
                  }
                };
            }
          };
      }
    }
    
    Set<K> createKeySet() {
      return new KeySet();
    }
    
    class KeySet extends Maps.KeySet<K, V> {
      KeySet() {
        super(Maps.FilteredEntryMap.this);
      }
      
      public boolean remove(Object o) {
        if (Maps.FilteredEntryMap.this.containsKey(o)) {
          Maps.FilteredEntryMap.this.unfiltered.remove(o);
          return true;
        } 
        return false;
      }
      
      private boolean removeIf(Predicate<? super K> keyPredicate) {
        return Iterables.removeIf(Maps.FilteredEntryMap.this.unfiltered.entrySet(), Predicates.and(Maps.FilteredEntryMap.this.predicate, Maps.keyPredicateOnEntries(keyPredicate)));
      }
      
      public boolean removeAll(Collection<?> c) {
        return removeIf(Predicates.in(c));
      }
      
      public boolean retainAll(Collection<?> c) {
        return removeIf(Predicates.not(Predicates.in(c)));
      }
      
      public Object[] toArray() {
        return Lists.<K>newArrayList(iterator()).toArray();
      }
      
      public <T> T[] toArray(T[] array) {
        return (T[])Lists.<K>newArrayList(iterator()).toArray((Object[])array);
      }
    }
  }
  
  private static <K, V> SortedMap<K, V> filterFiltered(FilteredEntrySortedMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
    Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
    return new FilteredEntrySortedMap<K, V>(map.sortedMap(), predicate);
  }
  
  private static class FilteredEntrySortedMap<K, V> extends FilteredEntryMap<K, V> implements SortedMap<K, V> {
    FilteredEntrySortedMap(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
      super(unfiltered, entryPredicate);
    }
    
    SortedMap<K, V> sortedMap() {
      return (SortedMap<K, V>)this.unfiltered;
    }
    
    public SortedSet<K> keySet() {
      return (SortedSet<K>)super.keySet();
    }
    
    SortedSet<K> createKeySet() {
      return new SortedKeySet();
    }
    
    class SortedKeySet extends Maps.FilteredEntryMap<K, V>.KeySet implements SortedSet<K> {
      public Comparator<? super K> comparator() {
        return Maps.FilteredEntrySortedMap.this.sortedMap().comparator();
      }
      
      public SortedSet<K> subSet(K fromElement, K toElement) {
        return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.subMap(fromElement, toElement).keySet();
      }
      
      public SortedSet<K> headSet(K toElement) {
        return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.headMap(toElement).keySet();
      }
      
      public SortedSet<K> tailSet(K fromElement) {
        return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.tailMap(fromElement).keySet();
      }
      
      public K first() {
        return (K)Maps.FilteredEntrySortedMap.this.firstKey();
      }
      
      public K last() {
        return (K)Maps.FilteredEntrySortedMap.this.lastKey();
      }
    }
    
    public Comparator<? super K> comparator() {
      return sortedMap().comparator();
    }
    
    public K firstKey() {
      return keySet().iterator().next();
    }
    
    public K lastKey() {
      SortedMap<K, V> headMap = sortedMap();
      while (true) {
        K key = headMap.lastKey();
        if (apply(key, this.unfiltered.get(key)))
          return key; 
        headMap = sortedMap().headMap(key);
      } 
    }
    
    public SortedMap<K, V> headMap(K toKey) {
      return new FilteredEntrySortedMap(sortedMap().headMap(toKey), this.predicate);
    }
    
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
      return new FilteredEntrySortedMap(sortedMap().subMap(fromKey, toKey), this.predicate);
    }
    
    public SortedMap<K, V> tailMap(K fromKey) {
      return new FilteredEntrySortedMap(sortedMap().tailMap(fromKey), this.predicate);
    }
  }
  
  @GwtIncompatible("NavigableMap")
  private static <K, V> NavigableMap<K, V> filterFiltered(FilteredEntryNavigableMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
    Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.entryPredicate, entryPredicate);
    return new FilteredEntryNavigableMap<K, V>(map.unfiltered, predicate);
  }
  
  @GwtIncompatible("NavigableMap")
  private static class FilteredEntryNavigableMap<K, V> extends AbstractNavigableMap<K, V> {
    private final NavigableMap<K, V> unfiltered;
    
    private final Predicate<? super Map.Entry<K, V>> entryPredicate;
    
    private final Map<K, V> filteredDelegate;
    
    FilteredEntryNavigableMap(NavigableMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
      this.unfiltered = (NavigableMap<K, V>)Preconditions.checkNotNull(unfiltered);
      this.entryPredicate = entryPredicate;
      this.filteredDelegate = new Maps.FilteredEntryMap<K, V>(unfiltered, entryPredicate);
    }
    
    public Comparator<? super K> comparator() {
      return this.unfiltered.comparator();
    }
    
    public NavigableSet<K> navigableKeySet() {
      return new Maps.NavigableKeySet<K, V>(this) {
          public boolean removeAll(Collection<?> c) {
            return Iterators.removeIf(Maps.FilteredEntryNavigableMap.this.unfiltered.entrySet().iterator(), Predicates.and(Maps.FilteredEntryNavigableMap.this.entryPredicate, Maps.keyPredicateOnEntries(Predicates.in(c))));
          }
          
          public boolean retainAll(Collection<?> c) {
            return Iterators.removeIf(Maps.FilteredEntryNavigableMap.this.unfiltered.entrySet().iterator(), Predicates.and(Maps.FilteredEntryNavigableMap.this.entryPredicate, Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c)))));
          }
        };
    }
    
    public Collection<V> values() {
      return new Maps.FilteredMapValues<K, V>(this, this.unfiltered, this.entryPredicate);
    }
    
    Iterator<Map.Entry<K, V>> entryIterator() {
      return Iterators.filter(this.unfiltered.entrySet().iterator(), this.entryPredicate);
    }
    
    Iterator<Map.Entry<K, V>> descendingEntryIterator() {
      return Iterators.filter(this.unfiltered.descendingMap().entrySet().iterator(), this.entryPredicate);
    }
    
    public int size() {
      return this.filteredDelegate.size();
    }
    
    @Nullable
    public V get(@Nullable Object key) {
      return this.filteredDelegate.get(key);
    }
    
    public boolean containsKey(@Nullable Object key) {
      return this.filteredDelegate.containsKey(key);
    }
    
    public V put(K key, V value) {
      return this.filteredDelegate.put(key, value);
    }
    
    public V remove(@Nullable Object key) {
      return this.filteredDelegate.remove(key);
    }
    
    public void putAll(Map<? extends K, ? extends V> m) {
      this.filteredDelegate.putAll(m);
    }
    
    public void clear() {
      this.filteredDelegate.clear();
    }
    
    public Set<Map.Entry<K, V>> entrySet() {
      return this.filteredDelegate.entrySet();
    }
    
    public Map.Entry<K, V> pollFirstEntry() {
      return Iterables.<Map.Entry<K, V>>removeFirstMatching(this.unfiltered.entrySet(), this.entryPredicate);
    }
    
    public Map.Entry<K, V> pollLastEntry() {
      return Iterables.<Map.Entry<K, V>>removeFirstMatching(this.unfiltered.descendingMap().entrySet(), this.entryPredicate);
    }
    
    public NavigableMap<K, V> descendingMap() {
      return Maps.filterEntries(this.unfiltered.descendingMap(), this.entryPredicate);
    }
    
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
      return Maps.filterEntries(this.unfiltered.subMap(fromKey, fromInclusive, toKey, toInclusive), this.entryPredicate);
    }
    
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
      return Maps.filterEntries(this.unfiltered.headMap(toKey, inclusive), this.entryPredicate);
    }
    
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
      return Maps.filterEntries(this.unfiltered.tailMap(fromKey, inclusive), this.entryPredicate);
    }
  }
  
  private static <K, V> BiMap<K, V> filterFiltered(FilteredEntryBiMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
    Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
    return new FilteredEntryBiMap<K, V>(map.unfiltered(), predicate);
  }
  
  static final class FilteredEntryBiMap<K, V> extends FilteredEntryMap<K, V> implements BiMap<K, V> {
    private final BiMap<V, K> inverse;
    
    private static <K, V> Predicate<Map.Entry<V, K>> inversePredicate(final Predicate<? super Map.Entry<K, V>> forwardPredicate) {
      return new Predicate<Map.Entry<V, K>>() {
          public boolean apply(Map.Entry<V, K> input) {
            return forwardPredicate.apply(Maps.immutableEntry(input.getValue(), input.getKey()));
          }
        };
    }
    
    FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Map.Entry<K, V>> predicate) {
      super(delegate, predicate);
      this.inverse = new FilteredEntryBiMap(delegate.inverse(), inversePredicate(predicate), this);
    }
    
    private FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Map.Entry<K, V>> predicate, BiMap<V, K> inverse) {
      super(delegate, predicate);
      this.inverse = inverse;
    }
    
    BiMap<K, V> unfiltered() {
      return (BiMap<K, V>)this.unfiltered;
    }
    
    public V forcePut(@Nullable K key, @Nullable V value) {
      Preconditions.checkArgument(apply(key, value));
      return unfiltered().forcePut(key, value);
    }
    
    public BiMap<V, K> inverse() {
      return this.inverse;
    }
    
    public Set<V> values() {
      return this.inverse.keySet();
    }
  }
  
  @GwtIncompatible("NavigableMap")
  public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, V> map) {
    Preconditions.checkNotNull(map);
    if (map instanceof UnmodifiableNavigableMap)
      return map; 
    return new UnmodifiableNavigableMap<K, V>(map);
  }
  
  @Nullable
  private static <K, V> Map.Entry<K, V> unmodifiableOrNull(@Nullable Map.Entry<K, V> entry) {
    return (entry == null) ? null : unmodifiableEntry(entry);
  }
  
  @GwtIncompatible("NavigableMap")
  static class UnmodifiableNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V>, Serializable {
    private final NavigableMap<K, V> delegate;
    
    private transient UnmodifiableNavigableMap<K, V> descendingMap;
    
    UnmodifiableNavigableMap(NavigableMap<K, V> delegate) {
      this.delegate = delegate;
    }
    
    UnmodifiableNavigableMap(NavigableMap<K, V> delegate, UnmodifiableNavigableMap<K, V> descendingMap) {
      this.delegate = delegate;
      this.descendingMap = descendingMap;
    }
    
    protected SortedMap<K, V> delegate() {
      return Collections.unmodifiableSortedMap(this.delegate);
    }
    
    public Map.Entry<K, V> lowerEntry(K key) {
      return Maps.unmodifiableOrNull(this.delegate.lowerEntry(key));
    }
    
    public K lowerKey(K key) {
      return this.delegate.lowerKey(key);
    }
    
    public Map.Entry<K, V> floorEntry(K key) {
      return Maps.unmodifiableOrNull(this.delegate.floorEntry(key));
    }
    
    public K floorKey(K key) {
      return this.delegate.floorKey(key);
    }
    
    public Map.Entry<K, V> ceilingEntry(K key) {
      return Maps.unmodifiableOrNull(this.delegate.ceilingEntry(key));
    }
    
    public K ceilingKey(K key) {
      return this.delegate.ceilingKey(key);
    }
    
    public Map.Entry<K, V> higherEntry(K key) {
      return Maps.unmodifiableOrNull(this.delegate.higherEntry(key));
    }
    
    public K higherKey(K key) {
      return this.delegate.higherKey(key);
    }
    
    public Map.Entry<K, V> firstEntry() {
      return Maps.unmodifiableOrNull(this.delegate.firstEntry());
    }
    
    public Map.Entry<K, V> lastEntry() {
      return Maps.unmodifiableOrNull(this.delegate.lastEntry());
    }
    
    public final Map.Entry<K, V> pollFirstEntry() {
      throw new UnsupportedOperationException();
    }
    
    public final Map.Entry<K, V> pollLastEntry() {
      throw new UnsupportedOperationException();
    }
    
    public NavigableMap<K, V> descendingMap() {
      UnmodifiableNavigableMap<K, V> result = this.descendingMap;
      return (result == null) ? (this.descendingMap = new UnmodifiableNavigableMap(this.delegate.descendingMap(), this)) : result;
    }
    
    public Set<K> keySet() {
      return navigableKeySet();
    }
    
    public NavigableSet<K> navigableKeySet() {
      return Sets.unmodifiableNavigableSet(this.delegate.navigableKeySet());
    }
    
    public NavigableSet<K> descendingKeySet() {
      return Sets.unmodifiableNavigableSet(this.delegate.descendingKeySet());
    }
    
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
      return subMap(fromKey, true, toKey, false);
    }
    
    public SortedMap<K, V> headMap(K toKey) {
      return headMap(toKey, false);
    }
    
    public SortedMap<K, V> tailMap(K fromKey) {
      return tailMap(fromKey, true);
    }
    
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
      return Maps.unmodifiableNavigableMap(this.delegate.subMap(fromKey, fromInclusive, toKey, toInclusive));
    }
    
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
      return Maps.unmodifiableNavigableMap(this.delegate.headMap(toKey, inclusive));
    }
    
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
      return Maps.unmodifiableNavigableMap(this.delegate.tailMap(fromKey, inclusive));
    }
  }
  
  @GwtIncompatible("NavigableMap")
  public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> navigableMap) {
    return Synchronized.navigableMap(navigableMap);
  }
  
  @GwtCompatible
  static abstract class ImprovedAbstractMap<K, V> extends AbstractMap<K, V> {
    private transient Set<Map.Entry<K, V>> entrySet;
    
    private transient Set<K> keySet;
    
    private transient Collection<V> values;
    
    abstract Set<Map.Entry<K, V>> createEntrySet();
    
    public Set<Map.Entry<K, V>> entrySet() {
      Set<Map.Entry<K, V>> result = this.entrySet;
      return (result == null) ? (this.entrySet = createEntrySet()) : result;
    }
    
    public Set<K> keySet() {
      Set<K> result = this.keySet;
      return (result == null) ? (this.keySet = createKeySet()) : result;
    }
    
    Set<K> createKeySet() {
      return new Maps.KeySet<K, V>(this);
    }
    
    public Collection<V> values() {
      Collection<V> result = this.values;
      return (result == null) ? (this.values = createValues()) : result;
    }
    
    Collection<V> createValues() {
      return new Maps.Values<K, V>(this);
    }
  }
  
  static <V> V safeGet(Map<?, V> map, @Nullable Object key) {
    Preconditions.checkNotNull(map);
    try {
      return map.get(key);
    } catch (ClassCastException e) {
      return null;
    } catch (NullPointerException e) {
      return null;
    } 
  }
  
  static boolean safeContainsKey(Map<?, ?> map, Object key) {
    Preconditions.checkNotNull(map);
    try {
      return map.containsKey(key);
    } catch (ClassCastException e) {
      return false;
    } catch (NullPointerException e) {
      return false;
    } 
  }
  
  static <V> V safeRemove(Map<?, V> map, Object key) {
    Preconditions.checkNotNull(map);
    try {
      return map.remove(key);
    } catch (ClassCastException e) {
      return null;
    } catch (NullPointerException e) {
      return null;
    } 
  }
  
  static boolean containsKeyImpl(Map<?, ?> map, @Nullable Object key) {
    return Iterators.contains(keyIterator(map.entrySet().iterator()), key);
  }
  
  static boolean containsValueImpl(Map<?, ?> map, @Nullable Object value) {
    return Iterators.contains(valueIterator(map.entrySet().iterator()), value);
  }
  
  static <K, V> boolean containsEntryImpl(Collection<Map.Entry<K, V>> c, Object o) {
    if (!(o instanceof Map.Entry))
      return false; 
    return c.contains(unmodifiableEntry((Map.Entry<?, ?>)o));
  }
  
  static <K, V> boolean removeEntryImpl(Collection<Map.Entry<K, V>> c, Object o) {
    if (!(o instanceof Map.Entry))
      return false; 
    return c.remove(unmodifiableEntry((Map.Entry<?, ?>)o));
  }
  
  static boolean equalsImpl(Map<?, ?> map, Object object) {
    if (map == object)
      return true; 
    if (object instanceof Map) {
      Map<?, ?> o = (Map<?, ?>)object;
      return map.entrySet().equals(o.entrySet());
    } 
    return false;
  }
  
  static final Joiner.MapJoiner STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");
  
  static String toStringImpl(Map<?, ?> map) {
    StringBuilder sb = Collections2.newStringBuilderForCollection(map.size()).append('{');
    STANDARD_JOINER.appendTo(sb, map);
    return sb.append('}').toString();
  }
  
  static <K, V> void putAllImpl(Map<K, V> self, Map<? extends K, ? extends V> map) {
    for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
      self.put(entry.getKey(), entry.getValue()); 
  }
  
  static class KeySet<K, V> extends Sets.ImprovedAbstractSet<K> {
    final Map<K, V> map;
    
    KeySet(Map<K, V> map) {
      this.map = (Map<K, V>)Preconditions.checkNotNull(map);
    }
    
    Map<K, V> map() {
      return this.map;
    }
    
    public Iterator<K> iterator() {
      return Maps.keyIterator(map().entrySet().iterator());
    }
    
    public int size() {
      return map().size();
    }
    
    public boolean isEmpty() {
      return map().isEmpty();
    }
    
    public boolean contains(Object o) {
      return map().containsKey(o);
    }
    
    public boolean remove(Object o) {
      if (contains(o)) {
        map().remove(o);
        return true;
      } 
      return false;
    }
    
    public void clear() {
      map().clear();
    }
  }
  
  @Nullable
  static <K> K keyOrNull(@Nullable Map.Entry<K, ?> entry) {
    return (entry == null) ? null : entry.getKey();
  }
  
  @Nullable
  static <V> V valueOrNull(@Nullable Map.Entry<?, V> entry) {
    return (entry == null) ? null : entry.getValue();
  }
  
  static class SortedKeySet<K, V> extends KeySet<K, V> implements SortedSet<K> {
    SortedKeySet(SortedMap<K, V> map) {
      super(map);
    }
    
    SortedMap<K, V> map() {
      return (SortedMap<K, V>)super.map();
    }
    
    public Comparator<? super K> comparator() {
      return map().comparator();
    }
    
    public SortedSet<K> subSet(K fromElement, K toElement) {
      return new SortedKeySet(map().subMap(fromElement, toElement));
    }
    
    public SortedSet<K> headSet(K toElement) {
      return new SortedKeySet(map().headMap(toElement));
    }
    
    public SortedSet<K> tailSet(K fromElement) {
      return new SortedKeySet(map().tailMap(fromElement));
    }
    
    public K first() {
      return map().firstKey();
    }
    
    public K last() {
      return map().lastKey();
    }
  }
  
  @GwtIncompatible("NavigableMap")
  static class NavigableKeySet<K, V> extends SortedKeySet<K, V> implements NavigableSet<K> {
    NavigableKeySet(NavigableMap<K, V> map) {
      super(map);
    }
    
    NavigableMap<K, V> map() {
      return (NavigableMap<K, V>)this.map;
    }
    
    public K lower(K e) {
      return map().lowerKey(e);
    }
    
    public K floor(K e) {
      return map().floorKey(e);
    }
    
    public K ceiling(K e) {
      return map().ceilingKey(e);
    }
    
    public K higher(K e) {
      return map().higherKey(e);
    }
    
    public K pollFirst() {
      return Maps.keyOrNull(map().pollFirstEntry());
    }
    
    public K pollLast() {
      return Maps.keyOrNull(map().pollLastEntry());
    }
    
    public NavigableSet<K> descendingSet() {
      return map().descendingKeySet();
    }
    
    public Iterator<K> descendingIterator() {
      return descendingSet().iterator();
    }
    
    public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
      return map().subMap(fromElement, fromInclusive, toElement, toInclusive).navigableKeySet();
    }
    
    public NavigableSet<K> headSet(K toElement, boolean inclusive) {
      return map().headMap(toElement, inclusive).navigableKeySet();
    }
    
    public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
      return map().tailMap(fromElement, inclusive).navigableKeySet();
    }
    
    public SortedSet<K> subSet(K fromElement, K toElement) {
      return subSet(fromElement, true, toElement, false);
    }
    
    public SortedSet<K> headSet(K toElement) {
      return headSet(toElement, false);
    }
    
    public SortedSet<K> tailSet(K fromElement) {
      return tailSet(fromElement, true);
    }
  }
  
  static class Values<K, V> extends AbstractCollection<V> {
    final Map<K, V> map;
    
    Values(Map<K, V> map) {
      this.map = (Map<K, V>)Preconditions.checkNotNull(map);
    }
    
    final Map<K, V> map() {
      return this.map;
    }
    
    public Iterator<V> iterator() {
      return Maps.valueIterator(map().entrySet().iterator());
    }
    
    public boolean remove(Object o) {
      try {
        return super.remove(o);
      } catch (UnsupportedOperationException e) {
        for (Map.Entry<K, V> entry : map().entrySet()) {
          if (Objects.equal(o, entry.getValue())) {
            map().remove(entry.getKey());
            return true;
          } 
        } 
        return false;
      } 
    }
    
    public boolean removeAll(Collection<?> c) {
      try {
        return super.removeAll((Collection)Preconditions.checkNotNull(c));
      } catch (UnsupportedOperationException e) {
        Set<K> toRemove = Sets.newHashSet();
        for (Map.Entry<K, V> entry : map().entrySet()) {
          if (c.contains(entry.getValue()))
            toRemove.add(entry.getKey()); 
        } 
        return map().keySet().removeAll(toRemove);
      } 
    }
    
    public boolean retainAll(Collection<?> c) {
      try {
        return super.retainAll((Collection)Preconditions.checkNotNull(c));
      } catch (UnsupportedOperationException e) {
        Set<K> toRetain = Sets.newHashSet();
        for (Map.Entry<K, V> entry : map().entrySet()) {
          if (c.contains(entry.getValue()))
            toRetain.add(entry.getKey()); 
        } 
        return map().keySet().retainAll(toRetain);
      } 
    }
    
    public int size() {
      return map().size();
    }
    
    public boolean isEmpty() {
      return map().isEmpty();
    }
    
    public boolean contains(@Nullable Object o) {
      return map().containsValue(o);
    }
    
    public void clear() {
      map().clear();
    }
  }
  
  static abstract class EntrySet<K, V> extends Sets.ImprovedAbstractSet<Map.Entry<K, V>> {
    abstract Map<K, V> map();
    
    public int size() {
      return map().size();
    }
    
    public void clear() {
      map().clear();
    }
    
    public boolean contains(Object o) {
      if (o instanceof Map.Entry) {
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
        Object key = entry.getKey();
        V value = Maps.safeGet(map(), key);
        return (Objects.equal(value, entry.getValue()) && (value != null || map().containsKey(key)));
      } 
      return false;
    }
    
    public boolean isEmpty() {
      return map().isEmpty();
    }
    
    public boolean remove(Object o) {
      if (contains(o)) {
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
        return map().keySet().remove(entry.getKey());
      } 
      return false;
    }
    
    public boolean removeAll(Collection<?> c) {
      try {
        return super.removeAll((Collection)Preconditions.checkNotNull(c));
      } catch (UnsupportedOperationException e) {
        return Sets.removeAllImpl(this, c.iterator());
      } 
    }
    
    public boolean retainAll(Collection<?> c) {
      try {
        return super.retainAll((Collection)Preconditions.checkNotNull(c));
      } catch (UnsupportedOperationException e) {
        Set<Object> keys = Sets.newHashSetWithExpectedSize(c.size());
        for (Object o : c) {
          if (contains(o)) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
            keys.add(entry.getKey());
          } 
        } 
        return map().keySet().retainAll(keys);
      } 
    }
  }
  
  @GwtIncompatible("NavigableMap")
  static abstract class DescendingMap<K, V> extends ForwardingMap<K, V> implements NavigableMap<K, V> {
    private transient Comparator<? super K> comparator;
    
    private transient Set<Map.Entry<K, V>> entrySet;
    
    private transient NavigableSet<K> navigableKeySet;
    
    protected final Map<K, V> delegate() {
      return forward();
    }
    
    public Comparator<? super K> comparator() {
      Comparator<? super K> result = this.comparator;
      if (result == null) {
        Comparator<? super K> forwardCmp = forward().comparator();
        if (forwardCmp == null)
          forwardCmp = Ordering.natural(); 
        result = this.comparator = reverse(forwardCmp);
      } 
      return result;
    }
    
    private static <T> Ordering<T> reverse(Comparator<T> forward) {
      return Ordering.<T>from(forward).reverse();
    }
    
    public K firstKey() {
      return forward().lastKey();
    }
    
    public K lastKey() {
      return forward().firstKey();
    }
    
    public Map.Entry<K, V> lowerEntry(K key) {
      return forward().higherEntry(key);
    }
    
    public K lowerKey(K key) {
      return forward().higherKey(key);
    }
    
    public Map.Entry<K, V> floorEntry(K key) {
      return forward().ceilingEntry(key);
    }
    
    public K floorKey(K key) {
      return forward().ceilingKey(key);
    }
    
    public Map.Entry<K, V> ceilingEntry(K key) {
      return forward().floorEntry(key);
    }
    
    public K ceilingKey(K key) {
      return forward().floorKey(key);
    }
    
    public Map.Entry<K, V> higherEntry(K key) {
      return forward().lowerEntry(key);
    }
    
    public K higherKey(K key) {
      return forward().lowerKey(key);
    }
    
    public Map.Entry<K, V> firstEntry() {
      return forward().lastEntry();
    }
    
    public Map.Entry<K, V> lastEntry() {
      return forward().firstEntry();
    }
    
    public Map.Entry<K, V> pollFirstEntry() {
      return forward().pollLastEntry();
    }
    
    public Map.Entry<K, V> pollLastEntry() {
      return forward().pollFirstEntry();
    }
    
    public NavigableMap<K, V> descendingMap() {
      return forward();
    }
    
    public Set<Map.Entry<K, V>> entrySet() {
      Set<Map.Entry<K, V>> result = this.entrySet;
      return (result == null) ? (this.entrySet = createEntrySet()) : result;
    }
    
    Set<Map.Entry<K, V>> createEntrySet() {
      return new Maps.EntrySet<K, V>() {
          Map<K, V> map() {
            return Maps.DescendingMap.this;
          }
          
          public Iterator<Map.Entry<K, V>> iterator() {
            return Maps.DescendingMap.this.entryIterator();
          }
        };
    }
    
    public Set<K> keySet() {
      return navigableKeySet();
    }
    
    public NavigableSet<K> navigableKeySet() {
      NavigableSet<K> result = this.navigableKeySet;
      return (result == null) ? (this.navigableKeySet = new Maps.NavigableKeySet<K, V>(this)) : result;
    }
    
    public NavigableSet<K> descendingKeySet() {
      return forward().navigableKeySet();
    }
    
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
      return forward().subMap(toKey, toInclusive, fromKey, fromInclusive).descendingMap();
    }
    
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
      return forward().tailMap(toKey, inclusive).descendingMap();
    }
    
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
      return forward().headMap(fromKey, inclusive).descendingMap();
    }
    
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
      return subMap(fromKey, true, toKey, false);
    }
    
    public SortedMap<K, V> headMap(K toKey) {
      return headMap(toKey, false);
    }
    
    public SortedMap<K, V> tailMap(K fromKey) {
      return tailMap(fromKey, true);
    }
    
    public Collection<V> values() {
      return new Maps.Values<K, V>(this);
    }
    
    public String toString() {
      return standardToString();
    }
    
    abstract NavigableMap<K, V> forward();
    
    abstract Iterator<Map.Entry<K, V>> entryIterator();
  }
  
  public static interface EntryTransformer<K, V1, V2> {
    V2 transformEntry(@Nullable K param1K, @Nullable V1 param1V1);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\Maps.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */