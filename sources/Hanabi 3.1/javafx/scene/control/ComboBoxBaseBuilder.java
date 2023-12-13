package javafx.scene.control;

import javafx.event.EventHandler;

/** @deprecated */
@Deprecated
public abstract class ComboBoxBaseBuilder extends ControlBuilder {
   private int __set;
   private boolean editable;
   private EventHandler onAction;
   private EventHandler onHidden;
   private EventHandler onHiding;
   private EventHandler onShowing;
   private EventHandler onShown;
   private String promptText;
   private Object value;

   protected ComboBoxBaseBuilder() {
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(ComboBoxBase var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setEditable(this.editable);
               break;
            case 1:
               var1.setOnAction(this.onAction);
               break;
            case 2:
               var1.setOnHidden(this.onHidden);
               break;
            case 3:
               var1.setOnHiding(this.onHiding);
               break;
            case 4:
               var1.setOnShowing(this.onShowing);
               break;
            case 5:
               var1.setOnShown(this.onShown);
               break;
            case 6:
               var1.setPromptText(this.promptText);
               break;
            case 7:
               var1.setValue(this.value);
         }
      }

   }

   public ComboBoxBaseBuilder editable(boolean var1) {
      this.editable = var1;
      this.__set(0);
      return this;
   }

   public ComboBoxBaseBuilder onAction(EventHandler var1) {
      this.onAction = var1;
      this.__set(1);
      return this;
   }

   public ComboBoxBaseBuilder onHidden(EventHandler var1) {
      this.onHidden = var1;
      this.__set(2);
      return this;
   }

   public ComboBoxBaseBuilder onHiding(EventHandler var1) {
      this.onHiding = var1;
      this.__set(3);
      return this;
   }

   public ComboBoxBaseBuilder onShowing(EventHandler var1) {
      this.onShowing = var1;
      this.__set(4);
      return this;
   }

   public ComboBoxBaseBuilder onShown(EventHandler var1) {
      this.onShown = var1;
      this.__set(5);
      return this;
   }

   public ComboBoxBaseBuilder promptText(String var1) {
      this.promptText = var1;
      this.__set(6);
      return this;
   }

   public ComboBoxBaseBuilder value(Object var1) {
      this.value = var1;
      this.__set(7);
      return this;
   }
}
