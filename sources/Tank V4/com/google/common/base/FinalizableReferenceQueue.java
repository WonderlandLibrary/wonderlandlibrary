package com.google.common.base;

import com.google.common.annotations.VisibleForTesting;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FinalizableReferenceQueue implements Closeable {
   private static final Logger logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
   private static final String FINALIZER_CLASS_NAME = "com.google.common.base.internal.Finalizer";
   private static final Method startFinalizer;
   final ReferenceQueue queue = new ReferenceQueue();
   final PhantomReference frqRef;
   final boolean threadStarted;

   public FinalizableReferenceQueue() {
      this.frqRef = new PhantomReference(this, this.queue);
      boolean var1 = false;

      try {
         startFinalizer.invoke((Object)null, FinalizableReference.class, this.queue, this.frqRef);
         var1 = true;
      } catch (IllegalAccessException var3) {
         throw new AssertionError(var3);
      } catch (Throwable var4) {
         logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", var4);
      }

      this.threadStarted = var1;
   }

   public void close() {
      this.frqRef.enqueue();
      this.cleanUp();
   }

   void cleanUp() {
      if (!this.threadStarted) {
         Reference var1;
         while((var1 = this.queue.poll()) != null) {
            var1.clear();

            try {
               ((FinalizableReference)var1).finalizeReferent();
            } catch (Throwable var3) {
               logger.log(Level.SEVERE, "Error cleaning up after reference.", var3);
            }
         }

      }
   }

   private static Class loadFinalizer(FinalizableReferenceQueue.FinalizerLoader... var0) {
      FinalizableReferenceQueue.FinalizerLoader[] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         FinalizableReferenceQueue.FinalizerLoader var4 = var1[var3];
         Class var5 = var4.loadFinalizer();
         if (var5 != null) {
            return var5;
         }
      }

      throw new AssertionError();
   }

   static Method getStartFinalizer(Class var0) {
      try {
         return var0.getMethod("startFinalizer", Class.class, ReferenceQueue.class, PhantomReference.class);
      } catch (NoSuchMethodException var2) {
         throw new AssertionError(var2);
      }
   }

   static Logger access$000() {
      return logger;
   }

   static {
      Class var0 = loadFinalizer(new FinalizableReferenceQueue.SystemLoader(), new FinalizableReferenceQueue.DecoupledLoader(), new FinalizableReferenceQueue.DirectLoader());
      startFinalizer = getStartFinalizer(var0);
   }

   static class DirectLoader implements FinalizableReferenceQueue.FinalizerLoader {
      public Class loadFinalizer() {
         try {
            return Class.forName("com.google.common.base.internal.Finalizer");
         } catch (ClassNotFoundException var2) {
            throw new AssertionError(var2);
         }
      }
   }

   static class DecoupledLoader implements FinalizableReferenceQueue.FinalizerLoader {
      private static final String LOADING_ERROR = "Could not load Finalizer in its own class loader.Loading Finalizer in the current class loader instead. As a result, you will not be ableto garbage collect this class loader. To support reclaiming this class loader, eitherresolve the underlying issue, or move Google Collections to your system class path.";

      public Class loadFinalizer() {
         try {
            URLClassLoader var1 = this.newLoader(this.getBaseUrl());
            return var1.loadClass("com.google.common.base.internal.Finalizer");
         } catch (Exception var2) {
            FinalizableReferenceQueue.access$000().log(Level.WARNING, "Could not load Finalizer in its own class loader.Loading Finalizer in the current class loader instead. As a result, you will not be ableto garbage collect this class loader. To support reclaiming this class loader, eitherresolve the underlying issue, or move Google Collections to your system class path.", var2);
            return null;
         }
      }

      URL getBaseUrl() throws IOException {
         String var1 = "com.google.common.base.internal.Finalizer".replace('.', '/') + ".class";
         URL var2 = this.getClass().getClassLoader().getResource(var1);
         if (var2 == null) {
            throw new FileNotFoundException(var1);
         } else {
            String var3 = var2.toString();
            if (!var3.endsWith(var1)) {
               throw new IOException("Unsupported path style: " + var3);
            } else {
               var3 = var3.substring(0, var3.length() - var1.length());
               return new URL(var2, var3);
            }
         }
      }

      URLClassLoader newLoader(URL var1) {
         return new URLClassLoader(new URL[]{var1}, (ClassLoader)null);
      }
   }

   static class SystemLoader implements FinalizableReferenceQueue.FinalizerLoader {
      @VisibleForTesting
      static boolean disabled;

      public Class loadFinalizer() {
         if (disabled) {
            return null;
         } else {
            ClassLoader var1;
            try {
               var1 = ClassLoader.getSystemClassLoader();
            } catch (SecurityException var4) {
               FinalizableReferenceQueue.access$000().info("Not allowed to access system class loader.");
               return null;
            }

            if (var1 != null) {
               try {
                  return var1.loadClass("com.google.common.base.internal.Finalizer");
               } catch (ClassNotFoundException var3) {
                  return null;
               }
            } else {
               return null;
            }
         }
      }
   }

   interface FinalizerLoader {
      Class loadFinalizer();
   }
}
