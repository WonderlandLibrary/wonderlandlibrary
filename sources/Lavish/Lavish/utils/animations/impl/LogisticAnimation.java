// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.utils.animations.impl;

import Lavish.utils.animations.Direction;
import Lavish.utils.animations.Animation;

public class LogisticAnimation extends Animation
{
    private final double steepness;
    
    public LogisticAnimation(final int ms, final int distance, final double steepness) {
        super(ms, distance);
        this.steepness = steepness;
    }
    
    public LogisticAnimation(final int ms, final int distance) {
        super(ms, distance);
        this.steepness = 1.0;
    }
    
    public LogisticAnimation(final int ms, final int distance, final Enum<Direction> direction, final double steepness) {
        super(ms, distance, direction);
        this.steepness = steepness;
    }
    
    @Override
    protected double getEquation(final double x) {
        return this.duration / (1.0 + Math.exp(-this.steepness * (x - this.duration / 2))) / this.duration;
    }
}
