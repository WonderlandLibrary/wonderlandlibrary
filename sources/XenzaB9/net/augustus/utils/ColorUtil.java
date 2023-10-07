// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils;

import net.augustus.utils.interfaces.MC;
import java.awt.Color;

public class ColorUtil
{
    private float current;
    private boolean reached;
    
    public ColorUtil() {
        this.current = 0.0f;
        this.reached = false;
    }
    
    public int getHue(final double paramDouble) {
        final double d = 1.0 - paramDouble / 360.0;
        return (int)Long.parseLong(Integer.toHexString(Color.HSBtoRGB((float)d, 1.0f, 1.0f)), 16);
    }
    
    public int getHue(final double paramDouble, final float paramFloat1, final float paramFloat2) {
        final double d = 1.0 - paramDouble / 360.0;
        return (int)Long.parseLong(Integer.toHexString(Color.HSBtoRGB((float)d, paramFloat1, paramFloat2)), 16);
    }
    
    public Color getRainbowColor(final int paramInt1, final int paramInt2, final float paramFloat) {
        double d = Math.ceil((double)((System.currentTimeMillis() + paramInt1) / paramInt2));
        d %= 100.0;
        return new Color(Color.HSBtoRGB((float)d / 100.0f, 1.0f, paramFloat));
    }
    
    public Color getSaturationFadeColor(final Color paramColor, final int paramInt, final float paramFloat) {
        final float[] arrayOfFloat = new float[3];
        Color.RGBtoHSB(paramColor.getRed(), paramColor.getGreen(), paramColor.getBlue(), arrayOfFloat);
        return new Color(Color.HSBtoRGB(arrayOfFloat[0], arrayOfFloat[1], (float)((float)(Math.sin(paramInt * 0.5 - MC.mc.thePlayer.ticksExisted * 0.22f) * 0.5 + 0.5) * 0.6 + 0.4)));
    }
}
