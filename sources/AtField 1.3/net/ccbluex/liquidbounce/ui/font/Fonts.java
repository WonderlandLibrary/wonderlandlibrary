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
    @FontDetails(fontName="Roboto Medium", fontSize=22)
    public static IFontRenderer font43;
    @FontDetails(fontName="Roboto Medium", fontSize=13)
    public static IFontRenderer font25;
    @FontDetails(fontName="Tenacitybold", fontSize=20)
    public static IFontRenderer tenacitybold40;
    @FontDetails(fontName="Roboto Medium", fontSize=20)
    public static IFontRenderer font40;
    @FontDetails(fontName="Minecraft Font")
    public static final IFontRenderer minecraftFont;
    @FontDetails(fontName="Tenacity", fontSize=20)
    public static IFontRenderer tenacity40;
    @FontDetails(fontName="Roboto Medium", fontSize=20)
    public static IFontRenderer roboto40;
    @FontDetails(fontName="Posterama", fontSize=18)
    public static IFontRenderer posterama35;
    @FontDetails(fontName="Roboto Bold", fontSize=90)
    public static IFontRenderer robotoBold180;
    @FontDetails(fontName="Posterama", fontSize=20)
    public static IFontRenderer posterama20;
    @FontDetails(fontName="Roboto Medium", fontSize=15)
    public static IFontRenderer font30;
    @FontDetails(fontName="flux", fontSize=18)
    public static IFontRenderer flux;
    @FontDetails(fontName="Tenacity", fontSize=40)
    public static IFontRenderer tenacity80;
    @FontDetails(fontName="Annabelle", fontSize=72)
    public static IFontRenderer annabelle72;
    @FontDetails(fontName="Posterama", fontSize=15)
    public static IFontRenderer posterama30;
    @FontDetails(fontName="Posterama", fontSize=20)
    public static IFontRenderer posterama40;
    @FontDetails(fontName="Tenacitycheck", fontSize=30)
    public static IFontRenderer tenacitycheck60;
    @FontDetails(fontName="Product Sans", fontSize=40)
    public static IFontRenderer productSans80;
    @FontDetails(fontName="NB", fontSize=18)
    public static IFontRenderer nbicon18;
    @FontDetails(fontName="Product Sans", fontSize=37)
    public static IFontRenderer productSans70;
    @FontDetails(fontName="Bold", fontSize=24)
    public static IFontRenderer bold24;
    @FontDetails(fontName="Tenacitybold", fontSize=18)
    public static IFontRenderer tenacitybold35;
    @FontDetails(fontName="Notification Icon", fontSize=35)
    public static IFontRenderer notificationIcon70;
    @FontDetails(fontName="Roboto Medium", fontSize=18)
    public static IFontRenderer roboto35;
    @FontDetails(fontName="NB", fontSize=20)
    public static IFontRenderer nbicon20;
    @FontDetails(fontName="Roboto Medium", fontSize=18)
    public static IFontRenderer font35;
    @FontDetails(fontName="Bold", fontSize=36)
    public static IFontRenderer bold36;
    @FontDetails(fontName="Product Sans", fontSize=18)
    public static IFontRenderer productSans35;
    @FontDetails(fontName="Product Sans", fontSize=20)
    public static IFontRenderer productSans40;
    @FontDetails(fontName="Bold", fontSize=12)
    public static IFontRenderer bold12;
    @FontDetails(fontName="Tenacitybold", fontSize=21)
    public static IFontRenderer tenacitybold43;
    @FontDetails(fontName="Tenacitybold", fontSize=15)
    public static IFontRenderer tenacitybold30;
    @FontDetails(fontName="flux", fontSize=23)
    public static IFontRenderer flux45;

    public static FontInfo getFontDetails(IFontRenderer iFontRenderer) {
        for (Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object object = field.get(null);
                System.out.println(field.get(null) == null);
                if (!object.equals(iFontRenderer)) continue;
                FontDetails fontDetails = field.getAnnotation(FontDetails.class);
                return new FontInfo(fontDetails.fontName(), fontDetails.fontSize());
            }
            catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
        }
        return null;
    }

    private static IFontRenderer getFont(String string, int n) {
        Font font;
        try {
            InputStream inputStream = minecraft.func_110442_L().func_110536_a(new ResourceLocation("More/font/" + string)).func_110527_b();
            Font font2 = Font.createFont(0, inputStream);
            font2 = font2.deriveFont(0, n);
            inputStream.close();
            font = font2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            font = new Font("default", 0, n);
        }
        return classProvider.wrapFontRenderer(new GameFontRenderer(font));
    }

    private static IFontRenderer getOtherPathFont(String string, int n) {
        Font font;
        try {
            InputStream inputStream = minecraft.func_110442_L().func_110536_a(new ResourceLocation(string)).func_110527_b();
            Font font2 = Font.createFont(0, inputStream);
            font2 = font2.deriveFont(0, n);
            inputStream.close();
            font = font2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            font = new Font("default", 0, n);
        }
        return classProvider.wrapFontRenderer(new GameFontRenderer(font));
    }

    static {
        minecraftFont = mc.getFontRendererObj();
    }

    public static List getFonts() {
        ArrayList<IFontRenderer> arrayList = new ArrayList<IFontRenderer>();
        for (Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object object = field.get(null);
                if (!(object instanceof IFontRenderer)) continue;
                arrayList.add((IFontRenderer)object);
            }
            catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
        }
        return arrayList;
    }

    public static void loadFonts() {
        long l = System.currentTimeMillis();
        ClientUtils.getLogger().info("Loading Fonts.");
        font25 = Fonts.getFont("sfui.ttf", 13);
        font30 = Fonts.getFont("sfui.ttf", 15);
        font35 = Fonts.getFont("sfui.ttf", 18);
        font40 = Fonts.getFont("sfui.ttf", 20);
        font43 = Fonts.getFont("sfui.ttf", 22);
        roboto35 = Fonts.getFont("roboto-medium.ttf", 18);
        roboto40 = Fonts.getFont("roboto-medium.ttf", 20);
        robotoBold180 = Fonts.getFont("roboto-bold.ttf", 90);
        productSans35 = Fonts.getFont("product-sans.ttf", 18);
        productSans40 = Fonts.getFont("product-sans.ttf", 20);
        productSans70 = Fonts.getFont("product-sans.ttf", 37);
        productSans80 = Fonts.getFont("product-sans.ttf", 40);
        notificationIcon70 = Fonts.getFont("notification-icon.ttf", 35);
        posterama20 = Fonts.getFont("posterama.ttf", 20);
        posterama30 = Fonts.getFont("posterama.ttf", 15);
        posterama35 = Fonts.getFont("posterama.ttf", 18);
        posterama40 = Fonts.getFont("posterama.ttf", 20);
        tenacity40 = Fonts.getFont("tenacitybold.ttf", 40);
        tenacity80 = Fonts.getFont("tenacitybold.ttf", 40);
        tenacitybold30 = Fonts.getFont("tenacitybold.ttf", 15);
        tenacitybold35 = Fonts.getFont("tenacitybold.ttf", 18);
        tenacitybold40 = Fonts.getFont("tenacitybold.ttf", 20);
        tenacitybold43 = Fonts.getFont("tenacitybold.ttf", 21);
        tenacitycheck60 = Fonts.getFont("tenacitycheck.ttf", 30);
        flux = Fonts.getFont("fluxicon.ttf", 18);
        flux45 = Fonts.getFont("fluxicon.ttf", 23);
        nbicon18 = Fonts.getFont("newicon.ttf", 18);
        nbicon20 = Fonts.getFont("newicon.ttf", 23);
        annabelle72 = Fonts.getOtherPathFont("atfield/Fonts/annabelle.ttf", 72);
        bold12 = Fonts.getFont("bold.ttf", 12);
        bold24 = Fonts.getFont("bold.ttf", 24);
        bold36 = Fonts.getFont("bold.ttf", 36);
        ClientUtils.getLogger().info("Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }

    public static IFontRenderer getFontRenderer(String string, int n) {
        for (Field field : Fonts.class.getDeclaredFields()) {
            try {
                FontDetails fontDetails;
                field.setAccessible(true);
                Object object = field.get(null);
                if (!(object instanceof IFontRenderer) || !(fontDetails = field.getAnnotation(FontDetails.class)).fontName().equals(string) || fontDetails.fontSize() != n) continue;
                return (IFontRenderer)object;
            }
            catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
        }
        return Fonts.getFont("default", 35);
    }

    public static class FontInfo {
        private final int fontSize;
        private final String name;

        public FontInfo(String string, int n) {
            this.name = string;
            this.fontSize = n;
        }

        public int hashCode() {
            int n = this.name != null ? this.name.hashCode() : 0;
            n = 31 * n + this.fontSize;
            return n;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            FontInfo fontInfo = (FontInfo)object;
            if (this.fontSize != fontInfo.fontSize) {
                return false;
            }
            return Objects.equals(this.name, fontInfo.name);
        }

        public int getFontSize() {
            return this.fontSize;
        }

        public FontInfo(Font font) {
            this(font.getName(), font.getSize());
        }

        public String getName() {
            return this.name;
        }
    }
}

