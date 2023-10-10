package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import sun.misc.Cleaner;

final class Cleaner0 {
   private static final long CLEANER_FIELD_OFFSET;
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Cleaner0.class);

   static void freeDirectBuffer(ByteBuffer var0) {
      if (var0.isDirect()) {
         try {
            Cleaner var1 = (Cleaner)PlatformDependent0.getObject(var0, CLEANER_FIELD_OFFSET);
            if (var1 != null) {
               var1.clean();
            }
         } catch (Throwable var2) {
         }

      }
   }

   private Cleaner0() {
   }

   static {
      ByteBuffer var0 = ByteBuffer.allocateDirect(1);
      long var2 = -1L;
      if (PlatformDependent0.hasUnsafe()) {
         try {
            Field var1 = var0.getClass().getDeclaredField("cleaner");
            var1.setAccessible(true);
            Cleaner var4 = (Cleaner)var1.get(var0);
            var4.clean();
            var2 = PlatformDependent0.objectFieldOffset(var1);
         } catch (Throwable var5) {
            var2 = -1L;
         }
      }

      logger.debug("java.nio.ByteBuffer.cleaner(): {}", (Object)(var2 != -1L ? "available" : "unavailable"));
      CLEANER_FIELD_OFFSET = var2;
      freeDirectBuffer(var0);
   }
}
