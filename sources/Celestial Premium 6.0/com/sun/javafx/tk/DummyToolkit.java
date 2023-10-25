/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Dimension2D
 *  javafx.scene.image.Image
 *  javafx.scene.input.Dragboard
 *  javafx.scene.input.InputMethodRequests
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.TransferMode
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.ImagePattern
 *  javafx.scene.paint.LinearGradient
 *  javafx.scene.paint.RadialGradient
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
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.runtime.async.AsyncOperation;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.tk.AppletWindow;
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
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.animation.AbstractPrimaryTimer;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import java.io.File;
import java.io.InputStream;
import java.security.AccessControlContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public final class DummyToolkit
extends Toolkit {
    @Override
    public boolean init() {
        return true;
    }

    @Override
    public boolean canStartNestedEventLoop() {
        return false;
    }

    @Override
    public Object enterNestedEventLoop(Object object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void exitNestedEventLoop(Object object, Object object2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void exitAllNestedEventLoops() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TKStage createTKStage(Window window, boolean bl, StageStyle stageStyle, boolean bl2, Modality modality, TKStage tKStage, boolean bl3, AccessControlContext accessControlContext) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TKStage createTKPopupStage(Window window, StageStyle stageStyle, TKStage tKStage, AccessControlContext accessControlContext) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TKStage createTKEmbeddedStage(HostInterface hostInterface, AccessControlContext accessControlContext) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AppletWindow createAppletWindow(long l, String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void closeAppletWindow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TKSystemMenu getSystemMenu() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ImageLoader loadImage(String string, double d, double d2, boolean bl, boolean bl2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ImageLoader loadImage(InputStream inputStream, double d, double d2, boolean bl, boolean bl2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AsyncOperation loadImageAsync(AsyncOperationListener<? extends ImageLoader> asyncOperationListener, String string, double d, double d2, boolean bl, boolean bl2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ImageLoader loadPlatformImage(Object object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PlatformImage createPlatformImage(int n, int n2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void startup(Runnable runnable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void defer(Runnable runnable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Future addRenderJob(RenderJob renderJob) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<Object, Object> getContextMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRefreshRate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAnimationRunnable(DelayedRunnable delayedRunnable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PerformanceTracker getPerformanceTracker() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PerformanceTracker createPerformanceTracker() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void waitFor(Toolkit.Task task) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Object createColorPaint(Color color) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Object createLinearGradientPaint(LinearGradient linearGradient) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Object createRadialGradientPaint(RadialGradient radialGradient) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Object createImagePatternPaint(ImagePattern imagePattern) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void accumulateStrokeBounds(Shape shape, float[] arrf, StrokeType strokeType, double d, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f, BaseTransform baseTransform) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean strokeContains(Shape shape, double d, double d2, StrokeType strokeType, double d3, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Shape createStrokedShape(Shape shape, StrokeType strokeType, double d, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f, float[] arrf, float f2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getKeyCodeForChar(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Dimension2D getBestCursorSize(int n, int n2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getMaximumCursorColors() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PathElement[] convertShapeToFXPath(Object object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Filterable toFilterable(Image image) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FilterContext getFilterContext(Object object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isForwardTraversalKey(KeyEvent keyEvent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isBackwardTraversalKey(KeyEvent keyEvent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isNestedLoopRunning() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AbstractPrimaryTimer getPrimaryTimer() {
        return null;
    }

    @Override
    public FontLoader getFontLoader() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TextLayoutFactory getTextLayoutFactory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object createSVGPathObject(SVGPath sVGPath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Path2D createSVGPath2D(SVGPath sVGPath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean imageContains(Object object, float f, float f2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TKClipboard getSystemClipboard() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TKClipboard getNamedClipboard(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ScreenConfigurationAccessor setScreenConfigurationListener(TKScreenConfigurationListener tKScreenConfigurationListener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getPrimaryScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<?> getScreens() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ScreenConfigurationAccessor getScreenConfigurationAccessor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void registerDragGestureListener(TKScene tKScene, Set<TransferMode> set, TKDragGestureListener tKDragGestureListener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void startDrag(TKScene tKScene, Set<TransferMode> set, TKDragSourceListener tKDragSourceListener, Dragboard dragboard) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void enableDrop(TKScene tKScene, TKDropTargetListener tKDropTargetListener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void installInputMethodRequests(TKScene tKScene, InputMethodRequests inputMethodRequests) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object renderToImage(Toolkit.ImageRenderingContext imageRenderingContext) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public KeyCode getPlatformShortcutKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Optional<Boolean> isKeyLocked(KeyCode keyCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CommonDialogs.FileChooserResult showFileChooser(TKStage tKStage, String string, File file, String string2, FileChooserType fileChooserType, List<FileChooser.ExtensionFilter> list, FileChooser.ExtensionFilter extensionFilter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File showDirectoryChooser(TKStage tKStage, String string, File file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getMultiClickTime() {
        return 0L;
    }

    @Override
    public int getMultiClickMaxX() {
        return 0;
    }

    @Override
    public int getMultiClickMaxY() {
        return 0;
    }

    @Override
    public void requestNextPulse() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public GlassRobot createRobot() {
        throw new UnsupportedOperationException("not implemented");
    }
}

