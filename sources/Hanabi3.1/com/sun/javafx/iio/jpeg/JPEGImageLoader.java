package com.sun.javafx.iio.jpeg;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.AccessController;

public class JPEGImageLoader extends ImageLoaderImpl {
   public static final int JCS_UNKNOWN = 0;
   public static final int JCS_GRAYSCALE = 1;
   public static final int JCS_RGB = 2;
   public static final int JCS_YCbCr = 3;
   public static final int JCS_CMYK = 4;
   public static final int JCS_YCC = 5;
   public static final int JCS_RGBA = 6;
   public static final int JCS_YCbCrA = 7;
   public static final int JCS_YCCA = 10;
   public static final int JCS_YCCK = 11;
   private long structPointer = 0L;
   private int inWidth;
   private int inHeight;
   private int inColorSpaceCode;
   private int outColorSpaceCode;
   private byte[] iccData;
   private int outWidth;
   private int outHeight;
   private ImageStorage.ImageType outImageType;
   private boolean isDisposed = false;
   private Lock accessLock = new Lock();

   private static native void initJPEGMethodIDs(Class var0);

   private static native void disposeNative(long var0);

   private native long initDecompressor(InputStream var1) throws IOException;

   private native int startDecompression(long var1, int var3, int var4, int var5);

   private native boolean decompressIndirect(long var1, boolean var3, byte[] var4) throws IOException;

   private void setInputAttributes(int var1, int var2, int var3, int var4, int var5, byte[] var6) {
      this.inWidth = var1;
      this.inHeight = var2;
      this.inColorSpaceCode = var3;
      this.outColorSpaceCode = var4;
      this.iccData = var6;
      switch (var4) {
         case 0:
            switch (var5) {
               case 1:
                  this.outImageType = ImageStorage.ImageType.GRAY;
                  return;
               case 2:
               default:
                  assert false;

                  return;
               case 3:
                  this.outImageType = ImageStorage.ImageType.RGB;
                  return;
               case 4:
                  this.outImageType = ImageStorage.ImageType.RGBA_PRE;
                  return;
            }
         case 1:
            this.outImageType = ImageStorage.ImageType.GRAY;
            break;
         case 2:
         case 3:
         case 5:
            this.outImageType = ImageStorage.ImageType.RGB;
            break;
         case 4:
         case 6:
         case 7:
         case 10:
         case 11:
            this.outImageType = ImageStorage.ImageType.RGBA_PRE;
            break;
         case 8:
         case 9:
         default:
            assert false;
      }

   }

   private void setOutputAttributes(int var1, int var2) {
      this.outWidth = var1;
      this.outHeight = var2;
   }

   private void updateImageProgress(int var1) {
      this.updateImageProgress(100.0F * (float)var1 / (float)this.outHeight);
   }

   JPEGImageLoader(InputStream var1) throws IOException {
      super(JPEGDescriptor.getInstance());
      if (var1 == null) {
         throw new IllegalArgumentException("input == null!");
      } else {
         try {
            this.structPointer = this.initDecompressor(var1);
         } catch (IOException var3) {
            this.dispose();
            throw var3;
         }

         if (this.structPointer == 0L) {
            throw new IOException("Unable to initialize JPEG decompressor");
         }
      }
   }

   public synchronized void dispose() {
      if (!this.accessLock.isLocked() && !this.isDisposed && this.structPointer != 0L) {
         this.isDisposed = true;
         disposeNative(this.structPointer);
         this.structPointer = 0L;
      }

   }

   protected void finalize() {
      this.dispose();
   }

   public ImageFrame load(int var1, int var2, int var3, boolean var4, boolean var5) throws IOException {
      if (var1 != 0) {
         return null;
      } else {
         this.accessLock.lock();
         int[] var6 = ImageTools.computeDimensions(this.inWidth, this.inHeight, var2, var3, var4);
         var2 = var6[0];
         var3 = var6[1];
         ImageMetadata var7 = new ImageMetadata((Float)null, true, (Integer)null, (Integer)null, (Integer)null, (Integer)null, (Integer)null, var2, var3, (Integer)null, (Integer)null, (Integer)null);
         this.updateImageMetadata(var7);
         ByteBuffer var8 = null;

         int var9;
         try {
            var9 = this.startDecompression(this.structPointer, this.outColorSpaceCode, var2, var3);
            if (this.outWidth < 0 || this.outHeight < 0 || var9 < 0) {
               throw new IOException("negative dimension.");
            }

            if (this.outWidth > Integer.MAX_VALUE / var9) {
               throw new IOException("bad width.");
            }

            int var10 = this.outWidth * var9;
            if (var10 > Integer.MAX_VALUE / this.outHeight) {
               throw new IOException("bad height.");
            }

            byte[] var11 = new byte[var10 * this.outHeight];
            var8 = ByteBuffer.wrap(var11);
            this.decompressIndirect(this.structPointer, this.listeners != null && !this.listeners.isEmpty(), var8.array());
         } catch (IOException var16) {
            throw var16;
         } catch (Throwable var17) {
            throw new IOException(var17);
         } finally {
            this.accessLock.unlock();
            this.dispose();
         }

         if (var8 == null) {
            throw new IOException("Error decompressing JPEG stream!");
         } else {
            if (this.outWidth != var2 || this.outHeight != var3) {
               var8 = ImageTools.scaleImage(var8, this.outWidth, this.outHeight, var9, var2, var3, var5);
            }

            return new ImageFrame(this.outImageType, var8, var2, var3, var2 * var9, (byte[][])null, var7);
         }
      }
   }

   static {
      AccessController.doPrivileged(() -> {
         NativeLibLoader.loadLibrary("javafx_iio");
         return null;
      });
      initJPEGMethodIDs(InputStream.class);
   }

   private static class Lock {
      private boolean locked = false;

      public Lock() {
      }

      public synchronized boolean isLocked() {
         return this.locked;
      }

      public synchronized void lock() {
         if (this.locked) {
            throw new IllegalStateException("Recursive loading is not allowed.");
         } else {
            this.locked = true;
         }
      }

      public synchronized void unlock() {
         if (!this.locked) {
            throw new IllegalStateException("Invalid loader state.");
         } else {
            this.locked = false;
         }
      }
   }
}
