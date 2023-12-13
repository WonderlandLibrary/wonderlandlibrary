package com.sun.webkit;

import com.sun.webkit.graphics.WCImageFrame;
import java.util.logging.Level;
import java.util.logging.Logger;

final class WCPasteboard {
   private static final Logger log = Logger.getLogger(WCPasteboard.class.getName());
   private static final Pasteboard pasteboard = Utilities.getUtilities().createPasteboard();

   private WCPasteboard() {
   }

   private static String getPlainText() {
      log.fine("getPlainText()");
      return pasteboard.getPlainText();
   }

   private static String getHtml() {
      log.fine("getHtml()");
      return pasteboard.getHtml();
   }

   private static void writePlainText(String var0) {
      log.log(Level.FINE, "writePlainText(): text = {0}", new Object[]{var0});
      pasteboard.writePlainText(var0);
   }

   private static void writeSelection(boolean var0, String var1, String var2) {
      log.log(Level.FINE, "writeSelection(): canSmartCopyOrDelete = {0},\n text = \n{1}\n html=\n{2}", new Object[]{var0, var1, var2});
      pasteboard.writeSelection(var0, var1, var2);
   }

   private static void writeImage(WCImageFrame var0) {
      log.log(Level.FINE, "writeImage(): img = {0}", new Object[]{var0});
      pasteboard.writeImage(var0);
   }

   private static void writeUrl(String var0, String var1) {
      log.log(Level.FINE, "writeUrl(): url = {0}, markup = {1}", new Object[]{var0, var1});
      pasteboard.writeUrl(var0, var1);
   }
}
