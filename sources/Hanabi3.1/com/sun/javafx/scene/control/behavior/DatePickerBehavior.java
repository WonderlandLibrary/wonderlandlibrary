package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.DatePicker;

public class DatePickerBehavior extends ComboBoxBaseBehavior {
   protected static final List DATE_PICKER_BINDINGS = new ArrayList();

   public DatePickerBehavior(DatePicker var1) {
      super(var1, DATE_PICKER_BINDINGS);
   }

   public void onAutoHide() {
      DatePicker var1 = (DatePicker)this.getControl();
      DatePickerSkin var2 = (DatePickerSkin)var1.getSkin();
      var2.syncWithAutoUpdate();
      if (!var1.isShowing()) {
         super.onAutoHide();
      }

   }

   static {
      DATE_PICKER_BINDINGS.addAll(COMBO_BOX_BASE_BINDINGS);
   }
}
