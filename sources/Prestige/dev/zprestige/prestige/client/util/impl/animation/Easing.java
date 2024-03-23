package dev.zprestige.prestige.client.util.impl.animation;

//#SKIDDED
public enum Easing {

    /**
     * No easing.
     */
    LINEAR {
        @Override
        public double ease(double factor) {
            return factor;
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInSine">The easing on easings.net</a>
     */
    SINE_IN {
        @Override
        public double ease(double factor) {
            return 1 - Math.cos((factor * Math.PI) / 2);
        }
    },

    /**
     * @see <a href="https://easings.net/#easeOutSine">The easing on easings.net</a>
     */
    SINE_OUT {
        @Override
        public double ease(double factor) {
            return Math.sin((factor * Math.PI) / 2);
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInOutSine">The easing on easings.net</a>
     */
    SINE_IN_OUT {
        @Override
        public double ease(double factor) {
            return -(Math.cos(Math.PI * factor) - 1) / 2;
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInCubic">The easing on easings.net</a>
     */
    CUBIC_IN {
        @Override
        public double ease(double factor) {
            return Math.pow(factor, 3);
        }
    },

    /**
     * @see <a href="https://easings.net/#easeOutCubic">The easing on easings.net</a>
     */
    CUBIC_OUT {
        @Override
        public double ease(double factor) {
            return 1 - Math.pow(1 - factor, 3);
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInOutCubic">The easing on easings.net</a>
     */
    CUBIC_IN_OUT {
        @Override
        public double ease(double factor) {
            return factor < 0.5 ? 4 * Math.pow(factor, 3) : 1 - Math.pow(-2 * factor + 2, 3) / 2;
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInQuad">The easing on easings.net</a>
     */
    QUAD_IN {
        @Override
        public double ease(double factor) {
            return Math.pow(factor, 2);
        }
    },

    /**
     * @see <a href="https://easings.net/#easeOutQuad">The easing on easings.net</a>
     */
    QUAD_OUT {
        @Override
        public double ease(double factor) {
            return 1 - (1 - factor) * (1 - factor);
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInOutQuad">The easing on easings.net</a>
     */
    QUAD_IN_OUT {
        @Override
        public double ease(double factor) {
            return factor < 0.5 ? 8 * Math.pow(factor, 4) : 1 - Math.pow(-2 * factor + 2, 4) / 2;
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInQuart">The easing on easings.net</a>
     */
    QUART_IN {
        @Override
        public double ease(double factor) {
            return Math.pow(factor, 4);
        }
    },

    /**
     * @see <a href="https://easings.net/#easeOutQuart">The easing on easings.net</a>
     */
    QUART_OUT {
        @Override
        public double ease(double factor) {
            return 1 - Math.pow(1 - factor, 4);
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInOutQuart">The easing on easings.net</a>
     */
    QUART_IN_OUT {
        @Override
        public double ease(double factor) {
            return factor < 0.5 ? 8 * Math.pow(factor, 4) : 1 - Math.pow(-2 * factor + 2, 4) / 2;
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInQuint">The easing on easings.net</a>
     */
    QUINT_IN {
        @Override
        public double ease(double factor) {
            return Math.pow(factor, 5);
        }
    },

    /**
     * @see <a href="https://easings.net/#easeOutQuint">The easing on easings.net</a>
     */
    QUINT_OUT {
        @Override
        public double ease(double factor) {
            return 1 - Math.pow(1 - factor, 5);
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInOutQuint">The easing on easings.net</a>
     */
    QUINT_IN_OUT {
        @Override
        public double ease(double factor) {
            return factor < 0.5 ? 16 * Math.pow(factor, 5) : 1 - Math.pow(-2 * factor + 2, 5) / 2;
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInCirc">The easing on easings.net</a>
     */
    CIRC_IN {
        @Override
        public double ease(double factor) {
            return 1 - Math.sqrt(1 - Math.pow(factor, 2));
        }
    },

    /**
     * @see <a href="https://easings.net/#easeOutCirc">The easing on easings.net</a>
     */
    CIRC_OUT {
        @Override
        public double ease(double factor) {
            return Math.sqrt(1 - Math.pow(factor - 1, 2));
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInOutCirc">The easing on easings.net</a>
     */
    CIRC_IN_OUT {
        @Override
        public double ease(double factor) {
            return factor < 0.5 ? (1 - Math.sqrt(1 - Math.pow(2 * factor, 2))) / 2 :  (Math.sqrt(1 - Math.pow(-2 * factor + 2, 2)) + 1) / 2;
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInExpo">The easing on easings.net</a>
     */
    EXPO_IN {
        @Override
        public double ease(double factor) {
            return Math.min(0, Math.pow(2, 10 * factor - 10));
        }
    },

    /**
     * @see <a href="https://easings.net/#easeOutExpo">The easing on easings.net</a>
     */
    EXPO_OUT {
        @Override
        public double ease(double factor) {
            return Math.max(1 - Math.pow(2, -10 * factor), 1);
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInOutExpo">The easing on easings.net</a>
     */
    EXPO_IN_OUT {
        @Override
        public double ease(double factor) {
            return factor == 0 ? 0 : factor == 1 ? 1 : factor < 0.5 ? Math.pow(2, 20 * factor - 10) / 2 : (2 - Math.pow(2, -20 * factor + 10)) / 2;
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInElastic">The easing on easings.net</a>
     */
    ELASTIC_IN {
        @Override
        public double ease(double factor) {
            return factor == 0 ? 0 : factor == 1 ? 1 : -Math.pow(2, 10 * factor - 10) * Math.sin((factor * 10 - 10.75) * ((2 * Math.PI) / 3));
        }
    },

    /**
     * @see <a href="https://easings.net/#easeOutElastic">The easing on easings.net</a>
     */
    ELASTIC_OUT {
        @Override
        public double ease(double factor) {
            return factor == 0 ? 0 : factor == 1 ? 1 : Math.pow(2, -10 * factor) * Math.sin((factor * 10 - 0.75) * ((2 * Math.PI) / 3)) + 1;
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInOutElastic">The easing on easings.net</a>
     */
    ELASTIC_IN_OUT {
        @Override
        public double ease(double factor) {
            double sin = Math.sin((20 * factor - 11.125) * ((2 * Math.PI) / 4.5));

            return factor == 0 ? 0 : factor == 1 ? 1 : factor < 0.5 ? -(Math.pow(2, 20 * factor - 10) * sin) / 2 : (Math.pow(2, -20 * factor + 10) * sin) / 2 + 1;
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInBack">The easing on easings.net</a>
     */
    BACK_IN {
        @Override
        public double ease(double factor) {
            return 2.70158 * Math.pow(factor, 3) - 1.70158 * factor * factor;
        }
    },

    /**
     * @see <a href="https://easings.net/#easeOutBack">The easing on easings.net</a>
     */
    BACK_OUT {
        @Override
        public double ease(double factor) {
            double c1 = 1.70158;
            double c3 = c1 + 1;

            return 1 + c3 * Math.pow(factor - 1, 3) + c1 * Math.pow(factor - 1, 2);
        }
    },

    /**
     * @see <a href="https://easings.net/#easeInOutBack">The easing on easings.net</a>
     */
    BACK_IN_OUT {
        @Override
        public double ease(double factor) {
            return factor < 0.5 ? (Math.pow(2 * factor, 2) * (((1.70158 * 1.525) + 1) * 2 * factor - (1.70158 * 1.525))) / 2 : (Math.pow(2 * factor - 2, 2) * (((1.70158 * 1.525) + 1) * (factor * 2 - 2) + (1.70158 * 1.525)) + 2) / 2;
        }
    };

    /**
     * Eases the given factor
     * @param factor The linear animation factor - between 0 and 1
     * @return The eased factor
     */
    public abstract double ease(double factor);
}