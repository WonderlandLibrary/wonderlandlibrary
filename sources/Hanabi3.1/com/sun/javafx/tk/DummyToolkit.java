package com.sun.javafx.tk;

import com.sun.glass.ui.CommonDialogs;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.runtime.async.AsyncOperation;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.animation.AbstractPrimaryTimer;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import java.io.File;
import java.io.InputStream;
import java.security.AccessControlContext;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

public final class DummyToolkit extends Toolkit {
   public boolean init() {
      return true;
   }

   public boolean canStartNestedEventLoop() {
      return false;
   }

   public Object enterNestedEventLoop(Object var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void exitNestedEventLoop(Object var1, Object var2) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public TKStage createTKStage(Window var1, boolean var2, StageStyle var3, boolean var4, Modality var5, TKStage var6, boolean var7, AccessControlContext var8) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public TKStage createTKPopupStage(Window var1, StageStyle var2, TKStage var3, AccessControlContext var4) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public TKStage createTKEmbeddedStage(HostInterface var1, AccessControlContext var2) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public AppletWindow createAppletWindow(long var1, String var3) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void closeAppletWindow() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public TKSystemMenu getSystemMenu() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public ImageLoader loadImage(String var1, int var2, int var3, boolean var4, boolean var5) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public ImageLoader loadImage(InputStream var1, int var2, int var3, boolean var4, boolean var5) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public AsyncOperation loadImageAsync(AsyncOperationListener var1, String var2, int var3, int var4, boolean var5, boolean var6) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public ImageLoader loadPlatformImage(Object var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public PlatformImage createPlatformImage(int var1, int var2) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void startup(Runnable var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void defer(Runnable var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public Future addRenderJob(RenderJob var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public Map getContextMap() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public int getRefreshRate() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void setAnimationRunnable(DelayedRunnable var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public PerformanceTracker getPerformanceTracker() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public PerformanceTracker createPerformanceTracker() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void waitFor(Toolkit.Task var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   protected Object createColorPaint(Color var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   protected Object createLinearGradientPaint(LinearGradient var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   protected Object createRadialGradientPaint(RadialGradient var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   protected Object createImagePatternPaint(ImagePattern var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void accumulateStrokeBounds(Shape var1, float[] var2, StrokeType var3, double var4, StrokeLineCap var6, StrokeLineJoin var7, float var8, BaseTransform var9) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public boolean strokeContains(Shape var1, double var2, double var4, StrokeType var6, double var7, StrokeLineCap var9, StrokeLineJoin var10, float var11) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public Shape createStrokedShape(Shape var1, StrokeType var2, double var3, StrokeLineCap var5, StrokeLineJoin var6, float var7, float[] var8, float var9) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public int getKeyCodeForChar(String var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public Dimension2D getBestCursorSize(int var1, int var2) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public int getMaximumCursorColors() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public PathElement[] convertShapeToFXPath(Object var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public HitInfo convertHitInfoToFX(Object var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public Filterable toFilterable(Image var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public FilterContext getFilterContext(Object var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public boolean isForwardTraversalKey(KeyEvent var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public boolean isBackwardTraversalKey(KeyEvent var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public boolean isNestedLoopRunning() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public AbstractPrimaryTimer getPrimaryTimer() {
      return null;
   }

   public FontLoader getFontLoader() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public TextLayoutFactory getTextLayoutFactory() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public Object createSVGPathObject(SVGPath var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public Path2D createSVGPath2D(SVGPath var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public boolean imageContains(Object var1, float var2, float var3) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public TKClipboard getSystemClipboard() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public TKClipboard getNamedClipboard(String var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public ScreenConfigurationAccessor setScreenConfigurationListener(TKScreenConfigurationListener var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public Object getPrimaryScreen() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public List getScreens() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public ScreenConfigurationAccessor getScreenConfigurationAccessor() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void registerDragGestureListener(TKScene var1, Set var2, TKDragGestureListener var3) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void startDrag(TKScene var1, Set var2, TKDragSourceListener var3, Dragboard var4) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void enableDrop(TKScene var1, TKDropTargetListener var2) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void installInputMethodRequests(TKScene var1, InputMethodRequests var2) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public Object renderToImage(Toolkit.ImageRenderingContext var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public KeyCode getPlatformShortcutKey() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public CommonDialogs.FileChooserResult showFileChooser(TKStage var1, String var2, File var3, String var4, FileChooserType var5, List var6, FileChooser.ExtensionFilter var7) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public File showDirectoryChooser(TKStage var1, String var2, File var3) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public long getMultiClickTime() {
      return 0L;
   }

   public int getMultiClickMaxX() {
      return 0;
   }

   public int getMultiClickMaxY() {
      return 0;
   }

   public void requestNextPulse() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
