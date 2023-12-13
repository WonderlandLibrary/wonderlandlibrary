package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.PaginationSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Pagination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class PaginationBehavior extends BehaviorBase {
   private static final String LEFT = "Left";
   private static final String RIGHT = "Right";
   protected static final List PAGINATION_BINDINGS = new ArrayList();

   protected String matchActionForEvent(KeyEvent var1) {
      String var2 = super.matchActionForEvent(var1);
      if (var2 != null) {
         if (var1.getCode() == KeyCode.LEFT) {
            if (((Pagination)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
               var2 = "Right";
            }
         } else if (var1.getCode() == KeyCode.RIGHT && ((Pagination)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
            var2 = "Left";
         }
      }

      return var2;
   }

   protected void callAction(String var1) {
      PaginationSkin var2;
      if ("Left".equals(var1)) {
         var2 = (PaginationSkin)((Pagination)this.getControl()).getSkin();
         var2.selectPrevious();
      } else if ("Right".equals(var1)) {
         var2 = (PaginationSkin)((Pagination)this.getControl()).getSkin();
         var2.selectNext();
      } else {
         super.callAction(var1);
      }

   }

   public void mousePressed(MouseEvent var1) {
      super.mousePressed(var1);
      Pagination var2 = (Pagination)this.getControl();
      var2.requestFocus();
   }

   public PaginationBehavior(Pagination var1) {
      super(var1, PAGINATION_BINDINGS);
   }

   static {
      PAGINATION_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "Left"));
      PAGINATION_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "Right"));
   }
}
