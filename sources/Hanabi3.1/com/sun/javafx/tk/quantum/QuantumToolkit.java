package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.EventLoop;
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
import com.sun.javafx.scene.text.HitInfo;
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
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Paint;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.animation.AbstractPrimaryTimer;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import com.sun.scenario.effect.impl.prism.PrImage;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import javafx.application.ConditionalFeature;
import javafx.geometry.Dimension2D;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
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
import javafx.stage.StageStyle;
import javafx.stage.Window;

public final class QuantumToolkit extends Toolkit {
   public static final boolean verbose = (Boolean)AccessController.doPrivileged(() -> {
      return Boolean.getBoolean("quantum.verbose");
   });
   public static final boolean pulseDebug = (Boolean)AccessController.doPrivileged(() -> {
      return Boolean.getBoolean("quantum.pulse");
   });
   private static final boolean multithreaded = (Boolean)AccessController.doPrivileged(() -> {
      String var0 = System.getProperty("quantum.multithreaded");
      if (var0 == null) {
         return true;
      } else {
         boolean var1 = Boolean.parseBoolean(var0);
         if (verbose) {
            System.out.println(var1 ? "Multi-Threading Enabled" : "Multi-Threading Disabled");
         }

         return var1;
      }
   });
   private static boolean debug = (Boolean)AccessController.doPrivileged(() -> {
      return Boolean.getBoolean("quantum.debug");
   });
   private static Integer pulseHZ = (Integer)AccessController.doPrivileged(() -> {
      return Integer.getInteger("javafx.animation.pulse");
   });
   static final boolean liveResize = (Boolean)AccessController.doPrivileged(() -> {
      boolean var0 = "swt".equals(System.getProperty("glass.platform"));
      String var1 = (PlatformUtil.isMac() || PlatformUtil.isWindows()) && !var0 ? "true" : "false";
      return "true".equals(System.getProperty("javafx.live.resize", var1));
   });
   static final boolean drawInPaint = (Boolean)AccessController.doPrivileged(() -> {
      boolean var0 = "swt".equals(System.getProperty("glass.platform"));
      String var1 = PlatformUtil.isMac() && var0 ? "true" : "false";
      return "true".equals(System.getProperty("javafx.draw.in.paint", var1));
   });
   private static boolean singleThreaded = (Boolean)AccessController.doPrivileged(() -> {
      Boolean var0 = Boolean.getBoolean("quantum.singlethreaded");
      if (var0) {
         System.out.println("Warning: Single GUI Threadiong is enabled, FPS should be slower");
      }

      return var0;
   });
   private static boolean noRenderJobs = (Boolean)AccessController.doPrivileged(() -> {
      Boolean var0 = Boolean.getBoolean("quantum.norenderjobs");
      if (var0) {
         System.out.println("Warning: Quantum will not submit render jobs, nothing should draw");
      }

      return var0;
   });
   private AtomicBoolean toolkitRunning = new AtomicBoolean(false);
   private PulseTask animationRunning = new PulseTask(false);
   private PulseTask nextPulseRequested = new PulseTask(false);
   private AtomicBoolean pulseRunning = new AtomicBoolean(false);
   private int inPulse = 0;
   private CountDownLatch launchLatch = new CountDownLatch(1);
   final int PULSE_INTERVAL;
   final int FULLSPEED_INTERVAL;
   boolean nativeSystemVsync;
   private long firstPauseRequestTime;
   private boolean pauseRequested;
   private static final long PAUSE_THRESHOLD_DURATION = 250L;
   private float _maxPixelScale;
   private Runnable pulseRunnable;
   private Runnable userRunnable;
   private Runnable timerRunnable;
   private Timer pulseTimer;
   private Thread shutdownHook;
   private PaintCollector collector;
   private QuantumRenderer renderer;
   private GraphicsPipeline pipeline;
   private ClassLoader ccl;
   private HashMap eventLoopMap;
   private final PerformanceTracker perfTracker;
   private static ScreenConfigurationAccessor screenAccessor = new ScreenConfigurationAccessor() {
      public int getMinX(Object var1) {
         return ((Screen)var1).getX();
      }

      public int getMinY(Object var1) {
         return ((Screen)var1).getY();
      }

      public int getWidth(Object var1) {
         return ((Screen)var1).getWidth();
      }

      public int getHeight(Object var1) {
         return ((Screen)var1).getHeight();
      }

      public int getVisualMinX(Object var1) {
         return ((Screen)var1).getVisibleX();
      }

      public int getVisualMinY(Object var1) {
         return ((Screen)var1).getVisibleY();
      }

      public int getVisualWidth(Object var1) {
         return ((Screen)var1).getVisibleWidth();
      }

      public int getVisualHeight(Object var1) {
         return ((Screen)var1).getVisibleHeight();
      }

      public float getDPI(Object var1) {
         return (float)((Screen)var1).getResolutionX();
      }

      public float getRecommendedOutputScaleX(Object var1) {
         return ((Screen)var1).getRecommendedOutputScaleX();
      }

      public float getRecommendedOutputScaleY(Object var1) {
         return ((Screen)var1).getRecommendedOutputScaleY();
      }
   };
   private Map contextMap;
   private DelayedRunnable animationRunnable;
   static BasicStroke tmpStroke = new BasicStroke();
   private QuantumClipboard clipboard;
   private GlassSystemMenu systemMenu;

