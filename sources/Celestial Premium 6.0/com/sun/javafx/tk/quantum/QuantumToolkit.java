/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.PlatformUtil
 *  com.sun.javafx.logging.PulseLogger
 *  javafx.application.ConditionalFeature
 *  javafx.geometry.Dimension2D
 *  javafx.scene.image.Image
 *  javafx.scene.image.PixelBuffer
 *  javafx.scene.image.PixelFormat
 *  javafx.scene.input.Dragboard
 *  javafx.scene.input.InputMethodRequests
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.TransferMode
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.CycleMethod
 *  javafx.scene.paint.ImagePattern
 *  javafx.scene.paint.LinearGradient
 *  javafx.scene.paint.Paint
 *  javafx.scene.paint.RadialGradient
 *  javafx.scene.paint.Stop
 *  javafx.scene.shape.ClosePath
 *  javafx.scene.shape.CubicCurveTo
 *  javafx.scene.shape.FillRule
 *  javafx.scene.shape.LineTo
 *  javafx.scene.shape.MoveTo
 *  javafx.scene.shape.PathElement
 *  javafx.scene.shape.QuadCurveTo
 *  javafx.scene.shape.SVGPath
 *  javafx.scene.shape.StrokeLineCap
 *  javafx.scene.shape.StrokeLineJoin
 *  javafx.scene.shape.StrokeType
 *  javafx.stage.FileChooser$ExtensionFilter
 *  javafx.stage.Modality
 *  javafx.stage.Screen
 *  javafx.stage.StageStyle
 *  javafx.stage.Window
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.EventLoop;
import com.sun.glass.ui.GlassRobot;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Timer;
import com.sun.glass.ui.View;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.font.PrismFontLoader;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.runtime.async.AbstractRemoteResource;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.scene.input.DragboardHelper;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.text.PrismTextLayoutFactory;
import com.sun.javafx.tk.AppletWindow;
import com.sun.javafx.tk.CompletionListener;
import com.sun.javafx.tk.FileChooserType;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.ScreenConfigurationAccessor;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.TKDragGestureListener;
import com.sun.javafx.tk.TKDragSourceListener;
import com.sun.javafx.tk.TKDropTargetListener;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKScreenConfigurationListener;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.TKSystemMenu;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.tk.quantum.CursorUtils;
import com.sun.javafx.tk.quantum.EmbeddedStage;
import com.sun.javafx.tk.quantum.GlassAppletWindow;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.GlassStage;
import com.sun.javafx.tk.quantum.GlassSystemMenu;
import com.sun.javafx.tk.quantum.PaintCollector;
import com.sun.javafx.tk.quantum.PaintRenderJob;
import com.sun.javafx.tk.quantum.PathIteratorHelper;
import com.sun.javafx.tk.quantum.PerformanceTrackerImpl;
import com.sun.javafx.tk.quantum.PrimaryTimer;
import com.sun.javafx.tk.quantum.PrismImageLoader2;
import com.sun.javafx.tk.quantum.QuantumClipboard;
import com.sun.javafx.tk.quantum.QuantumRenderer;
import com.sun.javafx.tk.quantum.ViewPainter;
import com.sun.javafx.tk.quantum.WindowStage;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.animation.AbstractPrimaryTimer;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import com.sun.scenario.effect.impl.prism.PrImage;
import java.io.File;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import javafx.application.ConditionalFeature;
import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public final class QuantumToolkit
extends Toolkit {
    public static final boolean verbose = AccessController.doPrivileged(() -> Boolean.getBoolean("quantum.verbose"));
    public static final boolean pulseDebug = AccessController.doPrivileged(() -> Boolean.getBoolean("quantum.pulse"));
    private static final boolean multithreaded = AccessController.doPrivileged(() -> {
        String string = System.getProperty("quantum.multithreaded");
        if (string == null) {
            return true;
        }
        boolean bl = Boolean.parseBoolean(string);
        if (verbose) {
            System.out.println(bl ? "Multi-Threading Enabled" : "Multi-Threading Disabled");
        }
        return bl;
    });
    private static boolean debug = AccessController.doPrivileged(() -> Boolean.getBoolean("quantum.debug"));
    private static Integer pulseHZ = AccessController.doPrivileged(() -> Integer.getInteger("javafx.animation.pulse"));
    static final boolean liveResize = AccessController.doPrivileged(() -> {
        boolean bl = "swt".equals(System.getProperty("glass.platform"));
        String string = (PlatformUtil.isMac() || PlatformUtil.isWindows()) && !bl ? "true" : "false";
        return "true".equals(System.getProperty("javafx.live.resize", string));
    });
    static final boolean drawInPaint = AccessController.doPrivileged(() -> {
        boolean bl = "swt".equals(System.getProperty("glass.platform"));
        String string = PlatformUtil.isMac() && bl ? "true" : "false";
        return "true".equals(System.getProperty("javafx.draw.in.paint", string));
    });
    private static boolean singleThreaded = AccessController.doPrivileged(() -> {
        Boolean bl = Boolean.getBoolean("quantum.singlethreaded");
        if (bl.booleanValue()) {
            System.out.println("Warning: Single GUI Threadiong is enabled, FPS should be slower");
        }
        return bl;
    });
    private static boolean noRenderJobs = AccessController.doPrivileged(() -> {
        Boolean bl = Boolean.getBoolean("quantum.norenderjobs");
        if (bl.booleanValue()) {
            System.out.println("Warning: Quantum will not submit render jobs, nothing should draw");
        }
        return bl;
    });
    private AtomicBoolean toolkitRunning = new AtomicBoolean(false);
    private PulseTask animationRunning = new PulseTask(false);
    private PulseTask nextPulseRequested = new PulseTask(false);
    private AtomicBoolean pulseRunning = new AtomicBoolean(false);
    private int inPulse = 0;
    private CountDownLatch launchLatch = new CountDownLatch(1);
    final int PULSE_INTERVAL = (int)(TimeUnit.SECONDS.toMillis(1L) / (long)this.getRefreshRate());
    final int FULLSPEED_INTERVAL = 1;
    boolean nativeSystemVsync = false;
    private long firstPauseRequestTime = 0L;
    private boolean pauseRequested = false;
    private static final long PAUSE_THRESHOLD_DURATION = 250L;
    private float _maxPixelScale;
    private Runnable pulseRunnable;
    private Runnable userRunnable;
    private Runnable timerRunnable;
    private Timer pulseTimer = null;
    private Thread shutdownHook = null;
    private PaintCollector collector;
    private QuantumRenderer renderer;
    private GraphicsPipeline pipeline;
    private ClassLoader ccl;
    private HashMap<Object, EventLoop> eventLoopMap = null;
    private final PerformanceTracker perfTracker = new PerformanceTrackerImpl();
    private static ScreenConfigurationAccessor screenAccessor = new ScreenConfigurationAccessor(){

        @Override
        public int getMinX(Object object) {
            return ((com.sun.glass.ui.Screen)object).getX();
        }

        @Override
        public int getMinY(Object object) {
            return ((com.sun.glass.ui.Screen)object).getY();
        }

        @Override
        public int getWidth(Object object) {
            return ((com.sun.glass.ui.Screen)object).getWidth();
        }

        @Override
        public int getHeight(Object object) {
            return ((com.sun.glass.ui.Screen)object).getHeight();
        }

        @Override
        public int getVisualMinX(Object object) {
            return ((com.sun.glass.ui.Screen)object).getVisibleX();
        }

        @Override
        public int getVisualMinY(Object object) {
            return ((com.sun.glass.ui.Screen)object).getVisibleY();
        }

        @Override
        public int getVisualWidth(Object object) {
            return ((com.sun.glass.ui.Screen)object).getVisibleWidth();
        }

        @Override
        public int getVisualHeight(Object object) {
            return ((com.sun.glass.ui.Screen)object).getVisibleHeight();
        }

        @Override
        public float getDPI(Object object) {
            return ((com.sun.glass.ui.Screen)object).getResolutionX();
        }

        @Override
        public float getRecommendedOutputScaleX(Object object) {
            return ((com.sun.glass.ui.Screen)object).getRecommendedOutputScaleX();
        }

        @Override
        public float getRecommendedOutputScaleY(Object object) {
            return ((com.sun.glass.ui.Screen)object).getRecommendedOutputScaleY();
        }
    };
    private Map<Object, Object> contextMap = Collections.synchronizedMap(new HashMap());
    private DelayedRunnable animationRunnable;
    static BasicStroke tmpStroke = new BasicStroke();
    private QuantumClipboard clipboard;
    private GlassSystemMenu systemMenu = new GlassSystemMenu();

    @Override
    public boolean init() {
        this.renderer = QuantumRenderer.getInstance();
        this.collector = PaintCollector.createInstance(this);
        this.pipeline = GraphicsPipeline.getPipeline();
        this.shutdownHook = new Thread("Glass/Prism Shutdown Hook"){

            @Override
            public void run() {
                QuantumToolkit.this.dispose();
            }
        };
        Void void_ = AccessController.doPrivileged(() -> {
            Runtime.getRuntime().addShutdownHook(this.shutdownHook);
            return null;
        });
        return true;
    }

    @Override
    public void startup(Runnable runnable) {
        this.ccl = Thread.currentThread().getContextClassLoader();
        try {
            this.userRunnable = runnable;
            Application.run(() -> this.runToolkit());
        }
        catch (RuntimeException runtimeException) {
            if (verbose) {
                runtimeException.printStackTrace();
            }
            throw runtimeException;
        }
        catch (Throwable throwable) {
            if (verbose) {
                throwable.printStackTrace();
            }
            throw new RuntimeException(throwable);
        }
        try {
            this.launchLatch.await();
        }
        catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    private void assertToolkitRunning() {
    }

    boolean shouldWaitForRenderingToComplete() {
        return !multithreaded;
    }

    private static void initSceneGraph() {
        Screen.getPrimary();
    }

    void runToolkit() {
        Thread thread = Thread.currentThread();
        if (!this.toolkitRunning.getAndSet(true)) {
            thread.setName("JavaFX Application Thread");
            thread.setContextClassLoader(this.ccl);
            QuantumToolkit.setFxUserThread(thread);
            QuantumToolkit.assignScreensAdapters();
            this.renderer.createResourceFactory();
            this.pulseRunnable = () -> this.pulseFromQueue();
            this.timerRunnable = () -> {
                try {
                    this.postPulse();
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace(System.err);
                }
            };
            this.pulseTimer = Application.GetApplication().createTimer(this.timerRunnable);
            Application.GetApplication().setEventHandler(new Application.EventHandler(){

                @Override
                public void handleQuitAction(Application application, long l) {
                    GlassStage.requestClosingAllWindows();
                }

                @Override
                public boolean handleThemeChanged(String string) {
                    String string2 = Application.GetApplication().getHighContrastScheme(string);
                    return PlatformImpl.setAccessibilityTheme(string2);
                }
            });
        }
        QuantumToolkit.initSceneGraph();
        this.launchLatch.countDown();
        try {
            Application.invokeAndWait(this.userRunnable);
            if (this.getPrimaryTimer().isFullspeed()) {
                this.pulseTimer.start(1);
            } else {
                boolean bl = this.nativeSystemVsync = com.sun.glass.ui.Screen.getVideoRefreshPeriod() != 0.0;
                if (this.nativeSystemVsync) {
                    this.pulseTimer.start();
                } else {
                    this.pulseTimer.start(this.PULSE_INTERVAL);
                }
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace(System.err);
        }
        finally {
            if (PrismSettings.verbose) {
                System.err.println(" vsync: " + PrismSettings.isVsyncEnabled + " vpipe: " + this.pipeline.isVsyncSupported());
            }
            PerformanceTracker.logEvent("Toolkit.startup - finished");
        }
    }

    public static <T> T runWithoutRenderLock(Supplier<T> supplier) {
        boolean bl = ViewPainter.renderLock.isHeldByCurrentThread();
        try {
            if (bl) {
                ViewPainter.renderLock.unlock();
            }
            T t = supplier.get();
            return t;
        }
        finally {
            if (bl) {
                ViewPainter.renderLock.lock();
            }
        }
    }

    public static <T> T runWithRenderLock(Supplier<T> supplier) {
        ViewPainter.renderLock.lock();
        try {
            T t = supplier.get();
            return t;
        }
        finally {
            ViewPainter.renderLock.unlock();
        }
    }

    boolean hasNativeSystemVsync() {
        return this.nativeSystemVsync;
    }

    boolean isVsyncEnabled() {
        return PrismSettings.isVsyncEnabled && this.pipeline.isVsyncSupported();
    }

    @Override
    public void checkFxUserThread() {
        super.checkFxUserThread();
        this.renderer.checkRendererIdle();
    }

    protected static Thread getFxUserThread() {
        return Toolkit.getFxUserThread();
    }

    @Override
    public Future addRenderJob(RenderJob renderJob) {
        if (noRenderJobs) {
            CompletionListener completionListener = renderJob.getCompletionListener();
            if (renderJob instanceof PaintRenderJob) {
                ((PaintRenderJob)renderJob).getScene().setPainting(false);
            }
            if (completionListener != null) {
                try {
                    completionListener.done(renderJob);
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
            return null;
        }
        if (singleThreaded) {
            renderJob.run();
            return null;
        }
        return this.renderer.submitRenderJob(renderJob);
    }

    void postPulse() {
        if (this.toolkitRunning.get() && (this.animationRunning.get() || this.nextPulseRequested.get()) && !this.setPulseRunning()) {
            Application.invokeLater(this.pulseRunnable);
            if (debug) {
                System.err.println("QT.postPulse@(" + System.nanoTime() + "): " + this.pulseString());
            }
        } else if (!(this.animationRunning.get() || this.nextPulseRequested.get() || this.pulseRunning.get())) {
            this.pauseTimer();
        } else if (debug) {
            System.err.println("QT.postPulse#(" + System.nanoTime() + "): DROP : " + this.pulseString());
        }
    }

    private synchronized void pauseTimer() {
        if (!this.pauseRequested) {
            this.pauseRequested = true;
            this.firstPauseRequestTime = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - this.firstPauseRequestTime >= 250L) {
            this.pulseTimer.pause();
            if (debug) {
                System.err.println("QT.pauseTimer#(" + System.nanoTime() + "): Pausing Timer : " + this.pulseString());
            }
        } else if (debug) {
            System.err.println("QT.pauseTimer#(" + System.nanoTime() + "): Pause Timer : DROP : " + this.pulseString());
        }
    }

    private synchronized void resumeTimer() {
        this.pauseRequested = false;
        this.pulseTimer.resume();
    }

    private String pulseString() {
        return (this.toolkitRunning.get() ? "T" : "t") + (this.animationRunning.get() ? "A" : "a") + (this.pulseRunning.get() ? "P" : "p") + (this.nextPulseRequested.get() ? "N" : "n");
    }

    private boolean setPulseRunning() {
        return this.pulseRunning.getAndSet(true);
    }

    private void endPulseRunning() {
        this.pulseRunning.set(false);
        if (debug) {
            System.err.println("QT.endPulse: " + System.nanoTime());
        }
    }

    void pulseFromQueue() {
        try {
            this.pulse();
        }
        finally {
            this.endPulseRunning();
        }
    }

    protected void pulse() {
        this.pulse(true);
    }

    void pulse(boolean bl) {
        try {
            ++this.inPulse;
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.pulseStart();
            }
            if (!this.toolkitRunning.get()) {
                return;
            }
            this.nextPulseRequested.set(false);
            if (this.animationRunnable != null) {
                this.animationRunning.set(true);
                this.animationRunnable.run();
            } else {
                this.animationRunning.set(false);
            }
            this.firePulse();
            if (bl) {
                this.collector.renderAll();
            }
        }
        finally {
            --this.inPulse;
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.pulseEnd();
            }
        }
    }

    void vsyncHint() {
        if (this.isVsyncEnabled()) {
            if (debug) {
                System.err.println("QT.vsyncHint: postPulse: " + System.nanoTime());
            }
            this.postPulse();
        }
    }

    @Override
    public AppletWindow createAppletWindow(long l, String string) {
        GlassAppletWindow glassAppletWindow = new GlassAppletWindow(l, string);
        WindowStage.setAppletWindow(glassAppletWindow);
        return glassAppletWindow;
    }

    @Override
    public void closeAppletWindow() {
        GlassAppletWindow glassAppletWindow = WindowStage.getAppletWindow();
        if (null != glassAppletWindow) {
            glassAppletWindow.dispose();
            WindowStage.setAppletWindow(null);
        }
    }

    @Override
    public TKStage createTKStage(Window window, boolean bl, StageStyle stageStyle, boolean bl2, Modality modality, TKStage tKStage, boolean bl3, AccessControlContext accessControlContext) {
        this.assertToolkitRunning();
        WindowStage windowStage = new WindowStage(window, bl, stageStyle, modality, tKStage);
        windowStage.setSecurityContext(accessControlContext);
        if (bl2) {
            windowStage.setIsPrimary();
        }
        windowStage.setRTL(bl3);
        windowStage.init(this.systemMenu);
        return windowStage;
    }

    @Override
    public boolean canStartNestedEventLoop() {
        return this.inPulse == 0;
    }

    @Override
    public Object enterNestedEventLoop(Object object) {
        this.checkFxUserThread();
        if (object == null) {
            throw new NullPointerException();
        }
        if (!this.canStartNestedEventLoop()) {
            throw new IllegalStateException("Cannot enter nested loop during animation or layout processing");
        }
        if (this.eventLoopMap == null) {
            this.eventLoopMap = new HashMap();
        }
        if (this.eventLoopMap.containsKey(object)) {
            throw new IllegalArgumentException("Key already associated with a running event loop: " + object);
        }
        EventLoop eventLoop = Application.GetApplication().createEventLoop();
        this.eventLoopMap.put(object, eventLoop);
        Object object2 = eventLoop.enter();
        if (!this.isNestedLoopRunning()) {
            this.notifyLastNestedLoopExited();
        }
        return object2;
    }

    @Override
    public void exitNestedEventLoop(Object object, Object object2) {
        this.checkFxUserThread();
        if (object == null) {
            throw new NullPointerException();
        }
        if (this.eventLoopMap == null || !this.eventLoopMap.containsKey(object)) {
            throw new IllegalArgumentException("Key not associated with a running event loop: " + object);
        }
        EventLoop eventLoop = this.eventLoopMap.get(object);
        this.eventLoopMap.remove(object);
        eventLoop.leave(object2);
    }

    @Override
    public void exitAllNestedEventLoops() {
        this.checkFxUserThread();
        for (EventLoop eventLoop : this.eventLoopMap.values()) {
            eventLoop.leave(null);
        }
        this.eventLoopMap.clear();
        this.eventLoopMap = null;
    }

    @Override
    public TKStage createTKPopupStage(Window window, StageStyle stageStyle, TKStage tKStage, AccessControlContext accessControlContext) {
        this.assertToolkitRunning();
        boolean bl = tKStage instanceof WindowStage ? ((WindowStage)tKStage).isSecurityDialog() : false;
        WindowStage windowStage = new WindowStage(window, bl, stageStyle, null, tKStage);
        windowStage.setSecurityContext(accessControlContext);
        windowStage.setIsPopup();
        windowStage.init(this.systemMenu);
        return windowStage;
    }

    @Override
    public TKStage createTKEmbeddedStage(HostInterface hostInterface, AccessControlContext accessControlContext) {
        this.assertToolkitRunning();
        EmbeddedStage embeddedStage = new EmbeddedStage(hostInterface);
        embeddedStage.setSecurityContext(accessControlContext);
        return embeddedStage;
    }

    @Override
    public ScreenConfigurationAccessor setScreenConfigurationListener(final TKScreenConfigurationListener tKScreenConfigurationListener) {
        com.sun.glass.ui.Screen.setEventHandler(new Screen.EventHandler(){

            @Override
            public void handleSettingsChanged() {
                QuantumToolkit.notifyScreenListener(tKScreenConfigurationListener);
            }
        });
        return screenAccessor;
    }

    private static void assignScreensAdapters() {
        GraphicsPipeline graphicsPipeline = GraphicsPipeline.getPipeline();
        for (com.sun.glass.ui.Screen screen : com.sun.glass.ui.Screen.getScreens()) {
            screen.setAdapterOrdinal(graphicsPipeline.getAdapterOrdinal(screen));
        }
    }

    private static void notifyScreenListener(TKScreenConfigurationListener tKScreenConfigurationListener) {
        QuantumToolkit.assignScreensAdapters();
        tKScreenConfigurationListener.screenConfigurationChanged();
    }

    @Override
    public Object getPrimaryScreen() {
        return com.sun.glass.ui.Screen.getMainScreen();
    }

    @Override
    public List<?> getScreens() {
        return com.sun.glass.ui.Screen.getScreens();
    }

    @Override
    public ScreenConfigurationAccessor getScreenConfigurationAccessor() {
        return screenAccessor;
    }

    @Override
    public PerformanceTracker getPerformanceTracker() {
        return this.perfTracker;
    }

    @Override
    public PerformanceTracker createPerformanceTracker() {
        return new PerformanceTrackerImpl();
    }

    private float getMaxRenderScale() {
        if (this._maxPixelScale == 0.0f) {
            for (Object obj : this.getScreens()) {
                this._maxPixelScale = Math.max(this._maxPixelScale, ((com.sun.glass.ui.Screen)obj).getRecommendedOutputScaleX());
                this._maxPixelScale = Math.max(this._maxPixelScale, ((com.sun.glass.ui.Screen)obj).getRecommendedOutputScaleY());
            }
        }
        return this._maxPixelScale;
    }

    @Override
    public ImageLoader loadImage(String string, double d, double d2, boolean bl, boolean bl2) {
        return new PrismImageLoader2(string, d, d2, bl, this.getMaxRenderScale(), bl2);
    }

    @Override
    public ImageLoader loadImage(InputStream inputStream, double d, double d2, boolean bl, boolean bl2) {
        return new PrismImageLoader2(inputStream, d, d2, bl, bl2);
    }

    public AbstractRemoteResource<? extends ImageLoader> loadImageAsync(AsyncOperationListener asyncOperationListener, String string, double d, double d2, boolean bl, boolean bl2) {
        return new PrismImageLoader2.AsyncImageLoader(asyncOperationListener, string, d, d2, bl, bl2);
    }

    @Override
    public void defer(Runnable runnable) {
        if (!this.toolkitRunning.get()) {
            return;
        }
        Application.invokeLater(runnable);
    }

    @Override
    public void exit() {
        this.checkFxUserThread();
        this.pulseTimer.stop();
        PaintCollector.getInstance().waitForRenderingToComplete();
        this.notifyShutdownHooks();
        QuantumToolkit.runWithRenderLock(() -> {
            Application application = Application.GetApplication();
            application.terminate();
            return null;
        });
        this.dispose();
        super.exit();
    }

    public void dispose() {
        if (this.toolkitRunning.compareAndSet(true, false)) {
            this.pulseTimer.stop();
            this.renderer.stopRenderer();
            try {
                AccessController.doPrivileged(() -> {
                    Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
                    return null;
                });
            }
            catch (IllegalStateException illegalStateException) {
                // empty catch block
            }
        }
    }

    @Override
    public boolean isForwardTraversalKey(KeyEvent keyEvent) {
        return keyEvent.getCode() == KeyCode.TAB && keyEvent.getEventType() == KeyEvent.KEY_PRESSED && !keyEvent.isShiftDown();
    }

    @Override
    public boolean isBackwardTraversalKey(KeyEvent keyEvent) {
        return keyEvent.getCode() == KeyCode.TAB && keyEvent.getEventType() == KeyEvent.KEY_PRESSED && keyEvent.isShiftDown();
    }

    @Override
    public Map<Object, Object> getContextMap() {
        return this.contextMap;
    }

    @Override
    public int getRefreshRate() {
        if (pulseHZ == null) {
            return 60;
        }
        return pulseHZ;
    }

    @Override
    public void setAnimationRunnable(DelayedRunnable delayedRunnable) {
        if (delayedRunnable != null) {
            this.animationRunning.set(true);
        }
        this.animationRunnable = delayedRunnable;
    }

    @Override
    public void requestNextPulse() {
        this.nextPulseRequested.set(true);
    }

    @Override
    public void waitFor(Toolkit.Task task) {
        if (task.isFinished()) {
            return;
        }
    }

    @Override
    protected Object createColorPaint(Color color) {
        return new com.sun.prism.paint.Color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)color.getOpacity());
    }

    private com.sun.prism.paint.Color toPrismColor(Color color) {
        return (com.sun.prism.paint.Color)Toolkit.getPaintAccessor().getPlatformPaint((javafx.scene.paint.Paint)color);
    }

    private List<Stop> convertStops(List<javafx.scene.paint.Stop> list) {
        ArrayList<Stop> arrayList = new ArrayList<Stop>(list.size());
        for (javafx.scene.paint.Stop stop : list) {
            arrayList.add(new Stop(this.toPrismColor(stop.getColor()), (float)stop.getOffset()));
        }
        return arrayList;
    }

    @Override
    protected Object createLinearGradientPaint(javafx.scene.paint.LinearGradient linearGradient) {
        int n = 2;
        CycleMethod cycleMethod = linearGradient.getCycleMethod();
        if (cycleMethod == CycleMethod.NO_CYCLE) {
            n = 0;
        } else if (cycleMethod == CycleMethod.REFLECT) {
            n = 1;
        }
        List<Stop> list = this.convertStops(linearGradient.getStops());
        return new LinearGradient((float)linearGradient.getStartX(), (float)linearGradient.getStartY(), (float)linearGradient.getEndX(), (float)linearGradient.getEndY(), null, linearGradient.isProportional(), n, list);
    }

    @Override
    protected Object createRadialGradientPaint(javafx.scene.paint.RadialGradient radialGradient) {
        float f = (float)radialGradient.getCenterX();
        float f2 = (float)radialGradient.getCenterY();
        float f3 = (float)radialGradient.getFocusAngle();
        float f4 = (float)radialGradient.getFocusDistance();
        int n = 0;
        n = radialGradient.getCycleMethod() == CycleMethod.NO_CYCLE ? 0 : (radialGradient.getCycleMethod() == CycleMethod.REFLECT ? 1 : 2);
        List<Stop> list = this.convertStops(radialGradient.getStops());
        return new RadialGradient(f, f2, f3, f4, (float)radialGradient.getRadius(), null, radialGradient.isProportional(), n, list);
    }

    @Override
    protected Object createImagePatternPaint(ImagePattern imagePattern) {
        if (imagePattern.getImage() == null) {
            return com.sun.prism.paint.Color.TRANSPARENT;
        }
        return new com.sun.prism.paint.ImagePattern((com.sun.prism.Image)Toolkit.getImageAccessor().getPlatformImage(imagePattern.getImage()), (float)imagePattern.getX(), (float)imagePattern.getY(), (float)imagePattern.getWidth(), (float)imagePattern.getHeight(), imagePattern.isProportional(), Toolkit.getPaintAccessor().isMutable((javafx.scene.paint.Paint)imagePattern));
    }

    private void initStroke(StrokeType strokeType, double d, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f, float[] arrf, float f2) {
        int n = strokeType == StrokeType.CENTERED ? 0 : (strokeType == StrokeType.INSIDE ? 1 : 2);
        int n2 = strokeLineCap == StrokeLineCap.BUTT ? 0 : (strokeLineCap == StrokeLineCap.SQUARE ? 2 : 1);
        int n3 = strokeLineJoin == StrokeLineJoin.BEVEL ? 2 : (strokeLineJoin == StrokeLineJoin.MITER ? 0 : 1);
        tmpStroke.set(n, (float)d, n2, n3, f);
        if (arrf != null && arrf.length > 0) {
            tmpStroke.set(arrf, f2);
        } else {
            tmpStroke.set((float[])null, 0.0f);
        }
    }

    @Override
    public void accumulateStrokeBounds(Shape shape, float[] arrf, StrokeType strokeType, double d, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f, BaseTransform baseTransform) {
        this.initStroke(strokeType, d, strokeLineCap, strokeLineJoin, f, null, 0.0f);
        if (baseTransform.isTranslateOrIdentity()) {
            tmpStroke.accumulateShapeBounds(arrf, shape, baseTransform);
        } else {
            Shape.accumulate(arrf, tmpStroke.createStrokedShape(shape), baseTransform);
        }
    }

    @Override
    public boolean strokeContains(Shape shape, double d, double d2, StrokeType strokeType, double d3, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f) {
        this.initStroke(strokeType, d3, strokeLineCap, strokeLineJoin, f, null, 0.0f);
        return tmpStroke.createStrokedShape(shape).contains((float)d, (float)d2);
    }

    @Override
    public Shape createStrokedShape(Shape shape, StrokeType strokeType, double d, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f, float[] arrf, float f2) {
        this.initStroke(strokeType, d, strokeLineCap, strokeLineJoin, f, arrf, f2);
        return tmpStroke.createStrokedShape(shape);
    }

    @Override
    public Dimension2D getBestCursorSize(int n, int n2) {
        return CursorUtils.getBestCursorSize(n, n2);
    }

    @Override
    public int getMaximumCursorColors() {
        return 2;
    }

    @Override
    public int getKeyCodeForChar(String string) {
        return string.length() == 1 ? com.sun.glass.events.KeyEvent.getKeyCodeForChar(string.charAt(0)) : 0;
    }

    @Override
    public PathElement[] convertShapeToFXPath(Object object) {
        if (object == null) {
            return new PathElement[0];
        }
        ArrayList<MoveTo> arrayList = new ArrayList<MoveTo>();
        Shape shape = (Shape)object;
        PathIterator pathIterator = shape.getPathIterator(null);
        PathIteratorHelper pathIteratorHelper = new PathIteratorHelper(pathIterator);
        PathIteratorHelper.Struct struct = new PathIteratorHelper.Struct();
        while (!pathIteratorHelper.isDone()) {
            MoveTo moveTo;
            boolean bl = pathIteratorHelper.getWindingRule() == 0;
            int n = pathIteratorHelper.currentSegment(struct);
            if (n == 0) {
                moveTo = new MoveTo((double)struct.f0, (double)struct.f1);
            } else if (n == 1) {
                moveTo = new LineTo((double)struct.f0, (double)struct.f1);
            } else if (n == 2) {
                moveTo = new QuadCurveTo((double)struct.f0, (double)struct.f1, (double)struct.f2, (double)struct.f3);
            } else if (n == 3) {
                moveTo = new CubicCurveTo((double)struct.f0, (double)struct.f1, (double)struct.f2, (double)struct.f3, (double)struct.f4, (double)struct.f5);
            } else if (n == 4) {
                moveTo = new ClosePath();
            } else {
                throw new IllegalStateException("Invalid element type: " + n);
            }
            pathIteratorHelper.next();
            arrayList.add(moveTo);
        }
        return arrayList.toArray((T[])new PathElement[arrayList.size()]);
    }

    @Override
    public Filterable toFilterable(Image image) {
        return PrImage.create((com.sun.prism.Image)Toolkit.getImageAccessor().getPlatformImage(image));
    }

    @Override
    public FilterContext getFilterContext(Object object) {
        if (object == null || !(object instanceof com.sun.glass.ui.Screen)) {
            return PrFilterContext.getDefaultInstance();
        }
        com.sun.glass.ui.Screen screen = (com.sun.glass.ui.Screen)object;
        return PrFilterContext.getInstance(screen);
    }

    @Override
    public AbstractPrimaryTimer getPrimaryTimer() {
        return PrimaryTimer.getInstance();
    }

    @Override
    public FontLoader getFontLoader() {
        return PrismFontLoader.getInstance();
    }

    @Override
    public TextLayoutFactory getTextLayoutFactory() {
        return PrismTextLayoutFactory.getFactory();
    }

    @Override
    public Object createSVGPathObject(SVGPath sVGPath) {
        int n = sVGPath.getFillRule() == FillRule.NON_ZERO ? 1 : 0;
        Path2D path2D = new Path2D(n);
        path2D.appendSVGPath(sVGPath.getContent());
        return path2D;
    }

    @Override
    public Path2D createSVGPath2D(SVGPath sVGPath) {
        int n = sVGPath.getFillRule() == FillRule.NON_ZERO ? 1 : 0;
        Path2D path2D = new Path2D(n);
        path2D.appendSVGPath(sVGPath.getContent());
        return path2D;
    }

    @Override
    public boolean imageContains(Object object, float f, float f2) {
        if (object == null) {
            return false;
        }
        com.sun.prism.Image image = (com.sun.prism.Image)object;
        int n = (int)f + image.getMinX();
        int n2 = (int)f2 + image.getMinY();
        if (image.isOpaque()) {
            return true;
        }
        if (image.getPixelFormat() == com.sun.prism.PixelFormat.INT_ARGB_PRE) {
            IntBuffer intBuffer = (IntBuffer)image.getPixelBuffer();
            int n3 = n + n2 * image.getRowLength();
            if (n3 >= intBuffer.limit()) {
                return false;
            }
            return (intBuffer.get(n3) & 0xFF000000) != 0;
        }
        if (image.getPixelFormat() == com.sun.prism.PixelFormat.BYTE_BGRA_PRE) {
            ByteBuffer byteBuffer = (ByteBuffer)image.getPixelBuffer();
            int n4 = n * image.getBytesPerPixelUnit() + n2 * image.getScanlineStride() + 3;
            if (n4 >= byteBuffer.limit()) {
                return false;
            }
            return (byteBuffer.get(n4) & 0xFF) != 0;
        }
        if (image.getPixelFormat() == com.sun.prism.PixelFormat.BYTE_ALPHA) {
            ByteBuffer byteBuffer = (ByteBuffer)image.getPixelBuffer();
            int n5 = n * image.getBytesPerPixelUnit() + n2 * image.getScanlineStride();
            if (n5 >= byteBuffer.limit()) {
                return false;
            }
            return (byteBuffer.get(n5) & 0xFF) != 0;
        }
        return true;
    }

    @Override
    public boolean isNestedLoopRunning() {
        return Application.isNestedLoopRunning();
    }

    @Override
    public boolean isSupported(ConditionalFeature conditionalFeature) {
        switch (conditionalFeature) {
            case SCENE3D: {
                return GraphicsPipeline.getPipeline().is3DSupported();
            }
            case EFFECT: {
                return GraphicsPipeline.getPipeline().isEffectSupported();
            }
            case SHAPE_CLIP: {
                return true;
            }
            case INPUT_METHOD: {
                return Application.GetApplication().supportsInputMethods();
            }
            case TRANSPARENT_WINDOW: {
                return Application.GetApplication().supportsTransparentWindows();
            }
            case UNIFIED_WINDOW: {
                return Application.GetApplication().supportsUnifiedWindows();
            }
            case TWO_LEVEL_FOCUS: {
                return Application.GetApplication().hasTwoLevelFocus();
            }
            case VIRTUAL_KEYBOARD: {
                return Application.GetApplication().hasVirtualKeyboard();
            }
            case INPUT_TOUCH: {
                return Application.GetApplication().hasTouch();
            }
            case INPUT_MULTITOUCH: {
                return Application.GetApplication().hasMultiTouch();
            }
            case INPUT_POINTER: {
                return Application.GetApplication().hasPointer();
            }
        }
        return false;
    }

    @Override
    public boolean isMSAASupported() {
        return GraphicsPipeline.getPipeline().isMSAASupported();
    }

    private int toGlassKeyCode(KeyCode keyCode) {
        switch (keyCode) {
            case CAPS: {
                return 20;
            }
            case NUM_LOCK: {
                return 144;
            }
        }
        return 0;
    }

    @Override
    public Optional<Boolean> isKeyLocked(KeyCode keyCode) {
        return Application.GetApplication().isKeyLocked(this.toGlassKeyCode(keyCode));
    }

    static TransferMode clipboardActionToTransferMode(int n) {
        switch (n) {
            case 0: {
                return null;
            }
            case 1: 
            case 0x40000001: {
                return TransferMode.COPY;
            }
            case 2: 
            case 0x40000002: {
                return TransferMode.MOVE;
            }
            case 0x40000000: {
                return TransferMode.LINK;
            }
            case 0x4FFFFFFF: {
                return TransferMode.COPY;
            }
        }
        return null;
    }

    @Override
    public TKClipboard getSystemClipboard() {
        if (this.clipboard == null) {
            this.clipboard = QuantumClipboard.getClipboardInstance(new ClipboardAssistance("SYSTEM"));
        }
        return this.clipboard;
    }

    @Override
    public TKSystemMenu getSystemMenu() {
        return this.systemMenu;
    }

    @Override
    public TKClipboard getNamedClipboard(String string) {
        return null;
    }

    @Override
    public void startDrag(TKScene tKScene, Set<TransferMode> set, TKDragSourceListener tKDragSourceListener, Dragboard dragboard) {
        if (dragboard == null) {
            throw new IllegalArgumentException("dragboard should not be null");
        }
        GlassScene glassScene = (GlassScene)tKScene;
        glassScene.setTKDragSourceListener(tKDragSourceListener);
        QuantumClipboard quantumClipboard = (QuantumClipboard)DragboardHelper.getPeer(dragboard);
        quantumClipboard.setSupportedTransferMode(set);
        quantumClipboard.flush();
        quantumClipboard.close();
    }

    @Override
    public void enableDrop(TKScene tKScene, TKDropTargetListener tKDropTargetListener) {
        assert (tKScene instanceof GlassScene);
        GlassScene glassScene = (GlassScene)tKScene;
        glassScene.setTKDropTargetListener(tKDropTargetListener);
    }

    @Override
    public void registerDragGestureListener(TKScene tKScene, Set<TransferMode> set, TKDragGestureListener tKDragGestureListener) {
        assert (tKScene instanceof GlassScene);
        GlassScene glassScene = (GlassScene)tKScene;
        glassScene.setTKDragGestureListener(tKDragGestureListener);
    }

    @Override
    public void installInputMethodRequests(TKScene tKScene, InputMethodRequests inputMethodRequests) {
        assert (tKScene instanceof GlassScene);
        GlassScene glassScene = (GlassScene)tKScene;
        glassScene.setInputMethodRequests(inputMethodRequests);
    }

    @Override
    public ImageLoader loadPlatformImage(Object object) {
        if (object instanceof QuantumImage) {
            return (QuantumImage)object;
        }
        if (object instanceof com.sun.prism.Image) {
            return new QuantumImage((com.sun.prism.Image)object);
        }
        if (object instanceof PixelBuffer) {
            return new QuantumImage((PixelBuffer<Buffer>)((PixelBuffer)object));
        }
        throw new UnsupportedOperationException("unsupported class for loadPlatformImage");
    }

    @Override
    public PlatformImage createPlatformImage(int n, int n2) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(n * n2 * 4);
        return com.sun.prism.Image.fromByteBgraPreData(byteBuffer, n, n2);
    }

    @Override
    public Object renderToImage(Toolkit.ImageRenderingContext imageRenderingContext) {
        Object object = imageRenderingContext.platformImage;
        final Toolkit.ImageRenderingContext imageRenderingContext2 = imageRenderingContext;
        final Paint paint = imageRenderingContext.platformPaint instanceof Paint ? (Paint)imageRenderingContext.platformPaint : null;
        RenderJob renderJob2 = new RenderJob(new Runnable(){

            private com.sun.prism.paint.Color getClearColor() {
                if (paint == null) {
                    return com.sun.prism.paint.Color.WHITE;
                }
                if (paint.getType() == Paint.Type.COLOR) {
                    return (com.sun.prism.paint.Color)paint;
                }
                if (paint.isOpaque()) {
                    return com.sun.prism.paint.Color.TRANSPARENT;
                }
                return com.sun.prism.paint.Color.WHITE;
            }

            private void draw(Graphics graphics, int n, int n2, int n3, int n4) {
                graphics.setLights(imageRenderingContext2.lights);
                graphics.setDepthBuffer(imageRenderingContext2.depthBuffer);
                graphics.clear(this.getClearColor());
                if (paint != null && paint.getType() != Paint.Type.COLOR) {
                    graphics.getRenderTarget().setOpaque(paint.isOpaque());
                    graphics.setPaint(paint);
                    graphics.fillQuad(0.0f, 0.0f, n3, n4);
                }
                if (n != 0 || n2 != 0) {
                    graphics.translate(-n, -n2);
                }
                if (imageRenderingContext2.transform != null) {
                    graphics.transform(imageRenderingContext2.transform);
                }
                if (imageRenderingContext2.root != null) {
                    if (imageRenderingContext2.camera != null) {
                        graphics.setCamera(imageRenderingContext2.camera);
                    }
                    NGNode nGNode = imageRenderingContext2.root;
                    nGNode.render(graphics);
                }
            }

            private void renderTile(int n, int n2, int n3, int n4, int n5, int n6, IntBuffer intBuffer, ResourceFactory resourceFactory, QuantumImage quantumImage, QuantumImage quantumImage2) {
                RTTexture rTTexture = quantumImage.getRT(n5, n6, resourceFactory);
                if (rTTexture == null) {
                    return;
                }
                Graphics graphics = rTTexture.createGraphics();
                this.draw(graphics, n + n2, n3 + n4, n5, n6);
                int[] arrn = rTTexture.getPixels();
                if (arrn != null) {
                    intBuffer.put(arrn);
                } else {
                    rTTexture.readPixels(intBuffer, rTTexture.getContentX(), rTTexture.getContentY(), n5, n6);
                }
                quantumImage2.image.setPixels(n2, n4, n5, n6, PixelFormat.getIntArgbPreInstance(), intBuffer, n5);
                rTTexture.unlock();
            }

            private void renderWholeImage(int n, int n2, int n3, int n4, ResourceFactory resourceFactory, QuantumImage quantumImage) {
                RTTexture rTTexture = quantumImage.getRT(n3, n4, resourceFactory);
                if (rTTexture == null) {
                    return;
                }
                Graphics graphics = rTTexture.createGraphics();
                this.draw(graphics, n, n2, n3, n4);
                int[] arrn = rTTexture.getPixels();
                if (arrn != null) {
                    quantumImage.setImage(com.sun.prism.Image.fromIntArgbPreData(arrn, n3, n4));
                } else {
                    IntBuffer intBuffer = IntBuffer.allocate(n3 * n4);
                    if (rTTexture.readPixels(intBuffer, rTTexture.getContentX(), rTTexture.getContentY(), n3, n4)) {
                        quantumImage.setImage(com.sun.prism.Image.fromIntArgbPreData(intBuffer, n3, n4));
                    } else {
                        quantumImage.dispose();
                        quantumImage = null;
                    }
                }
                rTTexture.unlock();
            }

            private int computeTileSize(int n, int n2) {
                for (int i = 1; i <= 3; ++i) {
                    int n3 = n / i;
                    if (n3 > n2 || n3 * i != n) continue;
                    return n3;
                }
                return n2;
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void run() {
                ResourceFactory resourceFactory = GraphicsPipeline.getDefaultResourceFactory();
                if (!resourceFactory.isDeviceReady()) {
                    return;
                }
                int n = imageRenderingContext2.x;
                int n2 = imageRenderingContext2.y;
                int n3 = imageRenderingContext2.width;
                int n4 = imageRenderingContext2.height;
                if (n3 <= 0 || n4 <= 0) {
                    return;
                }
                boolean bl = false;
                QuantumImage quantumImage = null;
                try {
                    QuantumImage quantumImage2 = imageRenderingContext2.platformImage instanceof QuantumImage ? (QuantumImage)imageRenderingContext2.platformImage : new QuantumImage((com.sun.prism.Image)null);
                    int n5 = resourceFactory.getMaximumTextureSize();
                    if (n4 > n5 || n3 > n5) {
                        int n6;
                        int n7;
                        quantumImage = new QuantumImage((com.sun.prism.Image)null);
                        if (quantumImage2.image == null) {
                            quantumImage2.setImage(com.sun.prism.Image.fromIntArgbPreData(IntBuffer.allocate(n3 * n4), n3, n4));
                        }
                        int n8 = this.computeTileSize(n3, n5);
                        int n9 = this.computeTileSize(n4, n5);
                        IntBuffer intBuffer = IntBuffer.allocate(n8 * n9);
                        int n10 = 0;
                        int n11 = 0;
                        n10 = 0;
                        while (n10 + n8 <= n3) {
                            n11 = 0;
                            while (n11 + n9 <= n4) {
                                this.renderTile(n, n10, n2, n11, n8, n9, intBuffer, resourceFactory, quantumImage, quantumImage2);
                                n11 += n9;
                            }
                            n10 += n8;
                        }
                        int n12 = n10;
                        int n13 = n3 - n12;
                        if (n13 > 0) {
                            n7 = 0;
                            while (n7 + n9 <= n4) {
                                this.renderTile(n, n12, n2, n7, n13, n9, intBuffer, resourceFactory, quantumImage, quantumImage2);
                                n7 += n9;
                            }
                        }
                        if ((n6 = n4 - (n7 = n11)) > 0) {
                            int n14 = 0;
                            while (n14 + n8 <= n3) {
                                this.renderTile(n, n14, n2, n7, n8, n6, intBuffer, resourceFactory, quantumImage, quantumImage2);
                                n14 += n8;
                            }
                        }
                        if (n13 > 0 && n6 > 0) {
                            this.renderTile(n, n12, n2, n7, n13, n6, intBuffer, resourceFactory, quantumImage, quantumImage2);
                        }
                    } else {
                        this.renderWholeImage(n, n2, n3, n4, resourceFactory, quantumImage2);
                    }
                    imageRenderingContext2.platformImage = quantumImage2;
                }
                catch (Throwable throwable) {
                    bl = true;
                    throwable.printStackTrace(System.err);
                }
                finally {
                    if (quantumImage != null) {
                        quantumImage.dispose();
                    }
                    Disposer.cleanUp();
                    resourceFactory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(bl);
                }
            }
        });
        CountDownLatch countDownLatch = new CountDownLatch(1);
        renderJob2.setCompletionListener(renderJob -> countDownLatch.countDown());
        this.addRenderJob(renderJob2);
        while (true) {
            try {
                countDownLatch.await();
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
                continue;
            }
            break;
        }
        Object object2 = imageRenderingContext2.platformImage;
        imageRenderingContext2.platformImage = object;
        return object2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public CommonDialogs.FileChooserResult showFileChooser(TKStage tKStage, String string, File file, String string2, FileChooserType fileChooserType, List<FileChooser.ExtensionFilter> list, FileChooser.ExtensionFilter extensionFilter) {
        WindowStage windowStage = null;
        try {
            windowStage = this.blockOwnerStage(tKStage);
            CommonDialogs.FileChooserResult fileChooserResult = CommonDialogs.showFileChooser(tKStage instanceof WindowStage ? ((WindowStage)tKStage).getPlatformWindow() : null, file, string2, string, fileChooserType == FileChooserType.SAVE ? 1 : 0, fileChooserType == FileChooserType.OPEN_MULTIPLE, QuantumToolkit.convertExtensionFilters(list), list.indexOf((Object)extensionFilter));
            return fileChooserResult;
        }
        finally {
            if (windowStage != null) {
                windowStage.setEnabled(true);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public File showDirectoryChooser(TKStage tKStage, String string, File file) {
        WindowStage windowStage = null;
        try {
            windowStage = this.blockOwnerStage(tKStage);
            File file2 = CommonDialogs.showFolderChooser(tKStage instanceof WindowStage ? ((WindowStage)tKStage).getPlatformWindow() : null, file, string);
            return file2;
        }
        finally {
            if (windowStage != null) {
                windowStage.setEnabled(true);
            }
        }
    }

    private WindowStage blockOwnerStage(TKStage tKStage) {
        GlassStage glassStage;
        if (tKStage instanceof WindowStage && (glassStage = ((WindowStage)tKStage).getOwner()) instanceof WindowStage) {
            WindowStage windowStage = (WindowStage)glassStage;
            windowStage.setEnabled(false);
            return windowStage;
        }
        return null;
    }

    private static List<CommonDialogs.ExtensionFilter> convertExtensionFilters(List<FileChooser.ExtensionFilter> list) {
        CommonDialogs.ExtensionFilter[] arrextensionFilter = new CommonDialogs.ExtensionFilter[list.size()];
        int n = 0;
        for (FileChooser.ExtensionFilter extensionFilter : list) {
            arrextensionFilter[n++] = new CommonDialogs.ExtensionFilter(extensionFilter.getDescription(), extensionFilter.getExtensions());
        }
        return Arrays.asList(arrextensionFilter);
    }

    @Override
    public long getMultiClickTime() {
        return View.getMultiClickTime();
    }

    @Override
    public int getMultiClickMaxX() {
        return View.getMultiClickMaxX();
    }

    @Override
    public int getMultiClickMaxY() {
        return View.getMultiClickMaxY();
    }

    @Override
    public String getThemeName() {
        return Application.GetApplication().getHighContrastTheme();
    }

    @Override
    public GlassRobot createRobot() {
        return Application.GetApplication().createRobot();
    }

    private class PulseTask {
        private volatile boolean isRunning;

        PulseTask(boolean bl) {
            this.isRunning = bl;
        }

        synchronized void set(boolean bl) {
            this.isRunning = bl;
            if (this.isRunning) {
                QuantumToolkit.this.resumeTimer();
            }
        }

        boolean get() {
            return this.isRunning;
        }
    }

    static class QuantumImage
    implements ImageLoader,
    ResourceFactoryListener {
        private RTTexture rt;
        private com.sun.prism.Image image;
        private ResourceFactory rf;

        QuantumImage(com.sun.prism.Image image) {
            this.image = image;
        }

        QuantumImage(PixelBuffer<Buffer> pixelBuffer) {
            switch (pixelBuffer.getPixelFormat().getType()) {
                case INT_ARGB_PRE: {
                    this.image = com.sun.prism.Image.fromPixelBufferPreData(com.sun.prism.PixelFormat.INT_ARGB_PRE, pixelBuffer.getBuffer(), pixelBuffer.getWidth(), pixelBuffer.getHeight());
                    break;
                }
                case BYTE_BGRA_PRE: {
                    this.image = com.sun.prism.Image.fromPixelBufferPreData(com.sun.prism.PixelFormat.BYTE_BGRA_PRE, pixelBuffer.getBuffer(), pixelBuffer.getWidth(), pixelBuffer.getHeight());
                    break;
                }
                default: {
                    throw new InternalError("Unsupported PixelFormat: " + pixelBuffer.getPixelFormat().getType());
                }
            }
        }

        RTTexture getRT(int n, int n2, ResourceFactory resourceFactory) {
            boolean bl;
            boolean bl2 = bl = this.rt != null && this.rf == resourceFactory && this.rt.getContentWidth() == n && this.rt.getContentHeight() == n2;
            if (bl) {
                this.rt.lock();
                if (this.rt.isSurfaceLost()) {
                    bl = false;
                }
            }
            if (!bl) {
                if (this.rt != null) {
                    this.rt.dispose();
                }
                if (this.rf != null) {
                    this.rf.removeFactoryListener(this);
                    this.rf = null;
                }
                this.rt = resourceFactory.createRTTexture(n, n2, Texture.WrapMode.CLAMP_TO_ZERO);
                if (this.rt != null) {
                    this.rf = resourceFactory;
                    this.rf.addFactoryListener(this);
                }
            }
            return this.rt;
        }

        void dispose() {
            if (this.rt != null) {
                this.rt.dispose();
                this.rt = null;
            }
        }

        void setImage(com.sun.prism.Image image) {
            this.image = image;
        }

        @Override
        public Exception getException() {
            return this.image == null ? new IllegalStateException("Unitialized image") : null;
        }

        @Override
        public int getFrameCount() {
            return 1;
        }

        @Override
        public PlatformImage getFrame(int n) {
            return this.image;
        }

        @Override
        public int getFrameDelay(int n) {
            return 0;
        }

        @Override
        public int getLoopCount() {
            return 0;
        }

        @Override
        public double getWidth() {
            return this.image.getWidth();
        }

        @Override
        public double getHeight() {
            return this.image.getHeight();
        }

        @Override
        public void factoryReset() {
            this.dispose();
        }

        @Override
        public void factoryReleased() {
            this.dispose();
            if (this.rf != null) {
                this.rf.removeFactoryListener(this);
                this.rf = null;
            }
        }
    }
}

