package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class EnumHashBiMap<K extends Enum<K>, V> extends AbstractBiMap<K, V> {
  private transient Class<K> keyType;
  
  @GwtIncompatible("only needed in emulated source.")
  private static final long serialVersionUID = 0L;
  
  public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Class<K> keyType) {
    return new EnumHashBiMap<K, V>(keyType);
  }
  
  public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Map<K, ? extends V> map) {
    EnumHashBiMap<K, V> bimap = create(EnumBiMap.inferKeyType(map));
    bimap.putAll(map);
    return bimap;
  }
  
  private EnumHashBiMap(Class<K> keyType) {
    super(WellBehavedMap.wrap(new EnumMap<K, V>(keyType)), Maps.newHashMapWithExpectedSize(((Enum[])keyType.getEnumConstants()).length));
    this.keyType = keyType;
  }
  
  K checkKey(K key) {
    return (K)Preconditions.checkNotNull(key);
  }
  
  public V put(K key, @Nullable V value) {
    return super.put(key, value);
  }
  
  public V forcePut(K key, @Nullable V value) {
    return super.forcePut(key, value);
  }
  
  public Class<K> keyType() {
    return this.keyType;
  }
  
  @GwtIncompatible("java.io.ObjectOutputStream")
  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    stream.writeObject(this.keyType);
    Serialization.writeMap(this, stream);
  }
  
  @GwtIncompatible("java.io.ObjectInputStream")
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.keyType = (Class<K>)stream.readObject();
    setDelegates(WellBehavedMap.wrap(new EnumMap<K, V>(this.keyType)), new HashMap<V, K>(((Enum[])this.keyType.getEnumConstants()).length * 3 / 2));
    Serialization.populateMap(this, stream);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\EnumHashBiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */