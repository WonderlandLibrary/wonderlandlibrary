package com.sun.webkit;

final class WCFrameView extends WCWidget {
   WCFrameView(WebPage var1) {
      super(var1);
   }

   protected void requestFocus() {
      WebPageClient var1 = this.getPage().getPageClient();
      if (var1 != null) {
         var1.setFocus(true);
      }

   }

   protected void setCursor(long var1) {
      WebPageClient var3 = this.getPage().getPageClient();
      if (var3 != null) {
         var3.setCursor(var1);
      }

   }
}
