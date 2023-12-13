package com.sun.glass.ui.win;

import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Window;

class WinMenuImpl {
   private long ptr = 0L;

   private static native void _initIDs();

   long getHMENU() {
      return this.ptr;
   }

   boolean create() {
      this.ptr = this._create();
      return this.ptr != 0L;
   }

   void destroy() {
      if (this.ptr != 0L) {
         this._destroy(this.ptr);
         this.ptr = 0L;
      }

   }

   boolean insertSubmenu(WinMenuDelegate var1, int var2) {
      var1.setParent(this);
      if (!this._insertSubmenu(this.ptr, var2, var1.getHMENU(), var1.getOwner().getTitle(), var1.getOwner().isEnabled())) {
         var1.setParent((WinMenuImpl)null);
         return false;
      } else {
         return true;
      }
   }

   boolean insertItem(WinMenuItemDelegate var1, int var2) {
      if (var1 == null) {
         return this._insertSeparator(this.ptr, var2);
      } else {
         var1.setParent(this);
         if (!this._insertItem(this.ptr, var2, var1.getCmdID(), var1.getOwner().getTitle(), var1.getOwner().isEnabled(), var1.getOwner().isChecked(), var1.getOwner().getCallback(), var1.getOwner().getShortcutKey(), var1.getOwner().getShortcutModifiers())) {
            var1.setParent((WinMenuImpl)null);
            return false;
         } else {
            return true;
         }
      }
   }

   boolean removeMenu(WinMenuDelegate var1, int var2) {
      if (this._removeAtPos(this.ptr, var2)) {
         var1.setParent((WinMenuImpl)null);
         return true;
      } else {
         return false;
      }
   }

   boolean removeItem(WinMenuItemDelegate var1, int var2) {
      if (this._removeAtPos(this.ptr, var2)) {
         if (var1 != null) {
            var1.setParent((WinMenuImpl)null);
         }

         return true;
      } else {
         return false;
      }
   }

   boolean setSubmenuTitle(WinMenuDelegate var1, String var2) {
      return this._setSubmenuTitle(this.ptr, var1.getHMENU(), var2);
   }

   boolean setItemTitle(WinMenuItemDelegate var1, String var2) {
      return this._setItemTitle(this.ptr, var1.getCmdID(), var2);
   }

   boolean enableSubmenu(WinMenuDelegate var1, boolean var2) {
      return this._enableSubmenu(this.ptr, var1.getHMENU(), var2);
   }

   boolean enableItem(WinMenuItemDelegate var1, boolean var2) {
      return this._enableItem(this.ptr, var1.getCmdID(), var2);
   }

   public boolean checkItem(WinMenuItemDelegate var1, boolean var2) {
      return this._checkItem(this.ptr, var1.getCmdID(), var2);
   }

   private static boolean notifyCommand(Window var0, int var1) {
      WinMenuItemDelegate var2 = WinMenuItemDelegate.CommandIDManager.getHandler(var1);
      if (var2 != null) {
         MenuItem.Callback var3 = var2.getOwner().getCallback();
         if (var3 != null) {
            var3.action();
            return true;
         }
      }

      return false;
   }

   private native long _create();

   private native void _destroy(long var1);

   private native boolean _insertItem(long var1, int var3, int var4, String var5, boolean var6, boolean var7, MenuItem.Callback var8, int var9, int var10);

   private native boolean _insertSubmenu(long var1, int var3, long var4, String var6, boolean var7);

   private native boolean _insertSeparator(long var1, int var3);

   private native boolean _removeAtPos(long var1, int var3);

   private native boolean _setItemTitle(long var1, int var3, String var4);

   private native boolean _setSubmenuTitle(long var1, long var3, String var5);

   private native boolean _enableItem(long var1, int var3, boolean var4);

   private native boolean _enableSubmenu(long var1, long var3, boolean var5);

   private native boolean _checkItem(long var1, int var3, boolean var4);

   static {
      _initIDs();
   }
}
