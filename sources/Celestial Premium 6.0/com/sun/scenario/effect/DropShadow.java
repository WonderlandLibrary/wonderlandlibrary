/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.scenario.effect.AbstractShadow;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.DelegateEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.GaussianShadow;
import com.sun.scenario.effect.Merge;
import com.sun.scenario.effect.Offset;

public class DropShadow
extends DelegateEffect {
    private AbstractShadow shadow;
    private final Offset offset;
    private final Merge merge;

    public DropShadow() {
        this(DefaultInput, DefaultInput);
    }

    public DropShadow(Effect effect) {
        this(effect, effect);
    }

    public DropShadow(Effect effect, Effect effect2) {
        super(effect, effect2);
        this.shadow = new GaussianShadow(10.0f, Color4f.BLACK, effect);
        this.offset = new Offset(0, 0, this.shadow);
        this.merge = new Merge(this.offset, effect2);
    }

    public AbstractShadow.ShadowMode getShadowMode() {
        return this.shadow.getMode();
    }

    public void setShadowMode(AbstractShadow.ShadowMode shadowMode) {
        AbstractShadow.ShadowMode shadowMode2 = this.shadow.getMode();
        AbstractShadow abstractShadow = this.shadow.implFor(shadowMode);
        if (abstractShadow != this.shadow) {
            this.offset.setInput(abstractShadow);
        }
        this.shadow = abstractShadow;
    }

    @Override
    protected Effect getDelegate() {
        return this.merge;
    }

    public final Effect getShadowSourceInput() {
        return this.shadow.getInput();
    }

    public void setShadowSourceInput(Effect effect) {
        this.shadow.setInput(effect);
    }

    public final Effect getContentInput() {
        return this.merge.getTopInput();
    }

    public void setContentInput(Effect effect) {
        this.merge.setTopInput(effect);
    }

    public float getRadius() {
        return this.shadow.getGaussianRadius();
    }

    public void setRadius(float f) {
        float f2 = this.shadow.getGaussianRadius();
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
        this.shadow.setGaussianWidth(f);
    }

    public void setGaussianHeight(float f) {
        float f2 = this.shadow.getGaussianHeight();
        this.shadow.setGaussianHeight(f);
    }

    public float getSpread() {
        return this.shadow.getSpread();
    }

    public void setSpread(float f) {
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
        return this.offset.getX();
    }

    public void setOffsetX(int n) {
        int n2 = this.offset.getX();
        this.offset.setX(n);
    }

    public int getOffsetY() {
        return this.offset.getY();
    }

    public void setOffsetY(int n) {
        int n2 = this.offset.getY();
        this.offset.setY(n);
    }

    @Override
    public Effect.AccelType getAccelType(FilterContext filterContext) {
        return this.shadow.getAccelType(filterContext);
    }
}

