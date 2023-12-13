// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.elementars.eclient.Xulu;
import java.awt.Color;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class EnchantColor extends Module
{
    private final Value<String> mode;
    private final Value<Integer> red;
    private final Value<Integer> green;
    private final Value<Integer> blue;
    
    public EnchantColor() {
        super("EnchantColor", "Changes the color of the enchantment effect", 0, Category.RENDER, true);
        this.mode = this.register(new Value<String>("Mode", this, "Color", new String[] { "Color", "Rainbow" }));
        this.red = this.register(new Value<Integer>("Red", this, 255, 0, 255));
        this.green = this.register(new Value<Integer>("Green", this, 255, 0, 255));
        this.blue = this.register(new Value<Integer>("Blue", this, 255, 0, 255));
    }
    
    public static Color getColor(final long offset, final float fade) {
        if (Xulu.MODULE_MANAGER.getModuleT(EnchantColor.class).mode.getValue().equalsIgnoreCase("Color")) {
            return new Color(Xulu.MODULE_MANAGER.getModuleT(EnchantColor.class).red.getValue(), Xulu.MODULE_MANAGER.getModuleT(EnchantColor.class).green.getValue(), Xulu.MODULE_MANAGER.getModuleT(EnchantColor.class).blue.getValue());
        }
        final float hue = (System.nanoTime() + offset) / 1.0E10f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        final Color c = new Color((int)color);
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
    }
}
