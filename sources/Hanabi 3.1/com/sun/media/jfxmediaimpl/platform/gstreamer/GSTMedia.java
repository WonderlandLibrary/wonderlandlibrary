package com.sun.media.jfxmediaimpl.platform.gstreamer;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.media.jfxmediaimpl.NativeMedia;
import com.sun.media.jfxmediaimpl.platform.Platform;

final class GSTMedia extends NativeMedia {
   private final Object markerMutex = new Object();
   protected long refNativeMedia;

   GSTMedia(Locator var1) {
      super(var1);
      this.init();
   }

   public Platform getPlatform() {
      return GSTPlatform.getPlatformInstance();
   }

   private void init() {
      long[] var1 = new long[1];
      Locator var3 = this.getLocator();
      MediaError var2 = MediaError.getFromCode(this.gstInitNativeMedia(var3, var3.getContentType(), var3.getContentLength(), var1));
      if (var2 != MediaError.ERROR_NONE && var2 != MediaError.ERROR_PLATFORM_UNSUPPORTED) {
         MediaUtils.nativeError(this, var2);
      }

      this.refNativeMedia = var1[0];
   }

   long getNativeMediaRef() {
      return this.refNativeMedia;
   }

   public synchronized void dispose() {
      if (0L != this.refNativeMedia) {
         this.gstDispose(this.refNativeMedia);
         this.refNativeMedia = 0L;
      }

   }

   private native int gstInitNativeMedia(Locator var1, String var2, long var3, long[] var5);

   private native void gstDispose(long var1);
}
