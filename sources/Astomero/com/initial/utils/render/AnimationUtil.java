package com.initial.utils.render;

public enum AnimationUtil
{
    INSTANCE;
    
    public double animate(final double target, double current, double speed) {
        final boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        }
        else if (speed > 1.0) {
            speed = 1.0;
        }
        final double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger) {
            current += factor;
        }
        else {
            current -= factor;
        }
        return current;
    }
    
    public float value(final long startTime) {
        return Math.min(1.0f, (float)Math.pow((System.currentTimeMillis() - startTime) / 10.0, 1.4) / 80.0f);
    }
}
