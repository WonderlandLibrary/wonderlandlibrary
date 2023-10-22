package com.sun.javafx.webkit.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.media.jfxmedia.MediaManager;
import com.sun.prism.Graphics;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCFontCustomPlatformData;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCImageDecoder;
import com.sun.webkit.graphics.WCImageFrame;
import com.sun.webkit.graphics.WCMediaPlayer;
import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPath;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCRenderQueue;
import com.sun.webkit.graphics.WCTransform;
import com.sun.webkit.perf.WCFontPerfLogger;
import com.sun.webkit.perf.WCGraphicsPerfLogger;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;

public final class PrismGraphicsManager extends WCGraphicsManager {
   private static final float highestPixelScale;
   private static final BaseTransform pixelScaleTransform;

   static BaseTransform getPixelScaleTransform() {
      return pixelScaleTransform;
   }

   public float getDevicePixelScale() {
      return highestPixelScale;
   }

   protected WCImageDecoder getImageDecoder() {
      return new WCImageDecoderImpl();
   }

   public WCRenderQueue createRenderQueue(WCRectangle var1, boolean var2) {
      return new WCRenderQueueImpl(var1, var2);
   }

   protected WCRenderQueue createBufferedContextRQ(WCImage var1) {
      WCBufferedContext var2 = new WCBufferedContext((PrismImage)var1);
      WCRenderQueueImpl var3 = new WCRenderQueueImpl((WCGraphicsContext)(WCGraphicsPerfLogger.isEnabled() ? new WCGraphicsPerfLogger(var2) : var2));
      var1.setRQ(var3);
      return var3;
   }

   protected WCFont getWCFont(String var1, boolean var2, boolean var3, float var4) {
      WCFont var5 = WCFontImpl.getFont(var1, var2, var3, var4);
      return (WCFont)(WCFontPerfLogger.isEnabled() && var5 != null ? new WCFontPerfLogger(var5) : var5);
   }

   protected WCFontCustomPlatformData createFontCustomPlatformData(InputStream var1) throws IOException {
      return new WCFontCustomPlatformDataImpl(var1);
   }

   public WCGraphicsContext createGraphicsContext(Object var1) {
      WCGraphicsPrismContext var2 = new WCGraphicsPrismContext((Graphics)var1);
      return (WCGraphicsContext)(WCGraphicsPerfLogger.isEnabled() ? new WCGraphicsPerfLogger(var2) : var2);
   }

   public WCPageBackBuffer createPageBackBuffer() {
      return new WCPageBackBufferImpl(highestPixelScale);
   }

   protected WCPath createWCPath() {
      return new WCPathImpl();
   }

   protected WCPath createWCPath(WCPath var1) {
      return new WCPathImpl((WCPathImpl)var1);
   }

   protected WCImage createWCImage(int var1, int var2) {
      return new WCImageImpl(var1, var2);
   }

   protected WCImage createRTImage(int var1, int var2) {
      return new RTImage(var1, var2, highestPixelScale);
   }

   public WCImage getIconImage(String var1) {
      return null;
   }

   public Object toPlatformImage(WCImage var1) {
      return ((WCImageImpl)var1).getImage();
   }

   protected WCImageFrame createFrame(final int var1, final int var2, ByteBuffer var3) {
      int[] var4 = new int[var3.capacity() / 4];
      var3.order(ByteOrder.nativeOrder());
      var3.asIntBuffer().get(var4);
      final WCImageImpl var5 = new WCImageImpl(var4, var1, var2);
      return new WCImageFrame() {
         public WCImage getFrame() {
            return var5;
         }

         public int[] getSize() {
            return new int[]{var1, var2};
         }
      };
   }

   protected WCTransform createTransform(double var1, double var3, double var5, double var7, double var9, double var11) {
      return new WCTransform(var1, var3, var5, var7, var9, var11);
   }

   protected String[] getSupportedMediaTypes() {
      String[] var1 = MediaManager.getSupportedContentTypes();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         if ("video/x-flv".compareToIgnoreCase(var1[var3]) == 0) {
            System.arraycopy(var1, var3 + 1, var1, var3, var2 - (var3 + 1));
            --var2;
         }
      }

      if (var2 < var1.length) {
         String[] var4 = new String[var2];
         System.arraycopy(var1, 0, var4, 0, var2);
         var1 = var4;
      }

      return var1;
   }

   protected WCMediaPlayer createMediaPlayer() {
      return new WCMediaPlayerImpl();
   }

   static {
      float var0 = 1.0F;

      Screen var2;
      for(Iterator var1 = Screen.getScreens().iterator(); var1.hasNext(); var0 = Math.max(var2.getRecommendedOutputScaleY(), var0)) {
         var2 = (Screen)var1.next();
         var0 = Math.max(var2.getRecommendedOutputScaleX(), var0);
      }

      highestPixelScale = (float)Math.ceil((double)var0);
      pixelScaleTransform = BaseTransform.getScaleInstance((double)highestPixelScale, (double)highestPixelScale);
   }
}
