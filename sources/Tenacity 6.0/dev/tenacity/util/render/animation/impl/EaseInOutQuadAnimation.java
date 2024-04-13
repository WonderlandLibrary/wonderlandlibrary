package dev.tenacity.util.render.animation.impl;

import dev.tenacity.util.render.animation.AbstractAnimation;
import dev.tenacity.util.render.animation.AnimationDirection;

public class EaseInOutQuadAnimation extends AbstractAnimation {

    public EaseInOutQuadAnimation(final int duration, final double endPoint, final AnimationDirection direction) {
        super(duration, endPoint, direction);
    }

    @Override
    protected double getEquation(final double x) {
        return x < 0.5 ? 2 * Math.pow(x, 2) : 1 - Math.pow(-2 * x + 2, 2) / 2;
    }
}
