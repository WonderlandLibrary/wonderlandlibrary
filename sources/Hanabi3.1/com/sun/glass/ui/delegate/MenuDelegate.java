package com.sun.glass.ui.delegate;

import com.sun.glass.ui.Pixels;

public interface MenuDelegate {
   boolean createMenu(String var1, boolean var2);

   boolean setTitle(String var1);

   boolean setEnabled(boolean var1);

   boolean setPixels(Pixels var1);

   boolean insert(MenuDelegate var1, int var2);

   boolean insert(MenuItemDelegate var1, int var2);

   boolean remove(MenuDelegate var1, int var2);

   boolean remove(MenuItemDelegate var1, int var2);
}
