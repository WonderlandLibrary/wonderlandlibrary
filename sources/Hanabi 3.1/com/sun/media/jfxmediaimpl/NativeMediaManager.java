package com.sun.media.jfxmediaimpl;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.MediaPlayer;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.platform.PlatformManager;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;

public class NativeMediaManager {
   private static boolean isNativeLayerInitialized = false;
   private final List errorListeners = new ArrayList();
   private static final NativeMediaPlayerDisposer playerDisposer = new NativeMediaPlayerDisposer();
   private static final Map allMediaPlayers = new WeakHashMap();
   private final List supportedContentTypes = new ArrayList();
   private final List supportedProtocols = new ArrayList();

   public static NativeMediaManager getDefaultInstance() {
      return NativeMediaManager.NativeMediaManagerInitializer.globalInstance;
   }

   protected NativeMediaManager() {
      try {
         AccessController.doPrivileged(() -> {
            if (HostUtils.isWindows() || HostUtils.isMacOSX()) {
               NativeLibLoader.loadLibrary("glib-lite");
            }

            if (!HostUtils.isLinux() && !HostUtils.isIOS()) {
               NativeLibLoader.loadLibrary("gstreamer-lite");
            }

            NativeLibLoader.loadLibrary("jfxmedia");
            return null;
         });
      } catch (PrivilegedActionException var2) {
         MediaUtils.error((Object)null, MediaError.ERROR_MANAGER_ENGINEINIT_FAIL.code(), "Unable to load one or more dependent libraries.", var2);
      }

      if (!Logger.initNative()) {
         MediaUtils.error((Object)null, MediaError.ERROR_MANAGER_LOGGER_INIT.code(), "Unable to init logger", (Throwable)null);
      }

   }

   static synchronized void initNativeLayer() {
      if (!isNativeLayerInitialized) {
         PlatformManager.getManager().loadPlatforms();
         isNativeLayerInitialized = true;
      }

   }

   private synchronized void loadContentTypes() {
      if (this.supportedContentTypes.isEmpty()) {
         List var1 = PlatformManager.getManager().getSupportedContentTypes();
         if (null != var1 && !var1.isEmpty()) {
            this.supportedContentTypes.addAll(var1);
         }

         if (Logger.canLog(1)) {
            StringBuilder var2 = new StringBuilder("JFXMedia supported content types:\n");
            Iterator var3 = this.supportedContentTypes.iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               var2.append("    ");
               var2.append(var4);
               var2.append("\n");
            }

            Logger.logMsg(1, var2.toString());
         }

      }
   }

   private synchronized void loadProtocols() {
      if (this.supportedProtocols.isEmpty()) {
         List var1 = PlatformManager.getManager().getSupportedProtocols();
         if (null != var1 && !var1.isEmpty()) {
            this.supportedProtocols.addAll(var1);
         }

         if (Logger.canLog(1)) {
            StringBuilder var2 = new StringBuilder("JFXMedia supported protocols:\n");
            Iterator var3 = this.supportedProtocols.iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               var2.append("    ");
               var2.append(var4);
               var2.append("\n");
            }

            Logger.logMsg(1, var2.toString());
         }

      }
   }

   public boolean canPlayContentType(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("contentType == null!");
      } else {
         if (this.supportedContentTypes.isEmpty()) {
            this.loadContentTypes();
         }

         Iterator var2 = this.supportedContentTypes.iterator();

         String var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (String)var2.next();
         } while(!var1.equalsIgnoreCase(var3));

         return true;
      }
   }

   public String[] getSupportedContentTypes() {
      if (this.supportedContentTypes.isEmpty()) {
         this.loadContentTypes();
      }

      return (String[])this.supportedContentTypes.toArray(new String[1]);
   }

   public boolean canPlayProtocol(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("protocol == null!");
      } else {
         if (this.supportedProtocols.isEmpty()) {
            this.loadProtocols();
         }

         Iterator var2 = this.supportedProtocols.iterator();

         String var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (String)var2.next();
         } while(!var1.equalsIgnoreCase(var3));

         return true;
      }
   }

   public static MetadataParser getMetadataParser(Locator var0) {
      return PlatformManager.getManager().createMetadataParser(var0);
   }

   public MediaPlayer getPlayer(Locator var1) {
      initNativeLayer();
      MediaPlayer var2 = PlatformManager.getManager().createMediaPlayer(var1);
      if (null == var2) {
         throw new MediaException("Could not create player!");
      } else {
         allMediaPlayers.put(var2, Boolean.TRUE);
         return var2;
      }
   }

   public Media getMedia(Locator var1) {
      initNativeLayer();
      return PlatformManager.getManager().createMedia(var1);
   }

   public void addMediaErrorListener(MediaErrorListener var1) {
      if (var1 != null) {
         ListIterator var2 = this.errorListeners.listIterator();

         while(var2.hasNext()) {
            MediaErrorListener var3 = (MediaErrorListener)((WeakReference)var2.next()).get();
            if (var3 == null) {
               var2.remove();
            }
         }

         this.errorListeners.add(new WeakReference(var1));
      }

   }

   public void removeMediaErrorListener(MediaErrorListener var1) {
      if (var1 != null) {
         ListIterator var2 = this.errorListeners.listIterator();

         while(true) {
            MediaErrorListener var3;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = (MediaErrorListener)((WeakReference)var2.next()).get();
            } while(var3 != null && var3 != var1);

            var2.remove();
         }
      }
   }

   public static void registerMediaPlayerForDispose(Object var0, MediaPlayer var1) {
      MediaDisposer.addResourceDisposer(var0, var1, playerDisposer);
   }

   public List getAllMediaPlayers() {
      ArrayList var1 = null;
      if (!allMediaPlayers.isEmpty()) {
         var1 = new ArrayList(allMediaPlayers.keySet());
      }

      return var1;
   }

   List getMediaErrorListeners() {
      return this.errorListeners;
   }

   private static class NativeMediaPlayerDisposer implements MediaDisposer.ResourceDisposer {
      private NativeMediaPlayerDisposer() {
      }

      public void disposeResource(Object var1) {
         MediaPlayer var2 = (MediaPlayer)var1;
         if (var2 != null) {
            var2.dispose();
         }

      }

      // $FF: synthetic method
      NativeMediaPlayerDisposer(Object var1) {
         this();
      }
   }

   private static class NativeMediaManagerInitializer {
      private static final NativeMediaManager globalInstance = new NativeMediaManager();
   }
}
