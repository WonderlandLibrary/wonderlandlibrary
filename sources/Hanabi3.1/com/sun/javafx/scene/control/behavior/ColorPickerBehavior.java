package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ColorPickerBehavior extends ComboBoxBaseBehavior {
   protected static final String OPEN_ACTION = "Open";
   protected static final String CLOSE_ACTION = "Close";
   protected static final List COLOR_PICKER_BINDINGS = new ArrayList();

   public ColorPickerBehavior(ColorPicker var1) {
      super(var1, COLOR_PICKER_BINDINGS);
   }

   protected void callAction(String var1) {
      if ("Open".equals(var1)) {
         this.show();
      } else if ("Close".equals(var1)) {
         this.hide();
      } else {
         super.callAction(var1);
      }

   }

   public void onAutoHide() {
      ColorPicker var1 = (ColorPicker)this.getControl();
      ColorPickerSkin var2 = (ColorPickerSkin)var1.getSkin();
      var2.syncWithAutoUpdate();
      if (!var1.isShowing()) {
         super.onAutoHide();
      }

   }

   static {
      COLOR_PICKER_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, KeyEvent.KEY_PRESSED, "Close"));
      COLOR_PICKER_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_PRESSED, "Open"));
      COLOR_PICKER_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_PRESSED, "Open"));
   }
}
