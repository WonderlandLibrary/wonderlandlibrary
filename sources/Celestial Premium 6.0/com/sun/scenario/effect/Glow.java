/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.Point2D;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Crop;
import com.sun.scenario.effect.DelegateEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.GaussianBlur;

public class Glow
extends DelegateEffect {
    private final GaussianBlur blur;
    private final Blend blend;

    public Glow() {
        this(DefaultInput);
    }

    public Glow(Effect effect) {
        super(effect);
        this.blur = new GaussianBlur(10.0f, effect);
        Crop crop = new Crop(this.blur, effect);
        this.blend = new Blend(Blend.Mode.ADD, effect, crop);
        this.blend.setOpacity(0.3f);
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
        this.blur.setInput(effect);
        this.blend.setBottomInput(effect);
    }

    public float getLevel() {
        return this.blend.getOpacity();
    }

    public void setLevel(float f) {
        float f2 = this.blend.getOpacity();
        this.blend.setOpacity(f);
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

