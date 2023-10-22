package com.sun.webkit;

import com.sun.webkit.graphics.WCImageFrame;

public interface Pasteboard {
   String getPlainText();

   String getHtml();

   void writePlainText(String var1);

   void writeSelection(boolean var1, String var2, String var3);

   void writeImage(WCImageFrame var1);

   void writeUrl(String var1, String var2);
}
