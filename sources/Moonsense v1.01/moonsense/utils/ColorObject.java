// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils;

import moonsense.ui.utils.GuiUtils;
import java.awt.Color;

public class ColorObject
{
    private int color;
    private boolean chroma;
    private int chromaSpeed;
    
    public ColorObject(final int color, final boolean chroma, final int chromaSpeed) {
        this.color = color;
        this.chroma = chroma;
        this.chromaSpeed = chromaSpeed;
    }
    
    public int getChromaColor() {
        return this.getChromaColor(this.getHue());
    }
    
    public int getChromaColor(final float hue) {
        return GuiUtils.getRGB(Color.HSBtoRGB(hue, 1.0f, 1.0f), this.color >> 24 & 0xFF);
    }
    
    public float getHue() {
        return System.currentTimeMillis() % (long)this.getActualChromaSpeed() / this.getActualChromaSpeed();
    }
    
    public float getActualChromaSpeed() {
        return (float)((100 - this.chromaSpeed) * 100);
    }
    
    public void setColor(final int color) {
        this.color = color;
    }
    
    public void setChroma(final boolean chroma) {
        this.chroma = chroma;
    }
    
    public void setChromaSpeed(final int chromaSpeed) {
        this.chromaSpeed = chromaSpeed;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public int getAlpha() {
        return new Color(this.color).getAlpha();
    }
    
    public void setAlpha(final int alpha) {
        final float[] f = new Color(this.color).getColorComponents(null);
        this.color = new Color(f[0], f[1], f[2], (float)(alpha / 255)).getRGB();
    }
    
    public boolean isChroma() {
        return this.chroma;
    }
    
    public int getChromaSpeed() {
        return this.chromaSpeed;
    }
    
    public ColorObject darker() {
        return new ColorObject(new Color(this.color).darker().getRGB(), this.chroma, this.chromaSpeed);
    }
}
