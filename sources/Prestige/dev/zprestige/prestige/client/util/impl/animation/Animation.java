package dev.zprestige.prestige.client.util.impl.animation;

import java.util.function.Supplier;

//#SKIDDED
public class Animation {

    // The length of the animation
    private final Supplier<Float> length;

    // The easing method to use
    private final Supplier<Easing> easing;

    // The time since the last state change
    private long lastMillis = 0L;

    // The original state we set the animation to
    private final boolean initialState;

    // The current state of the animation
    private boolean state = false;

    /**
     * Constructor that takes two suppliers
     * @param length The length of the animation
     * @param initialState The initial state of the animation (where it should start)
     * @param easing Which easing method to use
     */
    public Animation(Supplier<Float> length, boolean initialState, Supplier<Easing> easing) {
        this.length = length;
        this.initialState = initialState;
        setState(initialState);
        this.easing = easing;
    }

    /**
     * Constructor that does not take suppliers as parameters
     * @param length The length of the animation
     * @param initialState The initial state of the animation (where it should start)
     * @param easing Which easing method to use
     */
    public Animation(float length, boolean initialState, Easing easing) {
        this(() -> length, initialState, () -> easing);
    }

    /**
     * Constructor that only takes one supplier (length) and an immutable easing
     * @param length The length of the animation
     * @param initialState The initial state of the animation (where it should start)
     * @param easing Which easing method to use
     */
    public Animation(Supplier<Float> length, boolean initialState, Easing easing) {
        this(length, initialState, () -> easing);
    }

    /**
     * Constructor that only takes one supplier (easing) and an immutable length
     * @param length The length of the animation
     * @param initialState The initial state of the animation (where it should start)
     * @param easing Which easing method to use
     */
    public Animation(float length, boolean initialState, Supplier<Easing> easing) {
        this(() -> length, initialState, easing);
    }

    /**
     * Gets how much the animation has progressed
     * @return A value between 0 and 1.
     */
    public float getAnimationFactor() {
        return (float) easing.get().ease(getLinearFactor());
    }

    /**
     * Resets an animation to it's original state.
     */
    public void resetToDefault() {
        state = initialState;

        lastMillis = (long) (initialState ? System.currentTimeMillis() - ((1 - getLinearFactor()) * length.get()) : System.currentTimeMillis() - (getLinearFactor() * length.get()));
    }

    /**
     * Gets the current state of the animation
     * @return The state of the animation
     */
    public boolean getState() {
        return state;
    }

    /**
     * Sets the state of the animation
     * @param in The new state of the animation
     */
    public void setState(boolean in) {
        lastMillis = (long) (!in ? System.currentTimeMillis() - ((1 - getLinearFactor()) * length.get()) : System.currentTimeMillis() - (getLinearFactor() * length.get()));

        this.state = in;
    }

    /**
     * Gets the linear animation factor. This method ignores the <code>{@link Easing#ease(double)}</code> methods found in each
     * Easing element.
     * @return The animation factor without the given easing being applied
     */
    public double getLinearFactor() {
        return state ? clamp(((System.currentTimeMillis() - lastMillis) / length.get())) : clamp((1 - (System.currentTimeMillis() - lastMillis) / length.get()));
    }

    /**
     * Internal use only! Clamps the given value in between 0 and 1
     * @param in The given value.
     * @return The clamped value.
     */
    private double clamp(double in) {
        return in < 0 ? 0 : Math.min(in, 1);
    }

}