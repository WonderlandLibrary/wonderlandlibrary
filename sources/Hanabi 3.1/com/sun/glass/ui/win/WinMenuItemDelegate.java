package com.sun.glass.ui.win;

import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.delegate.MenuItemDelegate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class WinMenuItemDelegate implements MenuItemDelegate {
   private final MenuItem owner;
   private WinMenuImpl parent = null;
   private int cmdID = -1;

   public WinMenuItemDelegate(MenuItem var1) {
      this.owner = var1;
   }

   public MenuItem getOwner() {
      return this.owner;
   }

   public boolean createMenuItem(String var1, MenuItem.Callback var2, int var3, int var4, Pixels var5, boolean var6, boolean var7) {
      return true;
   }

   public boolean setTitle(String var1) {
      if (this.parent != null) {
         var1 = this.getTitle(var1, this.getOwner().getShortcutKey(), this.getOwner().getShortcutModifiers());
         return this.parent.setItemTitle(this, var1);
      } else {
         return true;
      }
   }

   public boolean setCallback(MenuItem.Callback var1) {
      return true;
   }

   public boolean setShortcut(int var1, int var2) {
      if (this.parent != null) {
         String var3 = this.getTitle(this.getOwner().getTitle(), var1, var2);
         return this.parent.setItemTitle(this, var3);
      } else {
         return true;
      }
   }

   public boolean setPixels(Pixels var1) {
      return false;
   }

   public boolean setEnabled(boolean var1) {
      return this.parent != null ? this.parent.enableItem(this, var1) : true;
   }

   public boolean setChecked(boolean var1) {
      return this.parent != null ? this.parent.checkItem(this, var1) : true;
   }

   private String getTitle(String var1, int var2, int var3) {
      return var2 == 0 ? var1 : var1;
   }

   WinMenuImpl getParent() {
      return this.parent;
   }

   void setParent(WinMenuImpl var1) {
      if (this.parent != null) {
         WinMenuItemDelegate.CommandIDManager.freeID(this.cmdID);
         this.cmdID = -1;
      }

      if (var1 != null) {
         this.cmdID = WinMenuItemDelegate.CommandIDManager.getID(this);
      }

      this.parent = var1;
   }

   int getCmdID() {
      return this.cmdID;
   }

   static class CommandIDManager {
      private static final int FIRST_ID = 1;
      private static final int LAST_ID = 65535;
      private static List freeList = new ArrayList();
      private static final Map map = new HashMap();
      private static int nextID = 1;

      public static synchronized int getID(WinMenuItemDelegate var0) {
         Integer var1;
         if (freeList.isEmpty()) {
            if (nextID > 65535) {
               nextID = 1;
            }

            var1 = nextID;
            ++nextID;
         } else {
            var1 = (Integer)freeList.remove(freeList.size() - 1);
         }

         map.put(var1, var0);
         return var1;
      }

      public static synchronized void freeID(int var0) {
         Integer var1 = var0;
         if (map.remove(var1) != null) {
            freeList.add(var1);
         }

      }

      public static WinMenuItemDelegate getHandler(int var0) {
         return (WinMenuItemDelegate)map.get(var0);
      }
   }
}
