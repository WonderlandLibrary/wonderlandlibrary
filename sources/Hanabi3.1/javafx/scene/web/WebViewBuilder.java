package javafx.scene.web;

import javafx.event.EventHandler;
import javafx.scene.ParentBuilder;
import javafx.util.Builder;
import javafx.util.Callback;

/** @deprecated */
@Deprecated
public final class WebViewBuilder extends ParentBuilder implements Builder {
   private double fontScale;
   private boolean fontScaleSet;
   private double maxHeight;
   private boolean maxHeightSet;
   private double maxWidth;
   private boolean maxWidthSet;
   private double minHeight;
   private boolean minHeightSet;
   private double minWidth;
   private boolean minWidthSet;
   private double prefHeight;
   private boolean prefHeightSet;
   private double prefWidth;
   private boolean prefWidthSet;
   private WebEngineBuilder engineBuilder;

   public static WebViewBuilder create() {
      return new WebViewBuilder();
   }

   public WebView build() {
      WebView var1 = new WebView();
      this.applyTo(var1);
      return var1;
   }

   public void applyTo(WebView var1) {
      super.applyTo(var1);
      if (this.fontScaleSet) {
         var1.setFontScale(this.fontScale);
      }

      if (this.maxHeightSet) {
         var1.setMaxHeight(this.maxHeight);
      }

      if (this.maxWidthSet) {
         var1.setMaxWidth(this.maxWidth);
      }

      if (this.minHeightSet) {
         var1.setMinHeight(this.minHeight);
      }

      if (this.minWidthSet) {
         var1.setMinWidth(this.minWidth);
      }

      if (this.prefHeightSet) {
         var1.setPrefHeight(this.prefHeight);
      }

      if (this.prefWidthSet) {
         var1.setPrefWidth(this.prefWidth);
      }

      if (this.engineBuilder != null) {
         this.engineBuilder.applyTo(var1.getEngine());
      }

   }

   public WebViewBuilder fontScale(double var1) {
      this.fontScale = var1;
      this.fontScaleSet = true;
      return this;
   }

   public WebViewBuilder maxHeight(double var1) {
      this.maxHeight = var1;
      this.maxHeightSet = true;
      return this;
   }

   public WebViewBuilder maxWidth(double var1) {
      this.maxWidth = var1;
      this.maxWidthSet = true;
      return this;
   }

   public WebViewBuilder minHeight(double var1) {
      this.minHeight = var1;
      this.minHeightSet = true;
      return this;
   }

   public WebViewBuilder minWidth(double var1) {
      this.minWidth = var1;
      this.minWidthSet = true;
      return this;
   }

   public WebViewBuilder prefHeight(double var1) {
      this.prefHeight = var1;
      this.prefHeightSet = true;
      return this;
   }

   public WebViewBuilder prefWidth(double var1) {
      this.prefWidth = var1;
      this.prefWidthSet = true;
      return this;
   }

   public WebViewBuilder confirmHandler(Callback var1) {
      this.engineBuilder().confirmHandler(var1);
      return this;
   }

   public WebViewBuilder createPopupHandler(Callback var1) {
      this.engineBuilder().createPopupHandler(var1);
      return this;
   }

   public WebViewBuilder onAlert(EventHandler var1) {
      this.engineBuilder().onAlert(var1);
      return this;
   }

   public WebViewBuilder onResized(EventHandler var1) {
      this.engineBuilder().onResized(var1);
      return this;
   }

   public WebViewBuilder onStatusChanged(EventHandler var1) {
      this.engineBuilder().onStatusChanged(var1);
      return this;
   }

   public WebViewBuilder onVisibilityChanged(EventHandler var1) {
      this.engineBuilder().onVisibilityChanged(var1);
      return this;
   }

   public WebViewBuilder promptHandler(Callback var1) {
      this.engineBuilder().promptHandler(var1);
      return this;
   }

   public WebViewBuilder location(String var1) {
      this.engineBuilder().location(var1);
      return this;
   }

   private WebEngineBuilder engineBuilder() {
      if (this.engineBuilder == null) {
         this.engineBuilder = WebEngineBuilder.create();
      }

      return this.engineBuilder;
   }
}
