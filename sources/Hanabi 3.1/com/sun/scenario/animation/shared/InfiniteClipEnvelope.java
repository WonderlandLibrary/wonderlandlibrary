package com.sun.scenario.animation.shared;

import javafx.animation.Animation;
import javafx.util.Duration;

public class InfiniteClipEnvelope extends ClipEnvelope {
   private boolean autoReverse;
   private long pos;

   protected InfiniteClipEnvelope(Animation var1) {
      super(var1);
      if (var1 != null) {
         this.autoReverse = var1.isAutoReverse();
      }

   }

   public void setAutoReverse(boolean var1) {
      this.autoReverse = var1;
   }

   protected double calculateCurrentRate() {
      return !this.autoReverse ? this.rate : (this.ticks % (2L * this.cycleTicks) < this.cycleTicks ? this.rate : -this.rate);
   }

   public ClipEnvelope setCycleDuration(Duration var1) {
      if (var1.isIndefinite()) {
         return create(this.animation);
      } else {
         this.updateCycleTicks(var1);
         return this;
      }
   }

   public ClipEnvelope setCycleCount(int var1) {
      return (ClipEnvelope)(var1 != -1 ? create(this.animation) : this);
   }

   public void setRate(double var1) {
      Animation.Status var3 = this.animation.getStatus();
      if (var3 != Animation.Status.STOPPED) {
         if (var3 == Animation.Status.RUNNING) {
            this.setCurrentRate(Math.abs(this.currentRate - this.rate) < 1.0E-12 ? var1 : -var1);
         }

         this.deltaTicks = this.ticks - Math.round((double)(this.ticks - this.deltaTicks) * Math.abs(var1 / this.rate));
         if (var1 * this.rate < 0.0) {
            long var4 = 2L * this.cycleTicks - this.pos;
            this.deltaTicks += var4;
            this.ticks += var4;
         }

         this.abortCurrentPulse();
      }

      this.rate = var1;
   }

   public void timePulse(long var1) {
      if (this.cycleTicks != 0L) {
         this.aborted = false;
         this.inTimePulse = true;

         try {
            long var3 = this.ticks;
            this.ticks = Math.max(0L, this.deltaTicks + Math.round((double)var1 * Math.abs(this.rate)));
            long var5 = this.ticks - var3;
            if (var5 != 0L) {
               for(long var7 = this.currentRate > 0.0 ? this.cycleTicks - this.pos : this.pos; var5 >= var7; var7 = this.cycleTicks) {
                  if (var7 > 0L) {
                     this.pos = this.currentRate > 0.0 ? this.cycleTicks : 0L;
                     var5 -= var7;
                     AnimationAccessor.getDefault().playTo(this.animation, this.pos, this.cycleTicks);
                     if (this.aborted) {
                        return;
                     }
                  }

                  if (this.autoReverse) {
                     this.setCurrentRate(-this.currentRate);
                  } else {
                     this.pos = this.currentRate > 0.0 ? 0L : this.cycleTicks;
                     AnimationAccessor.getDefault().jumpTo(this.animation, this.pos, this.cycleTicks, false);
                  }
               }

               if (var5 > 0L) {
                  this.pos += this.currentRate > 0.0 ? var5 : -var5;
                  AnimationAccessor.getDefault().playTo(this.animation, this.pos, this.cycleTicks);
               }

            }
         } finally {
            this.inTimePulse = false;
         }
      }
   }

   public void jumpTo(long var1) {
      if (this.cycleTicks != 0L) {
         long var3 = this.ticks;
         this.ticks = Math.max(0L, var1) % (2L * this.cycleTicks);
         long var5 = this.ticks - var3;
         if (var5 != 0L) {
            this.deltaTicks += var5;
            if (this.autoReverse) {
               if (this.ticks > this.cycleTicks) {
                  this.pos = 2L * this.cycleTicks - this.ticks;
                  if (this.animation.getStatus() == Animation.Status.RUNNING) {
                     this.setCurrentRate(-this.rate);
                  }
               } else {
                  this.pos = this.ticks;
                  if (this.animation.getStatus() == Animation.Status.RUNNING) {
                     this.setCurrentRate(this.rate);
                  }
               }
            } else {
               this.pos = this.ticks % this.cycleTicks;
               if (this.pos == 0L) {
                  this.pos = this.ticks;
               }
            }

            AnimationAccessor.getDefault().jumpTo(this.animation, this.pos, this.cycleTicks, false);
            this.abortCurrentPulse();
         }

      }
   }
}
