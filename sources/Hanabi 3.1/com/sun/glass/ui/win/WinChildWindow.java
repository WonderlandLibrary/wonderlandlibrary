package com.sun.glass.ui.win;

import com.sun.glass.ui.Pixels;

final class WinChildWindow extends WinWindow {
   protected WinChildWindow(long var1) {
      super(var1);
   }

   protected long _createWindow(long var1, long var3, int var5) {
      return 0L;
   }

   protected boolean _setMenubar(long var1, long var3) {
      return false;
   }

   protected boolean _minimize(long var1, boolean var3) {
      return false;
   }

   protected boolean _maximize(long var1, boolean var3, boolean var4) {
      return false;
   }

   protected boolean _setResizable(long var1, boolean var3) {
      return false;
   }

   protected boolean _setTitle(long var1, String var3) {
      return false;
   }

   protected void _setLevel(long var1, int var3) {
   }

   protected void _setAlpha(long var1, float var3) {
   }

   protected boolean _setMinimumSize(long var1, int var3, int var4) {
      return false;
   }

   protected boolean _setMaximumSize(long var1, int var3, int var4) {
      return false;
   }

   protected void _setIcon(long var1, Pixels var3) {
   }

   protected void _enterModal(long var1) {
   }

   protected void _enterModalWithWindow(long var1, long var3) {
   }

   protected void _exitModal(long var1) {
   }
}
