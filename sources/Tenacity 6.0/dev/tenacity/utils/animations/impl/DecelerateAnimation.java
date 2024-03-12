// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.animations.impl;

import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.Animation;

public class DecelerateAnimation extends Animation
{
    public DecelerateAnimation(final int ms, final double endPoint) {
        super(ms, endPoint);
    }
    
    public DecelerateAnimation(final int ms, final double endPoint, final Direction direction) {
        super(ms, endPoint, direction);
    }
    
    @Override
    protected double getEquation(final double x) {
        return 1.0 - (x - 1.0) * (x - 1.0);
    }
}
