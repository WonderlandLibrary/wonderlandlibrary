package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.MenuButtonBehavior;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuButton;

public class MenuButtonSkin extends MenuButtonSkinBase {
   static final String AUTOHIDE = "autoHide";

   public MenuButtonSkin(MenuButton var1) {
      super(var1, new MenuButtonBehavior(var1));
      this.popup.setOnAutoHide(new EventHandler() {
         public void handle(Event var1) {
            MenuButton var2 = (MenuButton)MenuButtonSkin.this.getSkinnable();
            if (!var2.getProperties().containsKey("autoHide")) {
               var2.getProperties().put("autoHide", Boolean.TRUE);
            }

         }
      });
      this.popup.setOnShown((var1x) -> {
         ContextMenuContent var2 = (ContextMenuContent)this.popup.getSkin().getNode();
         if (var2 != null) {
            var2.requestFocus();
         }

      });
      if (var1.getOnAction() == null) {
         var1.setOnAction((var1x) -> {
            var1.show();
         });
      }

      this.label.setLabelFor(var1);
   }
}
