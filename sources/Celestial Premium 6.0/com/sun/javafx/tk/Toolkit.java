/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.PlatformUtil
 *  com.sun.javafx.runtime.VersionInfo
 *  javafx.application.ConditionalFeature
 *  javafx.beans.property.ReadOnlyObjectProperty
 *  javafx.geometry.Dimension2D
 *  javafx.scene.effect.BlurType
 *  javafx.scene.image.Image
 *  javafx.scene.image.PixelFormat
 *  javafx.scene.image.WritableImage
 *  javafx.scene.input.Dragboard
 *  javafx.scene.input.InputMethodRequests
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.TransferMode
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.ImagePattern
 *  javafx.scene.paint.LinearGradient
 *  javafx.scene.paint.Paint
 *  javafx.scene.paint.RadialGradient
 *  javafx.scene.paint.Stop
 *  javafx.scene.shape.PathElement
 *  javafx.scene.shape.SVGPath
 *  javafx.scene.shape.StrokeLineCap
 *  javafx.scene.shape.StrokeLineJoin
 *  javafx.scene.shape.StrokeType
 *  javafx.stage.FileChooser$ExtensionFilter
 *  javafx.stage.Modality
 *  javafx.stage.StageStyle
 *  javafx.stage.Window
 */
package com.sun.javafx.tk;

