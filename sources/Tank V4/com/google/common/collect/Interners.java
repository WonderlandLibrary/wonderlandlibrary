package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.concurrent.ConcurrentMap;

@Beta
public final class Interners {
   private Interners() {
   }

   public static Interner newStrongInterner() {
      ConcurrentMap var0 = (new MapMaker()).makeMap();
      return new Interner(var0) {
         final ConcurrentMap val$map;

         {
            this.val$map = var1;
         }

         public Object intern(Object var1) {
            Object var2 = this.val$map.putIfAbsent(Preconditions.checkNotNull(var1), var1);
            return var2 == null ? var1 : var2;
         }
      };
   }

   @GwtIncompatible("java.lang.ref.WeakReference")
   public static Interner newWeakInterner() {
      return new Interners.WeakInterner();
   }

   public static Function asFunction(Interner var0) {
      return new Interners.InternerFunction((Interner)Preconditions.checkNotNull(var0));
   }

   private static class InternerFunction implements Function {
      private final Interner interner;

      public InternerFunction(Interner var1) {
         this.interner = var1;
      }

      public Object apply(Object var1) {
         return this.interner.intern(var1);
      }

      public int hashCode() {
         return this.interner.hashCode();
      }

      public boolean equals(Object var1) {
         if (var1 instanceof Interners.InternerFunction) {
            Interners.InternerFunction var2 = (Interners.InternerFunction)var1;
            return this.interner.equals(var2.interner);
         } else {
            return false;
         }
      }
   }

   private static class WeakInterner implements Interner {
      private final MapMakerInternalMap map;

      private WeakInterner() {
         this.map = (new MapMaker()).weakKeys().keyEquivalence(Equivalence.equals()).makeCustomMap();
      }

      public Object intern(Object var1) {
         Interners.WeakInterner.Dummy var4;
         do {
            MapMakerInternalMap.ReferenceEntry var2 = this.map.getEntry(var1);
            if (var2 != null) {
               Object var3 = var2.getKey();
               if (var3 != null) {
                  return var3;
               }
            }

            var4 = (Interners.WeakInterner.Dummy)this.map.putIfAbsent(var1, Interners.WeakInterner.Dummy.VALUE);
         } while(var4 != null);

         return var1;
      }

      WeakInterner(Object var1) {
         this();
      }

      private static enum Dummy {
         VALUE;

         private static final Interners.WeakInterner.Dummy[] $VALUES = new Interners.WeakInterner.Dummy[]{VALUE};
      }
   }
}
