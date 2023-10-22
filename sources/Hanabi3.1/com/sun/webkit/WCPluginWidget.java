package com.sun.webkit;

import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.network.URLs;
import com.sun.webkit.plugin.Plugin;
import com.sun.webkit.plugin.PluginListener;
import com.sun.webkit.plugin.PluginManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

final class WCPluginWidget extends WCWidget implements PluginListener {
   private static final Logger log = Logger.getLogger(WCPluginWidget.class.getName());
   private final Plugin plugin;
   private long pData = 0L;

   private static native void initIDs();

   private WCPluginWidget(WebPage var1, Plugin var2, int var3, int var4) {
      super(var1);
      this.plugin = var2;
      this.setBounds(0, 0, var3, var4);
      WebPageClient var5 = var1.getPageClient();
      this.plugin.activate(null == var5 ? null : var5.getContainer(), this);
   }

   protected void requestFocus() {
      this.plugin.requestFocus();
   }

   private static WCPluginWidget create(WebPage var0, int var1, int var2, String var3, String var4, String[] var5, String[] var6) {
      URL var7 = null;

      try {
         var7 = URLs.newURL(var3);
      } catch (MalformedURLException var9) {
         log.log(Level.FINE, (String)null, var9);
      }

      return new WCPluginWidget(var0, PluginManager.createPlugin(var7, var4, var5, var6), var1, var2);
   }

   private void fwkSetNativeContainerBounds(int var1, int var2, int var3, int var4) {
      this.plugin.setNativeContainerBounds(var1, var2, var3, var4);
   }

   void setBounds(int var1, int var2, int var3, int var4) {
      super.setBounds(var1, var2, var3, var4);
      this.plugin.setBounds(var1, var2, var3, var4);
   }

   private void setEnabled(boolean var1) {
      this.plugin.setEnabled(var1);
   }

   protected void setVisible(boolean var1) {
      this.plugin.setVisible(var1);
   }

   protected void destroy() {
      this.pData = 0L;
      this.plugin.destroy();
   }

   private void paint(WCGraphicsContext var1, int var2, int var3, int var4, int var5) {
      WCRectangle var6 = this.getBounds();
      WCRectangle var7 = var6.intersection(new WCRectangle((float)var2, (float)var3, (float)var4, (float)var5));
      if (!var7.isEmpty()) {
         var1.translate(var6.getX(), var6.getY());
         var7.translate(-var6.getX(), -var6.getY());
         var1.setClip(var7.getIntX(), var7.getIntY(), var7.getIntWidth(), var7.getIntHeight());
         this.plugin.paint(var1, var7.getIntX(), var7.getIntY(), var7.getIntWidth(), var7.getIntHeight());
      }

   }

   private native WCRectangle twkConvertToPage(WCRectangle var1);

   private native void twkInvalidateWindowlessPluginRect(int var1, int var2, int var3, int var4);

   private boolean fwkHandleMouseEvent(String var1, int var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8, boolean var9, boolean var10, boolean var11, long var12) {
      return this.plugin.handleMouseEvent(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   public void fwkRedraw(int var1, int var2, int var3, int var4, boolean var5) {
      this.twkInvalidateWindowlessPluginRect(var1, var2, var3, var4);
   }

   private native void twkSetPlugunFocused(boolean var1);

   public String fwkEvent(int var1, String var2, String var3) {
      if (-1 == var1 && Boolean.parseBoolean(var3)) {
         this.twkSetPlugunFocused(Boolean.valueOf(var3));
      }

      return "";
   }

   static {
      initIDs();
   }
}
