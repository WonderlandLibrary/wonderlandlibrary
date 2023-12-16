// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.animations;

public class Easings
{
    public static float linear(final float x) {
        return x;
    }
    
    public static float easeInSine(final float x) {
        return (float)(1.0 - Math.cos(x * 3.141592653589793 / 2.0));
    }
    
    public static float easeOutSine(final float x) {
        return (float)Math.sin(x * 3.141592653589793 / 2.0);
    }
    
    public static float easeInOutSine(final float x) {
        return (float)(-(Math.cos(3.141592653589793 * x) - 1.0) / 2.0);
    }
    
    public static float easeInCubic(final float x) {
        return x * x * x;
    }
    
    public static float easeOutCubic(final float x) {
        return (float)(1.0 - Math.pow(1.0f - x, 3.0));
    }
    
    public static float easeInOutCubic(final float x) {
        return (float)((x < 0.5) ? (4.0f * x * x * x) : (1.0 - Math.pow(-2.0f * x + 2.0f, 3.0) / 2.0));
    }
    
    public static float easeInQuint(final float x) {
        return x * x * x * x * x;
    }
    
    public static float easeOutQuint(final float x) {
        return (float)(1.0 - Math.pow(1.0f - x, 5.0));
    }
    
    public static float easeInOutQuint(final float x) {
        return (float)((x < 0.5) ? (16.0f * x * x * x * x * x) : (1.0 - Math.pow(-2.0f * x + 2.0f, 5.0) / 2.0));
    }
    
    public static float easeInCirc(final float x) {
        return (float)(1.0 - Math.sqrt(1.0 - Math.pow(x, 2.0)));
    }
    
    public static float easeOutCirc(final float x) {
        return (float)Math.sqrt(1.0 - Math.pow(x - 1.0f, 2.0));
    }
    
    public static float easeInOutCirc(final float x) {
        return (float)((x < 0.5) ? ((1.0 - Math.sqrt(1.0 - Math.pow(2.0f * x, 2.0))) / 2.0) : ((Math.sqrt(1.0 - Math.pow(-2.0f * x + 2.0f, 2.0)) + 1.0) / 2.0));
    }
    
    public static float easeInElastic(final float x) {
        if (x <= 0.0f) {
            return 0.0f;
        }
        if (x >= 1.0f) {
            return 1.0f;
        }
        return (float)(-Math.pow(2.0, 10.0f * x - 10.0f) * Math.sin((x * 10.0f - 10.75) * 2.0943951023931953));
    }
    
    public static float easeOutElastic(final float x) {
        if (x <= 0.0f) {
            return 0.0f;
        }
        if (x >= 1.0f) {
            return 1.0f;
        }
        return (float)(Math.pow(2.0, -10.0f * x) * Math.sin((x * 10.0f - 0.75) * 2.0943951023931953) + 1.0);
    }
    
    public static float easeInOutElastic(final float x) {
        if (x <= 0.0f) {
            return 0.0f;
        }
        if (x >= 1.0f) {
            return 1.0f;
        }
        return (float)((x < 0.5) ? (-(Math.pow(2.0, 20.0f * x - 10.0f) * Math.sin((20.0f * x - 11.125) * 1.3962634015954636)) / 2.0) : (Math.pow(2.0, -20.0f * x + 10.0f) * Math.sin((20.0f * x - 11.125) * 1.3962634015954636) / 2.0 + 1.0));
    }
    
    public static float easeInQuad(final float x) {
        return x * x;
    }
    
    public static float easeOutQuad(final float x) {
        return 1.0f - (1.0f - x) * (1.0f - x);
    }
    
    public static float easeInOutQuad(final float x) {
        return (float)((x < 0.5) ? (2.0f * x * x) : (1.0 - Math.pow(-2.0f * x + 2.0f, 2.0) / 2.0));
    }
    
    public static float easeInQuart(final float x) {
        return x * x * x * x;
    }
    
    public static float easeOutQuart(final float x) {
        return (float)(1.0 - Math.pow(1.0f - x, 4.0));
    }
    
    public static float easeInOutQuart(final float x) {
        return (float)((x < 0.5) ? (8.0f * x * x * x * x) : (1.0 - Math.pow(-2.0f * x + 2.0f, 4.0) / 2.0));
    }
    
    public static float easeInExponential(final float x) {
        return (float)((x == 0.0f) ? 0.0 : Math.pow(2.0, 10.0f * x - 10.0f));
    }
    
    public static float easeOutExponential(final float x) {
        return (float)((x == 1.0f) ? 1.0 : (1.0 - Math.pow(2.0, -10.0f * x)));
    }
    
    public static float easeInOutExponential(final float x) {
        return (float)((x == 0.0f) ? 0.0 : ((x == 1.0f) ? 1.0 : ((x < 0.5) ? (Math.pow(2.0, 20.0f * x - 10.0f) / 2.0) : ((2.0 - Math.pow(2.0, -20.0f * x + 10.0f)) / 2.0))));
    }
    
