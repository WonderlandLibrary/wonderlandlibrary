package org.lwjgl.util.mapped;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

final class CacheLineSize {
   private CacheLineSize() {
   }

   static int getCacheLineSize() {
      boolean var0 = true;
      int var1 = 200000;
      int var2 = 100000;
      int var3 = LWJGLUtil.getPrivilegedInteger("org.lwjgl.util.mapped.CacheLineMaxSize", 1024) / 4;
      double var4 = 1.0D + (double)LWJGLUtil.getPrivilegedInteger("org.lwjgl.util.mapped.CacheLineTimeThreshold", 50) / 100.0D;
      ExecutorService var6 = Executors.newFixedThreadPool(2);
      ExecutorCompletionService var7 = new ExecutorCompletionService(var6);
      IntBuffer var8 = getMemory(var3);
      boolean var9 = true;

      for(int var10 = 0; var10 < 10; ++var10) {
         doTest(2, 100000, 0, var8, var7);
      }

      long var21 = 0L;
      int var12 = 0;
      int var13 = 64;
      boolean var14 = false;

      for(int var15 = var3; var15 >= 1; var15 >>= 1) {
         long var16 = doTest(2, 100000, var15, var8, var7);
         if (var21 > 0L) {
            long var18 = var21 / (long)var12;
            if ((double)var16 / (double)var18 > var4) {
               var13 = (var15 << 1) * 4;
               var14 = true;
               break;
            }
         }

         var21 += var16;
         ++var12;
      }

      if (LWJGLUtil.DEBUG) {
         if (var14) {
            LWJGLUtil.log("Cache line size detected: " + var13 + " bytes");
         } else {
            LWJGLUtil.log("Failed to detect cache line size, assuming " + var13 + " bytes");
         }
      }

      var6.shutdown();
      return var13;
   }

   public static void main(String[] var0) {
      CacheUtil.getCacheLineSize();
   }

   static long memoryLoop(int var0, int var1, IntBuffer var2, int var3) {
      long var4 = MemoryUtil.getAddress(var2) + (long)(var0 * var3 * 4);
      long var6 = System.nanoTime();

      for(int var8 = 0; var8 < var1; ++var8) {
         MappedHelper.ivput(MappedHelper.ivget(var4) + 1, var4);
      }

      return System.nanoTime() - var6;
   }

   private static IntBuffer getMemory(int var0) {
      int var1 = MappedObjectUnsafe.INSTANCE.pageSize();
      ByteBuffer var2 = ByteBuffer.allocateDirect(var0 * 4 + var1).order(ByteOrder.nativeOrder());
      if (MemoryUtil.getAddress(var2) % (long)var1 != 0L) {
         var2.position(var1 - (int)(MemoryUtil.getAddress(var2) & (long)(var1 - 1)));
      }

      return var2.asIntBuffer();
   }

   private static long doTest(int var0, int var1, int var2, IntBuffer var3, ExecutorCompletionService var4) {
      for(int var5 = 0; var5 < var0; ++var5) {
         submitTest(var4, var5, var1, var3, var2);
      }

      return waitForResults(var0, var4);
   }

   private static void submitTest(ExecutorCompletionService var0, int var1, int var2, IntBuffer var3, int var4) {
      var0.submit(new Callable(var1, var2, var3, var4) {
         final int val$index;
         final int val$repeats;
         final IntBuffer val$memory;
         final int val$padding;

         {
            this.val$index = var1;
            this.val$repeats = var2;
            this.val$memory = var3;
            this.val$padding = var4;
         }

         public Long call() throws Exception {
            return CacheLineSize.memoryLoop(this.val$index, this.val$repeats, this.val$memory, this.val$padding);
         }

         public Object call() throws Exception {
            return this.call();
         }
      });
   }

   private static long waitForResults(int var0, ExecutorCompletionService var1) {
      try {
         long var2 = 0L;

         for(int var4 = 0; var4 < var0; ++var4) {
            var2 += (Long)var1.take().get();
         }

         return var2;
      } catch (Exception var5) {
         throw new RuntimeException(var5);
      }
   }
}
