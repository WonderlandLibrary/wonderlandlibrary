/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.BufferUtil;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import com.sun.scenario.effect.impl.state.RenderState;
import java.nio.FloatBuffer;

public class GaussianRenderState
extends LinearConvolveRenderState {
    public static final float MAX_RADIUS = (MAX_KERNEL_SIZE - 1) / 2;
    private boolean isShadow;
    private Color4f shadowColor;
    private float spread;
    private RenderState.EffectCoordinateSpace space;
    private BaseTransform inputtx;
    private BaseTransform resulttx;
    private float inputRadiusX;
    private float inputRadiusY;
    private float spreadPass;
    private int validatedPass;
    private LinearConvolveRenderState.PassType passType;
    private float passRadius;
    private FloatBuffer weights;
    private float[] samplevectors;
    private float weightsValidRadius;
    private float weightsValidSpread;

    static FloatBuffer getGaussianWeights(FloatBuffer floatBuffer, int n, float f, float f2) {
        int n2;
        int n3 = n;
        int n4 = n3 * 2 + 1;
        if (floatBuffer == null) {
            floatBuffer = BufferUtil.newFloatBuffer(128);
        }
        floatBuffer.clear();
        float f3 = f / 3.0f;
        float f4 = 2.0f * f3 * f3;
        if (f4 < Float.MIN_VALUE) {
            f4 = Float.MIN_VALUE;
        }
        float f5 = 0.0f;
        for (n2 = -n3; n2 <= n3; ++n2) {
            float f6 = (float)Math.exp((float)(-(n2 * n2)) / f4);
            floatBuffer.put(f6);
            f5 += f6;
        }
        f5 += (floatBuffer.get(0) - f5) * f2;
        for (n2 = 0; n2 < n4; ++n2) {
            floatBuffer.put(n2, floatBuffer.get(n2) / f5);
        }
        n2 = GaussianRenderState.getPeerSize(n4);
        while (floatBuffer.position() < n2) {
            floatBuffer.put(0.0f);
        }
        floatBuffer.limit(n2);
        floatBuffer.rewind();
        return floatBuffer;
    }

    public GaussianRenderState(float f, float f2, float f3, boolean bl, Color4f color4f, BaseTransform baseTransform) {
        this.isShadow = bl;
        this.shadowColor = color4f;
        this.spread = f3;
        if (baseTransform == null) {
            baseTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        double d = baseTransform.getMxx();
        double d2 = baseTransform.getMxy();
        double d3 = baseTransform.getMyx();
        double d4 = baseTransform.getMyy();
        double d5 = Math.hypot(d, d3);
        double d6 = Math.hypot(d2, d4);
        boolean bl2 = false;
        float f4 = (float)((double)f * d5);
        float f5 = (float)((double)f2 * d6);
        if (f4 < 0.00390625f && f5 < 0.00390625f) {
            this.inputRadiusX = 0.0f;
            this.inputRadiusY = 0.0f;
            this.spreadPass = 0.0f;
            this.space = RenderState.EffectCoordinateSpace.RenderSpace;
            this.inputtx = baseTransform;
            this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
            this.samplevectors = new float[]{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};
        } else {
            if (f4 > MAX_RADIUS) {
                f4 = MAX_RADIUS;
                d5 = MAX_RADIUS / f;
                bl2 = true;
            }
            if (f5 > MAX_RADIUS) {
                f5 = MAX_RADIUS;
                d6 = MAX_RADIUS / f2;
                bl2 = true;
            }
            this.inputRadiusX = f4;
            this.inputRadiusY = f5;
            float f6 = this.spreadPass = this.inputRadiusY > 1.0f || this.inputRadiusY >= this.inputRadiusX ? 1.0f : 0.0f;
            if (bl2) {
                this.space = RenderState.EffectCoordinateSpace.CustomSpace;
                this.inputtx = BaseTransform.getScaleInstance(d5, d6);
                this.resulttx = baseTransform.copy().deriveWithScale(1.0 / d5, 1.0 / d6, 1.0);
                this.samplevectors = new float[]{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};
            } else {
                this.space = RenderState.EffectCoordinateSpace.RenderSpace;
                this.inputtx = baseTransform;
                this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
                this.samplevectors = new float[]{(float)(d / d5), (float)(d3 / d5), (float)(d2 / d6), (float)(d4 / d6), 0.0f, 0.0f};
            }
        }
    }

    public GaussianRenderState(float f, float f2, float f3, BaseTransform baseTransform) {
        this.isShadow = false;
        this.spread = 0.0f;
        if (baseTransform == null) {
            baseTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        double d = baseTransform.getMxx();
        double d2 = baseTransform.getMxy();
        double d3 = baseTransform.getMyx();
        double d4 = baseTransform.getMyy();
        double d5 = d * (double)f2 + d2 * (double)f3;
        double d6 = d3 * (double)f2 + d4 * (double)f3;
        double d7 = Math.hypot(d5, d6);
        boolean bl = false;
        float f4 = (float)((double)f * d7);
        if (f4 < 0.00390625f) {
            this.inputRadiusX = 0.0f;
            this.inputRadiusY = 0.0f;
            this.spreadPass = 0.0f;
            this.space = RenderState.EffectCoordinateSpace.RenderSpace;
            this.inputtx = baseTransform;
            this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
            this.samplevectors = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        } else {
            if (f4 > MAX_RADIUS) {
                f4 = MAX_RADIUS;
                d7 = MAX_RADIUS / f;
                bl = true;
            }
            this.inputRadiusX = f4;
            this.inputRadiusY = 0.0f;
            this.spreadPass = 0.0f;
            if (bl) {
                BaseTransform baseTransform2;
                double d8 = d2 * (double)f2 - d * (double)f3;
                double d9 = d4 * (double)f2 - d3 * (double)f3;
                double d10 = Math.hypot(d8, d9);
                this.space = RenderState.EffectCoordinateSpace.CustomSpace;
                Affine2D affine2D = new Affine2D();
                affine2D.scale(d7, d10);
                affine2D.rotate(f2, -f3);
                try {
                    baseTransform2 = affine2D.createInverse();
                }
                catch (NoninvertibleTransformException noninvertibleTransformException) {
                    baseTransform2 = BaseTransform.IDENTITY_TRANSFORM;
                }
                this.inputtx = affine2D;
                this.resulttx = baseTransform.copy().deriveWithConcatenation(baseTransform2);
                this.samplevectors = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
            } else {
                this.space = RenderState.EffectCoordinateSpace.RenderSpace;
                this.inputtx = baseTransform;
                this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
                this.samplevectors = new float[]{(float)(d5 / d7), (float)(d6 / d7), 0.0f, 0.0f, 0.0f, 0.0f};
            }
        }
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

    @Override
    public Rectangle getInputClip(int n, Rectangle rectangle) {
        if (rectangle != null) {
            int n2;
            double d = this.samplevectors[0] * this.inputRadiusX;
            double d2 = this.samplevectors[1] * this.inputRadiusX;
            double d3 = this.samplevectors[2] * this.inputRadiusY;
            double d4 = this.samplevectors[3] * this.inputRadiusY;
            int n3 = (int)Math.ceil(d + d3);
            if ((n3 | (n2 = (int)Math.ceil(d2 + d4))) != 0) {
                rectangle = new Rectangle(rectangle);
                rectangle.grow(n3, n2);
            }
        }
        return rectangle;
    }

    @Override
    public ImageData validatePassInput(ImageData imageData, int n) {
        this.validatedPass = n;
        Filterable filterable = imageData.getUntransformedImage();
        BaseTransform baseTransform = imageData.getTransform();
        float f = n == 0 ? this.inputRadiusX : this.inputRadiusY;
        int n2 = n * 2;
        if (baseTransform.isTranslateOrIdentity()) {
            this.passRadius = f;
            this.samplevectors[4] = this.samplevectors[n2];
            this.samplevectors[5] = this.samplevectors[n2 + 1];
            this.passType = this.validatedPass == 0 ? (GaussianRenderState.nearOne(this.samplevectors[4], filterable.getPhysicalWidth()) && GaussianRenderState.nearZero(this.samplevectors[5], filterable.getPhysicalWidth()) ? LinearConvolveRenderState.PassType.HORIZONTAL_CENTERED : LinearConvolveRenderState.PassType.GENERAL_VECTOR) : (GaussianRenderState.nearZero(this.samplevectors[4], filterable.getPhysicalHeight()) && GaussianRenderState.nearOne(this.samplevectors[5], filterable.getPhysicalHeight()) ? LinearConvolveRenderState.PassType.VERTICAL_CENTERED : LinearConvolveRenderState.PassType.GENERAL_VECTOR);
        } else {
            this.passType = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
            try {
                baseTransform.inverseDeltaTransform(this.samplevectors, n2, this.samplevectors, 4, 1);
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                this.passRadius = 0.0f;
                this.samplevectors[5] = 0.0f;
                this.samplevectors[4] = 0.0f;
                return imageData;
            }
            double d = Math.hypot(this.samplevectors[4], this.samplevectors[5]);
            float f2 = (float)((double)f * d);
            if (f2 > MAX_RADIUS) {
                f2 = MAX_RADIUS;
                d = MAX_RADIUS / f;
            }
            this.passRadius = f2;
            this.samplevectors[4] = (float)((double)this.samplevectors[4] / d);
            this.samplevectors[5] = (float)((double)this.samplevectors[5] / d);
        }
        this.samplevectors[4] = this.samplevectors[4] / (float)filterable.getPhysicalWidth();
        this.samplevectors[5] = this.samplevectors[5] / (float)filterable.getPhysicalHeight();
        return imageData;
    }

    @Override
    public Rectangle getPassResultBounds(Rectangle rectangle, Rectangle rectangle2) {
        double d = this.validatedPass == 0 ? (double)this.inputRadiusX : (double)this.inputRadiusY;
        int n = this.validatedPass * 2;
        double d2 = (double)this.samplevectors[n + 0] * d;
        double d3 = (double)this.samplevectors[n + 1] * d;
        int n2 = (int)Math.ceil(Math.abs(d2));
        int n3 = (int)Math.ceil(Math.abs(d3));
        Rectangle rectangle3 = new Rectangle(rectangle);
        rectangle3.grow(n2, n3);
        if (rectangle2 != null) {
            if (this.validatedPass == 0) {
                d2 = (double)this.samplevectors[2] * d;
                d3 = (double)this.samplevectors[3] * d;
                n2 = (int)Math.ceil(Math.abs(d2));
                if ((n2 | (n3 = (int)Math.ceil(Math.abs(d3)))) != 0) {
                    rectangle2 = new Rectangle(rectangle2);
                    rectangle2.grow(n2, n3);
                }
            }
            rectangle3.intersectWith(rectangle2);
        }
        return rectangle3;
    }

    @Override
    public LinearConvolveRenderState.PassType getPassType() {
        return this.passType;
    }

    @Override
    public float[] getPassVector() {
        float f = this.samplevectors[4];
        float f2 = this.samplevectors[5];
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

    @Override
    public int getInputKernelSize(int n) {
        return 1 + 2 * (int)Math.ceil(n == 0 ? (double)this.inputRadiusX : (double)this.inputRadiusY);
    }

    @Override
    public int getPassKernelSize() {
        return 1 + 2 * (int)Math.ceil(this.passRadius);
    }

    @Override
    public boolean isNop() {
        if (this.isShadow) {
            return false;
        }
        return this.inputRadiusX < 0.00390625f && this.inputRadiusY < 0.00390625f;
    }

    @Override
    public boolean isPassNop() {
        if (this.isShadow && this.validatedPass == 1) {
            return false;
        }
        return this.passRadius < 0.00390625f;
    }

    private void validateWeights() {
        float f;
        float f2 = this.passRadius;
        float f3 = f = (float)this.validatedPass == this.spreadPass ? this.spread : 0.0f;
        if (this.weights == null || this.weightsValidRadius != f2 || this.weightsValidSpread != f) {
            this.weights = GaussianRenderState.getGaussianWeights(this.weights, (int)Math.ceil(f2), f2, f);
            this.weightsValidRadius = f2;
            this.weightsValidSpread = f;
        }
    }
}

