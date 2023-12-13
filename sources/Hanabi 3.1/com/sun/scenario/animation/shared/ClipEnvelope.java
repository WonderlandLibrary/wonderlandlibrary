package com.sun.scenario.animation.shared;

import com.sun.javafx.animation.TickCalculation;
import javafx.animation.Animation;
import javafx.util.Duration;

public abstract class ClipEnvelope {
   protected static final long INDEFINITE = Long.MAX_VALUE;
   protected static final double EPSILON = 1.0E-12;
   protected Animation animation;
   protected double rate = 1.0;
   protected long cycleTicks = 0L;
   protected long deltaTicks = 0L;
   protected long ticks = 0L;
   protected double currentRate;
   protected boolean inTimePulse;
   protected boolean aborted;

   protected ClipEnvelope(Animation var1) {
      this.currentRate = this.rate;
      this.inTimePulse = false;
      this.aborted = false;
      this.animation = var1;
      if (var1 != null) {
         Duration var2 = var1.getCycleDuration();
         this.cycleTicks = TickCalculation.fromDuration(var2);
         this.rate = var1.getRate();
      }

   }

   public static ClipEnvelope create(Animation var0) {
      if (var0.getCycleCount() != 1 && !var0.getCycleDuration().isIndefinite()) {
         return (ClipEnvelope)(var0.getCycleCount() == -1 ? new InfiniteClipEnvelope(var0) : new FiniteClipEnvelope(var0));
      } else {
         return new SingleLoopClipEnvelope(var0);
      }
   }

   public abstract ClipEnvelope setCycleDuration(Duration var1);

   public abstract void setRate(double var1);

   public abstract void setAutoReverse(boolean var1);

   public abstract ClipEnvelope setCycleCount(int var1);

   protected void updateCycleTicks(Duration var1) {
      this.cycleTicks = TickCalculation.fromDuration(var1);
   }

   public boolean wasSynched() {
      return this.cycleTicks != 0L;
   }

   public void start() {
      this.setCurrentRate(this.calculateCurrentRate());
      this.deltaTicks = this.ticks;
   }

   public abstract void timePulse(long var1);

   public abstract void jumpTo(long var1);

   public void abortCurrentPulse() {
      if (this.inTimePulse) {
         this.aborted = true;
         this.inTimePulse = false;
      }

   }

   protected abstract double calculateCurrentRate();

   protected void setCurrentRate(double var1) {
      this.currentRate = var1;
      AnimationAccessor.getDefault().setCurrentRate(this.animation, var1);
   }

   protected static long checkBounds(long var0, long var2) {
      return Math.max(0L, Math.min(var0, var2));
   }

   public double getCurrentRate() {
      return this.currentRate;
   }
}
