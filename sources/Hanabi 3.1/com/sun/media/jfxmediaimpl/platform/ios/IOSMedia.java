package com.sun.media.jfxmediaimpl.platform.ios;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.media.jfxmediaimpl.NativeMedia;
import com.sun.media.jfxmediaimpl.platform.Platform;

final class IOSMedia extends NativeMedia {
   private long refNativeMedia;

   IOSMedia(Locator var1) {
      super(var1);
      this.init();
   }

   public Platform getPlatform() {
      return IOSPlatform.getPlatformInstance();
   }

   private void init() {
      long[] var1 = new long[1];
      Locator var2 = this.getLocator();
      MediaError var3 = MediaError.getFromCode(this.iosInitNativeMedia(var2, var2.getContentType(), var2.getContentLength(), var1));
      if (var3 != MediaError.ERROR_NONE) {
         MediaUtils.nativeError(this, var3);
      }

      this.refNativeMedia = var1[0];
   }

   long getNativeMediaRef() {
      return this.refNativeMedia;
   }

   public synchronized void dispose() {
      if (0L != this.refNativeMedia) {
         this.iosDispose(this.refNativeMedia);
         this.refNativeMedia = 0L;
      }

   }

   private native int iosInitNativeMedia(Locator var1, String var2, long var3, long[] var5);

   private native void iosDispose(long var1);
}
