package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.ScrollPaneSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ScrollPaneBehavior extends BehaviorBase {
   static final String TRAVERSE_DEBUG = "TraverseDebug";
   static final String HORIZONTAL_UNITDECREMENT = "HorizontalUnitDecrement";
   static final String HORIZONTAL_UNITINCREMENT = "HorizontalUnitIncrement";
   static final String VERTICAL_UNITDECREMENT = "VerticalUnitDecrement";
   static final String VERTICAL_UNITINCREMENT = "VerticalUnitIncrement";
   static final String VERTICAL_PAGEDECREMENT = "VerticalPageDecrement";
   static final String VERTICAL_PAGEINCREMENT = "VerticalPageIncrement";
   static final String VERTICAL_HOME = "VerticalHome";
   static final String VERTICAL_END = "VerticalEnd";
   protected static final List SCROLL_PANE_BINDINGS = new ArrayList();

   public ScrollPaneBehavior(ScrollPane var1) {
      super(var1, SCROLL_PANE_BINDINGS);
   }

   public void horizontalUnitIncrement() {
      ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).hsbIncrement();
   }

   public void horizontalUnitDecrement() {
      ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).hsbDecrement();
   }

   public void verticalUnitIncrement() {
      ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).vsbIncrement();
   }

   void verticalUnitDecrement() {
      ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).vsbDecrement();
   }

   void horizontalPageIncrement() {
      ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).hsbPageIncrement();
   }

   void horizontalPageDecrement() {
      ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).hsbPageDecrement();
   }

   void verticalPageIncrement() {
      ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).vsbPageIncrement();
   }

   void verticalPageDecrement() {
      ((ScrollPaneSkin)((ScrollPane)this.getControl()).getSkin()).vsbPageDecrement();
   }

   void verticalHome() {
      ScrollPane var1 = (ScrollPane)this.getControl();
      var1.setHvalue(var1.getHmin());
      var1.setVvalue(var1.getVmin());
   }

   void verticalEnd() {
      ScrollPane var1 = (ScrollPane)this.getControl();
      var1.setHvalue(var1.getHmax());
      var1.setVvalue(var1.getVmax());
   }

   public void contentDragged(double var1, double var3) {
      ScrollPane var5 = (ScrollPane)this.getControl();
      if (var5.isPannable()) {
         if (var1 < 0.0 && var5.getHvalue() != 0.0 || var1 > 0.0 && var5.getHvalue() != var5.getHmax()) {
            var5.setHvalue(var5.getHvalue() + var1);
         }

         if (var3 < 0.0 && var5.getVvalue() != 0.0 || var3 > 0.0 && var5.getVvalue() != var5.getVmax()) {
            var5.setVvalue(var5.getVvalue() + var3);
         }

      }
   }

   protected String matchActionForEvent(KeyEvent var1) {
      String var2 = super.matchActionForEvent(var1);
      if (var2 != null) {
         if (var1.getCode() == KeyCode.LEFT) {
            if (((ScrollPane)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
               var2 = "HorizontalUnitIncrement";
            }
         } else if (var1.getCode() == KeyCode.RIGHT && ((ScrollPane)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
            var2 = "HorizontalUnitDecrement";
         }
      }

      return var2;
   }

   protected void callAction(String var1) {
      switch (var1) {
         case "HorizontalUnitDecrement":
            this.horizontalUnitDecrement();
            break;
         case "HorizontalUnitIncrement":
            this.horizontalUnitIncrement();
            break;
         case "VerticalUnitDecrement":
            this.verticalUnitDecrement();
            break;
         case "VerticalUnitIncrement":
            this.verticalUnitIncrement();
            break;
         case "VerticalPageDecrement":
            this.verticalPageDecrement();
            break;
         case "VerticalPageIncrement":
            this.verticalPageIncrement();
            break;
         case "VerticalHome":
            this.verticalHome();
            break;
         case "VerticalEnd":
            this.verticalEnd();
            break;
         default:
            super.callAction(var1);
      }

   }

   public void mouseClicked() {
      ((ScrollPane)this.getControl()).requestFocus();
   }

   public void mousePressed(MouseEvent var1) {
      super.mousePressed(var1);
      ((ScrollPane)this.getControl()).requestFocus();
   }

   static {
      SCROLL_PANE_BINDINGS.add((new KeyBinding(KeyCode.F4, "TraverseDebug")).alt().ctrl().shift());
      SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "HorizontalUnitDecrement"));
      SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "HorizontalUnitIncrement"));
      SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.UP, "VerticalUnitDecrement"));
      SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "VerticalUnitIncrement"));
      SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "VerticalPageDecrement"));
      SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "VerticalPageIncrement"));
      SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "VerticalPageIncrement"));
      SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.HOME, "VerticalHome"));
      SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.END, "VerticalEnd"));
   }
}
