package com.sun.glass.ui.delegate;

public interface MenuBarDelegate {
   boolean createMenuBar();

   boolean insert(MenuDelegate var1, int var2);

   boolean remove(MenuDelegate var1, int var2);

   long getNativeMenu();
}
