package com.sun.javafx.scene.web.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import com.sun.javafx.scene.web.skin.HTMLEditorSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.scene.web.HTMLEditor;

public class HTMLEditorBehavior extends BehaviorBase {
   protected static final List HTML_EDITOR_BINDINGS = new ArrayList();

   public HTMLEditorBehavior(HTMLEditor var1) {
      super(var1, HTML_EDITOR_BINDINGS);
   }

   protected void callAction(String var1) {
      if (!"bold".equals(var1) && !"italic".equals(var1) && !"underline".equals(var1)) {
         if ("F12".equals(var1)) {
            ((HTMLEditor)this.getControl()).getImpl_traversalEngine().selectFirst().requestFocus();
         } else {
            super.callAction(var1);
         }
      } else {
         HTMLEditor var2 = (HTMLEditor)this.getControl();
         HTMLEditorSkin var3 = (HTMLEditorSkin)var2.getSkin();
         var3.keyboardShortcuts(var1);
      }

   }

   static {
      HTML_EDITOR_BINDINGS.add((new KeyBinding(KeyCode.B, "bold")).shortcut());
      HTML_EDITOR_BINDINGS.add((new KeyBinding(KeyCode.I, "italic")).shortcut());
      HTML_EDITOR_BINDINGS.add((new KeyBinding(KeyCode.U, "underline")).shortcut());
      HTML_EDITOR_BINDINGS.add(new KeyBinding(KeyCode.F12, "F12"));
      HTML_EDITOR_BINDINGS.add((new KeyBinding(KeyCode.TAB, "TraverseNext")).ctrl());
      HTML_EDITOR_BINDINGS.add((new KeyBinding(KeyCode.TAB, "TraversePrevious")).ctrl().shift());
   }
}
