// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.animations.impl;

import ru.tuskevich.util.animations.Direction;
import ru.tuskevich.util.animations.Animation;

public class ElasticAnimation extends Animation
{
    float easeAmount;
    float smooth;
    boolean reallyElastic;
    
    public ElasticAnimation(final int ms, final double endPoint, final float elasticity, final float smooth, final boolean moreElasticity) {
        super(ms, endPoint);
        this.easeAmount = elasticity;
        this.smooth = smooth;
        this.reallyElastic = moreElasticity;
    }
    
    public ElasticAnimation(final int ms, final double endPoint, final float elasticity, final float smooth, final boolean moreElasticity, final Direction direction) {
        super(ms, endPoint, direction);
        this.easeAmount = elasticity;
        this.smooth = smooth;
        this.reallyElastic = moreElasticity;
    }
    
    @Override
    protected double getEquation(final double x) {
        final double x2 = Math.pow(x / this.duration, this.smooth);
        final double elasticity = this.easeAmount * 0.1f;
        return Math.pow(2.0, -10.0 * (this.reallyElastic ? Math.sqrt(x2) : x2)) * Math.sin((x2 - elasticity / 4.0) * (6.283185307179586 / elasticity)) + 1.0;
    }
}
