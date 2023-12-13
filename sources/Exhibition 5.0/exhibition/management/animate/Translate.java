// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.animate;

public class Translate
{
    private float x;
    private float y;
    private long lastMS;
    
    public Translate(final float x, final float y) {
        this.x = x;
        this.y = y;
        this.lastMS = System.currentTimeMillis();
    }
    
    public void interpolate(final float targetX, final float targetY, final int speed) {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        this.x = AnimationUtil.calculateCompensation(targetX, this.x, delta, speed);
        this.y = AnimationUtil.calculateCompensation(targetY, this.y, delta, speed);
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
