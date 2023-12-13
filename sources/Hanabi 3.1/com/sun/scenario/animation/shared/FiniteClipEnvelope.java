package com.sun.scenario.animation.shared;

import javafx.animation.Animation;
import javafx.util.Duration;

public class FiniteClipEnvelope extends ClipEnvelope {
   private boolean autoReverse;
   private int cycleCount;
   private long totalTicks;
   private long pos;

   protected FiniteClipEnvelope(Animation var1) {
      super(var1);
      if (var1 != null) {
         this.autoReverse = var1.isAutoReverse();
         this.cycleCount = var1.getCycleCount();
      }

      this.updateTotalTicks();
   }

   public void setAutoReverse(boolean var1) {
      this.autoReverse = var1;
   }

   protected double calculateCurrentRate() {
      return !this.autoReverse ? this.rate : (this.ticks % (2L * this.cycleTicks) < this.cycleTicks == this.rate > 0.0 ? this.rate : -this.rate);
   }

   public ClipEnvelope setCycleDuration(Duration var1) {
      if (var1.isIndefinite()) {
         return create(this.animation);
      } else {
         this.updateCycleTicks(var1);
         this.updateTotalTicks();
         return this;
      }
   }

   public ClipEnvelope setCycleCount(int var1) {
      if (var1 != 1 && var1 != -1) {
         this.cycleCount = var1;
         this.updateTotalTicks();
         return this;
      } else {
         return create(this.animation);
      }
   }

   public void setRate(double var1) {
      boolean var3 = var1 * this.rate < 0.0;
      long var4 = var3 ? this.totalTicks - this.ticks : this.ticks;
      Animation.Status var6 = this.animation.getStatus();
      if (var6 != Animation.Status.STOPPED) {
         if (var6 == Animation.Status.RUNNING) {
            this.setCurrentRate(Math.abs(this.currentRate - this.rate) < 1.0E-12 ? var1 : -var1);
         }

         this.deltaTicks = var4 - Math.round((double)(this.ticks - this.deltaTicks) * Math.abs(var1 / this.rate));
         this.abortCurrentPulse();
      }

      this.ticks = var4;
      this.rate = var1;
   }

   private void updateTotalTicks() {
      this.totalTicks = (long)this.cycleCount * this.cycleTicks;
   }

   public void timePulse(long var1) {
      if (this.cycleTicks != 0L) {
         this.aborted = false;
         this.inTimePulse = true;

         try {
            long var3 = this.ticks;
            this.ticks = ClipEnvelope.checkBounds(this.deltaTicks + Math.round((double)var1 * Math.abs(this.rate)), this.totalTicks);
            boolean var5 = this.ticks >= this.totalTicks;
            long var6 = this.ticks - var3;
            if (var6 == 0L) {
               return;
            }

            for(long var8 = this.currentRate > 0.0 ? this.cycleTicks - this.pos : this.pos; var6 >= var8; var8 = this.cycleTicks) {
               if (var8 > 0L) {
                  this.pos = this.currentRate > 0.0 ? this.cycleTicks : 0L;
                  var6 -= var8;
                  AnimationAccessor.getDefault().playTo(this.animation, this.pos, this.cycleTicks);
                  if (this.aborted) {
                     return;
                  }
               }

               if (!var5 || var6 > 0L) {
                  if (this.autoReverse) {
                     this.setCurrentRate(-this.currentRate);
                  } else {
                     this.pos = this.currentRate > 0.0 ? 0L : this.cycleTicks;
                     AnimationAccessor.getDefault().jumpTo(this.animation, this.pos, this.cycleTicks, false);
                  }
               }
            }

            if (var6 > 0L && !var5) {
               this.pos += this.currentRate > 0.0 ? var6 : -var6;
               AnimationAccessor.getDefault().playTo(this.animation, this.pos, this.cycleTicks);
            }

            if (var5 && !this.aborted) {
               AnimationAccessor.getDefault().finished(this.animation);
            }
         } finally {
            this.inTimePulse = false;
         }

      }
   }

   public void jumpTo(long var1) {
      if (this.cycleTicks != 0L) {
         long var3 = this.ticks;
         if (this.rate < 0.0) {
            var1 = this.totalTicks - var1;
         }

         this.ticks = ClipEnvelope.checkBounds(var1, this.totalTicks);
         long var5 = this.ticks - var3;
         if (var5 != 0L) {
            this.deltaTicks += var5;
            if (this.autoReverse) {
               boolean var7 = this.ticks % (2L * this.cycleTicks) < this.cycleTicks;
               if (var7 == this.rate > 0.0) {
                  this.pos = this.ticks % this.cycleTicks;
                  if (this.animation.getStatus() == Animation.Status.RUNNING) {
                     this.setCurrentRate(Math.abs(this.rate));
                  }
               } else {
                  this.pos = this.cycleTicks - this.ticks % this.cycleTicks;
                  if (this.animation.getStatus() == Animation.Status.RUNNING) {
                     this.setCurrentRate(-Math.abs(this.rate));
                  }
               }
            } else {
               this.pos = this.ticks % this.cycleTicks;
               if (this.rate < 0.0) {
                  this.pos = this.cycleTicks - this.pos;
               }

               if (this.pos == 0L && this.ticks > 0L) {
                  this.pos = this.cycleTicks;
               }
            }

            AnimationAccessor.getDefault().jumpTo(this.animation, this.pos, this.cycleTicks, false);
            this.abortCurrentPulse();
         }

      }
   }
}
