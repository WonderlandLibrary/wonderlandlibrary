package com.sun.javafx.charts;

import java.util.HashMap;
import java.util.Map;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.chart.Axis;

public final class ChartLayoutAnimator extends AnimationTimer implements EventHandler {
   private Parent nodeToLayout;
   private final Map activeTimeLines = new HashMap();
   private final boolean isAxis;

   public ChartLayoutAnimator(Parent var1) {
      this.nodeToLayout = var1;
      this.isAxis = var1 instanceof Axis;
   }

   public void handle(long var1) {
      if (this.isAxis) {
         ((Axis)this.nodeToLayout).requestAxisLayout();
      } else {
         this.nodeToLayout.requestLayout();
      }

   }

   public void handle(ActionEvent var1) {
      this.activeTimeLines.remove(var1.getSource());
      if (this.activeTimeLines.isEmpty()) {
         this.stop();
      }

      this.handle(0L);
   }

   public void stop(Object var1) {
      Animation var2 = (Animation)this.activeTimeLines.remove(var1);
      if (var2 != null) {
         var2.stop();
      }

      if (this.activeTimeLines.isEmpty()) {
         this.stop();
      }

   }

   public Object animate(KeyFrame... var1) {
      Timeline var2 = new Timeline();
      var2.setAutoReverse(false);
      var2.setCycleCount(1);
      var2.getKeyFrames().addAll(var1);
      var2.setOnFinished(this);
      if (this.activeTimeLines.isEmpty()) {
         this.start();
      }

      this.activeTimeLines.put(var2, var2);
      var2.play();
      return var2;
   }

   public Object animate(Animation var1) {
      SequentialTransition var2 = new SequentialTransition();
      var2.getChildren().add(var1);
      var2.setOnFinished(this);
      if (this.activeTimeLines.isEmpty()) {
         this.start();
      }

      this.activeTimeLines.put(var2, var2);
      var2.play();
      return var2;
   }
}
