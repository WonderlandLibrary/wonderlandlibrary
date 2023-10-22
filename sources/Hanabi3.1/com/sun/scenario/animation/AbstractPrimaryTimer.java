package com.sun.scenario.animation;

import com.sun.javafx.animation.TickCalculation;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.Settings;
import com.sun.scenario.animation.shared.PulseReceiver;
import com.sun.scenario.animation.shared.TimerReceiver;
import java.util.Arrays;
import javafx.util.Callback;

public abstract class AbstractPrimaryTimer {
   protected static final String FULLSPEED_PROP = "javafx.animation.fullspeed";
   private static boolean fullspeed = Settings.getBoolean("javafx.animation.fullspeed");
   protected static final String ADAPTIVE_PULSE_PROP = "com.sun.scenario.animation.adaptivepulse";
   private static boolean useAdaptivePulse = Settings.getBoolean("com.sun.scenario.animation.adaptivepulse");
   protected static final String PULSE_PROP = "javafx.animation.pulse";
   protected static final String FRAMERATE_PROP = "javafx.animation.framerate";
   protected static final String FIXED_PULSE_LENGTH_PROP = "com.sun.scenario.animation.fixed.pulse.length";
   protected static final String ANIMATION_MBEAN_ENABLED = "com.sun.scenario.animation.AnimationMBean.enabled";
   protected static final boolean enableAnimationMBean = false;
   private final int PULSE_DURATION_NS = this.getPulseDuration(1000000000);
   private final int PULSE_DURATION_TICKS = this.getPulseDuration((int)TickCalculation.fromMillis(1000.0));
   private static Callback pcl = (var0x) -> {
      switch (var0x) {
         case "javafx.animation.fullspeed":
            fullspeed = Settings.getBoolean("javafx.animation.fullspeed");
            break;
         case "com.sun.scenario.animation.adaptivepulse":
            useAdaptivePulse = Settings.getBoolean("com.sun.scenario.animation.adaptivepulse");
            break;
         case "com.sun.scenario.animation.AnimationMBean.enabled":
            AnimationPulse.getDefaultBean().setEnabled(Settings.getBoolean("com.sun.scenario.animation.AnimationMBean.enabled"));
      }

      return null;
   };
   private boolean paused = false;
   private long totalPausedTime;
   private long startPauseTime;
   private PulseReceiver[] receivers = new PulseReceiver[2];
   private int receiversLength;
   private boolean receiversLocked;
   private TimerReceiver[] animationTimers = new TimerReceiver[2];
   private int animationTimersLength;
   private boolean animationTimersLocked;
   private final long fixedPulseLength;
   private long debugNanos;
   private final MainLoop theMainLoop;

   boolean isPaused() {
      return this.paused;
   }

   long getTotalPausedTime() {
      return this.totalPausedTime;
   }

   long getStartPauseTime() {
      return this.startPauseTime;
   }

   public int getDefaultResolution() {
      return this.PULSE_DURATION_TICKS;
   }

   public void pause() {
      if (!this.paused) {
         this.startPauseTime = this.nanos();
         this.paused = true;
      }

   }

   public void resume() {
      if (this.paused) {
         this.paused = false;
         this.totalPausedTime += this.nanos() - this.startPauseTime;
      }

   }

   public long nanos() {
      if (this.fixedPulseLength > 0L) {
         return this.debugNanos;
      } else {
         return this.paused ? this.startPauseTime : System.nanoTime() - this.totalPausedTime;
      }
   }

   public boolean isFullspeed() {
      return fullspeed;
   }

   protected AbstractPrimaryTimer() {
      this.fixedPulseLength = Boolean.getBoolean("com.sun.scenario.animation.fixed.pulse.length") ? (long)this.PULSE_DURATION_NS : 0L;
      this.debugNanos = 0L;
      this.theMainLoop = new MainLoop();
   }

   public void addPulseReceiver(PulseReceiver var1) {
      boolean var2 = this.receiversLength == this.receivers.length;
      if (this.receiversLocked || var2) {
         this.receivers = (PulseReceiver[])Arrays.copyOf(this.receivers, var2 ? this.receivers.length * 3 / 2 + 1 : this.receivers.length);
         this.receiversLocked = false;
      }

      this.receivers[this.receiversLength++] = var1;
      if (this.receiversLength == 1) {
         this.theMainLoop.updateAnimationRunnable();
      }

   }

   public void removePulseReceiver(PulseReceiver var1) {
      if (this.receiversLocked) {
         this.receivers = (PulseReceiver[])this.receivers.clone();
         this.receiversLocked = false;
      }

      for(int var2 = 0; var2 < this.receiversLength; ++var2) {
         if (var1 == this.receivers[var2]) {
            if (var2 == this.receiversLength - 1) {
               this.receivers[var2] = null;
            } else {
               System.arraycopy(this.receivers, var2 + 1, this.receivers, var2, this.receiversLength - var2 - 1);
               this.receivers[this.receiversLength - 1] = null;
            }

            --this.receiversLength;
            break;
         }
      }

      if (this.receiversLength == 0) {
         this.theMainLoop.updateAnimationRunnable();
      }

   }

