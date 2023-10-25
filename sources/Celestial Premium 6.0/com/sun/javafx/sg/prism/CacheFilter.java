/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.logging.PulseLogger
 *  javafx.geometry.Insets
 *  javafx.scene.CacheHint
 *  javafx.scene.layout.Background
 *  javafx.scene.layout.BackgroundFill
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.sg.prism.NGGroup;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGRegion;
import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class CacheFilter {
    private static final Rectangle TEMP_RECT = new Rectangle();
    private static final DirtyRegionContainer TEMP_CONTAINER = new DirtyRegionContainer(1);
    private static final Affine3D TEMP_CACHEFILTER_TRANSFORM = new Affine3D();
    private static final RectBounds TEMP_BOUNDS = new RectBounds();
    private static final double EPSILON = 1.0E-7;
    private RTTexture tempTexture;
    private double lastXDelta;
    private double lastYDelta;
    private ScrollCacheState scrollCacheState = ScrollCacheState.CHECKING_PRECONDITIONS;
    private ImageData cachedImageData;
    private Rectangle cacheBounds = new Rectangle();
    private final Affine2D cachedXform = new Affine2D();
    private double cachedScaleX;
    private double cachedScaleY;
    private double cachedRotate;
    private double cachedX;
    private double cachedY;
    private NGNode node;
    private final Affine2D screenXform = new Affine2D();
    private boolean scaleHint;
    private boolean rotateHint;
    private CacheHint cacheHint;
    private boolean wasUnsupported = false;

    private Rectangle computeDirtyRegionForTranslate() {
        if (this.lastXDelta != 0.0) {
            if (this.lastXDelta > 0.0) {
                TEMP_RECT.setBounds(0, 0, (int)this.lastXDelta, this.cacheBounds.height);
            } else {
                TEMP_RECT.setBounds(this.cacheBounds.width + (int)this.lastXDelta, 0, -((int)this.lastXDelta), this.cacheBounds.height);
            }
        } else if (this.lastYDelta > 0.0) {
            TEMP_RECT.setBounds(0, 0, this.cacheBounds.width, (int)this.lastYDelta);
        } else {
            TEMP_RECT.setBounds(0, this.cacheBounds.height + (int)this.lastYDelta, this.cacheBounds.width, -((int)this.lastYDelta));
        }
        return TEMP_RECT;
    }

    protected CacheFilter(NGNode nGNode, CacheHint cacheHint) {
        this.node = nGNode;
        this.scrollCacheState = ScrollCacheState.CHECKING_PRECONDITIONS;
        this.setHint(cacheHint);
    }

    public void setHint(CacheHint cacheHint) {
        this.cacheHint = cacheHint;
        this.scaleHint = cacheHint == CacheHint.SPEED || cacheHint == CacheHint.SCALE || cacheHint == CacheHint.SCALE_AND_ROTATE;
        this.rotateHint = cacheHint == CacheHint.SPEED || cacheHint == CacheHint.ROTATE || cacheHint == CacheHint.SCALE_AND_ROTATE;
    }

    final boolean isScaleHint() {
        return this.scaleHint;
    }

    final boolean isRotateHint() {
        return this.rotateHint;
    }

    boolean matchesHint(CacheHint cacheHint) {
        return this.cacheHint == cacheHint;
    }

    boolean unsupported(double[] arrd) {
        double d = arrd[0];
        double d2 = arrd[1];
        double d3 = arrd[2];
        return (d3 > 1.0E-7 || d3 < -1.0E-7) && (d > d2 + 1.0E-7 || d2 > d + 1.0E-7 || d < d2 - 1.0E-7 || d2 < d - 1.0E-7 || this.cachedScaleX > this.cachedScaleY + 1.0E-7 || this.cachedScaleY > this.cachedScaleX + 1.0E-7 || this.cachedScaleX < this.cachedScaleY - 1.0E-7 || this.cachedScaleY < this.cachedScaleX - 1.0E-7);
    }

    private boolean isXformScrollCacheCapable(double[] arrd) {
        if (this.unsupported(arrd)) {
            return false;
        }
        double d = arrd[2];
        return this.rotateHint || d == 0.0;
    }

    private boolean needToRenderCache(BaseTransform baseTransform, double[] arrd, float f, float f2) {
        if (this.cachedImageData == null) {
            return true;
        }
        if (this.lastXDelta != 0.0 || this.lastYDelta != 0.0) {
            if (Math.abs(this.lastXDelta) >= (double)this.cacheBounds.width || Math.abs(this.lastYDelta) >= (double)this.cacheBounds.height || Math.rint(this.lastXDelta) != this.lastXDelta || Math.rint(this.lastYDelta) != this.lastYDelta) {
                this.node.clearDirtyTree();
                this.lastYDelta = 0.0;
                this.lastXDelta = 0.0;
                return true;
            }
            if (this.scrollCacheState == ScrollCacheState.CHECKING_PRECONDITIONS) {
                if (this.scrollCacheCapable() && this.isXformScrollCacheCapable(arrd)) {
                    this.scrollCacheState = ScrollCacheState.ENABLED;
                } else {
                    this.scrollCacheState = ScrollCacheState.DISABLED;
                    return true;
                }
            }
        }
        if (this.cachedXform.getMxx() == baseTransform.getMxx() && this.cachedXform.getMyy() == baseTransform.getMyy() && this.cachedXform.getMxy() == baseTransform.getMxy() && this.cachedXform.getMyx() == baseTransform.getMyx()) {
            return false;
        }
        if (this.wasUnsupported || this.unsupported(arrd)) {
            return true;
        }
        double d = arrd[0];
        double d2 = arrd[1];
        double d3 = arrd[2];
        if (this.scaleHint) {
            if (this.cachedScaleX < (double)f || this.cachedScaleY < (double)f2) {
                return true;
            }
            if (this.rotateHint) {
                return false;
            }
            return !(this.cachedRotate - 1.0E-7 < d3) || !(d3 < this.cachedRotate + 1.0E-7);
        }
        if (this.rotateHint) {
            return !(this.cachedScaleX - 1.0E-7 < d) || !(d < this.cachedScaleX + 1.0E-7) || !(this.cachedScaleY - 1.0E-7 < d2) || !(d2 < this.cachedScaleY + 1.0E-7);
        }
        return true;
    }

    void updateScreenXform(double[] arrd) {
        if (this.scaleHint) {
            if (this.rotateHint) {
                double d = arrd[0] / this.cachedScaleX;
                double d2 = arrd[1] / this.cachedScaleY;
                double d3 = arrd[2] - this.cachedRotate;
                this.screenXform.setToScale(d, d2);
                this.screenXform.rotate(d3);
            } else {
                double d = arrd[0] / this.cachedScaleX;
                double d4 = arrd[1] / this.cachedScaleY;
                this.screenXform.setToScale(d, d4);
            }
        } else if (this.rotateHint) {
            double d = arrd[2] - this.cachedRotate;
            this.screenXform.setToRotation(d, 0.0, 0.0);
        } else {
            this.screenXform.setTransform(BaseTransform.IDENTITY_TRANSFORM);
        }
    }

    public void invalidate() {
        if (this.scrollCacheState == ScrollCacheState.ENABLED) {
            this.scrollCacheState = ScrollCacheState.CHECKING_PRECONDITIONS;
        }
        this.imageDataUnref();
        this.lastYDelta = 0.0;
        this.lastXDelta = 0.0;
    }

    void imageDataUnref() {
        if (this.tempTexture != null) {
            this.tempTexture.dispose();
            this.tempTexture = null;
        }
        if (this.cachedImageData != null) {
            Filterable filterable = this.cachedImageData.getUntransformedImage();
            if (filterable != null) {
                filterable.lock();
            }
            this.cachedImageData.unref();
            this.cachedImageData = null;
        }
    }

    void invalidateByTranslation(double d, double d2) {
        if (this.cachedImageData == null) {
            return;
        }
        if (this.scrollCacheState == ScrollCacheState.DISABLED) {
            this.imageDataUnref();
        } else if (d != 0.0 && d2 != 0.0) {
            this.imageDataUnref();
        } else {
            this.lastYDelta = d2;
            this.lastXDelta = d;
        }
    }

    public void dispose() {
        this.invalidate();
        this.node = null;
    }

    double[] unmatrix(BaseTransform baseTransform) {
        double[] arrd = new double[3];
        double[][] arrarrd = new double[][]{{baseTransform.getMxx(), baseTransform.getMxy()}, {baseTransform.getMyx(), baseTransform.getMyy()}};
        double d = this.unitDir(arrarrd[0][0]);
        double d2 = this.unitDir(arrarrd[1][1]);
        double d3 = d * this.v2length(arrarrd[0]);
        this.v2scale(arrarrd[0], d);
        double d4 = this.v2dot(arrarrd[0], arrarrd[1]);
        this.v2combine(arrarrd[1], arrarrd[0], arrarrd[1], 1.0, -d4);
        double d5 = d2 * this.v2length(arrarrd[1]);
        this.v2scale(arrarrd[1], d2);
        double d6 = arrarrd[1][0];
        double d7 = arrarrd[0][0];
        double d8 = 0.0;
        d8 = d6 >= 0.0 ? Math.acos(d7) : (d7 > 0.0 ? Math.PI * 2 + Math.asin(d6) : Math.PI + Math.acos(-d7));
        arrd[0] = d3;
        arrd[1] = d5;
        arrd[2] = d8;
        return arrd;
    }

    double unitDir(double d) {
        return d < 0.0 ? -1.0 : 1.0;
    }

    void v2combine(double[] arrd, double[] arrd2, double[] arrd3, double d, double d2) {
        arrd3[0] = d * arrd[0] + d2 * arrd2[0];
        arrd3[1] = d * arrd[1] + d2 * arrd2[1];
    }

    double v2dot(double[] arrd, double[] arrd2) {
        return arrd[0] * arrd2[0] + arrd[1] * arrd2[1];
    }

    void v2scale(double[] arrd, double d) {
        double d2 = this.v2length(arrd);
        if (d2 != 0.0) {
            arrd[0] = arrd[0] * (d / d2);
            arrd[1] = arrd[1] * (d / d2);
        }
    }

    double v2length(double[] arrd) {
        return Math.sqrt(arrd[0] * arrd[0] + arrd[1] * arrd[1]);
    }

    void render(Graphics graphics) {
        Object object;
        float f;
        float f2;
        Filterable filterable;
        BaseTransform baseTransform = graphics.getTransformNoClone();
        PrFilterContext prFilterContext = PrFilterContext.getInstance(graphics.getAssociatedScreen());
        double[] arrd = this.unmatrix(baseTransform);
        boolean bl = this.unsupported(arrd);
        this.lastXDelta *= arrd[0];
        this.lastYDelta *= arrd[1];
        if (this.cachedImageData != null && (filterable = this.cachedImageData.getUntransformedImage()) != null) {
            filterable.lock();
            if (!this.cachedImageData.validate(prFilterContext)) {
                filterable.unlock();
                this.invalidate();
            }
        }
        if (this.needToRenderCache(baseTransform, arrd, f2 = graphics.getPixelScaleFactorX(), f = graphics.getPixelScaleFactorY())) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.incrementCounter((String)"CacheFilter rebuilding");
            }
            if (this.cachedImageData != null) {
                object = this.cachedImageData.getUntransformedImage();
                if (object != null) {
                    object.unlock();
                }
                this.invalidate();
            }
            if (this.scaleHint) {
                this.cachedScaleX = Math.max((double)f2, arrd[0]);
                this.cachedScaleY = Math.max((double)f, arrd[1]);
                this.cachedRotate = 0.0;
                this.cachedXform.setTransform(this.cachedScaleX, 0.0, 0.0, this.cachedScaleX, 0.0, 0.0);
                this.updateScreenXform(arrd);
            } else {
                this.cachedScaleX = arrd[0];
                this.cachedScaleY = arrd[1];
                this.cachedRotate = arrd[2];
                this.cachedXform.setTransform(baseTransform.getMxx(), baseTransform.getMyx(), baseTransform.getMxy(), baseTransform.getMyy(), 0.0, 0.0);
                this.screenXform.setTransform(BaseTransform.IDENTITY_TRANSFORM);
            }
            this.cacheBounds = this.getCacheBounds(this.cacheBounds, this.cachedXform);
            this.cachedImageData = this.createImageData(prFilterContext, this.cacheBounds);
            this.renderNodeToCache(this.cachedImageData, this.cacheBounds, this.cachedXform, null);
            object = this.cachedImageData.getUntransformedBounds();
            this.cachedX = ((Rectangle)object).x;
            this.cachedY = ((Rectangle)object).y;
        } else {
            if (this.scrollCacheState == ScrollCacheState.ENABLED && (this.lastXDelta != 0.0 || this.lastYDelta != 0.0)) {
                this.moveCacheBy(this.cachedImageData, this.lastXDelta, this.lastYDelta);
                this.renderNodeToCache(this.cachedImageData, this.cacheBounds, this.cachedXform, this.computeDirtyRegionForTranslate());
                this.lastYDelta = 0.0;
                this.lastXDelta = 0.0;
            }
            if (bl) {
                this.screenXform.setTransform(BaseTransform.IDENTITY_TRANSFORM);
            } else {
                this.updateScreenXform(arrd);
            }
        }
        this.wasUnsupported = bl;
        object = this.cachedImageData.getUntransformedImage();
        if (object == null) {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.incrementCounter((String)"CacheFilter not used");
            }
            this.renderNodeToScreen(graphics);
        } else {
            double d = baseTransform.getMxt();
            double d2 = baseTransform.getMyt();
            this.renderCacheToScreen(graphics, (Filterable)object, d, d2);
            object.unlock();
        }
    }

    ImageData createImageData(FilterContext filterContext, Rectangle rectangle) {
        Filterable filterable;
        try {
            filterable = Effect.getCompatibleImage(filterContext, rectangle.width, rectangle.height);
            Object t = ((PrDrawable)filterable).getTextureObject();
            t.contentsUseful();
        }
        catch (Throwable throwable) {
            filterable = null;
        }
        return new ImageData(filterContext, filterable, rectangle);
    }

    void renderNodeToCache(ImageData imageData, Rectangle rectangle, BaseTransform baseTransform, Rectangle rectangle2) {
        PrDrawable prDrawable = (PrDrawable)imageData.getUntransformedImage();
        if (prDrawable != null) {
            Graphics graphics = prDrawable.createGraphics();
            TEMP_CACHEFILTER_TRANSFORM.setToIdentity();
            TEMP_CACHEFILTER_TRANSFORM.translate(-rectangle.x, -rectangle.y);
            if (baseTransform != null) {
                TEMP_CACHEFILTER_TRANSFORM.concatenate(baseTransform);
            }
            if (rectangle2 != null) {
                TEMP_CONTAINER.deriveWithNewRegion((RectBounds)TEMP_BOUNDS.deriveWithNewBounds(rectangle2));
                this.node.doPreCulling(TEMP_CONTAINER, TEMP_CACHEFILTER_TRANSFORM, new GeneralTransform3D());
                graphics.setHasPreCullingBits(true);
                graphics.setClipRectIndex(0);
                graphics.setClipRect(rectangle2);
            }
            graphics.transform(TEMP_CACHEFILTER_TRANSFORM);
            if (this.node.getClipNode() != null) {
                this.node.renderClip(graphics);
            } else if (this.node.getEffectFilter() != null) {
                this.node.renderEffect(graphics);
            } else {
                this.node.renderContent(graphics);
            }
        }
    }

    void renderNodeToScreen(Object object) {
        Graphics graphics = (Graphics)object;
        if (this.node.getEffectFilter() != null) {
            this.node.renderEffect(graphics);
        } else {
            this.node.renderContent(graphics);
        }
    }

    void renderCacheToScreen(Object object, Filterable filterable, double d, double d2) {
        Graphics graphics = (Graphics)object;
        graphics.setTransform(this.screenXform.getMxx(), this.screenXform.getMyx(), this.screenXform.getMxy(), this.screenXform.getMyy(), d, d2);
        graphics.translate((float)this.cachedX, (float)this.cachedY);
        Object t = ((PrDrawable)filterable).getTextureObject();
        Rectangle rectangle = this.cachedImageData.getUntransformedBounds();
        graphics.drawTexture((Texture)t, 0.0f, 0.0f, rectangle.width, rectangle.height);
    }

    boolean scrollCacheCapable() {
        if (!(this.node instanceof NGGroup)) {
            return false;
        }
        List<NGNode> list = ((NGGroup)this.node).getChildren();
        if (list.size() != 1) {
            return false;
        }
        NGNode nGNode = list.get(0);
        if (!nGNode.getTransform().is2D()) {
            return false;
        }
        NGNode nGNode2 = this.node.getClipNode();
        if (nGNode2 == null || !nGNode2.isRectClip(BaseTransform.IDENTITY_TRANSFORM, false)) {
            return false;
        }
        if (this.node instanceof NGRegion) {
            NGRegion nGRegion = (NGRegion)this.node;
            if (!nGRegion.getBorder().isEmpty()) {
                return false;
            }
            Background background = nGRegion.getBackground();
            if (!background.isEmpty()) {
                if (!background.getImages().isEmpty() || background.getFills().size() != 1) {
                    return false;
                }
                BackgroundFill backgroundFill = (BackgroundFill)background.getFills().get(0);
                Paint paint = backgroundFill.getFill();
                BaseBounds baseBounds = nGNode2.getCompleteBounds(TEMP_BOUNDS, BaseTransform.IDENTITY_TRANSFORM);
                return paint.isOpaque() && paint instanceof Color && backgroundFill.getInsets().equals((Object)Insets.EMPTY) && baseBounds.getMinX() == 0.0f && baseBounds.getMinY() == 0.0f && baseBounds.getMaxX() == nGRegion.getWidth() && baseBounds.getMaxY() == nGRegion.getHeight();
            }
        }
        return true;
    }

    void moveCacheBy(ImageData imageData, double d, double d2) {
        PrDrawable prDrawable = (PrDrawable)imageData.getUntransformedImage();
        Rectangle rectangle = imageData.getUntransformedBounds();
        int n = (int)Math.max(0.0, -d);
        int n2 = (int)Math.max(0.0, -d2);
        int n3 = (int)Math.max(0.0, d);
        int n4 = (int)Math.max(0.0, d2);
        int n5 = rectangle.width - (int)Math.abs(d);
        int n6 = rectangle.height - (int)Math.abs(d2);
        Graphics graphics = prDrawable.createGraphics();
        if (this.tempTexture != null) {
            this.tempTexture.lock();
            if (this.tempTexture.isSurfaceLost()) {
                this.tempTexture = null;
            }
        }
        if (this.tempTexture == null) {
            this.tempTexture = graphics.getResourceFactory().createRTTexture(prDrawable.getPhysicalWidth(), prDrawable.getPhysicalHeight(), Texture.WrapMode.CLAMP_NOT_NEEDED);
        }
        Graphics graphics2 = this.tempTexture.createGraphics();
        graphics2.clear();
        graphics2.drawTexture((Texture)prDrawable.getTextureObject(), 0.0f, 0.0f, n5, n6, n, n2, n + n5, n2 + n6);
        graphics2.sync();
        graphics.clear();
        graphics.drawTexture(this.tempTexture, n3, n4, n3 + n5, n4 + n6, 0.0f, 0.0f, n5, n6);
        this.tempTexture.unlock();
    }

    Rectangle getCacheBounds(Rectangle rectangle, BaseTransform baseTransform) {
        BaseBounds baseBounds = this.node.getClippedBounds(TEMP_BOUNDS, baseTransform);
        rectangle.setBounds(baseBounds);
        return rectangle;
    }

    BaseBounds computeDirtyBounds(BaseBounds baseBounds, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        baseBounds = !this.node.dirtyBounds.isEmpty() ? baseBounds.deriveWithNewBounds(this.node.dirtyBounds) : baseBounds.deriveWithNewBounds(this.node.transformedBounds);
        if (!baseBounds.isEmpty()) {
            baseBounds.roundOut();
            baseBounds = this.node.computePadding(baseBounds);
            baseBounds = baseTransform.transform(baseBounds, baseBounds);
            baseBounds = generalTransform3D.transform(baseBounds, baseBounds);
        }
        return baseBounds;
    }

    private static final class ScrollCacheState
    extends Enum<ScrollCacheState> {
        public static final /* enum */ ScrollCacheState CHECKING_PRECONDITIONS = new ScrollCacheState();
        public static final /* enum */ ScrollCacheState ENABLED = new ScrollCacheState();
        public static final /* enum */ ScrollCacheState DISABLED = new ScrollCacheState();
        private static final /* synthetic */ ScrollCacheState[] $VALUES;

        public static ScrollCacheState[] values() {
            return (ScrollCacheState[])$VALUES.clone();
        }

        public static ScrollCacheState valueOf(String string) {
            return Enum.valueOf(ScrollCacheState.class, string);
        }

        private static /* synthetic */ ScrollCacheState[] $values() {
            return new ScrollCacheState[]{CHECKING_PRECONDITIONS, ENABLED, DISABLED};
        }

        static {
            $VALUES = ScrollCacheState.$values();
        }
    }
}

