package com.sun.glass.ui;

public abstract class Cursor {
   public static final int CURSOR_NONE = -1;
   public static final int CURSOR_CUSTOM = 0;
   public static final int CURSOR_DEFAULT = 1;
   public static final int CURSOR_TEXT = 2;
   public static final int CURSOR_CROSSHAIR = 3;
   public static final int CURSOR_CLOSED_HAND = 4;
   public static final int CURSOR_OPEN_HAND = 5;
   public static final int CURSOR_POINTING_HAND = 6;
   public static final int CURSOR_RESIZE_LEFT = 7;
   public static final int CURSOR_RESIZE_RIGHT = 8;
   public static final int CURSOR_RESIZE_UP = 9;
   public static final int CURSOR_RESIZE_DOWN = 10;
   public static final int CURSOR_RESIZE_LEFTRIGHT = 11;
   public static final int CURSOR_RESIZE_UPDOWN = 12;
   public static final int CURSOR_DISAPPEAR = 13;
   public static final int CURSOR_WAIT = 14;
   public static final int CURSOR_RESIZE_SOUTHWEST = 15;
   public static final int CURSOR_RESIZE_SOUTHEAST = 16;
   public static final int CURSOR_RESIZE_NORTHWEST = 17;
   public static final int CURSOR_RESIZE_NORTHEAST = 18;
   public static final int CURSOR_MOVE = 19;
   private final int type;
   private long ptr;

   protected Cursor(int var1) {
      Application.checkEventThread();
      this.type = var1;
   }

   protected Cursor(int var1, int var2, Pixels var3) {
      this(0);
      this.ptr = this._createCursor(var1, var2, var3);
   }

   public final int getType() {
      Application.checkEventThread();
      return this.type;
   }

   protected final long getNativeCursor() {
      Application.checkEventThread();
      return this.ptr;
   }

   public static void setVisible(boolean var0) {
      Application.checkEventThread();
      Application.GetApplication().staticCursor_setVisible(var0);
   }

   public static Size getBestSize(int var0, int var1) {
      Application.checkEventThread();
      return Application.GetApplication().staticCursor_getBestSize(var0, var1);
   }

   protected abstract long _createCursor(int var1, int var2, Pixels var3);
}
