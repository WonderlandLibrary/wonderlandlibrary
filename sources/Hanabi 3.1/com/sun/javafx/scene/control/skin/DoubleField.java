package com.sun.javafx.scene.control.skin;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Skin;

class DoubleField extends InputField {
   private DoubleProperty value = new SimpleDoubleProperty(this, "value");

   public final double getValue() {
      return this.value.get();
   }

   public final void setValue(double var1) {
      this.value.set(var1);
   }

   public final DoubleProperty valueProperty() {
      return this.value;
   }

   public DoubleField() {
      this.getStyleClass().setAll((Object[])("double-field"));
   }

   protected Skin createDefaultSkin() {
      return new DoubleFieldSkin(this);
   }
}
