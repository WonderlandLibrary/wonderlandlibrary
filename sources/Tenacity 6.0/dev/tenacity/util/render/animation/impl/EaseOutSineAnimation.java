package dev.tenacity.util.render.animation.impl;

import dev.tenacity.util.render.animation.AbstractAnimation;
import dev.tenacity.util.render.animation.AnimationDirection;

public final class EaseOutSineAnimation extends AbstractAnimation {

    public EaseOutSineAnimation(final int duration, final double endPoint, final AnimationDirection direction) {
        super(duration, endPoint, direction);
    }

    @Override
    protected boolean correctOutput() {
        return true;
    }

    @Override
    protected double getEquation(final double x) {
        return Math.sin(x * (Math.PI / 2));
    }
}
