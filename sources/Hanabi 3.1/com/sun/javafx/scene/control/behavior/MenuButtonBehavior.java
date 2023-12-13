package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.MenuButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MenuButtonBehavior extends MenuButtonBehaviorBase {
   protected static final List MENU_BUTTON_BINDINGS = new ArrayList();

   public MenuButtonBehavior(MenuButton var1) {
      super(var1, MENU_BUTTON_BINDINGS);
   }

   static {
      MENU_BUTTON_BINDINGS.addAll(BASE_MENU_BUTTON_BINDINGS);
      MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_PRESSED, "Open"));
      MENU_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_PRESSED, "Open"));
   }
}
