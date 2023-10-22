package com.sun.javafx.scene.control.skin;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.scene.Node;

public class DoubleFieldSkin extends InputFieldSkin {
   private InvalidationListener doubleFieldValueListener;

   public DoubleFieldSkin(DoubleField var1) {
      super(var1);
      var1.valueProperty().addListener(this.doubleFieldValueListener = (var1x) -> {
         this.updateText();
      });
   }

   public DoubleField getSkinnable() {
      return (DoubleField)this.control;
   }

   public Node getNode() {
      return this.getTextField();
   }

   public void dispose() {
      ((DoubleField)this.control).valueProperty().removeListener(this.doubleFieldValueListener);
      super.dispose();
   }

   protected boolean accept(String var1) {
      if (var1.length() == 0) {
         return true;
      } else {
         if (var1.matches("[0-9\\.]*")) {
            try {
               Double.parseDouble(var1);
               return true;
            } catch (NumberFormatException var3) {
            }
         }

         return false;
      }
   }

   protected void updateText() {
      this.getTextField().setText("" + ((DoubleField)this.control).getValue());
   }

   protected void updateValue() {
      double var1 = ((DoubleField)this.control).getValue();
      String var5 = this.getTextField().getText() == null ? "" : this.getTextField().getText().trim();

      try {
         double var3 = Double.parseDouble(var5);
         if (var3 != var1) {
            ((DoubleField)this.control).setValue(var3);
         }
      } catch (NumberFormatException var7) {
         ((DoubleField)this.control).setValue(0.0);
         Platform.runLater(() -> {
            this.getTextField().positionCaret(1);
         });
      }

   }
}
