/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Scene
 */
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

    public static PerformanceTracker getSceneTracker(Scene scene) {
        PerformanceTracker performanceTracker = null;
        if (sceneAccessor != null) {
            performanceTracker = sceneAccessor.getPerfTracker(scene);
            if (performanceTracker == null) {
                performanceTracker = Toolkit.getToolkit().createPerformanceTracker();
            }
            sceneAccessor.setPerfTracker(scene, performanceTracker);
        }
        return performanceTracker;
    }

    public static void releaseSceneTracker(Scene scene) {
        if (sceneAccessor != null) {
            sceneAccessor.setPerfTracker(scene, null);
        }
    }

    public static void setSceneAccessor(SceneAccessor sceneAccessor) {
        PerformanceTracker.sceneAccessor = sceneAccessor;
    }

    public static void logEvent(String string) {
        Toolkit.getToolkit().getPerformanceTracker().doLogEvent(string);
    }

    public static void outputLog() {
        Toolkit.getToolkit().getPerformanceTracker().doOutputLog();
    }

    protected boolean isPerfLoggingEnabled() {
        return this.perfLoggingEnabled;
    }

    protected void setPerfLoggingEnabled(boolean bl) {
        this.perfLoggingEnabled = bl;
    }

    protected abstract long nanoTime();

    public abstract void doOutputLog();

    public abstract void doLogEvent(String var1);

    public synchronized float getInstantFPS() {
        return this.instantFPS;
    }

    public synchronized float getAverageFPS() {
        long l = this.nanoTime() - this.avgStartTime;
        if (l > 0L) {
            return (float)this.avgFramesTotal * 1.0E9f / (float)l;
        }
        return this.getInstantFPS();
    }

    public synchronized void resetAverageFPS() {
        this.avgStartTime = this.nanoTime();
        this.avgFramesTotal = 0;
    }

    public float getInstantPulses() {
        return this.instantPulses;
    }

    public float getAveragePulses() {
        long l = this.nanoTime() - this.avgPulsesStartTime;
        if (l > 0L) {
            return (float)this.avgPulsesTotal * 1.0E9f / (float)l;
        }
        return this.getInstantPulses();
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
        long l = this.nanoTime() - this.instantFPSStartTime;
        if (l > 1000000000L) {
            this.instantFPS = 1.0E9f * (float)this.instantFPSFrames / (float)l;
            this.instantFPSFrames = 0;
            this.instantFPSStartTime = this.nanoTime();
        }
    }

    private void updateInstantPulses() {
        long l = this.nanoTime() - this.instantPulsesStartTime;
        if (l > 1000000000L) {
            this.instantPulses = 1.0E9f * (float)this.instantPulsesFrames / (float)l;
            this.instantPulsesFrames = 0;
            this.instantPulsesStartTime = this.nanoTime();
        }
    }

    public void setOnPulse(Runnable runnable) {
        this.onPulse = runnable;
    }

    public Runnable getOnPulse() {
        return this.onPulse;
    }

    public void setOnFirstPulse(Runnable runnable) {
        this.onFirstPulse = runnable;
    }

    public Runnable getOnFirstPulse() {
        return this.onFirstPulse;
    }

    public void setOnRenderedFrameTask(Runnable runnable) {
        this.onRenderedFrameTask = runnable;
    }

    public Runnable getOnRenderedFrameTask() {
        return this.onRenderedFrameTask;
    }

    public static abstract class SceneAccessor {
        public abstract void setPerfTracker(Scene var1, PerformanceTracker var2);

        public abstract PerformanceTracker getPerfTracker(Scene var1);
    }
}

