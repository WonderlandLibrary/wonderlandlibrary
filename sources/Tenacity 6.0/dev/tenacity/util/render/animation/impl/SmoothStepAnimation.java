package dev.tenacity.util.render.animation.impl;

import dev.tenacity.util.render.animation.AbstractAnimation;
import dev.tenacity.util.render.animation.AnimationDirection;

public final class SmoothStepAnimation extends AbstractAnimation {

    public SmoothStepAnimation(final int duration, final double endPoint, final AnimationDirection direction) {
        super(duration, endPoint, direction);
    }

    @Override
    protected double getEquation(final double x) {
        return -2 * Math.pow(x, 3) + (3 * Math.pow(x, 2));
    }
}
