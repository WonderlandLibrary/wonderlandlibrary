// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.animations;

import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.utils.animations.impl.SmoothStepAnimation;

public class ContinualAnimation
{
    private float output;
    private float endpoint;
    private Animation animation;
    
    public ContinualAnimation() {
        this.animation = new SmoothStepAnimation(0, 0.0, Direction.BACKWARDS);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void animate(final float destination, final int ms) {
        this.output = this.endpoint - this.animation.getOutput().floatValue();
        this.endpoint = destination;
        if (this.output != this.endpoint - destination) {
            this.animation = new SmoothStepAnimation(ms, this.endpoint - this.output, Direction.BACKWARDS);
        }
    }
    
    public boolean isDone() {
        return this.output == this.endpoint || this.animation.isDone();
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public float getOutput() {
        return this.output = this.endpoint - this.animation.getOutput().floatValue();
    }
    
    public Animation getAnimation() {
        return this.animation;
    }
}
