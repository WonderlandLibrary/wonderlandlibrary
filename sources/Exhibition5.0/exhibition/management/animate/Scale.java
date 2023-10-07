// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.animate;

public class Scale
{
    private float centerX;
    private float centerY;
    private float width;
    private float height;
    private long lastMS;
    
    public Scale(final float centerX, final float centerY, final float width, final float height) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.height = height;
        this.width = width;
        this.lastMS = System.currentTimeMillis();
    }
    
    public void interpolate(final float tWidth, final float tHeight, final int speed) {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        final float diffW = this.width - tWidth;
        if (diffW > speed) {
            this.width -= speed * delta / 16L;
        }
        else if (diffW < -speed) {
            this.width += speed * delta / 16L;
        }
        else {
            this.width = tWidth;
        }
        final float diffH = this.height - tHeight;
        if (diffH > speed) {
            this.height -= speed * delta / 16L;
        }
        else if (diffH < -speed) {
            this.height += speed * delta / 16L;
        }
        else {
            this.height = tHeight;
        }
    }
    
    public float getCenterX() {
        return this.centerX;
    }
    
    public float getCenterY() {
        return this.centerY;
    }
    
    public float getWidth() {
        return this.width;
    }
    
    public float getHeight() {
        return this.height;
    }
}
