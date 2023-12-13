package com.sun.javafx.scene.control.skin;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Skin;

class IntegerField extends InputField {
   private IntegerProperty value;
   private IntegerProperty maxValue;

   public final int getValue() {
      return this.value.get();
   }

   public final void setValue(int var1) {
      this.value.set(var1);
   }

   public final IntegerProperty valueProperty() {
      return this.value;
   }

   public final int getMaxValue() {
      return this.maxValue.get();
   }

   public final void setMaxValue(int var1) {
      this.maxValue.set(var1);
   }

   public final IntegerProperty maxValueProperty() {
      return this.maxValue;
   }

   public IntegerField() {
      this(-1);
   }

   public IntegerField(int var1) {
      this.value = new SimpleIntegerProperty(this, "value");
      this.maxValue = new SimpleIntegerProperty(this, "maxValue", -1);
      this.getStyleClass().setAll((Object[])("integer-field"));
      this.setMaxValue(var1);
   }

   protected Skin createDefaultSkin() {
      return new IntegerFieldSkin(this);
   }
}