    public static float easeInBack(final float x) {
        final float c1 = 1.70158f;
        return (c1 + 1.0f) * x * x * x - c1 * x * x;
    }
    
    public static float easeOutBack(final float x) {
        final float c1 = 1.70158f;
        return (float)(1.0 + (c1 + 1.0f) * Math.pow(x - 1.0f, 3.0) + c1 * Math.pow(x - 1.0f, 2.0));
    }
    
    public static float easeInOutBack(final float x) {
        final float c1 = 1.70158f;
        final float c2 = c1 * 1.525f;
        return (float)((x < 0.5) ? (Math.pow(2.0f * x, 2.0) * ((c2 + 1.0f) * 2.0f * x - c2) / 2.0) : ((Math.pow(2.0f * x - 2.0f, 2.0) * ((c2 + 1.0f) * (x * 2.0f - 2.0f) + c2) + 2.0) / 2.0));
    }
    
    public static float easeInBounce(final float x) {
        return 1.0f - easeOutBounce(1.0f - x);
    }
    
    public static float easeOutBounce(float x) {
        final float n1 = 7.5625f;
        final float d1 = 2.75f;
        if (x < 1.0f / d1) {
            return n1 * x * x;
        }
        if (x < 2.0f / d1) {
            return (float)(n1 * (x -= (float)(1.5 / d1)) * x + 0.75);
        }
        if (x < 2.5 / d1) {
            return (float)(n1 * (x -= (float)(2.25 / d1)) * x + 0.9375);
        }
        return (float)(n1 * (x -= (float)(2.625 / d1)) * x + 0.984375);
    }
    
    public static float easeInOutBounce(final float x) {
        return (x < 0.5) ? ((1.0f - easeOutBounce(1.0f - 2.0f * x)) / 2.0f) : ((1.0f + easeOutBounce(2.0f * x - 1.0f)) / 2.0f);
    }
    
    public static float getEasingValue(final float x, final Easing easing) {
        if (x <= 0.0f) {
            return 0.0f;
        }
        if (x >= 1.0f) {
            return 1.0f;
        }
        switch (easing) {
            case LINEAR: {
                return linear(x);
            }
            case EASE_IN_SINE: {
                return easeInSine(x);
            }
            case EASE_OUT_SINE: {
                return easeOutSine(x);
            }
            case EASE_IN_OUT_SINE: {
                return easeInOutSine(x);
            }
            case EASE_IN_CUBIC: {
                return easeInCubic(x);
            }
            case EASE_OUT_CUBIC: {
                return easeOutCubic(x);
            }
            case EASE_IN_OUT_CUBIC: {
                return easeInOutCubic(x);
            }
            case EASE_IN_QUINT: {
                return easeInQuint(x);
            }
            case EASE_OUT_QUINT: {
                return easeOutQuint(x);
            }
            case EASE_IN_OUT_QUINT: {
                return easeInOutQuint(x);
            }
            case EASE_IN_CIRC: {
                return easeInCirc(x);
            }
            case EASE_OUT_CIRC: {
                return easeOutCirc(x);
            }
            case EASE_IN_OUT_CIRC: {
                return easeInOutCirc(x);
            }
            case EASE_IN_ELASTIC: {
                return easeInElastic(x);
            }
            case EASE_OUT_ELASTIC: {
                return easeOutElastic(x);
            }
            case EASE_IN_OUT_ELASTIC: {
                return easeInOutElastic(x);
            }
            case EASE_IN_QUAD: {
                return easeInQuad(x);
            }
            case EASE_OUT_QUAD: {
                return easeOutQuad(x);
            }
            case EASE_IN_OUT_QUAD: {
                return easeInOutQuad(x);
            }
            case EASE_IN_QUART: {
                return easeInQuart(x);
            }
            case EASE_OUT_QUART: {
                return easeOutQuart(x);
            }
            case EASE_IN_OUT_QUART: {
                return easeInOutQuart(x);
            }
            case EASE_IN_EXPONENTIAL: {
                return easeInExponential(x);
            }
            case EASE_OUT_EXPONENTIAL: {
                return easeOutExponential(x);
            }
            case EASE_IN_OUT_EXPONENTIAL: {
                return easeInOutExponential(x);
            }
            case EASE_IN_BACK: {
                return easeInBack(x);
            }
            case EASE_OUT_BACK: {
                return easeOutBack(x);
            }
            case EASE_IN_OUT_BACK: {
                return easeInOutBack(x);
            }
            case EASE_IN_BOUNCE: {
                return easeInBounce(x);
            }
            case EASE_OUT_BOUNCE: {
                return easeOutBounce(x);
            }
            case EASE_IN_OUT_BOUNCE: {
                return easeInOutBounce(x);
            }
            default: {
                System.err.println("Unkown Easing type " + easing.name());
                return x;
            }
        }
    }
}
