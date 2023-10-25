/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.javafx.tk.CompletionListener;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.quantum.PaintCollector;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.javafx.tk.quantum.ViewPainter;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.GraphicsResource;
import com.sun.prism.Presentable;
import com.sun.prism.ResourceFactory;
import com.sun.prism.impl.PrismSettings;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

final class QuantumRenderer
extends ThreadPoolExecutor {
    private static boolean usePurgatory = AccessController.doPrivileged(() -> Boolean.getBoolean("decora.purgatory"));
    private static final AtomicReference<QuantumRenderer> instanceReference = new AtomicReference<Object>(null);
    private Thread _renderer;
    private Throwable _initThrowable = null;
    private CountDownLatch initLatch = new CountDownLatch(1);

    private QuantumRenderer() {
        super(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        this.setThreadFactory(new QuantumThreadFactory());
    }

    protected Throwable initThrowable() {
        return this._initThrowable;
    }

    private void setInitThrowable(Throwable throwable) {
        this._initThrowable = throwable;
    }

    protected void createResourceFactory() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CompletionListener completionListener = renderJob -> countDownLatch.countDown();
        Runnable runnable = () -> {
            ResourceFactory resourceFactory = GraphicsPipeline.getDefaultResourceFactory();
            assert (resourceFactory != null);
        };
        RenderJob renderJob2 = new RenderJob(runnable, completionListener);
        this.submit(renderJob2);
        try {
            countDownLatch.await();
        }
        catch (Throwable throwable) {
            throwable.printStackTrace(System.err);
        }
    }

    protected void disposePresentable(Presentable presentable) {
        assert (!Thread.currentThread().equals(this._renderer));
        if (presentable instanceof GraphicsResource) {
            GraphicsResource graphicsResource = (GraphicsResource)((Object)presentable);
            Runnable runnable = () -> graphicsResource.dispose();
            RenderJob renderJob = new RenderJob(runnable, null);
            this.submit(renderJob);
        }
    }

    protected void stopRenderer() {
        AccessController.doPrivileged(() -> {
            this.shutdown();
            return null;
        });
        if (PrismSettings.verbose) {
            System.out.println("QuantumRenderer: shutdown");
        }
        assert (this.isShutdown());
        instanceReference.set(null);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T t) {
        return (RenderJob)runnable;
    }

    protected Future submitRenderJob(RenderJob renderJob) {
        return this.submit(renderJob);
    }

    @Override
    public void afterExecute(Runnable runnable, Throwable throwable) {
        super.afterExecute(runnable, throwable);
        if (usePurgatory) {
            Screen screen = Screen.getMainScreen();
            Renderer renderer = Renderer.getRenderer(PrFilterContext.getInstance(screen));
            renderer.releasePurgatory();
        }
    }

    void checkRendererIdle() {
        if (PrismSettings.threadCheck) {
            boolean bl;
            PaintCollector paintCollector = PaintCollector.getInstance();
            boolean bl2 = bl = ViewPainter.renderLock.isLocked() && !ViewPainter.renderLock.isHeldByCurrentThread();
            if (bl) {
                System.err.println("ERROR: PrismPen / FX threads co-running: DIRTY: " + paintCollector.hasDirty());
                for (StackTraceElement stackTraceElement : QuantumToolkit.getFxUserThread().getStackTrace()) {
                    System.err.println("FX: " + stackTraceElement);
                }
                for (StackTraceElement stackTraceElement : this._renderer.getStackTrace()) {
                    System.err.println("QR: " + stackTraceElement);
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static synchronized QuantumRenderer getInstance() {
        if (instanceReference.get() != null) return instanceReference.get();
        Class<QuantumRenderer> class_ = QuantumRenderer.class;
        synchronized (QuantumRenderer.class) {
            QuantumRenderer quantumRenderer;
            block7: {
                quantumRenderer = null;
                try {
                    quantumRenderer = new QuantumRenderer();
                    quantumRenderer.prestartCoreThread();
                    quantumRenderer.initLatch.await();
                }
                catch (Throwable throwable) {
                    if (quantumRenderer != null) {
                        quantumRenderer.setInitThrowable(throwable);
                    }
                    if (!PrismSettings.verbose) break block7;
                    throwable.printStackTrace();
                }
            }
            if (quantumRenderer != null && quantumRenderer.initThrowable() != null) {
                if (!PrismSettings.noFallback) throw new RuntimeException(quantumRenderer.initThrowable());
                System.err.println("Cannot initialize a graphics pipeline, and Prism fallback is disabled");
                throw new InternalError("Could not initialize prism toolkit, and the fallback is disabled.");
            }
            instanceReference.set(quantumRenderer);
            // ** MonitorExit[var0] (shouldn't be in output)
            return instanceReference.get();
        }
    }

    private class QuantumThreadFactory
    implements ThreadFactory {
        final AtomicInteger threadNumber = new AtomicInteger(0);

        private QuantumThreadFactory() {
        }

        @Override
        public Thread newThread(Runnable runnable) {
            PipelineRunnable pipelineRunnable = new PipelineRunnable(runnable);
            QuantumRenderer.this._renderer = AccessController.doPrivileged(() -> {
                Thread thread2 = new Thread(pipelineRunnable);
                thread2.setName("QuantumRenderer-" + this.threadNumber.getAndIncrement());
                thread2.setDaemon(true);
                thread2.setUncaughtExceptionHandler((thread, throwable) -> {
                    System.err.println(thread.getName() + " uncaught: " + throwable.getClass().getName());
                    throwable.printStackTrace();
                });
                return thread2;
            });
            assert (this.threadNumber.get() == 1);
            return QuantumRenderer.this._renderer;
        }
    }

    private class PipelineRunnable
    implements Runnable {
        private Runnable work;

        public PipelineRunnable(Runnable runnable) {
            this.work = runnable;
        }

        public void init() {
            try {
                if (GraphicsPipeline.createPipeline() == null) {
                    String string = "Error initializing QuantumRenderer: no suitable pipeline found";
                    System.err.println(string);
                    throw new RuntimeException(string);
                }
                HashMap<Object, Boolean> hashMap = GraphicsPipeline.getPipeline().getDeviceDetails();
                if (hashMap == null) {
                    hashMap = new HashMap<Object, Boolean>();
                }
                hashMap.put(View.Capability.kHiDPIAwareKey, PrismSettings.allowHiDPIScaling);
                Map map = Application.getDeviceDetails();
                if (map != null) {
                    hashMap.putAll(map);
                }
                Application.setDeviceDetails(hashMap);
            }
            catch (Throwable throwable) {
                QuantumRenderer.this.setInitThrowable(throwable);
            }
            finally {
                QuantumRenderer.this.initLatch.countDown();
            }
        }

        public void cleanup() {
            GraphicsPipeline graphicsPipeline = GraphicsPipeline.getPipeline();
            if (graphicsPipeline != null) {
                graphicsPipeline.dispose();
            }
        }

        @Override
        public void run() {
            try {
                this.init();
                this.work.run();
            }
            finally {
                this.cleanup();
            }
        }
    }
}

