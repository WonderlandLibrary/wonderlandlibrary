// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.objects;

import org.lwjgl.input.Mouse;
import dev.tenacity.utils.animations.impl.SmoothStepAnimation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.Animation;

public class Scroll
{
    private float maxScroll;
    private float minScroll;
    private float rawScroll;
    private float scroll;
    private Animation scrollAnimation;
    
    public Scroll() {
        this.maxScroll = Float.MAX_VALUE;
        this.minScroll = 0.0f;
        this.rawScroll = 0.0f;
        this.scrollAnimation = new SmoothStepAnimation(0, 0.0, Direction.BACKWARDS);
    }
    
    public void onScroll(final int ms) {
        this.scroll = this.rawScroll - this.scrollAnimation.getOutput().floatValue();
        this.rawScroll += Mouse.getDWheel() / 4.0f;
        this.rawScroll = Math.max(Math.min(this.minScroll, this.rawScroll), -this.maxScroll);
        this.scrollAnimation = new SmoothStepAnimation(ms, this.rawScroll - this.scroll, Direction.BACKWARDS);
    }
    
    public boolean isScrollAnimationDone() {
        return this.scrollAnimation.isDone();
    }
    
    public float getScroll() {
        return this.scroll = this.rawScroll - this.scrollAnimation.getOutput().floatValue();
    }
    
    public float getMaxScroll() {
        return this.maxScroll;
    }
    
    public float getMinScroll() {
        return this.minScroll;
    }
    
    public float getRawScroll() {
        return this.rawScroll;
    }
    
    public void setMaxScroll(final float maxScroll) {
        this.maxScroll = maxScroll;
    }
    
    public void setMinScroll(final float minScroll) {
        this.minScroll = minScroll;
    }
    
    public void setRawScroll(final float rawScroll) {
        this.rawScroll = rawScroll;
    }
    
    public Animation getScrollAnimation() {
        return this.scrollAnimation;
    }
}
