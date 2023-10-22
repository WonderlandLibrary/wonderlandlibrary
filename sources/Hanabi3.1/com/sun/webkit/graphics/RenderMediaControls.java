package com.sun.webkit.graphics;

import com.sun.prism.paint.Color;
import java.util.HashMap;
import java.util.Map;

final class RenderMediaControls {
   private static final int PLAY_BUTTON = 1;
   private static final int PAUSE_BUTTON = 2;
   private static final int DISABLED_PLAY_BUTTON = 3;
   private static final int MUTE_BUTTON = 4;
   private static final int UNMUTE_BUTTON = 5;
   private static final int DISABLED_MUTE_BUTTON = 6;
   private static final int TIME_SLIDER_TRACK = 9;
   private static final int TIME_SLIDER_THUMB = 10;
   private static final int VOLUME_CONTAINER = 11;
   private static final int VOLUME_TRACK = 12;
   private static final int VOLUME_THUMB = 13;
   private static final Color TimeSliderTrackUnbufferedColor = rgba(236, 135, 125);
   private static final Color TimeSliderTrackBufferedColor = rgba(249, 26, 2);
   private static final int TimeSliderTrackThickness = 3;
   private static final Color VolumeTrackColor = rgba(208, 208, 208, 128);
   private static final int VolumeTrackThickness = 1;
   private static final int SLIDER_TYPE_TIME = 0;
   private static final int SLIDER_TYPE_VOLUME = 1;
   private static final Map controlImages = new HashMap();
   private static final boolean log = false;

   private static String getControlName(int var0) {
      switch (var0) {
         case 1:
            return "PLAY_BUTTON";
         case 2:
            return "PAUSE_BUTTON";
         case 3:
            return "DISABLED_PLAY_BUTTON";
         case 4:
            return "MUTE_BUTTON";
         case 5:
            return "UNMUTE_BUTTON";
         case 6:
            return "DISABLED_MUTE_BUTTON";
         case 7:
         case 8:
         default:
            return "{UNKNOWN CONTROL " + var0 + "}";
         case 9:
            return "TIME_SLIDER_TRACK";
         case 10:
            return "TIME_SLIDER_THUMB";
         case 11:
            return "VOLUME_CONTAINER";
         case 12:
            return "VOLUME_TRACK";
         case 13:
            return "VOLUME_THUMB";
      }
   }

   private RenderMediaControls() {
   }

   static void paintControl(WCGraphicsContext var0, int var1, int var2, int var3, int var4, int var5) {
      switch (var1) {
         case 1:
            paintControlImage("mediaPlay", var0, var2, var3, var4, var5);
            break;
         case 2:
            paintControlImage("mediaPause", var0, var2, var3, var4, var5);
            break;
         case 3:
            paintControlImage("mediaPlayDisabled", var0, var2, var3, var4, var5);
            break;
         case 4:
            paintControlImage("mediaMute", var0, var2, var3, var4, var5);
            break;
         case 5:
            paintControlImage("mediaUnmute", var0, var2, var3, var4, var5);
            break;
         case 6:
            paintControlImage("mediaMuteDisabled", var0, var2, var3, var4, var5);
         case 7:
         case 8:
         case 9:
         case 11:
         case 12:
         default:
            break;
         case 10:
            paintControlImage("mediaTimeThumb", var0, var2, var3, var4, var5);
            break;
         case 13:
            paintControlImage("mediaVolumeThumb", var0, var2, var3, var4, var5);
      }

   }

   static void paintTimeSliderTrack(WCGraphicsContext var0, float var1, float var2, float[] var3, int var4, int var5, int var6, int var7) {
      var5 += (var7 - 3) / 2;
      byte var12 = 3;
      int var8 = fwkGetSliderThumbSize(0) >> 16 & '\uffff';
      var6 -= var8;
      var4 += var8 / 2;
      if (!(var1 < 0.0F)) {
         float var9 = 1.0F / var1 * (float)var6;
         float var10 = 0.0F;

         for(int var11 = 0; var11 < var3.length; var11 += 2) {
            var0.fillRect((float)var4 + var9 * var10, (float)var5, var9 * (var3[var11] - var10), (float)var12, TimeSliderTrackUnbufferedColor);
            var0.fillRect((float)var4 + var9 * var3[var11], (float)var5, var9 * (var3[var11 + 1] - var3[var11]), (float)var12, TimeSliderTrackBufferedColor);
            var10 = var3[var11 + 1];
         }

         if (var10 < var1) {
            var0.fillRect((float)var4 + var9 * var10, (float)var5, var9 * (var1 - var10), (float)var12, TimeSliderTrackUnbufferedColor);
         }
      }

   }

   static void paintVolumeTrack(WCGraphicsContext var0, float var1, boolean var2, int var3, int var4, int var5, int var6) {
      var3 += (var5 + 1 - 1) / 2;
      byte var8 = 1;
      int var7 = fwkGetSliderThumbSize(0) & '\uffff';
      var6 -= var7;
      var4 += var7 / 2;
      var0.fillRect((float)var3, (float)var4, (float)var8, (float)var6, VolumeTrackColor);
   }

   private static int fwkGetSliderThumbSize(int var0) {
      WCImage var1 = null;
      switch (var0) {
         case 0:
            var1 = getControlImage("mediaTimeThumb");
            break;
         case 1:
            var1 = getControlImage("mediaVolumeThumb");
      }

      return var1 != null ? var1.getWidth() << 16 | var1.getHeight() : 0;
   }

   private static WCImage getControlImage(String var0) {
      WCImage var1 = (WCImage)controlImages.get(var0);
      if (var1 == null) {
         WCImageDecoder var2 = WCGraphicsManager.getGraphicsManager().getImageDecoder();
         var2.loadFromResource(var0);
         WCImageFrame var3 = var2.getFrame(0);
         if (var3 != null) {
            var1 = var3.getFrame();
            controlImages.put(var0, var1);
         }
      }

      return var1;
   }

   private static void paintControlImage(String var0, WCGraphicsContext var1, int var2, int var3, int var4, int var5) {
      WCImage var6 = getControlImage(var0);
      if (var6 != null) {
         var2 += (var4 - var6.getWidth()) / 2;
         var4 = var6.getWidth();
         var3 += (var5 - var6.getHeight()) / 2;
         var5 = var6.getHeight();
         var1.drawImage(var6, (float)var2, (float)var3, (float)var4, (float)var5, 0.0F, 0.0F, (float)var6.getWidth(), (float)var6.getHeight());
      }

   }

   private static Color rgba(int var0, int var1, int var2, int var3) {
      return new Color((float)(var0 & 255) / 255.0F, (float)(var1 & 255) / 255.0F, (float)(var2 & 255) / 255.0F, (float)(var3 & 255) / 255.0F);
   }

   private static Color rgba(int var0, int var1, int var2) {
      return rgba(var0, var1, var2, 255);
   }

   private static void log(String var0) {
      System.out.println(var0);
      System.out.flush();
   }
}
