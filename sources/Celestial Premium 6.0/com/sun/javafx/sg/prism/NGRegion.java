/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.PlatformUtil
 *  com.sun.javafx.logging.PulseLogger
 *  javafx.geometry.Insets
 *  javafx.geometry.Side
 *  javafx.scene.Node
 *  javafx.scene.layout.Background
 *  javafx.scene.layout.BackgroundFill
 *  javafx.scene.layout.BackgroundImage
 *  javafx.scene.layout.BackgroundPosition
 *  javafx.scene.layout.BackgroundRepeat
 *  javafx.scene.layout.BackgroundSize
 *  javafx.scene.layout.Border
 *  javafx.scene.layout.BorderImage
 *  javafx.scene.layout.BorderRepeat
 *  javafx.scene.layout.BorderStroke
 *  javafx.scene.layout.BorderStrokeStyle
 *  javafx.scene.layout.BorderWidths
 *  javafx.scene.layout.CornerRadii
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.ImagePattern
 *  javafx.scene.paint.LinearGradient
 *  javafx.scene.paint.Paint
 *  javafx.scene.shape.Shape
 *  javafx.scene.shape.StrokeLineCap
 *  javafx.scene.shape.StrokeLineJoin
 *  javafx.scene.shape.StrokeType
 */
package com.sun.javafx.sg.prism;

