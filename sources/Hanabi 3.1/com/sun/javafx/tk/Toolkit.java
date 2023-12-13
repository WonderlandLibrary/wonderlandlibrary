package com.sun.javafx.tk;

import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.runtime.VersionInfo;
import com.sun.javafx.runtime.async.AsyncOperation;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.animation.AbstractPrimaryTimer;
import com.sun.scenario.effect.AbstractShadow;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import javafx.application.ConditionalFeature;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public abstract class Toolkit {
   private static String tk;
   private static Toolkit TOOLKIT;
   private static Thread fxUserThread = null;
   private static final String QUANTUM_TOOLKIT = "com.sun.javafx.tk.quantum.QuantumToolkit";
   private static final String DEFAULT_TOOLKIT = "com.sun.javafx.tk.quantum.QuantumToolkit";
   private static final Map gradientMap = new WeakHashMap();
   private static final boolean verbose = (Boolean)AccessController.doPrivileged(() -> {
      return Boolean.getBoolean("javafx.verbose");
   });
   private static final String[] msLibNames = new String[]{"api-ms-win-core-console-l1-1-0", "api-ms-win-core-console-l1-2-0", "api-ms-win-core-datetime-l1-1-0", "api-ms-win-core-debug-l1-1-0", "api-ms-win-core-errorhandling-l1-1-0", "api-ms-win-core-file-l1-1-0", "api-ms-win-core-file-l1-2-0", "api-ms-win-core-file-l2-1-0", "api-ms-win-core-handle-l1-1-0", "api-ms-win-core-heap-l1-1-0", "api-ms-win-core-interlocked-l1-1-0", "api-ms-win-core-libraryloader-l1-1-0", "api-ms-win-core-localization-l1-2-0", "api-ms-win-core-memory-l1-1-0", "api-ms-win-core-namedpipe-l1-1-0", "api-ms-win-core-processenvironment-l1-1-0", "api-ms-win-core-processthreads-l1-1-0", "api-ms-win-core-processthreads-l1-1-1", "api-ms-win-core-profile-l1-1-0", "api-ms-win-core-rtlsupport-l1-1-0", "api-ms-win-core-string-l1-1-0", "api-ms-win-core-synch-l1-1-0", "api-ms-win-core-synch-l1-2-0", "api-ms-win-core-sysinfo-l1-1-0", "api-ms-win-core-timezone-l1-1-0", "api-ms-win-core-util-l1-1-0", "api-ms-win-crt-conio-l1-1-0", "api-ms-win-crt-convert-l1-1-0", "api-ms-win-crt-environment-l1-1-0", "api-ms-win-crt-filesystem-l1-1-0", "api-ms-win-crt-heap-l1-1-0", "api-ms-win-crt-locale-l1-1-0", "api-ms-win-crt-math-l1-1-0", "api-ms-win-crt-multibyte-l1-1-0", "api-ms-win-crt-private-l1-1-0", "api-ms-win-crt-process-l1-1-0", "api-ms-win-crt-runtime-l1-1-0", "api-ms-win-crt-stdio-l1-1-0", "api-ms-win-crt-string-l1-1-0", "api-ms-win-crt-time-l1-1-0", "api-ms-win-crt-utility-l1-1-0", "ucrtbase", "vcruntime140", "vcruntime140_1", "msvcp140"};
   private final Map stagePulseListeners = new WeakHashMap();
   private final Map scenePulseListeners = new WeakHashMap();
   private final Map postScenePulseListeners = new WeakHashMap();
   private final Map toolkitListeners = new WeakHashMap();
   private final Set shutdownHooks = new HashSet();
   private TKPulseListener lastTkPulseListener = null;
   private AccessControlContext lastTkPulseAcc = null;
   private CountDownLatch pauseScenesLatch = null;
   private Set highlightRegions;
   private static WritableImageAccessor writableImageAccessor = null;
   private static PaintAccessor paintAccessor = null;
   private static ImageAccessor imageAccessor = null;

   private static String lookupToolkitClass(String var0) {
      if ("prism".equalsIgnoreCase(var0)) {
         return "com.sun.javafx.tk.quantum.QuantumToolkit";
      } else {
         return "quantum".equalsIgnoreCase(var0) ? "com.sun.javafx.tk.quantum.QuantumToolkit" : var0;
      }
   }

   public static synchronized void loadMSWindowsLibraries() {
      String[] var0 = msLibNames;
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         String var3 = var0[var2];

         try {
            NativeLibLoader.loadLibrary(var3);
         } catch (Throwable var5) {
            if (verbose) {
               System.err.println("Error: failed to load " + var3 + ".dll : " + var5);
            }
         }
      }

   }

   private static String getDefaultToolkit() {
      if (PlatformUtil.isWindows()) {
         return "com.sun.javafx.tk.quantum.QuantumToolkit";
      } else if (PlatformUtil.isMac()) {
         return "com.sun.javafx.tk.quantum.QuantumToolkit";
      } else if (PlatformUtil.isLinux()) {
         return "com.sun.javafx.tk.quantum.QuantumToolkit";
      } else if (PlatformUtil.isIOS()) {
         return "com.sun.javafx.tk.quantum.QuantumToolkit";
      } else if (PlatformUtil.isAndroid()) {
         return "com.sun.javafx.tk.quantum.QuantumToolkit";
      } else {
         throw new UnsupportedOperationException(System.getProperty("os.name") + " is not supported");
      }
   }

   public static synchronized Toolkit getToolkit() {
      if (TOOLKIT != null) {
         return TOOLKIT;
      } else {
         if (PlatformUtil.isWindows()) {
            loadMSWindowsLibraries();
         }

         AccessController.doPrivileged(() -> {
            VersionInfo.setupSystemProperties();
            return null;
         });
         boolean var0 = true;
         String var1 = null;

         try {
            var1 = System.getProperty("javafx.toolkit");
         } catch (SecurityException var5) {
         }

         if (var1 == null) {
            var1 = tk;
         }

         if (var1 == null) {
            var0 = false;
            var1 = getDefaultToolkit();
         }

         if (var1.indexOf(46) == -1) {
            var1 = lookupToolkitClass(var1);
         }

         boolean var2 = verbose || var0 && !var1.endsWith("StubToolkit");

         try {
            Class var3 = Class.forName(var1, false, Toolkit.class.getClassLoader());
            if (!Toolkit.class.isAssignableFrom(var3)) {
               throw new IllegalArgumentException("Unrecognized FX Toolkit class: " + var1);
            }

            TOOLKIT = (Toolkit)var3.newInstance();
            if (TOOLKIT.init()) {
               if (var2) {
                  System.err.println("JavaFX: using " + var1);
               }

               return TOOLKIT;
            }

            TOOLKIT = null;
         } catch (Exception var4) {
            TOOLKIT = null;
            var4.printStackTrace();
         }

         throw new RuntimeException("No toolkit found");
      }
   }

   protected static Thread getFxUserThread() {
      return fxUserThread;
   }

   protected static void setFxUserThread(Thread var0) {
      if (fxUserThread != null) {
         throw new IllegalStateException("Error: FX User Thread already initialized");
      } else {
         fxUserThread = var0;
      }
   }

   public void checkFxUserThread() {
      if (!this.isFxUserThread()) {
         throw new IllegalStateException("Not on FX application thread; currentThread = " + Thread.currentThread().getName());
      }
   }

   public boolean isFxUserThread() {
      return Thread.currentThread() == fxUserThread;
   }

   protected Toolkit() {
   }

   public abstract boolean init();

   public abstract boolean canStartNestedEventLoop();

   public abstract Object enterNestedEventLoop(Object var1);

   public abstract void exitNestedEventLoop(Object var1, Object var2);

   public abstract boolean isNestedLoopRunning();

   public abstract TKStage createTKStage(Window var1, boolean var2, StageStyle var3, boolean var4, Modality var5, TKStage var6, boolean var7, AccessControlContext var8);

   public abstract TKStage createTKPopupStage(Window var1, StageStyle var2, TKStage var3, AccessControlContext var4);

   public abstract TKStage createTKEmbeddedStage(HostInterface var1, AccessControlContext var2);

   public abstract AppletWindow createAppletWindow(long var1, String var3);

   public abstract void closeAppletWindow();

   private void runPulse(TKPulseListener var1, AccessControlContext var2) {
      if (var2 == null) {
         throw new IllegalStateException("Invalid AccessControlContext");
      } else {
         AccessController.doPrivileged(() -> {
            var1.pulse();
            return null;
         }, var2);
      }
   }

   public void firePulse() {
      WeakHashMap var1 = new WeakHashMap();
      WeakHashMap var2 = new WeakHashMap();
      WeakHashMap var3 = new WeakHashMap();
      synchronized(this) {
         var1.putAll(this.stagePulseListeners);
         var2.putAll(this.scenePulseListeners);
         var3.putAll(this.postScenePulseListeners);
      }

      Iterator var4 = var1.entrySet().iterator();

      Map.Entry var5;
      while(var4.hasNext()) {
         var5 = (Map.Entry)var4.next();
         this.runPulse((TKPulseListener)var5.getKey(), (AccessControlContext)var5.getValue());
      }

      var4 = var2.entrySet().iterator();

      while(var4.hasNext()) {
         var5 = (Map.Entry)var4.next();
         this.runPulse((TKPulseListener)var5.getKey(), (AccessControlContext)var5.getValue());
      }

      var4 = var3.entrySet().iterator();

      while(var4.hasNext()) {
         var5 = (Map.Entry)var4.next();
         this.runPulse((TKPulseListener)var5.getKey(), (AccessControlContext)var5.getValue());
      }

      if (this.lastTkPulseListener != null) {
         this.runPulse(this.lastTkPulseListener, this.lastTkPulseAcc);
      }

   }

   public void addStageTkPulseListener(TKPulseListener var1) {
      if (var1 != null) {
         synchronized(this) {
            AccessControlContext var3 = AccessController.getContext();
            this.stagePulseListeners.put(var1, var3);
         }
      }
   }

   public void removeStageTkPulseListener(TKPulseListener var1) {
      synchronized(this) {
         this.stagePulseListeners.remove(var1);
      }
   }

   public void addSceneTkPulseListener(TKPulseListener var1) {
      if (var1 != null) {
         synchronized(this) {
            AccessControlContext var3 = AccessController.getContext();
            this.scenePulseListeners.put(var1, var3);
         }
      }
   }

   public void removeSceneTkPulseListener(TKPulseListener var1) {
      synchronized(this) {
         this.scenePulseListeners.remove(var1);
      }
   }

   public void addPostSceneTkPulseListener(TKPulseListener var1) {
      if (var1 != null) {
         synchronized(this) {
            AccessControlContext var3 = AccessController.getContext();
            this.postScenePulseListeners.put(var1, var3);
         }
      }
   }

   public void removePostSceneTkPulseListener(TKPulseListener var1) {
      synchronized(this) {
         this.postScenePulseListeners.remove(var1);
      }
   }

   public void addTkListener(TKListener var1) {
      if (var1 != null) {
         AccessControlContext var2 = AccessController.getContext();
         this.toolkitListeners.put(var1, var2);
      }
   }

   public void removeTkListener(TKListener var1) {
      this.toolkitListeners.remove(var1);
   }

   public void setLastTkPulseListener(TKPulseListener var1) {
      this.lastTkPulseAcc = AccessController.getContext();
      this.lastTkPulseListener = var1;
   }

   public void addShutdownHook(Runnable var1) {
      if (var1 != null) {
         synchronized(this.shutdownHooks) {
            this.shutdownHooks.add(var1);
         }
      }
   }

   public void removeShutdownHook(Runnable var1) {
      synchronized(this.shutdownHooks) {
         this.shutdownHooks.remove(var1);
      }
   }

   protected void notifyShutdownHooks() {
      ArrayList var1;
      synchronized(this.shutdownHooks) {
         var1 = new ArrayList(this.shutdownHooks);
         this.shutdownHooks.clear();
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Runnable var3 = (Runnable)var2.next();
         var3.run();
      }

   }

   public void notifyWindowListeners(List var1) {
      Iterator var2 = this.toolkitListeners.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         TKListener var4 = (TKListener)var3.getKey();
         AccessControlContext var5 = (AccessControlContext)var3.getValue();
         if (var5 == null) {
            throw new IllegalStateException("Invalid AccessControlContext");
         }

         AccessController.doPrivileged(() -> {
            var4.changedTopLevelWindows(var1);
            return null;
         }, var5);
      }

   }

   public void notifyLastNestedLoopExited() {
      Iterator var1 = this.toolkitListeners.keySet().iterator();

      while(var1.hasNext()) {
         TKListener var2 = (TKListener)var1.next();
         var2.exitedLastNestedLoop();
      }

   }

   public abstract void requestNextPulse();

   public abstract Future addRenderJob(RenderJob var1);

   public InputStream getInputStream(String var1, Class var2) throws IOException {
      return !var1.startsWith("http:") && !var1.startsWith("https:") && !var1.startsWith("file:") && !var1.startsWith("jar:") ? var2.getResource(var1).openStream() : (new URL(var1)).openStream();
   }

   public abstract ImageLoader loadImage(String var1, int var2, int var3, boolean var4, boolean var5);

   public abstract ImageLoader loadImage(InputStream var1, int var2, int var3, boolean var4, boolean var5);

   public abstract AsyncOperation loadImageAsync(AsyncOperationListener var1, String var2, int var3, int var4, boolean var5, boolean var6);

   public abstract ImageLoader loadPlatformImage(Object var1);

   public abstract PlatformImage createPlatformImage(int var1, int var2);

   public boolean getDefaultImageSmooth() {
      return true;
   }

   public abstract void startup(Runnable var1);

   public abstract void defer(Runnable var1);

   public void exit() {
      fxUserThread = null;
   }

   public abstract Map getContextMap();

   public abstract int getRefreshRate();

   public abstract void setAnimationRunnable(DelayedRunnable var1);

   public abstract PerformanceTracker getPerformanceTracker();

   public abstract PerformanceTracker createPerformanceTracker();

   public abstract void waitFor(Task var1);

   private Object checkSingleColor(List var1) {
      if (var1.size() == 2) {
         Color var2 = ((Stop)var1.get(0)).getColor();
         if (var2.equals(((Stop)var1.get(1)).getColor())) {
            return getPaintAccessor().getPlatformPaint(var2);
         }
      }

      return null;
   }

   private Object getPaint(LinearGradient var1) {
      Object var2 = gradientMap.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         var2 = this.checkSingleColor(var1.getStops());
         if (var2 == null) {
            var2 = this.createLinearGradientPaint(var1);
         }

         gradientMap.put(var1, var2);
         return var2;
      }
   }

   private Object getPaint(RadialGradient var1) {
      Object var2 = gradientMap.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         var2 = this.checkSingleColor(var1.getStops());
         if (var2 == null) {
            var2 = this.createRadialGradientPaint(var1);
         }

         gradientMap.put(var1, var2);
         return var2;
      }
   }

   public Object getPaint(Paint var1) {
      if (var1 instanceof Color) {
         return this.createColorPaint((Color)var1);
      } else if (var1 instanceof LinearGradient) {
         return this.getPaint((LinearGradient)var1);
      } else if (var1 instanceof RadialGradient) {
         return this.getPaint((RadialGradient)var1);
      } else {
         return var1 instanceof ImagePattern ? this.createImagePatternPaint((ImagePattern)var1) : null;
      }
   }

   protected static final double clampStopOffset(double var0) {
      return var0 > 1.0 ? 1.0 : (var0 < 0.0 ? 0.0 : var0);
   }

   protected abstract Object createColorPaint(Color var1);

   protected abstract Object createLinearGradientPaint(LinearGradient var1);

   protected abstract Object createRadialGradientPaint(RadialGradient var1);

   protected abstract Object createImagePatternPaint(ImagePattern var1);

   public abstract void accumulateStrokeBounds(Shape var1, float[] var2, StrokeType var3, double var4, StrokeLineCap var6, StrokeLineJoin var7, float var8, BaseTransform var9);

   public abstract boolean strokeContains(Shape var1, double var2, double var4, StrokeType var6, double var7, StrokeLineCap var9, StrokeLineJoin var10, float var11);

   public abstract Shape createStrokedShape(Shape var1, StrokeType var2, double var3, StrokeLineCap var5, StrokeLineJoin var6, float var7, float[] var8, float var9);

   public abstract int getKeyCodeForChar(String var1);

   public abstract Dimension2D getBestCursorSize(int var1, int var2);

   public abstract int getMaximumCursorColors();

   public abstract PathElement[] convertShapeToFXPath(Object var1);

   public abstract HitInfo convertHitInfoToFX(Object var1);

   public abstract Filterable toFilterable(Image var1);

   public abstract FilterContext getFilterContext(Object var1);

   public abstract boolean isForwardTraversalKey(KeyEvent var1);

   public abstract boolean isBackwardTraversalKey(KeyEvent var1);

   public abstract AbstractPrimaryTimer getPrimaryTimer();

   public abstract FontLoader getFontLoader();

   public abstract TextLayoutFactory getTextLayoutFactory();

   public abstract Object createSVGPathObject(SVGPath var1);

   public abstract Path2D createSVGPath2D(SVGPath var1);

   public abstract boolean imageContains(Object var1, float var2, float var3);

   public abstract TKClipboard getSystemClipboard();

   public TKClipboard createLocalClipboard() {
      return new LocalClipboard();
   }

   public abstract TKSystemMenu getSystemMenu();

   public abstract TKClipboard getNamedClipboard(String var1);

   public boolean isSupported(ConditionalFeature var1) {
      return false;
   }

   public boolean isMSAASupported() {
      return false;
   }

   public abstract ScreenConfigurationAccessor setScreenConfigurationListener(TKScreenConfigurationListener var1);

   public abstract Object getPrimaryScreen();

   public abstract List getScreens();

   public abstract ScreenConfigurationAccessor getScreenConfigurationAccessor();

   public abstract void registerDragGestureListener(TKScene var1, Set var2, TKDragGestureListener var3);

   public abstract void startDrag(TKScene var1, Set var2, TKDragSourceListener var3, Dragboard var4);

   public void stopDrag(Dragboard var1) {
   }

   public abstract void enableDrop(TKScene var1, TKDropTargetListener var2);

   public Color4f toColor4f(Color var1) {
      return new Color4f((float)var1.getRed(), (float)var1.getGreen(), (float)var1.getBlue(), (float)var1.getOpacity());
   }

   public AbstractShadow.ShadowMode toShadowMode(BlurType var1) {
      switch (var1) {
         case ONE_PASS_BOX:
            return AbstractShadow.ShadowMode.ONE_PASS_BOX;
         case TWO_PASS_BOX:
            return AbstractShadow.ShadowMode.TWO_PASS_BOX;
         case THREE_PASS_BOX:
            return AbstractShadow.ShadowMode.THREE_PASS_BOX;
         default:
            return AbstractShadow.ShadowMode.GAUSSIAN;
      }
   }

   public abstract void installInputMethodRequests(TKScene var1, InputMethodRequests var2);

   public abstract Object renderToImage(ImageRenderingContext var1);

   public KeyCode getPlatformShortcutKey() {
      return PlatformUtil.isMac() ? KeyCode.META : KeyCode.CONTROL;
   }

   public abstract CommonDialogs.FileChooserResult showFileChooser(TKStage var1, String var2, File var3, String var4, FileChooserType var5, List var6, FileChooser.ExtensionFilter var7);

   public abstract File showDirectoryChooser(TKStage var1, String var2, File var3);

   public abstract long getMultiClickTime();

   public abstract int getMultiClickMaxX();

   public abstract int getMultiClickMaxY();

   public void pauseScenes() {
      this.pauseScenesLatch = new CountDownLatch(1);
      Iterator var1 = Window.impl_getWindows();

      while(var1.hasNext()) {
         Window var2 = (Window)var1.next();
         Scene var3 = var2.getScene();
         if (var3 != null) {
            this.removeSceneTkPulseListener(var3.impl_getScenePulseListener());
         }
      }

      this.getPrimaryTimer().pause();
      SceneHelper.setPaused(true);
   }

   public void resumeScenes() {
      SceneHelper.setPaused(false);
      this.getPrimaryTimer().resume();
      Iterator var1 = Window.impl_getWindows();

      while(var1.hasNext()) {
         Window var2 = (Window)var1.next();
         Scene var3 = var2.getScene();
         if (var3 != null) {
            this.addSceneTkPulseListener(var3.impl_getScenePulseListener());
         }
      }

      this.pauseScenesLatch.countDown();
      this.pauseScenesLatch = null;
   }

   public void pauseCurrentThread() {
      CountDownLatch var1 = this.pauseScenesLatch;
      if (var1 != null) {
         try {
            var1.await();
         } catch (InterruptedException var3) {
         }

      }
   }

   public Set getHighlightedRegions() {
      if (this.highlightRegions == null) {
         this.highlightRegions = new HashSet();
      }

      return this.highlightRegions;
   }

   public static void setWritableImageAccessor(WritableImageAccessor var0) {
      writableImageAccessor = var0;
   }

   public static WritableImageAccessor getWritableImageAccessor() {
      return writableImageAccessor;
   }

   public static void setPaintAccessor(PaintAccessor var0) {
      paintAccessor = var0;
   }

   public static PaintAccessor getPaintAccessor() {
      return paintAccessor;
   }

   public static void setImageAccessor(ImageAccessor var0) {
      imageAccessor = var0;
   }

   public static ImageAccessor getImageAccessor() {
      return imageAccessor;
   }

   public String getThemeName() {
      return null;
   }

   public interface ImageAccessor {
      boolean isAnimation(Image var1);

      ReadOnlyObjectProperty getImageProperty(Image var1);

      int[] getPreColors(PixelFormat var1);

      int[] getNonPreColors(PixelFormat var1);
   }

   public interface PaintAccessor {
      boolean isMutable(Paint var1);

      Object getPlatformPaint(Paint var1);

      void addListener(Paint var1, AbstractNotifyListener var2);

      void removeListener(Paint var1, AbstractNotifyListener var2);
   }

   public interface WritableImageAccessor {
      void loadTkImage(WritableImage var1, Object var2);

      Object getTkImageLoader(WritableImage var1);
   }

   public static class ImageRenderingContext {
      public NGNode root;
      public int x;
      public int y;
      public int width;
      public int height;
      public BaseTransform transform;
      public boolean depthBuffer;
      public Object platformPaint;
      public NGCamera camera;
      public NGLightBase[] lights;
      public Object platformImage;
   }

   public interface Task {
      boolean isFinished();
   }
}
