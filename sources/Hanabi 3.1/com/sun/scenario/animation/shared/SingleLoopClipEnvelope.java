package com.sun.scenario.animation.shared;

import javafx.animation.Animation;
import javafx.util.Duration;

public class SingleLoopClipEnvelope extends ClipEnvelope {
   private int cycleCount;

   public void setRate(double var1) {
      Animation.Status var3 = this.animation.getStatus();
      if (var3 != Animation.Status.STOPPED) {
         if (var3 == Animation.Status.RUNNING) {
            this.setCurrentRate(Math.abs(this.currentRate - this.rate) < 1.0E-12 ? var1 : -var1);
         }

         this.deltaTicks = this.ticks - Math.round((double)(this.ticks - this.deltaTicks) * var1 / this.rate);
         this.abortCurrentPulse();
      }

      this.rate = var1;
   }

   public void setAutoReverse(boolean var1) {
   }

   protected double calculateCurrentRate() {
      return this.rate;
   }

   protected SingleLoopClipEnvelope(Animation var1) {
      super(var1);
      if (var1 != null) {
         this.cycleCount = var1.getCycleCount();
      }

   }

   public boolean wasSynched() {
      return super.wasSynched() && this.cycleCount != 0;
   }

   public ClipEnvelope setCycleDuration(Duration var1) {
      if (this.cycleCount != 1 && !var1.isIndefinite()) {
         return create(this.animation);
      } else {
         this.updateCycleTicks(var1);
         return this;
      }
   }

   public ClipEnvelope setCycleCount(int var1) {
      if (var1 != 1 && this.cycleTicks != Long.MAX_VALUE) {
         return create(this.animation);
      } else {
         this.cycleCount = var1;
         return this;
      }
   }

   public void timePulse(long var1) {
      if (this.cycleTicks != 0L) {
         this.aborted = false;
         this.inTimePulse = true;

         try {
            this.ticks = ClipEnvelope.checkBounds(this.deltaTicks + Math.round((double)var1 * this.currentRate), this.cycleTicks);
            AnimationAccessor.getDefault().playTo(this.animation, this.ticks, this.cycleTicks);
            boolean var3 = this.currentRate > 0.0 ? this.ticks == this.cycleTicks : this.ticks == 0L;
            if (var3 && !this.aborted) {
               AnimationAccessor.getDefault().finished(this.animation);
            }
         } finally {
            this.inTimePulse = false;
         }

      }
   }

   public void jumpTo(long var1) {
      if (this.cycleTicks != 0L) {
         long var3 = ClipEnvelope.checkBounds(var1, this.cycleTicks);
         this.deltaTicks += var3 - this.ticks;
         this.ticks = var3;
         AnimationAccessor.getDefault().jumpTo(this.animation, var3, this.cycleTicks, false);
         this.abortCurrentPulse();
      }
   }
}
