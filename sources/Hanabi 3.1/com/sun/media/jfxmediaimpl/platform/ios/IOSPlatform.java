package com.sun.media.jfxmediaimpl.platform.ios;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.platform.Platform;
import java.util.Arrays;

public final class IOSPlatform extends Platform {
   private static final String[] CONTENT_TYPES = new String[]{"video/mp4", "audio/x-m4a", "video/x-m4v", "application/vnd.apple.mpegurl", "audio/mpegurl", "audio/mpeg", "audio/mp3", "audio/x-wav", "video/quicktime", "video/x-quicktime", "audio/x-aiff"};
   private static final String[] PROTOCOLS = new String[]{"http", "https", "ipod-library"};

   public static Platform getPlatformInstance() {
      return IOSPlatform.IOSPlatformInitializer.globalInstance;
   }

   private IOSPlatform() {
   }

   public boolean loadPlatform() {
      if (!HostUtils.isIOS()) {
         return false;
      } else {
         try {
            iosPlatformInit();
            return true;
         } catch (UnsatisfiedLinkError var2) {
            if (Logger.canLog(1)) {
               Logger.logMsg(1, "Unable to load iOS platform.");
            }

            return false;
         }
      }
   }

   public String[] getSupportedContentTypes() {
      return (String[])Arrays.copyOf(CONTENT_TYPES, CONTENT_TYPES.length);
   }

   public String[] getSupportedProtocols() {
      return (String[])Arrays.copyOf(PROTOCOLS, PROTOCOLS.length);
   }

   public Media createMedia(Locator var1) {
      return new IOSMedia(var1);
   }

   public MediaPlayer createMediaPlayer(Locator var1) {
      try {
         return new IOSMediaPlayer(var1);
      } catch (Exception var3) {
         if (Logger.canLog(1)) {
            Logger.logMsg(1, "IOSPlatform caught exception while creating media player: " + var3);
         }

         return null;
      }
   }

   private static native void iosPlatformInit();

   // $FF: synthetic method
   IOSPlatform(Object var1) {
      this();
   }

   private static final class IOSPlatformInitializer {
      private static final IOSPlatform globalInstance = new IOSPlatform();
   }
}
