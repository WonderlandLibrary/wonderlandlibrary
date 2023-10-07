// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.animate;

public class Expand
{
    private float x;
    private float y;
    private float expandX;
    private float expandY;
    private long lastMS;
    
    public Expand(final float x, final float y, final float expandX, final float expandY) {
        this.x = x;
        this.y = y;
        this.expandX = expandX;
        this.expandY = expandY;
    }
    
    public void interpolate(final float targetX, final float targetY, final int speed) {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        this.x = AnimationUtil.calculateCompensation(targetX, this.x, delta, speed);
        this.y = AnimationUtil.calculateCompensation(targetY, this.y, delta, speed);
    }
    
    public float getExpandX() {
        return this.expandX;
    }
    
    public float getExpandY() {
        return this.expandY;
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
