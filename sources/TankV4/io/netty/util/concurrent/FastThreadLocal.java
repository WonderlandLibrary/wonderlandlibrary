package io.netty.util.concurrent;

import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.PlatformDependent;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class FastThreadLocal {
   private static final int variablesToRemoveIndex = InternalThreadLocalMap.nextVariableIndex();
   private final int index = InternalThreadLocalMap.nextVariableIndex();

   public static void removeAll() {
      InternalThreadLocalMap var0 = InternalThreadLocalMap.getIfSet();
      if (var0 != null) {
         Object var1 = var0.indexedVariable(variablesToRemoveIndex);
         if (var1 != null && var1 != InternalThreadLocalMap.UNSET) {
            Set var2 = (Set)var1;
            FastThreadLocal[] var3 = (FastThreadLocal[])var2.toArray(new FastThreadLocal[var2.size()]);
            FastThreadLocal[] var4 = var3;
            int var5 = var3.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               FastThreadLocal var7 = var4[var6];
               var7.remove(var0);
            }
         }

         InternalThreadLocalMap.remove();
      }
   }

   public static int size() {
      InternalThreadLocalMap var0 = InternalThreadLocalMap.getIfSet();
      return var0 == null ? 0 : var0.size();
   }

   public static void destroy() {
      InternalThreadLocalMap.destroy();
   }

   private static void addToVariablesToRemove(InternalThreadLocalMap var0, FastThreadLocal var1) {
      Object var2 = var0.indexedVariable(variablesToRemoveIndex);
      Set var3;
      if (var2 != InternalThreadLocalMap.UNSET && var2 != null) {
         var3 = (Set)var2;
      } else {
         var3 = Collections.newSetFromMap(new IdentityHashMap());
         var0.setIndexedVariable(variablesToRemoveIndex, var3);
      }

      var3.add(var1);
   }

   private static void removeFromVariablesToRemove(InternalThreadLocalMap var0, FastThreadLocal var1) {
      Object var2 = var0.indexedVariable(variablesToRemoveIndex);
      if (var2 != InternalThreadLocalMap.UNSET && var2 != null) {
         Set var3 = (Set)var2;
         var3.remove(var1);
      }
   }

   public final Object get() {
      return this.get(InternalThreadLocalMap.get());
   }

   public final Object get(InternalThreadLocalMap var1) {
      Object var2 = var1.indexedVariable(this.index);
      return var2 != InternalThreadLocalMap.UNSET ? var2 : this.initialize(var1);
   }

   private Object initialize(InternalThreadLocalMap var1) {
      Object var2 = null;

      try {
         var2 = this.initialValue();
      } catch (Exception var4) {
         PlatformDependent.throwException(var4);
      }

      var1.setIndexedVariable(this.index, var2);
      addToVariablesToRemove(var1, this);
      return var2;
   }

   public final void set(Object var1) {
      if (var1 != InternalThreadLocalMap.UNSET) {
         this.set(InternalThreadLocalMap.get(), var1);
      } else {
         this.remove();
      }

   }

   public final void set(InternalThreadLocalMap var1, Object var2) {
      if (var2 != InternalThreadLocalMap.UNSET) {
         if (var1.setIndexedVariable(this.index, var2)) {
            addToVariablesToRemove(var1, this);
         }
      } else {
         this.remove(var1);
      }

   }

   public final boolean isSet() {
      return this.isSet(InternalThreadLocalMap.getIfSet());
   }

   public final boolean isSet(InternalThreadLocalMap var1) {
      return var1 != null && var1.isIndexedVariableSet(this.index);
   }

   public final void remove() {
      this.remove(InternalThreadLocalMap.getIfSet());
   }

   public final void remove(InternalThreadLocalMap var1) {
      if (var1 != null) {
         Object var2 = var1.removeIndexedVariable(this.index);
         removeFromVariablesToRemove(var1, this);
         if (var2 != InternalThreadLocalMap.UNSET) {
            try {
               this.onRemoval(var2);
            } catch (Exception var4) {
               PlatformDependent.throwException(var4);
            }
         }

      }
   }

   protected Object initialValue() throws Exception {
      return null;
   }

   protected void onRemoval(Object var1) throws Exception {
   }
}
