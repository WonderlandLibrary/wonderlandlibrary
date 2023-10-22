package javafx.scene.control;

import javafx.event.EventHandler;

/** @deprecated */
@Deprecated
public abstract class ButtonBaseBuilder extends LabeledBuilder {
   private boolean __set;
   private EventHandler onAction;

   protected ButtonBaseBuilder() {
   }

   public void applyTo(ButtonBase var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setOnAction(this.onAction);
      }

   }

   public ButtonBaseBuilder onAction(EventHandler var1) {
      this.onAction = var1;
      this.__set = true;
      return this;
   }
}
