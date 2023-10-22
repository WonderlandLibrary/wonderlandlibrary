package javafx.scene.web;

import javafx.event.EventHandler;
import javafx.util.Builder;
import javafx.util.Callback;

/** @deprecated */
@Deprecated
public final class WebEngineBuilder implements Builder {
   private Callback confirmHandler;
   private boolean confirmHandlerSet;
   private Callback createPopupHandler;
   private boolean createPopupHandlerSet;
   private EventHandler onAlert;
   private boolean onAlertSet;
   private EventHandler onResized;
   private boolean onResizedSet;
   private EventHandler onStatusChanged;
   private boolean onStatusChangedSet;
   private EventHandler onVisibilityChanged;
   private boolean onVisibilityChangedSet;
   private Callback promptHandler;
   private boolean promptHandlerSet;
   private String location;
   private boolean locationSet;

   public static WebEngineBuilder create() {
      return new WebEngineBuilder();
   }

   public WebEngine build() {
      WebEngine var1 = new WebEngine();
      this.applyTo(var1);
      return var1;
   }

   public void applyTo(WebEngine var1) {
      if (this.confirmHandlerSet) {
         var1.setConfirmHandler(this.confirmHandler);
      }

      if (this.createPopupHandlerSet) {
         var1.setCreatePopupHandler(this.createPopupHandler);
      }

      if (this.onAlertSet) {
         var1.setOnAlert(this.onAlert);
      }

      if (this.onResizedSet) {
         var1.setOnResized(this.onResized);
      }

      if (this.onStatusChangedSet) {
         var1.setOnStatusChanged(this.onStatusChanged);
      }

      if (this.onVisibilityChangedSet) {
         var1.setOnVisibilityChanged(this.onVisibilityChanged);
      }

      if (this.promptHandlerSet) {
         var1.setPromptHandler(this.promptHandler);
      }

      if (this.locationSet) {
         var1.load(this.location);
      }

   }

   public WebEngineBuilder confirmHandler(Callback var1) {
      this.confirmHandler = var1;
      this.confirmHandlerSet = true;
      return this;
   }

   public WebEngineBuilder createPopupHandler(Callback var1) {
      this.createPopupHandler = var1;
      this.createPopupHandlerSet = true;
      return this;
   }

   public WebEngineBuilder onAlert(EventHandler var1) {
      this.onAlert = var1;
      this.onAlertSet = true;
      return this;
   }

   public WebEngineBuilder onResized(EventHandler var1) {
      this.onResized = var1;
      this.onResizedSet = true;
      return this;
   }

   public WebEngineBuilder onStatusChanged(EventHandler var1) {
      this.onStatusChanged = var1;
      this.onStatusChangedSet = true;
      return this;
   }

   public WebEngineBuilder onVisibilityChanged(EventHandler var1) {
      this.onVisibilityChanged = var1;
      this.onVisibilityChangedSet = true;
      return this;
   }

   public WebEngineBuilder promptHandler(Callback var1) {
      this.promptHandler = var1;
      this.promptHandlerSet = true;
      return this;
   }

   public WebEngineBuilder location(String var1) {
      this.location = var1;
      this.locationSet = true;
      return this;
   }
}
