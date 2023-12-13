package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ButtonBehavior;
import javafx.scene.control.Hyperlink;

public class HyperlinkSkin extends LabeledSkinBase {
   public HyperlinkSkin(Hyperlink var1) {
      super(var1, new ButtonBehavior(var1));
   }
}
