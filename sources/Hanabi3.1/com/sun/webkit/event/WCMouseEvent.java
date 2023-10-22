package com.sun.webkit.event;

public final class WCMouseEvent {
   public static final int MOUSE_PRESSED = 0;
   public static final int MOUSE_RELEASED = 1;
   public static final int MOUSE_MOVED = 2;
   public static final int MOUSE_DRAGGED = 3;
   public static final int MOUSE_WHEEL = 4;
   public static final int NOBUTTON = 0;
   public static final int BUTTON1 = 1;
   public static final int BUTTON2 = 2;
   public static final int BUTTON3 = 4;
   private final int id;
   private final long when;
   private final int button;
   private final int clickCount;
   private final int x;
   private final int y;
   private final int screenX;
   private final int screenY;
   private final boolean shift;
   private final boolean control;
   private final boolean alt;
   private final boolean meta;
   private final boolean popupTrigger;

   public WCMouseEvent(int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8, boolean var10, boolean var11, boolean var12, boolean var13, boolean var14) {
      this.id = var1;
      this.button = var2;
      this.clickCount = var3;
      this.x = var4;
      this.y = var5;
      this.screenX = var6;
      this.screenY = var7;
      this.when = var8;
      this.shift = var10;
      this.control = var11;
      this.alt = var12;
      this.meta = var13;
      this.popupTrigger = var14;
   }

   public int getID() {
      return this.id;
   }

   public long getWhen() {
      return this.when;
   }

   public int getButton() {
      return this.button;
   }

   public int getClickCount() {
      return this.clickCount;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getScreenX() {
      return this.screenX;
   }

   public int getScreenY() {
      return this.screenY;
   }

   public boolean isShiftDown() {
      return this.shift;
   }

   public boolean isControlDown() {
      return this.control;
   }

   public boolean isAltDown() {
      return this.alt;
   }

   public boolean isMetaDown() {
      return this.meta;
   }

   public boolean isPopupTrigger() {
      return this.popupTrigger;
   }
}
