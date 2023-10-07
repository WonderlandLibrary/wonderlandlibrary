/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.ui.font;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.font.FontDetails;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.ui.font.TTFFontRenderer;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.FileUtils;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class Fonts {
    @FontDetails(fontName="Roboto Medium", fontSize=35)
    public static GameFontRenderer font35;
    @FontDetails(fontName="Font", fontSize=14)
    public static GameFontRenderer font14;
    @FontDetails(fontName="Font", fontSize=23)
    public static GameFontRenderer font23;
    @FontDetails(fontName="Font", fontSize=30)
    public static GameFontRenderer fontSmall;
    @FontDetails(fontName="Font", fontSize=72)
    public static GameFontRenderer font72;
    @FontDetails(fontName="Roboto Medium", fontSize=24)
    public static GameFontRenderer fontTiny;
    @FontDetails(fontName="Roboto Medium", fontSize=30)
    public static GameFontRenderer font30;
    @FontDetails(fontName="Roboto Medium", fontSize=40)
    public static GameFontRenderer font40;
    @FontDetails(fontName="Tahoma Bold", fontSize=30)
    public static GameFontRenderer fontTahoma30;
    public static TTFFontRenderer fontTahomaSmall;
    @FontDetails(fontName="Tahoma Bold", fontSize=35)
    public static GameFontRenderer fontTahoma;
    @FontDetails(fontName="Roboto Medium", fontSize=32)
    public static GameFontRenderer font32;
    @FontDetails(fontName="Roboto Medium", fontSize=37)
    public static GameFontRenderer font37;
    @FontDetails(fontName="Roboto Medium", fontSize=52)
    public static GameFontRenderer fontLarge;
    @FontDetails(fontName="Roboto Bold", fontSize=180)
    public static GameFontRenderer fontBold180;
    @FontDetails(fontName="Minecraft Font")
    public static final FontRenderer minecraftFont;
    @FontDetails(fontName="GangofThree.ttf", fontSize=40)
    public static GameFontRenderer GangofThree;
    @FontDetails(fontName="flux", fontSize=45)
    public static GameFontRenderer flux;
    @FontDetails(fontName="SFUI40", fontSize=20)
    public static GameFontRenderer SFUI40;
    @FontDetails(fontName="SFUI35", fontSize=18)
    public static GameFontRenderer SFUI35;
    @FontDetails(fontName="SFUI24", fontSize=10)
    public static GameFontRenderer SFUI24;
    @FontDetails(fontName="ICONFONT_50", fontSize=70)
    public static GameFontRenderer ICONFONT_50;
    @FontDetails(fontName="sbcnm", fontSize=70)
    public static GameFontRenderer sbcnm;
    @FontDetails(fontName="MiSansNormal Regular", fontSize=35)
    public static GameFontRenderer fontMiSansNormal35;
    private static final List<GameFontRenderer> CUSTOM_FONT_RENDERERS;
    public static GameFontRenderer icon20;

    public static void loadFonts() {
        long l = System.currentTimeMillis();
        ClientUtils.getLogger().info("Loading Fonts.");
        Fonts.downloadFonts();
        Fonts.initFonts();
        font14 = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 14));
        fontTiny = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 24));
        font23 = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 23));
        fontSmall = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 30));
        flux = new GameFontRenderer(Fonts.getFont3(45));
        font30 = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 30));
        font32 = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 35));
        font37 = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 35));
        font35 = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 35));
        font40 = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 40));
        font72 = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 72));
        fontMiSansNormal35 = new GameFontRenderer(Fonts.getFont("MiSans-Normal.ttf", 35));
        fontBold180 = new GameFontRenderer(Fonts.getFont("Roboto-Bold.ttf", 180));
        sbcnm = new GameFontRenderer(Fonts.getFont("sbcnm.ttf", 70));
        GangofThree = new GameFontRenderer(Fonts.getFont("GangofThree.ttf", 40));
        fontLarge = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 60));
        ICONFONT_50 = new GameFontRenderer(Fonts.getFont("stylesicons.ttf", 70));
        SFUI40 = new GameFontRenderer(Fonts.getFont("sfui.ttf", 20));
        SFUI35 = new GameFontRenderer(Fonts.getFont("sfui.ttf", 18));
        SFUI24 = new GameFontRenderer(Fonts.getFont("sfui.ttf", 10));
        SFUI24 = new GameFontRenderer(Fonts.getFont("sfui.ttf", 40));
        fontTahoma = new GameFontRenderer(Fonts.getFont("TahomaBold.ttf", 35));
        fontTahoma30 = new GameFontRenderer(Fonts.getFont("TahomaBold.ttf", 30));
        fontTahomaSmall = new TTFFontRenderer(Fonts.getFont("Tahoma.ttf", 11));
        try {
            CUSTOM_FONT_RENDERERS.clear();
            File fontsFile = new File(LiquidBounce.fileManager.fontsDir, "fonts.json");
            if (fontsFile.exists()) {
                JsonElement jsonElement = new JsonParser().parse((Reader)new BufferedReader(new FileReader(fontsFile)));
                if (jsonElement instanceof JsonNull) {
                    return;
                }
                JsonArray jsonArray = (JsonArray)jsonElement;
                for (JsonElement element : jsonArray) {
                    if (element instanceof JsonNull) {
                        return;
                    }
                    JsonObject fontObject = (JsonObject)element;
                    CUSTOM_FONT_RENDERERS.add(new GameFontRenderer(Fonts.getFont(fontObject.get("fontFile").getAsString(), fontObject.get("fontSize").getAsInt())));
                }
            } else {
                fontsFile.createNewFile();
                PrintWriter printWriter = new PrintWriter(new FileWriter(fontsFile));
                printWriter.println(new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)new JsonArray()));
                printWriter.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ClientUtils.getLogger().info("Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }

    private static void initFonts() {
        try {
            Fonts.initSingleFont("stylesicons.ttf");
            Fonts.initSingleFont("sbcnm.ttf");
            Fonts.initSingleFont("sfui.ttf");
            Fonts.initSingleFont("GangofThree.ttf");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initSingleFont(String name) throws IOException {
        FileUtils.unpackFile(new File(LiquidBounce.fileManager.fontsDir, name), name);
    }

    private static void downloadFonts() {
        try {
            File outputFile = new File(LiquidBounce.fileManager.fontsDir, "roboto.zip");
            if (!outputFile.exists()) {
                ClientUtils.getLogger().info("Downloading fonts...");
                HttpUtils.download("https://cloud.liquidbounce.net/LiquidBounce/fonts/Roboto.zip", outputFile);
                ClientUtils.getLogger().info("Extract fonts...");
                Fonts.extractZip(outputFile.getPath(), LiquidBounce.fileManager.fontsDir.getPath());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FontRenderer getFontRenderer(String name, int size) {
        for (Field field : Fonts.class.getDeclaredFields()) {
            try {
                FontDetails fontDetails;
                field.setAccessible(true);
                Object o = field.get(null);
                if (!(o instanceof FontRenderer) || !(fontDetails = field.getAnnotation(FontDetails.class)).fontName().equals(name) || fontDetails.fontSize() != size) continue;
                return (FontRenderer)o;
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        for (GameFontRenderer liquidFontRenderer : CUSTOM_FONT_RENDERERS) {
            Font font = liquidFontRenderer.getDefaultFont().getFont();
            if (!font.getName().equals(name) || font.getSize() != size) continue;
            return liquidFontRenderer;
        }
        return minecraftFont;
    }

    public static Object[] getFontDetails(FontRenderer fontRenderer) {
        for (Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object o = field.get(null);
                if (!o.equals(fontRenderer)) continue;
                FontDetails fontDetails = field.getAnnotation(FontDetails.class);
                return new Object[]{fontDetails.fontName(), fontDetails.fontSize()};
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (fontRenderer instanceof GameFontRenderer) {
            Font font = ((GameFontRenderer)fontRenderer).getDefaultFont().getFont();
            return new Object[]{font.getName(), font.getSize()};
        }
        return null;
    }

    public static List<FontRenderer> getFonts() {
        ArrayList<FontRenderer> fonts = new ArrayList<FontRenderer>();
        for (Field fontField : Fonts.class.getDeclaredFields()) {
            try {
                fontField.setAccessible(true);
                Object fontObj = fontField.get(null);
                if (!(fontObj instanceof FontRenderer)) continue;
                fonts.add((FontRenderer)fontObj);
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        fonts.addAll(CUSTOM_FONT_RENDERERS);
        return fonts;
    }

    private static Font getFont3(int size) {
        Font font;
        try {
            InputStream is = Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation("liquidbounce/icons/flux.ttf")).func_110527_b();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }

    private static Font getFont1(String fontName, int size) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(LiquidBounce.fileManager.fontsDir, fontName));
            Font awtClientFont = Font.createFont(0, inputStream);
            awtClientFont = awtClientFont.deriveFont(0, size);
            ((InputStream)inputStream).close();
            return awtClientFont;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Font("default", 0, size);
        }
    }

    private static Font getFont(String fontName, int size) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(LiquidBounce.fileManager.fontsDir, fontName));
            Font awtClientFont = Font.createFont(0, inputStream);
            awtClientFont = awtClientFont.deriveFont(0, size);
            ((InputStream)inputStream).close();
            return awtClientFont;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Font("default", 0, size);
        }
    }

    private static void extractZip(String zipFile, String outputFolder) {
        byte[] buffer = new byte[1024];
        try {
            File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                int i;
                File newFile = new File(outputFolder + File.separator + zipEntry.getName());
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                while ((i = zipInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, i);
                }
                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
            zipInputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void drawStringWithColor(String o, int mouseX, int mouseY, int rgb, boolean b) {
    }

    static {
        minecraftFont = Minecraft.func_71410_x().field_71466_p;
        CUSTOM_FONT_RENDERERS = new ArrayList<GameFontRenderer>();
    }
}

