package com.google.common.cache;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Random;
import sun.misc.Unsafe;

abstract class Striped64 extends Number {
   static final Striped64.ThreadHashCode threadHashCode = new Striped64.ThreadHashCode();
   static final int NCPU = Runtime.getRuntime().availableProcessors();
   transient volatile Striped64.Cell[] cells;
   transient volatile long base;
   transient volatile int busy;
   private static final Unsafe UNSAFE;
   private static final long baseOffset;
   private static final long busyOffset;

   final boolean casBase(long var1, long var3) {
      return UNSAFE.compareAndSwapLong(this, baseOffset, var1, var3);
   }

   final boolean casBusy() {
      return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
   }

   abstract long fn(long var1, long var3);

   final void retryUpdate(long var1, Striped64.HashCode var3, boolean var4) {
      int var5 = var3.code;
      boolean var6 = false;

      while(true) {
         Striped64.Cell[] var7;
         int var9;
         long var10;
         if ((var7 = this.cells) != null && (var9 = var7.length) > 0) {
            Striped64.Cell var8;
            if ((var8 = var7[var9 - 1 & var5]) == null) {
               if (this.busy == 0) {
                  Striped64.Cell var20 = new Striped64.Cell(var1);
                  if (this.busy == 0 && this.casBusy()) {
                     boolean var21 = false;
                     Striped64.Cell[] var14;
                     int var15;
                     int var16;
                     if ((var14 = this.cells) != null && (var15 = var14.length) > 0 && var14[var16 = var15 - 1 & var5] == null) {
                        var14[var16] = var20;
                        var21 = true;
                     }

                     this.busy = 0;
                     if (var21) {
                        break;
                     }
                     continue;
                  }
               }

               var6 = false;
            } else if (!var4) {
               var4 = true;
            } else {
               if (var8.cas(var10 = var8.value, this.fn(var10, var1))) {
                  break;
               }

               if (var9 < NCPU && this.cells == var7) {
                  if (!var6) {
                     var6 = true;
                  } else if (this.busy == 0 && this.casBusy()) {
                     if (this.cells == var7) {
                        Striped64.Cell[] var22 = new Striped64.Cell[var9 << 1];

                        for(int var23 = 0; var23 < var9; ++var23) {
                           var22[var23] = var7[var23];
                        }

                        this.cells = var22;
                     }

                     this.busy = 0;
                     var6 = false;
                     continue;
                  }
               } else {
                  var6 = false;
               }
            }

            var5 ^= var5 << 13;
            var5 ^= var5 >>> 17;
            var5 ^= var5 << 5;
         } else if (this.busy == 0 && this.cells == var7 && this.casBusy()) {
            boolean var12 = false;
            if (this.cells == var7) {
               Striped64.Cell[] var13 = new Striped64.Cell[2];
               var13[var5 & 1] = new Striped64.Cell(var1);
               this.cells = var13;
               var12 = true;
            }

            this.busy = 0;
            if (var12) {
               break;
            }
         } else if (this.casBase(var10 = this.base, this.fn(var10, var1))) {
            break;
         }
      }

      var3.code = var5;
   }

   final void internalReset(long var1) {
      Striped64.Cell[] var3 = this.cells;
      this.base = var1;
      if (var3 != null) {
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Striped64.Cell var6 = var3[var5];
            if (var6 != null) {
               var6.value = var1;
            }
         }
      }

   }

   private static Unsafe getUnsafe() {
      try {
         return Unsafe.getUnsafe();
      } catch (SecurityException var2) {
         try {
            return (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction() {
               public Unsafe run() throws Exception {
                  Class var1 = Unsafe.class;
                  Field[] var2 = var1.getDeclaredFields();
                  int var3 = var2.length;

                  for(int var4 = 0; var4 < var3; ++var4) {
                     Field var5 = var2[var4];
                     var5.setAccessible(true);
                     Object var6 = var5.get((Object)null);
                     if (var1.isInstance(var6)) {
                        return (Unsafe)var1.cast(var6);
                     }
                  }

                  throw new NoSuchFieldError("the Unsafe");
               }

               public Object run() throws Exception {
                  return this.run();
               }
            });
         } catch (PrivilegedActionException var1) {
            throw new RuntimeException("Could not initialize intrinsics", var1.getCause());
         }
      }
   }

   static Unsafe access$000() {
      return getUnsafe();
   }

   static {
      try {
         UNSAFE = getUnsafe();
         Class var0 = Striped64.class;
         baseOffset = UNSAFE.objectFieldOffset(var0.getDeclaredField("base"));
         busyOffset = UNSAFE.objectFieldOffset(var0.getDeclaredField("busy"));
      } catch (Exception var1) {
         throw new Error(var1);
      }
   }

   static final class ThreadHashCode extends ThreadLocal {
      public Striped64.HashCode initialValue() {
         return new Striped64.HashCode();
      }

      public Object initialValue() {
         return this.initialValue();
      }
   }

   static final class HashCode {
      static final Random rng = new Random();
      int code;

      HashCode() {
         int var1 = rng.nextInt();
         this.code = var1 == 0 ? 1 : var1;
      }
   }

   static final class Cell {
      volatile long p0;
      volatile long p1;
      volatile long p2;
      volatile long p3;
      volatile long p4;
      volatile long p5;
      volatile long p6;
      volatile long value;
      volatile long q0;
      volatile long q1;
      volatile long q2;
      volatile long q3;
      volatile long q4;
      volatile long q5;
      volatile long q6;
      private static final Unsafe UNSAFE;
      private static final long valueOffset;

      Cell(long var1) {
         this.value = var1;
      }

      final boolean cas(long var1, long var3) {
         return UNSAFE.compareAndSwapLong(this, valueOffset, var1, var3);
      }

      static {
         try {
            UNSAFE = Striped64.access$000();
            Class var0 = Striped64.Cell.class;
            valueOffset = UNSAFE.objectFieldOffset(var0.getDeclaredField("value"));
         } catch (Exception var1) {
            throw new Error(var1);
         }
      }
   }
}
