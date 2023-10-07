package com.ohare.client.utils.font;

import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Fonts {

    public static final MCFontRenderer hudfont = new MCFontRenderer(new Font("Tahoma", Font.PLAIN, 18), true, true);
    public static final MCFontRenderer clickfont = new MCFontRenderer(new Font("Tahoma", Font.PLAIN, 14), true, true);
    public static final MCFontRenderer sliderfont = new MCFontRenderer(new Font("Tahoma", Font.PLAIN, 12), true, true);
    public static final MCFontRenderer iconfont = new MCFontRenderer(fontFromTTF(new ResourceLocation("textures/client/IconFont.ttf"), 22, Font.PLAIN), true, true);

    public static Font fontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

}
