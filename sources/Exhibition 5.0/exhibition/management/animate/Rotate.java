// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.animate;

public class Rotate
{
    private float angle;
    
    public Rotate(final float angle) {
        this.angle = angle;
    }
    
    public void interpolate(final float targetAngle) {
        final float diffA = (this.angle - targetAngle) * 0.6f;
        float tempAngle = this.angle + diffA;
        tempAngle %= 360.0f;
        if (tempAngle >= 180.0f) {
            tempAngle -= 360.0f;
        }
        if (tempAngle < -180.0f) {
            tempAngle += 360.0f;
        }
        this.angle = tempAngle;
    }
    
    public float getAngle() {
        return this.angle;
    }
}