import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.GlassRobot;
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
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.AppletWindow;
import com.sun.javafx.tk.FileChooserType;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.LocalClipboard;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.ScreenConfigurationAccessor;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.TKDragGestureListener;
import com.sun.javafx.tk.TKDragSourceListener;
import com.sun.javafx.tk.TKDropTargetListener;
import com.sun.javafx.tk.TKListener;
import com.sun.javafx.tk.TKPulseListener;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKScreenConfigurationListener;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.TKSystemMenu;
import com.sun.javafx.util.Utils;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.animation.AbstractPrimaryTimer;
import com.sun.scenario.effect.AbstractShadow;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import javafx.application.ConditionalFeature;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Dimension2D;
import javafx.scene.effect.BlurType;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
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
    private static Thread fxUserThread;
    private static final String QUANTUM_TOOLKIT = "com.sun.javafx.tk.quantum.QuantumToolkit";
    private static final String DEFAULT_TOOLKIT = "com.sun.javafx.tk.quantum.QuantumToolkit";
    private static final Map gradientMap;
    private static final boolean verbose;
    private static final String[] msLibNames;
    private final Map<TKPulseListener, AccessControlContext> stagePulseListeners = new WeakHashMap<TKPulseListener, AccessControlContext>();
    private final Map<TKPulseListener, AccessControlContext> scenePulseListeners = new WeakHashMap<TKPulseListener, AccessControlContext>();
    private final Map<TKPulseListener, AccessControlContext> postScenePulseListeners = new WeakHashMap<TKPulseListener, AccessControlContext>();
    private final Map<TKListener, AccessControlContext> toolkitListeners = new WeakHashMap<TKListener, AccessControlContext>();
    private final Set<Runnable> shutdownHooks = new HashSet<Runnable>();
    private TKPulseListener lastTkPulseListener = null;
    private AccessControlContext lastTkPulseAcc = null;
    private CountDownLatch pauseScenesLatch = null;
    private static WritableImageAccessor writableImageAccessor;
    private static PaintAccessor paintAccessor;
    private static ImageAccessor imageAccessor;

    private static String lookupToolkitClass(String string) {
        if ("prism".equalsIgnoreCase(string)) {
            return "com.sun.javafx.tk.quantum.QuantumToolkit";
        }
        if ("quantum".equalsIgnoreCase(string)) {
            return "com.sun.javafx.tk.quantum.QuantumToolkit";
        }
        return string;
    }

    public static synchronized void loadMSWindowsLibraries() {
        for (String string : msLibNames) {
            try {
                NativeLibLoader.loadLibrary(string);
            }
            catch (Throwable throwable) {
                if (!verbose) continue;
                System.err.println("Error: failed to load " + string + ".dll : " + throwable);
            }
        }
    }

    private static String getDefaultToolkit() {
        if (PlatformUtil.isWindows()) {
            return "com.sun.javafx.tk.quantum.QuantumToolkit";
        }
        if (PlatformUtil.isMac()) {
            return "com.sun.javafx.tk.quantum.QuantumToolkit";
        }
        if (PlatformUtil.isLinux()) {
            return "com.sun.javafx.tk.quantum.QuantumToolkit";
        }
        if (PlatformUtil.isIOS()) {
            return "com.sun.javafx.tk.quantum.QuantumToolkit";
        }
        if (PlatformUtil.isAndroid()) {
            return "com.sun.javafx.tk.quantum.QuantumToolkit";
        }
        throw new UnsupportedOperationException(System.getProperty("os.name") + " is not supported");
    }

    public static synchronized Toolkit getToolkit() {
        if (TOOLKIT != null) {
            return TOOLKIT;
        }
        Object object = AccessController.doPrivileged(() -> {
            VersionInfo.setupSystemProperties();
            return null;
        });
        if (PlatformUtil.isWindows()) {
            Toolkit.loadMSWindowsLibraries();
        }
        boolean bl = true;
        String string = null;
        try {
            string = System.getProperty("javafx.toolkit");
        }
        catch (SecurityException securityException) {
            // empty catch block
        }
        if (string == null) {
            string = tk;
        }
        if (string == null) {
            bl = false;
            string = Toolkit.getDefaultToolkit();
        }
        if (string.indexOf(46) == -1) {
            string = Toolkit.lookupToolkitClass(string);
        }
        boolean bl2 = verbose || bl && !string.endsWith("StubToolkit");
        try {
            Class<?> class_ = null;
            try {
                ClassLoader classLoader = Toolkit.class.getClassLoader();
                class_ = Class.forName(string, false, classLoader);
            }
            catch (ClassNotFoundException classNotFoundException) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                class_ = Class.forName(string, false, classLoader);
            }
            if (!Toolkit.class.isAssignableFrom(class_)) {
                throw new IllegalArgumentException("Unrecognized FX Toolkit class: " + string);
            }
            TOOLKIT = (Toolkit)class_.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            if (TOOLKIT.init()) {
                if (bl2) {
                    System.err.println("JavaFX: using " + string);
                }
                return TOOLKIT;
            }
            TOOLKIT = null;
        }
        catch (Exception exception) {
            TOOLKIT = null;
            exception.printStackTrace();
        }
        throw new RuntimeException("No toolkit found");
    }

    protected static Thread getFxUserThread() {
        return fxUserThread;
    }

    protected static void setFxUserThread(Thread thread) {
        if (fxUserThread != null) {
            throw new IllegalStateException("Error: FX User Thread already initialized");
        }
        fxUserThread = thread;
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

    public abstract void exitAllNestedEventLoops();

    public abstract boolean isNestedLoopRunning();

    public abstract TKStage createTKStage(Window var1, boolean var2, StageStyle var3, boolean var4, Modality var5, TKStage var6, boolean var7, AccessControlContext var8);

    public abstract TKStage createTKPopupStage(Window var1, StageStyle var2, TKStage var3, AccessControlContext var4);

    public abstract TKStage createTKEmbeddedStage(HostInterface var1, AccessControlContext var2);

    public abstract AppletWindow createAppletWindow(long var1, String var3);

    public abstract void closeAppletWindow();

    private void runPulse(TKPulseListener tKPulseListener, AccessControlContext accessControlContext) {
        if (accessControlContext == null) {
            throw new IllegalStateException("Invalid AccessControlContext");
        }
        AccessController.doPrivileged(() -> {
            tKPulseListener.pulse();
            return null;
        }, accessControlContext);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void firePulse() {
        WeakHashMap<TKPulseListener, AccessControlContext> weakHashMap = new WeakHashMap<TKPulseListener, AccessControlContext>();
        WeakHashMap<TKPulseListener, AccessControlContext> weakHashMap2 = new WeakHashMap<TKPulseListener, AccessControlContext>();
        WeakHashMap<TKPulseListener, AccessControlContext> weakHashMap3 = new WeakHashMap<TKPulseListener, AccessControlContext>();
        Iterator iterator = this;
        synchronized (iterator) {
            weakHashMap.putAll(this.stagePulseListeners);
            weakHashMap2.putAll(this.scenePulseListeners);
            weakHashMap3.putAll(this.postScenePulseListeners);
        }
        for (Map.Entry entry : weakHashMap.entrySet()) {
            this.runPulse((TKPulseListener)entry.getKey(), (AccessControlContext)entry.getValue());
        }
        for (Map.Entry entry : weakHashMap2.entrySet()) {
            this.runPulse((TKPulseListener)entry.getKey(), (AccessControlContext)entry.getValue());
        }
        for (Map.Entry entry : weakHashMap3.entrySet()) {
            this.runPulse((TKPulseListener)entry.getKey(), (AccessControlContext)entry.getValue());
        }
        if (this.lastTkPulseListener != null) {
            this.runPulse(this.lastTkPulseListener, this.lastTkPulseAcc);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addStageTkPulseListener(TKPulseListener tKPulseListener) {
        if (tKPulseListener == null) {
            return;
        }
        Toolkit toolkit = this;
        synchronized (toolkit) {
            AccessControlContext accessControlContext = AccessController.getContext();
            this.stagePulseListeners.put(tKPulseListener, accessControlContext);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removeStageTkPulseListener(TKPulseListener tKPulseListener) {
        Toolkit toolkit = this;
        synchronized (toolkit) {
            this.stagePulseListeners.remove(tKPulseListener);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addSceneTkPulseListener(TKPulseListener tKPulseListener) {
        if (tKPulseListener == null) {
            return;
        }
        Toolkit toolkit = this;
        synchronized (toolkit) {
            AccessControlContext accessControlContext = AccessController.getContext();
            this.scenePulseListeners.put(tKPulseListener, accessControlContext);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removeSceneTkPulseListener(TKPulseListener tKPulseListener) {
        Toolkit toolkit = this;
        synchronized (toolkit) {
            this.scenePulseListeners.remove(tKPulseListener);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addPostSceneTkPulseListener(TKPulseListener tKPulseListener) {
        if (tKPulseListener == null) {
            return;
        }
        Toolkit toolkit = this;
        synchronized (toolkit) {
            AccessControlContext accessControlContext = AccessController.getContext();
            this.postScenePulseListeners.put(tKPulseListener, accessControlContext);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removePostSceneTkPulseListener(TKPulseListener tKPulseListener) {
        Toolkit toolkit = this;
        synchronized (toolkit) {
            this.postScenePulseListeners.remove(tKPulseListener);
        }
    }

    public void addTkListener(TKListener tKListener) {
        if (tKListener == null) {
            return;
        }
        AccessControlContext accessControlContext = AccessController.getContext();
        this.toolkitListeners.put(tKListener, accessControlContext);
    }

    public void removeTkListener(TKListener tKListener) {
        this.toolkitListeners.remove(tKListener);
    }

    public void setLastTkPulseListener(TKPulseListener tKPulseListener) {
        this.lastTkPulseAcc = AccessController.getContext();
        this.lastTkPulseListener = tKPulseListener;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addShutdownHook(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        Set<Runnable> set = this.shutdownHooks;
        synchronized (set) {
            this.shutdownHooks.add(runnable);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void removeShutdownHook(Runnable runnable) {
        Set<Runnable> set = this.shutdownHooks;
        synchronized (set) {
            this.shutdownHooks.remove(runnable);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void notifyShutdownHooks() {
        ArrayList<Runnable> arrayList;
        Set<Runnable> set = this.shutdownHooks;
        synchronized (set) {
            arrayList = new ArrayList<Runnable>(this.shutdownHooks);
            this.shutdownHooks.clear();
        }
        for (Runnable runnable : arrayList) {
            runnable.run();
        }
    }

    public void notifyWindowListeners(List<TKStage> list) {
        for (Map.Entry<TKListener, AccessControlContext> entry : this.toolkitListeners.entrySet()) {
            TKListener tKListener = entry.getKey();
            AccessControlContext accessControlContext = entry.getValue();
            if (accessControlContext == null) {
                throw new IllegalStateException("Invalid AccessControlContext");
            }
            AccessController.doPrivileged(() -> {
                tKListener.changedTopLevelWindows(list);
                return null;
            }, accessControlContext);
        }
    }

    public void notifyLastNestedLoopExited() {
        for (TKListener tKListener : this.toolkitListeners.keySet()) {
            tKListener.exitedLastNestedLoop();
        }
    }

    public abstract void requestNextPulse();

    public abstract Future addRenderJob(RenderJob var1);

    public abstract ImageLoader loadImage(String var1, double var2, double var4, boolean var6, boolean var7);

    public abstract ImageLoader loadImage(InputStream var1, double var2, double var4, boolean var6, boolean var7);

    public abstract AsyncOperation loadImageAsync(AsyncOperationListener<? extends ImageLoader> var1, String var2, double var3, double var5, boolean var7, boolean var8);

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

    public abstract Map<Object, Object> getContextMap();

    public abstract int getRefreshRate();

    public abstract void setAnimationRunnable(DelayedRunnable var1);

    public abstract PerformanceTracker getPerformanceTracker();

    public abstract PerformanceTracker createPerformanceTracker();

    public abstract void waitFor(Task var1);

    private Object checkSingleColor(List<Stop> list) {
        Color color;
        if (list.size() == 2 && (color = list.get(0).getColor()).equals((Object)list.get(1).getColor())) {
            return Toolkit.getPaintAccessor().getPlatformPaint((Paint)color);
        }
        return null;
    }

    private Object getPaint(LinearGradient linearGradient) {
        Object object = gradientMap.get((Object)linearGradient);
        if (object != null) {
            return object;
        }
        object = this.checkSingleColor(linearGradient.getStops());
        if (object == null) {
            object = this.createLinearGradientPaint(linearGradient);
        }
        gradientMap.put(linearGradient, object);
        return object;
    }

    private Object getPaint(RadialGradient radialGradient) {
        Object object = gradientMap.get((Object)radialGradient);
        if (object != null) {
            return object;
        }
        object = this.checkSingleColor(radialGradient.getStops());
        if (object == null) {
            object = this.createRadialGradientPaint(radialGradient);
        }
        gradientMap.put(radialGradient, object);
        return object;
    }

    public Object getPaint(Paint paint) {
        if (paint instanceof Color) {
            return this.createColorPaint((Color)paint);
        }
        if (paint instanceof LinearGradient) {
            return this.getPaint((LinearGradient)paint);
        }
        if (paint instanceof RadialGradient) {
            return this.getPaint((RadialGradient)paint);
        }
        if (paint instanceof ImagePattern) {
            return this.createImagePatternPaint((ImagePattern)paint);
        }
        return null;
    }

    protected static final double clampStopOffset(double d) {
        return d > 1.0 ? 1.0 : (d < 0.0 ? 0.0 : d);
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

    public boolean isSupported(ConditionalFeature conditionalFeature) {
        return false;
    }

    public boolean isMSAASupported() {
        return false;
    }

    public abstract ScreenConfigurationAccessor setScreenConfigurationListener(TKScreenConfigurationListener var1);

    public abstract Object getPrimaryScreen();

    public abstract List<?> getScreens();

    public abstract ScreenConfigurationAccessor getScreenConfigurationAccessor();

    public abstract void registerDragGestureListener(TKScene var1, Set<TransferMode> var2, TKDragGestureListener var3);

    public abstract void startDrag(TKScene var1, Set<TransferMode> var2, TKDragSourceListener var3, Dragboard var4);

    public void stopDrag(Dragboard dragboard) {
    }

    public abstract void enableDrop(TKScene var1, TKDropTargetListener var2);

    public Color4f toColor4f(Color color) {
        return new Color4f((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)color.getOpacity());
    }

    public AbstractShadow.ShadowMode toShadowMode(BlurType blurType) {
        switch (blurType) {
            case ONE_PASS_BOX: {
                return AbstractShadow.ShadowMode.ONE_PASS_BOX;
            }
            case TWO_PASS_BOX: {
                return AbstractShadow.ShadowMode.TWO_PASS_BOX;
            }
            case THREE_PASS_BOX: {
                return AbstractShadow.ShadowMode.THREE_PASS_BOX;
            }
        }
        return AbstractShadow.ShadowMode.GAUSSIAN;
    }

    public abstract void installInputMethodRequests(TKScene var1, InputMethodRequests var2);

    public abstract Object renderToImage(ImageRenderingContext var1);

    public KeyCode getPlatformShortcutKey() {
        return PlatformUtil.isMac() ? KeyCode.META : KeyCode.CONTROL;
    }

    public abstract Optional<Boolean> isKeyLocked(KeyCode var1);

    public abstract CommonDialogs.FileChooserResult showFileChooser(TKStage var1, String var2, File var3, String var4, FileChooserType var5, List<FileChooser.ExtensionFilter> var6, FileChooser.ExtensionFilter var7);

    public abstract File showDirectoryChooser(TKStage var1, String var2, File var3);

    public abstract long getMultiClickTime();

    public abstract int getMultiClickMaxX();

    public abstract int getMultiClickMaxY();

    public static void setWritableImageAccessor(WritableImageAccessor writableImageAccessor) {
        Toolkit.writableImageAccessor = writableImageAccessor;
    }

    public static WritableImageAccessor getWritableImageAccessor() {
        return writableImageAccessor;
    }

    public static void setPaintAccessor(PaintAccessor paintAccessor) {
        Toolkit.paintAccessor = paintAccessor;
    }

    public static PaintAccessor getPaintAccessor() {
        return paintAccessor;
    }

    public static void setImageAccessor(ImageAccessor imageAccessor) {
        Toolkit.imageAccessor = imageAccessor;
    }

    public static ImageAccessor getImageAccessor() {
        return imageAccessor;
    }

    public String getThemeName() {
        return null;
    }

    public abstract GlassRobot createRobot();

    static {
        fxUserThread = null;
        gradientMap = new WeakHashMap();
        verbose = AccessController.doPrivileged(() -> Boolean.getBoolean("javafx.verbose"));
        msLibNames = new String[]{"api-ms-win-core-console-l1-1-0", "api-ms-win-core-console-l1-2-0", "api-ms-win-core-datetime-l1-1-0", "api-ms-win-core-debug-l1-1-0", "api-ms-win-core-errorhandling-l1-1-0", "api-ms-win-core-file-l1-1-0", "api-ms-win-core-file-l1-2-0", "api-ms-win-core-file-l2-1-0", "api-ms-win-core-handle-l1-1-0", "api-ms-win-core-heap-l1-1-0", "api-ms-win-core-interlocked-l1-1-0", "api-ms-win-core-libraryloader-l1-1-0", "api-ms-win-core-localization-l1-2-0", "api-ms-win-core-memory-l1-1-0", "api-ms-win-core-namedpipe-l1-1-0", "api-ms-win-core-processenvironment-l1-1-0", "api-ms-win-core-processthreads-l1-1-0", "api-ms-win-core-processthreads-l1-1-1", "api-ms-win-core-profile-l1-1-0", "api-ms-win-core-rtlsupport-l1-1-0", "api-ms-win-core-string-l1-1-0", "api-ms-win-core-synch-l1-1-0", "api-ms-win-core-synch-l1-2-0", "api-ms-win-core-sysinfo-l1-1-0", "api-ms-win-core-timezone-l1-1-0", "api-ms-win-core-util-l1-1-0", "api-ms-win-crt-conio-l1-1-0", "api-ms-win-crt-convert-l1-1-0", "api-ms-win-crt-environment-l1-1-0", "api-ms-win-crt-filesystem-l1-1-0", "api-ms-win-crt-heap-l1-1-0", "api-ms-win-crt-locale-l1-1-0", "api-ms-win-crt-math-l1-1-0", "api-ms-win-crt-multibyte-l1-1-0", "api-ms-win-crt-private-l1-1-0", "api-ms-win-crt-process-l1-1-0", "api-ms-win-crt-runtime-l1-1-0", "api-ms-win-crt-stdio-l1-1-0", "api-ms-win-crt-string-l1-1-0", "api-ms-win-crt-time-l1-1-0", "api-ms-win-crt-utility-l1-1-0", "ucrtbase", "vcruntime140", "vcruntime140_1", "msvcp140"};
        writableImageAccessor = null;
        paintAccessor = null;
        Utils.forceInit(Image.class);
    }

    public static interface PaintAccessor {
        public boolean isMutable(Paint var1);

        public Object getPlatformPaint(Paint var1);

        public void addListener(Paint var1, AbstractNotifyListener var2);

        public void removeListener(Paint var1, AbstractNotifyListener var2);
    }

    public static interface WritableImageAccessor {
        public void loadTkImage(WritableImage var1, Object var2);

        public Object getTkImageLoader(WritableImage var1);
    }

    public static interface ImageAccessor {
        public boolean isAnimation(Image var1);

        public ReadOnlyObjectProperty<PlatformImage> getImageProperty(Image var1);

        public int[] getPreColors(PixelFormat<ByteBuffer> var1);

        public int[] getNonPreColors(PixelFormat<ByteBuffer> var1);

        public Object getPlatformImage(Image var1);

        public Image fromPlatformImage(Object var1);
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

    public static interface Task {
        public boolean isFinished();
    }
}

