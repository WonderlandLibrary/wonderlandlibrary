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

@GwtCompatible(
   emulated = true
)
public final class EnumBiMap extends AbstractBiMap {
   private transient Class keyType;
   private transient Class valueType;
   @GwtIncompatible("not needed in emulated source.")
   private static final long serialVersionUID = 0L;

   public static EnumBiMap create(Class var0, Class var1) {
      return new EnumBiMap(var0, var1);
   }

   public static EnumBiMap create(Map var0) {
      EnumBiMap var1 = create(inferKeyType(var0), inferValueType(var0));
      var1.putAll(var0);
      return var1;
   }

   private EnumBiMap(Class var1, Class var2) {
      super(WellBehavedMap.wrap(new EnumMap(var1)), (Map)WellBehavedMap.wrap(new EnumMap(var2)));
      this.keyType = var1;
      this.valueType = var2;
   }

   static Class inferKeyType(Map var0) {
      if (var0 instanceof EnumBiMap) {
         return ((EnumBiMap)var0).keyType();
      } else if (var0 instanceof EnumHashBiMap) {
         return ((EnumHashBiMap)var0).keyType();
      } else {
         Preconditions.checkArgument(!var0.isEmpty());
         return ((Enum)var0.keySet().iterator().next()).getDeclaringClass();
      }
   }

   private static Class inferValueType(Map var0) {
      if (var0 instanceof EnumBiMap) {
         return ((EnumBiMap)var0).valueType;
      } else {
         Preconditions.checkArgument(!var0.isEmpty());
         return ((Enum)var0.values().iterator().next()).getDeclaringClass();
      }
   }

   public Class keyType() {
      return this.keyType;
   }

   public Class valueType() {
      return this.valueType;
   }

   Enum checkKey(Enum var1) {
      return (Enum)Preconditions.checkNotNull(var1);
   }

   Enum checkValue(Enum var1) {
      return (Enum)Preconditions.checkNotNull(var1);
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(this.keyType);
      var1.writeObject(this.valueType);
      Serialization.writeMap(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.keyType = (Class)var1.readObject();
      this.valueType = (Class)var1.readObject();
      this.setDelegates(WellBehavedMap.wrap(new EnumMap(this.keyType)), WellBehavedMap.wrap(new EnumMap(this.valueType)));
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

   public boolean containsValue(Object var1) {
      return super.containsValue(var1);
   }

   Object checkValue(Object var1) {
      return this.checkValue((Enum)var1);
   }

   Object checkKey(Object var1) {
      return this.checkKey((Enum)var1);
   }
}
