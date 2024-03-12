// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.animations.impl;

import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.Animation;

public class EaseOutSine extends Animation
{
    public EaseOutSine(final int ms, final double endPoint) {
        super(ms, endPoint);
    }
    
    public EaseOutSine(final int ms, final double endPoint, final Direction direction) {
        super(ms, endPoint, direction);
    }
    
    @Override
    protected boolean correctOutput() {
        return true;
    }
    
    @Override
    protected double getEquation(final double x) {
        return Math.sin(x * 1.5707963267948966);
    }
}
