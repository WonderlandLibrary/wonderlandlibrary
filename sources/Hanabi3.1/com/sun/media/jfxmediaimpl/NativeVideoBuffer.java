package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.media.jfxmedia.control.VideoFormat;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicInteger;

final class NativeVideoBuffer implements VideoDataBuffer {
   private long nativePeer;
   private final AtomicInteger holdCount = new AtomicInteger(1);
   private NativeVideoBuffer cachedBGRARep;
   private static final boolean DEBUG_DISPOSED_BUFFERS = false;
   private static final VideoBufferDisposer disposer = new VideoBufferDisposer();

   private static native void nativeDisposeBuffer(long var0);

   private native double nativeGetTimestamp(long var1);

   private native ByteBuffer nativeGetBufferForPlane(long var1, int var3);

   private native int nativeGetWidth(long var1);

   private native int nativeGetHeight(long var1);

   private native int nativeGetEncodedWidth(long var1);

   private native int nativeGetEncodedHeight(long var1);

   private native int nativeGetFormat(long var1);

   private native boolean nativeHasAlpha(long var1);

   private native int nativeGetPlaneCount(long var1);

   private native int[] nativeGetPlaneStrides(long var1);

   private native long nativeConvertToFormat(long var1, int var3);

   private native void nativeSetDirty(long var1);

   public static NativeVideoBuffer createVideoBuffer(long var0) {
      NativeVideoBuffer var2 = new NativeVideoBuffer(var0);
      MediaDisposer.addResourceDisposer(var2, var0, disposer);
      return var2;
   }

   private NativeVideoBuffer(long var1) {
      this.nativePeer = var1;
   }

   public void holdFrame() {
      if (0L != this.nativePeer) {
         this.holdCount.incrementAndGet();
      }

   }

   public void releaseFrame() {
      if (0L != this.nativePeer && this.holdCount.decrementAndGet() <= 0) {
         if (null != this.cachedBGRARep) {
            this.cachedBGRARep.releaseFrame();
            this.cachedBGRARep = null;
         }

         MediaDisposer.removeResourceDisposer(this.nativePeer);
         nativeDisposeBuffer(this.nativePeer);
         this.nativePeer = 0L;
      }

   }

   public double getTimestamp() {
      return 0L != this.nativePeer ? this.nativeGetTimestamp(this.nativePeer) : 0.0;
   }

   public ByteBuffer getBufferForPlane(int var1) {
      if (0L != this.nativePeer) {
         ByteBuffer var2 = this.nativeGetBufferForPlane(this.nativePeer, var1);
         var2.order(ByteOrder.nativeOrder());
         return var2;
      } else {
         return null;
      }
   }

   public int getWidth() {
      return 0L != this.nativePeer ? this.nativeGetWidth(this.nativePeer) : 0;
   }

   public int getHeight() {
      return 0L != this.nativePeer ? this.nativeGetHeight(this.nativePeer) : 0;
   }

   public int getEncodedWidth() {
      return 0L != this.nativePeer ? this.nativeGetEncodedWidth(this.nativePeer) : 0;
   }

   public int getEncodedHeight() {
      return 0L != this.nativePeer ? this.nativeGetEncodedHeight(this.nativePeer) : 0;
   }

   public VideoFormat getFormat() {
      if (0L != this.nativePeer) {
         int var1 = this.nativeGetFormat(this.nativePeer);
         return VideoFormat.formatForType(var1);
      } else {
         return null;
      }
   }

   public boolean hasAlpha() {
      return 0L != this.nativePeer ? this.nativeHasAlpha(this.nativePeer) : false;
   }

   public int getPlaneCount() {
      return 0L != this.nativePeer ? this.nativeGetPlaneCount(this.nativePeer) : 0;
   }

   public int getStrideForPlane(int var1) {
      if (0L != this.nativePeer) {
         int[] var2 = this.nativeGetPlaneStrides(this.nativePeer);
         return var2[var1];
      } else {
         return 0;
      }
   }

   public int[] getPlaneStrides() {
      return 0L != this.nativePeer ? this.nativeGetPlaneStrides(this.nativePeer) : null;
   }

   public VideoDataBuffer convertToFormat(VideoFormat var1) {
      if (0L != this.nativePeer) {
         if (var1 == VideoFormat.BGRA_PRE && null != this.cachedBGRARep) {
            this.cachedBGRARep.holdFrame();
            return this.cachedBGRARep;
         } else {
            long var2 = this.nativeConvertToFormat(this.nativePeer, var1.getNativeType());
            if (0L != var2) {
               NativeVideoBuffer var4 = createVideoBuffer(var2);
               if (var1 == VideoFormat.BGRA_PRE) {
                  var4.holdFrame();
                  this.cachedBGRARep = var4;
               }

               return var4;
            } else {
               throw new UnsupportedOperationException("Conversion from " + this.getFormat() + " to " + var1 + " is not supported.");
            }
         }
      } else {
         return null;
      }
   }

   public void setDirty() {
      if (0L != this.nativePeer) {
         this.nativeSetDirty(this.nativePeer);
      }

   }

   public String toString() {
      return "[NativeVideoBuffer peer=" + Long.toHexString(this.nativePeer) + ", format=" + this.getFormat() + ", size=(" + this.getWidth() + "," + this.getHeight() + "), timestamp=" + this.getTimestamp() + "]";
   }

   private static class VideoBufferDisposer implements MediaDisposer.ResourceDisposer {
      private VideoBufferDisposer() {
      }

      public void disposeResource(Object var1) {
         if (var1 instanceof Long) {
            NativeVideoBuffer.nativeDisposeBuffer((Long)var1);
         }

      }

      // $FF: synthetic method
      VideoBufferDisposer(Object var1) {
         this();
      }
   }
}
