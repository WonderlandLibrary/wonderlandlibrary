// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.animations.impl;

import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.Animation;

public class EaseBackIn extends Animation
{
    private final float easeAmount;
    
    public EaseBackIn(final int ms, final double endPoint, final float easeAmount) {
        super(ms, endPoint);
        this.easeAmount = easeAmount;
    }
    
    public EaseBackIn(final int ms, final double endPoint, final float easeAmount, final Direction direction) {
        super(ms, endPoint, direction);
        this.easeAmount = easeAmount;
    }
    
    @Override
    protected boolean correctOutput() {
        return true;
    }
    
    @Override
    protected double getEquation(final double x) {
        final float shrink = this.easeAmount + 1.0f;
        return Math.max(0.0, 1.0 + shrink * Math.pow(x - 1.0, 3.0) + this.easeAmount * Math.pow(x - 1.0, 2.0));
    }
}
