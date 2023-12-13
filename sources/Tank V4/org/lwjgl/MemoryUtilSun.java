package org.lwjgl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.Buffer;
import sun.misc.Unsafe;
import sun.reflect.FieldAccessor;

final class MemoryUtilSun {
   private MemoryUtilSun() {
   }

   private static class AccessorReflectFast implements MemoryUtil.Accessor {
      private final FieldAccessor addressAccessor;

      AccessorReflectFast() {
         Field var1;
         try {
            var1 = MemoryUtil.getAddressField();
         } catch (NoSuchFieldException var4) {
            throw new UnsupportedOperationException(var4);
         }

         var1.setAccessible(true);

         try {
            Method var2 = Field.class.getDeclaredMethod("acquireFieldAccessor", Boolean.TYPE);
            var2.setAccessible(true);
            this.addressAccessor = (FieldAccessor)var2.invoke(var1, true);
         } catch (Exception var3) {
            throw new UnsupportedOperationException(var3);
         }
      }

      public long getAddress(Buffer var1) {
         return this.addressAccessor.getLong(var1);
      }
   }

   private static class AccessorUnsafe implements MemoryUtil.Accessor {
      private final Unsafe unsafe;
      private final long address;

      AccessorUnsafe() {
         try {
            this.unsafe = getUnsafeInstance();
            this.address = this.unsafe.objectFieldOffset(MemoryUtil.getAddressField());
         } catch (Exception var2) {
            throw new UnsupportedOperationException(var2);
         }
      }

      public long getAddress(Buffer var1) {
         return this.unsafe.getLong(var1, this.address);
      }

      private static Unsafe getUnsafeInstance() {
         Field[] var0 = Unsafe.class.getDeclaredFields();
         Field[] var1 = var0;
         int var2 = var0.length;
         int var3 = 0;

         while(true) {
            label31: {
               if (var3 < var2) {
                  Field var4 = var1[var3];
                  if (!var4.getType().equals(Unsafe.class)) {
                     break label31;
                  }

                  int var5 = var4.getModifiers();
                  if (!Modifier.isStatic(var5) || !Modifier.isFinal(var5)) {
                     break label31;
                  }

                  var4.setAccessible(true);

                  try {
                     return (Unsafe)var4.get((Object)null);
                  } catch (IllegalAccessException var7) {
                  }
               }

               throw new UnsupportedOperationException();
            }

            ++var3;
         }
      }
   }
}
