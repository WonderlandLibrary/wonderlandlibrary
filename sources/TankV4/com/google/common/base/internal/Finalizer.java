package com.google.common.base.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Finalizer implements Runnable {
   private static final Logger logger = Logger.getLogger(Finalizer.class.getName());
   private static final String FINALIZABLE_REFERENCE = "com.google.common.base.FinalizableReference";
   private final WeakReference finalizableReferenceClassReference;
   private final PhantomReference frqReference;
   private final ReferenceQueue queue;
   private static final Field inheritableThreadLocals = getInheritableThreadLocalsField();

   public static void startFinalizer(Class var0, ReferenceQueue var1, PhantomReference var2) {
      if (!var0.getName().equals("com.google.common.base.FinalizableReference")) {
         throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
      } else {
         Finalizer var3 = new Finalizer(var0, var1, var2);
         Thread var4 = new Thread(var3);
         var4.setName(Finalizer.class.getName());
         var4.setDaemon(true);

         try {
            if (inheritableThreadLocals != null) {
               inheritableThreadLocals.set(var4, (Object)null);
            }
         } catch (Throwable var6) {
            logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", var6);
         }

         var4.start();
      }
   }

   private Finalizer(Class var1, ReferenceQueue var2, PhantomReference var3) {
      this.queue = var2;
      this.finalizableReferenceClassReference = new WeakReference(var1);
      this.frqReference = var3;
   }

   public void run() {
      while(true) {
         try {
            if (this.queue.remove() == null) {
               return;
            }
         } catch (InterruptedException var2) {
         }
      }
   }

   private Method getFinalizeReferentMethod() {
      Class var1 = (Class)this.finalizableReferenceClassReference.get();
      if (var1 == null) {
         return null;
      } else {
         try {
            return var1.getMethod("finalizeReferent");
         } catch (NoSuchMethodException var3) {
            throw new AssertionError(var3);
         }
      }
   }

   public static Field getInheritableThreadLocalsField() {
      try {
         Field var0 = Thread.class.getDeclaredField("inheritableThreadLocals");
         var0.setAccessible(true);
         return var0;
      } catch (Throwable var1) {
         logger.log(Level.INFO, "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");
         return null;
      }
   }
}
