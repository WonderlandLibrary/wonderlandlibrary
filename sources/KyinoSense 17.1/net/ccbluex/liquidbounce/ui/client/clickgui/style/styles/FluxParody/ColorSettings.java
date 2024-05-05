/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.FluxParody;

import java.awt.Color;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.ColorValue;

public class ColorSettings {
    static int x;
    static int y;
    static int ticks;

    public void render(ColorValue colorValue, int x, int y, int mouseX, int mouseY, boolean isKeyDown) {
        ColorSettings.x = x;
        ColorSettings.y = y;
        for (ticks = 0; ticks < 50; ++ticks) {
            Color rainbowcolor = new Color(Color.HSBtoRGB((float)((double)ticks / 50.0 + Math.sin(1.6)) % 1.0f, 0.5f, 1.0f));
            if (mouseX > x && mouseX < x + 50 && mouseY > y && mouseY < y + 13 && isKeyDown) {
                colorValue.set(new Color(Color.HSBtoRGB((float)((double)(mouseX - x) / 50.0 + Math.sin(1.6)) % 1.0f, 0.5f, 1.0f)).getRGB());
                RenderUtils.drawRect((float)(mouseX - 1), (float)(mouseY - 1), (float)(mouseX + 1), (float)(mouseY + 1), new Color(100, 100, 100, 100).getRGB());
            }
            if (mouseX > x && mouseX < x + 50 && mouseY > y && mouseY < y + 13) {
                RenderUtils.drawRect((float)(mouseX - 1), (float)(mouseY - 1), (float)(mouseX + 1), (float)(mouseY + 1), new Color(100, 100, 100, 100).getRGB());
            }
            RenderUtils.drawRect((float)(x + ticks), (float)y, (float)(x + ticks + 1), (float)(y + 13), rainbowcolor.getRGB());
            RenderUtils.drawRect((float)x, (float)(y + 16), (float)(x + 50), (float)(y + 20), (int)((Integer)colorValue.get()));
        }
        if (++ticks > 50) {
            ticks = 0;
        }
    }

    static {
        y = 0;
    }
}

