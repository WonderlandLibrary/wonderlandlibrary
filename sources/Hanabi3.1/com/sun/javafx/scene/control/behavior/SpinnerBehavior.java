package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class SpinnerBehavior extends BehaviorBase {
   private static final double INITIAL_DURATION_MS = 750.0;
   private final int STEP_AMOUNT = 1;
   private boolean isIncrementing = false;
   private Timeline timeline;
   final EventHandler spinningKeyFrameEventHandler = (var1x) -> {
      SpinnerValueFactory var2 = ((Spinner)this.getControl()).getValueFactory();
      if (var2 != null) {
         if (this.isIncrementing) {
            this.increment(1);
         } else {
            this.decrement(1);
         }

      }
   };
   private boolean keyDown;
   protected static final List SPINNER_BINDINGS = new ArrayList();

   public SpinnerBehavior(Spinner var1) {
      super(var1, SPINNER_BINDINGS);
   }

   protected void callAction(String var1) {
      boolean var2 = this.arrowsAreVertical();
      switch (var1) {
         case "increment-up":
            if (var2) {
               this.increment(1);
            } else {
               this.traverseUp();
            }
            break;
         case "increment-right":
            if (!var2) {
               this.increment(1);
            } else {
               this.traverseRight();
            }
            break;
         case "decrement-down":
            if (var2) {
               this.decrement(1);
            } else {
               this.traverseDown();
            }
            break;
         case "decrement-left":
            if (!var2) {
               this.decrement(1);
            } else {
               this.traverseLeft();
            }
            break;
         default:
            super.callAction(var1);
      }

   }

   public void increment(int var1) {
      ((Spinner)this.getControl()).increment(var1);
   }

   public void decrement(int var1) {
      ((Spinner)this.getControl()).decrement(var1);
   }

   public void startSpinning(boolean var1) {
      this.isIncrementing = var1;
      if (this.timeline != null) {
         this.timeline.stop();
      }

      this.timeline = new Timeline();
      this.timeline.setCycleCount(-1);
      KeyFrame var2 = new KeyFrame(Duration.millis(750.0), this.spinningKeyFrameEventHandler, new KeyValue[0]);
      this.timeline.getKeyFrames().setAll((Object[])(var2));
      this.timeline.playFromStart();
      this.timeline.play();
      this.spinningKeyFrameEventHandler.handle((Event)null);
   }

   public void stopSpinning() {
      if (this.timeline != null) {
         this.timeline.stop();
      }

   }

   private boolean arrowsAreVertical() {
      ObservableList var1 = ((Spinner)this.getControl()).getStyleClass();
      return !var1.contains("arrows-on-left-horizontal") && !var1.contains("arrows-on-right-horizontal") && !var1.contains("split-arrows-horizontal");
   }

   static {
      SPINNER_BINDINGS.add(new KeyBinding(KeyCode.UP, "increment-up"));
      SPINNER_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "increment-right"));
      SPINNER_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "decrement-left"));
      SPINNER_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "decrement-down"));
   }
}
