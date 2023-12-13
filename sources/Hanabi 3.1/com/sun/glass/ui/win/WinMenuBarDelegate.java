package com.sun.glass.ui.win;

import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;

final class WinMenuBarDelegate extends WinMenuImpl implements MenuBarDelegate {
   private final MenuBar owner;

   WinMenuBarDelegate(MenuBar var1) {
      this.owner = var1;
   }

   public MenuBar getOwner() {
      return this.owner;
   }

   public boolean createMenuBar() {
      return this.create();
   }

   public boolean insert(MenuDelegate var1, int var2) {
      WinMenuDelegate var3 = (WinMenuDelegate)var1;
      if (var3.getParent() != null) {
      }

      return this.insertSubmenu(var3, var2);
   }

   public boolean remove(MenuDelegate var1, int var2) {
      WinMenuDelegate var3 = (WinMenuDelegate)var1;
      return this.removeMenu(var3, var2);
   }

   public long getNativeMenu() {
      return this.getHMENU();
   }
}
