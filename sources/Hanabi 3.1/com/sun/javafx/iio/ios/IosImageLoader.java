package com.sun.javafx.iio.ios;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageDescriptor;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;

public class IosImageLoader extends ImageLoaderImpl {
   public static final int GRAY = 0;
   public static final int GRAY_ALPHA = 1;
   public static final int GRAY_ALPHA_PRE = 2;
   public static final int PALETTE = 3;
   public static final int PALETTE_ALPHA = 4;
   public static final int PALETTE_ALPHA_PRE = 5;
   public static final int PALETTE_TRANS = 6;
   public static final int RGB = 7;
   public static final int RGBA = 8;
   public static final int RGBA_PRE = 9;
   private static final Map colorSpaceMapping = new HashMap();
   private long structPointer;
   private int inWidth;
   private int inHeight;
   private int nImages;
   private boolean isDisposed = false;
   private int delayTime;
   private int loopCount;

   private static native void initNativeLoading();

   private native long loadImage(InputStream var1, boolean var2) throws IOException;

   private native long loadImageFromURL(String var1, boolean var2) throws IOException;

   private native void resizeImage(long var1, int var3, int var4);

   private native byte[] getImageBuffer(long var1, int var3);

   private native int getNumberOfComponents(long var1);

   private native int getColorSpaceCode(long var1);

   private native int getDelayTime(long var1);

   private static native void disposeLoader(long var0);

   private void setInputParameters(int var1, int var2, int var3, int var4) {
      this.inWidth = var1;
      this.inHeight = var2;
      this.nImages = var3;
      this.loopCount = var4;
   }

   private void updateProgress(float var1) {
      this.updateImageProgress(var1);
   }

   private boolean shouldReportProgress() {
      return this.listeners != null && !this.listeners.isEmpty();
   }

   private void checkNativePointer() throws IOException {
      if (this.structPointer == 0L) {
         throw new IOException("Unable to initialize image native loader!");
      }
   }

   private void retrieveDelayTime() {
      if (this.nImages > 1) {
         this.delayTime = this.getDelayTime(this.structPointer);
      }

   }

   public IosImageLoader(String var1, ImageDescriptor var2) throws IOException {
      super(var2);

      try {
         new URL(var1);
      } catch (MalformedURLException var5) {
         throw new IllegalArgumentException("Image loader: Malformed URL!");
      }

      try {
         this.structPointer = this.loadImageFromURL(var1, this.shouldReportProgress());
      } catch (IOException var4) {
         this.dispose();
         throw var4;
      }

      this.checkNativePointer();
      this.retrieveDelayTime();
   }

   public IosImageLoader(InputStream var1, ImageDescriptor var2) throws IOException {
      super(var2);
      if (var1 == null) {
         throw new IllegalArgumentException("Image loader: input stream == null");
      } else {
         try {
            this.structPointer = this.loadImage(var1, this.shouldReportProgress());
         } catch (IOException var4) {
            this.dispose();
            throw var4;
         }

         this.checkNativePointer();
         this.retrieveDelayTime();
      }
   }

   public synchronized void dispose() {
      if (!this.isDisposed && this.structPointer != 0L) {
         this.isDisposed = true;
         disposeLoader(this.structPointer);
         this.structPointer = 0L;
      }

   }

   protected void finalize() {
      this.dispose();
   }

   public ImageFrame load(int var1, int var2, int var3, boolean var4, boolean var5) throws IOException {
      if (var1 >= this.nImages) {
         this.dispose();
         return null;
      } else {
         int[] var6 = ImageTools.computeDimensions(this.inWidth, this.inHeight, var2, var3, var4);
         var2 = var6[0];
         var3 = var6[1];
         ImageMetadata var7 = new ImageMetadata((Float)null, true, (Integer)null, (Integer)null, (Integer)null, this.delayTime == 0 ? null : this.delayTime, this.nImages > 1 ? this.loopCount : null, var2, var3, (Integer)null, (Integer)null, (Integer)null);
         this.updateImageMetadata(var7);
         this.resizeImage(this.structPointer, var2, var3);
         int var8 = this.getNumberOfComponents(this.structPointer);
         int var9 = this.getColorSpaceCode(this.structPointer);
         ImageStorage.ImageType var10 = (ImageStorage.ImageType)colorSpaceMapping.get(var9);
         byte[] var11 = this.getImageBuffer(this.structPointer, var1);
         return new ImageFrame(var10, ByteBuffer.wrap(var11), var2, var3, var2 * var8, (byte[][])null, var7);
      }
   }

   static {
      AccessController.doPrivileged(() -> {
         NativeLibLoader.loadLibrary("nativeiio");
         return null;
      });
      colorSpaceMapping.put(0, ImageStorage.ImageType.GRAY);
      colorSpaceMapping.put(1, ImageStorage.ImageType.GRAY_ALPHA);
      colorSpaceMapping.put(2, ImageStorage.ImageType.GRAY_ALPHA_PRE);
      colorSpaceMapping.put(3, ImageStorage.ImageType.PALETTE);
      colorSpaceMapping.put(4, ImageStorage.ImageType.PALETTE_ALPHA);
      colorSpaceMapping.put(5, ImageStorage.ImageType.PALETTE_ALPHA_PRE);
      colorSpaceMapping.put(6, ImageStorage.ImageType.PALETTE_TRANS);
      colorSpaceMapping.put(7, ImageStorage.ImageType.RGB);
      colorSpaceMapping.put(8, ImageStorage.ImageType.RGBA);
      colorSpaceMapping.put(9, ImageStorage.ImageType.RGBA_PRE);
      initNativeLoading();
   }
}
