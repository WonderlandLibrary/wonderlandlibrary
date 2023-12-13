// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.render;

import java.awt.Color;

public class BetterColor extends Color
{
    public BetterColor(final double red, final double green, final double blue) {
        super((int)clamp(red, 0.0, 255.0), (int)clamp(green, 0.0, 255.0), (int)clamp(blue, 0.0, 255.0));
    }
    
    public BetterColor(final double red, final double green, final double blue, final double alpha) {
        super((int)clamp(red, 0.0, 255.0), (int)clamp(green, 0.0, 255.0), (int)clamp(blue, 0.0, 255.0), (int)clamp(alpha, 0.0, 255.0));
    }
    
    public BetterColor(final Color color) {
        super((int)clamp(color.getRed(), 0.0, 255.0), (int)clamp(color.getGreen(), 0.0, 255.0), (int)clamp(color.getBlue(), 0.0, 255.0));
    }
    
    public BetterColor(final int hex) {
        super(hex);
    }
    
    @Override
    public final BetterColor brighter() {
        return new BetterColor(super.brighter().brighter().brighter().brighter());
    }
    
    @Override
    public final BetterColor darker() {
        return new BetterColor(super.darker().darker().darker().darker());
    }
    
    public final BetterColor brighter(final int brighteningValue) {
        Color newColor;
        final Color color = newColor = this;
        for (int i = 0; i < brighteningValue; ++i) {
            newColor = newColor.brighter();
        }
        return new BetterColor(newColor).setAlpha(color.getAlpha());
    }
    
    public final BetterColor darker(final int darkeningValue) {
        Color newColor;
        final Color color = newColor = this;
        for (int i = 0; i < darkeningValue; ++i) {
            newColor = newColor.darker();
        }
        return new BetterColor(newColor).setAlpha(color.getAlpha());
    }
    
    public final BetterColor addColoring(final int value) {
        final Color color = this;
        return new BetterColor(color.getRed() + value, color.getGreen() + value, color.getBlue() + value, (double)color.getAlpha());
    }
    
    public final BetterColor addColoring(final int red, final int green, final int blue) {
        final Color color = this;
        return new BetterColor(color.getRed() + red, color.getGreen() + green, color.getBlue() + blue, (double)color.getAlpha());
    }
    
    public final BetterColor setAlpha(int alpha) {
        alpha = (int)clamp(alpha, 0.0, 255.0);
        return new BetterColor(this.getRed(), this.getGreen(), this.getBlue(), (double)alpha);
    }
    
    static final double clamp(final double value, final double min, final double max) {
        return (value > max) ? max : ((value < min) ? min : value);
    }
    
    public static final BetterColor getHue(final double value) {
        final float hue = (float)(1.0 - value / 360.0);
        final int color = Color.HSBtoRGB(hue, 1.0f, 1.0f);
        return new BetterColor(new Color(color));
    }
    
    @Override
    public final String toString() {
        return String.format("[%s,%s,%s]", this.getRed(), this.getGreen(), this.getBlue());
    }
}
