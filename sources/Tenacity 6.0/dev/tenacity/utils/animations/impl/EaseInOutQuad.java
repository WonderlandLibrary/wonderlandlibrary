// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.animations.impl;

import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.Animation;

public class EaseInOutQuad extends Animation
{
    public EaseInOutQuad(final int ms, final double endPoint) {
        super(ms, endPoint);
    }
    
    public EaseInOutQuad(final int ms, final double endPoint, final Direction direction) {
        super(ms, endPoint, direction);
    }
    
    @Override
    protected double getEquation(final double x) {
        return (x < 0.5) ? (2.0 * Math.pow(x, 2.0)) : (1.0 - Math.pow(-2.0 * x + 2.0, 2.0) / 2.0);
    }
}
