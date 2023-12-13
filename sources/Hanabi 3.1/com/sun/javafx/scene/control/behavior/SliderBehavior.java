package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventType;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.control.Control;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class SliderBehavior extends BehaviorBase {
   protected static final List SLIDER_BINDINGS = new ArrayList();
   private TwoLevelFocusBehavior tlFocus;

   protected String matchActionForEvent(KeyEvent var1) {
      String var2 = super.matchActionForEvent(var1);
      if (var2 != null) {
         if (var1.getCode() != KeyCode.LEFT && var1.getCode() != KeyCode.KP_LEFT) {
            if ((var1.getCode() == KeyCode.RIGHT || var1.getCode() == KeyCode.KP_RIGHT) && ((Slider)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
               var2 = ((Slider)this.getControl()).getOrientation() == Orientation.HORIZONTAL ? "DecrementValue" : "IncrementValue";
            }
         } else if (((Slider)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
            var2 = ((Slider)this.getControl()).getOrientation() == Orientation.HORIZONTAL ? "IncrementValue" : "DecrementValue";
         }
      }

      return var2;
   }

   protected void callAction(String var1) {
      if ("Home".equals(var1)) {
         this.home();
      } else if ("End".equals(var1)) {
         this.end();
      } else if ("IncrementValue".equals(var1)) {
         this.incrementValue();
      } else if ("DecrementValue".equals(var1)) {
         this.decrementValue();
      } else {
         super.callAction(var1);
      }

   }

   public SliderBehavior(Slider var1) {
      super(var1, SLIDER_BINDINGS);
      if (Utils.isTwoLevelFocus()) {
         this.tlFocus = new TwoLevelFocusBehavior(var1);
      }

   }

   public void dispose() {
      if (this.tlFocus != null) {
         this.tlFocus.dispose();
      }

      super.dispose();
   }

   public void trackPress(MouseEvent var1, double var2) {
      Slider var4 = (Slider)this.getControl();
      if (!var4.isFocused()) {
         var4.requestFocus();
      }

      if (var4.getOrientation().equals(Orientation.HORIZONTAL)) {
         var4.adjustValue(var2 * (var4.getMax() - var4.getMin()) + var4.getMin());
      } else {
         var4.adjustValue((1.0 - var2) * (var4.getMax() - var4.getMin()) + var4.getMin());
      }

   }

   public void thumbPressed(MouseEvent var1, double var2) {
      Slider var4 = (Slider)this.getControl();
      if (!var4.isFocused()) {
         var4.requestFocus();
      }

      var4.setValueChanging(true);
   }

   public void thumbDragged(MouseEvent var1, double var2) {
      Slider var4 = (Slider)this.getControl();
      var4.setValue(com.sun.javafx.util.Utils.clamp(var4.getMin(), var2 * (var4.getMax() - var4.getMin()) + var4.getMin(), var4.getMax()));
   }

   public void thumbReleased(MouseEvent var1) {
      Slider var2 = (Slider)this.getControl();
      var2.setValueChanging(false);
      var2.adjustValue(var2.getValue());
   }

   void home() {
      Slider var1 = (Slider)this.getControl();
      var1.adjustValue(var1.getMin());
   }

   void decrementValue() {
      Slider var1 = (Slider)this.getControl();
      if (var1.isSnapToTicks()) {
         var1.adjustValue(var1.getValue() - this.computeIncrement());
      } else {
         var1.decrement();
      }

   }

   void end() {
      Slider var1 = (Slider)this.getControl();
      var1.adjustValue(var1.getMax());
   }

   void incrementValue() {
      Slider var1 = (Slider)this.getControl();
      if (var1.isSnapToTicks()) {
         var1.adjustValue(var1.getValue() + this.computeIncrement());
      } else {
         var1.increment();
      }

   }

   double computeIncrement() {
      Slider var1 = (Slider)this.getControl();
      double var2 = 0.0;
      if (var1.getMinorTickCount() != 0) {
         var2 = var1.getMajorTickUnit() / (double)(Math.max(var1.getMinorTickCount(), 0) + 1);
      } else {
         var2 = var1.getMajorTickUnit();
      }

      return var1.getBlockIncrement() > 0.0 && var1.getBlockIncrement() < var2 ? var2 : var1.getBlockIncrement();
   }

   static {
      SLIDER_BINDINGS.add((new KeyBinding(KeyCode.F4, "TraverseDebug")).alt().ctrl().shift());
      SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.LEFT, "DecrementValue"));
      SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.KP_LEFT, "DecrementValue"));
      SLIDER_BINDINGS.add((new SliderKeyBinding(KeyCode.UP, "IncrementValue")).vertical());
      SLIDER_BINDINGS.add((new SliderKeyBinding(KeyCode.KP_UP, "IncrementValue")).vertical());
      SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.RIGHT, "IncrementValue"));
      SLIDER_BINDINGS.add(new SliderKeyBinding(KeyCode.KP_RIGHT, "IncrementValue"));
      SLIDER_BINDINGS.add((new SliderKeyBinding(KeyCode.DOWN, "DecrementValue")).vertical());
      SLIDER_BINDINGS.add((new SliderKeyBinding(KeyCode.KP_DOWN, "DecrementValue")).vertical());
      SLIDER_BINDINGS.add(new KeyBinding(KeyCode.HOME, KeyEvent.KEY_RELEASED, "Home"));
      SLIDER_BINDINGS.add(new KeyBinding(KeyCode.END, KeyEvent.KEY_RELEASED, "End"));
   }

   public static class SliderKeyBinding extends OrientedKeyBinding {
      public SliderKeyBinding(KeyCode var1, String var2) {
         super(var1, var2);
      }

      public SliderKeyBinding(KeyCode var1, EventType var2, String var3) {
         super(var1, var2, var3);
      }

      public boolean getVertical(Control var1) {
         return ((Slider)var1).getOrientation() == Orientation.VERTICAL;
      }
   }
}
