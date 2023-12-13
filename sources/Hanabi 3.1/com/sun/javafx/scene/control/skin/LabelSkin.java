package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import java.util.Collections;
import javafx.scene.control.Label;

public class LabelSkin extends LabeledSkinBase {
   public LabelSkin(Label var1) {
      super(var1, new BehaviorBase(var1, Collections.emptyList()));
      this.consumeMouseEvents(false);
      this.registerChangeListener(var1.labelForProperty(), "LABEL_FOR");
   }

   protected void handleControlPropertyChanged(String var1) {
      super.handleControlPropertyChanged(var1);
      if ("LABEL_FOR".equals(var1)) {
         this.mnemonicTargetChanged();
      }

   }
}
