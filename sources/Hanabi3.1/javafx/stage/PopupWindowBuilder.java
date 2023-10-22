package javafx.stage;

import javafx.event.EventHandler;

/** @deprecated */
@Deprecated
public abstract class PopupWindowBuilder extends WindowBuilder {
   private int __set;
   private boolean autoFix;
   private boolean autoHide;
   private boolean consumeAutoHidingEvents;
   private boolean hideOnEscape;
   private EventHandler onAutoHide;

   protected PopupWindowBuilder() {
   }

   public void applyTo(PopupWindow var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAutoFix(this.autoFix);
      }

      if ((var2 & 2) != 0) {
         var1.setAutoHide(this.autoHide);
      }

      if ((var2 & 4) != 0) {
         var1.setConsumeAutoHidingEvents(this.consumeAutoHidingEvents);
      }

      if ((var2 & 8) != 0) {
         var1.setHideOnEscape(this.hideOnEscape);
      }

      if ((var2 & 16) != 0) {
         var1.setOnAutoHide(this.onAutoHide);
      }

   }

   public PopupWindowBuilder autoFix(boolean var1) {
      this.autoFix = var1;
      this.__set |= 1;
      return this;
   }

   public PopupWindowBuilder autoHide(boolean var1) {
      this.autoHide = var1;
      this.__set |= 2;
      return this;
   }

   public PopupWindowBuilder consumeAutoHidingEvents(boolean var1) {
      this.consumeAutoHidingEvents = var1;
      this.__set |= 4;
      return this;
   }

   public PopupWindowBuilder hideOnEscape(boolean var1) {
      this.hideOnEscape = var1;
      this.__set |= 8;
      return this;
   }

   public PopupWindowBuilder onAutoHide(EventHandler var1) {
      this.onAutoHide = var1;
      this.__set |= 16;
      return this;
   }
}
