/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.ps;

import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.AffineBase;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.MaskTextureGraphics;
import com.sun.prism.MultiTexture;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.ReadbackRenderTarget;
import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseGraphics;
import com.sun.prism.impl.GlyphCache;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.impl.ps.BaseShaderContext;
import com.sun.prism.impl.ps.PaintHelper;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.ShapeUtil;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import java.security.AccessController;

public abstract class BaseShaderGraphics
extends BaseGraphics
implements ShaderGraphics,
ReadbackGraphics,
MaskTextureGraphics {
    private static Affine2D TEMP_TX2D = new Affine2D();
    private static Affine3D TEMP_TX3D = new Affine3D();
    private final BaseShaderContext context;
    private Shader externalShader;
    private boolean isComplexPaint;
    private NGLightBase[] lights = null;
    private static RectBounds TMP_BOUNDS = new RectBounds();
    private static final float FRINGE_FACTOR;
    private static final double SQRT_2;
    private boolean lcdSampleInvalid = false;

    protected BaseShaderGraphics(BaseShaderContext baseShaderContext, RenderTarget renderTarget) {
        super(baseShaderContext, renderTarget);
        this.context = baseShaderContext;
    }

    BaseShaderContext getContext() {
        return this.context;
    }

    boolean isComplexPaint() {
        return this.isComplexPaint;
    }

    @Override
    public void getPaintShaderTransform(Affine3D affine3D) {
        affine3D.setTransform(this.getTransformNoClone());
    }

    public Shader getExternalShader() {
        return this.externalShader;
    }

    @Override
    public void setExternalShader(Shader shader) {
        this.externalShader = shader;
        this.context.setExternalShader(this, shader);
    }

    @Override
    public void setPaint(Paint paint) {
        Gradient gradient;
        this.isComplexPaint = paint.getType().isGradient() ? (gradient = (Gradient)paint).getNumStops() > 12 : false;
        super.setPaint(paint);
    }

    @Override
    public void setLights(NGLightBase[] arrnGLightBase) {
        this.lights = arrnGLightBase;
    }

    @Override
    public final NGLightBase[] getLights() {
        return this.lights;
    }

    @Override
    public void drawTexture(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        if (texture instanceof MultiTexture) {
            this.drawMultiTexture((MultiTexture)texture, f, f2, f3, f4, f5, f6, f7, f8);
        } else {
            super.drawTexture(texture, f, f2, f3, f4, f5, f6, f7, f8);
        }
    }

    @Override
    public void drawTexture3SliceH(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        if (!(texture instanceof MultiTexture)) {
            super.drawTexture3SliceH(texture, f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12);
            return;
        }
        MultiTexture multiTexture = (MultiTexture)texture;
        this.drawMultiTexture(multiTexture, f, f2, f9, f4, f5, f6, f11, f8);
        this.drawMultiTexture(multiTexture, f9, f2, f10, f4, f11, f6, f12, f8);
        this.drawMultiTexture(multiTexture, f10, f2, f3, f4, f12, f6, f7, f8);
    }

    @Override
    public void drawTexture3SliceV(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        if (!(texture instanceof MultiTexture)) {
            super.drawTexture3SliceV(texture, f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12);
            return;
        }
        MultiTexture multiTexture = (MultiTexture)texture;
        this.drawMultiTexture(multiTexture, f, f2, f3, f9, f5, f6, f7, f11);
        this.drawMultiTexture(multiTexture, f, f9, f3, f10, f5, f11, f7, f12);
        this.drawMultiTexture(multiTexture, f, f10, f3, f4, f5, f12, f7, f8);
    }

    @Override
    public void drawTexture9Slice(Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
        if (!(texture instanceof MultiTexture)) {
            super.drawTexture9Slice(texture, f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16);
            return;
        }
        MultiTexture multiTexture = (MultiTexture)texture;
        this.drawMultiTexture(multiTexture, f, f2, f9, f10, f5, f6, f13, f14);
        this.drawMultiTexture(multiTexture, f9, f2, f11, f10, f13, f6, f15, f14);
        this.drawMultiTexture(multiTexture, f11, f2, f3, f10, f15, f6, f7, f14);
        this.drawMultiTexture(multiTexture, f, f10, f9, f12, f5, f14, f13, f16);
        this.drawMultiTexture(multiTexture, f9, f10, f11, f12, f13, f14, f15, f16);
        this.drawMultiTexture(multiTexture, f11, f10, f3, f12, f15, f14, f7, f16);
        this.drawMultiTexture(multiTexture, f, f12, f9, f4, f5, f16, f13, f8);
        this.drawMultiTexture(multiTexture, f9, f12, f11, f4, f13, f16, f15, f8);
        this.drawMultiTexture(multiTexture, f11, f12, f3, f4, f15, f16, f7, f8);
    }

    private static float calculateScaleFactor(float f, float f2) {
        if (f == f2) {
            return 1.0f;
        }
        return (f - 1.0f) / f2;
    }

    protected void drawMultiTexture(MultiTexture multiTexture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        Texture texture;
        Texture texture2;
        Texture[] arrtexture;
        Shader shader;
        BaseTransform baseTransform = this.getTransformNoClone();
        if (this.isSimpleTranslate) {
            baseTransform = IDENT;
            f += this.transX;
            f2 += this.transY;
            f3 += this.transX;
            f4 += this.transY;
        }
        if (null == (shader = this.context.validateTextureOp(this, baseTransform, arrtexture = multiTexture.getTextures(), multiTexture.getPixelFormat()))) {
            return;
        }
        if (multiTexture.getPixelFormat() == PixelFormat.MULTI_YCbCr_420) {
            Texture texture3 = arrtexture[0];
            texture2 = arrtexture[2];
            texture = arrtexture[1];
            f14 = multiTexture.getContentWidth();
            f13 = multiTexture.getContentHeight();
            f12 = BaseShaderGraphics.calculateScaleFactor(f14, texture3.getPhysicalWidth());
            f11 = BaseShaderGraphics.calculateScaleFactor(f13, texture3.getPhysicalHeight());
            if (arrtexture.length > 3) {
                Texture texture4 = arrtexture[3];
                f10 = BaseShaderGraphics.calculateScaleFactor(f14, texture4.getPhysicalWidth());
                f9 = BaseShaderGraphics.calculateScaleFactor(f13, texture4.getPhysicalHeight());
            } else {
                f9 = 0.0f;
                f10 = 0.0f;
            }
        } else {
            throw new UnsupportedOperationException("Unsupported multitexture format " + multiTexture.getPixelFormat());
        }
        float f15 = (float)Math.floor((double)f14 / 2.0);
        float f16 = (float)Math.floor((double)f13 / 2.0);
        float f17 = BaseShaderGraphics.calculateScaleFactor(f15, texture2.getPhysicalWidth());
        float f18 = BaseShaderGraphics.calculateScaleFactor(f16, texture2.getPhysicalHeight());
        float f19 = BaseShaderGraphics.calculateScaleFactor(f15, texture.getPhysicalWidth());
        float f20 = BaseShaderGraphics.calculateScaleFactor(f16, texture.getPhysicalHeight());
        shader.setConstant("lumaAlphaScale", f12, f11, f10, f9);
        shader.setConstant("cbCrScale", f17, f18, f19, f20);
        float f21 = f5 / f14;
        float f22 = f6 / f13;
        float f23 = f7 / f14;
        float f24 = f8 / f13;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addQuad(f, f2, f3, f4, f21, f22, f23, f24);
    }

    @Override
    public void drawTextureRaw2(Texture texture, Texture texture2, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        BaseTransform baseTransform = this.getTransformNoClone();
        if (this.isSimpleTranslate) {
            baseTransform = IDENT;
            f += this.transX;
            f2 += this.transY;
            f3 += this.transX;
            f4 += this.transY;
        }
        this.context.validateTextureOp(this, baseTransform, texture, texture2, PixelFormat.INT_ARGB_PRE);
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addQuad(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12);
    }

    @Override
    public void drawMappedTextureRaw2(Texture texture, Texture texture2, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20) {
        BaseTransform baseTransform = this.getTransformNoClone();
        if (this.isSimpleTranslate) {
            baseTransform = IDENT;
            f += this.transX;
            f2 += this.transY;
            f3 += this.transX;
            f4 += this.transY;
        }
        this.context.validateTextureOp(this, baseTransform, texture, texture2, PixelFormat.INT_ARGB_PRE);
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addMappedQuad(f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20);
    }

    @Override
    public void drawPixelsMasked(RTTexture rTTexture, RTTexture rTTexture2, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        if (n3 <= 0 || n4 <= 0) {
            return;
        }
        float f = rTTexture.getPhysicalWidth();
        float f2 = rTTexture.getPhysicalHeight();
        float f3 = rTTexture2.getPhysicalWidth();
        float f4 = rTTexture2.getPhysicalHeight();
        float f5 = n;
        float f6 = n2;
        float f7 = n + n3;
        float f8 = n2 + n4;
        float f9 = (float)n5 / f;
        float f10 = (float)n6 / f2;
        float f11 = (float)(n5 + n3) / f;
        float f12 = (float)(n6 + n4) / f2;
        float f13 = (float)n7 / f3;
        float f14 = (float)n8 / f4;
        float f15 = (float)(n7 + n3) / f3;
        float f16 = (float)(n8 + n4) / f4;
        this.context.validateMaskTextureOp(this, IDENT, rTTexture, rTTexture2, PixelFormat.INT_ARGB_PRE);
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addQuad(f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16);
    }

    @Override
    public void maskInterpolatePixels(RTTexture rTTexture, RTTexture rTTexture2, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        if (n3 <= 0 || n4 <= 0) {
            return;
        }
        float f = rTTexture.getPhysicalWidth();
        float f2 = rTTexture.getPhysicalHeight();
        float f3 = rTTexture2.getPhysicalWidth();
        float f4 = rTTexture2.getPhysicalHeight();
        float f5 = n;
        float f6 = n2;
        float f7 = n + n3;
        float f8 = n2 + n4;
        float f9 = (float)n5 / f;
        float f10 = (float)n6 / f2;
        float f11 = (float)(n5 + n3) / f;
        float f12 = (float)(n6 + n4) / f2;
        float f13 = (float)n7 / f3;
        float f14 = (float)n8 / f4;
        float f15 = (float)(n7 + n3) / f3;
        float f16 = (float)(n8 + n4) / f4;
        CompositeMode compositeMode = this.getCompositeMode();
        this.setCompositeMode(CompositeMode.DST_OUT);
        this.context.validateTextureOp((BaseGraphics)this, IDENT, rTTexture2, PixelFormat.INT_ARGB_PRE);
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addQuad(f5, f6, f7, f8, f13, f14, f15, f16);
        this.setCompositeMode(CompositeMode.ADD);
        this.context.validateMaskTextureOp(this, IDENT, rTTexture, rTTexture2, PixelFormat.INT_ARGB_PRE);
        vertexBuffer.addQuad(f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16);
        this.setCompositeMode(compositeMode);
    }

    private void renderWithComplexPaint(Shape shape, BasicStroke basicStroke, float f, float f2, float f3, float f4) {
        this.context.flushVertexBuffer();
        BaseTransform baseTransform = this.getTransformNoClone();
        MaskData maskData = ShapeUtil.rasterizeShape(shape, basicStroke, this.getFinalClipNoClone(), baseTransform, true, this.isAntialiasedShape());
        int n = maskData.getWidth();
        int n2 = maskData.getHeight();
        float f5 = maskData.getOriginX();
        float f6 = maskData.getOriginY();
        float f7 = f5 + (float)n;
        float f8 = f6 + (float)n2;
        Gradient gradient = (Gradient)this.paint;
        TEMP_TX2D.setToTranslation(-f5, -f6);
        TEMP_TX2D.concatenate(baseTransform);
        Texture texture = this.context.getGradientTexture(gradient, TEMP_TX2D, n, n2, maskData, f, f2, f3, f4);
        float f9 = 0.0f;
        float f10 = 0.0f;
        float f11 = f9 + (float)n / (float)texture.getPhysicalWidth();
        float f12 = f10 + (float)n2 / (float)texture.getPhysicalHeight();
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        this.context.validateTextureOp(this, IDENT, texture, null, texture.getPixelFormat());
        vertexBuffer.addQuad(f5, f6, f7, f8, f9, f10, f11, f12);
        texture.unlock();
    }

    @Override
    protected void renderShape(Shape shape, BasicStroke basicStroke, float f, float f2, float f3, float f4) {
        AffineBase affineBase;
        if (this.isComplexPaint) {
            this.renderWithComplexPaint(shape, basicStroke, f, f2, f3, f4);
            return;
        }
        BaseTransform baseTransform = this.getTransformNoClone();
        MaskData maskData = ShapeUtil.rasterizeShape(shape, basicStroke, this.getFinalClipNoClone(), baseTransform, true, this.isAntialiasedShape());
        Texture texture = this.context.validateMaskTexture(maskData, false);
        if (PrismSettings.primTextureSize != 0) {
            Shader shader = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture, f, f2, f3, f4);
            affineBase = this.getPaintTextureTx(baseTransform, shader, f, f2, f3, f4);
        } else {
            this.context.validatePaintOp(this, IDENT, texture, f, f2, f3, f4);
            affineBase = null;
        }
        this.context.updateMaskTexture(maskData, TMP_BOUNDS, false);
        float f5 = maskData.getOriginX();
        float f6 = maskData.getOriginY();
        float f7 = f5 + (float)maskData.getWidth();
        float f8 = f6 + (float)maskData.getHeight();
        float f9 = TMP_BOUNDS.getMinX();
        float f10 = TMP_BOUNDS.getMinY();
        float f11 = TMP_BOUNDS.getMaxX();
        float f12 = TMP_BOUNDS.getMaxY();
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addQuad(f5, f6, f7, f8, f9, f10, f11, f12, affineBase);
        texture.unlock();
    }

    private static float getStrokeExpansionFactor(BasicStroke basicStroke) {
        if (basicStroke.getType() == 2) {
            return 1.0f;
        }
        if (basicStroke.getType() == 0) {
            return 0.5f;
        }
        return 0.0f;
    }

    private BaseTransform extract3Dremainder(BaseTransform baseTransform) {
        if (baseTransform.is2D()) {
            return IDENT;
        }
        TEMP_TX3D.setTransform(baseTransform);
        TEMP_TX2D.setTransform(baseTransform.getMxx(), baseTransform.getMyx(), baseTransform.getMxy(), baseTransform.getMyy(), baseTransform.getMxt(), baseTransform.getMyt());
        try {
            TEMP_TX2D.invert();
            TEMP_TX3D.concatenate(TEMP_TX2D);
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            // empty catch block
        }
        return TEMP_TX3D;
    }

    private void renderGeneralRoundedRect(float f, float f2, float f3, float f4, float f5, float f6, BaseShaderContext.MaskType maskType, BasicStroke basicStroke) {
        BaseTransform baseTransform;
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        float f16;
        float f17;
        float f18;
        if (basicStroke == null) {
            f18 = f;
            f17 = f2;
            f16 = f3;
            f15 = f4;
            f14 = 0.0f;
            f13 = 0.0f;
        } else {
            float f19 = basicStroke.getLineWidth();
            float f20 = BaseShaderGraphics.getStrokeExpansionFactor(basicStroke) * f19;
            f18 = f - f20;
            f17 = f2 - f20;
            f16 = f3 + (f20 *= 2.0f);
            f15 = f4 + f20;
            if (f5 > 0.0f && f6 > 0.0f) {
                f5 += f20;
                f6 += f20;
            } else if (basicStroke.getLineJoin() == 1) {
                f5 = f6 = f20;
                maskType = BaseShaderContext.MaskType.DRAW_ROUNDRECT;
            } else {
                f6 = 0.0f;
                f5 = 0.0f;
            }
            f13 = (f16 - f19 * 2.0f) / f16;
            f14 = (f15 - f19 * 2.0f) / f15;
            if (f13 <= 0.0f || f14 <= 0.0f) {
                maskType = maskType.getFillType();
            }
        }
        BaseTransform baseTransform2 = this.getTransformNoClone();
        if (this.isSimpleTranslate) {
            f12 = 1.0f;
            f11 = 1.0f;
            f10 = 0.0f;
            f9 = 0.0f;
            f8 = f18 + this.transX;
            f7 = f17 + this.transY;
            baseTransform = IDENT;
        } else {
            baseTransform = this.extract3Dremainder(baseTransform2);
            f11 = (float)baseTransform2.getMxx();
            f10 = (float)baseTransform2.getMxy();
            f9 = (float)baseTransform2.getMyx();
            f12 = (float)baseTransform2.getMyy();
            f8 = f18 * f11 + f17 * f10 + (float)baseTransform2.getMxt();
            f7 = f18 * f9 + f17 * f12 + (float)baseTransform2.getMyt();
        }
        float f21 = f5 / f16;
        float f22 = f6 / f15;
        this.renderGeneralRoundedPgram(f8, f7, f11 *= f16, f9 *= f16, f10 *= f15, f12 *= f15, f21, f22, f13, f14, baseTransform, maskType, f, f2, f3, f4);
    }

    private void renderGeneralRoundedPgram(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, BaseTransform baseTransform, BaseShaderContext.MaskType maskType, float f11, float f12, float f13, float f14) {
        float f15;
        float f16;
        float f17 = BaseShaderGraphics.len(f3, f4);
        float f18 = BaseShaderGraphics.len(f5, f6);
        if (f17 == 0.0f || f18 == 0.0f) {
            return;
        }
        float f19 = f;
        float f20 = f2;
        float f21 = f + f3;
        float f22 = f2 + f4;
        float f23 = f + f5;
        float f24 = f2 + f6;
        float f25 = f21 + f5;
        float f26 = f22 + f6;
        float f27 = (f3 * f6 - f4 * f5) * 0.5f;
        float f28 = f27 / f18;
        float f29 = f27 / f17;
        if (f28 < 0.0f) {
            f28 = -f28;
        }
        if (f29 < 0.0f) {
            f29 = -f29;
        }
        float f30 = f3 / f17;
        float f31 = f4 / f17;
        float f32 = f5 / f18;
        float f33 = f6 / f18;
        float f34 = -f5 * (f30 + f32) - f6 * (f31 + f33);
        float f35 = f6 * f3 - f5 * f4;
        float f36 = f34 / f35;
        float f37 = FRINGE_FACTOR * Math.signum(f36);
        float f38 = (f36 * f3 + f31) * f37;
        float f39 = (f36 * f4 - f30) * f37;
        f19 += f38;
        f20 += f39;
        f25 -= f38;
        f26 -= f39;
        f34 = f4 * (f33 - f31) - f3 * (f30 - f32);
        f36 = f34 / f35;
        f37 = FRINGE_FACTOR * Math.signum(f36);
        f38 = (f36 * f5 + f33) * f37;
        f39 = (f36 * f6 - f32) * f37;
        float f40 = (f19 + f25) * 0.5f;
        float f41 = (f20 + f26) * 0.5f;
        float f42 = f40 * f33 - f41 * f32;
        float f43 = f40 * f31 - f41 * f30;
        float f44 = f19 * f33 - f20 * f32 - f42;
        float f45 = f19 * f31 - f20 * f30 - f43;
        float f46 = (f21 += f38) * f33 - (f22 += f39) * f32 - f42;
        float f47 = f21 * f31 - f22 * f30 - f43;
        float f48 = (f23 -= f38) * f33 - (f24 -= f39) * f32 - f42;
        float f49 = f23 * f31 - f24 * f30 - f43;
        float f50 = f25 * f33 - f26 * f32 - f42;
        float f51 = f25 * f31 - f26 * f30 - f43;
        if (maskType == BaseShaderContext.MaskType.DRAW_ROUNDRECT || maskType == BaseShaderContext.MaskType.FILL_ROUNDRECT) {
            f16 = f28 * f7;
            f15 = f29 * f8;
            if ((double)f16 < 0.5 || (double)f15 < 0.5) {
                maskType = maskType == BaseShaderContext.MaskType.DRAW_ROUNDRECT ? BaseShaderContext.MaskType.DRAW_PGRAM : BaseShaderContext.MaskType.FILL_PGRAM;
            } else {
                float f52;
                float f53;
                float f54 = f28 - f16;
                float f55 = f29 - f15;
                if (maskType == BaseShaderContext.MaskType.DRAW_ROUNDRECT) {
                    float f56 = f28 * f9;
                    float f57 = f29 * f10;
                    f53 = f56 - f54;
                    f52 = f57 - f55;
                    if (f53 < 0.5f || f52 < 0.5f) {
                        f53 = f56;
                        f52 = f57;
                        maskType = BaseShaderContext.MaskType.DRAW_SEMIROUNDRECT;
                    } else {
                        f53 = 1.0f / f53;
                        f52 = 1.0f / f52;
                    }
                } else {
                    f52 = 0.0f;
                    f53 = 0.0f;
                }
                f16 = 1.0f / f16;
                f15 = 1.0f / f15;
                Shader shader = this.context.validatePaintOp(this, baseTransform, maskType, f11, f12, f13, f14, f16, f15, f53, f52, 0.0f, 0.0f);
                shader.setConstant("oinvarcradii", f16, f15);
                if (maskType == BaseShaderContext.MaskType.DRAW_ROUNDRECT) {
                    shader.setConstant("iinvarcradii", f53, f52);
                } else if (maskType == BaseShaderContext.MaskType.DRAW_SEMIROUNDRECT) {
                    shader.setConstant("idim", f53, f52);
                }
                f28 = f54;
                f29 = f55;
            }
        }
        if (maskType == BaseShaderContext.MaskType.DRAW_PGRAM || maskType == BaseShaderContext.MaskType.DRAW_ELLIPSE) {
            f16 = f28 * f9;
            f15 = f29 * f10;
            if (maskType == BaseShaderContext.MaskType.DRAW_ELLIPSE) {
                if ((double)Math.abs(f28 - f29) < 0.01) {
                    maskType = BaseShaderContext.MaskType.DRAW_CIRCLE;
                    f29 = (float)Math.min(1.0, (double)(f29 * f29) * Math.PI);
                    f15 = (float)Math.min(1.0, (double)(f15 * f15) * Math.PI);
                } else {
                    f28 = 1.0f / f28;
                    f29 = 1.0f / f29;
                    f16 = 1.0f / f16;
                    f15 = 1.0f / f15;
                }
            }
            Shader shader = this.context.validatePaintOp(this, baseTransform, maskType, f11, f12, f13, f14, f16, f15, 0.0f, 0.0f, 0.0f, 0.0f);
            shader.setConstant("idim", f16, f15);
        } else if (maskType == BaseShaderContext.MaskType.FILL_ELLIPSE) {
            if ((double)Math.abs(f28 - f29) < 0.01) {
                maskType = BaseShaderContext.MaskType.FILL_CIRCLE;
                f29 = (float)Math.min(1.0, (double)(f29 * f29) * Math.PI);
            } else {
                f28 = 1.0f / f28;
                f29 = 1.0f / f29;
                f44 *= f28;
                f45 *= f29;
                f46 *= f28;
                f47 *= f29;
                f48 *= f28;
                f49 *= f29;
                f50 *= f28;
                f51 *= f29;
            }
            this.context.validatePaintOp(this, baseTransform, maskType, f11, f12, f13, f14);
        } else if (maskType == BaseShaderContext.MaskType.FILL_PGRAM) {
            this.context.validatePaintOp(this, baseTransform, maskType, f11, f12, f13, f14);
        }
        this.context.getVertexBuffer().addMappedPgram(f19, f20, f21, f22, f23, f24, f25, f26, f44, f45, f46, f47, f48, f49, f50, f51, f28, f29);
    }

    AffineBase getPaintTextureTx(BaseTransform baseTransform, Shader shader, float f, float f2, float f3, float f4) {
        switch (this.paint.getType()) {
            case COLOR: {
                return null;
            }
            case LINEAR_GRADIENT: {
                return PaintHelper.getLinearGradientTx((LinearGradient)this.paint, shader, baseTransform, f, f2, f3, f4);
            }
            case RADIAL_GRADIENT: {
                return PaintHelper.getRadialGradientTx((RadialGradient)this.paint, shader, baseTransform, f, f2, f3, f4);
            }
            case IMAGE_PATTERN: {
                return PaintHelper.getImagePatternTx(this, (ImagePattern)this.paint, shader, baseTransform, f, f2, f3, f4);
            }
        }
        throw new InternalError("Unrecogized paint type: " + this.paint);
    }

    boolean fillPrimRect(float f, float f2, float f3, float f4, Texture texture, Texture texture2, float f5, float f6, float f7, float f8) {
        BaseTransform baseTransform = this.getTransformNoClone();
        float f9 = (float)baseTransform.getMxx();
        float f10 = (float)baseTransform.getMxy();
        float f11 = (float)baseTransform.getMxt();
        float f12 = (float)baseTransform.getMyx();
        float f13 = (float)baseTransform.getMyy();
        float f14 = (float)baseTransform.getMyt();
        float f15 = BaseShaderGraphics.len(f9, f12);
        float f16 = BaseShaderGraphics.len(f10, f13);
        if (f15 == 0.0f || f16 == 0.0f) {
            return true;
        }
        float f17 = 1.0f / f15;
        float f18 = 1.0f / f16;
        float f19 = f - f17 * 0.5f;
        float f20 = f2 - f18 * 0.5f;
        float f21 = f + f3 + f17 * 0.5f;
        float f22 = f2 + f4 + f18 * 0.5f;
        int n = (int)Math.ceil(f3 * f15 - 0.001953125f);
        int n2 = (int)Math.ceil(f4 * f16 - 0.001953125f);
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        int n3 = this.context.getRectTextureMaxSize();
        if (n <= n3 && n2 <= n3) {
            float f23 = (float)(n * (n + 1) / 2) - 0.5f;
            float f24 = (float)(n2 * (n2 + 1) / 2) - 0.5f;
            float f25 = f23 + (float)n + 1.0f;
            float f26 = f24 + (float)n2 + 1.0f;
            f23 /= (float)texture.getPhysicalWidth();
            f24 /= (float)texture.getPhysicalHeight();
            f25 /= (float)texture.getPhysicalWidth();
            f26 /= (float)texture.getPhysicalHeight();
            if (baseTransform.isTranslateOrIdentity()) {
                f19 += f11;
                f20 += f14;
                f21 += f11;
                f22 += f14;
                baseTransform = IDENT;
            } else {
                if (baseTransform.is2D()) {
                    Shader shader = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture, f5, f6, f7, f8);
                    AffineBase affineBase = this.getPaintTextureTx(IDENT, shader, f5, f6, f7, f8);
                    if (affineBase == null) {
                        vertexBuffer.addMappedPgram(f19 * f9 + f20 * f10 + f11, f19 * f12 + f20 * f13 + f14, f21 * f9 + f20 * f10 + f11, f21 * f12 + f20 * f13 + f14, f19 * f9 + f22 * f10 + f11, f19 * f12 + f22 * f13 + f14, f21 * f9 + f22 * f10 + f11, f21 * f12 + f22 * f13 + f14, f23, f24, f25, f24, f23, f26, f25, f26, 0.0f, 0.0f);
                    } else {
                        vertexBuffer.addMappedPgram(f19 * f9 + f20 * f10 + f11, f19 * f12 + f20 * f13 + f14, f21 * f9 + f20 * f10 + f11, f21 * f12 + f20 * f13 + f14, f19 * f9 + f22 * f10 + f11, f19 * f12 + f22 * f13 + f14, f21 * f9 + f22 * f10 + f11, f21 * f12 + f22 * f13 + f14, f23, f24, f25, f24, f23, f26, f25, f26, f19, f20, f21, f22, affineBase);
                    }
                    return true;
                }
                System.out.println("Not a 2d transform!");
                f14 = 0.0f;
                f11 = 0.0f;
            }
            Shader shader = this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture, f5, f6, f7, f8);
            AffineBase affineBase = this.getPaintTextureTx(IDENT, shader, f5, f6, f7, f8);
            if (affineBase == null) {
                vertexBuffer.addQuad(f19, f20, f21, f22, f23, f24, f25, f26);
            } else {
                affineBase.translate(-f11, -f14);
                vertexBuffer.addQuad(f19, f20, f21, f22, f23, f24, f25, f26, affineBase);
            }
            return true;
        }
        if (texture2 == null) {
            return false;
        }
        float f27 = 0.5f / (float)texture2.getPhysicalWidth();
        float f28 = 0.5f / (float)texture2.getPhysicalHeight();
        float f29 = ((float)n * 0.5f + 1.0f) / (float)texture2.getPhysicalWidth();
        float f30 = ((float)n2 * 0.5f + 1.0f) / (float)texture2.getPhysicalHeight();
        float f31 = f + f3 * 0.5f;
        float f32 = f2 + f4 * 0.5f;
        if (baseTransform.isTranslateOrIdentity()) {
            f19 += f11;
            f20 += f14;
            f31 += f11;
            f32 += f14;
            f21 += f11;
            f22 += f14;
            baseTransform = IDENT;
        } else {
            if (baseTransform.is2D()) {
                Shader shader = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture2, f5, f6, f7, f8);
                AffineBase affineBase = this.getPaintTextureTx(IDENT, shader, f5, f6, f7, f8);
                float f33 = f9 * f19;
                float f34 = f12 * f19;
                float f35 = f10 * f20;
                float f36 = f13 * f20;
                float f37 = f9 * f31;
                float f38 = f12 * f31;
                float f39 = f10 * f32;
                float f40 = f13 * f32;
                float f41 = f9 * f21;
                float f42 = f12 * f21;
                float f43 = f10 * f22;
                float f44 = f13 * f22;
                float f45 = f37 + f39 + f11;
                float f46 = f38 + f40 + f14;
                float f47 = f37 + f35 + f11;
                float f48 = f38 + f36 + f14;
                float f49 = f33 + f39 + f11;
                float f50 = f34 + f40 + f14;
                float f51 = f37 + f43 + f11;
                float f52 = f38 + f44 + f14;
                float f53 = f41 + f39 + f11;
                float f54 = f42 + f40 + f14;
                if (affineBase == null) {
                    vertexBuffer.addMappedPgram(f19 * f9 + f20 * f10 + f11, f19 * f12 + f20 * f13 + f14, f47, f48, f49, f50, f45, f46, f27, f28, f29, f28, f27, f30, f29, f30, 0.0f, 0.0f);
                    vertexBuffer.addMappedPgram(f21 * f9 + f20 * f10 + f11, f21 * f12 + f20 * f13 + f14, f47, f48, f53, f54, f45, f46, f27, f28, f29, f28, f27, f30, f29, f30, 0.0f, 0.0f);
                    vertexBuffer.addMappedPgram(f19 * f9 + f22 * f10 + f11, f19 * f12 + f22 * f13 + f14, f51, f52, f49, f50, f45, f46, f27, f28, f29, f28, f27, f30, f29, f30, 0.0f, 0.0f);
                    vertexBuffer.addMappedPgram(f21 * f9 + f22 * f10 + f11, f21 * f12 + f22 * f13 + f14, f51, f52, f53, f54, f45, f46, f27, f28, f29, f28, f27, f30, f29, f30, 0.0f, 0.0f);
                } else {
                    vertexBuffer.addMappedPgram(f19 * f9 + f20 * f10 + f11, f19 * f12 + f20 * f13 + f14, f47, f48, f49, f50, f45, f46, f27, f28, f29, f28, f27, f30, f29, f30, f19, f20, f31, f32, affineBase);
                    vertexBuffer.addMappedPgram(f21 * f9 + f20 * f10 + f11, f21 * f12 + f20 * f13 + f14, f47, f48, f53, f54, f45, f46, f27, f28, f29, f28, f27, f30, f29, f30, f21, f20, f31, f32, affineBase);
                    vertexBuffer.addMappedPgram(f19 * f9 + f22 * f10 + f11, f19 * f12 + f22 * f13 + f14, f51, f52, f49, f50, f45, f46, f27, f28, f29, f28, f27, f30, f29, f30, f19, f22, f31, f32, affineBase);
                    vertexBuffer.addMappedPgram(f21 * f9 + f22 * f10 + f11, f21 * f12 + f22 * f13 + f14, f51, f52, f53, f54, f45, f46, f27, f28, f29, f28, f27, f30, f29, f30, f21, f22, f31, f32, affineBase);
                }
                return true;
            }
            System.out.println("Not a 2d transform!");
            f14 = 0.0f;
            f11 = 0.0f;
        }
        Shader shader = this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture2, f5, f6, f7, f8);
        AffineBase affineBase = this.getPaintTextureTx(IDENT, shader, f5, f6, f7, f8);
        if (affineBase != null) {
            affineBase.translate(-f11, -f14);
        }
        vertexBuffer.addQuad(f19, f20, f31, f32, f27, f28, f29, f30, affineBase);
        vertexBuffer.addQuad(f21, f20, f31, f32, f27, f28, f29, f30, affineBase);
        vertexBuffer.addQuad(f19, f22, f31, f32, f27, f28, f29, f30, affineBase);
        vertexBuffer.addQuad(f21, f22, f31, f32, f27, f28, f29, f30, affineBase);
        return true;
    }

    boolean drawPrimRect(float f, float f2, float f3, float f4) {
        float f5 = this.stroke.getLineWidth();
        float f6 = BaseShaderGraphics.getStrokeExpansionFactor(this.stroke) * f5;
        BaseTransform baseTransform = this.getTransformNoClone();
        float f7 = (float)baseTransform.getMxx();
        float f8 = (float)baseTransform.getMxy();
        float f9 = (float)baseTransform.getMxt();
        float f10 = (float)baseTransform.getMyx();
        float f11 = (float)baseTransform.getMyy();
        float f12 = (float)baseTransform.getMyt();
        float f13 = BaseShaderGraphics.len(f7, f10);
        float f14 = BaseShaderGraphics.len(f8, f11);
        if (f13 == 0.0f || f14 == 0.0f) {
            return true;
        }
        float f15 = 1.0f / f13;
        float f16 = 1.0f / f14;
        float f17 = f - f6 - f15 * 0.5f;
        float f18 = f2 - f6 - f16 * 0.5f;
        float f19 = f + f3 * 0.5f;
        float f20 = f2 + f4 * 0.5f;
        float f21 = f + f3 + f6 + f15 * 0.5f;
        float f22 = f2 + f4 + f6 + f16 * 0.5f;
        Texture texture = this.context.getWrapRectTexture();
        float f23 = 1.0f / (float)texture.getPhysicalWidth();
        float f24 = 1.0f / (float)texture.getPhysicalHeight();
        float f25 = 0.5f * f23;
        float f26 = 0.5f * f24;
        float f27 = ((f3 * 0.5f + f6) * f13 + 1.0f) * f23;
        float f28 = ((f4 * 0.5f + f6) * f14 + 1.0f) * f24;
        float f29 = f5 * f13 * f23;
        float f30 = f5 * f14 * f24;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        if (baseTransform.isTranslateOrIdentity()) {
            f17 += f9;
            f18 += f12;
            f19 += f9;
            f20 += f12;
            f21 += f9;
            f22 += f12;
            baseTransform = IDENT;
        } else {
            if (baseTransform.is2D()) {
                Shader shader = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE_DIFF, texture, f, f2, f3, f4, f29, f30, 0.0f, 0.0f, 0.0f, 0.0f);
                shader.setConstant("innerOffset", f29, f30);
                AffineBase affineBase = this.getPaintTextureTx(IDENT, shader, f, f2, f3, f4);
                float f31 = f7 * f17;
                float f32 = f10 * f17;
                float f33 = f8 * f18;
                float f34 = f11 * f18;
                float f35 = f7 * f19;
                float f36 = f10 * f19;
                float f37 = f8 * f20;
                float f38 = f11 * f20;
                float f39 = f7 * f21;
                float f40 = f10 * f21;
                float f41 = f8 * f22;
                float f42 = f11 * f22;
                float f43 = f35 + f37 + f9;
                float f44 = f36 + f38 + f12;
                float f45 = f35 + f33 + f9;
                float f46 = f36 + f34 + f12;
                float f47 = f31 + f37 + f9;
                float f48 = f32 + f38 + f12;
                float f49 = f35 + f41 + f9;
                float f50 = f36 + f42 + f12;
                float f51 = f39 + f37 + f9;
                float f52 = f40 + f38 + f12;
                if (affineBase == null) {
                    vertexBuffer.addMappedPgram(f31 + f33 + f9, f32 + f34 + f12, f45, f46, f47, f48, f43, f44, f25, f26, f27, f26, f25, f28, f27, f28, 0.0f, 0.0f);
                    vertexBuffer.addMappedPgram(f39 + f33 + f9, f40 + f34 + f12, f45, f46, f51, f52, f43, f44, f25, f26, f27, f26, f25, f28, f27, f28, 0.0f, 0.0f);
                    vertexBuffer.addMappedPgram(f31 + f41 + f9, f32 + f42 + f12, f49, f50, f47, f48, f43, f44, f25, f26, f27, f26, f25, f28, f27, f28, 0.0f, 0.0f);
                    vertexBuffer.addMappedPgram(f39 + f41 + f9, f40 + f42 + f12, f49, f50, f51, f52, f43, f44, f25, f26, f27, f26, f25, f28, f27, f28, 0.0f, 0.0f);
                } else {
                    vertexBuffer.addMappedPgram(f31 + f33 + f9, f32 + f34 + f12, f45, f46, f47, f48, f43, f44, f25, f26, f27, f26, f25, f28, f27, f28, f17, f18, f19, f20, affineBase);
                    vertexBuffer.addMappedPgram(f39 + f33 + f9, f40 + f34 + f12, f45, f46, f51, f52, f43, f44, f25, f26, f27, f26, f25, f28, f27, f28, f21, f18, f19, f20, affineBase);
                    vertexBuffer.addMappedPgram(f31 + f41 + f9, f32 + f42 + f12, f49, f50, f47, f48, f43, f44, f25, f26, f27, f26, f25, f28, f27, f28, f17, f22, f19, f20, affineBase);
                    vertexBuffer.addMappedPgram(f39 + f41 + f9, f40 + f42 + f12, f49, f50, f51, f52, f43, f44, f25, f26, f27, f26, f25, f28, f27, f28, f21, f22, f19, f20, affineBase);
                }
                texture.unlock();
                return true;
            }
            System.out.println("Not a 2d transform!");
            f12 = 0.0f;
            f9 = 0.0f;
        }
        Shader shader = this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_TEXTURE_DIFF, texture, f, f2, f3, f4, f29, f30, 0.0f, 0.0f, 0.0f, 0.0f);
        shader.setConstant("innerOffset", f29, f30);
        AffineBase affineBase = this.getPaintTextureTx(IDENT, shader, f, f2, f3, f4);
        if (affineBase != null) {
            affineBase.translate(-f9, -f12);
        }
        vertexBuffer.addQuad(f17, f18, f19, f20, f25, f26, f27, f28, affineBase);
        vertexBuffer.addQuad(f21, f18, f19, f20, f25, f26, f27, f28, affineBase);
        vertexBuffer.addQuad(f17, f22, f19, f20, f25, f26, f27, f28, affineBase);
        vertexBuffer.addQuad(f21, f22, f19, f20, f25, f26, f27, f28, affineBase);
        texture.unlock();
        return true;
    }

    boolean drawPrimDiagonal(float f, float f2, float f3, float f4, float f5, int n, float f6, float f7, float f8, float f9) {
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        int n2;
        int n3;
        float f15;
        float f16;
        float f17;
        float f18;
        if (this.stroke.getType() == 0) {
            f5 *= 0.5f;
        }
        float f19 = f3 - f;
        float f20 = f4 - f2;
        float f21 = BaseShaderGraphics.len(f19, f20);
        float f22 = (f19 /= f21) * f5;
        float f23 = (f20 /= f21) * f5;
        float f24 = f + f23;
        float f25 = f2 - f22;
        float f26 = f3 + f23;
        float f27 = f4 - f22;
        float f28 = f - f23;
        float f29 = f2 + f22;
        float f30 = f3 - f23;
        float f31 = f4 + f22;
        if (n == 2) {
            f24 -= f22;
            f25 -= f23;
            f28 -= f22;
            f29 -= f23;
            f26 += f22;
            f27 += f23;
            f30 += f22;
            f31 += f23;
        }
        BaseTransform baseTransform = this.getTransformNoClone();
        float f32 = (float)baseTransform.getMxt();
        float f33 = (float)baseTransform.getMyt();
        if (baseTransform.isTranslateOrIdentity()) {
            f18 = f19;
            f17 = f20;
            f16 = f20;
            f15 = -f19;
            n3 = (int)Math.ceil(BaseShaderGraphics.len(f26 - f24, f27 - f25));
            n2 = (int)Math.ceil(BaseShaderGraphics.len(f28 - f24, f29 - f25));
            baseTransform = IDENT;
        } else if (baseTransform.is2D()) {
            float f34 = (float)baseTransform.getMxx();
            float f35 = (float)baseTransform.getMxy();
            f14 = (float)baseTransform.getMyx();
            f13 = (float)baseTransform.getMyy();
            f12 = f34 * f24 + f35 * f25;
            f11 = f14 * f24 + f13 * f25;
            f24 = f12;
            f25 = f11;
            f12 = f34 * f26 + f35 * f27;
            f11 = f14 * f26 + f13 * f27;
            f26 = f12;
            f27 = f11;
            f12 = f34 * f28 + f35 * f29;
            f11 = f14 * f28 + f13 * f29;
            f28 = f12;
            f29 = f11;
            f12 = f34 * f30 + f35 * f31;
            f11 = f14 * f30 + f13 * f31;
            f30 = f12;
            f31 = f11;
            f18 = f34 * f19 + f35 * f20;
            f17 = f14 * f19 + f13 * f20;
            f10 = BaseShaderGraphics.len(f18, f17);
            if (f10 == 0.0f) {
                return true;
            }
            f18 /= f10;
            f17 /= f10;
            f16 = f34 * f20 - f35 * f19;
            f15 = f14 * f20 - f13 * f19;
            f10 = BaseShaderGraphics.len(f16, f15);
            if (f10 == 0.0f) {
                return true;
            }
            n3 = (int)Math.ceil(Math.abs((f26 - f24) * f18 + (f27 - f25) * f17));
            n2 = (int)Math.ceil(Math.abs((f28 - f24) * (f16 /= f10) + (f29 - f25) * (f15 /= f10)));
            baseTransform = IDENT;
        } else {
            System.out.println("Not a 2d transform!");
            return false;
        }
        f24 = f24 + f32 + (f16 *= 0.5f) - (f18 *= 0.5f);
        f25 = f25 + f33 + (f15 *= 0.5f) - (f17 *= 0.5f);
        f26 = f26 + f32 + f16 + f18;
        f27 = f27 + f33 + f15 + f17;
        f28 = f28 + f32 - f16 - f18;
        f29 = f29 + f33 - f15 - f17;
        f30 = f30 + f32 - f16 + f18;
        f31 = f31 + f33 - f15 + f17;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        int n4 = this.context.getRectTextureMaxSize();
        if (n2 <= n4) {
            f14 = (float)(n2 * (n2 + 1) / 2) - 0.5f;
            f13 = f14 + (float)n2 + 1.0f;
            Texture texture = this.context.getRectTexture();
            f14 /= (float)texture.getPhysicalHeight();
            f13 /= (float)texture.getPhysicalHeight();
            if (n3 <= n4) {
                f11 = (float)(n3 * (n3 + 1) / 2) - 0.5f;
                f10 = f11 + (float)n3 + 1.0f;
                this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture, f6, f7, f8, f9);
                vertexBuffer.addMappedPgram(f24, f25, f26, f27, f28, f29, f30, f31, f11 /= (float)texture.getPhysicalWidth(), f14, f10 /= (float)texture.getPhysicalWidth(), f14, f11, f13, f10, f13, 0.0f, 0.0f);
                texture.unlock();
                return true;
            }
            if (n3 <= n4 * 2 - 1) {
                f11 = (f24 + f26) * 0.5f;
                f10 = (f25 + f27) * 0.5f;
                float f36 = (f28 + f30) * 0.5f;
                float f37 = (f29 + f31) * 0.5f;
                float f38 = (float)(n4 * (n4 + 1) / 2) - 0.5f;
                float f39 = f38 + 0.5f + (float)n3 * 0.5f;
                this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture, f6, f7, f8, f9);
                vertexBuffer.addMappedPgram(f24, f25, f11, f10, f28, f29, f36, f37, f38 /= (float)texture.getPhysicalWidth(), f14, f39 /= (float)texture.getPhysicalWidth(), f14, f38, f13, f39, f13, 0.0f, 0.0f);
                vertexBuffer.addMappedPgram(f26, f27, f11, f10, f30, f31, f36, f37, f38, f14, f39, f14, f38, f13, f39, f13, 0.0f, 0.0f);
                texture.unlock();
                return true;
            }
            f11 = 0.5f / (float)texture.getPhysicalWidth();
            f10 = 1.5f / (float)texture.getPhysicalWidth();
            float f40 = f24 + (f18 *= 2.0f);
            float f41 = f25 + (f17 *= 2.0f);
            float f42 = f26 - f18;
            float f43 = f27 - f17;
            float f44 = f28 + f18;
            float f45 = f29 + f17;
            float f46 = f30 - f18;
            float f47 = f31 - f17;
            this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture, f6, f7, f8, f9);
            vertexBuffer.addMappedPgram(f24, f25, f40, f41, f28, f29, f44, f45, f11, f14, f10, f14, f11, f13, f10, f13, 0.0f, 0.0f);
            vertexBuffer.addMappedPgram(f40, f41, f42, f43, f44, f45, f46, f47, f10, f14, f10, f14, f10, f13, f10, f13, 0.0f, 0.0f);
            vertexBuffer.addMappedPgram(f42, f43, f26, f27, f46, f47, f30, f31, f10, f14, f11, f14, f10, f13, f11, f13, 0.0f, 0.0f);
            texture.unlock();
            return true;
        }
        f14 = (f24 + f26) * 0.5f;
        f13 = (f25 + f27) * 0.5f;
        f12 = (f28 + f30) * 0.5f;
        f11 = (f29 + f31) * 0.5f;
        f10 = (f24 + f28) * 0.5f;
        float f48 = (f25 + f29) * 0.5f;
        float f49 = (f26 + f30) * 0.5f;
        float f50 = (f27 + f31) * 0.5f;
        float f51 = (f14 + f12) * 0.5f;
        float f52 = (f13 + f11) * 0.5f;
        Texture texture = this.context.getWrapRectTexture();
        float f53 = 0.5f / (float)texture.getPhysicalWidth();
        float f54 = 0.5f / (float)texture.getPhysicalHeight();
        float f55 = ((float)n3 * 0.5f + 1.0f) / (float)texture.getPhysicalWidth();
        float f56 = ((float)n2 * 0.5f + 1.0f) / (float)texture.getPhysicalHeight();
        this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture, f6, f7, f8, f9);
        vertexBuffer.addMappedPgram(f24, f25, f14, f13, f10, f48, f51, f52, f53, f54, f55, f54, f53, f56, f55, f56, 0.0f, 0.0f);
        vertexBuffer.addMappedPgram(f26, f27, f14, f13, f49, f50, f51, f52, f53, f54, f55, f54, f53, f56, f55, f56, 0.0f, 0.0f);
        vertexBuffer.addMappedPgram(f28, f29, f12, f11, f10, f48, f51, f52, f53, f54, f55, f54, f53, f56, f55, f56, 0.0f, 0.0f);
        vertexBuffer.addMappedPgram(f30, f31, f12, f11, f49, f50, f51, f52, f53, f54, f55, f54, f53, f56, f55, f56, 0.0f, 0.0f);
        texture.unlock();
        return true;
    }

    @Override
    public void fillRect(float f, float f2, float f3, float f4) {
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return;
        }
        if (!this.isAntialiasedShape()) {
            this.fillQuad(f, f2, f + f3, f2 + f4);
            return;
        }
        if (this.isComplexPaint) {
            scratchRRect.setRoundRect(f, f2, f3, f4, 0.0f, 0.0f);
            this.renderWithComplexPaint(scratchRRect, null, f, f2, f3, f4);
            return;
        }
        if (PrismSettings.primTextureSize != 0) {
            Texture texture = this.context.getRectTexture();
            Texture texture2 = this.context.getWrapRectTexture();
            boolean bl = this.fillPrimRect(f, f2, f3, f4, texture, texture2, f, f2, f3, f4);
            texture.unlock();
            texture2.unlock();
            if (bl) {
                return;
            }
        }
        this.renderGeneralRoundedRect(f, f2, f3, f4, 0.0f, 0.0f, BaseShaderContext.MaskType.FILL_PGRAM, null);
    }

    @Override
    public void fillEllipse(float f, float f2, float f3, float f4) {
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return;
        }
        if (this.isComplexPaint) {
            scratchEllipse.setFrame(f, f2, f3, f4);
            this.renderWithComplexPaint(scratchEllipse, null, f, f2, f3, f4);
            return;
        }
        if (!this.isAntialiasedShape()) {
            scratchEllipse.setFrame(f, f2, f3, f4);
            this.renderShape(scratchEllipse, null, f, f2, f3, f4);
            return;
        }
        if (PrismSettings.primTextureSize != 0 && this.fillPrimRect(f, f2, f3, f4, this.context.getOvalTexture(), null, f, f2, f3, f4)) {
            return;
        }
        this.renderGeneralRoundedRect(f, f2, f3, f4, f3, f4, BaseShaderContext.MaskType.FILL_ELLIPSE, null);
    }

    @Override
    public void fillRoundRect(float f, float f2, float f3, float f4, float f5, float f6) {
        f5 = Math.min(Math.abs(f5), f3);
        f6 = Math.min(Math.abs(f6), f4);
        if (f3 <= 0.0f || f4 <= 0.0f) {
            return;
        }
        if (this.isComplexPaint) {
            scratchRRect.setRoundRect(f, f2, f3, f4, f5, f6);
            this.renderWithComplexPaint(scratchRRect, null, f, f2, f3, f4);
            return;
        }
        if (!this.isAntialiasedShape()) {
            scratchRRect.setRoundRect(f, f2, f3, f4, f5, f6);
            this.renderShape(scratchRRect, null, f, f2, f3, f4);
            return;
        }
        this.renderGeneralRoundedRect(f, f2, f3, f4, f5, f6, BaseShaderContext.MaskType.FILL_ROUNDRECT, null);
    }

    @Override
    public void fillQuad(float f, float f2, float f3, float f4) {
        float f5;
        float f6;
        float f7;
        float f8;
        if (f <= f3) {
            f8 = f;
            f7 = f3 - f;
        } else {
            f8 = f3;
            f7 = f - f3;
        }
        if (f2 <= f4) {
            f6 = f2;
            f5 = f4 - f2;
        } else {
            f6 = f4;
            f5 = f2 - f4;
        }
        if (this.isComplexPaint) {
            scratchRRect.setRoundRect(f8, f6, f7, f5, 0.0f, 0.0f);
            this.renderWithComplexPaint(scratchRRect, null, f8, f6, f7, f5);
            return;
        }
        BaseTransform baseTransform = this.getTransformNoClone();
        if (PrismSettings.primTextureSize != 0) {
            float f9;
            float f10;
            if (baseTransform.isTranslateOrIdentity()) {
                f10 = (float)baseTransform.getMxt();
                f9 = (float)baseTransform.getMyt();
                baseTransform = IDENT;
                f += f10;
                f2 += f9;
                f3 += f10;
                f4 += f9;
            } else {
                f9 = 0.0f;
                f10 = 0.0f;
            }
            Shader shader = this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_ONE, null, f8, f6, f7, f5);
            AffineBase affineBase = this.getPaintTextureTx(IDENT, shader, f8, f6, f7, f5);
            if (affineBase != null) {
                affineBase.translate(-f10, -f9);
            }
            this.context.getVertexBuffer().addQuad(f, f2, f3, f4, 0.0f, 0.0f, 0.0f, 0.0f, affineBase);
            return;
        }
        if (this.isSimpleTranslate) {
            baseTransform = IDENT;
            f8 += this.transX;
            f6 += this.transY;
        }
        this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.SOLID, f8, f6, f7, f5);
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addQuad(f8, f6, f8 + f7, f6 + f5);
    }

    private static boolean canUseStrokeShader(BasicStroke basicStroke) {
        return !basicStroke.isDashed() && (basicStroke.getType() == 1 || basicStroke.getLineJoin() == 1 || basicStroke.getLineJoin() == 0 && (double)basicStroke.getMiterLimit() >= SQRT_2);
    }

    @Override
    public void blit(RTTexture rTTexture, RTTexture rTTexture2, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        if (rTTexture2 == null) {
            this.context.setRenderTarget(this);
        } else {
            this.context.setRenderTarget((BaseGraphics)rTTexture2.createGraphics());
        }
        this.context.blit(rTTexture, rTTexture2, n, n2, n3, n4, n5, n6, n7, n8);
    }

    @Override
    public void drawRect(float f, float f2, float f3, float f4) {
        if (f3 < 0.0f || f4 < 0.0f) {
            return;
        }
        if (f3 == 0.0f || f4 == 0.0f) {
            this.drawLine(f, f2, f + f3, f2 + f4);
            return;
        }
        if (this.isComplexPaint) {
            scratchRRect.setRoundRect(f, f2, f3, f4, 0.0f, 0.0f);
            this.renderWithComplexPaint(scratchRRect, this.stroke, f, f2, f3, f4);
            return;
        }
        if (!this.isAntialiasedShape()) {
            scratchRRect.setRoundRect(f, f2, f3, f4, 0.0f, 0.0f);
            this.renderShape(scratchRRect, this.stroke, f, f2, f3, f4);
            return;
        }
        if (BaseShaderGraphics.canUseStrokeShader(this.stroke)) {
            if (PrismSettings.primTextureSize != 0 && this.stroke.getLineJoin() != 1 && this.drawPrimRect(f, f2, f3, f4)) {
                return;
            }
            this.renderGeneralRoundedRect(f, f2, f3, f4, 0.0f, 0.0f, BaseShaderContext.MaskType.DRAW_PGRAM, this.stroke);
            return;
        }
        scratchRRect.setRoundRect(f, f2, f3, f4, 0.0f, 0.0f);
        this.renderShape(scratchRRect, this.stroke, f, f2, f3, f4);
    }

    private boolean checkInnerCurvature(float f, float f2) {
        float f3 = this.stroke.getLineWidth() * (1.0f - BaseShaderGraphics.getStrokeExpansionFactor(this.stroke));
        return (f -= f3) <= 0.0f || (f2 -= f3) <= 0.0f || f * 2.0f > f2 && f2 * 2.0f > f;
    }

    @Override
    public void drawEllipse(float f, float f2, float f3, float f4) {
        if (f3 < 0.0f || f4 < 0.0f) {
            return;
        }
        if (!this.isComplexPaint && !this.stroke.isDashed() && this.checkInnerCurvature(f3, f4) && this.isAntialiasedShape()) {
            this.renderGeneralRoundedRect(f, f2, f3, f4, f3, f4, BaseShaderContext.MaskType.DRAW_ELLIPSE, this.stroke);
            return;
        }
        scratchEllipse.setFrame(f, f2, f3, f4);
        this.renderShape(scratchEllipse, this.stroke, f, f2, f3, f4);
    }

    @Override
    public void drawRoundRect(float f, float f2, float f3, float f4, float f5, float f6) {
        f5 = Math.min(Math.abs(f5), f3);
        f6 = Math.min(Math.abs(f6), f4);
        if (f3 < 0.0f || f4 < 0.0f) {
            return;
        }
        if (!this.isComplexPaint && !this.stroke.isDashed() && this.checkInnerCurvature(f5, f6) && this.isAntialiasedShape()) {
            this.renderGeneralRoundedRect(f, f2, f3, f4, f5, f6, BaseShaderContext.MaskType.DRAW_ROUNDRECT, this.stroke);
            return;
        }
        scratchRRect.setRoundRect(f, f2, f3, f4, f5, f6);
        this.renderShape(scratchRRect, this.stroke, f, f2, f3, f4);
    }

    @Override
    public void drawLine(float f, float f2, float f3, float f4) {
        BaseShaderContext.MaskType maskType;
        float f5;
        float f6;
        BaseTransform baseTransform;
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        float f16;
        float f17;
        if (f <= f3) {
            f17 = f;
            f16 = f3 - f;
        } else {
            f17 = f3;
            f16 = f - f3;
        }
        if (f2 <= f4) {
            f15 = f2;
            f14 = f4 - f2;
        } else {
            f15 = f4;
            f14 = f2 - f4;
        }
        if (this.stroke.getType() == 1) {
            return;
        }
        if (this.isComplexPaint) {
            scratchLine.setLine(f, f2, f3, f4);
            this.renderWithComplexPaint(scratchLine, this.stroke, f17, f15, f16, f14);
            return;
        }
        if (!this.isAntialiasedShape()) {
            scratchLine.setLine(f, f2, f3, f4);
            this.renderShape(scratchLine, this.stroke, f17, f15, f16, f14);
            return;
        }
        int n = this.stroke.getEndCap();
        if (this.stroke.isDashed()) {
            scratchLine.setLine(f, f2, f3, f4);
            this.renderShape(scratchLine, this.stroke, f17, f15, f16, f14);
            return;
        }
        float f18 = this.stroke.getLineWidth();
        if (PrismSettings.primTextureSize != 0 && n != 1) {
            f13 = f18;
            if (this.stroke.getType() == 0) {
                f13 *= 0.5f;
            }
            if (f16 == 0.0f || f14 == 0.0f) {
                if (n == 2) {
                    f11 = f12 = f13;
                } else if (f16 != 0.0f) {
                    f11 = 0.0f;
                    f12 = f13;
                } else if (f14 != 0.0f) {
                    f11 = f13;
                    f12 = 0.0f;
                } else {
                    return;
                }
                Texture texture = this.context.getRectTexture();
                Texture texture2 = this.context.getWrapRectTexture();
                boolean bl = this.fillPrimRect(f17 - f11, f15 - f12, f16 + f11 + f11, f14 + f12 + f12, texture, texture2, f17, f15, f16, f14);
                texture.unlock();
                texture2.unlock();
                if (bl) {
                    return;
                }
            } else if (this.drawPrimDiagonal(f, f2, f3, f4, f18, n, f17, f15, f16, f14)) {
                return;
            }
        }
        if (this.stroke.getType() == 2) {
            f18 *= 2.0f;
        }
        if ((f12 = BaseShaderGraphics.len(f13 = f3 - f, f11 = f4 - f2)) == 0.0f) {
            if (n == 0) {
                return;
            }
            f10 = f18;
            f9 = 0.0f;
        } else {
            f10 = f18 * f13 / f12;
            f9 = f18 * f11 / f12;
        }
        BaseTransform baseTransform2 = this.getTransformNoClone();
        if (this.isSimpleTranslate) {
            double d = baseTransform2.getMxt();
            double d2 = baseTransform2.getMyt();
            f = (float)((double)f + d);
            f2 = (float)((double)f2 + d2);
            f3 = (float)((double)f3 + d);
            f4 = (float)((double)f4 + d2);
            f8 = f9;
            f7 = -f10;
            baseTransform = IDENT;
        } else {
            baseTransform = this.extract3Dremainder(baseTransform2);
            double[] arrd = new double[]{f, f2, f3, f4};
            baseTransform2.transform(arrd, 0, arrd, 0, 2);
            f = (float)arrd[0];
            f2 = (float)arrd[1];
            f3 = (float)arrd[2];
            f4 = (float)arrd[3];
            f13 = f3 - f;
            f11 = f4 - f2;
            arrd[0] = f10;
            arrd[1] = f9;
            arrd[2] = f9;
            arrd[3] = -f10;
            baseTransform2.deltaTransform(arrd, 0, arrd, 0, 2);
            f10 = (float)arrd[0];
            f9 = (float)arrd[1];
            f8 = (float)arrd[2];
            f7 = (float)arrd[3];
        }
        float f19 = f - f8 / 2.0f;
        float f20 = f2 - f7 / 2.0f;
        if (n != 0) {
            f19 -= f10 / 2.0f;
            f20 -= f9 / 2.0f;
            f13 += f10;
            f11 += f9;
            if (n == 1) {
                f6 = BaseShaderGraphics.len(f10, f9) / BaseShaderGraphics.len(f13, f11);
                f5 = 1.0f;
                maskType = BaseShaderContext.MaskType.FILL_ROUNDRECT;
            } else {
                f5 = 0.0f;
                f6 = 0.0f;
                maskType = BaseShaderContext.MaskType.FILL_PGRAM;
            }
        } else {
            f5 = 0.0f;
            f6 = 0.0f;
            maskType = BaseShaderContext.MaskType.FILL_PGRAM;
        }
        this.renderGeneralRoundedPgram(f19, f20, f13, f11, f8, f7, f6, f5, 0.0f, 0.0f, baseTransform, maskType, f17, f15, f16, f14);
    }

    private static float len(float f, float f2) {
        return f == 0.0f ? Math.abs(f2) : (f2 == 0.0f ? Math.abs(f) : (float)Math.sqrt(f * f + f2 * f2));
    }

    @Override
    public void setNodeBounds(RectBounds rectBounds) {
        this.nodeBounds = rectBounds;
        this.lcdSampleInvalid = rectBounds != null;
    }

    private void initLCDSampleRT() {
        if (this.lcdSampleInvalid) {
            RectBounds rectBounds = new RectBounds();
            this.getTransformNoClone().transform(this.nodeBounds, rectBounds);
            Rectangle rectangle = this.getClipRectNoClone();
            if (rectangle != null && !rectangle.isEmpty()) {
                rectBounds.intersectWith(rectangle);
            }
            float f = rectBounds.getMinX() - 1.0f;
            float f2 = rectBounds.getMinY() - 1.0f;
            float f3 = rectBounds.getWidth() + 2.0f;
            float f4 = rectBounds.getHeight() + 2.0f;
            this.context.validateLCDBuffer(this.getRenderTarget());
            BaseShaderGraphics baseShaderGraphics = (BaseShaderGraphics)this.context.getLCDBuffer().createGraphics();
            baseShaderGraphics.setCompositeMode(CompositeMode.SRC);
            this.context.validateLCDOp(baseShaderGraphics, IDENT, (Texture)((Object)this.getRenderTarget()), null, true, null);
            int n = this.getRenderTarget().getPhysicalHeight();
            int n2 = this.getRenderTarget().getPhysicalWidth();
            float f5 = f / (float)n2;
            float f6 = f2 / (float)n;
            float f7 = (f + f3) / (float)n2;
            float f8 = (f2 + f4) / (float)n;
            baseShaderGraphics.drawLCDBuffer(f, f2, f3, f4, f5, f6, f7, f8);
            this.context.setRenderTarget(this);
        }
        this.lcdSampleInvalid = false;
    }

    @Override
    public void drawString(GlyphList glyphList, FontStrike fontStrike, float f, float f2, Color color, int n, int n2) {
        Object object;
        RectBounds rectBounds;
        float f3;
        boolean bl;
        if (this.isComplexPaint || this.paint.getType().isImagePattern() || fontStrike.drawAsShapes()) {
            BaseTransform baseTransform = BaseTransform.getTranslateInstance(f, f2);
            Shape shape = fontStrike.getOutline(glyphList, baseTransform);
            this.fill(shape);
            return;
        }
        BaseTransform baseTransform = this.getTransformNoClone();
        Paint paint = this.getPaint();
        Color color2 = paint.getType() == Paint.Type.COLOR ? (Color)paint : null;
        CompositeMode compositeMode = this.getCompositeMode();
        boolean bl2 = bl = compositeMode == CompositeMode.SRC_OVER && color2 != null && baseTransform.is2D() && !this.getRenderTarget().isMSAA();
        if (fontStrike.getAAMode() == 1 && !bl) {
            FontResource fontResource = fontStrike.getFontResource();
            f3 = fontStrike.getSize();
            BaseTransform baseTransform2 = fontStrike.getTransform();
            fontStrike = fontResource.getStrike(f3, baseTransform2, 0);
        }
        float f4 = 0.0f;
        f3 = 0.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        if (this.paint.getType().isGradient() && ((Gradient)this.paint).isProportional()) {
            rectBounds = this.nodeBounds;
            if (rectBounds == null) {
                object = fontStrike.getMetrics();
                float f7 = -object.getAscent() * 0.4f;
                rectBounds = new RectBounds(-f7, object.getAscent(), glyphList.getWidth() + 2.0f * f7, object.getDescent() + object.getLineGap());
                f4 = f;
                f3 = f2;
            }
            f4 += rectBounds.getMinX();
            f3 += rectBounds.getMinY();
            f5 = rectBounds.getWidth();
            f6 = rectBounds.getHeight();
        }
        rectBounds = null;
        object = new Point2D(f, f2);
        if (this.isSimpleTranslate) {
            rectBounds = this.getFinalClipNoClone();
            baseTransform = IDENT;
            ((Point2D)object).x += this.transX;
            ((Point2D)object).y += this.transY;
        }
        GlyphCache glyphCache = this.context.getGlyphCache(fontStrike);
        Texture texture = glyphCache.getBackingStore();
        if (fontStrike.getAAMode() == 1) {
            if (this.nodeBounds == null) {
                Metrics metrics = fontStrike.getMetrics();
                RectBounds rectBounds2 = new RectBounds(f - 2.0f, f2 + metrics.getAscent(), f + 2.0f + glyphList.getWidth(), f2 + 1.0f + metrics.getDescent() + metrics.getLineGap());
                this.setNodeBounds(rectBounds2);
                this.initLCDSampleRT();
                this.setNodeBounds(null);
            } else {
                this.initLCDSampleRT();
            }
            float f8 = PrismFontFactory.getLCDContrast();
            float f9 = 1.0f / f8;
            color2 = new Color((float)Math.pow(color2.getRed(), f8), (float)Math.pow(color2.getGreen(), f8), (float)Math.pow(color2.getBlue(), f8), (float)Math.pow(color2.getAlpha(), f8));
            if (color != null) {
                color = new Color((float)Math.pow(color.getRed(), f8), (float)Math.pow(color.getGreen(), f8), (float)Math.pow(color.getBlue(), f8), (float)Math.pow(color.getAlpha(), f8));
            }
            this.setCompositeMode(CompositeMode.SRC);
            Shader shader = this.context.validateLCDOp(this, IDENT, this.context.getLCDBuffer(), texture, false, color2);
            float f10 = 1.0f / (float)texture.getPhysicalWidth();
            shader.setConstant("gamma", f9, f8, f10);
            this.setCompositeMode(compositeMode);
        } else {
            this.context.validatePaintOp(this, IDENT, texture, f4, f3, f5, f6);
        }
        if (this.isSimpleTranslate) {
            ((Point2D)object).y = Math.round(((Point2D)object).y);
            ((Point2D)object).x = Math.round(((Point2D)object).x);
        }
        glyphCache.render(this.context, glyphList, ((Point2D)object).x, ((Point2D)object).y, n, n2, color, color2, baseTransform, rectBounds);
    }

    private void drawLCDBuffer(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.context.setRenderTarget(this);
        this.context.getVertexBuffer().addQuad(f, f2, f + f3, f2 + f4, f5, f6, f7, f8);
    }

    @Override
    public boolean canReadBack() {
        RenderTarget renderTarget = this.getRenderTarget();
        return renderTarget instanceof ReadbackRenderTarget && ((ReadbackRenderTarget)renderTarget).getBackBuffer() != null;
    }

    @Override
    public RTTexture readBack(Rectangle rectangle) {
        RenderTarget renderTarget = this.getRenderTarget();
        this.context.flushVertexBuffer();
        this.context.validateLCDBuffer(renderTarget);
        RTTexture rTTexture = this.context.getLCDBuffer();
        Texture texture = ((ReadbackRenderTarget)renderTarget).getBackBuffer();
        float f = rectangle.x;
        float f2 = rectangle.y;
        float f3 = f + (float)rectangle.width;
        float f4 = f2 + (float)rectangle.height;
        BaseShaderGraphics baseShaderGraphics = (BaseShaderGraphics)rTTexture.createGraphics();
        baseShaderGraphics.setCompositeMode(CompositeMode.SRC);
        this.context.validateTextureOp((BaseGraphics)baseShaderGraphics, IDENT, texture, texture.getPixelFormat());
        baseShaderGraphics.drawTexture(texture, 0.0f, 0.0f, rectangle.width, rectangle.height, f, f2, f3, f4);
        this.context.flushVertexBuffer();
        this.context.setRenderTarget(this);
        return rTTexture;
    }

    @Override
    public void releaseReadBackBuffer(RTTexture rTTexture) {
    }

    @Override
    public void setup3DRendering() {
        this.context.setRenderTarget(this);
    }

    static {
        String string = (String)AccessController.doPrivileged(() -> System.getProperty("prism.primshaderpad"));
        if (string == null) {
            FRINGE_FACTOR = -0.5f;
        } else {
            FRINGE_FACTOR = -Float.valueOf(string).floatValue();
            System.out.println("Prism ShaderGraphics primitive shader pad = " + FRINGE_FACTOR);
        }
        SQRT_2 = Math.sqrt(2.0);
    }
}

