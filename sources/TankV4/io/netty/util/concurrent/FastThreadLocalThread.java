package io.netty.util.concurrent;

import io.netty.util.internal.InternalThreadLocalMap;

public class FastThreadLocalThread extends Thread {
   private InternalThreadLocalMap threadLocalMap;

   public FastThreadLocalThread() {
   }

   public FastThreadLocalThread(Runnable var1) {
      super(var1);
   }

   public FastThreadLocalThread(ThreadGroup var1, Runnable var2) {
      super(var1, var2);
   }

   public FastThreadLocalThread(String var1) {
      super(var1);
   }

   public FastThreadLocalThread(ThreadGroup var1, String var2) {
      super(var1, var2);
   }

   public FastThreadLocalThread(Runnable var1, String var2) {
      super(var1, var2);
   }

   public FastThreadLocalThread(ThreadGroup var1, Runnable var2, String var3) {
      super(var1, var2, var3);
   }

   public FastThreadLocalThread(ThreadGroup var1, Runnable var2, String var3, long var4) {
      super(var1, var2, var3, var4);
   }

   public final InternalThreadLocalMap threadLocalMap() {
      return this.threadLocalMap;
   }

   public final void setThreadLocalMap(InternalThreadLocalMap var1) {
      this.threadLocalMap = var1;
   }
}
