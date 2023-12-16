// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.config.utils;

public enum AnchorPoint
{
    TOP_LEFT("TOP_LEFT", 0, 0), 
    TOP_CENTER("TOP_CENTER", 1, 1), 
    TOP_RIGHT("TOP_RIGHT", 2, 2), 
    BOTTOM_LEFT("BOTTOM_LEFT", 3, 3), 
    BOTTOM_CENTER("BOTTOM_CENTER", 4, 4), 
    BOTTOM_RIGHT("BOTTOM_RIGHT", 5, 5), 
    CENTER_LEFT("CENTER_LEFT", 6, 6), 
    CENTER("CENTER", 7, 7), 
    CENTER_RIGHT("CENTER_RIGHT", 8, 8);
    
    private final int id;
    
    private AnchorPoint(final String name, final int ordinal, final int id) {
        this.id = id;
    }
    
    public static AnchorPoint fromId(final int id) {
        AnchorPoint[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final AnchorPoint ap = values[i];
            if (ap.getId() == id) {
                return ap;
            }
        }
        return null;
    }
    
    public int getX(final int maxX) {
        int x = 0;
        switch (this) {
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
            case CENTER_RIGHT: {
                x = maxX;
                break;
            }
            case TOP_CENTER:
            case BOTTOM_CENTER:
            case CENTER: {
                x = maxX / 2;
                break;
            }
            default: {
                x = 0;
                break;
            }
        }
        return x;
    }
    
    public int getY(final int maxY) {
        int y = 0;
        switch (this) {
            case BOTTOM_LEFT:
            case BOTTOM_CENTER:
            case BOTTOM_RIGHT: {
                y = maxY;
                break;
            }
            case CENTER_LEFT:
            case CENTER:
            case CENTER_RIGHT: {
                y = maxY / 2;
                break;
            }
            default: {
                y = 0;
                break;
            }
        }
        return y;
    }
    
    public boolean isRightSide() {
        switch (this) {
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
            case CENTER_RIGHT: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public int getId() {
        return this.id;
    }
}
