/*
 * Decompiled with CFR 0.152.
 */
package ui;

public class TranslateUtil {
    public float x;
    public float y;
    public long lastMS;

    public TranslateUtil(float x, float y) {
        this.x = x;
        this.y = y;
        this.lastMS = System.currentTimeMillis();
    }

    public void interpolate(float targetX, float targetY, float smoothing) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        int deltaX = (int)(Math.abs(targetX - this.x) * smoothing);
        int deltaY = (int)(Math.abs(targetY - this.y) * smoothing);
        this.x = this.calculateCompensation(targetX, this.x, delta, deltaX);
        this.y = this.calculateCompensation(targetY, this.y, delta, deltaY);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float calculateCompensation(float target, float current, long delta, int speed) {
        float diff = current - target;
        if (delta < 41L - 69L + 29L - 14L + 14L) {
            delta = 154L - 217L + 25L - 8L + 47L;
        }
        if (diff > (float)speed) {
            double dell = (double)((long)speed * delta / (214L - 218L + 53L + -33L)) < 0.25 ? 0.5 : (double)((long)speed * delta / (254L - 505L + 33L + 234L));
            if ((current = (float)((double)current - dell)) < target) {
                current = target;
            }
        } else if (diff < (float)(-speed)) {
            double dell = (double)((long)speed * delta / (31L - 47L + 38L - 14L + 8L)) < 0.25 ? 0.5 : (double)((long)speed * delta / (130L - 167L + 34L + 19L));
            if ((current = (float)((double)current + dell)) > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }
}

