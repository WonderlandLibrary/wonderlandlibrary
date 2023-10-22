package com.sun.webkit.graphics;

import com.sun.webkit.SharedBuffer;
import com.sun.webkit.SimpleSharedBufferInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class WCGraphicsManager {
   private static final Logger logger = Logger.getLogger(WCGraphicsManager.class.getName());
   private final AtomicInteger idCount = new AtomicInteger(0);
   private final HashMap refMap = new HashMap();
   private static ResourceBundle imageProperties = null;
   private static WCGraphicsManager manager = null;

   public static void setGraphicsManager(WCGraphicsManager var0) {
      manager = var0;
   }

   public static WCGraphicsManager getGraphicsManager() {
      return manager;
   }

   public abstract float getDevicePixelScale();

   protected abstract WCImageDecoder getImageDecoder();

   public abstract WCGraphicsContext createGraphicsContext(Object var1);

   public abstract WCRenderQueue createRenderQueue(WCRectangle var1, boolean var2);

   protected abstract WCRenderQueue createBufferedContextRQ(WCImage var1);

   public abstract WCPageBackBuffer createPageBackBuffer();

   protected abstract WCFont getWCFont(String var1, boolean var2, boolean var3, float var4);

   private WCFontCustomPlatformData fwkCreateFontCustomPlatformData(SharedBuffer var1) {
      try {
         return this.createFontCustomPlatformData(new SimpleSharedBufferInputStream(var1));
      } catch (IOException var3) {
         logger.log(Level.FINEST, "Error creating font custom platform data", var3);
         return null;
      }
   }

   protected abstract WCFontCustomPlatformData createFontCustomPlatformData(InputStream var1) throws IOException;

   protected abstract WCPath createWCPath();

   protected abstract WCPath createWCPath(WCPath var1);

   protected abstract WCImage createWCImage(int var1, int var2);

   protected abstract WCImage createRTImage(int var1, int var2);

   public abstract WCImage getIconImage(String var1);

   public abstract Object toPlatformImage(WCImage var1);

   protected abstract WCImageFrame createFrame(int var1, int var2, ByteBuffer var3);

   public static String getResourceName(String var0) {
      if (imageProperties == null) {
         imageProperties = ResourceBundle.getBundle("com.sun.webkit.graphics.Images");
      }

      try {
         return imageProperties.getString(var0);
      } catch (MissingResourceException var2) {
         return var0;
      }
   }

   private void fwkLoadFromResource(String var1, long var2) {
      InputStream var4 = this.getClass().getResourceAsStream(getResourceName(var1));
      if (var4 != null) {
         byte[] var5 = new byte[1024];

         try {
            int var6;
            while((var6 = var4.read(var5)) > -1) {
               append(var2, var5, var6);
            }

            var4.close();
         } catch (IOException var8) {
         }

      }
   }

   protected abstract WCTransform createTransform(double var1, double var3, double var5, double var7, double var9, double var11);

   protected String[] getSupportedMediaTypes() {
      return new String[0];
   }

   private WCMediaPlayer fwkCreateMediaPlayer(long var1) {
      WCMediaPlayer var3 = this.createMediaPlayer();
      var3.setNativePointer(var1);
      return var3;
   }

   protected abstract WCMediaPlayer createMediaPlayer();

   int createID() {
      return this.idCount.incrementAndGet();
   }

   synchronized void ref(Ref var1) {
      this.refMap.put(var1.getID(), var1);
   }

   synchronized Ref deref(Ref var1) {
      return (Ref)this.refMap.remove(var1.getID());
   }

   synchronized Ref getRef(int var1) {
      return (Ref)this.refMap.get(var1);
   }

   private static native void append(long var0, byte[] var2, int var3);
}
