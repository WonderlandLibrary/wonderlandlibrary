package com.sun.javafx.scene.control.skin;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;

class WebColorField extends InputField {
   private ObjectProperty value = new SimpleObjectProperty(this, "value");

   public final Color getValue() {
      return (Color)this.value.get();
   }

   public final void setValue(Color var1) {
      this.value.set(var1);
   }

   public final ObjectProperty valueProperty() {
      return this.value;
   }

   public WebColorField() {
      this.getStyleClass().setAll((Object[])("webcolor-field"));
   }

   protected Skin createDefaultSkin() {
      return new WebColorFieldSkin(this);
   }
}
