package com.sun.glass.ui;

import com.sun.glass.ui.delegate.MenuBarDelegate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MenuBar {
   private final MenuBarDelegate delegate;
   private final List menus = new ArrayList();

   protected MenuBar() {
      Application.checkEventThread();
      this.delegate = PlatformFactory.getPlatformFactory().createMenuBarDelegate(this);
      if (!this.delegate.createMenuBar()) {
         throw new RuntimeException("MenuBar creation error.");
      }
   }

   long getNativeMenu() {
      return this.delegate.getNativeMenu();
   }

   public void add(Menu var1) {
      Application.checkEventThread();
      this.insert(var1, this.menus.size());
   }

   public void insert(Menu var1, int var2) {
      Application.checkEventThread();
      synchronized(this.menus) {
         if (this.delegate.insert(var1.getDelegate(), var2)) {
            this.menus.add(var2, var1);
         }

      }
   }

   public void remove(int var1) {
      Application.checkEventThread();
      synchronized(this.menus) {
         Menu var3 = (Menu)this.menus.get(var1);
         if (this.delegate.remove(var3.getDelegate(), var1)) {
            this.menus.remove(var1);
         }

      }
   }

   public void remove(Menu var1) {
      Application.checkEventThread();
      synchronized(this.menus) {
         int var3 = this.menus.indexOf(var1);
         if (var3 >= 0 && this.delegate.remove(var1.getDelegate(), var3)) {
            this.menus.remove(var3);
         }

      }
   }

   public List getMenus() {
      Application.checkEventThread();
      return Collections.unmodifiableList(this.menus);
   }
}
