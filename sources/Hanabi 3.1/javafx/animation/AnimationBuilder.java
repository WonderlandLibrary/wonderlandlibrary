package javafx.animation;

import javafx.event.EventHandler;
import javafx.util.Duration;

/** @deprecated */
@Deprecated
public abstract class AnimationBuilder {
   private int __set;
   private boolean autoReverse;
   private int cycleCount;
   private Duration delay;
   private EventHandler onFinished;
   private double rate;
   private double targetFramerate;

   protected AnimationBuilder() {
   }

   public void applyTo(Animation var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAutoReverse(this.autoReverse);
      }

      if ((var2 & 2) != 0) {
         var1.setCycleCount(this.cycleCount);
      }

      if ((var2 & 4) != 0) {
         var1.setDelay(this.delay);
      }

      if ((var2 & 8) != 0) {
         var1.setOnFinished(this.onFinished);
      }

      if ((var2 & 16) != 0) {
         var1.setRate(this.rate);
      }

   }

   public AnimationBuilder autoReverse(boolean var1) {
      this.autoReverse = var1;
      this.__set |= 1;
      return this;
   }

   public AnimationBuilder cycleCount(int var1) {
      this.cycleCount = var1;
      this.__set |= 2;
      return this;
   }

   public AnimationBuilder delay(Duration var1) {
      this.delay = var1;
      this.__set |= 4;
      return this;
   }

   public AnimationBuilder onFinished(EventHandler var1) {
      this.onFinished = var1;
      this.__set |= 8;
      return this;
   }

   public AnimationBuilder rate(double var1) {
      this.rate = var1;
      this.__set |= 16;
      return this;
   }

   public AnimationBuilder targetFramerate(double var1) {
      this.targetFramerate = var1;
      return this;
   }
}
