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
    public static FontDrawer F64;
    public static FontDrawer TITLE20;
    public static FontDrawer F18;
    public static FontDrawer TITLE25;
    public static FontDrawer TITLE18;
    public static FontDrawer F16;
    public static FontDrawer TITLE16;
    public static FontDrawer F14;

    public static FontDrawer getFont(String string, int n, boolean bl) {
        Font font;
        try {
            font = Font.createFont(0, Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation("liquidwing/font/" + string + ".ttf")).func_110527_b()).deriveFont(0, n);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, n);
        }
        return new FontDrawer(font, bl);
    }

    public static void initFonts() {
        F18 = FontLoaders.getFont("misans", 18, true);
        F16 = FontLoaders.getFont("misans", 16, true);
        F14 = FontLoaders.getFont("misans", 14, true);
        F64 = FontLoaders.getFont("misans", 64, true);
        TITLE18 = FontLoaders.getFont("title", 18, true);
        TITLE16 = FontLoaders.getFont("logo", 16, true);
        TITLE20 = FontLoaders.getFont("logo", 20, true);
        TITLE25 = FontLoaders.getFont("logo", 23, true);
    }
}

