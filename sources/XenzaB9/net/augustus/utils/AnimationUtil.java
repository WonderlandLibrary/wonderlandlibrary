// 
// Decompiled by Procyon v0.6.0
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
        float var1 = this.speed / deltaTime;
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
        var1 = this.x + sx * this.side;
        if (var1 < minXY) {
            var1 = minXY;
        }
        else if (var1 > maxXY) {
            var1 = maxXY;
        }
        this.x = var1;
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
