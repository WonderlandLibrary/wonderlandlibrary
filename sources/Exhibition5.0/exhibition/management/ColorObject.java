// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management;

import exhibition.management.command.impl.Color;
import exhibition.util.render.Colors;

public class ColorObject
{
    public int red;
    public int green;
    public int blue;
    public int alpha;
    
    public ColorObject(final int red, final int green, final int blue, final int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
    
    public int getRed() {
        return this.red;
    }
    
    public int getGreen() {
        return this.green;
    }
    
    public int getBlue() {
        return this.blue;
    }
    
    public int getAlpha() {
        return this.alpha;
    }
    
    public void setRed(final int red) {
        this.red = red;
    }
    
    public void setGreen(final int green) {
        this.green = green;
    }
    
    public void setBlue(final int blue) {
        this.blue = blue;
    }
    
    public void setAlpha(final int alpha) {
        this.alpha = alpha;
    }
    
    public int getColorInt() {
        return Colors.getColor(this.red, this.green, this.blue, this.alpha);
    }
    
    public void updateColors(final int red, final int green, final int blue, final int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        Color.saveStatus();
    }
}
