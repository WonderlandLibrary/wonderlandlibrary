package com.sun.webkit.event;

public final class WCKeyEvent {
   public static final int KEY_TYPED = 0;
   public static final int KEY_PRESSED = 1;
   public static final int KEY_RELEASED = 2;
   public static final int VK_BACK = 8;
   public static final int VK_TAB = 9;
   public static final int VK_RETURN = 13;
   public static final int VK_ESCAPE = 27;
   public static final int VK_PRIOR = 33;
   public static final int VK_NEXT = 34;
   public static final int VK_END = 35;
   public static final int VK_HOME = 36;
   public static final int VK_LEFT = 37;
   public static final int VK_UP = 38;
   public static final int VK_RIGHT = 39;
   public static final int VK_DOWN = 40;
   public static final int VK_INSERT = 45;
   public static final int VK_DELETE = 46;
   public static final int VK_OEM_PERIOD = 190;
   private final int type;
   private final long when;
   private final String text;
   private final String keyIdentifier;
   private final int windowsVirtualKeyCode;
   private final boolean shift;
   private final boolean ctrl;
   private final boolean alt;
   private final boolean meta;

   public WCKeyEvent(int var1, String var2, String var3, int var4, boolean var5, boolean var6, boolean var7, boolean var8, long var9) {
      this.type = var1;
      this.text = var2;
      this.keyIdentifier = var3;
      this.windowsVirtualKeyCode = var4;
      this.shift = var5;
      this.ctrl = var6;
      this.alt = var7;
      this.meta = var8;
      this.when = var9;
   }

   public int getType() {
      return this.type;
   }

   public long getWhen() {
      return this.when;
   }

   public String getText() {
      return this.text;
   }

   public String getKeyIdentifier() {
      return this.keyIdentifier;
   }

   public int getWindowsVirtualKeyCode() {
      return this.windowsVirtualKeyCode;
   }

   public boolean isShiftDown() {
      return this.shift;
   }

   public boolean isCtrlDown() {
      return this.ctrl;
   }

   public boolean isAltDown() {
      return this.alt;
   }

   public boolean isMetaDown() {
      return this.meta;
   }

   public static boolean filterEvent(WCKeyEvent var0) {
      if (var0.getType() == 0) {
         String var1 = var0.getText();
         if (var1 == null || var1.length() != 1) {
            return true;
         }

         char var2 = var1.charAt(0);
         if (var2 == '\b' || var2 == '\n' || var2 == '\t' || var2 == '\uffff' || var2 == 24 || var2 == 27 || var2 == 127) {
            return true;
         }
      }

      return false;
   }
}
