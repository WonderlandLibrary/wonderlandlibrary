package com.sun.media.jfxmediaimpl.platform;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.HostUtils;
import com.sun.media.jfxmediaimpl.platform.gstreamer.GSTPlatform;
import com.sun.media.jfxmediaimpl.platform.ios.IOSPlatform;
import com.sun.media.jfxmediaimpl.platform.java.JavaPlatform;
import com.sun.media.jfxmediaimpl.platform.osx.OSXPlatform;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class PlatformManager {
   private static String enabledPlatforms;
   private final List platforms;

   private static void getPlatformSettings() {
      enabledPlatforms = System.getProperty("jfxmedia.platforms", "").toLowerCase();
   }

   private static boolean isPlatformEnabled(String var0) {
      if (null != enabledPlatforms && enabledPlatforms.length() != 0) {
         return enabledPlatforms.indexOf(var0.toLowerCase()) != -1;
      } else {
         return true;
      }
   }

   public static PlatformManager getManager() {
      return PlatformManager.PlatformManagerInitializer.globalInstance;
   }

   private PlatformManager() {
      this.platforms = new ArrayList();
      Platform var1;
      if (isPlatformEnabled("JavaPlatform")) {
         var1 = JavaPlatform.getPlatformInstance();
         if (null != var1) {
            this.platforms.add(var1);
         }
      }

      if (!HostUtils.isIOS() && isPlatformEnabled("GSTPlatform")) {
         var1 = GSTPlatform.getPlatformInstance();
         if (null != var1) {
            this.platforms.add(var1);
         }
      }

      if (HostUtils.isMacOSX() && isPlatformEnabled("OSXPlatform")) {
         var1 = OSXPlatform.getPlatformInstance();
         if (null != var1) {
            this.platforms.add(var1);
         }
      }

      if (HostUtils.isIOS() && isPlatformEnabled("IOSPlatform")) {
         var1 = IOSPlatform.getPlatformInstance();
         if (null != var1) {
            this.platforms.add(var1);
         }
      }

      if (Logger.canLog(1)) {
         StringBuilder var2 = new StringBuilder("Enabled JFXMedia platforms: ");
         Iterator var3 = this.platforms.iterator();

         while(var3.hasNext()) {
            Platform var4 = (Platform)var3.next();
            var2.append("\n   - ");
            var2.append(var4.getClass().getName());
         }

         Logger.logMsg(1, var2.toString());
      }

   }

   public synchronized void loadPlatforms() {
      Iterator var1 = this.platforms.iterator();

      while(var1.hasNext()) {
         Platform var2 = (Platform)var1.next();
         if (!var2.loadPlatform()) {
            if (Logger.canLog(1)) {
               Logger.logMsg(1, "Failed to load platform: " + var2);
            }

            var1.remove();
         }
      }

   }

   public List getSupportedContentTypes() {
      ArrayList var1 = new ArrayList();
      if (!this.platforms.isEmpty()) {
         Iterator var2 = this.platforms.iterator();

         while(true) {
            String[] var4;
            do {
               if (!var2.hasNext()) {
                  return var1;
               }

               Platform var3 = (Platform)var2.next();
               if (Logger.canLog(1)) {
                  Logger.logMsg(1, "Getting content types from platform: " + var3);
               }

               var4 = var3.getSupportedContentTypes();
            } while(var4 == null);

            String[] var5 = var4;
            int var6 = var4.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String var8 = var5[var7];
               if (!var1.contains(var8)) {
                  var1.add(var8);
               }
            }
         }
      } else {
         return var1;
      }
   }

   public List getSupportedProtocols() {
      ArrayList var1 = new ArrayList();
      if (!this.platforms.isEmpty()) {
         Iterator var2 = this.platforms.iterator();

         while(true) {
            String[] var4;
            do {
               if (!var2.hasNext()) {
                  return var1;
               }

               Platform var3 = (Platform)var2.next();
               var4 = var3.getSupportedProtocols();
            } while(var4 == null);

            String[] var5 = var4;
            int var6 = var4.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String var8 = var5[var7];
               if (!var1.contains(var8)) {
                  var1.add(var8);
               }
            }
         }
      } else {
         return var1;
      }
   }

   public MetadataParser createMetadataParser(Locator var1) {
      Iterator var2 = this.platforms.iterator();

      MetadataParser var4;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         Platform var3 = (Platform)var2.next();
         var4 = var3.createMetadataParser(var1);
      } while(var4 == null);

      return var4;
   }

   public Media createMedia(Locator var1) {
      String var2 = var1.getContentType();
      String var3 = var1.getProtocol();
      Iterator var4 = this.platforms.iterator();

      while(var4.hasNext()) {
         Platform var5 = (Platform)var4.next();
         if (var5.canPlayContentType(var2) && var5.canPlayProtocol(var3)) {
            Media var6 = var5.createMedia(var1);
            if (null != var6) {
               return var6;
            }
         }
      }

      return null;
   }

   public MediaPlayer createMediaPlayer(Locator var1) {
      String var2 = var1.getContentType();
      String var3 = var1.getProtocol();
      Iterator var4 = this.platforms.iterator();

      while(var4.hasNext()) {
         Platform var5 = (Platform)var4.next();
         if (var5.canPlayContentType(var2) && var5.canPlayProtocol(var3)) {
            MediaPlayer var6 = var5.createMediaPlayer(var1);
            if (null != var6) {
               return var6;
            }
         }
      }

      return null;
   }

   // $FF: synthetic method
   PlatformManager(Object var1) {
      this();
   }

   static {
      AccessController.doPrivileged(() -> {
         getPlatformSettings();
         return null;
      });
   }

   private static final class PlatformManagerInitializer {
      private static final PlatformManager globalInstance = new PlatformManager();
   }
}
