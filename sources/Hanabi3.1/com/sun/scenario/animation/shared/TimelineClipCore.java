package com.sun.scenario.animation.shared;

import com.sun.javafx.animation.TickCalculation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.util.Duration;

public class TimelineClipCore {
   private static final int UNDEFINED_KEYFRAME = -1;
   private static final Comparator KEY_FRAME_COMPARATOR = (var0, var1) -> {
      return var0.getTime().compareTo(var1.getTime());
   };
   Timeline timeline;
   private KeyFrame[] keyFrames = new KeyFrame[0];
   private long[] keyFrameTicks = new long[0];
   private boolean canSkipFrames = true;
   private ClipInterpolator clipInterpolator;
   private boolean aborted = false;
   private int lastKF = -1;
   private long curTicks = 0L;

   public TimelineClipCore(Timeline var1) {
      this.timeline = var1;
      this.clipInterpolator = ClipInterpolator.create(this.keyFrames, this.keyFrameTicks);
   }

   public Duration setKeyFrames(Collection var1) {
      int var2 = var1.size();
      KeyFrame[] var3 = new KeyFrame[var2];
      var1.toArray(var3);
      Arrays.sort(var3, KEY_FRAME_COMPARATOR);
      this.canSkipFrames = true;
      this.keyFrames = var3;
      this.keyFrameTicks = new long[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         this.keyFrameTicks[var4] = TickCalculation.fromDuration(this.keyFrames[var4].getTime());
         if (this.canSkipFrames && this.keyFrames[var4].getOnFinished() != null) {
            this.canSkipFrames = false;
         }
      }

      this.clipInterpolator = this.clipInterpolator.setKeyFrames(var3, this.keyFrameTicks);
      return var2 == 0 ? Duration.ZERO : var3[var2 - 1].getTime();
   }

   public void notifyCurrentRateChanged() {
      if (this.timeline.getStatus() != Animation.Status.RUNNING) {
         this.clearLastKeyFrame();
      }

   }

   public void abort() {
      this.aborted = true;
   }

   private void clearLastKeyFrame() {
      this.lastKF = -1;
   }

   public void jumpTo(long var1, boolean var3) {
      this.lastKF = -1;
      this.curTicks = var1;
      if (this.timeline.getStatus() != Animation.Status.STOPPED || var3) {
         if (var3) {
            this.clipInterpolator.validate(false);
         }

         this.clipInterpolator.interpolate(var1);
      }

   }

   public void start(boolean var1) {
      this.clearLastKeyFrame();
      this.clipInterpolator.validate(var1);
      if (this.curTicks > 0L) {
         this.clipInterpolator.interpolate(this.curTicks);
      }

   }

   public void playTo(long var1) {
      if (this.canSkipFrames) {
         this.clearLastKeyFrame();
         this.setTime(var1);
         this.clipInterpolator.interpolate(var1);
      } else {
         this.aborted = false;
         boolean var3 = this.curTicks <= var1;
         int var4;
         int var5;
         if (var3) {
            var4 = this.lastKF == -1 ? 0 : (this.keyFrameTicks[this.lastKF] <= this.curTicks ? this.lastKF + 1 : this.lastKF);
            var5 = this.keyFrames.length;

            for(int var6 = var4; var6 < var5; ++var6) {
               long var7 = this.keyFrameTicks[var6];
               if (var7 > var1) {
                  this.lastKF = var6 - 1;
                  break;
               }

               if (var7 >= this.curTicks) {
                  this.visitKeyFrame(var6, var7);
                  if (this.aborted) {
                     break;
                  }
               }
            }
         } else {
            var4 = this.lastKF == -1 ? this.keyFrames.length - 1 : (this.keyFrameTicks[this.lastKF] >= this.curTicks ? this.lastKF - 1 : this.lastKF);

            for(var5 = var4; var5 >= 0; --var5) {
               long var9 = this.keyFrameTicks[var5];
               if (var9 < var1) {
                  this.lastKF = var5 + 1;
                  break;
               }

               if (var9 <= this.curTicks) {
                  this.visitKeyFrame(var5, var9);
                  if (this.aborted) {
                     break;
                  }
               }
            }
         }

         if (!this.aborted && (this.lastKF == -1 || this.keyFrameTicks[this.lastKF] != var1 || this.keyFrames[this.lastKF].getOnFinished() == null)) {
            this.setTime(var1);
            this.clipInterpolator.interpolate(var1);
         }

      }
   }

   private void setTime(long var1) {
      this.curTicks = var1;
      AnimationAccessor.getDefault().setCurrentTicks(this.timeline, var1);
   }

   private void visitKeyFrame(int var1, long var2) {
      if (var1 != this.lastKF) {
         this.lastKF = var1;
         KeyFrame var4 = this.keyFrames[var1];
         EventHandler var5 = var4.getOnFinished();
         if (var5 != null) {
            this.setTime(var2);
            this.clipInterpolator.interpolate(var2);

            try {
               var5.handle(new ActionEvent(var4, (EventTarget)null));
            } catch (Throwable var7) {
               Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), var7);
            }
         }
      }

   }
}
