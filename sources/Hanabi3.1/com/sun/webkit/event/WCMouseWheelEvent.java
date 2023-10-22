package com.sun.webkit.event;

public final class WCMouseWheelEvent {
   private final long when;
   private final int x;
   private final int y;
   private final int screenX;
   private final int screenY;
   private final float deltaX;
   private final float deltaY;
   private final boolean shift;
   private final boolean control;
   private final boolean alt;
   private final boolean meta;

   public WCMouseWheelEvent(int var1, int var2, int var3, int var4, long var5, boolean var7, boolean var8, boolean var9, boolean var10, float var11, float var12) {
      this.x = var1;
      this.y = var2;
      this.screenX = var3;
      this.screenY = var4;
      this.when = var5;
      this.shift = var7;
      this.control = var8;
      this.alt = var9;
      this.meta = var10;
      this.deltaX = var11;
      this.deltaY = var12;
   }

   public long getWhen() {
      return this.when;
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

   public float getDeltaX() {
      return this.deltaX;
   }

   public float getDeltaY() {
      return this.deltaY;
   }
}
