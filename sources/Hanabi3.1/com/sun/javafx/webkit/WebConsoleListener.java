package com.sun.javafx.webkit;

import javafx.scene.web.WebView;

public interface WebConsoleListener {
   static void setDefaultListener(WebConsoleListener var0) {
      WebPageClientImpl.setConsoleListener(var0);
   }

   void messageAdded(WebView var1, String var2, int var3, String var4);
}
