package com.sun.media.jfxmediaimpl.platform.gstreamer;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.MediaUtils;
import com.sun.media.jfxmediaimpl.platform.Platform;
import java.util.Arrays;

public final class GSTPlatform extends Platform {
   private static final String[] CONTENT_TYPES = new String[]{"audio/x-aiff", "audio/mp3", "audio/mpeg", "audio/x-wav", "video/x-javafx", "video/x-flv", "video/x-fxm", "video/mp4", "audio/x-m4a", "video/x-m4v", "application/vnd.apple.mpegurl", "audio/mpegurl"};
   private static final String[] PROTOCOLS = new String[]{"file", "http", "https"};
   private static GSTPlatform globalInstance = null;

   public boolean loadPlatform() {
      MediaError var1;
      try {
         var1 = MediaError.getFromCode(gstInitPlatform());
      } catch (UnsatisfiedLinkError var3) {
         var1 = MediaError.ERROR_MANAGER_ENGINEINIT_FAIL;
      }

      if (var1 != MediaError.ERROR_NONE) {
         MediaUtils.nativeError(GSTPlatform.class, var1);
      }

      return true;
   }

   public static synchronized Platform getPlatformInstance() {
      if (null == globalInstance) {
         globalInstance = new GSTPlatform();
      }

      return globalInstance;
   }

   private GSTPlatform() {
   }

   public String[] getSupportedContentTypes() {
      return (String[])Arrays.copyOf(CONTENT_TYPES, CONTENT_TYPES.length);
   }

   public String[] getSupportedProtocols() {
      return (String[])Arrays.copyOf(PROTOCOLS, PROTOCOLS.length);
   }

   public Media createMedia(Locator var1) {
      return new GSTMedia(var1);
   }

   public MediaPlayer createMediaPlayer(Locator var1) {
      GSTMediaPlayer var2;
      try {
         var2 = new GSTMediaPlayer(var1);
      } catch (Exception var17) {
         if (Logger.canLog(1)) {
            Logger.logMsg(1, "GSTPlatform caught exception while creating media player: " + var17);
         }

         return null;
      }

      if (HostUtils.isMacOSX()) {
         String var3 = var1.getContentType();
         if ("video/mp4".equals(var3) || "video/x-m4v".equals(var3) || var1.getStringLocation().endsWith(".m3u8")) {
            String var4 = var1.getURI().getScheme();
            long var5 = !var4.equals("http") && !var4.equals("https") ? 5000L : 60000L;
            long var9 = 0L;
            Object var11 = new Object();

            for(PlayerStateEvent.PlayerState var12 = var2.getState(); var9 < var5 && (var12 == PlayerStateEvent.PlayerState.UNKNOWN || var12 == PlayerStateEvent.PlayerState.STALLED); var12 = var2.getState()) {
               try {
                  synchronized(var11) {
                     var11.wait(50L);
                     var9 += 50L;
                  }
               } catch (InterruptedException var16) {
               }

               if (var2.isErrorEventCached()) {
                  break;
               }
            }

            if (var2.getState() != PlayerStateEvent.PlayerState.READY) {
               var2.dispose();
               var2 = null;
            }
         }
      }

      return var2;
   }

   private static native int gstInitPlatform();
}
