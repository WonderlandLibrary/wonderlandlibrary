package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.DatePickerContent;
import com.sun.javafx.scene.traversal.Direction;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class DateCellBehavior extends BehaviorBase {
   protected static final List DATE_CELL_BINDINGS = new ArrayList();

   public DateCellBehavior(DateCell var1) {
      super(var1, DATE_CELL_BINDINGS);
   }

   public void callAction(String var1) {
      DateCell var2 = (DateCell)this.getControl();
      DatePickerContent var3 = this.findDatePickerContent(var2);
      if (var3 != null) {
         switch (var1) {
            case "SelectDate":
               var3.selectDayCell(var2);
               break;
            default:
               super.callAction(var1);
         }

      } else {
         super.callAction(var1);
      }
   }

   public void traverse(Node var1, Direction var2) {
      boolean var3 = var1.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
      switch (var2) {
         case UP:
         case DOWN:
         case LEFT:
         case RIGHT:
            if (var1 instanceof DateCell) {
               DatePickerContent var4 = this.findDatePickerContent(var1);
               if (var4 != null) {
                  DateCell var5 = (DateCell)var1;
                  switch (var2) {
                     case UP:
                        var4.goToDayCell(var5, -1, ChronoUnit.WEEKS, true);
                        break;
                     case DOWN:
                        var4.goToDayCell(var5, 1, ChronoUnit.WEEKS, true);
                        break;
                     case LEFT:
                        var4.goToDayCell(var5, var3 ? 1 : -1, ChronoUnit.DAYS, true);
                        break;
                     case RIGHT:
                        var4.goToDayCell(var5, var3 ? -1 : 1, ChronoUnit.DAYS, true);
                  }

                  return;
               }
            }
         default:
            super.traverse(var1, var2);
      }
   }

   protected DatePickerContent findDatePickerContent(Node var1) {
      Object var2 = var1;

      while((var2 = ((Node)var2).getParent()) != null && !(var2 instanceof DatePickerContent)) {
      }

      return (DatePickerContent)var2;
   }

   static {
      DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
      DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
      DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
      DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "TraverseRight"));
      DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_RELEASED, "SelectDate"));
      DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_RELEASED, "SelectDate"));
   }
}
