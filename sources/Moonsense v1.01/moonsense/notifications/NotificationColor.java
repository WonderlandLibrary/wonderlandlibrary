// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.notifications;

public enum NotificationColor
{
    RED("RED", 0, -40878, "Red"), 
    GREEN("GREEN", 1, -11337841, "Green"), 
    BLUE("BLUE", 2, -11355393, "Blue"), 
    YELLOW("YELLOW", 3, -6062, "Yellow"), 
    PINK("PINK", 4, -37891, "Pink");
    
    public int color;
    public String colorName;
    
    private NotificationColor(final String name, final int ordinal, final int color, final String colorName) {
        this.color = color;
        this.colorName = colorName;
    }
}
