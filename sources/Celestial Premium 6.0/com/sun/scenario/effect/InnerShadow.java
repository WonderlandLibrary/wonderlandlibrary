/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.AbstractShadow;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.DelegateEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.GaussianShadow;
import com.sun.scenario.effect.InvertMask;

public class InnerShadow
extends DelegateEffect {
    private final InvertMask invert;
    private AbstractShadow shadow;
    private final Blend blend;

    public InnerShadow() {
        this(DefaultInput, DefaultInput);
    }

    public InnerShadow(Effect effect) {
        this(effect, effect);
    }

    public InnerShadow(Effect effect, Effect effect2) {
        super(effect, effect2);
        this.invert = new InvertMask(10, effect);
        this.shadow = new GaussianShadow(10.0f, Color4f.BLACK, this.invert);
        this.blend = new Blend(Blend.Mode.SRC_ATOP, effect2, this.shadow);
    }

    public AbstractShadow.ShadowMode getShadowMode() {
        return this.shadow.getMode();
    }

    public void setShadowMode(AbstractShadow.ShadowMode shadowMode) {
        AbstractShadow.ShadowMode shadowMode2 = this.shadow.getMode();
        AbstractShadow abstractShadow = this.shadow.implFor(shadowMode);
        if (abstractShadow != this.shadow) {
            this.blend.setTopInput(abstractShadow);
        }
        this.shadow = abstractShadow;
    }

    @Override
    protected Effect getDelegate() {
        return this.blend;
    }

    @Override
    public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
        Effect effect2 = InnerShadow.getDefaultedInput(this.getContentInput(), effect);
        return effect2.getBounds(baseTransform, effect);
    }

    public final Effect getShadowSourceInput() {
        return this.invert.getInput();
    }

    public void setShadowSourceInput(Effect effect) {
        this.invert.setInput(effect);
    }

    public final Effect getContentInput() {
        return this.blend.getBottomInput();
    }

    public void setContentInput(Effect effect) {
        this.blend.setBottomInput(effect);
    }

    public float getRadius() {
        return this.shadow.getGaussianRadius();
    }

    public void setRadius(float f) {
        float f2 = this.shadow.getGaussianRadius();
        this.invert.setPad((int)Math.ceil(f));
        this.shadow.setGaussianRadius(f);
    }

    public float getGaussianRadius() {
        return this.shadow.getGaussianRadius();
    }

    public float getGaussianWidth() {
        return this.shadow.getGaussianWidth();
    }

    public float getGaussianHeight() {
        return this.shadow.getGaussianHeight();
    }

    public void setGaussianRadius(float f) {
        this.setRadius(f);
    }

    public void setGaussianWidth(float f) {
        float f2 = this.shadow.getGaussianWidth();
        float f3 = (Math.max(f, this.shadow.getGaussianHeight()) - 1.0f) / 2.0f;
        this.invert.setPad((int)Math.ceil(f3));
        this.shadow.setGaussianWidth(f);
    }

    public void setGaussianHeight(float f) {
        float f2 = this.shadow.getGaussianHeight();
        float f3 = (Math.max(this.shadow.getGaussianWidth(), f) - 1.0f) / 2.0f;
        this.invert.setPad((int)Math.ceil(f3));
        this.shadow.setGaussianHeight(f);
    }

    public float getChoke() {
        return this.shadow.getSpread();
    }

    public void setChoke(float f) {
        float f2 = this.shadow.getSpread();
        this.shadow.setSpread(f);
    }

    public Color4f getColor() {
        return this.shadow.getColor();
    }

    public void setColor(Color4f color4f) {
        Color4f color4f2 = this.shadow.getColor();
        this.shadow.setColor(color4f);
    }

    public int getOffsetX() {
        return this.invert.getOffsetX();
    }

    public void setOffsetX(int n) {
        int n2 = this.invert.getOffsetX();
        this.invert.setOffsetX(n);
    }

    public int getOffsetY() {
        return this.invert.getOffsetY();
    }

    public void setOffsetY(int n) {
        int n2 = this.invert.getOffsetY();
        this.invert.setOffsetY(n);
    }

    @Override
    public Point2D transform(Point2D point2D, Effect effect) {
        return this.getDefaultedInput(1, effect).transform(point2D, effect);
    }

    @Override
    public Point2D untransform(Point2D point2D, Effect effect) {
        return this.getDefaultedInput(1, effect).untransform(point2D, effect);
    }
}

