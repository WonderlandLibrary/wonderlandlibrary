package com.sun.webkit;

import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;

public interface WebPageClient {
   void setCursor(long var1);

   void setFocus(boolean var1);

   void transferFocus(boolean var1);

   void setTooltip(String var1);

   WCRectangle getScreenBounds(boolean var1);

   int getScreenDepth();

   Object getContainer();

   WCPoint screenToWindow(WCPoint var1);

   WCPoint windowToScreen(WCPoint var1);

   WCPageBackBuffer createBackBuffer();

   boolean isBackBufferSupported();

   void addMessageToConsole(String var1, int var2, String var3);

   void didClearWindowObject(long var1, long var3);
}
