/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.ps;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.prism.CompositeMode;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseContext;
import com.sun.prism.impl.BaseGraphics;
import com.sun.prism.impl.ps.BaseShaderGraphics;
import com.sun.prism.impl.ps.PaintHelper;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;

public abstract class BaseShaderContext
extends BaseContext {
    private static final int CHECK_SHADER = 1;
    private static final int CHECK_TRANSFORM = 2;
    private static final int CHECK_CLIP = 4;
    private static final int CHECK_COMPOSITE = 8;
    private static final int CHECK_PAINT_OP_MASK = 15;
    private static final int CHECK_TEXTURE_OP_MASK = 15;
    private static final int CHECK_CLEAR_OP_MASK = 4;
    private static final int NUM_STOCK_SHADER_SLOTS = MaskType.values().length << 4;
    private final Shader[] stockShaders = new Shader[NUM_STOCK_SHADER_SLOTS];
    private final Shader[] stockATShaders = new Shader[NUM_STOCK_SHADER_SLOTS];
    private final Shader[] specialShaders = new Shader[SpecialShaderType.values().length];
    private final Shader[] specialATShaders = new Shader[SpecialShaderType.values().length];
    private Shader externalShader;
    private RTTexture lcdBuffer;
    private final ShaderFactory factory;
    private State state;

    protected BaseShaderContext(Screen screen, ShaderFactory shaderFactory, int n) {
        super(screen, shaderFactory, n);
        this.factory = shaderFactory;
        this.init();
    }

    protected void init() {
        this.state = null;
        if (this.externalShader != null && !this.externalShader.isValid()) {
            this.externalShader.dispose();
            this.externalShader = null;
        }
    }

    @Override
    protected void setPerspectiveTransform(GeneralTransform3D generalTransform3D) {
        if (this.checkDisposed()) {
            return;
        }
        this.state.isXformValid = false;
        super.setPerspectiveTransform(generalTransform3D);
    }

    protected void resetLastClip(State state) {
        if (this.checkDisposed()) {
            return;
        }
        state.lastClip = null;
    }

    protected abstract State updateRenderTarget(RenderTarget var1, NGCamera var2, boolean var3);

    protected abstract void updateTexture(int var1, Texture var2);

    protected abstract void updateShaderTransform(Shader var1, BaseTransform var2);

    protected abstract void updateWorldTransform(BaseTransform var1);

    protected abstract void updateClipRect(Rectangle var1);

    protected abstract void updateCompositeMode(CompositeMode var1);

    private static int getStockShaderIndex(MaskType maskType, Paint paint) {
        int n;
        int n2;
        if (paint == null) {
            n2 = 0;
            n = 0;
        } else {
            n2 = paint.getType().ordinal();
            n = paint.getType().isGradient() ? ((Gradient)paint).getSpreadMethod() : 0;
        }
        return maskType.ordinal() << 4 | n2 << 2 | n << 0;
    }

    private Shader getPaintShader(boolean bl, MaskType maskType, Paint paint) {
        int n;
        if (this.checkDisposed()) {
            return null;
        }
        Shader[] arrshader = bl ? this.stockATShaders : this.stockShaders;
        Shader shader = arrshader[n = BaseShaderContext.getStockShaderIndex(maskType, paint)];
        if (shader != null && !shader.isValid()) {
            shader.dispose();
            shader = null;
        }
        if (shader == null) {
            String string = maskType.getName() + "_" + paint.getType().getName();
            if (paint.getType().isGradient() && !maskType.isNewPaintStyle()) {
                Gradient gradient = (Gradient)paint;
                int n2 = gradient.getSpreadMethod();
                if (n2 == 0) {
                    string = string + "_PAD";
                } else if (n2 == 1) {
                    string = string + "_REFLECT";
                } else if (n2 == 2) {
                    string = string + "_REPEAT";
                }
            }
            if (bl) {
                string = string + "_AlphaTest";
            }
            shader = arrshader[n] = this.factory.createStockShader(string);
        }
        return shader;
    }

    private void updatePaintShader(BaseShaderGraphics baseShaderGraphics, Shader shader, MaskType maskType, Paint paint, float f, float f2, float f3, float f4) {
        float f5;
        float f6;
        float f7;
        float f8;
        if (this.checkDisposed()) {
            return;
        }
        Paint.Type type = paint.getType();
        if (type == Paint.Type.COLOR || maskType.isNewPaintStyle()) {
            return;
        }
        if (paint.isProportional()) {
            f8 = f;
            f7 = f2;
            f6 = f3;
            f5 = f4;
        } else {
            f8 = 0.0f;
            f7 = 0.0f;
            f6 = 1.0f;
            f5 = 1.0f;
        }
        switch (type) {
            case LINEAR_GRADIENT: {
                PaintHelper.setLinearGradient(baseShaderGraphics, shader, (LinearGradient)paint, f8, f7, f6, f5);
                break;
            }
            case RADIAL_GRADIENT: {
                PaintHelper.setRadialGradient(baseShaderGraphics, shader, (RadialGradient)paint, f8, f7, f6, f5);
                break;
            }
            case IMAGE_PATTERN: {
                PaintHelper.setImagePattern(baseShaderGraphics, shader, (ImagePattern)paint, f8, f7, f6, f5);
            }
        }
    }

    private Shader getSpecialShader(BaseGraphics baseGraphics, SpecialShaderType specialShaderType) {
        if (this.checkDisposed()) {
            return null;
        }
        boolean bl = baseGraphics.isAlphaTestShader();
        Shader[] arrshader = bl ? this.specialATShaders : this.specialShaders;
        Shader shader = arrshader[specialShaderType.ordinal()];
        if (shader != null && !shader.isValid()) {
            shader.dispose();
            shader = null;
        }
        if (shader == null) {
            Object object = specialShaderType.getName();
            if (bl) {
                object = (String)object + "_AlphaTest";
            }
            arrshader[specialShaderType.ordinal()] = shader = this.factory.createStockShader((String)object);
        }
        return shader;
    }

    @Override
    public boolean isSuperShaderEnabled() {
        if (this.checkDisposed()) {
            return false;
        }
        return this.state.lastShader == this.specialATShaders[SpecialShaderType.SUPER.ordinal()] || this.state.lastShader == this.specialShaders[SpecialShaderType.SUPER.ordinal()];
    }

    private void updatePerVertexColor(Paint paint, float f) {
        if (this.checkDisposed()) {
            return;
        }
        if (paint != null && paint.getType() == Paint.Type.COLOR) {
            this.getVertexBuffer().setPerVertexColor((Color)paint, f);
        } else {
            this.getVertexBuffer().setPerVertexColor(f);
        }
    }

    @Override
    public void validateClearOp(BaseGraphics baseGraphics) {
        this.checkState((BaseShaderGraphics)baseGraphics, 4, null, null);
    }

    @Override
    public void validatePaintOp(BaseGraphics baseGraphics, BaseTransform baseTransform, Texture texture, float f, float f2, float f3, float f4) {
        this.validatePaintOp((BaseShaderGraphics)baseGraphics, baseTransform, texture, f, f2, f3, f4);
    }

    Shader validatePaintOp(BaseShaderGraphics baseShaderGraphics, BaseTransform baseTransform, MaskType maskType, float f, float f2, float f3, float f4) {
        return this.validatePaintOp(baseShaderGraphics, baseTransform, maskType, null, f, f2, f3, f4);
    }

    Shader validatePaintOp(BaseShaderGraphics baseShaderGraphics, BaseTransform baseTransform, MaskType maskType, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        if (this.checkDisposed()) {
            return null;
        }
        if (this.state.lastConst1 != f5 || this.state.lastConst2 != f6 || this.state.lastConst3 != f7 || this.state.lastConst4 != f8 || this.state.lastConst5 != f9 || this.state.lastConst6 != f10) {
            this.flushVertexBuffer();
            this.state.lastConst1 = f5;
            this.state.lastConst2 = f6;
            this.state.lastConst3 = f7;
            this.state.lastConst4 = f8;
            this.state.lastConst5 = f9;
            this.state.lastConst6 = f10;
        }
        return this.validatePaintOp(baseShaderGraphics, baseTransform, maskType, null, f, f2, f3, f4);
    }

    Shader validatePaintOp(BaseShaderGraphics baseShaderGraphics, BaseTransform baseTransform, MaskType maskType, Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        if (this.state.lastConst1 != f5 || this.state.lastConst2 != f6 || this.state.lastConst3 != f7 || this.state.lastConst4 != f8 || this.state.lastConst5 != f9 || this.state.lastConst6 != f10) {
            this.flushVertexBuffer();
            this.state.lastConst1 = f5;
            this.state.lastConst2 = f6;
            this.state.lastConst3 = f7;
            this.state.lastConst4 = f8;
            this.state.lastConst5 = f9;
            this.state.lastConst6 = f10;
        }
        return this.validatePaintOp(baseShaderGraphics, baseTransform, maskType, texture, f, f2, f3, f4);
    }

    Shader validatePaintOp(BaseShaderGraphics baseShaderGraphics, BaseTransform baseTransform, Texture texture, float f, float f2, float f3, float f4) {
        return this.validatePaintOp(baseShaderGraphics, baseTransform, MaskType.TEXTURE, texture, f, f2, f3, f4);
    }

    Shader validatePaintOp(BaseShaderGraphics baseShaderGraphics, BaseTransform baseTransform, MaskType maskType, Texture texture, float f, float f2, float f3, float f4) {
        if (maskType == null) {
            throw new InternalError("maskType must be non-null");
        }
        if (this.externalShader == null) {
            Texture texture2;
            Texture texture3;
            Object object;
            Paint paint = baseShaderGraphics.getPaint();
            Texture texture4 = null;
            if (paint.getType().isGradient()) {
                this.flushVertexBuffer();
                texture4 = maskType.isNewPaintStyle() ? PaintHelper.getWrapGradientTexture(baseShaderGraphics) : PaintHelper.getGradientTexture(baseShaderGraphics, (Gradient)paint);
            } else if (paint.getType() == Paint.Type.IMAGE_PATTERN) {
                this.flushVertexBuffer();
                object = (ImagePattern)paint;
                ResourceFactory resourceFactory = baseShaderGraphics.getResourceFactory();
                texture4 = resourceFactory.getCachedTexture(((ImagePattern)object).getImage(), Texture.WrapMode.REPEAT);
            }
            if (this.factory.isSuperShaderAllowed() && texture4 == null && texture == this.factory.getGlyphTexture()) {
                object = this.getSpecialShader(baseShaderGraphics, SpecialShaderType.SUPER);
                texture3 = this.factory.getRegionTexture();
                texture2 = texture;
            } else {
                if (texture != null) {
                    texture3 = texture;
                    texture2 = texture4;
                } else {
                    texture3 = texture4;
                    texture2 = null;
                }
                object = this.getPaintShader(baseShaderGraphics.isAlphaTestShader(), maskType, paint);
            }
            this.checkState(baseShaderGraphics, 15, baseTransform, (Shader)object);
            this.setTexture(0, texture3);
            this.setTexture(1, texture2);
            this.updatePaintShader(baseShaderGraphics, (Shader)object, maskType, paint, f, f2, f3, f4);
            this.updatePerVertexColor(paint, baseShaderGraphics.getExtraAlpha());
            if (texture4 != null) {
                texture4.unlock();
            }
            return object;
        }
        this.checkState(baseShaderGraphics, 15, baseTransform, this.externalShader);
        this.setTexture(0, texture);
        this.setTexture(1, null);
        this.updatePerVertexColor(null, baseShaderGraphics.getExtraAlpha());
        return this.externalShader;
    }

    @Override
    public void validateTextureOp(BaseGraphics baseGraphics, BaseTransform baseTransform, Texture texture, PixelFormat pixelFormat) {
        this.validateTextureOp((BaseShaderGraphics)baseGraphics, baseTransform, texture, null, pixelFormat);
    }

    public Shader validateLCDOp(BaseShaderGraphics baseShaderGraphics, BaseTransform baseTransform, Texture texture, Texture texture2, boolean bl, Paint paint) {
        if (this.checkDisposed()) {
            return null;
        }
        Shader shader = bl ? this.getSpecialShader(baseShaderGraphics, SpecialShaderType.TEXTURE_First_LCD) : this.getSpecialShader(baseShaderGraphics, SpecialShaderType.TEXTURE_SECOND_LCD);
        this.checkState(baseShaderGraphics, 15, baseTransform, shader);
        this.setTexture(0, texture);
        this.setTexture(1, texture2);
        this.updatePerVertexColor(paint, baseShaderGraphics.getExtraAlpha());
        return shader;
    }

    Shader validateTextureOp(BaseShaderGraphics baseShaderGraphics, BaseTransform baseTransform, Texture[] arrtexture, PixelFormat pixelFormat) {
        Shader shader;
        if (this.checkDisposed()) {
            return null;
        }
        if (pixelFormat == PixelFormat.MULTI_YCbCr_420) {
            if (arrtexture.length < 3) {
                return null;
            }
            shader = this.externalShader == null ? this.getSpecialShader(baseShaderGraphics, SpecialShaderType.TEXTURE_YV12) : this.externalShader;
        } else {
            return null;
        }
        if (null != shader) {
            this.checkState(baseShaderGraphics, 15, baseTransform, shader);
            int n = Math.max(0, Math.min(arrtexture.length, 4));
            for (int i = 0; i < n; ++i) {
                this.setTexture(i, arrtexture[i]);
            }
            this.updatePerVertexColor(null, baseShaderGraphics.getExtraAlpha());
        }
        return shader;
    }

    Shader validateTextureOp(BaseShaderGraphics baseShaderGraphics, BaseTransform baseTransform, Texture texture, Texture texture2, PixelFormat pixelFormat) {
        Shader shader;
        block7: {
            block6: {
                if (this.checkDisposed()) {
                    return null;
                }
                if (this.externalShader != null) break block6;
                switch (pixelFormat) {
                    case INT_ARGB_PRE: 
                    case BYTE_BGRA_PRE: 
                    case BYTE_RGB: 
                    case BYTE_GRAY: 
                    case BYTE_APPLE_422: {
                        if (this.factory.isSuperShaderAllowed() && texture == this.factory.getRegionTexture() && texture2 == null) {
                            shader = this.getSpecialShader(baseShaderGraphics, SpecialShaderType.SUPER);
                            texture2 = this.factory.getGlyphTexture();
                        } else {
                            shader = this.getSpecialShader(baseShaderGraphics, SpecialShaderType.TEXTURE_RGB);
                        }
                        break block7;
                    }
                    default: {
                        throw new InternalError("Pixel format not supported: " + pixelFormat);
                    }
                }
            }
            shader = this.externalShader;
        }
        this.checkState(baseShaderGraphics, 15, baseTransform, shader);
        this.setTexture(0, texture);
        this.setTexture(1, texture2);
        this.updatePerVertexColor(null, baseShaderGraphics.getExtraAlpha());
        return shader;
    }

    Shader validateMaskTextureOp(BaseShaderGraphics baseShaderGraphics, BaseTransform baseTransform, Texture texture, Texture texture2, PixelFormat pixelFormat) {
        Shader shader;
        block5: {
            block4: {
                if (this.checkDisposed()) {
                    return null;
                }
                if (this.externalShader != null) break block4;
                switch (pixelFormat) {
                    case INT_ARGB_PRE: 
                    case BYTE_BGRA_PRE: 
                    case BYTE_RGB: 
                    case BYTE_GRAY: 
                    case BYTE_APPLE_422: {
                        shader = this.getSpecialShader(baseShaderGraphics, SpecialShaderType.TEXTURE_MASK_RGB);
                        break block5;
                    }
                    default: {
                        throw new InternalError("Pixel format not supported: " + pixelFormat);
                    }
                }
            }
            shader = this.externalShader;
        }
        this.checkState(baseShaderGraphics, 15, baseTransform, shader);
        this.setTexture(0, texture);
        this.setTexture(1, texture2);
        this.updatePerVertexColor(null, baseShaderGraphics.getExtraAlpha());
        return shader;
    }

    void setExternalShader(BaseShaderGraphics baseShaderGraphics, Shader shader) {
        if (this.checkDisposed()) {
            return;
        }
        this.flushVertexBuffer();
        if (shader != null) {
            shader.enable();
        }
        this.externalShader = shader;
    }

    private void checkState(BaseShaderGraphics baseShaderGraphics, int n, BaseTransform baseTransform, Shader shader) {
        Object object;
        if (this.checkDisposed()) {
            return;
        }
        this.setRenderTarget(baseShaderGraphics);
        if ((n & 1) != 0 && shader != this.state.lastShader) {
            this.flushVertexBuffer();
            shader.enable();
            this.state.lastShader = shader;
            this.state.isXformValid = false;
            n |= 2;
        }
        if (!((n & 2) == 0 || this.state.isXformValid && baseTransform.equals(this.state.lastTransform))) {
            this.flushVertexBuffer();
            this.updateShaderTransform(shader, baseTransform);
            this.state.lastTransform.setTransform(baseTransform);
            this.state.isXformValid = true;
        }
        if ((n & 4) != 0 && (object = baseShaderGraphics.getClipRectNoClone()) != this.state.lastClip) {
            this.flushVertexBuffer();
            this.updateClipRect((Rectangle)object);
            this.state.lastClip = object;
        }
        if ((n & 8) != 0 && (object = baseShaderGraphics.getCompositeMode()) != this.state.lastComp) {
            this.flushVertexBuffer();
            this.updateCompositeMode((CompositeMode)((Object)object));
            this.state.lastComp = object;
        }
    }

    private void setTexture(int n, Texture texture) {
        if (this.checkDisposed()) {
            return;
        }
        if (texture != null) {
            texture.assertLocked();
        }
        if (texture != this.state.lastTextures[n]) {
            this.flushVertexBuffer();
            this.updateTexture(n, texture);
            this.state.lastTextures[n] = texture;
        }
    }

    public void initLCDBuffer(int n, int n2) {
        if (this.checkDisposed()) {
            return;
        }
        this.lcdBuffer = this.factory.createRTTexture(n, n2, Texture.WrapMode.CLAMP_NOT_NEEDED);
        this.lcdBuffer.makePermanent();
    }

    public void disposeLCDBuffer() {
        if (this.lcdBuffer != null) {
            this.lcdBuffer.dispose();
            this.lcdBuffer = null;
        }
    }

    @Override
    public RTTexture getLCDBuffer() {
        return this.lcdBuffer;
    }

    public void validateLCDBuffer(RenderTarget renderTarget) {
        if (this.checkDisposed()) {
            return;
        }
        if (this.lcdBuffer == null || this.lcdBuffer.getPhysicalWidth() < renderTarget.getPhysicalWidth() || this.lcdBuffer.getPhysicalHeight() < renderTarget.getPhysicalHeight()) {
            this.disposeLCDBuffer();
            this.initLCDBuffer(renderTarget.getPhysicalWidth(), renderTarget.getPhysicalHeight());
        }
    }

    public abstract void blit(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10);

    @Override
    protected void setRenderTarget(RenderTarget renderTarget, NGCamera nGCamera, boolean bl, boolean bl2) {
        if (this.checkDisposed()) {
            return;
        }
        if (renderTarget instanceof Texture) {
            ((Texture)((Object)renderTarget)).assertLocked();
        }
        if (this.state == null || bl2 != this.state.lastState3D || renderTarget != this.state.lastRenderTarget || nGCamera != this.state.lastCamera || bl != this.state.lastDepthTest) {
            this.flushVertexBuffer();
            this.state = this.updateRenderTarget(renderTarget, nGCamera, bl);
            this.state.lastRenderTarget = renderTarget;
            this.state.lastCamera = nGCamera;
            this.state.lastDepthTest = bl;
            this.state.isXformValid = false;
            if (bl2 != this.state.lastState3D) {
                this.state.lastState3D = bl2;
                this.state.lastShader = null;
                this.state.lastConst1 = Float.NaN;
                this.state.lastConst2 = Float.NaN;
                this.state.lastConst3 = Float.NaN;
                this.state.lastConst4 = Float.NaN;
                this.state.lastConst5 = Float.NaN;
                this.state.lastConst6 = Float.NaN;
                this.state.lastComp = null;
                this.state.lastClip = null;
                for (int i = 0; i != this.state.lastTextures.length; ++i) {
                    this.state.lastTextures[i] = null;
                }
                if (bl2) {
                    this.setDeviceParametersFor3D();
                } else {
                    this.setDeviceParametersFor2D();
                }
            }
        }
    }

    @Override
    protected void releaseRenderTarget() {
        if (this.state != null) {
            this.state.lastRenderTarget = null;
            for (int i = 0; i < this.state.lastTextures.length; ++i) {
                this.state.lastTextures[i] = null;
            }
        }
    }

    private void disposeShaders(Shader[] arrshader) {
        for (int i = 0; i < arrshader.length; ++i) {
            if (arrshader[i] == null) continue;
            arrshader[i].dispose();
            arrshader[i] = null;
        }
    }

    @Override
    public void dispose() {
        this.disposeShaders(this.stockShaders);
        this.disposeShaders(this.stockATShaders);
        this.disposeShaders(this.specialShaders);
        this.disposeShaders(this.specialATShaders);
        if (this.externalShader != null) {
            this.externalShader.dispose();
            this.externalShader = null;
        }
        this.disposeLCDBuffer();
        this.releaseRenderTarget();
        this.state = null;
        super.dispose();
    }

    public static final class SpecialShaderType
    extends Enum<SpecialShaderType> {
        public static final /* enum */ SpecialShaderType TEXTURE_RGB = new SpecialShaderType("Solid_TextureRGB");
        public static final /* enum */ SpecialShaderType TEXTURE_MASK_RGB = new SpecialShaderType("Mask_TextureRGB");
        public static final /* enum */ SpecialShaderType TEXTURE_YV12 = new SpecialShaderType("Solid_TextureYV12");
        public static final /* enum */ SpecialShaderType TEXTURE_First_LCD = new SpecialShaderType("Solid_TextureFirstPassLCD");
        public static final /* enum */ SpecialShaderType TEXTURE_SECOND_LCD = new SpecialShaderType("Solid_TextureSecondPassLCD");
        public static final /* enum */ SpecialShaderType SUPER = new SpecialShaderType("Mask_TextureSuper");
        private String name;
        private static final /* synthetic */ SpecialShaderType[] $VALUES;

        public static SpecialShaderType[] values() {
            return (SpecialShaderType[])$VALUES.clone();
        }

        public static SpecialShaderType valueOf(String string) {
            return Enum.valueOf(SpecialShaderType.class, string);
        }

        private SpecialShaderType(String string2) {
            this.name = string2;
        }

        public String getName() {
            return this.name;
        }

        private static /* synthetic */ SpecialShaderType[] $values() {
            return new SpecialShaderType[]{TEXTURE_RGB, TEXTURE_MASK_RGB, TEXTURE_YV12, TEXTURE_First_LCD, TEXTURE_SECOND_LCD, SUPER};
        }

        static {
            $VALUES = SpecialShaderType.$values();
        }
    }

    public static class State {
        private Shader lastShader;
        private RenderTarget lastRenderTarget;
        private NGCamera lastCamera;
        private boolean lastDepthTest;
        private BaseTransform lastTransform = new Affine3D();
        private Rectangle lastClip;
        private CompositeMode lastComp;
        private Texture[] lastTextures = new Texture[4];
        private boolean isXformValid;
        private float lastConst1 = Float.NaN;
        private float lastConst2 = Float.NaN;
        private float lastConst3 = Float.NaN;
        private float lastConst4 = Float.NaN;
        private float lastConst5 = Float.NaN;
        private float lastConst6 = Float.NaN;
        private boolean lastState3D = false;
    }

    public static final class MaskType
    extends Enum<MaskType> {
        public static final /* enum */ MaskType SOLID = new MaskType("Solid");
        public static final /* enum */ MaskType TEXTURE = new MaskType("Texture");
        public static final /* enum */ MaskType ALPHA_ONE = new MaskType("AlphaOne", true);
        public static final /* enum */ MaskType ALPHA_TEXTURE = new MaskType("AlphaTexture", true);
        public static final /* enum */ MaskType ALPHA_TEXTURE_DIFF = new MaskType("AlphaTextureDifference", true);
        public static final /* enum */ MaskType FILL_PGRAM = new MaskType("FillPgram");
        public static final /* enum */ MaskType DRAW_PGRAM = new MaskType("DrawPgram", FILL_PGRAM);
        public static final /* enum */ MaskType FILL_CIRCLE = new MaskType("FillCircle");
        public static final /* enum */ MaskType DRAW_CIRCLE = new MaskType("DrawCircle", FILL_CIRCLE);
        public static final /* enum */ MaskType FILL_ELLIPSE = new MaskType("FillEllipse");
        public static final /* enum */ MaskType DRAW_ELLIPSE = new MaskType("DrawEllipse", FILL_ELLIPSE);
        public static final /* enum */ MaskType FILL_ROUNDRECT = new MaskType("FillRoundRect");
        public static final /* enum */ MaskType DRAW_ROUNDRECT = new MaskType("DrawRoundRect", FILL_ROUNDRECT);
        public static final /* enum */ MaskType DRAW_SEMIROUNDRECT = new MaskType("DrawSemiRoundRect");
        private String name;
        private MaskType filltype;
        private boolean newPaintStyle;
        private static final /* synthetic */ MaskType[] $VALUES;

        public static MaskType[] values() {
            return (MaskType[])$VALUES.clone();
        }

        public static MaskType valueOf(String string) {
            return Enum.valueOf(MaskType.class, string);
        }

        private MaskType(String string2) {
            this.name = string2;
        }

        private MaskType(String string2, boolean bl) {
            this.name = string2;
            this.newPaintStyle = bl;
        }

        private MaskType(String string2, MaskType maskType) {
            this.name = string2;
            this.filltype = maskType;
        }

        public String getName() {
            return this.name;
        }

        public MaskType getFillType() {
            return this.filltype;
        }

        public boolean isNewPaintStyle() {
            return this.newPaintStyle;
        }

        private static /* synthetic */ MaskType[] $values() {
            return new MaskType[]{SOLID, TEXTURE, ALPHA_ONE, ALPHA_TEXTURE, ALPHA_TEXTURE_DIFF, FILL_PGRAM, DRAW_PGRAM, FILL_CIRCLE, DRAW_CIRCLE, FILL_ELLIPSE, DRAW_ELLIPSE, FILL_ROUNDRECT, DRAW_ROUNDRECT, DRAW_SEMIROUNDRECT};
        }

        static {
            $VALUES = MaskType.$values();
        }
    }
}

