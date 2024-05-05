// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.animations.impl;

import Lavish.utils.animations.Direction;
import Lavish.utils.animations.Animation;

public class LinearAnimation extends Animation
{
    public LinearAnimation(final int ms, final double endPoint, final Enum<Direction> direction) {
        super(ms, endPoint, direction);
    }
    
    public LinearAnimation(final int ms, final double endPoint) {
        super(ms, endPoint);
    }
    
    @Override
    protected double getEquation(final double x) {
        return x / this.duration;
    }
}
