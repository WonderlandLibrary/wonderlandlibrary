package com.sun.media.jfxmediaimpl.platform.osx;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.platform.Platform;
import java.security.AccessController;
import java.util.Arrays;

public final class OSXPlatform extends Platform {
   private static final String[] CONTENT_TYPES = new String[]{"audio/x-aiff", "audio/mp3", "audio/mpeg", "audio/x-m4a", "video/mp4", "video/x-m4v", "application/vnd.apple.mpegurl", "audio/mpegurl"};
   private static final String[] PROTOCOLS = new String[]{"file", "http", "https"};

   public static Platform getPlatformInstance() {
      return OSXPlatform.OSXPlatformInitializer.globalInstance;
   }

   private OSXPlatform() {
   }

   public boolean loadPlatform() {
      if (!HostUtils.isMacOSX()) {
         return false;
      } else {
         try {
            return osxPlatformInit();
         } catch (UnsatisfiedLinkError var2) {
            if (Logger.canLog(1)) {
               Logger.logMsg(1, "Unable to load OSX platform.");
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
      return new OSXMedia(var1);
   }

   public MediaPlayer createMediaPlayer(Locator var1) {
      try {
         return new OSXMediaPlayer(var1);
      } catch (Exception var3) {
         if (Logger.canLog(1)) {
            Logger.logMsg(1, "OSXPlatform caught exception while creating media player: " + var3);
            var3.printStackTrace();
         }

         return null;
      }
   }

   private static native boolean osxPlatformInit();

   // $FF: synthetic method
   OSXPlatform(Object var1) {
      this();
   }

   private static final class OSXPlatformInitializer {
      private static final OSXPlatform globalInstance;

      static {
         boolean var0 = false;

         try {
            var0 = (Boolean)AccessController.doPrivileged(() -> {
               boolean var0 = false;
               boolean var1 = false;

               try {
                  NativeLibLoader.loadLibrary("jfxmedia_avf");
                  var0 = true;
               } catch (UnsatisfiedLinkError var4) {
               }

               try {
                  NativeLibLoader.loadLibrary("jfxmedia_qtkit");
                  var1 = true;
               } catch (UnsatisfiedLinkError var3) {
               }

               return var0 || var1;
            });
         } catch (Exception var2) {
         }

         if (var0) {
            globalInstance = new OSXPlatform();
         } else {
            globalInstance = null;
         }

      }
   }
}
