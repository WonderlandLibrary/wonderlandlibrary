package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.logging.Logger;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MediaDisposer {
   private final ReferenceQueue purgatory = new ReferenceQueue();
   private final Map disposers = new HashMap();
   private static MediaDisposer theDisposinator;

   public static void addResourceDisposer(Object var0, Object var1, ResourceDisposer var2) {
      disposinator().implAddResourceDisposer(var0, var1, var2);
   }

   public static void removeResourceDisposer(Object var0) {
      disposinator().implRemoveResourceDisposer(var0);
   }

   public static void addDisposable(Object var0, Disposable var1) {
      disposinator().implAddDisposable(var0, var1);
   }

   private static synchronized MediaDisposer disposinator() {
      if (null == theDisposinator) {
         theDisposinator = new MediaDisposer();
         Thread var0 = new Thread(() -> {
            theDisposinator.disposerLoop();
         }, "Media Resource Disposer");
         var0.setDaemon(true);
         var0.start();
      }

      return theDisposinator;
   }

   private MediaDisposer() {
   }

   private void disposerLoop() {
      while(true) {
         try {
            Reference var1 = this.purgatory.remove();
            Disposable var2;
            synchronized(this.disposers) {
               var2 = (Disposable)this.disposers.remove(var1);
            }

            var1.clear();
            if (null != var2) {
               var2.dispose();
            }

            var1 = null;
            var2 = null;
         } catch (InterruptedException var6) {
            if (Logger.canLog(1)) {
               Logger.logMsg(1, MediaDisposer.class.getName(), "disposerLoop", "Disposer loop interrupted, terminating");
            }
         }
      }
   }

   private void implAddResourceDisposer(Object var1, Object var2, ResourceDisposer var3) {
      PhantomReference var4 = new PhantomReference(var1, this.purgatory);
      synchronized(this.disposers) {
         this.disposers.put(var4, new ResourceDisposerRecord(var2, var3));
      }
   }

   private void implRemoveResourceDisposer(Object var1) {
      Reference var2 = null;
      synchronized(this.disposers) {
         Iterator var4 = this.disposers.entrySet().iterator();

         while(var4.hasNext()) {
            Map.Entry var5 = (Map.Entry)var4.next();
            Disposable var6 = (Disposable)var5.getValue();
            if (var6 instanceof ResourceDisposerRecord) {
               ResourceDisposerRecord var7 = (ResourceDisposerRecord)var6;
               if (var7.resource.equals(var1)) {
                  var2 = (Reference)var5.getKey();
                  break;
               }
            }
         }

         if (null != var2) {
            this.disposers.remove(var2);
         }

      }
   }

   private void implAddDisposable(Object var1, Disposable var2) {
      PhantomReference var3 = new PhantomReference(var1, this.purgatory);
      synchronized(this.disposers) {
         this.disposers.put(var3, var2);
      }
   }

   private static class ResourceDisposerRecord implements Disposable {
      Object resource;
      ResourceDisposer disposer;

      public ResourceDisposerRecord(Object var1, ResourceDisposer var2) {
         this.resource = var1;
         this.disposer = var2;
      }

      public void dispose() {
         this.disposer.disposeResource(this.resource);
      }
   }

   public interface ResourceDisposer {
      void disposeResource(Object var1);
   }

   public interface Disposable {
      void dispose();
   }
}
