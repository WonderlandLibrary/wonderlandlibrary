// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils;

import net.augustus.utils.interfaces.MC;

public class AnimationUtil implements MC
{
    private float x;
    private float minX;
    private float maxX;
    private float speed;
    private long lastTime;
    private int side;
    
    public AnimationUtil(final float xy, final float minXY, final float maxXY, final float speed) {
        this.x = xy;
        this.minX = minXY;
        this.maxX = maxXY;
        this.speed = speed;
    }
    
    public float updateAnimation(final int side) {
        final long deltaTime = System.currentTimeMillis() - this.lastTime;
        float sx = 0.0f;
        if (this.speed != 0.0f) {
            final float var1 = this.speed / deltaTime;
            sx = (this.maxX - this.minX) / var1;
        }
        if (this.side == 0) {
            this.lastTime = System.currentTimeMillis();
            return this.x;
        }
        if (this.side > 0) {
            this.side = 1;
        }
        else {
            this.side = -1;
        }
        float cxy = this.x + sx * side;
        if (cxy < this.minX) {
            cxy = this.minX;
        }
        else if (cxy > this.maxX) {
            cxy = this.maxX;
        }
        this.x = cxy;
        this.lastTime = System.currentTimeMillis();
        return this.x;
    }
    
    public float updateAnimation(final float minXY, final float maxXY) {
        this.minX = minXY;
        this.maxX = maxXY;
        long deltaTime = System.currentTimeMillis() - this.lastTime;
        if (deltaTime > 60L) {
            deltaTime = 60L;
        }
        float sx = 0.0f;
        if (this.speed == 0.0f) {
            return this.x;
        }
        final float var1 = this.speed / deltaTime;
        sx = (this.maxX - this.minX) / var1;
        if (this.side == 0) {
            this.lastTime = System.currentTimeMillis();
            return this.x;
        }
        if (this.side > 0) {
            this.side = 1;
        }
        else {
            this.side = -1;
        }
        float cxy = this.x + sx * this.side;
        if (cxy < minXY) {
            cxy = minXY;
        }
        else if (cxy > maxXY) {
            cxy = maxXY;
        }
        this.x = cxy;
        this.lastTime = System.currentTimeMillis();
        return this.x;
    }
    
    public void setSide(final int side) {
        this.side = side;
    }
    
    public void setSpeed(final float speed) {
        this.speed = speed;
    }
}
