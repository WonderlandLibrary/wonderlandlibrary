// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.font;

import net.minecraft.client.Minecraft;
import java.awt.Font;
import net.minecraft.util.ResourceLocation;

public class FontUtility
{
    public static Font getFontFromTTF(final ResourceLocation fontLocation, final float fontSize, final int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
