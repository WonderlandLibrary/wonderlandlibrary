package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.Toolkit;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.Settings;
import com.sun.scenario.animation.AbstractPrimaryTimer;
import com.sun.scenario.animation.AnimationPulse;
import java.util.Map;

public final class PrimaryTimer extends AbstractPrimaryTimer {
   private static final Object PRIMARY_TIMER_KEY = new StringBuilder("PrimaryTimerKey");

   private PrimaryTimer() {
   }

   public static synchronized PrimaryTimer getInstance() {
      Map var0 = Toolkit.getToolkit().getContextMap();
      PrimaryTimer var1 = (PrimaryTimer)var0.get(PRIMARY_TIMER_KEY);
      if (var1 == null) {
         var1 = new PrimaryTimer();
         var0.put(PRIMARY_TIMER_KEY, var1);
         if (Settings.getBoolean("com.sun.scenario.animation.AnimationMBean.enabled", false)) {
            AnimationPulse.getDefaultBean().setEnabled(true);
         }
      }

      return var1;
   }

   protected int getPulseDuration(int var1) {
      int var2 = var1 / 60;
      int var3;
      if (Settings.get("javafx.animation.framerate") != null) {
         var3 = Settings.getInt("javafx.animation.framerate", 60);
         if (var3 > 0) {
            var2 = var1 / var3;
         }
      } else if (Settings.get("javafx.animation.pulse") != null) {
         var3 = Settings.getInt("javafx.animation.pulse", 60);
         if (var3 > 0) {
            var2 = var1 / var3;
         }
      } else {
         var3 = Toolkit.getToolkit().getRefreshRate();
         if (var3 > 0) {
            var2 = var1 / var3;
         }
      }

      return var2;
   }

   protected void postUpdateAnimationRunnable(DelayedRunnable var1) {
      Toolkit.getToolkit().setAnimationRunnable(var1);
   }

   protected void recordStart(long var1) {
      AnimationPulse.getDefaultBean().recordStart(var1);
   }

   protected void recordEnd() {
      AnimationPulse.getDefaultBean().recordEnd();
   }

   protected void recordAnimationEnd() {
      AnimationPulse.getDefaultBean().recordAnimationEnd();
   }
}
