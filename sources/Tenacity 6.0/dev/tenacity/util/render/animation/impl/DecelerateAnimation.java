package dev.tenacity.util.render.animation.impl;

import dev.tenacity.util.render.animation.AbstractAnimation;
import dev.tenacity.util.render.animation.AnimationDirection;

public final class DecelerateAnimation extends AbstractAnimation {

    public DecelerateAnimation(final int duration, final double endPoint, final AnimationDirection direction) {
        super(duration, endPoint, direction);
    }

    @Override
    protected double getEquation(final double x) {
        return 1 - ((x - 1) * (x - 1));
    }
}
