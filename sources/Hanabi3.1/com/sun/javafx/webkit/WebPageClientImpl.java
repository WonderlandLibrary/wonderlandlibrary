package com.sun.javafx.webkit;

import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.util.Utils;
import com.sun.webkit.CursorManager;
import com.sun.webkit.WebPageClient;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
import java.security.AccessController;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Window;

public final class WebPageClientImpl implements WebPageClient {
   private static final boolean backBufferSupported = Boolean.valueOf((String)AccessController.doPrivileged(() -> {
      return System.getProperty("com.sun.webkit.pagebackbuffer", "true");
   }));
   private static WebConsoleListener consoleListener = null;
   private final Accessor accessor;
   private Tooltip tooltip;
   private boolean isTooltipRegistered = false;
   private String oldTooltipText = "";

   static void setConsoleListener(WebConsoleListener var0) {
      consoleListener = var0;
   }

   public WebPageClientImpl(Accessor var1) {
      this.accessor = var1;
   }

   public void setFocus(boolean var1) {
      WebView var2 = this.accessor.getView();
      if (var2 != null && var1) {
         var2.requestFocus();
      }

   }

   public void setCursor(long var1) {
      WebView var3 = this.accessor.getView();
      if (var3 != null) {
         Object var4 = CursorManager.getCursorManager().getCursor(var1);
         var3.setCursor(var4 instanceof Cursor ? (Cursor)var4 : Cursor.DEFAULT);
      }

   }

   public void setTooltip(String var1) {
      WebView var2 = this.accessor.getView();
      if (var1 != null) {
         if (this.tooltip == null) {
            this.tooltip = new Tooltip(var1);
         } else {
            this.tooltip.setText(var1);
            if (!this.oldTooltipText.equals(var1)) {
               Tooltip.uninstall(var2, this.tooltip);
               this.isTooltipRegistered = false;
            }
         }

         this.oldTooltipText = var1;
         if (!this.isTooltipRegistered) {
            Tooltip.install(var2, this.tooltip);
            this.isTooltipRegistered = true;
         }
      } else if (this.isTooltipRegistered) {
         Tooltip.uninstall(var2, this.tooltip);
         this.isTooltipRegistered = false;
      }

   }

   public void transferFocus(boolean var1) {
      this.accessor.getView().impl_traverse(var1 ? Direction.NEXT : Direction.PREVIOUS);
   }

   public WCRectangle getScreenBounds(boolean var1) {
      WebView var2 = this.accessor.getView();
      Screen var3 = Utils.getScreen(var2);
      if (var3 != null) {
         Rectangle2D var4 = var1 ? var3.getVisualBounds() : var3.getBounds();
         return new WCRectangle((float)var4.getMinX(), (float)var4.getMinY(), (float)var4.getWidth(), (float)var4.getHeight());
      } else {
         return null;
      }
   }

   public int getScreenDepth() {
      return 24;
   }

   public WebView getContainer() {
      return this.accessor.getView();
   }

   public WCPoint screenToWindow(WCPoint var1) {
      WebView var2 = this.accessor.getView();
      Scene var3 = var2.getScene();
      Window var4 = null;
      if (var3 != null && (var4 = var3.getWindow()) != null) {
         Point2D var5 = var2.sceneToLocal((double)var1.getX() - var4.getX() - var3.getX(), (double)var1.getY() - var4.getY() - var3.getY());
         return new WCPoint((float)var5.getX(), (float)var5.getY());
      } else {
         return new WCPoint(0.0F, 0.0F);
      }
   }

   public WCPoint windowToScreen(WCPoint var1) {
      WebView var2 = this.accessor.getView();
      Scene var3 = var2.getScene();
      Window var4 = null;
      if (var3 != null && (var4 = var3.getWindow()) != null) {
         Point2D var5 = var2.localToScene((double)var1.getX(), (double)var1.getY());
         return new WCPoint((float)(var5.getX() + var3.getX() + var4.getX()), (float)(var5.getY() + var3.getY() + var4.getY()));
      } else {
         return new WCPoint(0.0F, 0.0F);
      }
   }

   public WCPageBackBuffer createBackBuffer() {
      return this.isBackBufferSupported() ? WCGraphicsManager.getGraphicsManager().createPageBackBuffer() : null;
   }

   public boolean isBackBufferSupported() {
      return backBufferSupported;
   }

   public void addMessageToConsole(String var1, int var2, String var3) {
      if (consoleListener != null) {
         try {
            consoleListener.messageAdded(this.accessor.getView(), var1, var2, var3);
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

   }

   public void didClearWindowObject(long var1, long var3) {
   }
}
