package com.sun.glass.ui;

import java.security.AccessController;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Screen {
   private static volatile List screens = null;
   private static final int dpiOverride = (Integer)AccessController.doPrivileged(() -> {
      return Integer.getInteger("com.sun.javafx.screenDPI", 0);
   });
   private static EventHandler eventHandler;
   private volatile long ptr;
   private volatile int adapter;
   private final int depth;
   private final int x;
   private final int y;
   private final int width;
   private final int height;
   private final int platformX;
   private final int platformY;
   private final int platformWidth;
   private final int platformHeight;
   private final int visibleX;
   private final int visibleY;
   private final int visibleWidth;
   private final int visibleHeight;
   private final int resolutionX;
   private final int resolutionY;
   private final float platformScaleX;
   private final float platformScaleY;
   private final float outputScaleX;
   private final float outputScaleY;

   public static double getVideoRefreshPeriod() {
      Application.checkEventThread();
      return Application.GetApplication().staticScreen_getVideoRefreshPeriod();
   }

   public static Screen getMainScreen() {
      return (Screen)getScreens().get(0);
   }

   public static List getScreens() {
      if (screens == null) {
         throw new RuntimeException("Internal graphics not initialized yet");
      } else {
         return screens;
      }
   }

   public Screen(long var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, float var18, float var19, float var20, float var21) {
      this.ptr = var1;
      this.depth = var3;
      this.x = var4;
      this.y = var5;
      this.width = var6;
      this.height = var7;
      this.platformX = var8;
      this.platformY = var9;
      this.platformWidth = var10;
      this.platformHeight = var11;
      this.visibleX = var12;
      this.visibleY = var13;
      this.visibleWidth = var14;
      this.visibleHeight = var15;
      if (dpiOverride > 0) {
         this.resolutionX = this.resolutionY = dpiOverride;
      } else {
         this.resolutionX = var16;
         this.resolutionY = var17;
      }

      this.platformScaleX = var18;
      this.platformScaleY = var19;
      this.outputScaleX = var20;
      this.outputScaleY = var21;
   }

   public int getDepth() {
      return this.depth;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getPlatformX() {
      return this.platformX;
   }

   public int getPlatformY() {
      return this.platformY;
   }

   public int getPlatformWidth() {
      return this.platformWidth;
   }

   public int getPlatformHeight() {
      return this.platformHeight;
   }

   public float fromPlatformX(int var1) {
      return (float)this.x + (float)(var1 - this.platformX) / this.platformScaleX;
   }

   public float fromPlatformY(int var1) {
      return (float)this.y + (float)(var1 - this.platformY) / this.platformScaleY;
   }

   public int toPlatformX(float var1) {
      return this.platformX + Math.round((var1 - (float)this.x) * this.platformScaleX);
   }

   public int toPlatformY(float var1) {
      return this.platformY + Math.round((var1 - (float)this.y) * this.platformScaleY);
   }

   public float portionIntersectsPlatformRect(int var1, int var2, int var3, int var4) {
      int var5 = Math.max(var1, this.platformX);
      int var6 = Math.max(var2, this.platformY);
      int var7 = Math.min(var1 + var3, this.platformX + this.platformWidth);
      int var8 = Math.min(var2 + var4, this.platformY + this.platformHeight);
      if ((var7 -= var5) <= 0) {
         return 0.0F;
      } else if ((var8 -= var6) <= 0) {
         return 0.0F;
      } else {
         float var9 = (float)(var7 * var8);
         return var9 / (float)var3 / (float)var4;
      }
   }

   public boolean containsPlatformRect(int var1, int var2, int var3, int var4) {
      if (!this.containsPlatformCoords(var1, var2)) {
         return false;
      } else if (var3 > 0 && var4 > 0) {
         return var1 + var3 <= this.platformX + this.platformWidth && var2 + var4 <= this.platformY + this.platformHeight;
      } else {
         return true;
      }
   }

   public boolean containsPlatformCoords(int var1, int var2) {
      var1 -= this.platformX;
      var2 -= this.platformY;
      return var1 >= 0 && var1 < this.platformWidth && var2 >= 0 && var2 < this.platformHeight;
   }

   public float getPlatformScaleX() {
      return this.platformScaleX;
   }

   public float getPlatformScaleY() {
      return this.platformScaleY;
   }

   public float getRecommendedOutputScaleX() {
      return this.outputScaleX;
   }

   public float getRecommendedOutputScaleY() {
      return this.outputScaleY;
   }

   public int getVisibleX() {
      return this.visibleX;
   }

   public int getVisibleY() {
      return this.visibleY;
   }

   public int getVisibleWidth() {
      return this.visibleWidth;
   }

   public int getVisibleHeight() {
      return this.visibleHeight;
   }

   public int getResolutionX() {
      return this.resolutionX;
   }

   public int getResolutionY() {
      return this.resolutionY;
   }

   public long getNativeScreen() {
      return this.ptr;
   }

   private void dispose() {
      this.ptr = 0L;
   }

   public int getAdapterOrdinal() {
      return this.adapter;
   }

   public void setAdapterOrdinal(int var1) {
      this.adapter = var1;
   }

   public static void setEventHandler(EventHandler var0) {
      Application.checkEventThread();
      eventHandler = var0;
   }

   public static void notifySettingsChanged() {
      List var0 = screens;
      initScreens();
      if (eventHandler != null) {
         eventHandler.handleSettingsChanged();
      }

      List var1 = Window.getWindows();
      Iterator var2 = var1.iterator();

      while(true) {
         while(var2.hasNext()) {
            Window var3 = (Window)var2.next();
            Screen var4 = var3.getScreen();
            Iterator var5 = screens.iterator();

            while(var5.hasNext()) {
               Screen var6 = (Screen)var5.next();
               if (var4.getNativeScreen() == var6.getNativeScreen()) {
                  var3.setScreen(var6);
                  break;
               }
            }
         }

         if (var0 != null) {
            var2 = var0.iterator();

            while(var2.hasNext()) {
               Screen var7 = (Screen)var2.next();
               var7.dispose();
            }
         }

         return;
      }
   }

   static void initScreens() {
      Application.checkEventThread();
      Screen[] var0 = Application.GetApplication().staticScreen_getScreens();
      if (var0 == null) {
         throw new RuntimeException("Internal graphics failed to initialize");
      } else {
         screens = Collections.unmodifiableList(Arrays.asList(var0));
      }
   }

   public String toString() {
      return "Screen:\n    ptr:" + this.getNativeScreen() + "\n    adapter:" + this.getAdapterOrdinal() + "\n    depth:" + this.getDepth() + "\n    x:" + this.getX() + "\n    y:" + this.getY() + "\n    width:" + this.getWidth() + "\n    height:" + this.getHeight() + "\n    platformX:" + this.getPlatformX() + "\n    platformY:" + this.getPlatformY() + "\n    platformWidth:" + this.getPlatformWidth() + "\n    platformHeight:" + this.getPlatformHeight() + "\n    visibleX:" + this.getVisibleX() + "\n    visibleY:" + this.getVisibleY() + "\n    visibleWidth:" + this.getVisibleWidth() + "\n    visibleHeight:" + this.getVisibleHeight() + "\n    platformScaleX:" + this.getPlatformScaleX() + "\n    platformScaleY:" + this.getPlatformScaleY() + "\n    outputScaleX:" + this.getRecommendedOutputScaleX() + "\n    outputScaleY:" + this.getRecommendedOutputScaleY() + "\n    resolutionX:" + this.getResolutionX() + "\n    resolutionY:" + this.getResolutionY() + "\n";
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         Screen var2 = (Screen)var1;
         return this.ptr == var2.ptr && this.adapter == var2.adapter && this.depth == var2.depth && this.x == var2.x && this.y == var2.y && this.width == var2.width && this.height == var2.height && this.visibleX == var2.visibleX && this.visibleY == var2.visibleY && this.visibleWidth == var2.visibleWidth && this.visibleHeight == var2.visibleHeight && this.resolutionX == var2.resolutionX && this.resolutionY == var2.resolutionY && Float.compare(var2.platformScaleX, this.platformScaleX) == 0 && Float.compare(var2.platformScaleY, this.platformScaleY) == 0 && Float.compare(var2.outputScaleX, this.outputScaleX) == 0 && Float.compare(var2.outputScaleY, this.outputScaleY) == 0;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + (int)(this.ptr ^ this.ptr >>> 32);
      var1 = 31 * var1 + this.adapter;
      var1 = 31 * var1 + this.depth;
      var1 = 31 * var1 + this.x;
      var1 = 31 * var1 + this.y;
      var1 = 31 * var1 + this.width;
      var1 = 31 * var1 + this.height;
      var1 = 31 * var1 + this.visibleX;
      var1 = 31 * var1 + this.visibleY;
      var1 = 31 * var1 + this.visibleWidth;
      var1 = 31 * var1 + this.visibleHeight;
      var1 = 31 * var1 + this.resolutionX;
      var1 = 31 * var1 + this.resolutionY;
      var1 = 31 * var1 + (this.platformScaleX != 0.0F ? Float.floatToIntBits(this.platformScaleX) : 0);
      var1 = 31 * var1 + (this.platformScaleY != 0.0F ? Float.floatToIntBits(this.platformScaleY) : 0);
      var1 = 31 * var1 + (this.outputScaleX != 0.0F ? Float.floatToIntBits(this.outputScaleX) : 0);
      var1 = 31 * var1 + (this.outputScaleY != 0.0F ? Float.floatToIntBits(this.outputScaleY) : 0);
      return var1;
   }

   public static class EventHandler {
      public void handleSettingsChanged() {
      }
   }
}