   public QuantumToolkit() {
      this.PULSE_INTERVAL = (int)(TimeUnit.SECONDS.toMillis(1L) / (long)this.getRefreshRate());
      this.FULLSPEED_INTERVAL = 1;
      this.nativeSystemVsync = false;
      this.firstPauseRequestTime = 0L;
      this.pauseRequested = false;
      this.pulseTimer = null;
      this.shutdownHook = null;
      this.eventLoopMap = null;
      this.perfTracker = new PerformanceTrackerImpl();
      this.contextMap = Collections.synchronizedMap(new HashMap());
      this.systemMenu = new GlassSystemMenu();
   }

   public boolean init() {
      this.renderer = QuantumRenderer.getInstance();
      this.collector = PaintCollector.createInstance(this);
      this.pipeline = GraphicsPipeline.getPipeline();
      this.shutdownHook = new Thread("Glass/Prism Shutdown Hook") {
         public void run() {
            QuantumToolkit.this.dispose();
         }
      };
      AccessController.doPrivileged(() -> {
         Runtime.getRuntime().addShutdownHook(this.shutdownHook);
         return null;
      });
      return true;
   }

   public void startup(Runnable var1) {
      this.ccl = Thread.currentThread().getContextClassLoader();

      try {
         this.userRunnable = var1;
         Application.run(() -> {
            this.runToolkit();
         });
      } catch (RuntimeException var4) {
         if (verbose) {
            var4.printStackTrace();
         }

         throw var4;
      } catch (Throwable var5) {
         if (verbose) {
            var5.printStackTrace();
         }

         throw new RuntimeException(var5);
      }

      try {
         this.launchLatch.await();
      } catch (InterruptedException var3) {
         var3.printStackTrace();
      }

   }

   private void assertToolkitRunning() {
   }

   boolean shouldWaitForRenderingToComplete() {
      return !multithreaded;
   }

   private static void initSceneGraph() {
      javafx.stage.Screen.getPrimary();
   }

   void runToolkit() {
      Thread var1 = Thread.currentThread();
      if (!this.toolkitRunning.getAndSet(true)) {
         var1.setName("JavaFX Application Thread");
         var1.setContextClassLoader(this.ccl);
         setFxUserThread(var1);
         assignScreensAdapters();
         this.renderer.createResourceFactory();
         this.pulseRunnable = () -> {
            this.pulseFromQueue();
         };
         this.timerRunnable = () -> {
            try {
               this.postPulse();
            } catch (Throwable var2) {
               var2.printStackTrace(System.err);
            }

         };
         this.pulseTimer = Application.GetApplication().createTimer(this.timerRunnable);
         Application.GetApplication().setEventHandler(new Application.EventHandler() {
            public void handleQuitAction(Application var1, long var2) {
               GlassStage.requestClosingAllWindows();
            }

            public boolean handleThemeChanged(String var1) {
               return PlatformImpl.setAccessibilityTheme(var1);
            }
         });
      }

      initSceneGraph();
      this.launchLatch.countDown();

      try {
         Application.invokeAndWait(this.userRunnable);
         if (this.getPrimaryTimer().isFullspeed()) {
            this.pulseTimer.start(1);
         } else {
            this.nativeSystemVsync = Screen.getVideoRefreshPeriod() != 0.0;
            if (this.nativeSystemVsync) {
               this.pulseTimer.start();
            } else {
               this.pulseTimer.start(this.PULSE_INTERVAL);
            }
         }
      } catch (Throwable var6) {
         var6.printStackTrace(System.err);
      } finally {
         if (PrismSettings.verbose) {
            System.err.println(" vsync: " + PrismSettings.isVsyncEnabled + " vpipe: " + this.pipeline.isVsyncSupported());
         }

         PerformanceTracker.logEvent("Toolkit.startup - finished");
      }

   }

   public static Object runWithoutRenderLock(Supplier var0) {
      boolean var1 = ViewPainter.renderLock.isHeldByCurrentThread();

      Object var2;
      try {
         if (var1) {
            ViewPainter.renderLock.unlock();
         }

         var2 = var0.get();
      } finally {
         if (var1) {
            ViewPainter.renderLock.lock();
         }

      }

      return var2;
   }

   public static Object runWithRenderLock(Supplier var0) {
      ViewPainter.renderLock.lock();

      Object var1;
      try {
         var1 = var0.get();
      } finally {
         ViewPainter.renderLock.unlock();
      }

      return var1;
   }

   boolean hasNativeSystemVsync() {
      return this.nativeSystemVsync;
   }

   boolean isVsyncEnabled() {
      return PrismSettings.isVsyncEnabled && this.pipeline.isVsyncSupported();
   }

   public void checkFxUserThread() {
      super.checkFxUserThread();
      this.renderer.checkRendererIdle();
   }

