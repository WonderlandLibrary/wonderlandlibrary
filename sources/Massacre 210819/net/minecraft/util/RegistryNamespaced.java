package net.minecraft.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Iterator;
import java.util.Map;

public class RegistryNamespaced<K, V> extends RegistrySimple<K, V> implements IObjectIntIterable<V> {
   protected final ObjectIntIdentityMap underlyingIntegerMap = new ObjectIntIdentityMap();
   protected final Map<V, K> inverseObjectRegistry;

   public RegistryNamespaced() {
      this.inverseObjectRegistry = ((BiMap)this.registryObjects).inverse();
   }

   public void register(int id, K p_177775_2_, V p_177775_3_) {
      this.underlyingIntegerMap.put(p_177775_3_, id);
      this.putObject(p_177775_2_, p_177775_3_);
   }

   protected Map<K, V> createUnderlyingMap() {
      return HashBiMap.create();
   }

   public V getObject(K name) {
      return super.getObject(name);
   }

   public K getNameForObject(V p_177774_1_) {
      return this.inverseObjectRegistry.get(p_177774_1_);
   }

   public boolean containsKey(K p_148741_1_) {
      return super.containsKey(p_148741_1_);
   }

   public int getIDForObject(V p_148757_1_) {
      return this.underlyingIntegerMap.get(p_148757_1_);
   }

   public V getObjectById(int id) {
      return this.underlyingIntegerMap.getByValue(id);
   }

   public Iterator<V> iterator() {
      return this.underlyingIntegerMap.iterator();
   }
}
