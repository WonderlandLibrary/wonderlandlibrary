/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui;

public class AnimationUtil {
    public static float moveTowards(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;
        if (movement > 0.0f) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 0.0f) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        return current + movement;
    }

    public static float calculateCompensation(float target, float current, long delta, int speed) {
        float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (diff > (float)speed) {
            double xD = (double)((long)speed * delta / 16L) < 0.25 ? 0.5 : (double)((long)speed * delta / 16L);
            if ((current = (float)((double)current - xD)) < target) {
                current = target;
            }
        } else if (diff < (float)(-speed)) {
            double xD = (double)((long)speed * delta / 16L) < 0.25 ? 0.5 : (double)((long)speed * delta / 16L);
            if ((current = (float)((double)current + xD)) > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public static float animate(float target, float current, double speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        float dif = Math.max(target, current) - Math.min(target, current);
        float factor = (float)((double)dif * speed);
        current = larger ? current + factor : current - factor;
        return current;
    }

    public static double animate(double target, double current, double speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < (double)0.1f) {
            factor = 0.1f;
        }
        current = larger ? current + factor : current - factor;
        return current;
    }

    public static float mvoeUD(float current, float end, float minSpeed) {
        return AnimationUtil.moveUD(current, end, 0.125f, minSpeed);
    }

    public static float moveUD(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;
        if (movement > 10.0f) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 10.0f) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        return current + movement;
    }
}

