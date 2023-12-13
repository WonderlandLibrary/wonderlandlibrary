package com.sun.webkit;

public abstract class ContextMenu {
   protected abstract void show(ShowContext var1, int var2, int var3);

   protected abstract void appendItem(ContextMenuItem var1);

   protected abstract void insertItem(ContextMenuItem var1, int var2);

   protected abstract int getItemCount();

   private static ContextMenu fwkCreateContextMenu() {
      return Utilities.getUtilities().createContextMenu();
   }

   private void fwkShow(WebPage var1, long var2, int var4, int var5) {
      this.show(new ShowContext(var1, var2), var4, var5);
   }

   private void fwkAppendItem(ContextMenuItem var1) {
      this.appendItem(var1);
   }

   private void fwkInsertItem(ContextMenuItem var1, int var2) {
      this.insertItem(var1, var2);
   }

   private int fwkGetItemCount() {
      return this.getItemCount();
   }

   private native void twkHandleItemSelected(long var1, int var3);

   public final class ShowContext {
      private final WebPage page;
      private final long pdata;

      private ShowContext(WebPage var2, long var3) {
         this.page = var2;
         this.pdata = var3;
      }

      public WebPage getPage() {
         return this.page;
      }

      public void notifyItemSelected(int var1) {
         ContextMenu.this.twkHandleItemSelected(this.pdata, var1);
      }

      // $FF: synthetic method
      ShowContext(WebPage var2, long var3, Object var5) {
         this(var2, var3);
      }
   }
}
