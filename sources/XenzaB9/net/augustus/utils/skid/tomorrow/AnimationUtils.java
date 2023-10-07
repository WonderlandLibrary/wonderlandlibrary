// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils.skid.tomorrow;

import net.augustus.utils.TimeHelper;

public class AnimationUtils
{
    private static float defaultSpeed;
    private TimeHelper timerUtil;
    
    public AnimationUtils() {
        this.timerUtil = new TimeHelper();
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
            if ((current -= (float)xD) < target) {
                current = target;
            }
        }
        else if (diff < -speed) {
            final double xD = (speed * delta / 16.0 < 0.5) ? 0.5 : (speed * delta / 16.0);
            if ((current += (float)xD) > target) {
                current = target;
            }
        }
        else {
            current = target;
        }
        return current;
    }
    
    public float mvoeUD(final float current, final float end, final float minSpeed) {
        return this.moveUD(current, end, AnimationUtils.defaultSpeed, minSpeed);
    }
    
    public double animate(final double target, double current, double speed) {
        if (this.timerUtil.reached(4L)) {
            final boolean bl;
            final boolean larger = bl = (target > current);
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
            current = (current = (larger ? (current + factor) : (current - factor)));
            this.timerUtil.reset();
        }
        return current;
    }
    
    public float animate(final float target, float current, float speed) {
        if (this.timerUtil.reached(4L)) {
            final boolean bl;
            final boolean larger = bl = (target > current);
            if (speed < 0.0f) {
                speed = 0.0f;
            }
            else if (speed > 1.0) {
                speed = 1.0f;
            }
            final float dif = Math.max(target, current) - Math.min(target, current);
            float factor = dif * speed;
            if (factor < 0.1f) {
                factor = 0.1f;
            }
            current = (current = (larger ? (current + factor) : (current - factor)));
            this.timerUtil.reset();
        }
        if (Math.abs(current - target) < 0.2) {
            return target;
        }
        return current;
    }
    
    public float moveUD(final float current, final float end, final float smoothSpeed, final float minSpeed) {
        float movement = 0.0f;
        if (this.timerUtil.reached(20L)) {
            movement = (end - current) * smoothSpeed;
            if (movement > 0.0f) {
                movement = Math.max(minSpeed, movement);
                movement = Math.min(end - current, movement);
            }
            else if (movement < 0.0f) {
                movement = Math.min(-minSpeed, movement);
                movement = Math.max(end - current, movement);
            }
            this.timerUtil.reset();
        }
        return current + movement;
    }
    
    static {
        AnimationUtils.defaultSpeed = 0.125f;
    }
}