   public void addAnimationTimer(TimerReceiver var1) {
      boolean var2 = this.animationTimersLength == this.animationTimers.length;
      if (this.animationTimersLocked || var2) {
         this.animationTimers = (TimerReceiver[])Arrays.copyOf(this.animationTimers, var2 ? this.animationTimers.length * 3 / 2 + 1 : this.animationTimers.length);
         this.animationTimersLocked = false;
      }

      this.animationTimers[this.animationTimersLength++] = var1;
      if (this.animationTimersLength == 1) {
         this.theMainLoop.updateAnimationRunnable();
      }

   }

   public void removeAnimationTimer(TimerReceiver var1) {
      if (this.animationTimersLocked) {
         this.animationTimers = (TimerReceiver[])this.animationTimers.clone();
         this.animationTimersLocked = false;
      }

      for(int var2 = 0; var2 < this.animationTimersLength; ++var2) {
         if (var1 == this.animationTimers[var2]) {
            if (var2 == this.animationTimersLength - 1) {
               this.animationTimers[var2] = null;
            } else {
               System.arraycopy(this.animationTimers, var2 + 1, this.animationTimers, var2, this.animationTimersLength - var2 - 1);
               this.animationTimers[this.animationTimersLength - 1] = null;
            }

            --this.animationTimersLength;
            break;
         }
      }

      if (this.animationTimersLength == 0) {
         this.theMainLoop.updateAnimationRunnable();
      }

   }

   protected void recordStart(long var1) {
   }

   protected void recordEnd() {
   }

   protected void recordAnimationEnd() {
   }

   protected abstract void postUpdateAnimationRunnable(DelayedRunnable var1);

   protected abstract int getPulseDuration(int var1);

   protected void timePulseImpl(long var1) {
      if (this.fixedPulseLength > 0L) {
         this.debugNanos += this.fixedPulseLength;
         var1 = this.debugNanos;
      }

      PulseReceiver[] var3 = this.receivers;
      int var4 = this.receiversLength;

      try {
         this.receiversLocked = true;

         for(int var5 = 0; var5 < var4; ++var5) {
            var3[var5].timePulse(TickCalculation.fromNano(var1));
         }
      } finally {
         this.receiversLocked = false;
      }

      this.recordAnimationEnd();
      TimerReceiver[] var15 = this.animationTimers;
      int var6 = this.animationTimersLength;

      try {
         this.animationTimersLocked = true;

         for(int var7 = 0; var7 < var6; ++var7) {
            var15[var7].handle(var1);
         }
      } finally {
         this.animationTimersLocked = false;
      }

   }

   static {
      Settings.addPropertyChangeListener(pcl);
      int var0 = Settings.getInt("javafx.animation.pulse", -1);
      if (var0 != -1) {
         System.err.println("Setting PULSE_DURATION to " + var0 + " hz");
      }

   }

   private final class MainLoop implements DelayedRunnable {
      private boolean inactive;
      private long nextPulseTime;
      private long lastPulseDuration;

      private MainLoop() {
         this.inactive = true;
         this.nextPulseTime = AbstractPrimaryTimer.this.nanos();
         this.lastPulseDuration = -2147483648L;
      }

      public void run() {
         if (!AbstractPrimaryTimer.this.paused) {
            long var1 = AbstractPrimaryTimer.this.nanos();
            AbstractPrimaryTimer.this.recordStart((this.nextPulseTime - var1) / 1000000L);
            AbstractPrimaryTimer.this.timePulseImpl(var1);
            AbstractPrimaryTimer.this.recordEnd();
            this.updateNextPulseTime(var1);
            this.updateAnimationRunnable();
         }
      }

      public long getDelay() {
         long var1 = AbstractPrimaryTimer.this.nanos();
         long var3 = (this.nextPulseTime - var1) / 1000000L;
         return Math.max(0L, var3);
      }

      private void updateNextPulseTime(long var1) {
         long var3 = AbstractPrimaryTimer.this.nanos();
         if (AbstractPrimaryTimer.fullspeed) {
            this.nextPulseTime = var3;
         } else if (AbstractPrimaryTimer.useAdaptivePulse) {
            this.nextPulseTime += (long)AbstractPrimaryTimer.this.PULSE_DURATION_NS;
            long var5 = var3 - var1;
            if (var5 - this.lastPulseDuration > 500000L) {
               var5 /= 2L;
            }

            if (var5 < 2000000L) {
               var5 = 2000000L;
            }

            if (var5 >= (long)AbstractPrimaryTimer.this.PULSE_DURATION_NS) {
               var5 = (long)(3 * AbstractPrimaryTimer.this.PULSE_DURATION_NS / 4);
            }

            this.lastPulseDuration = var5;
            this.nextPulseTime -= var5;
         } else {
            this.nextPulseTime = (this.nextPulseTime + (long)AbstractPrimaryTimer.this.PULSE_DURATION_NS) / (long)AbstractPrimaryTimer.this.PULSE_DURATION_NS * (long)AbstractPrimaryTimer.this.PULSE_DURATION_NS;
         }

      }

      private void updateAnimationRunnable() {
         boolean var1 = AbstractPrimaryTimer.this.animationTimersLength == 0 && AbstractPrimaryTimer.this.receiversLength == 0;
         if (this.inactive != var1) {
            this.inactive = var1;
            MainLoop var2 = this.inactive ? null : this;
            AbstractPrimaryTimer.this.postUpdateAnimationRunnable(var2);
         }

      }

      // $FF: synthetic method
      MainLoop(Object var2) {
         this();
      }
   }
}
