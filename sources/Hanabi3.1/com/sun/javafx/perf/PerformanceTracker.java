package com.sun.javafx.perf;

import com.sun.javafx.tk.Toolkit;
import javafx.scene.Scene;

public abstract class PerformanceTracker {
   private static SceneAccessor sceneAccessor;
   private boolean perfLoggingEnabled;
   private boolean firstPulse = true;
   private float instantFPS;
   private int instantFPSFrames;
   private long instantFPSStartTime;
   private long avgStartTime;
   private int avgFramesTotal;
   private float instantPulses;
   private int instantPulsesFrames;
   private long instantPulsesStartTime;
   private long avgPulsesStartTime;
   private int avgPulsesTotal;
   private Runnable onPulse;
   private Runnable onFirstPulse;
   private Runnable onRenderedFrameTask;

   public static boolean isLoggingEnabled() {
      return Toolkit.getToolkit().getPerformanceTracker().perfLoggingEnabled;
   }

   public static PerformanceTracker getSceneTracker(Scene var0) {
      PerformanceTracker var1 = null;
      if (sceneAccessor != null) {
         var1 = sceneAccessor.getPerfTracker(var0);
         if (var1 == null) {
            var1 = Toolkit.getToolkit().createPerformanceTracker();
         }

         sceneAccessor.setPerfTracker(var0, var1);
      }

      return var1;
   }

   public static void releaseSceneTracker(Scene var0) {
      if (sceneAccessor != null) {
         sceneAccessor.setPerfTracker(var0, (PerformanceTracker)null);
      }

   }

   public static void setSceneAccessor(SceneAccessor var0) {
      sceneAccessor = var0;
   }

   public static void logEvent(String var0) {
      Toolkit.getToolkit().getPerformanceTracker().doLogEvent(var0);
   }

   public static void outputLog() {
      Toolkit.getToolkit().getPerformanceTracker().doOutputLog();
   }

   protected boolean isPerfLoggingEnabled() {
      return this.perfLoggingEnabled;
   }

   protected void setPerfLoggingEnabled(boolean var1) {
      this.perfLoggingEnabled = var1;
   }

   protected abstract long nanoTime();

   public abstract void doOutputLog();

   public abstract void doLogEvent(String var1);

   public synchronized float getInstantFPS() {
      return this.instantFPS;
   }

   public synchronized float getAverageFPS() {
      long var1 = this.nanoTime() - this.avgStartTime;
      return var1 > 0L ? (float)this.avgFramesTotal * 1.0E9F / (float)var1 : this.getInstantFPS();
   }

   public synchronized void resetAverageFPS() {
      this.avgStartTime = this.nanoTime();
      this.avgFramesTotal = 0;
   }

   public float getInstantPulses() {
      return this.instantPulses;
   }

   public float getAveragePulses() {
      long var1 = this.nanoTime() - this.avgPulsesStartTime;
      return var1 > 0L ? (float)this.avgPulsesTotal * 1.0E9F / (float)var1 : this.getInstantPulses();
   }

   public void resetAveragePulses() {
      this.avgPulsesStartTime = this.nanoTime();
      this.avgPulsesTotal = 0;
   }

   public void pulse() {
      this.calcPulses();
      this.updateInstantFps();
      if (this.firstPulse) {
         this.doLogEvent("first repaint");
         this.firstPulse = false;
         this.resetAverageFPS();
         this.resetAveragePulses();
         if (this.onFirstPulse != null) {
            this.onFirstPulse.run();
         }
      }

      if (this.onPulse != null) {
         this.onPulse.run();
      }

   }

   public void frameRendered() {
      this.calcFPS();
      if (this.onRenderedFrameTask != null) {
         this.onRenderedFrameTask.run();
      }

   }

   private void calcPulses() {
      ++this.avgPulsesTotal;
      ++this.instantPulsesFrames;
      this.updateInstantPulses();
   }

   private synchronized void calcFPS() {
      ++this.avgFramesTotal;
      ++this.instantFPSFrames;
      this.updateInstantFps();
   }

   private synchronized void updateInstantFps() {
      long var1 = this.nanoTime() - this.instantFPSStartTime;
      if (var1 > 1000000000L) {
         this.instantFPS = 1.0E9F * (float)this.instantFPSFrames / (float)var1;
         this.instantFPSFrames = 0;
         this.instantFPSStartTime = this.nanoTime();
      }

   }

   private void updateInstantPulses() {
      long var1 = this.nanoTime() - this.instantPulsesStartTime;
      if (var1 > 1000000000L) {
         this.instantPulses = 1.0E9F * (float)this.instantPulsesFrames / (float)var1;
         this.instantPulsesFrames = 0;
         this.instantPulsesStartTime = this.nanoTime();
      }

   }

   public void setOnPulse(Runnable var1) {
      this.onPulse = var1;
   }

   public Runnable getOnPulse() {
      return this.onPulse;
   }

   public void setOnFirstPulse(Runnable var1) {
      this.onFirstPulse = var1;
   }

   public Runnable getOnFirstPulse() {
      return this.onFirstPulse;
   }

   public void setOnRenderedFrameTask(Runnable var1) {
      this.onRenderedFrameTask = var1;
   }

   public Runnable getOnRenderedFrameTask() {
      return this.onRenderedFrameTask;
   }

   public abstract static class SceneAccessor {
      public abstract void setPerfTracker(Scene var1, PerformanceTracker var2);

      public abstract PerformanceTracker getPerfTracker(Scene var1);
   }
}
