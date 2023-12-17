// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.animations;

public final class Translate
{
    private double x;
    private double y;
    
    public Translate(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public void interpolate(final double targetX, final double targetY, final double smoothing) {
        this.x = this.animate(targetX, this.x, smoothing);
        this.y = this.animate(targetY, this.y, smoothing);
    }
    
    public void animate(final double newX, final double newY) {
        this.x = this.animate(this.x, newX, 1.0);
        this.y = this.animate(this.y, newY, 1.0);
    }
    
    public double animate(final double target, double current, double speed) {
        final boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        }
        else if (speed > 1.0) {
            speed = 1.0;
        }
        final double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger) {
            current += factor;
        }
        else {
            current -= factor;
        }
        return current;
    }
    
    public double getX() {
        return (double)Math.round(this.x);
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public double getY() {
        return (double)Math.round(this.y);
    }
    
    public void setY(final double y) {
        this.y = y;
    }
}
