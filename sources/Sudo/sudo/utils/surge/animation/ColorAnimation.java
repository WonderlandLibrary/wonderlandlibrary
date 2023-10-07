package sudo.utils.surge.animation;

import java.awt.*;
import java.util.function.Supplier;

/**
 * @author Surge
 * @since 22/10/2022
 */
public class ColorAnimation extends Animation {

    // The Color we want to transition from
    public Color from;

    // The Color we want to transition to
    public Color to;

    /**
     * Constructor that takes two suppliers
     * @param from The Color to transition from
     * @param to The Color to transition to
     * @param length The length of the animation
     * @param initialState The initial state of the animation (where it should start)
     * @param easing Which easing method to use
     */
    public ColorAnimation(Color from, Color to, Supplier<Float> length, boolean initialState, Supplier<Easing> easing) {
        super(length, initialState, easing);

        this.from = from;
        this.to = to;
    }

    /**
     * Constructor that does not take suppliers as parameters
     * @param from The Color to transition from
     * @param to The Color to transition to
     * @param length The length of the animation
     * @param initialState The initial state of the animation (where it should start)
     * @param easing Which easing method to use
     */
    public ColorAnimation(Color from, Color to, float length, boolean initialState, Easing easing) {
        this(from, to, () -> length, initialState, () -> easing);
    }

    /**
     * Constructor that only takes one supplier (length) and an immutable easing
     * @param from The Color to transition from
     * @param to The Color to transition to
     * @param length The length of the animation
     * @param initialState The initial state of the animation (where it should start)
     * @param easing Which easing method to use
     */
    public ColorAnimation(Color from, Color to, Supplier<Float> length, boolean initialState, Easing easing) {
        this(from, to, length, initialState, () -> easing);
    }

    /**
     * Constructor that only takes one supplier (easing) and an immutable length
     * @param from The Color to transition from
     * @param to The Color to transition to
     * @param length The length of the animation
     * @param initialState The initial state of the animation (where it should start)
     * @param easing Which easing method to use
     */
    public ColorAnimation(Color from, Color to, float length, boolean initialState, Supplier<Easing> easing) {
        this(from, to, () -> length, initialState, easing);
    }

    /**
     * Gets the transitioned Color
     * @return The transitioned Color
     */
    public Color getColor() {
        double factor = getAnimationFactor();

        return new Color(
                (int) (from.getRed() + (to.getRed() - from.getRed()) * factor),
                (int) (from.getGreen() + (to.getGreen() - from.getGreen()) * factor),
                (int) (from.getBlue() + (to.getBlue() - from.getBlue()) * factor),
                (int) (from.getAlpha() + (to.getAlpha() - from.getAlpha()) * factor)
        );
    }

}