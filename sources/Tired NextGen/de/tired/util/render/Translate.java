package de.tired.util.render;

import lombok.Getter;
import lombok.Setter;

public class Translate {

    @Getter
    @Setter
    private float x, y;
    @Getter
    private long lastMS;

    public Translate(float x, float y) {
        this.x = x;
        this.y = y;
        this.lastMS = System.currentTimeMillis();
    }


    public void interpolate(float targetX, float targetY, double speed) {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - lastMS;
        lastMS = currentMS;
        double deltaX = 0;
        double deltaY = 0;
        if (speed != 0) {
            deltaX = (Math.abs(targetX - x) * 0.35f) / (10 / speed);
            deltaY = (Math.abs(targetY - y) * 0.35f) / (10 / speed);
        }
        x = calc(targetX, x, delta, deltaX);
        y = calc(targetY, y, delta, deltaY);
    }


    public static float calc(float target, float current, long delta, double speed) {
        float diff = current - target;
        if (delta < 1) {
            delta = 1;
        }
        if (delta > 1000) {
            delta = 16;
        }
        final double max = Math.max(speed * delta / (1000 / 60f), 0.5);
        if (diff > speed) {
            current -= max;
            if (current < target) {
                current = target;
            }
        } else if (diff < -speed) {
            current += max;
            if (current > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }


}
