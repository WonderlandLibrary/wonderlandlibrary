package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true,
   emulated = true
)
public class ImmutableListMultimap extends ImmutableMultimap implements ListMultimap {
   private transient ImmutableListMultimap inverse;
   @GwtIncompatible("Not needed in emulated source")
   private static final long serialVersionUID = 0L;

   public static ImmutableListMultimap of() {
      return EmptyImmutableListMultimap.INSTANCE;
   }

   public static ImmutableListMultimap of(Object var0, Object var1) {
      ImmutableListMultimap.Builder var2 = builder();
      var2.put(var0, var1);
      return var2.build();
   }

   public static ImmutableListMultimap of(Object var0, Object var1, Object var2, Object var3) {
      ImmutableListMultimap.Builder var4 = builder();
      var4.put(var0, var1);
      var4.put(var2, var3);
      return var4.build();
   }

   public static ImmutableListMultimap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5) {
      ImmutableListMultimap.Builder var6 = builder();
      var6.put(var0, var1);
      var6.put(var2, var3);
      var6.put(var4, var5);
      return var6.build();
   }

   public static ImmutableListMultimap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      ImmutableListMultimap.Builder var8 = builder();
      var8.put(var0, var1);
      var8.put(var2, var3);
      var8.put(var4, var5);
      var8.put(var6, var7);
      return var8.build();
   }

   public static ImmutableListMultimap of(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9) {
      ImmutableListMultimap.Builder var10 = builder();
      var10.put(var0, var1);
      var10.put(var2, var3);
      var10.put(var4, var5);
      var10.put(var6, var7);
      var10.put(var8, var9);
      return var10.build();
   }

   public static ImmutableListMultimap.Builder builder() {
      return new ImmutableListMultimap.Builder();
   }

   public static ImmutableListMultimap copyOf(Multimap var0) {
      if (var0.isEmpty()) {
         return of();
      } else {
         if (var0 instanceof ImmutableListMultimap) {
            ImmutableListMultimap var1 = (ImmutableListMultimap)var0;
            if (!var1.isPartialView()) {
               return var1;
            }
         }

         ImmutableMap.Builder var6 = ImmutableMap.builder();
         int var2 = 0;
         Iterator var3 = var0.asMap().entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            ImmutableList var5 = ImmutableList.copyOf((Collection)var4.getValue());
            if (!var5.isEmpty()) {
               var6.put(var4.getKey(), var5);
               var2 += var5.size();
            }
         }

         return new ImmutableListMultimap(var6.build(), var2);
      }
   }

   ImmutableListMultimap(ImmutableMap var1, int var2) {
      super(var1, var2);
   }

   public ImmutableList get(@Nullable Object var1) {
      ImmutableList var2 = (ImmutableList)this.map.get(var1);
      return var2 == null ? ImmutableList.of() : var2;
   }

   public ImmutableListMultimap inverse() {
      ImmutableListMultimap var1 = this.inverse;
      return var1 == null ? (this.inverse = this.invert()) : var1;
   }

   private ImmutableListMultimap invert() {
      ImmutableListMultimap.Builder var1 = builder();
      Iterator var2 = this.entries().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.put(var3.getValue(), var3.getKey());
      }

      ImmutableListMultimap var4 = var1.build();
      var4.inverse = this;
      return var4;
   }

   /** @deprecated */
   @Deprecated
   public ImmutableList removeAll(Object var1) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   public ImmutableList replaceValues(Object var1, Iterable var2) {
      throw new UnsupportedOperationException();
   }

   @GwtIncompatible("java.io.ObjectOutputStream")
   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      Serialization.writeMultimap(this, var1);
   }

   @GwtIncompatible("java.io.ObjectInputStream")
   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      int var2 = var1.readInt();
      if (var2 < 0) {
         throw new InvalidObjectException("Invalid key count " + var2);
      } else {
         ImmutableMap.Builder var3 = ImmutableMap.builder();
         int var4 = 0;

         for(int var5 = 0; var5 < var2; ++var5) {
            Object var6 = var1.readObject();
            int var7 = var1.readInt();
            if (var7 <= 0) {
               throw new InvalidObjectException("Invalid value count " + var7);
            }

            Object[] var8 = new Object[var7];

            for(int var9 = 0; var9 < var7; ++var9) {
               var8[var9] = var1.readObject();
            }

            var3.put(var6, ImmutableList.copyOf(var8));
            var4 += var7;
         }

         ImmutableMap var11;
         try {
            var11 = var3.build();
         } catch (IllegalArgumentException var10) {
            throw (InvalidObjectException)(new InvalidObjectException(var10.getMessage())).initCause(var10);
         }

         ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, var11);
         ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, var4);
      }
   }

   public ImmutableMultimap inverse() {
      return this.inverse();
   }

   public ImmutableCollection get(Object var1) {
      return this.get(var1);
   }

   public ImmutableCollection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   public ImmutableCollection removeAll(Object var1) {
      return this.removeAll(var1);
   }

   public Collection replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   public Collection get(Object var1) {
      return this.get(var1);
   }

   public Collection removeAll(Object var1) {
      return this.removeAll(var1);
   }

   public List replaceValues(Object var1, Iterable var2) {
      return this.replaceValues(var1, var2);
   }

   public List removeAll(Object var1) {
      return this.removeAll(var1);
   }

   public List get(Object var1) {
      return this.get(var1);
   }

   public static final class Builder extends ImmutableMultimap.Builder {
      public ImmutableListMultimap.Builder put(Object var1, Object var2) {
         super.put(var1, var2);
         return this;
      }

      public ImmutableListMultimap.Builder put(Entry var1) {
         super.put(var1);
         return this;
      }

      public ImmutableListMultimap.Builder putAll(Object var1, Iterable var2) {
         super.putAll(var1, var2);
         return this;
      }

      public ImmutableListMultimap.Builder putAll(Object var1, Object... var2) {
         super.putAll(var1, var2);
         return this;
      }

      public ImmutableListMultimap.Builder putAll(Multimap var1) {
         super.putAll(var1);
         return this;
      }

      public ImmutableListMultimap.Builder orderKeysBy(Comparator var1) {
         super.orderKeysBy(var1);
         return this;
      }

      public ImmutableListMultimap.Builder orderValuesBy(Comparator var1) {
         super.orderValuesBy(var1);
         return this;
      }

      public ImmutableListMultimap build() {
         return (ImmutableListMultimap)super.build();
      }

      public ImmutableMultimap build() {
         return this.build();
      }

      public ImmutableMultimap.Builder orderValuesBy(Comparator var1) {
         return this.orderValuesBy(var1);
      }

      public ImmutableMultimap.Builder orderKeysBy(Comparator var1) {
         return this.orderKeysBy(var1);
      }

      public ImmutableMultimap.Builder putAll(Multimap var1) {
         return this.putAll(var1);
      }

      public ImmutableMultimap.Builder putAll(Object var1, Object[] var2) {
         return this.putAll(var1, var2);
      }

      public ImmutableMultimap.Builder putAll(Object var1, Iterable var2) {
         return this.putAll(var1, var2);
      }

      public ImmutableMultimap.Builder put(Entry var1) {
         return this.put(var1);
      }

      public ImmutableMultimap.Builder put(Object var1, Object var2) {
         return this.put(var1, var2);
      }
   }
}
