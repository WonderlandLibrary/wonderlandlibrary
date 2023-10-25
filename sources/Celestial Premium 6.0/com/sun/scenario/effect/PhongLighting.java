/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.CoreEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.GaussianShadow;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.light.Light;

public class PhongLighting
extends CoreEffect<RenderState> {
    private float surfaceScale = 1.0f;
    private float diffuseConstant = 1.0f;
    private float specularConstant = 1.0f;
    private float specularExponent = 1.0f;
    private Light light;

    public PhongLighting(Light light) {
        this(light, new GaussianShadow(10.0f), DefaultInput);
    }

    public PhongLighting(Light light, Effect effect, Effect effect2) {
        super(effect, effect2);
        this.setLight(light);
    }

    public final Effect getBumpInput() {
        return this.getInputs().get(0);
    }

    public void setBumpInput(Effect effect) {
        this.setInput(0, effect);
    }

    public final Effect getContentInput() {
        return this.getInputs().get(1);
    }

    private Effect getContentInput(Effect effect) {
        return this.getDefaultedInput(1, effect);
    }

    public void setContentInput(Effect effect) {
        this.setInput(1, effect);
    }

    public Light getLight() {
        return this.light;
    }

    public void setLight(Light light) {
        if (light == null) {
            throw new IllegalArgumentException("Light must be non-null");
        }
        this.light = light;
        this.updatePeerKey("PhongLighting_" + light.getType().name());
    }

    public float getDiffuseConstant() {
        return this.diffuseConstant;
    }

    public void setDiffuseConstant(float f) {
        if (f < 0.0f || f > 2.0f) {
            throw new IllegalArgumentException("Diffuse constant must be in the range [0,2]");
        }
        float f2 = this.diffuseConstant;
        this.diffuseConstant = f;
    }

    public float getSpecularConstant() {
        return this.specularConstant;
    }

    public void setSpecularConstant(float f) {
        if (f < 0.0f || f > 2.0f) {
            throw new IllegalArgumentException("Specular constant must be in the range [0,2]");
        }
        float f2 = this.specularConstant;
        this.specularConstant = f;
    }

    public float getSpecularExponent() {
        return this.specularExponent;
    }

    public void setSpecularExponent(float f) {
        if (f < 0.0f || f > 40.0f) {
            throw new IllegalArgumentException("Specular exponent must be in the range [0,40]");
        }
        float f2 = this.specularExponent;
        this.specularExponent = f;
    }

    public float getSurfaceScale() {
        return this.surfaceScale;
    }

    public void setSurfaceScale(float f) {
        if (f < 0.0f || f > 10.0f) {
            throw new IllegalArgumentException("Surface scale must be in the range [0,10]");
        }
        float f2 = this.surfaceScale;
        this.surfaceScale = f;
    }

    @Override
    public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
        return this.getContentInput(effect).getBounds(baseTransform, effect);
    }

    @Override
    public Rectangle getResultBounds(BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        return super.getResultBounds(baseTransform, rectangle, arrimageData[1]);
    }

    @Override
    public Point2D transform(Point2D point2D, Effect effect) {
        return this.getContentInput(effect).transform(point2D, effect);
    }

    @Override
    public Point2D untransform(Point2D point2D, Effect effect) {
        return this.getContentInput(effect).untransform(point2D, effect);
    }

    @Override
    public RenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return new RenderState(){

            @Override
            public RenderState.EffectCoordinateSpace getEffectTransformSpace() {
                return RenderState.EffectCoordinateSpace.RenderSpace;
            }

            @Override
            public BaseTransform getInputTransform(BaseTransform baseTransform) {
                return baseTransform;
            }

            @Override
            public BaseTransform getResultTransform(BaseTransform baseTransform) {
                return BaseTransform.IDENTITY_TRANSFORM;
            }

            @Override
            public Rectangle getInputClip(int n, Rectangle rectangle) {
                if (n == 0 && rectangle != null) {
                    Rectangle rectangle2 = new Rectangle(rectangle);
                    rectangle2.grow(1, 1);
                    return rectangle2;
                }
                return rectangle;
            }
        };
    }

    @Override
    public boolean reducesOpaquePixels() {
        Effect effect = this.getContentInput();
        return effect != null && effect.reducesOpaquePixels();
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        Effect effect2 = this.getDefaultedInput(0, effect);
        DirtyRegionContainer dirtyRegionContainer = effect2.getDirtyRegions(effect, dirtyRegionPool);
        dirtyRegionContainer.grow(1, 1);
        Effect effect3 = this.getDefaultedInput(1, effect);
        DirtyRegionContainer dirtyRegionContainer2 = effect3.getDirtyRegions(effect, dirtyRegionPool);
        dirtyRegionContainer.merge(dirtyRegionContainer2);
        dirtyRegionPool.checkIn(dirtyRegionContainer2);
        return dirtyRegionContainer;
    }
}

