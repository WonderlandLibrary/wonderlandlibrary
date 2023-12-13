package com.sun.glass.ui.delegate;

import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;

public interface MenuItemDelegate {
   boolean createMenuItem(String var1, MenuItem.Callback var2, int var3, int var4, Pixels var5, boolean var6, boolean var7);

   boolean setTitle(String var1);

   boolean setCallback(MenuItem.Callback var1);

   boolean setShortcut(int var1, int var2);

   boolean setPixels(Pixels var1);

   boolean setEnabled(boolean var1);

   boolean setChecked(boolean var1);
}
