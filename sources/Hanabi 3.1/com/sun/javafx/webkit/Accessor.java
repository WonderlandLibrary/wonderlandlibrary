package com.sun.javafx.webkit;

import com.sun.webkit.WebPage;
import javafx.beans.InvalidationListener;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public abstract class Accessor {
   private static PageAccessor pageAccessor;

   public static void setPageAccessor(PageAccessor var0) {
      pageAccessor = var0;
   }

   public static WebPage getPageFor(WebEngine var0) {
      return pageAccessor.getPage(var0);
   }

   public abstract WebEngine getEngine();

   public abstract WebView getView();

   public abstract WebPage getPage();

   public abstract void addChild(Node var1);

   public abstract void removeChild(Node var1);

   public abstract void addViewListener(InvalidationListener var1);

   public interface PageAccessor {
      WebPage getPage(WebEngine var1);
   }
}
