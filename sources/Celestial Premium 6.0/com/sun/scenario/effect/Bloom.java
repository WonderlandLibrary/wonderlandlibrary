/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.Point2D;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Brightpass;
import com.sun.scenario.effect.Crop;
import com.sun.scenario.effect.DelegateEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.GaussianBlur;

public class Bloom
extends DelegateEffect {
    private final Brightpass brightpass;
    private final GaussianBlur blur;
    private final Blend blend;

    public Bloom() {
        this(DefaultInput);
    }

    public Bloom(Effect effect) {
        super(effect);
        this.brightpass = new Brightpass(effect);
        this.blur = new GaussianBlur(10.0f, (Effect)this.brightpass);
        Crop crop = new Crop(this.blur, effect);
        this.blend = new Blend(Blend.Mode.ADD, effect, crop);
    }

    @Override
    protected Effect getDelegate() {
        return this.blend;
    }

    public final Effect getInput() {
        return this.getInputs().get(0);
    }

    public void setInput(Effect effect) {
        this.setInput(0, effect);
        this.brightpass.setInput(effect);
        this.blend.setBottomInput(effect);
    }

    public float getThreshold() {
        return this.brightpass.getThreshold();
    }

    public void setThreshold(float f) {
        float f2 = this.brightpass.getThreshold();
        this.brightpass.setThreshold(f);
    }

    @Override
    public Point2D transform(Point2D point2D, Effect effect) {
        return this.getDefaultedInput(0, effect).transform(point2D, effect);
    }

    @Override
    public Point2D untransform(Point2D point2D, Effect effect) {
        return this.getDefaultedInput(0, effect).untransform(point2D, effect);
    }
}

