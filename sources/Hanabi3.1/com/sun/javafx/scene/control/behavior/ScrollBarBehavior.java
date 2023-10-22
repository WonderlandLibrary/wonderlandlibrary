package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class ScrollBarBehavior extends BehaviorBase {
   protected static final List SCROLL_BAR_BINDINGS = new ArrayList();
   Timeline timeline;

   public ScrollBarBehavior(ScrollBar var1) {
      super(var1, SCROLL_BAR_BINDINGS);
   }

   void home() {
      ((ScrollBar)this.getControl()).setValue(((ScrollBar)this.getControl()).getMin());
   }

   void decrementValue() {
      ((ScrollBar)this.getControl()).adjustValue(0.0);
   }

   void end() {
      ((ScrollBar)this.getControl()).setValue(((ScrollBar)this.getControl()).getMax());
   }

   void incrementValue() {
      ((ScrollBar)this.getControl()).adjustValue(1.0);
   }

   protected String matchActionForEvent(KeyEvent var1) {
      String var2 = super.matchActionForEvent(var1);
      if (var2 != null) {
         if (var1.getCode() != KeyCode.LEFT && var1.getCode() != KeyCode.KP_LEFT) {
            if ((var1.getCode() == KeyCode.RIGHT || var1.getCode() == KeyCode.KP_RIGHT) && ((ScrollBar)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
               var2 = ((ScrollBar)this.getControl()).getOrientation() == Orientation.HORIZONTAL ? "DecrementValue" : "IncrementValue";
            }
         } else if (((ScrollBar)this.getControl()).getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
            var2 = ((ScrollBar)this.getControl()).getOrientation() == Orientation.HORIZONTAL ? "IncrementValue" : "DecrementValue";
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

      super.callAction(var1);
   }

   public void trackPress(double var1) {
      if (this.timeline == null) {
         ScrollBar var3 = (ScrollBar)this.getControl();
         if (!var3.isFocused() && var3.isFocusTraversable()) {
            var3.requestFocus();
         }

         boolean var6 = var1 > (var3.getValue() - var3.getMin()) / (var3.getMax() - var3.getMin());
         this.timeline = new Timeline();
         this.timeline.setCycleCount(-1);
         EventHandler var7 = (var5) -> {
            boolean var6x = var1 > (var3.getValue() - var3.getMin()) / (var3.getMax() - var3.getMin());
            if (var6 == var6x) {
               var3.adjustValue(var1);
            } else {
               this.stopTimeline();
            }

         };
         KeyFrame var8 = new KeyFrame(Duration.millis(200.0), var7, new KeyValue[0]);
         this.timeline.getKeyFrames().add(var8);
         this.timeline.play();
         var7.handle((Event)null);
      }
   }

   public void trackRelease() {
      this.stopTimeline();
   }

   public void decButtonPressed() {
      ScrollBar var1 = (ScrollBar)this.getControl();
      if (!var1.isFocused() && var1.isFocusTraversable()) {
         var1.requestFocus();
      }

      this.stopTimeline();
      this.timeline = new Timeline();
      this.timeline.setCycleCount(-1);
      EventHandler var2 = (var2x) -> {
         if (var1.getValue() > var1.getMin()) {
            var1.decrement();
         } else {
            this.stopTimeline();
         }

      };
      KeyFrame var3 = new KeyFrame(Duration.millis(200.0), var2, new KeyValue[0]);
      this.timeline.getKeyFrames().add(var3);
      this.timeline.play();
      var2.handle((Event)null);
   }

   public void decButtonReleased() {
      this.stopTimeline();
   }

   public void incButtonPressed() {
      ScrollBar var1 = (ScrollBar)this.getControl();
      if (!var1.isFocused() && var1.isFocusTraversable()) {
         var1.requestFocus();
      }

      this.stopTimeline();
      this.timeline = new Timeline();
      this.timeline.setCycleCount(-1);
      EventHandler var2 = (var2x) -> {
         if (var1.getValue() < var1.getMax()) {
            var1.increment();
         } else {
            this.stopTimeline();
         }

      };
      KeyFrame var3 = new KeyFrame(Duration.millis(200.0), var2, new KeyValue[0]);
      this.timeline.getKeyFrames().add(var3);
      this.timeline.play();
      var2.handle((Event)null);
   }

   public void incButtonReleased() {
      this.stopTimeline();
   }

   public void thumbDragged(double var1) {
      ScrollBar var3 = (ScrollBar)this.getControl();
      this.stopTimeline();
      if (!var3.isFocused() && var3.isFocusTraversable()) {
         var3.requestFocus();
      }

      double var4 = var1 * (var3.getMax() - var3.getMin()) + var3.getMin();
      if (!Double.isNaN(var4)) {
         var3.setValue(Utils.clamp(var3.getMin(), var4, var3.getMax()));
      }

   }

   private void stopTimeline() {
      if (this.timeline != null) {
         this.timeline.stop();
         this.timeline = null;
      }

   }

   static {
      SCROLL_BAR_BINDINGS.add((new KeyBinding(KeyCode.F4, "TraverseDebug")).alt().ctrl().shift());
      SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.LEFT, "DecrementValue"));
      SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.KP_LEFT, "DecrementValue"));
      SCROLL_BAR_BINDINGS.add((new ScrollBarKeyBinding(KeyCode.UP, "DecrementValue")).vertical());
      SCROLL_BAR_BINDINGS.add((new ScrollBarKeyBinding(KeyCode.KP_UP, "DecrementValue")).vertical());
      SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.RIGHT, "IncrementValue"));
      SCROLL_BAR_BINDINGS.add(new ScrollBarKeyBinding(KeyCode.KP_RIGHT, "IncrementValue"));
      SCROLL_BAR_BINDINGS.add((new ScrollBarKeyBinding(KeyCode.DOWN, "IncrementValue")).vertical());
      SCROLL_BAR_BINDINGS.add((new ScrollBarKeyBinding(KeyCode.KP_DOWN, "IncrementValue")).vertical());
      SCROLL_BAR_BINDINGS.add(new KeyBinding(KeyCode.HOME, KeyEvent.KEY_RELEASED, "Home"));
      SCROLL_BAR_BINDINGS.add(new KeyBinding(KeyCode.END, KeyEvent.KEY_RELEASED, "End"));
   }

   public static class ScrollBarKeyBinding extends OrientedKeyBinding {
      public ScrollBarKeyBinding(KeyCode var1, String var2) {
         super(var1, var2);
      }

      public ScrollBarKeyBinding(KeyCode var1, EventType var2, String var3) {
         super(var1, var2, var3);
      }

      public boolean getVertical(Control var1) {
         return ((ScrollBar)var1).getOrientation() == Orientation.VERTICAL;
      }
   }
}
