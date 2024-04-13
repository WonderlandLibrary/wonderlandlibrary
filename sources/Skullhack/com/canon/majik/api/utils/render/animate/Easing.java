package com.canon.majik.api.utils.render.animate;

public enum Easing {

    LINEAR {
        @Override
        public double ease(double factor) {
            return factor;
        }
    };

    public abstract double ease(double factor);

}
