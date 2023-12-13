package com.sun.media.jfxmedia;

import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.NativeMediaManager;
import java.util.List;

public class MediaManager {
   private MediaManager() {
   }

   public static String[] getSupportedContentTypes() {
      return NativeMediaManager.getDefaultInstance().getSupportedContentTypes();
   }

   public static boolean canPlayContentType(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("contentType == null!");
      } else {
         return NativeMediaManager.getDefaultInstance().canPlayContentType(var0);
      }
   }

   public static boolean canPlayProtocol(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("protocol == null!");
      } else {
         return NativeMediaManager.getDefaultInstance().canPlayProtocol(var0);
      }
   }

   public static MetadataParser getMetadataParser(Locator var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("locator == null!");
      } else {
         NativeMediaManager.getDefaultInstance();
         return NativeMediaManager.getMetadataParser(var0);
      }
   }

   public static Media getMedia(Locator var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("locator == null!");
      } else {
         return NativeMediaManager.getDefaultInstance().getMedia(var0);
      }
   }

   public static MediaPlayer getPlayer(Locator var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("locator == null!");
      } else {
         return NativeMediaManager.getDefaultInstance().getPlayer(var0);
      }
   }

   public static void addMediaErrorListener(MediaErrorListener var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("listener == null!");
      } else {
         NativeMediaManager.getDefaultInstance().addMediaErrorListener(var0);
      }
   }

   public static void removeMediaErrorListener(MediaErrorListener var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("listener == null!");
      } else {
         NativeMediaManager.getDefaultInstance().removeMediaErrorListener(var0);
      }
   }

   public static void registerMediaPlayerForDispose(Object var0, MediaPlayer var1) {
      NativeMediaManager.registerMediaPlayerForDispose(var0, var1);
   }

   public static List getAllMediaPlayers() {
      return NativeMediaManager.getDefaultInstance().getAllMediaPlayers();
   }
}
