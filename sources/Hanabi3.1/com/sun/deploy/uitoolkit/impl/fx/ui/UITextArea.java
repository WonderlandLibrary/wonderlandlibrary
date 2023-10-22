package com.sun.deploy.uitoolkit.impl.fx.ui;

import javafx.scene.control.Label;

public class UITextArea extends Label {
   double preferred_width = 360.0;

   public UITextArea(String var1) {
      this.setText(var1);
      this.setPrefWidth(this.preferred_width);
   }

   public UITextArea(double var1) {
      this.preferred_width = var1;
      this.setPrefWidth(this.preferred_width);
      this.setMinSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
   }
}
