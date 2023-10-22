package com.sun.javafx.tk.quantum;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageLoadListener;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.ImageStorageException;
import com.sun.javafx.runtime.async.AbstractRemoteResource;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PlatformImage;
import com.sun.prism.Image;
import com.sun.prism.impl.PrismSettings;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import sun.util.logging.PlatformLogger;

class PrismImageLoader2 implements ImageLoader {
   private static PlatformLogger imageioLogger = null;
   private Image[] images;
   private int[] delayTimes;
   private int loopCount;
   private int width;
   private int height;
   private float pixelScale;
   private Exception exception;

   public PrismImageLoader2(String var1, int var2, int var3, boolean var4, float var5, boolean var6) {
      this.loadAll(var1, var2, var3, var4, var5, var6);
   }

   public PrismImageLoader2(InputStream var1, int var2, int var3, boolean var4, boolean var5) {
      this.loadAll(var1, var2, var3, var4, var5);
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getFrameCount() {
      return this.images == null ? 0 : this.images.length;
   }

   public PlatformImage getFrame(int var1) {
      return this.images == null ? null : this.images[var1];
   }

   public int getFrameDelay(int var1) {
      return this.images == null ? 0 : this.delayTimes[var1];
   }

   public int getLoopCount() {
      return this.images == null ? 0 : this.loopCount;
   }

   public Exception getException() {
      return this.exception;
   }

   private void loadAll(String var1, int var2, int var3, boolean var4, float var5, boolean var6) {
      PrismLoadListener var7 = new PrismLoadListener();

      try {
         ImageFrame[] var8 = ImageStorage.loadAll((String)var1, var7, var2, var3, var4, var5, var6);
         this.convertAll(var8);
      } catch (ImageStorageException var9) {
         this.handleException(var9);
      } catch (Exception var10) {
         this.handleException(var10);
      }

   }

   private void loadAll(InputStream var1, int var2, int var3, boolean var4, boolean var5) {
      PrismLoadListener var6 = new PrismLoadListener();

      try {
         ImageFrame[] var7 = ImageStorage.loadAll((InputStream)var1, var6, var2, var3, var4, 1.0F, var5);
         this.convertAll(var7);
      } catch (ImageStorageException var8) {
         this.handleException(var8);
      } catch (Exception var9) {
         this.handleException(var9);
      }

   }

   private void handleException(ImageStorageException var1) {
      Throwable var2 = var1.getCause();
      if (var2 instanceof Exception) {
         this.handleException((Exception)var2);
      } else {
         this.handleException((Exception)var1);
      }

   }

   private void handleException(Exception var1) {
      if (PrismSettings.verbose) {
         var1.printStackTrace(System.err);
      }

      this.exception = var1;
   }

   private void convertAll(ImageFrame[] var1) {
      int var2 = var1.length;
      this.images = new Image[var2];
      this.delayTimes = new int[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         ImageFrame var4 = var1[var3];
         this.images[var3] = Image.convertImageFrame(var4);
         ImageMetadata var5 = var4.getMetadata();
         if (var5 != null) {
            Integer var6 = var5.delayTime;
            if (var6 != null) {
               this.delayTimes[var3] = var6;
            }

            Integer var7 = var5.loopCount;
            if (var7 != null) {
               this.loopCount = var7;
            }
         }

         if (var3 == 0) {
            this.width = var4.getWidth();
            this.height = var4.getHeight();
         }
      }

   }

   private static synchronized PlatformLogger getImageioLogger() {
      if (imageioLogger == null) {
         imageioLogger = PlatformLogger.getLogger("javafx.scene.image");
      }

      return imageioLogger;
   }

   static final class AsyncImageLoader extends AbstractRemoteResource {
      private static final ExecutorService BG_LOADING_EXECUTOR = createExecutor();
      private final AccessControlContext acc;
      int width;
      int height;
      boolean preserveRatio;
      boolean smooth;

      public AsyncImageLoader(AsyncOperationListener var1, String var2, int var3, int var4, boolean var5, boolean var6) {
         super(var2, var1);
         this.width = var3;
         this.height = var4;
         this.preserveRatio = var5;
         this.smooth = var6;
         this.acc = AccessController.getContext();
      }

      protected PrismImageLoader2 processStream(InputStream var1) throws IOException {
         return new PrismImageLoader2(var1, this.width, this.height, this.preserveRatio, this.smooth);
      }

      public PrismImageLoader2 call() throws IOException {
         try {
            return (PrismImageLoader2)AccessController.doPrivileged(() -> {
               return (PrismImageLoader2)access$201(this);
            }, this.acc);
         } catch (PrivilegedActionException var3) {
            Throwable var2 = var3.getCause();
            if (var2 instanceof IOException) {
               throw (IOException)var2;
            } else {
               throw new UndeclaredThrowableException(var2);
            }
         }
      }

      public void start() {
         BG_LOADING_EXECUTOR.execute(this.future);
      }

      private static ExecutorService createExecutor() {
         ThreadGroup var0 = (ThreadGroup)AccessController.doPrivileged(() -> {
            return new ThreadGroup(QuantumToolkit.getFxUserThread().getThreadGroup(), "Background image loading thread pool");
         });
         ThreadFactory var1 = (var1x) -> {
            return (Thread)AccessController.doPrivileged(() -> {
               Thread var2 = new Thread(var0, var1x);
               var2.setPriority(1);
               return var2;
            });
         };
         ExecutorService var2 = Executors.newCachedThreadPool(var1);
         ((ThreadPoolExecutor)var2).setKeepAliveTime(1L, TimeUnit.SECONDS);
         return var2;
      }

      // $FF: synthetic method
      static Object access$201(AsyncImageLoader var0) throws IOException {
         return var0.call();
      }
   }

   private class PrismLoadListener implements ImageLoadListener {
      private PrismLoadListener() {
      }

      public void imageLoadWarning(com.sun.javafx.iio.ImageLoader var1, String var2) {
         PrismImageLoader2.getImageioLogger().warning(var2);
      }

      public void imageLoadProgress(com.sun.javafx.iio.ImageLoader var1, float var2) {
      }

      public void imageLoadMetaData(com.sun.javafx.iio.ImageLoader var1, ImageMetadata var2) {
      }

      // $FF: synthetic method
      PrismLoadListener(Object var2) {
         this();
      }
   }
}
