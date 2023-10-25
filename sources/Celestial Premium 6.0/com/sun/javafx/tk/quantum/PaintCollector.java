/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.PlatformUtil
 *  com.sun.javafx.logging.PulseLogger
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Window;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.tk.CompletionListener;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.PaintRenderJob;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.javafx.tk.quantum.ViewPainter;
import com.sun.javafx.tk.quantum.ViewScene;
import com.sun.javafx.tk.quantum.WindowStage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

final class PaintCollector
implements CompletionListener {
    private static volatile PaintCollector collector;
    private static final Comparator<GlassScene> DIRTY_SCENE_SORTER;
    private final List<GlassScene> dirtyScenes = new ArrayList<GlassScene>();
    private volatile CountDownLatch allWorkCompletedLatch = new CountDownLatch(0);
    private volatile boolean hasDirty;
    private final QuantumToolkit toolkit;
    private volatile boolean needsHint;

    static PaintCollector createInstance(QuantumToolkit quantumToolkit) {
        collector = new PaintCollector(quantumToolkit);
        return collector;
    }

    static PaintCollector getInstance() {
        return collector;
    }

    private PaintCollector(QuantumToolkit quantumToolkit) {
        this.toolkit = quantumToolkit;
    }

    void waitForRenderingToComplete() {
        while (true) {
            try {
                this.allWorkCompletedLatch.await();
                return;
            }
            catch (InterruptedException interruptedException) {
                continue;
            }
            break;
        }
    }

    final boolean hasDirty() {
        return this.hasDirty;
    }

    private final void setDirty(boolean bl) {
        this.hasDirty = bl;
        if (this.hasDirty) {
            QuantumToolkit.getToolkit().requestNextPulse();
        }
    }

    final void addDirtyScene(GlassScene glassScene) {
        assert (Thread.currentThread() == QuantumToolkit.getFxUserThread());
        assert (glassScene != null);
        if (QuantumToolkit.verbose) {
            System.err.println("PC.addDirtyScene: " + System.nanoTime() + glassScene);
        }
        if (!this.dirtyScenes.contains(glassScene)) {
            this.dirtyScenes.add(glassScene);
            this.setDirty(true);
        }
    }

    final void removeDirtyScene(GlassScene glassScene) {
        assert (Thread.currentThread() == QuantumToolkit.getFxUserThread());
        assert (glassScene != null);
        if (QuantumToolkit.verbose) {
            System.err.println("PC.removeDirtyScene: " + glassScene);
        }
        this.dirtyScenes.remove(glassScene);
        this.setDirty(!this.dirtyScenes.isEmpty());
    }

    final CompletionListener getRendered() {
        return this;
    }

    @Override
    public void done(RenderJob renderJob) {
        assert (Thread.currentThread() != QuantumToolkit.getFxUserThread());
        if (!(renderJob instanceof PaintRenderJob)) {
            throw new IllegalArgumentException("PaintCollector: invalid RenderJob");
        }
        PaintRenderJob paintRenderJob = (PaintRenderJob)renderJob;
        GlassScene glassScene = paintRenderJob.getScene();
        if (glassScene == null) {
            throw new IllegalArgumentException("PaintCollector: null scene");
        }
        glassScene.frameRendered();
        if (this.allWorkCompletedLatch.getCount() == 1L) {
            if (this.needsHint && !this.toolkit.hasNativeSystemVsync()) {
                this.toolkit.vsyncHint();
            }
            Application.GetApplication().notifyRenderingFinished();
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.renderEnd();
            }
        }
        this.allWorkCompletedLatch.countDown();
    }

    final void liveRepaintRenderJob(ViewScene viewScene) {
        ViewPainter viewPainter = viewScene.getPainter();
        QuantumToolkit quantumToolkit = (QuantumToolkit)QuantumToolkit.getToolkit();
        quantumToolkit.pulse(false);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        QuantumToolkit.runWithoutRenderLock(() -> {
            quantumToolkit.addRenderJob(new RenderJob((Runnable)viewPainter, renderJob -> countDownLatch.countDown()));
            try {
                countDownLatch.await();
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            return null;
        });
    }

    final void renderAll() {
        assert (Thread.currentThread() == QuantumToolkit.getFxUserThread());
        if (QuantumToolkit.pulseDebug) {
            System.err.println("PC.renderAll(" + this.dirtyScenes.size() + "): " + System.nanoTime());
        }
        if (!this.hasDirty) {
            return;
        }
        assert (!this.dirtyScenes.isEmpty());
        Collections.sort(this.dirtyScenes, DIRTY_SCENE_SORTER);
        this.setDirty(false);
        this.needsHint = false;
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.renderStart();
        }
        if (!Application.GetApplication().hasWindowManager()) {
            List<Window> list = Window.getWindows();
            this.allWorkCompletedLatch = new CountDownLatch(list.size());
            int n = list.size();
            for (int i = 0; i < n; ++i) {
                Window window = list.get(i);
                WindowStage windowStage = WindowStage.findWindowStage(window);
                if (windowStage == null) continue;
                ViewScene viewScene = windowStage.getViewScene();
                if (this.dirtyScenes.indexOf(viewScene) != -1 && !this.needsHint) {
                    this.needsHint = viewScene.isSynchronous();
                }
                if (!PlatformUtil.useEGL() || i == n - 1) {
                    viewScene.setDoPresent(true);
                } else {
                    viewScene.setDoPresent(false);
                }
                try {
                    viewScene.repaint();
                    continue;
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        } else {
            this.allWorkCompletedLatch = new CountDownLatch(this.dirtyScenes.size());
            for (GlassScene glassScene : this.dirtyScenes) {
                if (!this.needsHint) {
                    this.needsHint = glassScene.isSynchronous();
                }
                glassScene.setDoPresent(true);
                try {
                    glassScene.repaint();
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        this.dirtyScenes.clear();
        if (this.toolkit.shouldWaitForRenderingToComplete()) {
            this.waitForRenderingToComplete();
        }
    }

    static {
        DIRTY_SCENE_SORTER = (glassScene, glassScene2) -> {
            int n = glassScene.isSynchronous() ? 1 : 0;
            int n2 = glassScene2.isSynchronous() ? 1 : 0;
            return n - n2;
        };
    }
}

