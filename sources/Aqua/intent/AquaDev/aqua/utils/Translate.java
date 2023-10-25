package intent.AquaDev.aqua.utils;

import intent.AquaDev.aqua.utils.AnimationUtil;

public class Translate {
    private float x;
    private float y;
    private long lastMS;

    public Translate(float x, float y) {
        this.x = x;
        this.y = y;
        this.lastMS = System.currentTimeMillis();
    }

    public void interpolate(float targetX, float targetY, int xSpeed, int ySpeed) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        int deltaX = (int)(Math.abs((float)(targetX - this.x)) * 0.51f);
        int deltaY = (int)(Math.abs((float)(targetY - this.y)) * 0.51f);
        this.x = AnimationUtil.calculateCompensation((float)targetX, (float)this.x, (long)delta, (double)deltaX);
        this.y = AnimationUtil.calculateCompensation((float)targetY, (float)this.y, (long)delta, (double)deltaY);
    }

    public void interpolate(float targetX, float targetY, double speed) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        double deltaX = 0.0;
        double deltaY = 0.0;
        if (speed != 0.0) {
            deltaX = (double)Math.abs((float)(targetX - this.x)) * 0.3 / (10.0 / speed);
            deltaY = (double)Math.abs((float)(targetY - this.y)) * 0.25 / (10.0 / speed);
        }
        this.x = AnimationUtil.calculateCompensation((float)targetX, (float)this.x, (long)delta, (double)deltaX);
        this.y = AnimationUtil.calculateCompensation((float)targetY, (float)this.y, (long)delta, (double)deltaY);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
