package dev.tenacity.util.render.animation.impl;

import dev.tenacity.util.render.animation.AbstractAnimation;
import dev.tenacity.util.render.animation.AnimationDirection;

public final class EaseBackInAnimation extends AbstractAnimation {

    private final float easeAmount;

    public EaseBackInAnimation(final int duration, final double endPoint, final float easeAmount, final AnimationDirection direction) {
        super(duration, endPoint, direction);
        this.easeAmount = easeAmount;
    }

    @Override
    protected boolean correctOutput() {
        return true;
    }

    @Override
    protected double getEquation(final double x) {
        return Math.max(0, 1 + (easeAmount + 1) * Math.pow(x - 1, 3) + easeAmount * Math.pow(x - 1, 2));
    }
}
