/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 */
package net.ccbluex.liquidbounce.ui.font;

import java.awt.Font;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.ui.font.FontDetails;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.util.ResourceLocation;

public class Fonts
extends MinecraftInstance {
    @FontDetails(fontName="Minecraft Font")
    public static final IFontRenderer minecraftFont = mc.getFontRendererObj();
    @FontDetails(fontName="Notification Icon", fontSize=80)
    public static IFontRenderer notificationIcon80;
    @FontDetails(fontName="Roboto Medium", fontSize=30)
    public static IFontRenderer font30;
    @FontDetails(fontName="Roboto Medium", fontSize=20)
    public static IFontRenderer font20;
    @FontDetails(fontName="Roboto Medium", fontSize=40)
    public static IFontRenderer font40;
    @FontDetails(fontName="Roboto Medium", fontSize=45)
    public static IFontRenderer font45;
    @FontDetails(fontName="Roboto Medium", fontSize=40)
    public static IFontRenderer font90;
    @FontDetails(fontName="Roboto Medium", fontSize=35)
    public static IFontRenderer font35;
    @FontDetails(fontName="Tenacity", fontSize=80)
    public static IFontRenderer tenacity80;
    @FontDetails(fontName="Tenacity", fontSize=26)
    public static IFontRenderer tenacity26;
    @FontDetails(fontName="Tenacity", fontSize=82)
    public static IFontRenderer tenacity81;
    @FontDetails(fontName="Posterama", fontSize=35)
    public static IFontRenderer posterama35;
    @FontDetails(fontName="Posterama", fontSize=40)
    public static IFontRenderer posterama40;
    @FontDetails(fontName="Misans", fontSize=35)
    public static IFontRenderer misans35;
    @FontDetails(fontName="Misans", fontSize=45)
    public static IFontRenderer misans45;
    @FontDetails(fontName="Misans", fontSize=40)
    public static IFontRenderer misans40;
    @FontDetails(fontName="Posterama", fontSize=45)
    public static IFontRenderer posterama45;
    @FontDetails(fontName="Posterama", fontSize=90)
    public static IFontRenderer posterama90;

    public static void loadFonts() {
        long l = System.currentTimeMillis();
        ClientUtils.getLogger().info("Loading Fonts.");
        notificationIcon80 = Fonts.getFont("notification-icon.ttf", 80);
        font40 = Fonts.getFont("sfui.ttf", 40);
        font45 = Fonts.getFont("sfui.ttf", 45);
        font90 = Fonts.getFont("sfui.ttf", 90);
        font30 = Fonts.getFont("sfui.ttf", 30);
        font20 = Fonts.getFont("sfui.ttf", 20);
        font35 = Fonts.getFont("sfui.ttf", 35);
        posterama35 = Fonts.getFont("posterama.ttf", 35);
        posterama40 = Fonts.getFont("posterama.ttf", 40);
        posterama45 = Fonts.getFont("posterama.ttf", 45);
        misans40 = Fonts.getFont("posterama.ttf", 35);
        misans35 = Fonts.getFont("posterama.ttf", 40);
        misans45 = Fonts.getFont("posterama.ttf", 45);
        posterama90 = Fonts.getFont("posterama.ttf", 90);
        tenacity81 = Fonts.getFont("tenacitybold.ttf", 82);
        tenacity80 = Fonts.getFont("tenacitybold.ttf", 80);
        tenacity26 = Fonts.getFont("tenacitybold.ttf", 26);
        ClientUtils.getLogger().info("Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }

    public static IFontRenderer getFontRenderer(String name, int size) {
        for (Field field : Fonts.class.getDeclaredFields()) {
            try {
                FontDetails fontDetails;
                field.setAccessible(true);
                Object o = field.get(null);
                if (!(o instanceof IFontRenderer) || !(fontDetails = field.getAnnotation(FontDetails.class)).fontName().equals(name) || fontDetails.fontSize() != size) continue;
                return (IFontRenderer)o;
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return Fonts.getFont("default", 35);
    }

    public static FontInfo getFontDetails(IFontRenderer fontRenderer) {
        for (Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object o = field.get(null);
                if (!o.equals(fontRenderer)) continue;
                FontDetails fontDetails = field.getAnnotation(FontDetails.class);
                return new FontInfo(fontDetails.fontName(), fontDetails.fontSize());
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<IFontRenderer> getFonts() {
        ArrayList<IFontRenderer> fonts = new ArrayList<IFontRenderer>();
        for (Field fontField : Fonts.class.getDeclaredFields()) {
            try {
                fontField.setAccessible(true);
                Object fontObj = fontField.get(null);
                if (!(fontObj instanceof IFontRenderer)) continue;
                fonts.add((IFontRenderer)fontObj);
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return fonts;
    }

    private static IFontRenderer getFont(String fontName, int size) {
        Font font;
        try {
            InputStream inputStream = minecraft.func_110442_L().func_110536_a(new ResourceLocation("likingsense/font/" + fontName)).func_110527_b();
            Font awtClientFont = Font.createFont(0, inputStream);
            awtClientFont = awtClientFont.deriveFont(0, size);
            inputStream.close();
            font = awtClientFont;
        }
        catch (Exception e) {
            e.printStackTrace();
            font = new Font("default", 0, size);
        }
        return classProvider.wrapFontRenderer(new GameFontRenderer(font));
    }

    public static class FontInfo {
        private final String name;
        private final int fontSize;

        public FontInfo(String name, int fontSize) {
            this.name = name;
            this.fontSize = fontSize;
        }

        public FontInfo(Font font) {
            this(font.getName(), font.getSize());
        }

        public String getName() {
            return this.name;
        }

        public int getFontSize() {
            return this.fontSize;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            FontInfo fontInfo = (FontInfo)o;
            if (this.fontSize != fontInfo.fontSize) {
                return false;
            }
            return Objects.equals(this.name, fontInfo.name);
        }

        public int hashCode() {
            int result = this.name != null ? this.name.hashCode() : 0;
            result = 31 * result + this.fontSize;
            return result;
        }
    }
}

