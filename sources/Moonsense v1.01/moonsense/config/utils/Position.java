// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.config.utils;

import org.apache.commons.lang3.mutable.MutableFloat;

public class Position
{
    private final AnchorPoint anchorPoint;
    private final MutableFloat x;
    private final MutableFloat y;
    
    public Position(final AnchorPoint anchorPoint, final float x, final float y) {
        this.anchorPoint = anchorPoint;
        this.x = new MutableFloat(x);
        this.y = new MutableFloat(y);
    }
    
    public void setX(final float x) {
        this.x.setValue(x);
    }
    
    public float getX() {
        return this.x.getValue();
    }
    
    public void setY(final float y) {
        this.y.setValue(y);
    }
    
    public float getY() {
        return this.y.getValue();
    }
    
    public AnchorPoint getAnchorPoint() {
        return this.anchorPoint;
    }
}
