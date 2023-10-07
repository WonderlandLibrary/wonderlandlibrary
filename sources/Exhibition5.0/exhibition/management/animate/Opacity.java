// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.animate;

public class Opacity
{
    private int opacity;
    
    public Opacity(final int opacity) {
        this.opacity = opacity;
    }
    
    public void interpolate(final int targetOpacity) {
        final int diffO = (int)((this.opacity - targetOpacity) * 0.6);
        this.opacity += diffO;
    }
    
    public int getOpacity() {
        return this.opacity;
    }
    
    public void setOpacity(final int opacity) {
        this.opacity = opacity;
    }
}