   protected static Thread getFxUserThread() {
      return Toolkit.getFxUserThread();
   }

   public Future addRenderJob(RenderJob var1) {
      if (noRenderJobs) {
         CompletionListener var2 = var1.getCompletionListener();
         if (var1 instanceof PaintRenderJob) {
            ((PaintRenderJob)var1).getScene().setPainting(false);
         }

         if (var2 != null) {
            try {
               var2.done(var1);
            } catch (Throwable var4) {
               var4.printStackTrace();
            }
         }

         return null;
      } else if (singleThreaded) {
         var1.run();
         return null;
      } else {
         return this.renderer.submitRenderJob(var1);
      }
   }

   void postPulse() {
      if (this.toolkitRunning.get() && (this.animationRunning.get() || this.nextPulseRequested.get()) && !this.setPulseRunning()) {
         Application.invokeLater(this.pulseRunnable);
         if (debug) {
            System.err.println("QT.postPulse@(" + System.nanoTime() + "): " + this.pulseString());
         }
      } else if (!this.animationRunning.get() && !this.nextPulseRequested.get() && !this.pulseRunning.get()) {
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
      } finally {
         this.endPulseRunning();
      }

   }

   protected void pulse() {
      this.pulse(true);
   }

   void pulse(boolean var1) {
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
         if (var1) {
            this.collector.renderAll();
         }
      } finally {
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

   public AppletWindow createAppletWindow(long var1, String var3) {
      GlassAppletWindow var4 = new GlassAppletWindow(var1, var3);
      WindowStage.setAppletWindow(var4);
      return var4;
   }

   public void closeAppletWindow() {
      GlassAppletWindow var1 = WindowStage.getAppletWindow();
      if (null != var1) {
         var1.dispose();
         WindowStage.setAppletWindow((GlassAppletWindow)null);
      }

   }

   public TKStage createTKStage(Window var1, boolean var2, StageStyle var3, boolean var4, Modality var5, TKStage var6, boolean var7, AccessControlContext var8) {
      this.assertToolkitRunning();
      WindowStage var9 = new WindowStage(var1, var2, var3, var5, var6);
      var9.setSecurityContext(var8);
      if (var4) {
         var9.setIsPrimary();
      }

      var9.setRTL(var7);
      var9.init(this.systemMenu);
      return var9;
   }

   public boolean canStartNestedEventLoop() {
      return this.inPulse == 0;
   }

   public Object enterNestedEventLoop(Object var1) {
      this.checkFxUserThread();
      if (var1 == null) {
         throw new NullPointerException();
      } else if (!this.canStartNestedEventLoop()) {
         throw new IllegalStateException("Cannot enter nested loop during animation or layout processing");
      } else {
         if (this.eventLoopMap == null) {
            this.eventLoopMap = new HashMap();
         }

         if (this.eventLoopMap.containsKey(var1)) {
            throw new IllegalArgumentException("Key already associated with a running event loop: " + var1);
         } else {
            EventLoop var2 = Application.GetApplication().createEventLoop();
            this.eventLoopMap.put(var1, var2);
            Object var3 = var2.enter();
            if (!this.isNestedLoopRunning()) {
               this.notifyLastNestedLoopExited();
            }

            return var3;
         }
      }
   }

   public void exitNestedEventLoop(Object var1, Object var2) {
      this.checkFxUserThread();
      if (var1 == null) {
         throw new NullPointerException();
      } else if (this.eventLoopMap != null && this.eventLoopMap.containsKey(var1)) {
         EventLoop var3 = (EventLoop)this.eventLoopMap.get(var1);
         this.eventLoopMap.remove(var1);
         var3.leave(var2);
      } else {
         throw new IllegalArgumentException("Key not associated with a running event loop: " + var1);
      }
   }

   public TKStage createTKPopupStage(Window var1, StageStyle var2, TKStage var3, AccessControlContext var4) {
      this.assertToolkitRunning();
      boolean var5 = var3 instanceof WindowStage ? ((WindowStage)var3).isSecurityDialog() : false;
      WindowStage var6 = new WindowStage(var1, var5, var2, (Modality)null, var3);
      var6.setSecurityContext(var4);
      var6.setIsPopup();
      var6.init(this.systemMenu);
      return var6;
   }

   public TKStage createTKEmbeddedStage(HostInterface var1, AccessControlContext var2) {
      this.assertToolkitRunning();
      EmbeddedStage var3 = new EmbeddedStage(var1);
      var3.setSecurityContext(var2);
      return var3;
   }

   public ScreenConfigurationAccessor setScreenConfigurationListener(final TKScreenConfigurationListener var1) {
      Screen.setEventHandler(new Screen.EventHandler() {
         public void handleSettingsChanged() {
            QuantumToolkit.notifyScreenListener(var1);
         }
      });
      return screenAccessor;
   }

   private static void assignScreensAdapters() {
      GraphicsPipeline var0 = GraphicsPipeline.getPipeline();
      Iterator var1 = Screen.getScreens().iterator();

      while(var1.hasNext()) {
         Screen var2 = (Screen)var1.next();
         var2.setAdapterOrdinal(var0.getAdapterOrdinal(var2));
      }

   }

   private static void notifyScreenListener(TKScreenConfigurationListener var0) {
      assignScreensAdapters();
      var0.screenConfigurationChanged();
   }

   public Object getPrimaryScreen() {
      return Screen.getMainScreen();
   }

   public List getScreens() {
      return Screen.getScreens();
   }

   public ScreenConfigurationAccessor getScreenConfigurationAccessor() {
      return screenAccessor;
   }

   public PerformanceTracker getPerformanceTracker() {
      return this.perfTracker;
   }

   public PerformanceTracker createPerformanceTracker() {
      return new PerformanceTrackerImpl();
   }

   private float getMaxRenderScale() {
      Object var2;
      if (this._maxPixelScale == 0.0F) {
         for(Iterator var1 = this.getScreens().iterator(); var1.hasNext(); this._maxPixelScale = Math.max(this._maxPixelScale, ((Screen)var2).getRecommendedOutputScaleY())) {
            var2 = var1.next();
            this._maxPixelScale = Math.max(this._maxPixelScale, ((Screen)var2).getRecommendedOutputScaleX());
         }
      }

      return this._maxPixelScale;
   }

   public ImageLoader loadImage(String var1, int var2, int var3, boolean var4, boolean var5) {
      return new PrismImageLoader2(var1, var2, var3, var4, this.getMaxRenderScale(), var5);
   }

   public ImageLoader loadImage(InputStream var1, int var2, int var3, boolean var4, boolean var5) {
      return new PrismImageLoader2(var1, var2, var3, var4, var5);
   }

   public AbstractRemoteResource loadImageAsync(AsyncOperationListener var1, String var2, int var3, int var4, boolean var5, boolean var6) {
      return new PrismImageLoader2.AsyncImageLoader(var1, var2, var3, var4, var5, var6);
   }

   public void defer(Runnable var1) {
      if (this.toolkitRunning.get()) {
         Application.invokeLater(var1);
      }
   }

   public void exit() {
      this.checkFxUserThread();
      this.pulseTimer.stop();
      PaintCollector.getInstance().waitForRenderingToComplete();
      this.notifyShutdownHooks();
      runWithRenderLock(() -> {
         Application var0 = Application.GetApplication();
         var0.terminate();
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
         } catch (IllegalStateException var2) {
         }
      }

   }

   public boolean isForwardTraversalKey(KeyEvent var1) {
      return var1.getCode() == KeyCode.TAB && var1.getEventType() == KeyEvent.KEY_PRESSED && !var1.isShiftDown();
   }

   public boolean isBackwardTraversalKey(KeyEvent var1) {
      return var1.getCode() == KeyCode.TAB && var1.getEventType() == KeyEvent.KEY_PRESSED && var1.isShiftDown();
   }

   public Map getContextMap() {
      return this.contextMap;
   }

   public int getRefreshRate() {
      return pulseHZ == null ? 60 : pulseHZ;
   }

   public void setAnimationRunnable(DelayedRunnable var1) {
      if (var1 != null) {
         this.animationRunning.set(true);
      }

      this.animationRunnable = var1;
   }

   public void requestNextPulse() {
      this.nextPulseRequested.set(true);
   }

   public void waitFor(Toolkit.Task var1) {
      if (!var1.isFinished()) {
         ;
      }
   }

   protected Object createColorPaint(Color var1) {
      return new com.sun.prism.paint.Color((float)var1.getRed(), (float)var1.getGreen(), (float)var1.getBlue(), (float)var1.getOpacity());
   }

   private com.sun.prism.paint.Color toPrismColor(Color var1) {
      return (com.sun.prism.paint.Color)Toolkit.getPaintAccessor().getPlatformPaint(var1);
   }

   private List convertStops(List var1) {
      ArrayList var2 = new ArrayList(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Stop var4 = (Stop)var3.next();
         var2.add(new com.sun.prism.paint.Stop(this.toPrismColor(var4.getColor()), (float)var4.getOffset()));
      }

      return var2;
   }

   protected Object createLinearGradientPaint(LinearGradient var1) {
      byte var2 = 2;
      CycleMethod var3 = var1.getCycleMethod();
      if (var3 == CycleMethod.NO_CYCLE) {
         var2 = 0;
      } else if (var3 == CycleMethod.REFLECT) {
         var2 = 1;
      }

      List var4 = this.convertStops(var1.getStops());
      return new com.sun.prism.paint.LinearGradient((float)var1.getStartX(), (float)var1.getStartY(), (float)var1.getEndX(), (float)var1.getEndY(), (BaseTransform)null, var1.isProportional(), var2, var4);
   }

   protected Object createRadialGradientPaint(RadialGradient var1) {
      float var2 = (float)var1.getCenterX();
      float var3 = (float)var1.getCenterY();
      float var4 = (float)var1.getFocusAngle();
      float var5 = (float)var1.getFocusDistance();
      boolean var6 = false;
      byte var8;
      if (var1.getCycleMethod() == CycleMethod.NO_CYCLE) {
         var8 = 0;
      } else if (var1.getCycleMethod() == CycleMethod.REFLECT) {
         var8 = 1;
      } else {
         var8 = 2;
      }

      List var7 = this.convertStops(var1.getStops());
      return new com.sun.prism.paint.RadialGradient(var2, var3, var4, var5, (float)var1.getRadius(), (BaseTransform)null, var1.isProportional(), var8, var7);
   }

   protected Object createImagePatternPaint(ImagePattern var1) {
      return var1.getImage() == null ? com.sun.prism.paint.Color.TRANSPARENT : new com.sun.prism.paint.ImagePattern((Image)var1.getImage().impl_getPlatformImage(), (float)var1.getX(), (float)var1.getY(), (float)var1.getWidth(), (float)var1.getHeight(), var1.isProportional(), Toolkit.getPaintAccessor().isMutable(var1));
   }

   private void initStroke(StrokeType var1, double var2, StrokeLineCap var4, StrokeLineJoin var5, float var6, float[] var7, float var8) {
      byte var9;
      if (var1 == StrokeType.CENTERED) {
         var9 = 0;
      } else if (var1 == StrokeType.INSIDE) {
         var9 = 1;
      } else {
         var9 = 2;
      }

      byte var10;
      if (var4 == StrokeLineCap.BUTT) {
         var10 = 0;
      } else if (var4 == StrokeLineCap.SQUARE) {
         var10 = 2;
      } else {
         var10 = 1;
      }

      byte var11;
      if (var5 == StrokeLineJoin.BEVEL) {
         var11 = 2;
      } else if (var5 == StrokeLineJoin.MITER) {
         var11 = 0;
      } else {
         var11 = 1;
      }

      tmpStroke.set(var9, (float)var2, var10, var11, var6);
      if (var7 != null && var7.length > 0) {
         tmpStroke.set(var7, var8);
      } else {
         tmpStroke.set((float[])null, 0.0F);
      }

   }

   public void accumulateStrokeBounds(Shape var1, float[] var2, StrokeType var3, double var4, StrokeLineCap var6, StrokeLineJoin var7, float var8, BaseTransform var9) {
      this.initStroke(var3, var4, var6, var7, var8, (float[])null, 0.0F);
      if (var9.isTranslateOrIdentity()) {
         tmpStroke.accumulateShapeBounds(var2, var1, var9);
      } else {
         Shape.accumulate(var2, tmpStroke.createStrokedShape(var1), var9);
      }

   }

   public boolean strokeContains(Shape var1, double var2, double var4, StrokeType var6, double var7, StrokeLineCap var9, StrokeLineJoin var10, float var11) {
      this.initStroke(var6, var7, var9, var10, var11, (float[])null, 0.0F);
      return tmpStroke.createStrokedShape(var1).contains((float)var2, (float)var4);
   }

   public Shape createStrokedShape(Shape var1, StrokeType var2, double var3, StrokeLineCap var5, StrokeLineJoin var6, float var7, float[] var8, float var9) {
      this.initStroke(var2, var3, var5, var6, var7, var8, var9);
      return tmpStroke.createStrokedShape(var1);
   }

   public Dimension2D getBestCursorSize(int var1, int var2) {
      return CursorUtils.getBestCursorSize(var1, var2);
   }

   public int getMaximumCursorColors() {
      return 2;
   }

   public int getKeyCodeForChar(String var1) {
      return var1.length() == 1 ? com.sun.glass.events.KeyEvent.getKeyCodeForChar(var1.charAt(0)) : 0;
   }

   public PathElement[] convertShapeToFXPath(Object var1) {
      if (var1 == null) {
         return new PathElement[0];
      } else {
         ArrayList var2 = new ArrayList();
         Shape var3 = (Shape)var1;
         PathIterator var4 = var3.getPathIterator((BaseTransform)null);
         PathIteratorHelper var5 = new PathIteratorHelper(var4);
         PathIteratorHelper.Struct var6 = new PathIteratorHelper.Struct();

         while(!var5.isDone()) {
            boolean var7 = var5.getWindingRule() == 0;
            int var8 = var5.currentSegment(var6);
            Object var9;
            if (var8 == 0) {
               var9 = new MoveTo((double)var6.f0, (double)var6.f1);
            } else if (var8 == 1) {
               var9 = new LineTo((double)var6.f0, (double)var6.f1);
            } else if (var8 == 2) {
               var9 = new QuadCurveTo((double)var6.f0, (double)var6.f1, (double)var6.f2, (double)var6.f3);
            } else if (var8 == 3) {
               var9 = new CubicCurveTo((double)var6.f0, (double)var6.f1, (double)var6.f2, (double)var6.f3, (double)var6.f4, (double)var6.f5);
            } else {
               if (var8 != 4) {
                  throw new IllegalStateException("Invalid element type: " + var8);
               }

               var9 = new ClosePath();
            }

            var5.next();
            var2.add(var9);
         }

         return (PathElement[])var2.toArray(new PathElement[var2.size()]);
      }
   }

   public HitInfo convertHitInfoToFX(Object var1) {
      Integer var2 = (Integer)var1;
      HitInfo var3 = new HitInfo();
      var3.setCharIndex(var2);
      var3.setLeading(true);
      return var3;
   }

   public Filterable toFilterable(javafx.scene.image.Image var1) {
      return PrImage.create((Image)var1.impl_getPlatformImage());
   }

   public FilterContext getFilterContext(Object var1) {
      if (var1 != null && var1 instanceof Screen) {
         Screen var2 = (Screen)var1;
         return PrFilterContext.getInstance(var2);
      } else {
         return PrFilterContext.getDefaultInstance();
      }
   }

   public AbstractPrimaryTimer getPrimaryTimer() {
      return PrimaryTimer.getInstance();
   }

   public FontLoader getFontLoader() {
      return PrismFontLoader.getInstance();
   }

   public TextLayoutFactory getTextLayoutFactory() {
      return PrismTextLayoutFactory.getFactory();
   }

   public Object createSVGPathObject(SVGPath var1) {
      int var2 = var1.getFillRule() == FillRule.NON_ZERO ? 1 : 0;
      Path2D var3 = new Path2D(var2);
      var3.appendSVGPath(var1.getContent());
      return var3;
   }

   public Path2D createSVGPath2D(SVGPath var1) {
      int var2 = var1.getFillRule() == FillRule.NON_ZERO ? 1 : 0;
      Path2D var3 = new Path2D(var2);
      var3.appendSVGPath(var1.getContent());
      return var3;
   }

   public boolean imageContains(Object var1, float var2, float var3) {
      if (var1 == null) {
         return false;
      } else {
         Image var4 = (Image)var1;
         int var5 = (int)var2 + var4.getMinX();
         int var6 = (int)var3 + var4.getMinY();
         if (var4.isOpaque()) {
            return true;
         } else {
            int var8;
            if (var4.getPixelFormat() == PixelFormat.INT_ARGB_PRE) {
               IntBuffer var9 = (IntBuffer)var4.getPixelBuffer();
               var8 = var5 + var6 * var4.getRowLength();
               if (var8 >= var9.limit()) {
                  return false;
               } else {
                  return (var9.get(var8) & -16777216) != 0;
               }
            } else {
               ByteBuffer var7;
               if (var4.getPixelFormat() == PixelFormat.BYTE_BGRA_PRE) {
                  var7 = (ByteBuffer)var4.getPixelBuffer();
                  var8 = var5 * var4.getBytesPerPixelUnit() + var6 * var4.getScanlineStride() + 3;
                  if (var8 >= var7.limit()) {
                     return false;
                  } else {
                     return (var7.get(var8) & 255) != 0;
                  }
               } else if (var4.getPixelFormat() == PixelFormat.BYTE_ALPHA) {
                  var7 = (ByteBuffer)var4.getPixelBuffer();
                  var8 = var5 * var4.getBytesPerPixelUnit() + var6 * var4.getScanlineStride();
                  if (var8 >= var7.limit()) {
                     return false;
                  } else {
                     return (var7.get(var8) & 255) != 0;
                  }
               } else {
                  return true;
               }
            }
         }
      }
   }

   public boolean isNestedLoopRunning() {
      return Application.isNestedLoopRunning();
   }

   public boolean isSupported(ConditionalFeature var1) {
      switch (var1) {
         case SCENE3D:
            return GraphicsPipeline.getPipeline().is3DSupported();
         case EFFECT:
            return GraphicsPipeline.getPipeline().isEffectSupported();
         case SHAPE_CLIP:
            return true;
         case INPUT_METHOD:
            return Application.GetApplication().supportsInputMethods();
         case TRANSPARENT_WINDOW:
            return Application.GetApplication().supportsTransparentWindows();
         case UNIFIED_WINDOW:
            return Application.GetApplication().supportsUnifiedWindows();
         case TWO_LEVEL_FOCUS:
            return Application.GetApplication().hasTwoLevelFocus();
         case VIRTUAL_KEYBOARD:
            return Application.GetApplication().hasVirtualKeyboard();
         case INPUT_TOUCH:
            return Application.GetApplication().hasTouch();
         case INPUT_MULTITOUCH:
            return Application.GetApplication().hasMultiTouch();
         case INPUT_POINTER:
            return Application.GetApplication().hasPointer();
         default:
            return false;
      }
   }

   public boolean isMSAASupported() {
      return GraphicsPipeline.getPipeline().isMSAASupported();
   }

   static TransferMode clipboardActionToTransferMode(int var0) {
      switch (var0) {
         case 0:
            return null;
         case 1:
         case 1073741825:
            return TransferMode.COPY;
         case 2:
         case 1073741826:
            return TransferMode.MOVE;
         case 1073741824:
            return TransferMode.LINK;
         case 1342177279:
            return TransferMode.COPY;
         default:
            return null;
      }
   }

   public TKClipboard getSystemClipboard() {
      if (this.clipboard == null) {
         this.clipboard = QuantumClipboard.getClipboardInstance(new ClipboardAssistance("SYSTEM"));
      }

      return this.clipboard;
   }

   public TKSystemMenu getSystemMenu() {
      return this.systemMenu;
   }

   public TKClipboard getNamedClipboard(String var1) {
      return null;
   }

   public void startDrag(TKScene var1, Set var2, TKDragSourceListener var3, Dragboard var4) {
      if (var4 == null) {
         throw new IllegalArgumentException("dragboard should not be null");
      } else {
         GlassScene var5 = (GlassScene)var1;
         var5.setTKDragSourceListener(var3);
         QuantumClipboard var6 = (QuantumClipboard)var4.impl_getPeer();
         var6.setSupportedTransferMode(var2);
         var6.flush();
         var6.close();
      }
   }

   public void enableDrop(TKScene var1, TKDropTargetListener var2) {
      assert var1 instanceof GlassScene;

      GlassScene var3 = (GlassScene)var1;
      var3.setTKDropTargetListener(var2);
   }

   public void registerDragGestureListener(TKScene var1, Set var2, TKDragGestureListener var3) {
      assert var1 instanceof GlassScene;

      GlassScene var4 = (GlassScene)var1;
      var4.setTKDragGestureListener(var3);
   }

   public void installInputMethodRequests(TKScene var1, InputMethodRequests var2) {
      assert var1 instanceof GlassScene;

      GlassScene var3 = (GlassScene)var1;
      var3.setInputMethodRequests(var2);
   }

   public ImageLoader loadPlatformImage(Object var1) {
      if (var1 instanceof QuantumImage) {
         return (QuantumImage)var1;
      } else if (var1 instanceof Image) {
         return new QuantumImage((Image)var1);
      } else {
         throw new UnsupportedOperationException("unsupported class for loadPlatformImage");
      }
   }

   public PlatformImage createPlatformImage(int var1, int var2) {
      ByteBuffer var3 = ByteBuffer.allocate(var1 * var2 * 4);
      return Image.fromByteBgraPreData(var3, var1, var2);
   }

   public Object renderToImage(final Toolkit.ImageRenderingContext var1) {
      Object var2 = var1.platformImage;
      final Paint var4 = var1.platformPaint instanceof Paint ? (Paint)var1.platformPaint : null;
      RenderJob var5 = new RenderJob(new Runnable() {
         private com.sun.prism.paint.Color getClearColor() {
            if (var4 == null) {
               return com.sun.prism.paint.Color.WHITE;
            } else if (var4.getType() == Paint.Type.COLOR) {
               return (com.sun.prism.paint.Color)var4;
            } else {
               return var4.isOpaque() ? com.sun.prism.paint.Color.TRANSPARENT : com.sun.prism.paint.Color.WHITE;
            }
         }

         private void draw(Graphics var1x, int var2, int var3, int var4x, int var5) {
            var1x.setLights(var1.lights);
            var1x.setDepthBuffer(var1.depthBuffer);
            var1x.clear(this.getClearColor());
            if (var4 != null && var4.getType() != Paint.Type.COLOR) {
               var1x.getRenderTarget().setOpaque(var4.isOpaque());
               var1x.setPaint(var4);
               var1x.fillQuad(0.0F, 0.0F, (float)var4x, (float)var5);
            }

            if (var2 != 0 || var3 != 0) {
               var1x.translate((float)(-var2), (float)(-var3));
            }

            if (var1.transform != null) {
               var1x.transform(var1.transform);
            }

            if (var1.root != null) {
               if (var1.camera != null) {
                  var1x.setCamera(var1.camera);
               }

               NGNode var6 = var1.root;
               var6.render(var1x);
            }

         }

         public void run() {
            ResourceFactory var1x = GraphicsPipeline.getDefaultResourceFactory();
            if (var1x.isDeviceReady()) {
               int var2 = var1.x;
               int var3 = var1.y;
               int var4x = var1.width;
               int var5 = var1.height;
               if (var4x > 0 && var5 > 0) {
                  boolean var6 = false;

                  try {
                     QuantumImage var7 = var1.platformImage instanceof QuantumImage ? (QuantumImage)var1.platformImage : new QuantumImage((Image)null);
                     RTTexture var8 = var7.getRT(var4x, var5, var1x);
                     if (var8 != null) {
                        Graphics var9 = var8.createGraphics();
                        this.draw(var9, var2, var3, var4x, var5);
                        int[] var10 = var7.rt.getPixels();
                        if (var10 != null) {
                           var7.setImage(Image.fromIntArgbPreData(var10, var4x, var5));
                        } else {
                           IntBuffer var11 = IntBuffer.allocate(var4x * var5);
                           if (var7.rt.readPixels(var11, var7.rt.getContentX(), var7.rt.getContentY(), var4x, var5)) {
                              var7.setImage(Image.fromIntArgbPreData(var11, var4x, var5));
                           } else {
                              var7.dispose();
                              var7 = null;
                           }
                        }

                        var8.unlock();
                        var1.platformImage = var7;
                        return;
                     }
                  } catch (Throwable var15) {
                     var6 = true;
                     var15.printStackTrace(System.err);
                     return;
                  } finally {
                     Disposer.cleanUp();
                     var1x.getTextureResourcePool().freeDisposalRequestedAndCheckResources(var6);
                  }

               }
            }
         }
      });
      CountDownLatch var6 = new CountDownLatch(1);
      var5.setCompletionListener((var1x) -> {
         var6.countDown();
      });
      this.addRenderJob(var5);

      while(true) {
         try {
            var6.await();
            break;
         } catch (InterruptedException var8) {
            var8.printStackTrace();
         }
      }

      Object var7 = var1.platformImage;
      var1.platformImage = var2;
      return var7;
   }

   public CommonDialogs.FileChooserResult showFileChooser(TKStage var1, String var2, File var3, String var4, FileChooserType var5, List var6, FileChooser.ExtensionFilter var7) {
      WindowStage var8 = null;

      CommonDialogs.FileChooserResult var9;
      try {
         var8 = this.blockOwnerStage(var1);
         var9 = CommonDialogs.showFileChooser(var1 instanceof WindowStage ? ((WindowStage)var1).getPlatformWindow() : null, var3, var4, var2, var5 == FileChooserType.SAVE ? 1 : 0, var5 == FileChooserType.OPEN_MULTIPLE, convertExtensionFilters(var6), var6.indexOf(var7));
      } finally {
         if (var8 != null) {
            var8.setEnabled(true);
         }

      }

      return var9;
   }

   public File showDirectoryChooser(TKStage var1, String var2, File var3) {
      WindowStage var4 = null;

      File var5;
      try {
         var4 = this.blockOwnerStage(var1);
         var5 = CommonDialogs.showFolderChooser(var1 instanceof WindowStage ? ((WindowStage)var1).getPlatformWindow() : null, var3, var2);
      } finally {
         if (var4 != null) {
            var4.setEnabled(true);
         }

      }

      return var5;
   }

   private WindowStage blockOwnerStage(TKStage var1) {
      if (var1 instanceof WindowStage) {
         GlassStage var2 = ((WindowStage)var1).getOwner();
         if (var2 instanceof WindowStage) {
            WindowStage var3 = (WindowStage)var2;
            var3.setEnabled(false);
            return var3;
         }
      }

      return null;
   }

   private static List convertExtensionFilters(List var0) {
      CommonDialogs.ExtensionFilter[] var1 = new CommonDialogs.ExtensionFilter[var0.size()];
      int var2 = 0;

      FileChooser.ExtensionFilter var4;
      for(Iterator var3 = var0.iterator(); var3.hasNext(); var1[var2++] = new CommonDialogs.ExtensionFilter(var4.getDescription(), var4.getExtensions())) {
         var4 = (FileChooser.ExtensionFilter)var3.next();
      }

      return Arrays.asList(var1);
   }

   public long getMultiClickTime() {
      return View.getMultiClickTime();
   }

   public int getMultiClickMaxX() {
      return View.getMultiClickMaxX();
   }

   public int getMultiClickMaxY() {
      return View.getMultiClickMaxY();
   }

   public String getThemeName() {
      return Application.GetApplication().getHighContrastTheme();
   }

   static class QuantumImage implements ImageLoader, ResourceFactoryListener {
      private RTTexture rt;
      private Image image;
      private ResourceFactory rf;

      QuantumImage(Image var1) {
         this.image = var1;
      }

      RTTexture getRT(int var1, int var2, ResourceFactory var3) {
         boolean var4 = this.rt != null && this.rf == var3 && this.rt.getContentWidth() == var1 && this.rt.getContentHeight() == var2;
         if (var4) {
            this.rt.lock();
            if (this.rt.isSurfaceLost()) {
               var4 = false;
            }
         }

         if (!var4) {
            if (this.rt != null) {
               this.rt.dispose();
            }

            if (this.rf != null) {
               this.rf.removeFactoryListener(this);
               this.rf = null;
            }

            this.rt = var3.createRTTexture(var1, var2, Texture.WrapMode.CLAMP_TO_ZERO);
            if (this.rt != null) {
               this.rf = var3;
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

      void setImage(Image var1) {
         this.image = var1;
      }

      public Exception getException() {
         return this.image == null ? new IllegalStateException("Unitialized image") : null;
      }

      public int getFrameCount() {
         return 1;
      }

      public PlatformImage getFrame(int var1) {
         return this.image;
      }

      public int getFrameDelay(int var1) {
         return 0;
      }

      public int getLoopCount() {
         return 0;
      }

      public int getWidth() {
         return this.image.getWidth();
      }

      public int getHeight() {
         return this.image.getHeight();
      }

      public void factoryReset() {
         this.dispose();
      }

      public void factoryReleased() {
         this.dispose();
      }
   }

   private class PulseTask {
      private volatile boolean isRunning;

      PulseTask(boolean var2) {
         this.isRunning = var2;
      }

      synchronized void set(boolean var1) {
         this.isRunning = var1;
         if (this.isRunning) {
            QuantumToolkit.this.resumeTimer();
         }

      }

      boolean get() {
         return this.isRunning;
      }
   }
}
