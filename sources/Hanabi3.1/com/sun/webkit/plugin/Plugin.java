package com.sun.webkit.plugin;

import com.sun.webkit.graphics.WCGraphicsContext;
import java.io.IOException;

public interface Plugin {
   int EVENT_BEFOREACTIVATE = -4;
   int EVENT_FOCUSCHANGE = -1;

   void requestFocus();

   void setNativeContainerBounds(int var1, int var2, int var3, int var4);

   void activate(Object var1, PluginListener var2);

   void destroy();

   void setVisible(boolean var1);

   void setEnabled(boolean var1);

   void setBounds(int var1, int var2, int var3, int var4);

   Object invoke(String var1, String var2, Object[] var3) throws IOException;

   void paint(WCGraphicsContext var1, int var2, int var3, int var4, int var5);

   boolean handleMouseEvent(String var1, int var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8, boolean var9, boolean var10, boolean var11, long var12);
}
