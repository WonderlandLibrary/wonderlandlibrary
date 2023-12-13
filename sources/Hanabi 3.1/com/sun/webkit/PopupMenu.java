package com.sun.webkit;

import com.sun.webkit.graphics.WCFont;

public abstract class PopupMenu {
   private long pdata;

   protected abstract void show(WebPage var1, int var2, int var3, int var4);

   protected abstract void hide();

   protected abstract void setSelectedItem(int var1);

   protected abstract void appendItem(String var1, boolean var2, boolean var3, boolean var4, int var5, int var6, WCFont var7);

   protected void notifySelectionCommited(int var1) {
      this.twkSelectionCommited(this.pdata, var1);
   }

   protected void notifyPopupClosed() {
      this.twkPopupClosed(this.pdata);
   }

   private static PopupMenu fwkCreatePopupMenu(long var0) {
      PopupMenu var2 = Utilities.getUtilities().createPopupMenu();
      var2.pdata = var0;
      return var2;
   }

   private void fwkShow(WebPage var1, int var2, int var3, int var4) {
      assert var1 != null;

      this.show(var1, var2, var3, var4);
   }

   private void fwkHide() {
      this.hide();
   }

   private void fwkSetSelectedItem(int var1) {
      this.setSelectedItem(var1);
   }

   private void fwkAppendItem(String var1, boolean var2, boolean var3, boolean var4, int var5, int var6, WCFont var7) {
      this.appendItem(var1, var2, var3, var4, var5, var6, var7);
   }

   private void fwkDestroy() {
      this.pdata = 0L;
   }

   private native void twkSelectionCommited(long var1, int var3);

   private native void twkPopupClosed(long var1);
}
