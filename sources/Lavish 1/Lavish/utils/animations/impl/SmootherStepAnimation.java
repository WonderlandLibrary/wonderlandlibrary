// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.animations.impl;

import Lavish.utils.animations.Direction;
import Lavish.utils.animations.Animation;

public class SmootherStepAnimation extends Animation
{
    private double x;
    
    public SmootherStepAnimation(final int ms, final double maxOutput, final Enum<Direction> direction) {
        super(ms, maxOutput, direction);
    }
    
    public SmootherStepAnimation(final int ms, final double maxOutput) {
        super(ms, maxOutput);
    }
    
    @Override
    protected double getEquation(final double x) {
        this.x = x / this.duration;
        return 6.0 * Math.pow(this.x, 5.0) - 15.0 * Math.pow(this.x, 4.0) + 10.0 * Math.pow(this.x, 3.0);
    }
}