import com.sun.glass.ui.Screen;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.sg.prism.EffectFilter;
import com.sun.javafx.sg.prism.NGGroup;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.javafx.sg.prism.RegionImageCache;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Paint;
import com.sun.scenario.effect.Offset;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderRepeat;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public class NGRegion
extends NGGroup {
    private static final Affine2D SCRATCH_AFFINE = new Affine2D();
    private static final Rectangle TEMP_RECT = new Rectangle();
    private static WeakHashMap<Screen, RegionImageCache> imageCacheMap = new WeakHashMap();
    private static final int CACHE_SLICE_V = 1;
    private static final int CACHE_SLICE_H = 2;
    private Background background = Background.EMPTY;
    private Insets backgroundInsets = Insets.EMPTY;
    private Border border = Border.EMPTY;
    private List<CornerRadii> normalizedFillCorners;
    private List<CornerRadii> normalizedStrokeCorners;
    private Shape shape;
    private NGShape ngShape;
    private boolean scaleShape = true;
    private boolean centerShape = true;
    private boolean cacheShape = false;
    private float opaqueTop = Float.NaN;
    private float opaqueRight = Float.NaN;
    private float opaqueBottom = Float.NaN;
    private float opaqueLeft = Float.NaN;
    private float width;
    private float height;
    private int cacheMode;
    private Integer cacheKey;
    private static final Offset nopEffect = new Offset(0, 0, null);
    private EffectFilter nopEffectFilter;

    static Paint getPlatformPaint(javafx.scene.paint.Paint paint) {
        return (Paint)Toolkit.getPaintAccessor().getPlatformPaint(paint);
    }

    public void updateShape(Object object, boolean bl, boolean bl2, boolean bl3) {
        this.ngShape = object == null ? null : (NGShape)NodeHelper.getPeer((Node)((javafx.scene.shape.Shape)object));
        this.shape = object == null ? null : this.ngShape.getShape();
        this.scaleShape = bl;
        this.centerShape = bl2;
        this.cacheShape = bl3;
        this.invalidateOpaqueRegion();
        this.cacheKey = null;
        this.visualsChanged();
    }

    public void setSize(float f, float f2) {
        this.width = f;
        this.height = f2;
        this.invalidateOpaqueRegion();
        this.cacheKey = null;
        this.visualsChanged();
        if (this.background != null && this.background.isFillPercentageBased()) {
            this.backgroundInsets = null;
        }
    }

    public void imagesUpdated() {
        this.visualsChanged();
    }

    public void updateBorder(Border border) {
        Border border2 = this.border;
        Border border3 = this.border = border == null ? Border.EMPTY : border;
        if (!this.border.getOutsets().equals((Object)border2.getOutsets())) {
            this.geometryChanged();
        } else {
            this.visualsChanged();
        }
    }

    public void updateStrokeCorners(List<CornerRadii> list) {
        this.normalizedStrokeCorners = list;
    }

    private CornerRadii getNormalizedStrokeRadii(int n) {
        return this.normalizedStrokeCorners == null ? ((BorderStroke)this.border.getStrokes().get(n)).getRadii() : this.normalizedStrokeCorners.get(n);
    }

    public void updateBackground(Background background) {
        Background background2 = this.background;
        this.background = background == null ? Background.EMPTY : background;
        List list = this.background.getFills();
        this.cacheMode = 0;
        if (!(PrismSettings.disableRegionCaching || list.isEmpty() || this.shape != null && !this.cacheShape)) {
            this.cacheMode = 3;
            int n = list.size();
            for (int i = 0; i < n && this.cacheMode != 0; ++i) {
                BackgroundFill backgroundFill = (BackgroundFill)list.get(i);
                javafx.scene.paint.Paint paint = backgroundFill.getFill();
                if (this.shape == null) {
                    if (paint instanceof LinearGradient) {
                        LinearGradient linearGradient = (LinearGradient)paint;
                        if (linearGradient.getStartX() != linearGradient.getEndX()) {
                            this.cacheMode &= 0xFFFFFFFD;
                        }
                        if (linearGradient.getStartY() == linearGradient.getEndY()) continue;
                        this.cacheMode &= 0xFFFFFFFE;
                        continue;
                    }
                    if (paint instanceof Color) continue;
                    this.cacheMode = 0;
                    continue;
                }
                if (!(paint instanceof ImagePattern)) continue;
                this.cacheMode = 0;
            }
        }
        this.backgroundInsets = null;
        this.cacheKey = null;
        if (!this.background.getOutsets().equals((Object)background2.getOutsets())) {
            this.geometryChanged();
        } else {
            this.visualsChanged();
        }
    }

    public void updateFillCorners(List<CornerRadii> list) {
        this.normalizedFillCorners = list;
    }

    private CornerRadii getNormalizedFillRadii(int n) {
        return this.normalizedFillCorners == null ? ((BackgroundFill)this.background.getFills().get(n)).getRadii() : this.normalizedFillCorners.get(n);
    }

    public void setOpaqueInsets(float f, float f2, float f3, float f4) {
        this.opaqueTop = f;
        this.opaqueRight = f2;
        this.opaqueBottom = f3;
        this.opaqueLeft = f4;
        this.invalidateOpaqueRegion();
    }

    @Override
    public void clearDirtyTree() {
        super.clearDirtyTree();
        if (this.ngShape != null) {
            this.ngShape.clearDirtyTree();
        }
    }

    private RegionImageCache getImageCache(Graphics graphics) {
        RTTexture rTTexture;
        Screen screen = graphics.getAssociatedScreen();
        RegionImageCache regionImageCache = imageCacheMap.get(screen);
        if (regionImageCache != null && (rTTexture = regionImageCache.getBackingStore()).isSurfaceLost()) {
            imageCacheMap.remove(screen);
            regionImageCache = null;
        }
        if (regionImageCache == null) {
            regionImageCache = new RegionImageCache(graphics.getResourceFactory());
            imageCacheMap.put(screen, regionImageCache);
        }
        return regionImageCache;
    }

    private Integer getCacheKey(int n, int n2) {
        if (this.cacheKey == null) {
            int n3 = 31 * n;
            n3 = n3 * 37 + n2;
            n3 = n3 * 47 + this.background.hashCode();
            if (this.shape != null) {
                n3 = n3 * 73 + this.shape.hashCode();
            }
            this.cacheKey = n3;
        }
        return this.cacheKey;
    }

    @Override
    protected boolean supportsOpaqueRegions() {
        return true;
    }

    @Override
    protected boolean hasOpaqueRegion() {
        return super.hasOpaqueRegion() && !Float.isNaN(this.opaqueTop) && !Float.isNaN(this.opaqueRight) && !Float.isNaN(this.opaqueBottom) && !Float.isNaN(this.opaqueLeft);
    }

    @Override
    protected RectBounds computeOpaqueRegion(RectBounds rectBounds) {
        return (RectBounds)rectBounds.deriveWithNewBounds(this.opaqueLeft, this.opaqueTop, 0.0f, this.width - this.opaqueRight, this.height - this.opaqueBottom, 0.0f);
    }

    @Override
    protected NGNode.RenderRootResult computeRenderRoot(NodePath nodePath, RectBounds rectBounds, int n, BaseTransform baseTransform, GeneralTransform3D generalTransform3D) {
        NGNode.RenderRootResult renderRootResult = super.computeRenderRoot(nodePath, rectBounds, n, baseTransform, generalTransform3D);
        if (renderRootResult == NGNode.RenderRootResult.NO_RENDER_ROOT) {
            renderRootResult = this.computeNodeRenderRoot(nodePath, rectBounds, n, baseTransform, generalTransform3D);
        }
        return renderRootResult;
    }

    @Override
    protected boolean hasVisuals() {
        return !this.border.isEmpty() || !this.background.isEmpty();
    }

    @Override
    protected boolean hasOverlappingContents() {
        return true;
    }

    @Override
    protected void renderContent(Graphics graphics) {
        if (!graphics.getTransformNoClone().is2D() && this.isContentBounds2D()) {
            assert (this.getEffectFilter() == null);
            if (this.nopEffectFilter == null) {
                this.nopEffectFilter = new EffectFilter(nopEffect, this);
            }
            this.nopEffectFilter.render(graphics);
            return;
        }
        if (this.shape != null) {
            this.renderAsShape(graphics);
        } else if (this.width > 0.0f && this.height > 0.0f) {
            this.renderAsRectangle(graphics);
        }
        super.renderContent(graphics);
    }

    private void renderAsShape(Graphics graphics) {
        Object object;
        if (!this.background.isEmpty()) {
            RegionImageCache regionImageCache;
            object = this.background.getOutsets();
            Shape shape = this.resizeShape((float)(-object.getTop()), (float)(-object.getRight()), (float)(-object.getBottom()), (float)(-object.getLeft()));
            RectBounds rectBounds = shape.getBounds();
            int n = Math.round(rectBounds.getWidth());
            int n2 = Math.round(rectBounds.getHeight());
            RTTexture rTTexture = null;
            Rectangle rectangle = null;
            if (this.cacheMode != 0 && graphics.getTransformNoClone().isTranslateOrIdentity() && !(graphics instanceof PrinterGraphics) && (regionImageCache = this.getImageCache(graphics)).isImageCachable(n, n2)) {
                Integer n3 = this.getCacheKey(n, n2);
                rectangle = TEMP_RECT;
                rectangle.setBounds(0, 0, n + 1, n2 + 1);
                boolean bl = regionImageCache.getImageLocation(n3, rectangle, this.background, this.shape, graphics);
                if (!rectangle.isEmpty()) {
                    rTTexture = regionImageCache.getBackingStore();
                }
                if (rTTexture != null && bl) {
                    Graphics graphics2 = rTTexture.createGraphics();
                    graphics2.translate((float)rectangle.x - rectBounds.getMinX(), (float)rectangle.y - rectBounds.getMinY());
                    this.renderBackgroundShape(graphics2);
                    if (PulseLogger.PULSE_LOGGING_ENABLED) {
                        PulseLogger.incrementCounter((String)"Rendering region shape image to cache");
                    }
                }
            }
            if (rTTexture != null) {
                float f = rectBounds.getMinX();
                float f2 = rectBounds.getMinY();
                float f3 = rectBounds.getMaxX();
                float f4 = rectBounds.getMaxY();
                float f5 = rectangle.x;
                float f6 = rectangle.y;
                float f7 = f5 + (float)n;
                float f8 = f6 + (float)n2;
                graphics.drawTexture(rTTexture, f, f2, f3, f4, f5, f6, f7, f8);
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.incrementCounter((String)"Cached region shape image used");
                }
            } else {
                this.renderBackgroundShape(graphics);
            }
        }
        if (!this.border.isEmpty()) {
            object = this.border.getStrokes();
            int n = object.size();
            for (int i = 0; i < n; ++i) {
                BorderStroke borderStroke = (BorderStroke)object.get(i);
                this.setBorderStyle(graphics, borderStroke, -1.0, false);
                Insets insets = borderStroke.getInsets();
                graphics.draw(this.resizeShape((float)insets.getTop(), (float)insets.getRight(), (float)insets.getBottom(), (float)insets.getLeft()));
            }
        }
    }

    private void renderBackgroundShape(Graphics graphics) {
        Object object;
        Paint paint;
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.incrementCounter((String)"NGRegion renderBackgroundShape slow path");
            PulseLogger.addMessage((String)("Slow shape path for " + this.getName()));
        }
        List list = this.background.getFills();
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            BackgroundFill backgroundFill = (BackgroundFill)list.get(i);
            paint = NGRegion.getPlatformPaint(backgroundFill.getFill());
            assert (paint != null);
            graphics.setPaint(paint);
            object = backgroundFill.getInsets();
            graphics.fill(this.resizeShape((float)object.getTop(), (float)object.getRight(), (float)object.getBottom(), (float)object.getLeft()));
        }
        List list2 = this.background.getImages();
        int n2 = list2.size();
        for (n = 0; n < n2; ++n) {
            paint = (BackgroundImage)list2.get(n);
            object = (Image)Toolkit.getImageAccessor().getPlatformImage(paint.getImage());
            if (object == null) continue;
            Shape shape = this.resizeShape(0.0f, 0.0f, 0.0f, 0.0f);
            RectBounds rectBounds = shape.getBounds();
            com.sun.prism.paint.ImagePattern imagePattern = paint.getSize().isCover() ? new com.sun.prism.paint.ImagePattern((Image)object, rectBounds.getMinX(), rectBounds.getMinY(), rectBounds.getWidth(), rectBounds.getHeight(), false, false) : new com.sun.prism.paint.ImagePattern((Image)object, rectBounds.getMinX(), rectBounds.getMinY(), ((Image)object).getWidth(), ((Image)object).getHeight(), false, false);
            graphics.setPaint(imagePattern);
            graphics.fill(shape);
        }
    }

    private void renderAsRectangle(Graphics graphics) {
        if (!this.background.isEmpty()) {
            this.renderBackgroundRectangle(graphics);
        }
        if (!this.border.isEmpty()) {
            this.renderBorderRectangle(graphics);
        }
    }

    private void renderBackgroundRectangle(Graphics graphics) {
        Object object;
        int n;
        Object object2;
        if (this.backgroundInsets == null) {
            this.updateBackgroundInsets();
        }
        double d = this.backgroundInsets.getLeft() + 1.0;
        double d2 = this.backgroundInsets.getRight() + 1.0;
        double d3 = this.backgroundInsets.getTop() + 1.0;
        double d4 = this.backgroundInsets.getBottom() + 1.0;
        int n2 = this.roundUp(this.width);
        if ((this.cacheMode & 2) != 0) {
            n2 = Math.min(n2, (int)(d + d2));
        }
        int n3 = this.roundUp(this.height);
        if ((this.cacheMode & 1) != 0) {
            n3 = Math.min(n3, (int)(d3 + d4));
        }
        Insets insets = this.background.getOutsets();
        int n4 = this.roundUp(insets.getTop());
        int n5 = this.roundUp(insets.getRight());
        int n6 = this.roundUp(insets.getBottom());
        int n7 = this.roundUp(insets.getLeft());
        int n8 = n7 + n2 + n5;
        int n9 = n4 + n3 + n6;
        boolean bl = this.background.getFills().size() > 1 && this.cacheMode != 0 && graphics.getTransformNoClone().isTranslateOrIdentity() && !(graphics instanceof PrinterGraphics);
        RTTexture rTTexture = null;
        Rectangle rectangle = null;
        if (bl && ((RegionImageCache)(object2 = this.getImageCache(graphics))).isImageCachable(n8, n9)) {
            Integer n10 = this.getCacheKey(n8, n9);
            rectangle = TEMP_RECT;
            rectangle.setBounds(0, 0, n8 + 1, n9 + 1);
            n = ((RegionImageCache)object2).getImageLocation(n10, rectangle, this.background, this.shape, graphics) ? 1 : 0;
            if (!rectangle.isEmpty()) {
                rTTexture = ((RegionImageCache)object2).getBackingStore();
            }
            if (rTTexture != null && n != 0) {
                object = rTTexture.createGraphics();
                object.translate(rectangle.x + n7, rectangle.y + n4);
                this.renderBackgroundRectanglesDirectly((Graphics)object, n2, n3);
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.incrementCounter((String)"Rendering region background image to cache");
                }
            }
        }
        if (rTTexture != null) {
            this.renderBackgroundRectangleFromCache(graphics, rTTexture, rectangle, n8, n9, d3, d2, d4, d, n4, n5, n6, n7);
        } else {
            this.renderBackgroundRectanglesDirectly(graphics, this.width, this.height);
        }
        object2 = this.background.getImages();
        n = object2.size();
        for (int i = 0; i < n; ++i) {
            double d5;
            double d6;
            double d7;
            double d8;
            double d9;
            double d10;
            object = (BackgroundImage)object2.get(i);
            Image image = (Image)Toolkit.getImageAccessor().getPlatformImage(object.getImage());
            if (image == null) continue;
            int n11 = (int)object.getImage().getWidth();
            int n12 = (int)object.getImage().getHeight();
            int n13 = image.getWidth();
            int n14 = image.getHeight();
            if (n13 == 0 || n14 == 0) continue;
            BackgroundSize backgroundSize = object.getSize();
            if (backgroundSize.isCover()) {
                float f = Math.max(this.width / (float)n13, this.height / (float)n14);
                Texture texture = graphics.getResourceFactory().getCachedTexture(image, Texture.WrapMode.CLAMP_TO_EDGE);
                graphics.drawTexture(texture, 0.0f, 0.0f, this.width, this.height, 0.0f, 0.0f, this.width / f, this.height / f);
                texture.unlock();
                continue;
            }
            double d11 = backgroundSize.isWidthAsPercentage() ? backgroundSize.getWidth() * (double)this.width : backgroundSize.getWidth();
            double d12 = d10 = backgroundSize.isHeightAsPercentage() ? backgroundSize.getHeight() * (double)this.height : backgroundSize.getHeight();
            if (backgroundSize.isContain()) {
                float f = this.width / (float)n11;
                float f2 = this.height / (float)n12;
                float f3 = Math.min(f, f2);
                d9 = Math.ceil(f3 * (float)n11);
                d8 = Math.ceil(f3 * (float)n12);
            } else if (backgroundSize.getWidth() >= 0.0 && backgroundSize.getHeight() >= 0.0) {
                d9 = d11;
                d8 = d10;
            } else if (d11 >= 0.0) {
                d9 = d11;
                double d13 = d9 / (double)n11;
                d8 = (double)n12 * d13;
            } else if (d10 >= 0.0) {
                d8 = d10;
                double d14 = d8 / (double)n12;
                d9 = (double)n11 * d14;
            } else {
                d9 = n11;
                d8 = n12;
            }
            BackgroundPosition backgroundPosition = object.getPosition();
            if (backgroundPosition.getHorizontalSide() == Side.LEFT) {
                d7 = backgroundPosition.getHorizontalPosition();
                d6 = backgroundPosition.isHorizontalAsPercentage() ? d7 * (double)this.width - d7 * d9 : d7;
            } else if (backgroundPosition.isHorizontalAsPercentage()) {
                d7 = 1.0 - backgroundPosition.getHorizontalPosition();
                d6 = d7 * (double)this.width - d7 * d9;
            } else {
                d6 = (double)this.width - d9 - backgroundPosition.getHorizontalPosition();
            }
            if (backgroundPosition.getVerticalSide() == Side.TOP) {
                d7 = backgroundPosition.getVerticalPosition();
                d5 = backgroundPosition.isVerticalAsPercentage() ? d7 * (double)this.height - d7 * d8 : d7;
            } else if (backgroundPosition.isVerticalAsPercentage()) {
                d7 = 1.0 - backgroundPosition.getVerticalPosition();
                d5 = d7 * (double)this.height - d7 * d8;
            } else {
                d5 = (double)this.height - d8 - backgroundPosition.getVerticalPosition();
            }
            this.paintTiles(graphics, image, object.getRepeatX(), object.getRepeatY(), backgroundPosition.getHorizontalSide(), backgroundPosition.getVerticalSide(), 0.0f, 0.0f, this.width, this.height, 0, 0, n13, n14, (float)d6, (float)d5, (float)d9, (float)d8);
        }
    }

    private void renderBackgroundRectangleFromCache(Graphics graphics, RTTexture rTTexture, Rectangle rectangle, int n, int n2, double d, double d2, double d3, double d4, int n3, int n4, int n5, int n6) {
        double d5;
        float f = (float)n6 + this.width + (float)n4;
        float f2 = (float)n3 + this.height + (float)n5;
        boolean bl = (float)n == f;
        boolean bl2 = (float)n2 == f2;
        float f3 = (float)(-n6) - 0.49609375f;
        float f4 = (float)(-n3) - 0.49609375f;
        float f5 = this.width + (float)n4 + 0.49609375f;
        float f6 = this.height + (float)n5 + 0.49609375f;
        float f7 = (float)rectangle.x - 0.49609375f;
        float f8 = (float)rectangle.y - 0.49609375f;
        float f9 = (float)(rectangle.x + n) + 0.49609375f;
        float f10 = (float)(rectangle.y + n2) + 0.49609375f;
        double d6 = d4;
        double d7 = d2;
        double d8 = d;
        double d9 = d3;
        if (d4 + d2 > (double)this.width) {
            d5 = (double)this.width / (d4 + d2);
            d6 *= d5;
            d7 *= d5;
        }
        if (d + d3 > (double)this.height) {
            d5 = (double)this.height / (d + d3);
            d8 *= d5;
            d9 *= d5;
        }
        if (bl && bl2) {
            graphics.drawTexture(rTTexture, f3, f4, f5, f6, f7, f8, f9, f10);
        } else if (bl2) {
            float f11 = 0.49609375f + (float)(d6 + (double)n6);
            float f12 = 0.49609375f + (float)(d7 + (double)n4);
            float f13 = f3 + f11;
            float f14 = f5 - f12;
            float f15 = f7 + f11;
            float f16 = f9 - f12;
            graphics.drawTexture3SliceH(rTTexture, f3, f4, f5, f6, f7, f8, f9, f10, f13, f14, f15, f16);
        } else if (bl) {
            float f17 = 0.49609375f + (float)(d8 + (double)n3);
            float f18 = 0.49609375f + (float)(d9 + (double)n5);
            float f19 = f4 + f17;
            float f20 = f6 - f18;
            float f21 = f8 + f17;
            float f22 = f10 - f18;
            graphics.drawTexture3SliceV(rTTexture, f3, f4, f5, f6, f7, f8, f9, f10, f19, f20, f21, f22);
        } else {
            float f23 = 0.49609375f + (float)(d6 + (double)n6);
            float f24 = 0.49609375f + (float)(d8 + (double)n3);
            float f25 = 0.49609375f + (float)(d7 + (double)n4);
            float f26 = 0.49609375f + (float)(d9 + (double)n5);
            float f27 = f3 + f23;
            float f28 = f5 - f25;
            float f29 = f7 + f23;
            float f30 = f9 - f25;
            float f31 = f4 + f24;
            float f32 = f6 - f26;
            float f33 = f8 + f24;
            float f34 = f10 - f26;
            graphics.drawTexture9Slice(rTTexture, f3, f4, f5, f6, f7, f8, f9, f10, f27, f31, f28, f32, f29, f33, f30, f34);
        }
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.incrementCounter((String)"Cached region background image used");
        }
    }

    private void renderBackgroundRectanglesDirectly(Graphics graphics, float f, float f2) {
        List list = this.background.getFills();
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            BackgroundFill backgroundFill = (BackgroundFill)list.get(i);
            Insets insets = backgroundFill.getInsets();
            float f3 = (float)insets.getTop();
            float f4 = (float)insets.getLeft();
            float f5 = (float)insets.getBottom();
            float f6 = (float)insets.getRight();
            float f7 = f - f4 - f6;
            float f8 = f2 - f3 - f5;
            if (!(f7 > 0.0f) || !(f8 > 0.0f)) continue;
            Paint paint = NGRegion.getPlatformPaint(backgroundFill.getFill());
            graphics.setPaint(paint);
            CornerRadii cornerRadii = this.getNormalizedFillRadii(i);
            if (cornerRadii.isUniform() && (PlatformImpl.isCaspian() || PlatformUtil.isEmbedded() || PlatformUtil.isIOS() || !(cornerRadii.getTopLeftHorizontalRadius() > 0.0) || !(cornerRadii.getTopLeftHorizontalRadius() <= 4.0))) {
                float f9 = (float)cornerRadii.getTopLeftHorizontalRadius();
                float f10 = (float)cornerRadii.getTopLeftVerticalRadius();
                if (f9 == 0.0f && f10 == 0.0f) {
                    graphics.fillRect(f4, f3, f7, f8);
                    continue;
                }
                float f11 = f9 + f9;
                float f12 = f10 + f10;
                if (f11 > f7) {
                    f11 = f7;
                }
                if (f12 > f8) {
                    f12 = f8;
                }
                graphics.fillRoundRect(f4, f3, f7, f8, f11, f12);
                continue;
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.incrementCounter((String)"NGRegion renderBackgrounds slow path");
                PulseLogger.addMessage((String)("Slow background path for " + this.getName()));
            }
            graphics.fill(this.createPath(f, f2, f3, f4, f5, f6, cornerRadii));
        }
    }

    private void renderBorderRectangle(Graphics graphics) {
        float f;
        float f2;
        float f3;
        javafx.scene.paint.Paint paint;
        javafx.scene.paint.Paint paint2;
        javafx.scene.paint.Paint paint3;
        Object object;
        BorderStroke borderStroke;
        int n;
        List list = this.border.getImages();
        List list2 = list.isEmpty() ? this.border.getStrokes() : Collections.emptyList();
        int n2 = list2.size();
        for (n = 0; n < n2; ++n) {
            double d;
            borderStroke = (BorderStroke)list2.get(n);
            object = borderStroke.getWidths();
            CornerRadii cornerRadii = this.getNormalizedStrokeRadii(n);
            Insets insets = borderStroke.getInsets();
            javafx.scene.paint.Paint paint4 = borderStroke.getTopStroke();
            paint3 = borderStroke.getRightStroke();
            paint2 = borderStroke.getBottomStroke();
            paint = borderStroke.getLeftStroke();
            float f4 = (float)insets.getTop();
            float f5 = (float)insets.getRight();
            float f6 = (float)insets.getBottom();
            float f7 = (float)insets.getLeft();
            float f8 = (float)(object.isTopAsPercentage() ? (double)this.height * object.getTop() : object.getTop());
            float f9 = (float)(object.isRightAsPercentage() ? (double)this.width * object.getRight() : object.getRight());
            float f10 = (float)(object.isBottomAsPercentage() ? (double)this.height * object.getBottom() : object.getBottom());
            float f11 = (float)(object.isLeftAsPercentage() ? (double)this.width * object.getLeft() : object.getLeft());
            BorderStrokeStyle borderStrokeStyle = borderStroke.getTopStyle();
            BorderStrokeStyle borderStrokeStyle2 = borderStroke.getRightStyle();
            BorderStrokeStyle borderStrokeStyle3 = borderStroke.getBottomStyle();
            BorderStrokeStyle borderStrokeStyle4 = borderStroke.getLeftStyle();
            StrokeType strokeType = borderStrokeStyle.getType();
            StrokeType strokeType2 = borderStrokeStyle2.getType();
            StrokeType strokeType3 = borderStrokeStyle3.getType();
            StrokeType strokeType4 = borderStrokeStyle4.getType();
            float f12 = f4 + (strokeType == StrokeType.OUTSIDE ? -f8 / 2.0f : (strokeType == StrokeType.INSIDE ? f8 / 2.0f : 0.0f));
            float f13 = f7 + (strokeType4 == StrokeType.OUTSIDE ? -f11 / 2.0f : (strokeType4 == StrokeType.INSIDE ? f11 / 2.0f : 0.0f));
            float f14 = f6 + (strokeType3 == StrokeType.OUTSIDE ? -f10 / 2.0f : (strokeType3 == StrokeType.INSIDE ? f10 / 2.0f : 0.0f));
            float f15 = f5 + (strokeType2 == StrokeType.OUTSIDE ? -f9 / 2.0f : (strokeType2 == StrokeType.INSIDE ? f9 / 2.0f : 0.0f));
            f3 = (float)cornerRadii.getTopLeftHorizontalRadius();
            if (borderStroke.isStrokeUniform()) {
                if (paint4 instanceof Color && ((Color)paint4).getOpacity() == 0.0 || borderStrokeStyle == BorderStrokeStyle.NONE) continue;
                f2 = this.width - f13 - f15;
                f = this.height - f12 - f14;
                double d2 = 2.0 * cornerRadii.getTopLeftHorizontalRadius();
                double d3 = d2 * Math.PI;
                double d4 = d3 + 2.0 * ((double)f2 - d2) + 2.0 * ((double)f - d2);
                if (!(f2 >= 0.0f) || !(f >= 0.0f)) continue;
                this.setBorderStyle(graphics, borderStroke, d4, true);
                if (cornerRadii.isUniform() && f3 == 0.0f) {
                    graphics.drawRect(f13, f12, f2, f);
                    continue;
                }
                if (cornerRadii.isUniform()) {
                    float f16 = f3 + f3;
                    if (f16 > f2) {
                        f16 = f2;
                    }
                    if (f16 > f) {
                        f16 = f;
                    }
                    graphics.drawRoundRect(f13, f12, f2, f, f16, f16);
                    continue;
                }
                graphics.draw(this.createPath(this.width, this.height, f12, f13, f14, f15, cornerRadii));
                continue;
            }
            if (cornerRadii.isUniform() && f3 == 0.0f) {
                if (!(paint4 instanceof Color && ((Color)paint4).getOpacity() == 0.0 || borderStrokeStyle == BorderStrokeStyle.NONE)) {
                    graphics.setPaint(NGRegion.getPlatformPaint(paint4));
                    if (BorderStrokeStyle.SOLID == borderStrokeStyle) {
                        graphics.fillRect(f7, f4, this.width - f7 - f5, f8);
                    } else {
                        graphics.setStroke(this.createStroke(borderStrokeStyle, f8, this.width, true));
                        graphics.drawLine(f13, f12, this.width - f15, f12);
                    }
                }
                if (!(paint3 instanceof Color && ((Color)paint3).getOpacity() == 0.0 || borderStrokeStyle2 == BorderStrokeStyle.NONE)) {
                    graphics.setPaint(NGRegion.getPlatformPaint(paint3));
                    if (BorderStrokeStyle.SOLID == borderStrokeStyle2) {
                        graphics.fillRect(this.width - f5 - f9, f4, f9, this.height - f4 - f6);
                    } else {
                        graphics.setStroke(this.createStroke(borderStrokeStyle2, f9, this.height, true));
                        graphics.drawLine(this.width - f15, f12, this.width - f15, this.height - f14);
                    }
                }
                if (!(paint2 instanceof Color && ((Color)paint2).getOpacity() == 0.0 || borderStrokeStyle3 == BorderStrokeStyle.NONE)) {
                    graphics.setPaint(NGRegion.getPlatformPaint(paint2));
                    if (BorderStrokeStyle.SOLID == borderStrokeStyle3) {
                        graphics.fillRect(f7, this.height - f6 - f10, this.width - f7 - f5, f10);
                    } else {
                        graphics.setStroke(this.createStroke(borderStrokeStyle3, f10, this.width, true));
                        graphics.drawLine(f13, this.height - f14, this.width - f15, this.height - f14);
                    }
                }
                if (paint instanceof Color && ((Color)paint).getOpacity() == 0.0 || borderStrokeStyle4 == BorderStrokeStyle.NONE) continue;
                graphics.setPaint(NGRegion.getPlatformPaint(paint));
                if (BorderStrokeStyle.SOLID == borderStrokeStyle4) {
                    graphics.fillRect(f7, f4, f11, this.height - f4 - f6);
                    continue;
                }
                graphics.setStroke(this.createStroke(borderStrokeStyle4, f11, this.height, true));
                graphics.drawLine(f13, f12, f13, this.height - f14);
                continue;
            }
            Path2D[] arrpath2D = this.createPaths(f12, f13, f14, f15, cornerRadii);
            if (borderStrokeStyle != BorderStrokeStyle.NONE) {
                double d5 = cornerRadii.getTopLeftHorizontalRadius() + cornerRadii.getTopRightHorizontalRadius();
                d = (double)this.width + d5 * -0.21460183660255172;
                graphics.setStroke(this.createStroke(borderStrokeStyle, f8, d, true));
                graphics.setPaint(NGRegion.getPlatformPaint(paint4));
                graphics.draw(arrpath2D[0]);
            }
            if (borderStrokeStyle2 != BorderStrokeStyle.NONE) {
                double d6 = cornerRadii.getTopRightVerticalRadius() + cornerRadii.getBottomRightVerticalRadius();
                d = (double)this.height + d6 * -0.21460183660255172;
                graphics.setStroke(this.createStroke(borderStrokeStyle2, f9, d, true));
                graphics.setPaint(NGRegion.getPlatformPaint(paint3));
                graphics.draw(arrpath2D[1]);
            }
            if (borderStrokeStyle3 != BorderStrokeStyle.NONE) {
                double d7 = cornerRadii.getBottomLeftHorizontalRadius() + cornerRadii.getBottomRightHorizontalRadius();
                d = (double)this.width + d7 * -0.21460183660255172;
                graphics.setStroke(this.createStroke(borderStrokeStyle3, f10, d, true));
                graphics.setPaint(NGRegion.getPlatformPaint(paint2));
                graphics.draw(arrpath2D[2]);
            }
            if (borderStrokeStyle4 == BorderStrokeStyle.NONE) continue;
            double d8 = cornerRadii.getTopLeftVerticalRadius() + cornerRadii.getBottomLeftVerticalRadius();
            d = (double)this.height + d8 * -0.21460183660255172;
            graphics.setStroke(this.createStroke(borderStrokeStyle4, f11, d, true));
            graphics.setPaint(NGRegion.getPlatformPaint(paint));
            graphics.draw(arrpath2D[3]);
        }
        n2 = list.size();
        for (n = 0; n < n2; ++n) {
            borderStroke = (BorderImage)list.get(n);
            object = (Image)Toolkit.getImageAccessor().getPlatformImage(borderStroke.getImage());
            if (object == null) continue;
            int n3 = ((Image)object).getWidth();
            int n4 = ((Image)object).getHeight();
            float f17 = ((Image)object).getPixelScale();
            paint3 = borderStroke.getWidths();
            paint2 = borderStroke.getInsets();
            paint = borderStroke.getSlices();
            int n5 = (int)Math.round(paint2.getTop());
            int n6 = (int)Math.round(paint2.getRight());
            int n7 = (int)Math.round(paint2.getBottom());
            int n8 = (int)Math.round(paint2.getLeft());
            int n9 = this.widthSize(paint3.isTopAsPercentage(), paint3.getTop(), this.height);
            int n10 = this.widthSize(paint3.isRightAsPercentage(), paint3.getRight(), this.width);
            int n11 = this.widthSize(paint3.isBottomAsPercentage(), paint3.getBottom(), this.height);
            int n12 = this.widthSize(paint3.isLeftAsPercentage(), paint3.getLeft(), this.width);
            int n13 = this.sliceSize(paint.isTopAsPercentage(), paint.getTop(), n4, f17);
            int n14 = this.sliceSize(paint.isRightAsPercentage(), paint.getRight(), n3, f17);
            int n15 = this.sliceSize(paint.isBottomAsPercentage(), paint.getBottom(), n4, f17);
            int n16 = this.sliceSize(paint.isLeftAsPercentage(), paint.getLeft(), n3, f17);
            if ((float)(n8 + n12 + n6 + n10) > this.width || (float)(n5 + n9 + n7 + n11) > this.height) continue;
            int n17 = n8 + n12;
            int n18 = n5 + n9;
            int n19 = Math.round(this.width) - n6 - n10 - n17;
            int n20 = Math.round(this.height) - n7 - n11 - n18;
            int n21 = n19 + n17;
            int n22 = n20 + n18;
            int n23 = n3 - n16 - n14;
            int n24 = n4 - n13 - n15;
            this.paintTiles(graphics, (Image)object, BorderRepeat.STRETCH, BorderRepeat.STRETCH, Side.LEFT, Side.TOP, (float)n8, (float)n5, (float)n12, (float)n9, 0, 0, n16, n13, 0.0f, 0.0f, (float)n12, (float)n9);
            f3 = borderStroke.getRepeatX() == BorderRepeat.STRETCH ? (float)n19 : (float)(n13 > 0 ? n23 * n9 / n13 : 0);
            f2 = n9;
            this.paintTiles(graphics, (Image)object, borderStroke.getRepeatX(), BorderRepeat.STRETCH, Side.LEFT, Side.TOP, (float)n17, (float)n5, (float)n19, (float)n9, n16, 0, n23, n13, ((float)n19 - f3) / 2.0f, 0.0f, f3, f2);
            this.paintTiles(graphics, (Image)object, BorderRepeat.STRETCH, BorderRepeat.STRETCH, Side.LEFT, Side.TOP, (float)n21, (float)n5, (float)n10, (float)n9, n3 - n14, 0, n14, n13, 0.0f, 0.0f, (float)n10, (float)n9);
            f3 = n12;
            f2 = borderStroke.getRepeatY() == BorderRepeat.STRETCH ? (float)n20 : (float)(n16 > 0 ? n12 * n24 / n16 : 0);
            this.paintTiles(graphics, (Image)object, BorderRepeat.STRETCH, borderStroke.getRepeatY(), Side.LEFT, Side.TOP, (float)n8, (float)n18, (float)n12, (float)n20, 0, n13, n16, n24, 0.0f, ((float)n20 - f2) / 2.0f, f3, f2);
            f3 = n10;
            f2 = borderStroke.getRepeatY() == BorderRepeat.STRETCH ? (float)n20 : (float)(n14 > 0 ? n10 * n24 / n14 : 0);
            this.paintTiles(graphics, (Image)object, BorderRepeat.STRETCH, borderStroke.getRepeatY(), Side.LEFT, Side.TOP, (float)n21, (float)n18, (float)n10, (float)n20, n3 - n14, n13, n14, n24, 0.0f, ((float)n20 - f2) / 2.0f, f3, f2);
            this.paintTiles(graphics, (Image)object, BorderRepeat.STRETCH, BorderRepeat.STRETCH, Side.LEFT, Side.TOP, (float)n8, (float)n22, (float)n12, (float)n11, 0, n4 - n15, n16, n15, 0.0f, 0.0f, (float)n12, (float)n11);
            f3 = borderStroke.getRepeatX() == BorderRepeat.STRETCH ? (float)n19 : (float)(n15 > 0 ? n23 * n11 / n15 : 0);
            f2 = n11;
            this.paintTiles(graphics, (Image)object, borderStroke.getRepeatX(), BorderRepeat.STRETCH, Side.LEFT, Side.TOP, (float)n17, (float)n22, (float)n19, (float)n11, n16, n4 - n15, n23, n15, ((float)n19 - f3) / 2.0f, 0.0f, f3, f2);
            this.paintTiles(graphics, (Image)object, BorderRepeat.STRETCH, BorderRepeat.STRETCH, Side.LEFT, Side.TOP, (float)n21, (float)n22, (float)n10, (float)n11, n3 - n14, n4 - n15, n14, n15, 0.0f, 0.0f, (float)n10, (float)n11);
            if (!borderStroke.isFilled()) continue;
            f = borderStroke.getRepeatX() == BorderRepeat.STRETCH ? (float)n19 : (float)n23;
            float f18 = borderStroke.getRepeatY() == BorderRepeat.STRETCH ? (float)n20 : (float)n24;
            this.paintTiles(graphics, (Image)object, borderStroke.getRepeatX(), borderStroke.getRepeatY(), Side.LEFT, Side.TOP, (float)n17, (float)n18, (float)n19, (float)n20, n16, n13, n23, n24, 0.0f, 0.0f, f, f18);
        }
    }

    private void updateBackgroundInsets() {
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        List list = this.background.getFills();
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            BackgroundFill backgroundFill = (BackgroundFill)list.get(i);
            Insets insets = backgroundFill.getInsets();
            CornerRadii cornerRadii = this.getNormalizedFillRadii(i);
            f = (float)Math.max((double)f, insets.getTop() + Math.max(cornerRadii.getTopLeftVerticalRadius(), cornerRadii.getTopRightVerticalRadius()));
            f2 = (float)Math.max((double)f2, insets.getRight() + Math.max(cornerRadii.getTopRightHorizontalRadius(), cornerRadii.getBottomRightHorizontalRadius()));
            f3 = (float)Math.max((double)f3, insets.getBottom() + Math.max(cornerRadii.getBottomRightVerticalRadius(), cornerRadii.getBottomLeftVerticalRadius()));
            f4 = (float)Math.max((double)f4, insets.getLeft() + Math.max(cornerRadii.getTopLeftHorizontalRadius(), cornerRadii.getBottomLeftHorizontalRadius()));
        }
        this.backgroundInsets = new Insets((double)this.roundUp(f), (double)this.roundUp(f2), (double)this.roundUp(f3), (double)this.roundUp(f4));
    }

    private int widthSize(boolean bl, double d, float f) {
        return (int)Math.round(bl ? d * (double)f : d);
    }

    private int sliceSize(boolean bl, double d, float f, float f2) {
        if (bl) {
            d *= (double)f;
        }
        if (d > (double)f) {
            d = f;
        }
        return (int)Math.round(d * (double)f2);
    }

    private int roundUp(double d) {
        return d - (double)((int)d) == 0.0 ? (int)d : (int)(d + 1.0);
    }

    private BasicStroke createStroke(BorderStrokeStyle borderStrokeStyle, double d, double d2, boolean bl) {
        BasicStroke basicStroke;
        int n;
        int n2 = borderStrokeStyle.getLineCap() == StrokeLineCap.BUTT ? 0 : (borderStrokeStyle.getLineCap() == StrokeLineCap.SQUARE ? 2 : 1);
        int n3 = borderStrokeStyle.getLineJoin() == StrokeLineJoin.BEVEL ? 2 : (borderStrokeStyle.getLineJoin() == StrokeLineJoin.MITER ? 0 : 1);
        if (bl) {
            n = 0;
        } else if (this.scaleShape) {
            n = 1;
        } else {
            switch (borderStrokeStyle.getType()) {
                case INSIDE: {
                    n = 1;
                    break;
                }
                case OUTSIDE: {
                    n = 2;
                    break;
                }
                default: {
                    n = 0;
                }
            }
        }
        if (borderStrokeStyle == BorderStrokeStyle.NONE) {
            throw new AssertionError((Object)"Should never have been asked to draw a border with NONE");
        }
        if (d <= 0.0) {
            basicStroke = new BasicStroke((float)d, n2, n3, (float)borderStrokeStyle.getMiterLimit());
        } else if (borderStrokeStyle.getDashArray().size() > 0) {
            float f;
            double[] arrd;
            List list = borderStrokeStyle.getDashArray();
            if (list == BorderStrokeStyle.DOTTED.getDashArray()) {
                if (d2 > 0.0) {
                    double d3 = d2 % (d * 2.0);
                    double d4 = d2 / (d * 2.0);
                    double d5 = d * 2.0 + d3 / d4;
                    arrd = new double[]{0.0, d5};
                    f = 0.0f;
                } else {
                    arrd = new double[]{0.0, d * 2.0};
                    f = 0.0f;
                }
            } else if (list == BorderStrokeStyle.DASHED.getDashArray()) {
                if (d2 > 0.0) {
                    double d6 = d * 2.0;
                    double d7 = d * 1.4;
                    double d8 = d6 + d7;
                    double d9 = d2 / d8;
                    double d10 = (int)d9;
                    if (d10 > 0.0) {
                        double d11 = d10 * d6;
                        d7 = (d2 - d11) / d10;
                    }
                    arrd = new double[]{d6, d7};
                    f = (float)(d6 * 0.6);
                } else {
                    arrd = new double[]{2.0 * d, 1.4 * d};
                    f = 0.0f;
                }
            } else {
                arrd = new double[list.size()];
                for (int i = 0; i < arrd.length; ++i) {
                    arrd[i] = (Double)list.get(i);
                }
                f = (float)borderStrokeStyle.getDashOffset();
            }
            basicStroke = new BasicStroke(n, (float)d, n2, n3, (float)borderStrokeStyle.getMiterLimit(), arrd, f);
        } else {
            basicStroke = new BasicStroke(n, (float)d, n2, n3, (float)borderStrokeStyle.getMiterLimit());
        }
        return basicStroke;
    }

    private void setBorderStyle(Graphics graphics, BorderStroke borderStroke, double d, boolean bl) {
        BorderWidths borderWidths = borderStroke.getWidths();
        BorderStrokeStyle borderStrokeStyle = borderStroke.getTopStyle();
        double d2 = borderWidths.isTopAsPercentage() ? (double)this.height * borderWidths.getTop() : borderWidths.getTop();
        Paint paint = NGRegion.getPlatformPaint(borderStroke.getTopStroke());
        if (borderStrokeStyle == null) {
            borderStrokeStyle = borderStroke.getLeftStyle();
            d2 = borderWidths.isLeftAsPercentage() ? (double)this.width * borderWidths.getLeft() : borderWidths.getLeft();
            paint = NGRegion.getPlatformPaint(borderStroke.getLeftStroke());
            if (borderStrokeStyle == null) {
                borderStrokeStyle = borderStroke.getBottomStyle();
                d2 = borderWidths.isBottomAsPercentage() ? (double)this.height * borderWidths.getBottom() : borderWidths.getBottom();
                paint = NGRegion.getPlatformPaint(borderStroke.getBottomStroke());
                if (borderStrokeStyle == null) {
                    borderStrokeStyle = borderStroke.getRightStyle();
                    d2 = borderWidths.isRightAsPercentage() ? (double)this.width * borderWidths.getRight() : borderWidths.getRight();
                    paint = NGRegion.getPlatformPaint(borderStroke.getRightStroke());
                }
            }
        }
        if (borderStrokeStyle == null || borderStrokeStyle == BorderStrokeStyle.NONE) {
            return;
        }
        graphics.setStroke(this.createStroke(borderStrokeStyle, d2, d, bl));
        graphics.setPaint(paint);
    }

    private void doCorner(Path2D path2D, CornerRadii cornerRadii, float f, float f2, int n, float f3, float f4, boolean bl) {
        float f5;
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        switch (n & 3) {
            case 0: {
                f10 = (float)cornerRadii.getTopLeftHorizontalRadius();
                f9 = (float)cornerRadii.getTopLeftVerticalRadius();
                f8 = 0.0f;
                f7 = f9;
                f6 = f10;
                f5 = 0.0f;
                break;
            }
            case 1: {
                f10 = (float)cornerRadii.getTopRightHorizontalRadius();
                f9 = (float)cornerRadii.getTopRightVerticalRadius();
                f8 = -f10;
                f7 = 0.0f;
                f6 = 0.0f;
                f5 = f9;
                break;
            }
            case 2: {
                f10 = (float)cornerRadii.getBottomRightHorizontalRadius();
                f9 = (float)cornerRadii.getBottomRightVerticalRadius();
                f8 = 0.0f;
                f7 = -f9;
                f6 = -f10;
                f5 = 0.0f;
                break;
            }
            case 3: {
                f10 = (float)cornerRadii.getBottomLeftHorizontalRadius();
                f9 = (float)cornerRadii.getBottomLeftVerticalRadius();
                f8 = f10;
                f7 = 0.0f;
                f6 = 0.0f;
                f5 = -f9;
                break;
            }
            default: {
                return;
            }
        }
        if (f10 > 0.0f && f9 > 0.0f) {
            path2D.appendOvalQuadrant(f + f8, f2 + f7, f, f2, f + f6, f2 + f5, f3, f4, bl ? Path2D.CornerPrefix.MOVE_THEN_CORNER : Path2D.CornerPrefix.LINE_THEN_CORNER);
        } else if (bl) {
            path2D.moveTo(f, f2);
        } else {
            path2D.lineTo(f, f2);
        }
    }

    private Path2D createPath(float f, float f2, float f3, float f4, float f5, float f6, CornerRadii cornerRadii) {
        float f7 = f - f6;
        float f8 = f2 - f5;
        Path2D path2D = new Path2D();
        this.doCorner(path2D, cornerRadii, f4, f3, 0, 0.0f, 1.0f, true);
        this.doCorner(path2D, cornerRadii, f7, f3, 1, 0.0f, 1.0f, false);
        this.doCorner(path2D, cornerRadii, f7, f8, 2, 0.0f, 1.0f, false);
        this.doCorner(path2D, cornerRadii, f4, f8, 3, 0.0f, 1.0f, false);
        path2D.closePath();
        return path2D;
    }

    private Path2D makeRoundedEdge(CornerRadii cornerRadii, float f, float f2, float f3, float f4, int n) {
        Path2D path2D = new Path2D();
        this.doCorner(path2D, cornerRadii, f, f2, n, 0.5f, 1.0f, true);
        this.doCorner(path2D, cornerRadii, f3, f4, n + 1, 0.0f, 0.5f, false);
        return path2D;
    }

    private Path2D[] createPaths(float f, float f2, float f3, float f4, CornerRadii cornerRadii) {
        float f5 = this.width - f4;
        float f6 = this.height - f3;
        return new Path2D[]{this.makeRoundedEdge(cornerRadii, f2, f, f5, f, 0), this.makeRoundedEdge(cornerRadii, f5, f, f5, f6, 1), this.makeRoundedEdge(cornerRadii, f5, f6, f2, f6, 2), this.makeRoundedEdge(cornerRadii, f2, f6, f2, f, 3)};
    }

    private Shape resizeShape(float f, float f2, float f3, float f4) {
        RectBounds rectBounds = this.shape.getBounds();
        if (this.scaleShape) {
            SCRATCH_AFFINE.setToIdentity();
            SCRATCH_AFFINE.translate(f4, f);
            float f5 = this.width - f4 - f2;
            float f6 = this.height - f - f3;
            SCRATCH_AFFINE.scale(f5 / rectBounds.getWidth(), f6 / rectBounds.getHeight());
            if (this.centerShape) {
                SCRATCH_AFFINE.translate(-rectBounds.getMinX(), -rectBounds.getMinY());
            }
            return SCRATCH_AFFINE.createTransformedShape(this.shape);
        }
        if (this.centerShape) {
            float f7 = rectBounds.getWidth();
            float f8 = rectBounds.getHeight();
            float f9 = f7 - f4 - f2;
            float f10 = f8 - f - f3;
            SCRATCH_AFFINE.setToIdentity();
            SCRATCH_AFFINE.translate(f4 + (this.width - f7) / 2.0f - rectBounds.getMinX(), f + (this.height - f8) / 2.0f - rectBounds.getMinY());
            if (f10 != f8 || f9 != f7) {
                SCRATCH_AFFINE.translate(rectBounds.getMinX(), rectBounds.getMinY());
                SCRATCH_AFFINE.scale(f9 / f7, f10 / f8);
                SCRATCH_AFFINE.translate(-rectBounds.getMinX(), -rectBounds.getMinY());
            }
            return SCRATCH_AFFINE.createTransformedShape(this.shape);
        }
        if (f != 0.0f || f2 != 0.0f || f3 != 0.0f || f4 != 0.0f) {
            float f11 = rectBounds.getWidth() - f4 - f2;
            float f12 = rectBounds.getHeight() - f - f3;
            SCRATCH_AFFINE.setToIdentity();
            SCRATCH_AFFINE.translate(f4, f);
            SCRATCH_AFFINE.translate(rectBounds.getMinX(), rectBounds.getMinY());
            SCRATCH_AFFINE.scale(f11 / rectBounds.getWidth(), f12 / rectBounds.getHeight());
            SCRATCH_AFFINE.translate(-rectBounds.getMinX(), -rectBounds.getMinY());
            return SCRATCH_AFFINE.createTransformedShape(this.shape);
        }
        return this.shape;
    }

    private void paintTiles(Graphics graphics, Image image, BorderRepeat borderRepeat, BorderRepeat borderRepeat2, Side side, Side side2, float f, float f2, float f3, float f4, int n, int n2, int n3, int n4, float f5, float f6, float f7, float f8) {
        BackgroundRepeat backgroundRepeat = null;
        BackgroundRepeat backgroundRepeat2 = null;
        switch (borderRepeat) {
            case REPEAT: {
                backgroundRepeat = BackgroundRepeat.REPEAT;
                break;
            }
            case STRETCH: {
                backgroundRepeat = BackgroundRepeat.NO_REPEAT;
                break;
            }
            case ROUND: {
                backgroundRepeat = BackgroundRepeat.ROUND;
                break;
            }
            case SPACE: {
                backgroundRepeat = BackgroundRepeat.SPACE;
            }
        }
        switch (borderRepeat2) {
            case REPEAT: {
                backgroundRepeat2 = BackgroundRepeat.REPEAT;
                break;
            }
            case STRETCH: {
                backgroundRepeat2 = BackgroundRepeat.NO_REPEAT;
                break;
            }
            case ROUND: {
                backgroundRepeat2 = BackgroundRepeat.ROUND;
                break;
            }
            case SPACE: {
                backgroundRepeat2 = BackgroundRepeat.SPACE;
            }
        }
        this.paintTiles(graphics, image, backgroundRepeat, backgroundRepeat2, side, side2, f, f2, f3, f4, n, n2, n3, n4, f5, f6, f7, f8);
    }

    private void paintTiles(Graphics graphics, Image image, BackgroundRepeat backgroundRepeat, BackgroundRepeat backgroundRepeat2, Side side, Side side2, float f, float f2, float f3, float f4, int n, int n2, int n3, int n4, float f5, float f6, float f7, float f8) {
        if (f3 <= 0.0f || f4 <= 0.0f || n3 <= 0 || n4 <= 0) {
            return;
        }
        assert (n >= 0 && n2 >= 0 && n3 > 0 && n4 > 0);
        if (f5 == 0.0f && f6 == 0.0f && backgroundRepeat == BackgroundRepeat.REPEAT && backgroundRepeat2 == BackgroundRepeat.REPEAT) {
            if (n != 0 || n2 != 0 || n3 != image.getWidth() || n4 != image.getHeight()) {
                image = image.createSubImage(n, n2, n3, n4);
            }
            graphics.setPaint(new com.sun.prism.paint.ImagePattern(image, 0.0f, 0.0f, f7, f8, false, false));
            graphics.fillRect(f, f2, f3, f4);
        } else {
            float f9;
            int n5;
            float f10;
            int n6;
            float f11;
            float f12;
            if (backgroundRepeat == BackgroundRepeat.SPACE && f3 < f7 * 2.0f) {
                backgroundRepeat = BackgroundRepeat.NO_REPEAT;
            }
            if (backgroundRepeat2 == BackgroundRepeat.SPACE && f4 < f8 * 2.0f) {
                backgroundRepeat2 = BackgroundRepeat.NO_REPEAT;
            }
            if (backgroundRepeat == BackgroundRepeat.REPEAT) {
                f12 = 0.0f;
                if (f5 != 0.0f) {
                    f11 = f5 % f7;
                    f12 = f5 = f11 == 0.0f ? 0.0f : (f5 < 0.0f ? f11 : f11 - f7);
                }
                n6 = (int)Math.max(1.0, Math.ceil((f3 - f12) / f7));
                f10 = side == Side.RIGHT ? -f7 : f7;
            } else if (backgroundRepeat == BackgroundRepeat.SPACE) {
                f5 = 0.0f;
                n6 = (int)(f3 / f7);
                f12 = f3 % f7;
                f10 = f7 + f12 / (float)(n6 - 1);
            } else if (backgroundRepeat == BackgroundRepeat.ROUND) {
                f5 = 0.0f;
                n6 = (int)(f3 / f7);
                f10 = f7 = f3 / (float)((int)(f3 / f7));
            } else {
                n6 = 1;
                float f13 = f10 = side == Side.RIGHT ? -f7 : f7;
            }
            if (backgroundRepeat2 == BackgroundRepeat.REPEAT) {
                f12 = 0.0f;
                if (f6 != 0.0f) {
                    f11 = f6 % f8;
                    f12 = f6 = f11 == 0.0f ? 0.0f : (f6 < 0.0f ? f11 : f11 - f8);
                }
                n5 = (int)Math.max(1.0, Math.ceil((f4 - f12) / f8));
                f9 = side2 == Side.BOTTOM ? -f8 : f8;
            } else if (backgroundRepeat2 == BackgroundRepeat.SPACE) {
                f6 = 0.0f;
                n5 = (int)(f4 / f8);
                f12 = f4 % f8;
                f9 = f8 + f12 / (float)(n5 - 1);
            } else if (backgroundRepeat2 == BackgroundRepeat.ROUND) {
                f6 = 0.0f;
                n5 = (int)(f4 / f8);
                f9 = f8 = f4 / (float)((int)(f4 / f8));
            } else {
                n5 = 1;
                f9 = side2 == Side.BOTTOM ? -f8 : f8;
            }
            Texture texture = graphics.getResourceFactory().getCachedTexture(image, Texture.WrapMode.CLAMP_TO_EDGE);
            int n7 = n + n3;
            int n8 = n2 + n4;
            float f14 = f + f3;
            float f15 = f2 + f4;
            float f16 = f2 + f6;
            for (int i = 0; i < n5; ++i) {
                float f17 = f16 + f8;
                float f18 = f + f5;
                for (int j = 0; j < n6; ++j) {
                    float f19;
                    float f20;
                    float f21 = f18 + f7;
                    boolean bl = false;
                    float f22 = f18 < f ? f : f18;
                    float f23 = f20 = f16 < f2 ? f2 : f16;
                    if (f22 > f14 || f20 > f15) {
                        bl = true;
                    }
                    float f24 = f21 > f14 ? f14 : f21;
                    float f25 = f19 = f17 > f15 ? f15 : f17;
                    if (f24 < f || f19 < f2) {
                        bl = true;
                    }
                    if (!bl) {
                        float f26 = f18 < f ? (float)n + (float)n3 * (-f5 / f7) : (float)n;
                        float f27 = f16 < f2 ? (float)n2 + (float)n4 * (-f6 / f8) : (float)n2;
                        float f28 = f21 > f14 ? (float)n7 - (float)n3 * ((f21 - f14) / f7) : (float)n7;
                        float f29 = f17 > f15 ? (float)n8 - (float)n4 * ((f17 - f15) / f8) : (float)n8;
                        graphics.drawTexture(texture, f22, f20, f24, f19, f26, f27, f28, f29);
                    }
                    f18 += f10;
                }
                f16 += f9;
            }
            texture.unlock();
        }
    }

    final Border getBorder() {
        return this.border;
    }

    final Background getBackground() {
        return this.background;
    }

    final float getWidth() {
        return this.width;
    }

    final float getHeight() {
        return this.height;
    }
}

