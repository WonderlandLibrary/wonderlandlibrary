package com.sun.media.jfxmediaimpl.platform.java;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmediaimpl.platform.Platform;

public final class JavaPlatform extends Platform {
   private static JavaPlatform globalInstance = null;

   public static synchronized Platform getPlatformInstance() {
      if (null == globalInstance) {
         globalInstance = new JavaPlatform();
      }

      return globalInstance;
   }

   JavaPlatform() {
   }

   public boolean loadPlatform() {
      return true;
   }

   public MetadataParser createMetadataParser(Locator var1) {
      String var2 = var1.getContentType();
      if (!var2.equals("video/x-javafx") && !var2.equals("video/x-flv")) {
         return !var2.equals("audio/mpeg") && !var2.equals("audio/mp3") ? null : new ID3MetadataParser(var1);
      } else {
         return new FLVMetadataParser(var1);
      }
   }

   public Media createMedia(Locator var1) {
      return null;
   }

   public MediaPlayer createMediaPlayer(Locator var1) {
      return null;
   }
}
