/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.ResourceLocation
 */
package net.ccbluex.liquidbounce.ui.cnfont;

import java.awt.Font;
import net.ccbluex.liquidbounce.ui.cnfont.FontDrawer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class FontLoaders {
    public static FontDrawer F14;
    public static FontDrawer F18;
    public static FontDrawer F16;
    public static FontDrawer F20;
    public static FontDrawer F28;
    public static FontDrawer SF22;
    public static FontDrawer jellolightBig2;
    public static FontDrawer jellolight18;
    public static FontDrawer SF18;
    public static FontDrawer SF45;

    public static void initFonts() {
        String string = "jellolight.ttf";
        jellolightBig2 = (FontDrawer)0;
        String string2 = "jellolight.ttf";
        jellolight18 = (FontDrawer)0;
        String string3 = "misans";
        F18 = (FontDrawer)0;
        String string4 = "misans";
        F16 = (FontDrawer)0;
        String string5 = "misans";
        F28 = (FontDrawer)0;
        String string6 = "misans";
        F14 = (FontDrawer)0;
        String string7 = "misans";
        F20 = (FontDrawer)0;
        String string8 = "sfui";
        SF22 = (FontDrawer)0;
        String string9 = "sfui";
        SF18 = (FontDrawer)0;
        String string10 = "sfui";
        SF45 = (FontDrawer)0;
    }

    public static FontDrawer getFont(String name, int size, boolean antiAliasing) {
        Font font;
        try {
            font = Font.createFont(0, Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation("likingsense/font/" + name + ".ttf")).func_110527_b()).deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return new FontDrawer(font, antiAliasing);
    }
}

