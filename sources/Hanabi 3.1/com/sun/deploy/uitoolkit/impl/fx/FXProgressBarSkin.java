package com.sun.deploy.uitoolkit.impl.fx;

import com.sun.javafx.scene.control.skin.ProgressBarSkin;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class FXProgressBarSkin extends ProgressBarSkin {
   Rectangle topGradient;
   Rectangle bottomGradient;
   Rectangle verticalLines;
   double gradientMargin = 4.0;
   double gradientRadius = 0.55;
   double gradientTweak = 1.4;

   public FXProgressBarSkin(ProgressBar var1) {
      super(var1);
      this.topGradient = new Rectangle(0.0, 0.0, new RadialGradient(0.05, 0.0, 0.5, 0.0, this.gradientRadius, true, CycleMethod.NO_CYCLE, new Stop[]{new Stop(0.0, Color.rgb(255, 255, 255, 0.82)), new Stop(0.13, Color.rgb(255, 255, 255, 0.82)), new Stop(0.98, Color.rgb(255, 255, 255, 0.0))}));
      this.bottomGradient = new Rectangle(0.0, 0.0, new RadialGradient(0.05, 0.0, 0.5, 1.0, this.gradientRadius, true, CycleMethod.NO_CYCLE, new Stop[]{new Stop(0.0, Color.rgb(255, 255, 255, 0.82)), new Stop(0.13, Color.rgb(255, 255, 255, 0.82)), new Stop(0.98, Color.rgb(255, 255, 255, 0.0))}));
      this.topGradient.setManaged(false);
      this.bottomGradient.setManaged(false);
      ((StackPane)this.getChildren().get(1)).getChildren().addAll(this.topGradient, this.bottomGradient);
      this.verticalLines = new Rectangle(0.0, 0.0, new LinearGradient(0.0, 0.0, 14.0, 0.0, false, CycleMethod.REPEAT, new Stop[]{new Stop(0.0, Color.TRANSPARENT), new Stop(0.93, Color.TRANSPARENT), new Stop(0.93, Color.rgb(184, 184, 184, 0.2)), new Stop(1.0, Color.rgb(184, 184, 184, 0.2))}));
      this.verticalLines.setManaged(false);
      this.getChildren().add(this.verticalLines);
   }

   protected void layoutChildren(double var1, double var3, double var5, double var7) {
      super.layoutChildren(var1, var3, var5, var7);
      if (!((ProgressIndicator)this.getSkinnable()).isIndeterminate()) {
         StackPane var9 = (StackPane)this.getChildren().get(0);
         StackPane var10 = (StackPane)this.getChildren().get(1);
         if (!var10.getChildren().contains(this.topGradient)) {
            var10.getChildren().add(this.topGradient);
         }

         if (!var10.getChildren().contains(this.bottomGradient)) {
            var10.getChildren().add(this.bottomGradient);
         }

         if (!this.getChildren().contains(this.verticalLines)) {
            this.getChildren().add(this.verticalLines);
         }

         double var11 = Math.floor(var5 / 14.0) * 14.0 / var5;
         double var13 = var10.getWidth() * var11;
         double var15 = var10.getHeight();
         var9.resize(var9.getWidth() * var11, var9.getHeight());
         var10.resize(var13, var15);
         this.topGradient.setX(var1 + this.gradientMargin);
         this.topGradient.setY(var3 + 0.5);
         this.topGradient.setWidth(var13 - 2.0 * this.gradientMargin);
         this.topGradient.setHeight(var15 * 0.3 / this.gradientRadius * this.gradientTweak);
         this.bottomGradient.setX(var1 + this.gradientMargin);
         this.bottomGradient.setWidth(var13 - 2.0 * this.gradientMargin);
         double var17 = var15 * 0.21 / this.gradientRadius * this.gradientTweak;
         this.bottomGradient.setY(var15 - var17 - 0.5);
         this.bottomGradient.setHeight(var17);
         this.verticalLines.setX(var1);
         this.verticalLines.setY(var3);
         this.verticalLines.setWidth(var5 * var11);
         this.verticalLines.setHeight(var7);
      }
   }
}
