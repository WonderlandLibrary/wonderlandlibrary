package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.SplitMenuButtonBehavior;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.input.MouseEvent;

public class SplitMenuButtonSkin extends MenuButtonSkinBase {
   public SplitMenuButtonSkin(SplitMenuButton var1) {
      super(var1, new SplitMenuButtonBehavior(var1));
      this.behaveLikeButton = true;
      this.arrowButton.addEventHandler(MouseEvent.ANY, (var0) -> {
         var0.consume();
      });
      this.arrowButton.setOnMousePressed((var1x) -> {
         ((SplitMenuButtonBehavior)this.getBehavior()).mousePressed(var1x, false);
         var1x.consume();
      });
      this.arrowButton.setOnMouseReleased((var1x) -> {
         ((SplitMenuButtonBehavior)this.getBehavior()).mouseReleased(var1x, false);
         var1x.consume();
      });
      this.label.setLabelFor(var1);
   }
}
