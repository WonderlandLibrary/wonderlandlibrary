/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.PixelFormat;
import com.sun.prism.RectShadowGraphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseContext;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

public abstract class BaseGraphics
implements RectShadowGraphics {
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1.0f, 2, 0, 10.0f);
    private static final Paint DEFAULT_PAINT = Color.WHITE;
    protected static final RoundRectangle2D scratchRRect = new RoundRectangle2D();
    protected static final Ellipse2D scratchEllipse = new Ellipse2D();
    protected static final Line2D scratchLine = new Line2D();
    protected static final BaseTransform IDENT = BaseTransform.IDENTITY_TRANSFORM;
    private final Affine3D transform3D = new Affine3D();
    private NGCamera camera = NGCamera.INSTANCE;
    private RectBounds devClipRect;
    private RectBounds finalClipRect;
    protected RectBounds nodeBounds = null;
    private Rectangle clipRect;
    private int clipRectIndex;
    private boolean hasPreCullingBits = false;
    private float extraAlpha = 1.0f;
    private CompositeMode compMode;
    private boolean antialiasedShape = true;
    private boolean depthBuffer = false;
    private boolean depthTest = false;
    protected Paint paint = DEFAULT_PAINT;
    protected BasicStroke stroke = DEFAULT_STROKE;
    protected boolean isSimpleTranslate = true;
    protected float transX;
    protected float transY;
    private final BaseContext context;
    private final RenderTarget renderTarget;
    private boolean state3D = false;
    private float pixelScaleX = 1.0f;
    private float pixelScaleY = 1.0f;
    private NodePath renderRoot;

    protected BaseGraphics(BaseContext baseContext, RenderTarget renderTarget) {
        this.context = baseContext;
        this.renderTarget = renderTarget;
        this.devClipRect = new RectBounds(0.0f, 0.0f, renderTarget.getContentWidth(), renderTarget.getContentHeight());
        this.finalClipRect = new RectBounds(this.devClipRect);
        this.compMode = CompositeMode.SRC_OVER;
        if (baseContext != null) {
            baseContext.setRenderTarget(this);
        }
    }

    protected NGCamera getCamera() {
        return this.camera;
    }

    @Override
    public RenderTarget getRenderTarget() {
        return this.renderTarget;
    }

    @Override
    public void setState3D(boolean bl) {
        this.state3D = bl;
    }

    @Override
    public boolean isState3D() {
        return this.state3D;
    }

    @Override
    public Screen getAssociatedScreen() {
        return this.context.getAssociatedScreen();
    }

    @Override
    public ResourceFactory getResourceFactory() {
        return this.context.getResourceFactory();
    }

    @Override
    public BaseTransform getTransformNoClone() {
        return this.transform3D;
    }

    @Override
    public void setPerspectiveTransform(GeneralTransform3D generalTransform3D) {
        this.context.setPerspectiveTransform(generalTransform3D);
    }

    @Override
    public void setTransform(BaseTransform baseTransform) {
        if (baseTransform == null) {
            this.transform3D.setToIdentity();
        } else {
            this.transform3D.setTransform(baseTransform);
        }
        this.validateTransformAndPaint();
    }

    @Override
    public void setTransform(double d, double d2, double d3, double d4, double d5, double d6) {
        this.transform3D.setTransform(d, d2, d3, d4, d5, d6);
        this.validateTransformAndPaint();
    }

    @Override
    public void setTransform3D(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        this.transform3D.setTransform(d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
        this.validateTransformAndPaint();
    }

    @Override
    public void transform(BaseTransform baseTransform) {
        this.transform3D.concatenate(baseTransform);
        this.validateTransformAndPaint();
    }

    @Override
    public void translate(float f, float f2) {
        if (f != 0.0f || f2 != 0.0f) {
            this.transform3D.translate(f, f2);
            this.validateTransformAndPaint();
        }
    }

    @Override
    public void translate(float f, float f2, float f3) {
        if (f != 0.0f || f2 != 0.0f || f3 != 0.0f) {
            this.transform3D.translate(f, f2, f3);
            this.validateTransformAndPaint();
        }
    }

    @Override
    public void scale(float f, float f2) {
        if (f != 1.0f || f2 != 1.0f) {
            this.transform3D.scale(f, f2);
            this.validateTransformAndPaint();
        }
    }

    @Override
    public void scale(float f, float f2, float f3) {
        if (f != 1.0f || f2 != 1.0f || f3 != 1.0f) {
            this.transform3D.scale(f, f2, f3);
            this.validateTransformAndPaint();
        }
    }

    @Override
    public void setClipRectIndex(int n) {
        this.clipRectIndex = n;
    }

    @Override
    public int getClipRectIndex() {
        return this.clipRectIndex;
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
    public final void setRenderRoot(NodePath nodePath) {
        this.renderRoot = nodePath;
    }

    @Override
    public final NodePath getRenderRoot() {
        return this.renderRoot;
    }

    private void validateTransformAndPaint() {
        if (this.transform3D.isTranslateOrIdentity() && this.paint.getType() == Paint.Type.COLOR) {
            this.isSimpleTranslate = true;
            this.transX = (float)this.transform3D.getMxt();
            this.transY = (float)this.transform3D.getMyt();
        } else {
            this.isSimpleTranslate = false;
            this.transX = 0.0f;
            this.transY = 0.0f;
        }
    }

    @Override
    public NGCamera getCameraNoClone() {
        return this.camera;
    }

    @Override
    public void setDepthTest(boolean bl) {
        this.depthTest = bl;
    }

    @Override
    public boolean isDepthTest() {
        return this.depthTest;
    }

    @Override
    public void setDepthBuffer(boolean bl) {
        this.depthBuffer = bl;
    }

    @Override
    public boolean isDepthBuffer() {
        return this.depthBuffer;
    }

    @Override
    public boolean isAlphaTestShader() {
        return PrismSettings.forceAlphaTestShader || this.isDepthTest() && this.isDepthBuffer();
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
    public void setPixelScaleFactors(float f, float f2) {
        this.pixelScaleX = f;
        this.pixelScaleY = f2;
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
    public void setCamera(NGCamera nGCamera) {
        this.camera = nGCamera;
    }

    @Override
    public Rectangle getClipRect() {
        return this.clipRect != null ? new Rectangle(this.clipRect) : null;
    }

    @Override
    public Rectangle getClipRectNoClone() {
        return this.clipRect;
    }

    @Override
    public RectBounds getFinalClipNoClone() {
        return this.finalClipRect;
    }

    @Override
    public void setClipRect(Rectangle rectangle) {
        this.finalClipRect.setBounds(this.devClipRect);
        if (rectangle == null) {
            this.clipRect = null;
        } else {
            this.clipRect = new Rectangle(rectangle);
            this.finalClipRect.intersectWith(rectangle);
        }
    }

    @Override
    public float getExtraAlpha() {
        return this.extraAlpha;
    }

    @Override
    public void setExtraAlpha(float f) {
        this.extraAlpha = f;
    }

    @Override
    public CompositeMode getCompositeMode() {
        return this.compMode;
    }

    @Override
    public void setCompositeMode(CompositeMode compositeMode) {
        this.compMode = compositeMode;
    }

    @Override
    public Paint getPaint() {
        return this.paint;
    }

    @Override
    public void setPaint(Paint paint) {
        this.paint = paint;
        this.validateTransformAndPaint();
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
    public void clear() {
        this.clear(Color.TRANSPARENT);
    }

    protected abstract void renderShape(Shape var1, BasicStroke var2, float var3, float var4, float var5, float var6);

    @Override
    public void fill(Shape shape) {
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        if (this.paint.isProportional()) {
            if (this.nodeBounds != null) {
                f = this.nodeBounds.getMinX();
                f2 = this.nodeBounds.getMinY();
                f3 = this.nodeBounds.getWidth();
                f4 = this.nodeBounds.getHeight();
            } else {
                float[] arrf = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
                Shape.accumulate(arrf, shape, BaseTransform.IDENTITY_TRANSFORM);
                f = arrf[0];
                f2 = arrf[1];
                f3 = arrf[2] - f;
                f4 = arrf[3] - f2;
            }
        }
        this.renderShape(shape, null, f, f2, f3, f4);
    }

    @Override
    public void draw(Shape shape) {
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        if (this.paint.isProportional()) {
            if (this.nodeBounds != null) {
                f = this.nodeBounds.getMinX();
                f2 = this.nodeBounds.getMinY();
                f3 = this.nodeBounds.getWidth();
                f4 = this.nodeBounds.getHeight();
            } else {
                float[] arrf = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
                Shape.accumulate(arrf, shape, BaseTransform.IDENTITY_TRANSFORM);
                f = arrf[0];
                f2 = arrf[1];
                f3 = arrf[2] - f;
                f4 = arrf[3] - f2;
            }
        }
        this.renderShape(shape, this.stroke, f, f2, f3, f4);
    }

    @Override
    public void drawTexture(Texture texture, float f, float f2, float f3, float f4) {
        this.drawTexture(texture, f, f2, f + f3, f2 + f4, 0.0f, 0.0f, f3, f4);
    }

    @Override
    public void drawTexture(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        BaseTransform baseTransform = this.isSimpleTranslate ? IDENT : this.getTransformNoClone();
        PixelFormat pixelFormat = texture.getPixelFormat();
        if (pixelFormat == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, baseTransform, texture, f, f2, f3 - f, f4 - f2);
        } else {
            this.context.validateTextureOp(this, baseTransform, texture, pixelFormat);
        }
        if (this.isSimpleTranslate) {
            f += this.transX;
            f2 += this.transY;
            f3 += this.transX;
            f4 += this.transY;
        }
        float f9 = texture.getPhysicalWidth();
        float f10 = texture.getPhysicalHeight();
        float f11 = texture.getContentX();
        float f12 = texture.getContentY();
        float f13 = (f11 + f5) / f9;
        float f14 = (f12 + f6) / f10;
        float f15 = (f11 + f7) / f9;
        float f16 = (f12 + f8) / f10;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        if (this.context.isSuperShaderEnabled()) {
            vertexBuffer.addSuperQuad(f, f2, f3, f4, f13, f14, f15, f16, false);
        } else {
            vertexBuffer.addQuad(f, f2, f3, f4, f13, f14, f15, f16);
        }
    }

    @Override
    public void drawTexture3SliceH(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        BaseTransform baseTransform = this.isSimpleTranslate ? IDENT : this.getTransformNoClone();
        PixelFormat pixelFormat = texture.getPixelFormat();
        if (pixelFormat == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, baseTransform, texture, f, f2, f3 - f, f4 - f2);
        } else {
            this.context.validateTextureOp(this, baseTransform, texture, pixelFormat);
        }
        if (this.isSimpleTranslate) {
            f += this.transX;
            f2 += this.transY;
            f3 += this.transX;
            f4 += this.transY;
            f9 += this.transX;
            f10 += this.transX;
        }
        float f13 = texture.getPhysicalWidth();
        float f14 = texture.getPhysicalHeight();
        float f15 = texture.getContentX();
        float f16 = texture.getContentY();
        float f17 = (f15 + f5) / f13;
        float f18 = (f16 + f6) / f14;
        float f19 = (f15 + f7) / f13;
        float f20 = (f16 + f8) / f14;
        float f21 = (f15 + f11) / f13;
        float f22 = (f15 + f12) / f13;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        if (this.context.isSuperShaderEnabled()) {
            vertexBuffer.addSuperQuad(f, f2, f9, f4, f17, f18, f21, f20, false);
            vertexBuffer.addSuperQuad(f9, f2, f10, f4, f21, f18, f22, f20, false);
            vertexBuffer.addSuperQuad(f10, f2, f3, f4, f22, f18, f19, f20, false);
        } else {
            vertexBuffer.addQuad(f, f2, f9, f4, f17, f18, f21, f20);
            vertexBuffer.addQuad(f9, f2, f10, f4, f21, f18, f22, f20);
            vertexBuffer.addQuad(f10, f2, f3, f4, f22, f18, f19, f20);
        }
    }

    @Override
    public void drawTexture3SliceV(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        BaseTransform baseTransform = this.isSimpleTranslate ? IDENT : this.getTransformNoClone();
        PixelFormat pixelFormat = texture.getPixelFormat();
        if (pixelFormat == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, baseTransform, texture, f, f2, f3 - f, f4 - f2);
        } else {
            this.context.validateTextureOp(this, baseTransform, texture, pixelFormat);
        }
        if (this.isSimpleTranslate) {
            f += this.transX;
            f2 += this.transY;
            f3 += this.transX;
            f4 += this.transY;
            f9 += this.transY;
            f10 += this.transY;
        }
        float f13 = texture.getPhysicalWidth();
        float f14 = texture.getPhysicalHeight();
        float f15 = texture.getContentX();
        float f16 = texture.getContentY();
        float f17 = (f15 + f5) / f13;
        float f18 = (f16 + f6) / f14;
        float f19 = (f15 + f7) / f13;
        float f20 = (f16 + f8) / f14;
        float f21 = (f16 + f11) / f14;
        float f22 = (f16 + f12) / f14;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        if (this.context.isSuperShaderEnabled()) {
            vertexBuffer.addSuperQuad(f, f2, f3, f9, f17, f18, f19, f21, false);
            vertexBuffer.addSuperQuad(f, f9, f3, f10, f17, f21, f19, f22, false);
            vertexBuffer.addSuperQuad(f, f10, f3, f4, f17, f22, f19, f20, false);
        } else {
            vertexBuffer.addQuad(f, f2, f3, f9, f17, f18, f19, f21);
            vertexBuffer.addQuad(f, f9, f3, f10, f17, f21, f19, f22);
            vertexBuffer.addQuad(f, f10, f3, f4, f17, f22, f19, f20);
        }
    }

    @Override
    public void drawTexture9Slice(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        BaseTransform baseTransform = this.isSimpleTranslate ? IDENT : this.getTransformNoClone();
        PixelFormat pixelFormat = texture.getPixelFormat();
        if (pixelFormat == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, baseTransform, texture, f, f2, f3 - f, f4 - f2);
        } else {
            this.context.validateTextureOp(this, baseTransform, texture, pixelFormat);
        }
        if (this.isSimpleTranslate) {
            f += this.transX;
            f2 += this.transY;
            f3 += this.transX;
            f4 += this.transY;
            f9 += this.transX;
            f10 += this.transY;
            f11 += this.transX;
            f12 += this.transY;
        }
        float f17 = texture.getPhysicalWidth();
        float f18 = texture.getPhysicalHeight();
        float f19 = texture.getContentX();
        float f20 = texture.getContentY();
        float f21 = (f19 + f5) / f17;
        float f22 = (f20 + f6) / f18;
        float f23 = (f19 + f7) / f17;
        float f24 = (f20 + f8) / f18;
        float f25 = (f19 + f13) / f17;
        float f26 = (f20 + f14) / f18;
        float f27 = (f19 + f15) / f17;
        float f28 = (f20 + f16) / f18;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        if (this.context.isSuperShaderEnabled()) {
            vertexBuffer.addSuperQuad(f, f2, f9, f10, f21, f22, f25, f26, false);
            vertexBuffer.addSuperQuad(f9, f2, f11, f10, f25, f22, f27, f26, false);
            vertexBuffer.addSuperQuad(f11, f2, f3, f10, f27, f22, f23, f26, false);
            vertexBuffer.addSuperQuad(f, f10, f9, f12, f21, f26, f25, f28, false);
            vertexBuffer.addSuperQuad(f9, f10, f11, f12, f25, f26, f27, f28, false);
            vertexBuffer.addSuperQuad(f11, f10, f3, f12, f27, f26, f23, f28, false);
            vertexBuffer.addSuperQuad(f, f12, f9, f4, f21, f28, f25, f24, false);
            vertexBuffer.addSuperQuad(f9, f12, f11, f4, f25, f28, f27, f24, false);
            vertexBuffer.addSuperQuad(f11, f12, f3, f4, f27, f28, f23, f24, false);
        } else {
            vertexBuffer.addQuad(f, f2, f9, f10, f21, f22, f25, f26);
            vertexBuffer.addQuad(f9, f2, f11, f10, f25, f22, f27, f26);
            vertexBuffer.addQuad(f11, f2, f3, f10, f27, f22, f23, f26);
            vertexBuffer.addQuad(f, f10, f9, f12, f21, f26, f25, f28);
            vertexBuffer.addQuad(f9, f10, f11, f12, f25, f26, f27, f28);
            vertexBuffer.addQuad(f11, f10, f3, f12, f27, f26, f23, f28);
            vertexBuffer.addQuad(f, f12, f9, f4, f21, f28, f25, f24);
            vertexBuffer.addQuad(f9, f12, f11, f4, f25, f28, f27, f24);
            vertexBuffer.addQuad(f11, f12, f3, f4, f27, f28, f23, f24);
        }
    }

    @Override
    public void drawTextureVO(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        BaseTransform baseTransform = this.isSimpleTranslate ? IDENT : this.getTransformNoClone();
        PixelFormat pixelFormat = texture.getPixelFormat();
        if (pixelFormat == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, baseTransform, texture, f3, f4, f5 - f3, f6 - f4);
        } else {
            this.context.validateTextureOp(this, baseTransform, texture, pixelFormat);
        }
        if (this.isSimpleTranslate) {
            f3 += this.transX;
            f4 += this.transY;
            f5 += this.transX;
            f6 += this.transY;
        }
        float f11 = texture.getPhysicalWidth();
        float f12 = texture.getPhysicalHeight();
        float f13 = texture.getContentX();
        float f14 = texture.getContentY();
        float f15 = (f13 + f7) / f11;
        float f16 = (f14 + f8) / f12;
        float f17 = (f13 + f9) / f11;
        float f18 = (f14 + f10) / f12;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        if (f == 1.0f && f2 == 1.0f) {
            vertexBuffer.addQuad(f3, f4, f5, f6, f15, f16, f17, f18);
        } else {
            vertexBuffer.addQuadVO(f *= this.getExtraAlpha(), f2 *= this.getExtraAlpha(), f3, f4, f5, f6, f15, f16, f17, f18);
        }
    }

    @Override
    public void drawTextureRaw(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        PixelFormat pixelFormat;
        float f9 = f;
        float f10 = f2;
        float f11 = f3 - f;
        float f12 = f4 - f2;
        BaseTransform baseTransform = this.getTransformNoClone();
        if (this.isSimpleTranslate) {
            baseTransform = IDENT;
            f += this.transX;
            f2 += this.transY;
            f3 += this.transX;
            f4 += this.transY;
        }
        if ((pixelFormat = texture.getPixelFormat()) == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, baseTransform, texture, f9, f10, f11, f12);
        } else {
            this.context.validateTextureOp(this, baseTransform, texture, pixelFormat);
        }
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addQuad(f, f2, f3, f4, f5, f6, f7, f8);
    }

    @Override
    public void drawMappedTextureRaw(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        PixelFormat pixelFormat;
        float f13 = f;
        float f14 = f2;
        float f15 = f3 - f;
        float f16 = f4 - f2;
        BaseTransform baseTransform = this.getTransformNoClone();
        if (this.isSimpleTranslate) {
            baseTransform = IDENT;
            f += this.transX;
            f2 += this.transY;
            f3 += this.transX;
            f4 += this.transY;
        }
        if ((pixelFormat = texture.getPixelFormat()) == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, baseTransform, texture, f13, f14, f15, f16);
        } else {
            this.context.validateTextureOp(this, baseTransform, texture, pixelFormat);
        }
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addMappedQuad(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12);
    }
}

