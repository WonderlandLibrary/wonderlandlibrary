package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@GwtCompatible(emulated = true)
public final class EnumBiMap<K extends Enum<K>, V extends Enum<V>> extends AbstractBiMap<K, V> {
  private transient Class<K> keyType;
  
  private transient Class<V> valueType;
  
  @GwtIncompatible("not needed in emulated source.")
  private static final long serialVersionUID = 0L;
  
  public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Class<K> keyType, Class<V> valueType) {
    return new EnumBiMap<K, V>(keyType, valueType);
  }
  
  public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Map<K, V> map) {
    EnumBiMap<K, V> bimap = create(inferKeyType(map), inferValueType(map));
    bimap.putAll(map);
    return bimap;
  }
  
  private EnumBiMap(Class<K> keyType, Class<V> valueType) {
    super(WellBehavedMap.wrap(new EnumMap<K, V>(keyType)), WellBehavedMap.wrap((Map)new EnumMap<V, Object>(valueType)));
    this.keyType = keyType;
    this.valueType = valueType;
  }
  
  static <K extends Enum<K>> Class<K> inferKeyType(Map<K, ?> map) {
    if (map instanceof EnumBiMap)
      return ((EnumBiMap)map).keyType(); 
    if (map instanceof EnumHashBiMap)
      return ((EnumHashBiMap)map).keyType(); 
    Preconditions.checkArgument(!map.isEmpty());
    return ((Enum<K>)map.keySet().iterator().next()).getDeclaringClass();
  }
  
  private static <V extends Enum<V>> Class<V> inferValueType(Map<?, V> map) {
    if (map instanceof EnumBiMap)
      return ((EnumBiMap)map).valueType; 
    Preconditions.checkArgument(!map.isEmpty());
    return ((Enum<V>)map.values().iterator().next()).getDeclaringClass();
  }
  
  public Class<K> keyType() {
    return this.keyType;
  }
  
  public Class<V> valueType() {
    return this.valueType;
  }
  
  K checkKey(K key) {
    return (K)Preconditions.checkNotNull(key);
  }
  
  V checkValue(V value) {
    return (V)Preconditions.checkNotNull(value);
  }
  
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    stream.writeObject(this.keyType);
    stream.writeObject(this.valueType);
    Serialization.writeMap(this, stream);
  }
  
  @GwtIncompatible("java.io.ObjectInputStream")
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.keyType = (Class<K>)stream.readObject();
    this.valueType = (Class<V>)stream.readObject();
    setDelegates(WellBehavedMap.wrap(new EnumMap<K, V>(this.keyType)), WellBehavedMap.wrap((Map)new EnumMap<V, Object>(this.valueType)));
    Serialization.populateMap(this, stream);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\EnumBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */