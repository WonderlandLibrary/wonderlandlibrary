package net.ccbluex.liquidbounce.Gui.cfont;

import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import java.awt.Font;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;


public abstract class FontLoaders {
    public static CFontRenderer Leain20 = new CFontRenderer(FontLoaders.getFont("ETB", 20), true, true);
    public static CFontRenderer Leain18 = new CFontRenderer(FontLoaders.getFont("ETB", 18), true, true);
    public static CFontRenderer kiona17 = new CFontRenderer(FontLoaders.getFont("ETB", 14), true, true);;
    public static CFontRenderer kiona14 = new CFontRenderer(FontLoaders.getKiona(14), true, true);
    public static CFontRenderer kionau28 = new CFontRenderer(FontLoaders.getKiona(28), true, true);

    public static CFontRenderer ETB18 = new CFontRenderer(FontLoaders.ETB("ETB", 18), true, true);
    public static CFontRenderer ETB14 = new CFontRenderer(FontLoaders.ETB("ETB", 14), true, true);
    public static CFontRenderer ETB16 = new CFontRenderer(FontLoaders.ETB("ETB", 16), true, true);

    public static CFontRenderer J18 = new CFontRenderer(FontLoaders.getFont("ETB", 16), true, true);
    public static CFontRenderer J19 = new CFontRenderer(FontLoaders.getFont("ETB", 18), true, true);

    public static CFontRenderer kiona18 = new CFontRenderer(FontLoaders.getFont("ETB", 18), true, true);
    public static CFontRenderer kiona16 = new CFontRenderer(FontLoaders.getFont("ETB", 16), true, true);
    public static CFontRenderer kiona20 = new CFontRenderer(FontLoaders.getFont("ETB", 20), true, true);
    public static CFontRenderer kiona22 = new CFontRenderer(FontLoaders.getFont("ETB", 22), true, true);
    public static CFontRenderer kiona24 = new CFontRenderer(FontLoaders.getFont("ETB", 24), true, true);
    public static CFontRenderer kiona26 = new CFontRenderer(FontLoaders.getFont("ETB", 26), true, true);
    public static CFontRenderer kiona28 = new CFontRenderer(FontLoaders.getFont("ETB", 28), true, true);
    public static CFontRenderer kiona40 = new CFontRenderer(FontLoaders.getFont("ETB", 40), true, true);
    public static CFontRenderer kiona12 = new CFontRenderer(FontLoaders.getFont("ETB", 12), true, true);
    public static CFontRenderer kiona25 = new CFontRenderer(FontLoaders.getFont("ETB", 25), true, true);
    public static CFontRenderer Text18 = new CFontRenderer(FontLoaders.getFont("AmText", 16), true, true);
    public static CFontRenderer Text19 = new CFontRenderer(FontLoaders.getFont("AmText", 18), true, true);
    public static CFontRenderer Text20 = new CFontRenderer(FontLoaders.getFont("AmText", 20), true, true);
    public static CFontRenderer Text22 = new CFontRenderer(FontLoaders.getFont("AmText", 22), true, true);
    public static CFontRenderer Text24 = new CFontRenderer(FontLoaders.getFont("AmText", 24), true, true);
    public static CFontRenderer Text26 = new CFontRenderer(FontLoaders.getFont("AmText", 26), true, true);
    public static CFontRenderer Text28 = new CFontRenderer(FontLoaders.getFont("AmText", 28), true, true);
    public static CFontRenderer Text40 = new CFontRenderer(FontLoaders.getFont("AmText", 50), true, true);
    public static CFontRenderer Text48 = new CFontRenderer(FontLoaders.getFont("AmText", 48), true, true);
    public static CFontRenderer Text12 = new CFontRenderer(FontLoaders.getFont("AmText", 12), true, true);
    public static CFontRenderer Text14 = new CFontRenderer(FontLoaders.getFont("AmText", 14), true, true);
    public static CFontRenderer Text25 = new CFontRenderer(FontLoaders.getFont("AmText", 25), true, true);

    public static CFontRenderer newLeain20 = new CFontRenderer(FontLoaders.getFont("ETB", 20), true, true);
    public static CFontRenderer newLeain30 = new CFontRenderer(FontLoaders.getFont("ETB", 40), true, true);
    public static CFontRenderer newLeain16 = new CFontRenderer(FontLoaders.getFont("ETB", 16), true, true);
    public static CFontRenderer newLeain18 = new CFontRenderer(FontLoaders.getFont("ETB", 18), true, true);
    public static CFontRenderer newLeain26 = new CFontRenderer(FontLoaders.getFont("ETB", 26), true, true);

    public static CFontRenderer fy16 = new CFontRenderer(FontLoaders.getKiona2(16), true, true);
    public static CFontRenderer fy18 = new CFontRenderer(FontLoaders.getKiona2(18), true, true);
    public static CFontRenderer fy20 = new CFontRenderer(FontLoaders.getKiona2(20), true, true);
    public static CFontRenderer fy28 = new CFontRenderer(FontLoaders.getKiona2(28), true, true);

    public static CFontRenderer pp22 = new CFontRenderer(FontLoaders.getKiona7(22), true, true);

    private static Font getKiona2(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("liquidbounce/font/ETB.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    private static Font getKiona7(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("liquidbounce/font/ETB.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    public static Font ETB(String name, int size) {
        Font font;
        try {
            InputStream is2 = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("liquidbounce/font/ETB.ttf")).getInputStream();
            font = Font.createFont(0, is2);
            font = font.deriveFont(0, size);
        } catch (Exception ex2) {
            ex2.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    public static Font getFont(String name, int size) {
        Font font;
        try {
            InputStream is2 = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("liquidbounce/font/" + name + ".ttf")).getInputStream();
            font = Font.createFont(0, is2);
            font = font.deriveFont(0, size);
        } catch (Exception ex2) {
            ex2.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    private static Font getKiona(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager()
                    .getResource(new ResourceLocation("liquidbounce/font/ETB.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
}
