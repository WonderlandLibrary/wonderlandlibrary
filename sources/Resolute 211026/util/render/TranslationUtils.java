// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.render;

public class TranslationUtils
{
    private float x;
    private float y;
    private long lastMS;
    
    public TranslationUtils(final float x, final float y) {
        this.x = x;
        this.y = y;
        this.lastMS = System.currentTimeMillis();
    }
    
    public void interpolate(final float targetX, final float targetY, final int xSpeed, final int ySpeed) {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        final int deltaX = (int)(Math.abs(targetX - this.x) * 0.51f);
        final int deltaY = (int)(Math.abs(targetY - this.y) * 0.51f);
        this.x = RenderUtils.calculateCompensation(targetX, this.x, delta, deltaX);
        this.y = RenderUtils.calculateCompensation(targetY, this.y, delta, deltaY);
    }
    
    public static float moveUD(final float current, final float end, final float smoothSpeed, final float minSpeed) {
        float movement = (end - current) * smoothSpeed;
        if (movement > 0.0f) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        }
        else if (movement < 0.0f) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }
        return current + movement;
    }
    
    public void interpolate(final float targetX, final float targetY, final double speed) {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        double deltaX = 0.0;
        double deltaY = 0.0;
        if (speed != 0.0) {
            deltaX = Math.abs(targetX - this.x) * 0.35f / (10.0 / speed);
            deltaY = Math.abs(targetY - this.y) * 0.35f / (10.0 / speed);
        }
        this.x = RenderUtils.calculateCompensation(targetX, this.x, delta, deltaX);
        this.y = RenderUtils.calculateCompensation(targetY, this.y, delta, deltaY);
    }
    
    public float getX() {
        return this.x;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
}
