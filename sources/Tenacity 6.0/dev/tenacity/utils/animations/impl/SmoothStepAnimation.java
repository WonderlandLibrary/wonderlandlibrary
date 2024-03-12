// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.animations.impl;

import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.Animation;

public class SmoothStepAnimation extends Animation
{
    public SmoothStepAnimation(final int ms, final double endPoint) {
        super(ms, endPoint);
    }
    
    public SmoothStepAnimation(final int ms, final double endPoint, final Direction direction) {
        super(ms, endPoint, direction);
    }
    
    @Override
    protected double getEquation(final double x) {
        return -2.0 * Math.pow(x, 3.0) + 3.0 * Math.pow(x, 2.0);
    }
}
