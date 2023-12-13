package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;

public class ToggleButtonBehavior extends ButtonBehavior {
   protected static final List TOGGLE_BUTTON_BINDINGS = new ArrayList();

   public ToggleButtonBehavior(ToggleButton var1) {
      super(var1, TOGGLE_BUTTON_BINDINGS);
   }

   private int nextToggleIndex(ObservableList var1, int var2) {
      if (var2 >= 0 && var2 < var1.size()) {
         Toggle var3;
         int var4;
         for(var4 = (var2 + 1) % var1.size(); var4 != var2 && (var3 = (Toggle)var1.get(var4)) instanceof Node && ((Node)var3).isDisabled(); var4 = (var4 + 1) % var1.size()) {
         }

         return var4;
      } else {
         return 0;
      }
   }

   private int previousToggleIndex(ObservableList var1, int var2) {
      if (var2 >= 0 && var2 < var1.size()) {
         Toggle var3;
         int var4;
         for(var4 = Math.floorMod(var2 - 1, var1.size()); var4 != var2 && (var3 = (Toggle)var1.get(var4)) instanceof Node && ((Node)var3).isDisabled(); var4 = Math.floorMod(var4 - 1, var1.size())) {
         }

         return var4;
      } else {
         return var1.size();
      }
   }

   protected void callAction(String var1) {
      ToggleButton var2 = (ToggleButton)this.getControl();
      ToggleGroup var3 = var2.getToggleGroup();
      if (var3 == null) {
         super.callAction(var1);
      } else {
         ObservableList var4 = var3.getToggles();
         int var5 = var4.indexOf(var2);
         switch (var1) {
            case "ToggleNext-Right":
            case "ToggleNext-Down":
            case "TogglePrevious-Left":
            case "TogglePrevious-Up":
               boolean var8 = this.traversingToNext(var1, var2.getEffectiveNodeOrientation());
               if (Utils.isTwoLevelFocus()) {
                  super.callAction(this.toggleToTraverseAction(var1));
               } else {
                  int var9;
                  Toggle var10;
                  if (var8) {
                     var9 = this.nextToggleIndex(var4, var5);
                     if (var9 == var5) {
                        super.callAction(this.toggleToTraverseAction(var1));
                     } else {
                        var10 = (Toggle)var4.get(var9);
                        var3.selectToggle(var10);
                        ((Control)var10).requestFocus();
                     }
                  } else {
                     var9 = this.previousToggleIndex(var4, var5);
                     if (var9 == var5) {
                        super.callAction(this.toggleToTraverseAction(var1));
                     } else {
                        var10 = (Toggle)var4.get(var9);
                        var3.selectToggle(var10);
                        ((Control)var10).requestFocus();
                     }
                  }
               }
               break;
            default:
               super.callAction(var1);
         }

      }
   }

   private boolean traversingToNext(String var1, NodeOrientation var2) {
      boolean var3 = var2 == NodeOrientation.RIGHT_TO_LEFT;
      switch (var1) {
         case "ToggleNext-Right":
            return !var3;
         case "ToggleNext-Down":
            return true;
         case "TogglePrevious-Left":
            return var3;
         case "TogglePrevious-Up":
            return false;
         default:
            throw new IllegalArgumentException("Not a toggle action");
      }
   }

   private String toggleToTraverseAction(String var1) {
      switch (var1) {
         case "ToggleNext-Right":
            return "TraverseRight";
         case "ToggleNext-Down":
            return "TraverseDown";
         case "TogglePrevious-Left":
            return "TraverseLeft";
         case "TogglePrevious-Up":
            return "TraverseUp";
         default:
            throw new IllegalArgumentException("Not a toggle action");
      }
   }

   static {
      TOGGLE_BUTTON_BINDINGS.addAll(BUTTON_BINDINGS);
      TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "ToggleNext-Right"));
      TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TogglePrevious-Left"));
      TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "ToggleNext-Down"));
      TOGGLE_BUTTON_BINDINGS.add(new KeyBinding(KeyCode.UP, "TogglePrevious-Up"));
   }
}
