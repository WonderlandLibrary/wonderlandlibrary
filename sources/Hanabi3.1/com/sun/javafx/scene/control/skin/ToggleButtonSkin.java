package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ToggleButtonBehavior;
import javafx.scene.control.ToggleButton;

public class ToggleButtonSkin extends LabeledSkinBase {
   public ToggleButtonSkin(ToggleButton var1) {
      super(var1, new ToggleButtonBehavior(var1));
   }
}
