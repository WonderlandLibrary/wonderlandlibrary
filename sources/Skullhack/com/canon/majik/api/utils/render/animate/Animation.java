package com.canon.majik.api.utils.render.animate;

import java.util.function.Supplier;

public class Animation {

    private final Supplier<Float> length;
    private final Supplier<Easing> easing;
    private long lastMillis = 0L;
    private final boolean initialState;
    private boolean state = false;

    public Animation(Supplier<Float> length, boolean initialState, Supplier<Easing> easing) {
        this.length = length;
        this.initialState = initialState;
        setState(initialState);
        this.easing = easing;
    }

    public Animation(float length, boolean initialState, Easing easing) {
        this(() -> length, initialState, () -> easing);
    }

    public Animation(Supplier<Float> length, boolean initialState, Easing easing) {
        this(length, initialState, () -> easing);
    }

    public Animation(float length, boolean initialState, Supplier<Easing> easing) {
        this(() -> length, initialState, easing);
    }

    public double getAnimationFactor() {
        return easing.get().ease(getLinearFactor());
    }

    public void resetToDefault() {
        state = initialState;

        lastMillis = (long) (initialState ? System.currentTimeMillis() - ((1 - getLinearFactor()) * length.get()) : System.currentTimeMillis() - (getLinearFactor() * length.get()));
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean in) {
        lastMillis = (long) (!in ? System.currentTimeMillis() - ((1 - getLinearFactor()) * length.get()) : System.currentTimeMillis() - (getLinearFactor() * length.get()));

        this.state = in;
    }

    public double getLinearFactor() {
        return state ? clamp(((System.currentTimeMillis() - lastMillis) / length.get())) : clamp((1 - (System.currentTimeMillis() - lastMillis) / length.get()));
    }

    private double clamp(double in) {
        return in < 0 ? 0 : Math.min(in, 1);
    }
}
