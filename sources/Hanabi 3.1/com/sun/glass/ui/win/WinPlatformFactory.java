package com.sun.glass.ui.win;

import com.sun.glass.ui.Menu;
import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.PlatformFactory;
import com.sun.glass.ui.delegate.ClipboardDelegate;
import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;

public final class WinPlatformFactory extends PlatformFactory {
   public WinApplication createApplication() {
      return new WinApplication();
   }

   public MenuBarDelegate createMenuBarDelegate(MenuBar var1) {
      return new WinMenuBarDelegate(var1);
   }

   public MenuDelegate createMenuDelegate(Menu var1) {
      return new WinMenuDelegate(var1);
   }

   public MenuItemDelegate createMenuItemDelegate(MenuItem var1) {
      return new WinMenuItemDelegate(var1);
   }

   public ClipboardDelegate createClipboardDelegate() {
      return new WinClipboardDelegate();
   }
}
