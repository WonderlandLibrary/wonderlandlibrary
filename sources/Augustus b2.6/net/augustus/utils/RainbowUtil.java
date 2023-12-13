// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils;

import java.awt.Color;

public class RainbowUtil
{
    private float rainBowColor;
    private Color color;
    private long lastTime;
    
    public RainbowUtil() {
        this.rainBowColor = 0.0f;
        this.color = new Color(0, 0, 0);
    }
    
    public Color updateRainbow(float speed, final int alpha) {
        final long deltaTime = System.currentTimeMillis() - this.lastTime;
        if (speed > 0.0f) {
            speed *= deltaTime / 2.0f;
        }
        this.rainBowColor += speed;
        this.rainBowColor = ((this.rainBowColor > 1.0f) ? (this.rainBowColor %= 1.0f) : this.rainBowColor);
        final int red = Color.getHSBColor(this.rainBowColor, 1.0f, 1.0f).getRed();
        final int green = Color.getHSBColor(this.rainBowColor, 1.0f, 1.0f).getGreen();
        final int blue = Color.getHSBColor(this.rainBowColor, 1.0f, 1.0f).getBlue();
        this.color = new Color(red, green, blue, alpha);
        this.lastTime = System.currentTimeMillis();
        return this.color;
    }
    
    public Color getColor() {
        return this.color;
    }
}
