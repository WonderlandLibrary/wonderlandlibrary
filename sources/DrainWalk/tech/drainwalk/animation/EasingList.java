package tech.drainwalk.animation;

public class EasingList {
    public static final double c1 = 1.70158;
    public static final double c2 = 2.5949095;
    public static final double c3 = 2.70158;
    public static final double c4 = 2.0943951023931953;
    public static final double c5 = 1.3962634015954636;
    public static final Easing SINE_IN = (value) -> (float) (1.0 - Math.cos(value * Math.PI / 2.0));
    public static final Easing SINE_OUT = (value) -> (float) Math.sin(value * Math.PI / 2.0);
    public static final Easing SINE_BOTH = (value) -> (float) (-(Math.cos(Math.PI * value) - 1.0) / 2.0);
    public static final Easing CIRC_IN = (value) -> (float) (1.0 - Math.sqrt(1.0 - Math.pow(value, 2.0)));
    public static final Easing CIRC_OUT = (value) -> (float) Math.sqrt(1.0 - Math.pow(value - 1.0, 2.0));
    public static final Easing CIRC_BOTH = (value) -> (float) (value < 0.5 ? (1.0 - Math.sqrt(1.0 - Math.pow(2.0 * value, 2.0))) / 2.0 : (Math.sqrt(1.0 - Math.pow(-2.0 * value + 2.0, 2.0)) + 1.0) / 2.0);
    public static final Easing ELASTIC_IN = (value) -> value != 0.0 && value != 1.0 ? (float) (Math.pow(-2.0, 10.0 * value - 10.0) * Math.sin((value * 10.0 - 10.75) * 2.0943951023931953)) : value;
    public static final Easing ELASTIC_OUT = (value) -> value != 0.0 && value != 1.0 ? (float) (Math.pow(2.0, -10.0 * value) * Math.sin((value * 10.0 - 0.75) * 2.0943951023931953) + 1.0) : value;
    public static final Easing ELASTIC_BOTH = (value) -> {
        if (value != 0.0 && value != 1.0) {
            return (float) (value < 0.5 ? -(Math.pow(2.0, 20.0 * value - 10.0) * Math.sin((20.0 * value - 11.125) * 1.3962634015954636)) / 2.0 : Math.pow(2.0, -20.0 * value + 10.0) * Math.sin((20.0 * value - 11.125) * 1.3962634015954636) / 2.0 + 1.0);
        } else {
            return value;
        }
    };
    public static final Easing EXPO_IN = (value) -> value != 0.0 ? (float) Math.pow(2.0, 10.0 * value - 10.0) : value;
    public static final Easing EXPO_OUT = (value) -> value != 1.0 ? (float) (1.0 - Math.pow(2.0, -10.0 * value)) : value;
    public static final Easing EXPO_BOTH = (value) -> {
        if (value != 0.0 && value != 1.0) {
            return (float) (value < 0.5 ? Math.pow(2.0, 20.0 * value - 10.0) / 2.0 : (2.0 - Math.pow(2.0, -20.0 * value + 10.0)) / 2.0);
        } else {
            return value;
        }
    };
    public static final Easing BACK_IN = (value) -> (float) (2.70158 * Math.pow(value, 3.0) - 1.70158 * Math.pow(value, 2.0));
    public static final Easing BACK_OUT = (value) -> (float) (1.0 + 2.70158 * Math.pow(value - 1.0, 3.0) + 1.70158 * Math.pow(value - 1.0, 2.0));
    public static final Easing NONE = (value) -> value;
    public static final Easing BACK_BOTH = (value) -> (float) (value < 0.5 ? Math.pow(2.0 * value, 2.0) * (7.189819 * value - 2.5949095) / 2.0 : (Math.pow(2.0 * value - 2.0, 2.0) * (3.5949095 * (value * 2.0 - 2.0) + 2.5949095) + 2.0) / 2.0);
    public static final Easing BOUNCE_OUT = (value) -> {
        float n1 = 7.5625F;
        float d1 = 2.75F;
        if (value < 1.0 / d1) {
            return (float) (n1 * Math.pow(value, 2.0));
        } else if (value < 2.0 / d1) {
            return (float) (n1 * Math.pow(value - 1.5 / d1, 2.0) + 0.75);
        } else {
            return (float) (value < 2.5 / d1 ? n1 * Math.pow(value - 2.25 / d1, 2.0) + 0.9375 : n1 * Math.pow(value - 2.625 / d1, 2.0) + 0.984375);
        }
    };
    public static final Easing BOUNCE_IN = (value) -> (float) (1.0 - BOUNCE_OUT.ease((float) (1.0 - value)));
    public static final Easing BOUNCE_BOTH = (value) -> (float) (value < 0.5 ? (1.0 - BOUNCE_OUT.ease((float) (1.0 - 2.0 * value))) / 2.0 : (1.0 + BOUNCE_OUT.ease((float) (2.0 * value - 1.0))) / 2.0);

    public static final Easing QUINT_IN = (x) -> {
        return x < 0.5 ? 16 * x * x * x * x * x : (float) (1 - Math.pow(-2 * x + 2, 5) / 2);
    };


    @FunctionalInterface
    public interface Easing {
        float ease(float value);
    }
}
