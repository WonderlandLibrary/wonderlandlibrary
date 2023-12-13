// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.render;

public final class TranslateUtils
{
    private double x;
    private double y;
    
    public TranslateUtils(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    public void animate(final double newX, final double newY) {
        this.x = RenderUtils.progressiveAnimation(this.x, newX, 1.0);
        this.y = RenderUtils.progressiveAnimation(this.y, newY, 1.0);
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
