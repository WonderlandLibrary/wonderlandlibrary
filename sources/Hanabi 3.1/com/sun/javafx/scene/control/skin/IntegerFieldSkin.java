package com.sun.javafx.scene.control.skin;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.scene.Node;

public class IntegerFieldSkin extends InputFieldSkin {
   private InvalidationListener integerFieldValueListener;

   public IntegerFieldSkin(IntegerField var1) {
      super(var1);
      var1.valueProperty().addListener(this.integerFieldValueListener = (var1x) -> {
         this.updateText();
      });
   }

   public IntegerField getSkinnable() {
      return (IntegerField)this.control;
   }

   public Node getNode() {
      return this.getTextField();
   }

   public void dispose() {
      ((IntegerField)this.control).valueProperty().removeListener(this.integerFieldValueListener);
      super.dispose();
   }

   protected boolean accept(String var1) {
      if (var1.length() == 0) {
         return true;
      } else {
         if (var1.matches("[0-9]*")) {
            try {
               Integer.parseInt(var1);
               int var2 = Integer.parseInt(var1);
               int var3 = ((IntegerField)this.control).getMaxValue();
               return var3 != -1 ? var2 <= var3 : true;
            } catch (NumberFormatException var4) {
            }
         }

         return false;
      }
   }

   protected void updateText() {
      this.getTextField().setText("" + ((IntegerField)this.control).getValue());
   }

   protected void updateValue() {
      int var1 = ((IntegerField)this.control).getValue();
      String var3 = this.getTextField().getText() == null ? "" : this.getTextField().getText().trim();

      try {
         int var2 = Integer.parseInt(var3);
         if (var2 != var1) {
            ((IntegerField)this.control).setValue(var2);
         }
      } catch (NumberFormatException var5) {
         ((IntegerField)this.control).setValue(0);
         Platform.runLater(() -> {
            this.getTextField().positionCaret(1);
         });
      }

   }
}
