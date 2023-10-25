/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.pisces.PiscesRenderer;
import com.sun.pisces.Transform6;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.Paint;
import com.sun.prism.sw.SWArgbPreTexture;
import com.sun.prism.sw.SWContext;
import com.sun.prism.sw.SWPaint;
import com.sun.prism.sw.SWRTTexture;
import com.sun.prism.sw.SWResourceFactory;
import com.sun.prism.sw.SWUtils;

final class SWGraphics
implements ReadbackGraphics {
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1.0f, 2, 0, 10.0f);
    private static final Paint DEFAULT_PAINT = Color.WHITE;
    private final PiscesRenderer pr;
    private final SWContext context;
    private final SWRTTexture target;
    private final SWPaint swPaint;
    private final BaseTransform tx = new Affine2D();
    private CompositeMode compositeMode = CompositeMode.SRC_OVER;
    private Rectangle clip;
    private final Rectangle finalClip = new Rectangle();
    private RectBounds nodeBounds;
    private int clipRectIndex;
    private Paint paint = DEFAULT_PAINT;
    private BasicStroke stroke = DEFAULT_STROKE;
    private Ellipse2D ellipse2d;
    private Line2D line2d;
    private RoundRectangle2D rect2d;
    private boolean antialiasedShape = true;
    private boolean hasPreCullingBits = false;
    private float pixelScaleX = 1.0f;
    private float pixelScaleY = 1.0f;
    private NodePath renderRoot;

    @Override
    public void setRenderRoot(NodePath nodePath) {
        this.renderRoot = nodePath;
    }

    @Override
    public NodePath getRenderRoot() {
        return this.renderRoot;
    }

    public SWGraphics(SWRTTexture sWRTTexture, SWContext sWContext, PiscesRenderer piscesRenderer) {
        this.target = sWRTTexture;
        this.context = sWContext;
        this.pr = piscesRenderer;
        this.swPaint = new SWPaint(sWContext, piscesRenderer);
        this.setClipRect(null);
    }

    @Override
    public RenderTarget getRenderTarget() {
        return this.target;
    }

    @Override
    public SWResourceFactory getResourceFactory() {
        return this.target.getResourceFactory();
    }

    @Override
    public Screen getAssociatedScreen() {
        return this.target.getAssociatedScreen();
    }

    @Override
    public void sync() {
    }

    @Override
    public BaseTransform getTransformNoClone() {
        if (PrismSettings.debug) {
            System.out.println("+ getTransformNoClone " + this + "; tr: " + this.tx);
        }
        return this.tx;
    }

    @Override
    public void setTransform(BaseTransform baseTransform) {
        if (baseTransform == null) {
            baseTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        if (PrismSettings.debug) {
            System.out.println("+ setTransform " + this + "; tr: " + baseTransform);
        }
        this.tx.setTransform(baseTransform);
    }

    @Override
    public void setTransform(double d, double d2, double d3, double d4, double d5, double d6) {
        this.tx.restoreTransform(d, d2, d3, d4, d5, d6);
        if (PrismSettings.debug) {
            System.out.println("+ restoreTransform " + this + "; tr: " + this.tx);
        }
    }

    @Override
    public void setTransform3D(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        if (d3 != 0.0 || d7 != 0.0 || d9 != 0.0 || d10 != 0.0 || d11 != 1.0 || d12 != 0.0) {
            throw new UnsupportedOperationException("3D transforms not supported.");
        }
        this.setTransform(d, d5, d2, d6, d4, d8);
    }

    @Override
    public void transform(BaseTransform baseTransform) {
        if (PrismSettings.debug) {
            System.out.println("+ concatTransform " + this + "; tr: " + baseTransform);
        }
        this.tx.deriveWithConcatenation(baseTransform);
    }

    @Override
    public void translate(float f, float f2) {
        if (PrismSettings.debug) {
            System.out.println("+ concat translate " + this + "; tx: " + f + "; ty: " + f2);
        }
        this.tx.deriveWithTranslation(f, f2);
    }

    @Override
    public void translate(float f, float f2, float f3) {
        throw new UnsupportedOperationException("translate3D: unimp");
    }

    @Override
    public void scale(float f, float f2) {
        if (PrismSettings.debug) {
            System.out.println("+ concat scale " + this + "; sx: " + f + "; sy: " + f2);
        }
        this.tx.deriveWithConcatenation(f, 0.0, 0.0, f2, 0.0, 0.0);
    }

    @Override
    public void scale(float f, float f2, float f3) {
        throw new UnsupportedOperationException("scale3D: unimp");
    }

    @Override
    public void setCamera(NGCamera nGCamera) {
    }

    @Override
    public void setPerspectiveTransform(GeneralTransform3D generalTransform3D) {
    }

    @Override
    public NGCamera getCameraNoClone() {
        throw new UnsupportedOperationException("getCameraNoClone: unimp");
    }

    @Override
    public void setDepthTest(boolean bl) {
    }

    @Override
    public boolean isDepthTest() {
        return false;
    }

    @Override
    public void setDepthBuffer(boolean bl) {
    }

    @Override
    public boolean isDepthBuffer() {
        return false;
    }

    @Override
    public boolean isAlphaTestShader() {
        if (PrismSettings.verbose && PrismSettings.forceAlphaTestShader) {
            System.out.println("SW pipe doesn't support shader with alpha testing");
        }
        return false;
    }

    @Override
    public void setAntialiasedShape(boolean bl) {
        this.antialiasedShape = bl;
    }

    @Override
    public boolean isAntialiasedShape() {
        return this.antialiasedShape;
    }

    @Override
    public Rectangle getClipRect() {
        return this.clip == null ? null : new Rectangle(this.clip);
    }

    @Override
    public Rectangle getClipRectNoClone() {
        return this.clip;
    }

    @Override
    public RectBounds getFinalClipNoClone() {
        return this.finalClip.toRectBounds();
    }

    @Override
    public void setClipRect(Rectangle rectangle) {
        this.finalClip.setBounds(this.target.getDimensions());
        if (rectangle == null) {
            if (PrismSettings.debug) {
                System.out.println("+ PR.resetClip");
            }
            this.clip = null;
        } else {
            if (PrismSettings.debug) {
                System.out.println("+ PR.setClip: " + rectangle);
            }
            this.finalClip.intersectWith(rectangle);
            this.clip = new Rectangle(rectangle);
        }
        this.pr.setClip(this.finalClip.x, this.finalClip.y, this.finalClip.width, this.finalClip.height);
    }

    @Override
    public void setHasPreCullingBits(boolean bl) {
        this.hasPreCullingBits = bl;
    }

    @Override
    public boolean hasPreCullingBits() {
        return this.hasPreCullingBits;
    }

    @Override
    public int getClipRectIndex() {
        return this.clipRectIndex;
    }

    @Override
    public void setClipRectIndex(int n) {
        if (PrismSettings.debug) {
            System.out.println("+ PR.setClipRectIndex: " + n);
        }
        this.clipRectIndex = n;
    }

    @Override
    public float getExtraAlpha() {
        return this.swPaint.getCompositeAlpha();
    }

    @Override
    public void setExtraAlpha(float f) {
        if (PrismSettings.debug) {
            System.out.println("PR.setCompositeAlpha, value: " + f);
        }
        this.swPaint.setCompositeAlpha(f);
    }

    @Override
    public Paint getPaint() {
        return this.paint;
    }

    @Override
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    public BasicStroke getStroke() {
        return this.stroke;
    }

    @Override
    public void setStroke(BasicStroke basicStroke) {
        this.stroke = basicStroke;
    }

    @Override
    public CompositeMode getCompositeMode() {
        return this.compositeMode;
    }

    @Override
    public void setCompositeMode(CompositeMode compositeMode) {
        int n;
        this.compositeMode = compositeMode;
        switch (compositeMode) {
            case CLEAR: {
                n = 0;
                if (!PrismSettings.debug) break;
                System.out.println("PR.setCompositeRule - CLEAR");
                break;
            }
            case SRC: {
                n = 1;
                if (!PrismSettings.debug) break;
                System.out.println("PR.setCompositeRule - SRC");
                break;
            }
            case SRC_OVER: {
                n = 2;
                if (!PrismSettings.debug) break;
                System.out.println("PR.setCompositeRule - SRC_OVER");
                break;
            }
            default: {
                throw new InternalError("Unrecognized composite mode: " + compositeMode);
            }
        }
        this.pr.setCompositeRule(n);
    }

    @Override
    public void setNodeBounds(RectBounds rectBounds) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.setNodeBounds: " + rectBounds);
        }
        this.nodeBounds = rectBounds;
    }

    @Override
    public void clear() {
        this.clear(Color.TRANSPARENT);
    }

    @Override
    public void clear(Color color) {
        if (PrismSettings.debug) {
            System.out.println("+ PR.clear: " + color);
        }
        this.swPaint.setColor(color, 1.0f);
        this.pr.clearRect(0, 0, this.target.getPhysicalWidth(), this.target.getPhysicalHeight());
        this.getRenderTarget().setOpaque(color.isOpaque());
    }

    @Override
    public void clearQuad(float f, float f2, float f3, float f4) {
        CompositeMode compositeMode = this.compositeMode;
        Paint paint = this.paint;
        this.setCompositeMode(CompositeMode.SRC);
        this.setPaint(Color.TRANSPARENT);
        this.fillQuad(f, f2, f3, f4);
        this.setCompositeMode(compositeMode);
        this.setPaint(paint);
    }

    @Override
    public void fill(Shape shape) {
        if (PrismSettings.debug) {
            System.out.println("+ fill(Shape)");
        }
        this.paintShape(shape, null, this.tx);
    }

    @Override
    public void fillQuad(float f, float f2, float f3, float f4) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.fillQuad");
        }
        this.fillRect(Math.min(f, f3), Math.min(f2, f4), Math.abs(f3 - f), Math.abs(f4 - f2));
    }

    @Override
    public void fillRect(float f, float f2, float f3, float f4) {
        if (PrismSettings.debug) {
            System.out.printf("+ SWG.fillRect, x: %f, y: %f, w: %f, h: %f\n", Float.valueOf(f), Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4));
        }
        if (this.tx.getMxy() == 0.0 && this.tx.getMyx() == 0.0) {
            if (PrismSettings.debug) {
                System.out.println("GR: " + this);
                System.out.println("target: " + this.target + " t.w: " + this.target.getPhysicalWidth() + ", t.h: " + this.target.getPhysicalHeight() + ", t.dims: " + this.target.getDimensions());
                System.out.println("Tx: " + this.tx);
                System.out.println("Clip: " + this.finalClip);
                System.out.println("Composite rule: " + this.compositeMode);
            }
            Point2D point2D = new Point2D(f, f2);
            Point2D point2D2 = new Point2D(f + f3, f2 + f4);
            this.tx.transform(point2D, point2D);
            this.tx.transform(point2D2, point2D2);
            if (this.paint.getType() == Paint.Type.IMAGE_PATTERN) {
                int n;
                ImagePattern imagePattern = (ImagePattern)this.paint;
                if (imagePattern.getImage().getPixelFormat() == PixelFormat.BYTE_ALPHA) {
                    throw new UnsupportedOperationException("Alpha image is not supported as an image pattern.");
                }
                Transform6 transform6 = this.swPaint.computeSetTexturePaintTransform(this.paint, this.tx, this.nodeBounds, f, f2, f3, f4);
                SWArgbPreTexture sWArgbPreTexture = this.context.validateImagePaintTexture(imagePattern.getImage().getWidth(), imagePattern.getImage().getHeight());
                sWArgbPreTexture.update(imagePattern.getImage());
                float f5 = this.swPaint.getCompositeAlpha();
                if (f5 == 1.0f) {
                    n = 1;
                } else {
                    n = 2;
                    this.pr.setColor(255, 255, 255, (int)(255.0f * f5));
                }
                this.pr.drawImage(1, n, sWArgbPreTexture.getDataNoClone(), sWArgbPreTexture.getContentWidth(), sWArgbPreTexture.getContentHeight(), sWArgbPreTexture.getOffset(), sWArgbPreTexture.getPhysicalWidth(), transform6, sWArgbPreTexture.getWrapMode() == Texture.WrapMode.REPEAT, sWArgbPreTexture.getLinearFiltering(), (int)(Math.min(point2D.x, point2D2.x) * 65536.0f), (int)(Math.min(point2D.y, point2D2.y) * 65536.0f), (int)(Math.abs(point2D2.x - point2D.x) * 65536.0f), (int)(Math.abs(point2D2.y - point2D.y) * 65536.0f), 0, 0, 0, 0, 0, 0, sWArgbPreTexture.getContentWidth() - 1, sWArgbPreTexture.getContentHeight() - 1, sWArgbPreTexture.hasAlpha());
            } else {
                this.swPaint.setPaintFromShape(this.paint, this.tx, null, this.nodeBounds, f, f2, f3, f4);
                this.pr.fillRect((int)(Math.min(point2D.x, point2D2.x) * 65536.0f), (int)(Math.min(point2D.y, point2D2.y) * 65536.0f), (int)(Math.abs(point2D2.x - point2D.x) * 65536.0f), (int)(Math.abs(point2D2.y - point2D.y) * 65536.0f));
            }
        } else {
            this.fillRoundRect(f, f2, f3, f4, 0.0f, 0.0f);
        }
    }

    @Override
    public void fillRoundRect(float f, float f2, float f3, float f4, float f5, float f6) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.fillRoundRect");
        }
        this.paintRoundRect(f, f2, f3, f4, f5, f6, null);
    }

    @Override
    public void fillEllipse(float f, float f2, float f3, float f4) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.fillEllipse");
        }
        this.paintEllipse(f, f2, f3, f4, null);
    }

    @Override
    public void draw(Shape shape) {
        if (PrismSettings.debug) {
            System.out.println("+ draw(Shape)");
        }
        this.paintShape(shape, this.stroke, this.tx);
    }

    private void paintShape(Shape shape, BasicStroke basicStroke, BaseTransform baseTransform) {
        if (this.finalClip.isEmpty()) {
            if (PrismSettings.debug) {
                System.out.println("Final clip is empty: not rendering the shape: " + shape);
            }
            return;
        }
        this.swPaint.setPaintFromShape(this.paint, this.tx, shape, this.nodeBounds, 0.0f, 0.0f, 0.0f, 0.0f);
        this.paintShapePaintAlreadySet(shape, basicStroke, baseTransform);
    }

    private void paintShapePaintAlreadySet(Shape shape, BasicStroke basicStroke, BaseTransform baseTransform) {
        if (this.finalClip.isEmpty()) {
            if (PrismSettings.debug) {
                System.out.println("Final clip is empty: not rendering the shape: " + shape);
            }
            return;
        }
        if (PrismSettings.debug) {
            System.out.println("GR: " + this);
            System.out.println("target: " + this.target + " t.w: " + this.target.getPhysicalWidth() + ", t.h: " + this.target.getPhysicalHeight() + ", t.dims: " + this.target.getDimensions());
            System.out.println("Shape: " + shape);
            System.out.println("Stroke: " + basicStroke);
            System.out.println("Tx: " + baseTransform);
            System.out.println("Clip: " + this.finalClip);
            System.out.println("Composite rule: " + this.compositeMode);
        }
        this.context.renderShape(this.pr, shape, basicStroke, baseTransform, this.finalClip, this.isAntialiasedShape());
    }

    private void paintRoundRect(float f, float f2, float f3, float f4, float f5, float f6, BasicStroke basicStroke) {
        if (this.rect2d == null) {
            this.rect2d = new RoundRectangle2D(f, f2, f3, f4, f5, f6);
        } else {
            this.rect2d.setRoundRect(f, f2, f3, f4, f5, f6);
        }
        this.paintShape(this.rect2d, basicStroke, this.tx);
    }

    private void paintEllipse(float f, float f2, float f3, float f4, BasicStroke basicStroke) {
        if (this.ellipse2d == null) {
            this.ellipse2d = new Ellipse2D(f, f2, f3, f4);
        } else {
            this.ellipse2d.setFrame(f, f2, f3, f4);
        }
        this.paintShape(this.ellipse2d, basicStroke, this.tx);
    }

    @Override
    public void drawLine(float f, float f2, float f3, float f4) {
        if (PrismSettings.debug) {
            System.out.println("+ drawLine");
        }
        if (this.line2d == null) {
            this.line2d = new Line2D(f, f2, f3, f4);
        } else {
            this.line2d.setLine(f, f2, f3, f4);
        }
        this.paintShape(this.line2d, this.stroke, this.tx);
    }

    @Override
    public void drawRect(float f, float f2, float f3, float f4) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.drawRect");
        }
        this.drawRoundRect(f, f2, f3, f4, 0.0f, 0.0f);
    }

    @Override
    public void drawRoundRect(float f, float f2, float f3, float f4, float f5, float f6) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.drawRoundRect");
        }
        this.paintRoundRect(f, f2, f3, f4, f5, f6, this.stroke);
    }

    @Override
    public void drawEllipse(float f, float f2, float f3, float f4) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.drawEllipse");
        }
        this.paintEllipse(f, f2, f3, f4, this.stroke);
    }

    @Override
    public void drawString(GlyphList glyphList, FontStrike fontStrike, float f, float f2, Color color, int n, int n2) {
        float f3;
        float f4;
        float f5;
        float f6;
        if (PrismSettings.debug) {
            System.out.println("+ SWG.drawGlyphList, gl.Count: " + glyphList.getGlyphCount() + ", x: " + f + ", y: " + f2 + ", selectStart: " + n + ", selectEnd: " + n2);
        }
        if (this.paint.isProportional()) {
            if (this.nodeBounds != null) {
                f6 = this.nodeBounds.getMinX();
                f5 = this.nodeBounds.getMinY();
                f4 = this.nodeBounds.getWidth();
                f3 = this.nodeBounds.getHeight();
            } else {
                Metrics metrics = fontStrike.getMetrics();
                f6 = 0.0f;
                f5 = metrics.getAscent();
                f4 = glyphList.getWidth();
                f3 = metrics.getLineHeight();
            }
        } else {
            f3 = 0.0f;
            f4 = 0.0f;
            f5 = 0.0f;
            f6 = 0.0f;
        }
        boolean bl = this.tx.isTranslateOrIdentity() && !fontStrike.drawAsShapes();
        boolean bl2 = bl && fontStrike.getAAMode() == 1 && this.getRenderTarget().isOpaque() && this.paint.getType() == Paint.Type.COLOR && this.tx.is2D();
        Affine2D affine2D = null;
        if (bl2) {
            this.pr.setLCDGammaCorrection(1.0f / PrismFontFactory.getLCDContrast());
        } else if (bl) {
            FontResource fontResource = fontStrike.getFontResource();
            float f7 = fontStrike.getSize();
            BaseTransform baseTransform = fontStrike.getTransform();
            fontStrike = fontResource.getStrike(f7, baseTransform, 0);
        } else {
            affine2D = new Affine2D();
        }
        if (color == null) {
            this.swPaint.setPaintBeforeDraw(this.paint, this.tx, f6, f5, f4, f3);
            for (int i = 0; i < glyphList.getGlyphCount(); ++i) {
                this.drawGlyph(fontStrike, glyphList, i, affine2D, bl, f, f2);
            }
        } else {
            for (int i = 0; i < glyphList.getGlyphCount(); ++i) {
                int n3 = glyphList.getCharOffset(i);
                boolean bl3 = n <= n3 && n3 < n2;
                this.swPaint.setPaintBeforeDraw(bl3 ? color : this.paint, this.tx, f6, f5, f4, f3);
                this.drawGlyph(fontStrike, glyphList, i, affine2D, bl, f, f2);
            }
        }
    }

    private void drawGlyph(FontStrike fontStrike, GlyphList glyphList, int n, BaseTransform baseTransform, boolean bl, float f, float f2) {
        Glyph glyph = fontStrike.getGlyph(glyphList.getGlyphCode(n));
        if (glyph.getGlyphCode() == 65535) {
            return;
        }
        if (bl) {
            Point2D point2D = new Point2D((float)((double)f + this.tx.getMxt() + (double)glyphList.getPosX(n)), (float)((double)f2 + this.tx.getMyt() + (double)glyphList.getPosY(n)));
            int n2 = fontStrike.getQuantizedPosition(point2D);
            byte[] arrby = glyph.getPixelData(n2);
            if (arrby != null) {
                int n3 = glyph.getOriginX() + (int)point2D.x;
                int n4 = glyph.getOriginY() + (int)point2D.y;
                if (glyph.isLCDGlyph()) {
                    this.pr.fillLCDAlphaMask(arrby, n3, n4, glyph.getWidth(), glyph.getHeight(), 0, glyph.getWidth());
                } else {
                    this.pr.fillAlphaMask(arrby, n3, n4, glyph.getWidth(), glyph.getHeight(), 0, glyph.getWidth());
                }
            }
        } else {
            Shape shape = glyph.getShape();
            if (shape != null) {
                baseTransform.setTransform(this.tx);
                baseTransform.deriveWithTranslation(f + glyphList.getPosX(n), f2 + glyphList.getPosY(n));
                this.paintShapePaintAlreadySet(shape, null, baseTransform);
            }
        }
    }

    @Override
    public void drawTexture(Texture texture, float f, float f2, float f3, float f4) {
        if (PrismSettings.debug) {
            System.out.printf("+ drawTexture1, x: %f, y: %f, w: %f, h: %f\n", Float.valueOf(f), Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4));
        }
        this.drawTexture(texture, f, f2, f + f3, f2 + f4, 0.0f, 0.0f, f3, f4);
    }

    @Override
    public void drawTexture(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.drawTexture(texture, f, f2, f3, f4, f5, f6, f7, f8, 0, 0, 0, 0);
    }

    private void drawTexture(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int n, int n2, int n3, int n4) {
        int n5;
        float f9 = this.swPaint.getCompositeAlpha();
        if (f9 == 1.0f) {
            n5 = 1;
        } else {
            n5 = 2;
            this.pr.setColor(255, 255, 255, (int)(255.0f * f9));
        }
        this.drawTexture(texture, n5, f, f2, f3, f4, f5, f6, f7, f8, n, n2, n3, n4);
    }

    private void drawTexture(Texture texture, int n, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int n2, int n3, int n4, int n5) {
        if (PrismSettings.debug) {
            System.out.println("+ drawTexture: " + texture + ", imageMode: " + n + ", tex.w: " + texture.getPhysicalWidth() + ", tex.h: " + texture.getPhysicalHeight() + ", tex.cw: " + texture.getContentWidth() + ", tex.ch: " + texture.getContentHeight());
            System.out.println("target: " + this.target + " t.w: " + this.target.getPhysicalWidth() + ", t.h: " + this.target.getPhysicalHeight() + ", t.dims: " + this.target.getDimensions());
            System.out.println("GR: " + this);
            System.out.println("dx1:" + f + " dy1:" + f2 + " dx2:" + f3 + " dy2:" + f4);
            System.out.println("sx1:" + f5 + " sy1:" + f6 + " sx2:" + f7 + " sy2:" + f8);
            System.out.println("Clip: " + this.finalClip);
            System.out.println("Composite rule: " + this.compositeMode);
        }
        SWArgbPreTexture sWArgbPreTexture = (SWArgbPreTexture)texture;
        int[] arrn = sWArgbPreTexture.getDataNoClone();
        RectBounds rectBounds = new RectBounds(Math.min(f, f3), Math.min(f2, f4), Math.max(f, f3), Math.max(f2, f4));
        RectBounds rectBounds2 = new RectBounds();
        this.tx.transform(rectBounds, rectBounds2);
        Transform6 transform6 = this.swPaint.computeDrawTexturePaintTransform(this.tx, f, f2, f3, f4, f5, f6, f7, f8);
        if (PrismSettings.debug) {
            System.out.println("tx: " + this.tx);
            System.out.println("piscesTx: " + transform6);
            System.out.println("srcBBox: " + rectBounds);
            System.out.println("dstBBox: " + rectBounds2);
        }
        int n6 = Math.max(0, SWUtils.fastFloor(Math.min(f5, f7)));
        int n7 = Math.max(0, SWUtils.fastFloor(Math.min(f6, f8)));
        int n8 = Math.min(texture.getContentWidth() - 1, SWUtils.fastCeil(Math.max(f5, f7)) - 1);
        int n9 = Math.min(texture.getContentHeight() - 1, SWUtils.fastCeil(Math.max(f6, f8)) - 1);
        this.pr.drawImage(1, n, arrn, texture.getContentWidth(), texture.getContentHeight(), sWArgbPreTexture.getOffset(), texture.getPhysicalWidth(), transform6, texture.getWrapMode() == Texture.WrapMode.REPEAT, texture.getLinearFiltering(), (int)(65536.0f * rectBounds2.getMinX()), (int)(65536.0f * rectBounds2.getMinY()), (int)(65536.0f * rectBounds2.getWidth()), (int)(65536.0f * rectBounds2.getHeight()), n2, n3, n4, n5, n6, n7, n8, n9, sWArgbPreTexture.hasAlpha());
        if (PrismSettings.debug) {
            System.out.println("* drawTexture, DONE");
        }
    }

    @Override
    public void drawTexture3SliceH(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        this.drawTexture(texture, f, f2, f9, f4, f5, f6, f11, f8, 0, 1, 0, 0);
        this.drawTexture(texture, f9, f2, f10, f4, f11, f6, f12, f8, 2, 1, 0, 0);
        this.drawTexture(texture, f10, f2, f3, f4, f12, f6, f7, f8, 2, 0, 0, 0);
    }

    @Override
    public void drawTexture3SliceV(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        this.drawTexture(texture, f, f2, f3, f9, f5, f6, f7, f11, 0, 0, 0, 1);
        this.drawTexture(texture, f, f9, f3, f10, f5, f11, f7, f12, 0, 0, 2, 1);
        this.drawTexture(texture, f, f10, f3, f4, f5, f12, f7, f8, 0, 0, 2, 0);
    }

    @Override
    public void drawTexture9Slice(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        this.drawTexture(texture, f, f2, f9, f10, f5, f6, f13, f14, 0, 1, 0, 1);
        this.drawTexture(texture, f9, f2, f11, f10, f13, f6, f15, f14, 2, 1, 0, 1);
        this.drawTexture(texture, f11, f2, f3, f10, f15, f6, f7, f14, 2, 0, 0, 1);
        this.drawTexture(texture, f, f10, f9, f12, f5, f14, f13, f16, 0, 1, 2, 1);
        this.drawTexture(texture, f9, f10, f11, f12, f13, f14, f15, f16, 2, 1, 2, 1);
        this.drawTexture(texture, f11, f10, f3, f12, f15, f14, f7, f16, 2, 0, 2, 1);
        this.drawTexture(texture, f, f12, f9, f4, f5, f16, f13, f8, 0, 1, 2, 0);
        this.drawTexture(texture, f9, f12, f11, f4, f13, f16, f15, f8, 2, 1, 2, 0);
        this.drawTexture(texture, f11, f12, f3, f4, f15, f16, f7, f8, 2, 0, 2, 0);
    }

    @Override
    public void drawTextureVO(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        if (PrismSettings.debug) {
            System.out.println("* drawTextureVO");
        }
        int[] arrn = new int[]{0, 65536};
        int[] arrn2 = new int[]{0xFFFFFF | (int)(f * 255.0f) << 24, 0xFFFFFF | (int)(f2 * 255.0f) << 24};
        Transform6 transform6 = new Transform6();
        SWUtils.convertToPiscesTransform(this.tx, transform6);
        this.pr.setLinearGradient(0, (int)(65536.0f * f4), 0, (int)(65536.0f * f6), arrn, arrn2, 0, transform6);
        this.drawTexture(texture, 2, f3, f4, f5, f6, f7, f8, f9, f10, 0, 0, 0, 0);
    }

    @Override
    public void drawTextureRaw(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        if (PrismSettings.debug) {
            System.out.println("+ drawTextureRaw");
        }
        int n = texture.getContentWidth();
        int n2 = texture.getContentHeight();
        this.drawTexture(texture, f, f2, f3, f4, f5 *= (float)n, f6 *= (float)n2, f7 *= (float)n, f8 *= (float)n2);
    }

    @Override
    public void drawMappedTextureRaw(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        if (PrismSettings.debug) {
            System.out.println("+ drawMappedTextureRaw");
        }
        double d = this.tx.getMxx();
        double d2 = this.tx.getMyx();
        double d3 = this.tx.getMxy();
        double d4 = this.tx.getMyy();
        double d5 = this.tx.getMxt();
        double d6 = this.tx.getMyt();
        try {
            float f13 = f7 - f5;
            float f14 = f8 - f6;
            float f15 = f9 - f5;
            float f16 = f10 - f6;
            Affine2D affine2D = new Affine2D(f13, f14, f15, f16, f5, f6);
            ((BaseTransform)affine2D).invert();
            this.tx.setToIdentity();
            this.tx.deriveWithTranslation(f, f2);
            this.tx.deriveWithConcatenation(f3 - f, 0.0, 0.0, f4 - f4, 0.0, 0.0);
            this.tx.deriveWithConcatenation(affine2D);
            this.drawTexture(texture, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, texture.getContentWidth(), texture.getContentHeight());
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            // empty catch block
        }
        this.tx.restoreTransform(d, d2, d3, d4, d5, d6);
    }

    @Override
    public boolean canReadBack() {
        return true;
    }

    @Override
    public RTTexture readBack(Rectangle rectangle) {
        if (PrismSettings.debug) {
            System.out.println("+ readBack, rect: " + rectangle + ", target.dims: " + this.target.getDimensions());
        }
        int n = Math.max(1, rectangle.width);
        int n2 = Math.max(1, rectangle.height);
        SWRTTexture sWRTTexture = this.context.validateRBBuffer(n, n2);
        if (rectangle.isEmpty()) {
            return sWRTTexture;
        }
        int[] arrn = sWRTTexture.getDataNoClone();
        this.target.getSurface().getRGB(arrn, 0, sWRTTexture.getPhysicalWidth(), rectangle.x, rectangle.y, n, n2);
        return sWRTTexture;
    }

    @Override
    public void releaseReadBackBuffer(RTTexture rTTexture) {
    }

    @Override
    public void setState3D(boolean bl) {
    }

    @Override
    public boolean isState3D() {
        return false;
    }

    @Override
    public void setup3DRendering() {
    }

    @Override
    public void setPixelScaleFactors(float f, float f2) {
        this.pixelScaleX = f;
    }

    @Override
    public float getPixelScaleFactorX() {
        return this.pixelScaleX;
    }

    @Override
    public float getPixelScaleFactorY() {
        return this.pixelScaleY;
    }

    @Override
    public void setLights(NGLightBase[] arrnGLightBase) {
    }

    @Override
    public NGLightBase[] getLights() {
        return null;
    }

    @Override
    public void blit(RTTexture rTTexture, RTTexture rTTexture2, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        Graphics graphics = rTTexture2.createGraphics();
        graphics.drawTexture(rTTexture, n5, n6, n7, n8, n, n2, n3, n4);
    }
}

