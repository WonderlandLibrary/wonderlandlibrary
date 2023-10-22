package com.sun.media.jfxmediaimpl.platform;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.locator.Locator;

public abstract class Platform {
   public static Platform getPlatformInstance() {
      throw new UnsupportedOperationException("Invalid platform class.");
   }

   public boolean loadPlatform() {
      return false;
   }

   public boolean canPlayContentType(String var1) {
      String[] var2 = this.getSupportedContentTypes();
      if (var2 != null) {
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var6.equalsIgnoreCase(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean canPlayProtocol(String var1) {
      String[] var2 = this.getSupportedProtocols();
      if (var2 != null) {
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var6.equalsIgnoreCase(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   public String[] getSupportedContentTypes() {
      return new String[0];
   }

   public String[] getSupportedProtocols() {
      return new String[0];
   }

   public MetadataParser createMetadataParser(Locator var1) {
      return null;
   }

   public abstract Media createMedia(Locator var1);

   public abstract MediaPlayer createMediaPlayer(Locator var1);
}
