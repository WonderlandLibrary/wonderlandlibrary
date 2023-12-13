package com.thealtening.auth.service;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Optional;

public final class FieldAdapter {
   private final HashMap fields = new HashMap();
   private static final Lookup LOOKUP;
   private static Field MODIFIERS;

   static {
      try {
         MODIFIERS = Field.class.getDeclaredField("modifiers");
         MODIFIERS.setAccessible(true);
      } catch (NoSuchFieldException var3) {
         var3.printStackTrace();
      }

      Lookup var0;
      try {
         Field var1 = Lookup.class.getDeclaredField("IMPL_LOOKUP");
         var1.setAccessible(true);
         var0 = (Lookup)var1.get((Object)null);
      } catch (ReflectiveOperationException var2) {
         var0 = MethodHandles.lookup();
      }

      LOOKUP = var0;
   }

   public FieldAdapter(String var1) {
      try {
         Class var2 = Class.forName(var1);
         Field var3 = MODIFIERS;
         Field[] var7;
         int var6 = (var7 = var2.getDeclaredFields()).length;

         for(int var5 = 0; var5 < var6; ++var5) {
            Field var4 = var7[var5];
            var4.setAccessible(true);
            int var8 = var4.getModifiers();
            if (Modifier.isFinal(var8)) {
               var3.setInt(var4, var8 & -17);
            }

            MethodHandle var9 = LOOKUP.unreflectSetter(var4);
            var9 = var9.asType(var9.type().generic().changeReturnType(Void.TYPE));
            this.fields.put(var4.getName(), var9);
         }

      } catch (ClassNotFoundException var10) {
         throw new RuntimeException("Couldn't load/find the specified class");
      } catch (Exception var11) {
         throw new RuntimeException("Couldn't create a method handler for the field");
      }
   }

   public void updateFieldIfPresent(String var1, Object var2) {
      Optional.ofNullable((MethodHandle)this.fields.get(var1)).ifPresent(FieldAdapter::lambda$0);
   }

   private static void lambda$0(Object var0, MethodHandle var1) {
      try {
         var1.invokeExact(var0);
      } catch (Throwable var3) {
         var3.printStackTrace();
      }

   }
}
