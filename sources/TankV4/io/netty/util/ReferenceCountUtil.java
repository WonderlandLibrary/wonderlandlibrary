package io.netty.util;

import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class ReferenceCountUtil {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ReferenceCountUtil.class);
   private static final Map pendingReleases = new IdentityHashMap();

   public static Object retain(Object var0) {
      return var0 instanceof ReferenceCounted ? ((ReferenceCounted)var0).retain() : var0;
   }

   public static Object retain(Object var0, int var1) {
      return var0 instanceof ReferenceCounted ? ((ReferenceCounted)var0).retain(var1) : var0;
   }

   public static boolean release(Object var0) {
      return var0 instanceof ReferenceCounted ? ((ReferenceCounted)var0).release() : false;
   }

   public static boolean release(Object var0, int var1) {
      return var0 instanceof ReferenceCounted ? ((ReferenceCounted)var0).release(var1) : false;
   }

   public static Object releaseLater(Object var0) {
      return releaseLater(var0, 1);
   }

   public static Object releaseLater(Object var0, int var1) {
      if (var0 instanceof ReferenceCounted) {
         Map var2;
         synchronized(var2 = pendingReleases){}
         Thread var3 = Thread.currentThread();
         Object var4 = (List)pendingReleases.get(var3);
         if (var4 == null) {
            if (pendingReleases.isEmpty()) {
               ReferenceCountUtil.ReleasingTask var5 = new ReferenceCountUtil.ReleasingTask();
               var5.future = GlobalEventExecutor.INSTANCE.scheduleWithFixedDelay(var5, 1L, 1L, TimeUnit.SECONDS);
            }

            var4 = new ArrayList();
            pendingReleases.put(var3, var4);
         }

         ((List)var4).add(new ReferenceCountUtil.Entry((ReferenceCounted)var0, var1));
      }

      return var0;
   }

   private ReferenceCountUtil() {
   }

   static Map access$100() {
      return pendingReleases;
   }

   static InternalLogger access$200() {
      return logger;
   }

   private static final class ReleasingTask implements Runnable {
      volatile ScheduledFuture future;

      private ReleasingTask() {
      }

      public void run() {
         Map var1;
         synchronized(var1 = ReferenceCountUtil.access$100()){}
         Iterator var2 = ReferenceCountUtil.access$100().entrySet().iterator();

         while(var2.hasNext()) {
            java.util.Map.Entry var3 = (java.util.Map.Entry)var2.next();
            if (!((Thread)var3.getKey()).isAlive()) {
               releaseAll((Iterable)var3.getValue());
               var2.remove();
            }
         }

         if (ReferenceCountUtil.access$100().isEmpty()) {
            this.future.cancel(false);
         }

      }

      private static void releaseAll(Iterable var0) {
         Iterator var1 = var0.iterator();

         while(var1.hasNext()) {
            ReferenceCountUtil.Entry var2 = (ReferenceCountUtil.Entry)var1.next();

            try {
               if (!var2.obj.release(var2.decrement)) {
                  ReferenceCountUtil.access$200().warn("Non-zero refCnt: {}", (Object)var2);
               } else {
                  ReferenceCountUtil.access$200().warn("Released: {}", (Object)var2);
               }
            } catch (Exception var4) {
               ReferenceCountUtil.access$200().warn("Failed to release an object: {}", var2.obj, var4);
            }
         }

      }

      ReleasingTask(Object var1) {
         this();
      }
   }

   private static final class Entry {
      final ReferenceCounted obj;
      final int decrement;

      Entry(ReferenceCounted var1, int var2) {
         this.obj = var1;
         this.decrement = var2;
      }

      public String toString() {
         return StringUtil.simpleClassName((Object)this.obj) + ".release(" + this.decrement + ") refCnt: " + this.obj.refCnt();
      }
   }
}
