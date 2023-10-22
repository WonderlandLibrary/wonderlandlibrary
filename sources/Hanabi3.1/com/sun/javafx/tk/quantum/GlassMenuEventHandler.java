package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Menu;
import com.sun.javafx.menu.MenuBase;

class GlassMenuEventHandler extends Menu.EventHandler {
   private MenuBase menuBase;
   private boolean menuOpen;

   public GlassMenuEventHandler(MenuBase var1) {
      this.menuBase = var1;
   }

   public void handleMenuOpening(Menu var1, long var2) {
      this.menuBase.show();
      this.menuOpen = true;
   }

   public void handleMenuClosed(Menu var1, long var2) {
      this.menuBase.hide();
      this.menuOpen = false;
   }

   protected boolean isMenuOpen() {
      return this.menuOpen;
   }
}
