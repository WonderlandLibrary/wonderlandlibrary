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

@GwtCompatible(
   emulated = true
)
public final class EnumHashBiMap extends AbstractBiMap {
   private transient Class keyType;
   @GwtIncompatible("only needed in emulated source.")
   private static final long serialVersionUID = 0L;

   public static EnumHashBiMap create(Class var0) {
      return new EnumHashBiMap(var0);
   }

   public static EnumHashBiMap create(Map var0) {
      EnumHashBiMap var1 = create(EnumBiMap.inferKeyType(var0));
      var1.putAll(var0);
      return var1;
   }

   private EnumHashBiMap(Class var1) {
      super(WellBehavedMap.wrap(new EnumMap(var1)), (Map)Maps.newHashMapWithExpectedSize(((Enum[])var1.getEnumConstants()).length));
      this.keyType = var1;
   }

   Enum checkKey(Enum var1) {
      return (Enum)Preconditions.checkNotNull(var1);
   }

   public Object put(Enum var1, @Nullable Object var2) {
      return super.put(var1, var2);
   }

   public Object forcePut(Enum var1, @Nullable Object var2) {
      return super.forcePut(var1, var2);
   }

   public Class keyType() {
      return this.keyType;
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(this.keyType);
      Serialization.writeMap(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.keyType = (Class)var1.readObject();
      this.setDelegates(WellBehavedMap.wrap(new EnumMap(this.keyType)), new HashMap(((Enum[])this.keyType.getEnumConstants()).length * 3 / 2));
      Serialization.populateMap(this, var1);
   }

   public Set entrySet() {
      return super.entrySet();
   }

   public Set values() {
      return super.values();
   }

   public Set keySet() {
      return super.keySet();
   }

   public BiMap inverse() {
      return super.inverse();
   }

   public void clear() {
      super.clear();
   }

   public void putAll(Map var1) {
      super.putAll(var1);
   }

   public Object remove(Object var1) {
      return super.remove(var1);
   }

   public Object forcePut(Object var1, Object var2) {
      return this.forcePut((Enum)var1, var2);
   }

   public Object put(Object var1, Object var2) {
      return this.put((Enum)var1, var2);
   }

   public boolean containsValue(Object var1) {
      return super.containsValue(var1);
   }

   Object checkKey(Object var1) {
      return this.checkKey((Enum)var1);
   }
}
