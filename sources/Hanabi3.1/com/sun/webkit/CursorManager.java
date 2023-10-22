package com.sun.webkit;

import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCImageFrame;
import java.util.HashMap;
import java.util.Map;

public abstract class CursorManager {
   public static final int POINTER = 0;
   public static final int CROSS = 1;
   public static final int HAND = 2;
   public static final int MOVE = 3;
   public static final int TEXT = 4;
   public static final int WAIT = 5;
   public static final int HELP = 6;
   public static final int EAST_RESIZE = 7;
   public static final int NORTH_RESIZE = 8;
   public static final int NORTH_EAST_RESIZE = 9;
   public static final int NORTH_WEST_RESIZE = 10;
   public static final int SOUTH_RESIZE = 11;
   public static final int SOUTH_EAST_RESIZE = 12;
   public static final int SOUTH_WEST_RESIZE = 13;
   public static final int WEST_RESIZE = 14;
   public static final int NORTH_SOUTH_RESIZE = 15;
   public static final int EAST_WEST_RESIZE = 16;
   public static final int NORTH_EAST_SOUTH_WEST_RESIZE = 17;
   public static final int NORTH_WEST_SOUTH_EAST_RESIZE = 18;
   public static final int COLUMN_RESIZE = 19;
   public static final int ROW_RESIZE = 20;
   public static final int MIDDLE_PANNING = 21;
   public static final int EAST_PANNING = 22;
   public static final int NORTH_PANNING = 23;
   public static final int NORTH_EAST_PANNING = 24;
   public static final int NORTH_WEST_PANNING = 25;
   public static final int SOUTH_PANNING = 26;
   public static final int SOUTH_EAST_PANNING = 27;
   public static final int SOUTH_WEST_PANNING = 28;
   public static final int WEST_PANNING = 29;
   public static final int VERTICAL_TEXT = 30;
   public static final int CELL = 31;
   public static final int CONTEXT_MENU = 32;
   public static final int NO_DROP = 33;
   public static final int NOT_ALLOWED = 34;
   public static final int PROGRESS = 35;
   public static final int ALIAS = 36;
   public static final int ZOOM_IN = 37;
   public static final int ZOOM_OUT = 38;
   public static final int COPY = 39;
   public static final int NONE = 40;
   public static final int GRAB = 41;
   public static final int GRABBING = 42;
   private static CursorManager instance;
   private final Map map = new HashMap();

   public static void setCursorManager(CursorManager var0) {
      instance = var0;
   }

   public static CursorManager getCursorManager() {
      return instance;
   }

   protected abstract Object getCustomCursor(WCImage var1, int var2, int var3);

   protected abstract Object getPredefinedCursor(int var1);

   private long getCustomCursorID(WCImageFrame var1, int var2, int var3) {
      return this.putCursor(this.getCustomCursor(var1.getFrame(), var2, var3));
   }

   private long getPredefinedCursorID(int var1) {
      return this.putCursor(this.getPredefinedCursor(var1));
   }

   public final Object getCursor(long var1) {
      return this.map.get(var1);
   }

   private long putCursor(Object var1) {
      long var2 = (long)var1.hashCode();
      this.map.put(var2, var1);
      return var2;
   }
}
