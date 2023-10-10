package org.lwjgl.opengl;

import org.lwjgl.Sys;

class Sync {
   private static final long NANOS_IN_SECOND = 1000000000L;
   private static long nextFrame = 0L;
   private static boolean initialised = false;
   private static Sync.RunningAvg sleepDurations = new Sync.RunningAvg(10);
   private static Sync.RunningAvg yieldDurations = new Sync.RunningAvg(10);

   public static void sync(int var0) {
      if (var0 > 0) {
         if (!initialised) {
            initialise();
         }

         try {
            long var1;
            long var3;
            for(var1 = getTime(); nextFrame - var1 > sleepDurations.avg(); var1 = var3) {
               Thread.sleep(1L);
               sleepDurations.add((var3 = getTime()) - var1);
            }

            sleepDurations.dampenForLowResTicker();

            for(var1 = getTime(); nextFrame - var1 > yieldDurations.avg(); var1 = var3) {
               Thread.yield();
               yieldDurations.add((var3 = getTime()) - var1);
            }
         } catch (InterruptedException var5) {
         }

         nextFrame = Math.max(nextFrame + 1000000000L / (long)var0, getTime());
      }
   }

   private static void initialise() {
      initialised = true;
      sleepDurations.init(1000000L);
      yieldDurations.init((long)((int)((double)(-(getTime() - getTime())) * 1.333D)));
      nextFrame = getTime();
      String var0 = System.getProperty("os.name");
      if (var0.startsWith("Win")) {
         Thread var1 = new Thread(new Runnable() {
            public void run() {
               try {
                  Thread.sleep(Long.MAX_VALUE);
               } catch (Exception var2) {
               }

            }
         });
         var1.setName("LWJGL Timer");
         var1.setDaemon(true);
         var1.start();
      }

   }

   private static long getTime() {
      return Sys.getTime() * 1000000000L / Sys.getTimerResolution();
   }

   private static class RunningAvg {
      private final long[] slots;
      private int offset;
      private static final long DAMPEN_THRESHOLD = 10000000L;
      private static final float DAMPEN_FACTOR = 0.9F;

      public RunningAvg(int var1) {
         this.slots = new long[var1];
         this.offset = 0;
      }

      public void init(long var1) {
         while(this.offset < this.slots.length) {
            this.slots[this.offset++] = var1;
         }

      }

      public void add(long var1) {
         this.slots[this.offset++ % this.slots.length] = var1;
         this.offset %= this.slots.length;
      }

      public long avg() {
         long var1 = 0L;

         for(int var3 = 0; var3 < this.slots.length; ++var3) {
            var1 += this.slots[var3];
         }

         return var1 / (long)this.slots.length;
      }

      public void dampenForLowResTicker() {
         if (this.avg() > 10000000L) {
            for(int var1 = 0; var1 < this.slots.length; ++var1) {
               long[] var10000 = this.slots;
               var10000[var1] = (long)((float)var10000[var1] * 0.9F);
            }
         }

      }
   }
}
