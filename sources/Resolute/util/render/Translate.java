// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.render;

import net.minecraft.util.MathHelper;

public class Translate
{
    private double x;
    private double y;
    private long lastMS;
    
    public Translate(final double x, final double y) {
        this.x = x;
        this.y = y;
        this.lastMS = System.currentTimeMillis();
    }
    
    public void interpolate(final float targetX, final float targetY, final int xSpeed, final int ySpeed) {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        final int deltaX = (int)(Math.abs(targetX - this.x) * 0.5099999904632568);
        final int deltaY = (int)(Math.abs(targetY - this.y) * 0.5099999904632568);
        this.x = calculateCompensation(targetX, (float)this.x, delta, deltaX);
        this.y = calculateCompensation(targetY, (float)this.y, delta, deltaY);
    }
    
    public static final double slide(final double current, final double min, final double max, double speed, final boolean sliding) {
        speed *= System.currentTimeMillis() * 0.2;
        return MathHelper.clamp_double(sliding ? ((current < max) ? (current + (max - current) * speed) : current) : ((current > min) ? (current - (current - min) * speed) : current), min, max);
    }
    
    public static double animate(final double target, double current, double speed) {
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
    
    public void interpolate(final float targetX, final float targetY, final double speed) {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        double deltaX = 0.0;
        double deltaY = 0.0;
        if (speed != 0.0) {
            deltaX = Math.abs(targetX - this.x) * 0.3499999940395355 / (10.0 / speed);
            deltaY = Math.abs(targetY - this.y) * 0.3499999940395355 / (10.0 / speed);
        }
        this.x = calculateCompensation(targetX, (float)this.x, delta, deltaX);
        this.y = calculateCompensation(targetY, (float)this.y, delta, deltaY);
    }
    
    public void interpolate(final float target, final float targetY, final double doubleYSpeed, final double speed) {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        if (this.x < target) {
            this.x += speed * delta;
        }
        final double deltaX = 0.0;
        double deltaY = 0.0;
        if (speed != 0.0) {
            deltaY = Math.abs(targetY - this.y) * 0.3499999940395355 / (10.0 / doubleYSpeed);
        }
        this.y = calculateCompensation(targetY, (float)this.y, delta, deltaY);
    }
    
    public static float calculateCompensation(final float target, float current, long delta, final double speed) {
        final float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (delta > 1000L) {
            delta = 16L;
        }
        if (diff > speed) {
            final double xD = (speed * delta / 16.0 < 0.5) ? 0.5 : (speed * delta / 16.0);
            current -= (float)xD;
            if (current < target) {
                current = target;
            }
        }
        else if (diff < -speed) {
            final double xD = (speed * delta / 16.0 < 0.5) ? 0.5 : (speed * delta / 16.0);
            current += (float)xD;
            if (current > target) {
                current = target;
            }
        }
        else {
            current = target;
        }
        return current;
    }
    
    public void animate(final double newX, final double newY) {
        this.x = RenderUtils.transition(this.x, newX, 0.2);
        this.y = RenderUtils.transition(this.y, newY, 0.2);
    }
    
    public void animate2(final double newX, final double newY) {
        this.x = RenderUtils.transition(this.x, newX, 0.3);
        this.y = RenderUtils.transition(this.y, newY, 0.8);
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
}
