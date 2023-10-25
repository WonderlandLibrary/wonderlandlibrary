/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.LinearConvolveCoreEffect;

public abstract class AbstractShadow
extends LinearConvolveCoreEffect {
    public AbstractShadow(Effect effect) {
        super(effect);
    }

    public abstract ShadowMode getMode();

    public abstract AbstractShadow implFor(ShadowMode var1);

    public abstract float getGaussianRadius();

    public abstract void setGaussianRadius(float var1);

    public abstract float getGaussianWidth();

    public abstract void setGaussianWidth(float var1);

    public abstract float getGaussianHeight();

    public abstract void setGaussianHeight(float var1);

    public abstract float getSpread();

    public abstract void setSpread(float var1);

    public abstract Color4f getColor();

    public abstract void setColor(Color4f var1);

    public abstract Effect getInput();

    public abstract void setInput(Effect var1);

    public static final class ShadowMode
    extends Enum<ShadowMode> {
        public static final /* enum */ ShadowMode ONE_PASS_BOX = new ShadowMode();
        public static final /* enum */ ShadowMode TWO_PASS_BOX = new ShadowMode();
        public static final /* enum */ ShadowMode THREE_PASS_BOX = new ShadowMode();
        public static final /* enum */ ShadowMode GAUSSIAN = new ShadowMode();
        private static final /* synthetic */ ShadowMode[] $VALUES;

        public static ShadowMode[] values() {
            return (ShadowMode[])$VALUES.clone();
        }

        public static ShadowMode valueOf(String string) {
            return Enum.valueOf(ShadowMode.class, string);
        }

        private static /* synthetic */ ShadowMode[] $values() {
            return new ShadowMode[]{ONE_PASS_BOX, TWO_PASS_BOX, THREE_PASS_BOX, GAUSSIAN};
        }

        static {
            $VALUES = ShadowMode.$values();
        }
    }
}

