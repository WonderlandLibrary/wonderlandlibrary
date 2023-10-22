package com.sun.webkit.graphics;

public abstract class ScrollBarTheme extends Ref {
   public static final int NO_PART = 0;
   public static final int BACK_BUTTON_START_PART = 1;
   public static final int FORWARD_BUTTON_START_PART = 2;
   public static final int BACK_TRACK_PART = 4;
   public static final int THUMB_PART = 8;
   public static final int FORWARD_TRACK_PART = 16;
   public static final int BACK_BUTTON_END_PART = 32;
   public static final int FORWARD_BUTTON_END_PART = 64;
   public static final int SCROLLBAR_BG_PART = 128;
   public static final int TRACK_BG_PART = 256;
   public static final int HORIZONTAL_SCROLLBAR = 0;
   public static final int VERTICAL_SCROLLBAR = 1;
   private static int thickness;

   public static int getThickness() {
      return thickness > 0 ? thickness : 12;
   }

   public static void setThickness(int var0) {
      thickness = var0;
   }

   protected abstract Ref createWidget(long var1, int var3, int var4, int var5, int var6, int var7, int var8);

   public abstract void paint(WCGraphicsContext var1, Ref var2, int var3, int var4, int var5, int var6);

   protected abstract void getScrollBarPartRect(long var1, int var3, int[] var4);

   public abstract WCSize getWidgetSize(Ref var1);
}
