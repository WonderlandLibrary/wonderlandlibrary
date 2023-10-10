package org.lwjgl.util.mapped;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import sun.misc.Unsafe;

final class MappedObjectUnsafe {
   static final Unsafe INSTANCE = getUnsafeInstance();
   private static final long BUFFER_ADDRESS_OFFSET = getObjectFieldOffset(ByteBuffer.class, "address");
   private static final long BUFFER_CAPACITY_OFFSET = getObjectFieldOffset(ByteBuffer.class, "capacity");
   private static final ByteBuffer global = ByteBuffer.allocateDirect(4096);

   static ByteBuffer newBuffer(long var0, int var2) {
      if (var0 > 0L && var2 >= 0) {
         ByteBuffer var3 = global.duplicate().order(ByteOrder.nativeOrder());
         INSTANCE.putLong(var3, BUFFER_ADDRESS_OFFSET, var0);
         INSTANCE.putInt(var3, BUFFER_CAPACITY_OFFSET, var2);
         var3.position(0);
         var3.limit(var2);
         return var3;
      } else {
         throw new IllegalStateException("you almost crashed the jvm");
      }
   }

   private static long getObjectFieldOffset(Class var0, String var1) {
      while(var0 != null) {
         try {
            return INSTANCE.objectFieldOffset(var0.getDeclaredField(var1));
         } catch (Throwable var3) {
            var0 = var0.getSuperclass();
         }
      }

      throw new UnsupportedOperationException();
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
