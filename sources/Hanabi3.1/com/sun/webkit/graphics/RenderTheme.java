package com.sun.webkit.graphics;

import java.nio.ByteBuffer;

public abstract class RenderTheme extends Ref {
   public static final int TEXT_FIELD = 0;
   public static final int BUTTON = 1;
   public static final int CHECK_BOX = 2;
   public static final int RADIO_BUTTON = 3;
   public static final int MENU_LIST = 4;
   public static final int MENU_LIST_BUTTON = 5;
   public static final int SLIDER = 6;
   public static final int PROGRESS_BAR = 7;
   public static final int METER = 8;
   public static final int CHECKED = 1;
   public static final int INDETERMINATE = 2;
   public static final int ENABLED = 4;
   public static final int FOCUSED = 8;
   public static final int PRESSED = 16;
   public static final int HOVERED = 32;
   public static final int READ_ONLY = 64;
   public static final int BACKGROUND = 0;
   public static final int FOREGROUND = 1;

   protected abstract Ref createWidget(long var1, int var3, int var4, int var5, int var6, int var7, ByteBuffer var8);

   public abstract void drawWidget(WCGraphicsContext var1, Ref var2, int var3, int var4);

   protected abstract int getRadioButtonSize();

   protected abstract int getSelectionColor(int var1);

   public abstract WCSize getWidgetSize(Ref var1);
}
