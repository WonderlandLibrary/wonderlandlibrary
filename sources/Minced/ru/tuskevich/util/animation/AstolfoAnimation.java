// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.animation;

import ru.tuskevich.util.color.ColorUtility;
import net.minecraft.client.Minecraft;

public class AstolfoAnimation
{
    private double value;
    private double prevValue;
    
    public void update() {
        this.prevValue = this.value;
        this.value += 0.01;
    }
    
    public int getColor(final double offset) {
        double hue = (this.prevValue + (this.value - this.prevValue) * Minecraft.getMinecraft().getRenderPartialTicks() + offset) % 1.0;
        if (hue > 0.5) {
            hue = 0.5 - (hue - 0.5);
        }
        hue += 0.5;
        return ColorUtility.HSBtoRGB((float)hue, 0.5f, 1.0f);
    }
}
