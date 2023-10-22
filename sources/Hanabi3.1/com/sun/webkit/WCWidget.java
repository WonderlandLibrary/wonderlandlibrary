package com.sun.webkit;

import com.sun.webkit.graphics.WCRectangle;
import java.util.logging.Level;
import java.util.logging.Logger;

class WCWidget {
   private static final Logger log = Logger.getLogger(WCWidget.class.getName());
   private int x;
   private int y;
   private int width;
   private int height;
   private final WebPage page;

   WCWidget(WebPage var1) {
      this.page = var1;
   }

   WebPage getPage() {
      return this.page;
   }

   WCRectangle getBounds() {
      return new WCRectangle((float)this.x, (float)this.y, (float)this.width, (float)this.height);
   }

   void setBounds(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.width = var3;
      this.height = var4;
   }

   protected void destroy() {
   }

   protected void requestFocus() {
   }

   protected void setCursor(long var1) {
   }

   protected void setVisible(boolean var1) {
   }

   private void fwkDestroy() {
      log.log(Level.FINER, "destroy");
      this.destroy();
   }

   private void fwkSetBounds(int var1, int var2, int var3, int var4) {
      if (log.isLoggable(Level.FINER)) {
         log.log(Level.FINER, "setBounds({0}, {1}, {2}, {3})", new Object[]{var1, var2, var3, var4});
      }

      this.setBounds(var1, var2, var3, var4);
   }

   private void fwkRequestFocus() {
      log.log(Level.FINER, "requestFocus");
      this.requestFocus();
   }

   private void fwkSetCursor(long var1) {
      if (log.isLoggable(Level.FINER)) {
         log.log(Level.FINER, "setCursor({0})", var1);
      }

      this.setCursor(var1);
   }

   private void fwkSetVisible(boolean var1) {
      if (log.isLoggable(Level.FINER)) {
         log.log(Level.FINER, "setVisible({0})", var1);
      }

      this.setVisible(var1);
   }

   protected int fwkGetScreenDepth() {
      log.log(Level.FINER, "getScreenDepth");
      WebPageClient var1 = this.page.getPageClient();
      return var1 != null ? var1.getScreenDepth() : 24;
   }

   protected WCRectangle fwkGetScreenRect(boolean var1) {
      if (log.isLoggable(Level.FINER)) {
         log.log(Level.FINER, "getScreenRect({0})", var1);
      }

      WebPageClient var2 = this.page.getPageClient();
      return var2 != null ? var2.getScreenBounds(var1) : null;
   }

   private static native void initIDs();

   static {
      initIDs();
   }
}
