/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.BufferUtil;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import com.sun.scenario.effect.impl.state.RenderState;
import java.nio.FloatBuffer;

public class BoxRenderState
extends LinearConvolveRenderState {
    private static final int[] MAX_BOX_SIZES = new int[]{BoxRenderState.getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 0), BoxRenderState.getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 1), BoxRenderState.getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 2), BoxRenderState.getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 3)};
    private final boolean isShadow;
    private final int blurPasses;
    private final float spread;
    private Color4f shadowColor;
    private RenderState.EffectCoordinateSpace space;
    private BaseTransform inputtx;
    private BaseTransform resulttx;
    private final float inputSizeH;
    private final float inputSizeV;
    private final int spreadPass;
    private float[] samplevectors;
    private int validatedPass;
    private float passSize;
    private FloatBuffer weights;
    private float weightsValidSize;
    private float weightsValidSpread;
    private boolean swCompatible;

    public static int getMaxSizeForKernelSize(int n, int n2) {
        if (n2 == 0) {
            return Integer.MAX_VALUE;
        }
        int n3 = n - 1 | 1;
        n3 = (n3 - 1) / n2 | 1;
        assert (BoxRenderState.getKernelSize(n3, n2) <= n);
        return n3;
    }

    public static int getKernelSize(int n, int n2) {
        int n3 = n < 1 ? 1 : n;
        n3 = (n3 - 1) * n2 + 1;
        return n3 |= 1;
    }

    public BoxRenderState(float f, float f2, int n, float f3, boolean bl, Color4f color4f, BaseTransform baseTransform) {
        boolean bl2;
        this.isShadow = bl;
        this.shadowColor = color4f;
        this.spread = f3;
        this.blurPasses = n;
        if (baseTransform == null) {
            baseTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        double d = Math.hypot(baseTransform.getMxx(), baseTransform.getMyx());
        double d2 = Math.hypot(baseTransform.getMxy(), baseTransform.getMyy());
        float f4 = (float)((double)f * d);
        float f5 = (float)((double)f2 * d2);
        int n2 = MAX_BOX_SIZES[n];
        if (f4 > (float)n2) {
            d = (float)n2 / f;
            f4 = n2;
        }
        if (f5 > (float)n2) {
            d2 = (float)n2 / f2;
            f5 = n2;
        }
        this.inputSizeH = f4;
        this.inputSizeV = f5;
        this.spreadPass = f5 > 1.0f ? 1 : 0;
        boolean bl3 = bl2 = d != baseTransform.getMxx() || 0.0 != baseTransform.getMyx() || d2 != baseTransform.getMyy() || 0.0 != baseTransform.getMxy();
        if (bl2) {
            this.space = RenderState.EffectCoordinateSpace.CustomSpace;
            this.inputtx = BaseTransform.getScaleInstance(d, d2);
            this.resulttx = baseTransform.copy().deriveWithScale(1.0 / d, 1.0 / d2, 1.0);
        } else {
            this.space = RenderState.EffectCoordinateSpace.RenderSpace;
            this.inputtx = baseTransform;
            this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
        }
    }

    public int getBoxPixelSize(int n) {
        float f = this.passSize;
        if (f < 1.0f) {
            f = 1.0f;
        }
        int n2 = (int)Math.ceil(f) | 1;
        return n2;
    }

    public int getBlurPasses() {
        return this.blurPasses;
    }

    public float getSpread() {
        return this.spread;
    }

    @Override
    public boolean isShadow() {
        return this.isShadow;
    }

    @Override
    public Color4f getShadowColor() {
        return this.shadowColor;
    }

    @Override
    public float[] getPassShadowColorComponents() {
        return this.validatedPass == 0 ? BLACK_COMPONENTS : this.shadowColor.getPremultipliedRGBComponents();
    }

    @Override
    public RenderState.EffectCoordinateSpace getEffectTransformSpace() {
        return this.space;
    }

    @Override
    public BaseTransform getInputTransform(BaseTransform baseTransform) {
        return this.inputtx;
    }

    @Override
    public BaseTransform getResultTransform(BaseTransform baseTransform) {
        return this.resulttx;
    }

    public EffectPeer<BoxRenderState> getPassPeer(Renderer renderer, FilterContext filterContext) {
        String string;
        if (this.isPassNop()) {
            return null;
        }
        int n = this.getPassKernelSize();
        int n2 = BoxRenderState.getPeerSize(n);
        Effect.AccelType accelType = renderer.getAccelType();
        switch (accelType) {
            case NONE: 
            case SIMD: {
                if (this.swCompatible && this.spread == 0.0f) {
                    string = this.isShadow() ? "BoxShadow" : "BoxBlur";
                    break;
                }
            }
            default: {
                string = this.isShadow() ? "LinearConvolveShadow" : "LinearConvolve";
            }
        }
        EffectPeer effectPeer = renderer.getPeerInstance(filterContext, string, n2);
        return effectPeer;
    }

    @Override
    public Rectangle getInputClip(int n, Rectangle rectangle) {
        int n2;
        int n3;
        if (rectangle != null && ((n3 = this.getInputKernelSize(0)) | (n2 = this.getInputKernelSize(1))) > 1) {
            rectangle = new Rectangle(rectangle);
            rectangle.grow(n3 / 2, n2 / 2);
        }
        return rectangle;
    }

    @Override
    public ImageData validatePassInput(ImageData imageData, int n) {
        float f;
        this.validatedPass = n;
        BaseTransform baseTransform = imageData.getTransform();
        this.samplevectors = new float[2];
        this.samplevectors[n] = 1.0f;
        float f2 = f = n == 0 ? this.inputSizeH : this.inputSizeV;
        if (baseTransform.isTranslateOrIdentity()) {
            this.swCompatible = true;
            this.passSize = f;
        } else {
            try {
                baseTransform.inverseDeltaTransform(this.samplevectors, 0, this.samplevectors, 0, 1);
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                this.passSize = 0.0f;
                this.samplevectors[1] = 0.0f;
                this.samplevectors[0] = 0.0f;
                this.swCompatible = true;
                return imageData;
            }
            double d = Math.hypot(this.samplevectors[0], this.samplevectors[1]);
            float f3 = (float)((double)f * d);
            f3 = (float)((double)f3 * d);
            int n2 = MAX_BOX_SIZES[this.blurPasses];
            if (f3 > (float)n2) {
                f3 = n2;
                d = (float)n2 / f;
            }
            this.passSize = f3;
            this.samplevectors[0] = (float)((double)this.samplevectors[0] / d);
            this.samplevectors[1] = (float)((double)this.samplevectors[1] / d);
            Rectangle rectangle = imageData.getUntransformedBounds();
            this.swCompatible = n == 0 ? BoxRenderState.nearOne(this.samplevectors[0], rectangle.width) && BoxRenderState.nearZero(this.samplevectors[1], rectangle.width) : BoxRenderState.nearZero(this.samplevectors[0], rectangle.height) && BoxRenderState.nearOne(this.samplevectors[1], rectangle.height);
        }
        Filterable filterable = imageData.getUntransformedImage();
        this.samplevectors[0] = this.samplevectors[0] / (float)filterable.getPhysicalWidth();
        this.samplevectors[1] = this.samplevectors[1] / (float)filterable.getPhysicalHeight();
        return imageData;
    }

    @Override
    public Rectangle getPassResultBounds(Rectangle rectangle, Rectangle rectangle2) {
        Rectangle rectangle3 = new Rectangle(rectangle);
        if (this.validatedPass == 0) {
            rectangle3.grow(this.getInputKernelSize(0) / 2, 0);
        } else {
            rectangle3.grow(0, this.getInputKernelSize(1) / 2);
        }
        if (rectangle2 != null) {
            if (this.validatedPass == 0) {
                rectangle2 = new Rectangle(rectangle2);
                rectangle2.grow(0, this.getInputKernelSize(1) / 2);
            }
            rectangle3.intersectWith(rectangle2);
        }
        return rectangle3;
    }

    @Override
    public float[] getPassVector() {
        float f = this.samplevectors[0];
        float f2 = this.samplevectors[1];
        int n = this.getPassKernelSize();
        int n2 = n / 2;
        float[] arrf = new float[]{f, f2, (float)(-n2) * f, (float)(-n2) * f2};
        return arrf;
    }

    @Override
    public int getPassWeightsArrayLength() {
        this.validateWeights();
        return this.weights.limit() / 4;
    }

    @Override
    public FloatBuffer getPassWeights() {
        this.validateWeights();
        this.weights.rewind();
        return this.weights;
    }

    private void validateWeights() {
        int n;
        int n2;
        float f;
        float f2;
        if (this.blurPasses == 0) {
            f2 = 1.0f;
        } else {
            f2 = this.passSize;
            if (f2 < 1.0f) {
                f2 = 1.0f;
            }
        }
        float f3 = f = this.validatedPass == this.spreadPass ? this.spread : 0.0f;
        if (this.weights != null && this.weightsValidSize == f2 && this.weightsValidSpread == f) {
            return;
        }
        int n3 = n2 = (int)Math.ceil(f2) | 1;
        for (int i = 1; i < this.blurPasses; ++i) {
            n3 += n2 - 1;
        }
        double[] arrd = new double[n3];
        for (int i = 0; i < n2; ++i) {
            arrd[i] = 1.0;
        }
        double d = (float)n2 - f2;
        if (d > 0.0) {
            double d2 = 1.0 - d * 0.5;
            arrd[n2 - 1] = d2;
            arrd[0] = d2;
        }
        int n4 = n2;
        for (int i = 1; i < this.blurPasses; ++i) {
            int n5;
            double d3;
            int n6 = (n4 += n2 - 1) - 1;
            while (n6 > n2) {
                d3 = arrd[n6];
                for (n5 = 1; n5 < n2; ++n5) {
                    d3 += arrd[n6 - n5];
                }
                arrd[n6--] = d3;
            }
            while (n6 > 0) {
                d3 = arrd[n6];
                for (n5 = 0; n5 < n6; ++n5) {
                    d3 += arrd[n5];
                }
                arrd[n6--] = d3;
            }
        }
        double d4 = 0.0;
        for (n = 0; n < arrd.length; ++n) {
            d4 += arrd[n];
        }
        d4 += (1.0 - d4) * (double)f;
        if (this.weights == null) {
            n = BoxRenderState.getPeerSize(MAX_KERNEL_SIZE);
            n = n + 3 & 0xFFFFFFFC;
            this.weights = BufferUtil.newFloatBuffer(n);
        }
        this.weights.clear();
        for (n = 0; n < arrd.length; ++n) {
            this.weights.put((float)(arrd[n] / d4));
        }
        n = BoxRenderState.getPeerSize(arrd.length);
        while (this.weights.position() < n) {
            this.weights.put(0.0f);
        }
        this.weights.limit(n);
        this.weights.rewind();
    }

    @Override
    public int getInputKernelSize(int n) {
        float f;
        float f2 = f = n == 0 ? this.inputSizeH : this.inputSizeV;
        if (f < 1.0f) {
            f = 1.0f;
        }
        int n2 = (int)Math.ceil(f) | 1;
        int n3 = 1;
        for (int i = 0; i < this.blurPasses; ++i) {
            n3 += n2 - 1;
        }
        return n3;
    }

    @Override
    public int getPassKernelSize() {
        float f = this.passSize;
        if (f < 1.0f) {
            f = 1.0f;
        }
        int n = (int)Math.ceil(f) | 1;
        int n2 = 1;
        for (int i = 0; i < this.blurPasses; ++i) {
            n2 += n - 1;
        }
        return n2;
    }

    @Override
    public boolean isNop() {
        if (this.isShadow) {
            return false;
        }
        return this.blurPasses == 0 || this.inputSizeH <= 1.0f && this.inputSizeV <= 1.0f;
    }

    @Override
    public boolean isPassNop() {
        if (this.isShadow && this.validatedPass == 1) {
            return false;
        }
        return this.blurPasses == 0 || this.passSize <= 1.0f;
    }
}

