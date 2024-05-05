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
 *  net.minecraft.util.ResourceLocation
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.ui.font.FontDetails;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Fonts
extends MinecraftInstance {
    @FontDetails(fontName="Minecraft Font")
    public static final IFontRenderer minecraftFont = mc.getFontRendererObj();
    private static final HashMap<FontInfo, IFontRenderer> CUSTOM_FONT_RENDERERS = new HashMap();
    @FontDetails(fontName="Roboto Medium", fontSize=35)
    public static IFontRenderer font35;
    @FontDetails(fontName="neverlose500", fontSize=35)
    public static IFontRenderer neverlose35;
    @FontDetails(fontName="Bold", fontSize=28)
    public static IFontRenderer bold28;
    @FontDetails(fontName="SFUI35", fontSize=35)
    public static GameFontRenderer SFUI35;
    @FontDetails(fontName="Roboto Medium", fontSize=40)
    public static IFontRenderer font40;
    @FontDetails(fontName="Roboto Bold", fontSize=180)
    public static IFontRenderer fontBold180;
    @FontDetails(fontName="Bold", fontSize=35)
    public static IFontRenderer bold35;
    @FontDetails(fontName="Bold", fontSize=40)
    public static IFontRenderer bold40;
    @FontDetails(fontName="Roboto Medium", fontSize=25)
    public static IFontRenderer font25;
    @FontDetails(fontName="SFUI Regular", fontSize=35)
    public static IFontRenderer fontSFUI35;
    @FontDetails(fontName="Roboto Medium", fontSize=30)
    public static GameFontRenderer font30;
    @FontDetails(fontName="Ico", fontSize=60)
    public static GameFontRenderer ico1;
    @FontDetails(fontName="sfbold35", fontSize=40)
    public static GameFontRenderer sfbold35;
    @FontDetails(fontName="sfbold28", fontSize=40)
    public static GameFontRenderer sfbold28;
    @FontDetails(fontName="Tenacitybold", fontSize=18)
    public static GameFontRenderer tenacitybold35;
    @FontDetails(fontName="Tenacitybold", fontSize=20)
    public static GameFontRenderer tenacitybold40;
    @FontDetails(fontName="Tenacitybold", fontSize=18)
    public static GameFontRenderer tenacitybold30;
    @FontDetails(fontName="sfbold40", fontSize=40)
    public static IFontRenderer sfbold40;

    public static void loadFonts() {
        long l = System.currentTimeMillis();
        font30 = new GameFontRenderer(Fonts.getchinese(30));
        ClientUtils.getLogger().info("Loading Fonts.");
        Fonts.downloadFonts();
        font35 = classProvider.wrapFontRenderer(new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 35)));
        font40 = classProvider.wrapFontRenderer(new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 40)));
        bold35 = classProvider.wrapFontRenderer(new GameFontRenderer(Fonts.getBold(35)));
        SFUI35 = new GameFontRenderer(Fonts.getFont("sfui.ttf", 40));
        ico1 = new GameFontRenderer(Fonts.gets(60));
        neverlose35 = classProvider.wrapFontRenderer(new GameFontRenderer(Fonts.getneverlose500(35)));
        tenacitybold35 = new GameFontRenderer(Fonts.gett(35));
        tenacitybold30 = new GameFontRenderer(Fonts.gett(30));
        tenacitybold40 = new GameFontRenderer(Fonts.gett(40));
        tenacitybold35 = new GameFontRenderer(Fonts.gett(35));
        bold28 = classProvider.wrapFontRenderer(new GameFontRenderer(Fonts.getBold(28)));
        sfbold28 = new GameFontRenderer(Fonts.getsfbold(28));
        sfbold35 = new GameFontRenderer(Fonts.getsfbold(35));
        fontSFUI35 = classProvider.wrapFontRenderer(new GameFontRenderer(Fonts.getSFUI(35)));
        bold40 = classProvider.wrapFontRenderer(new GameFontRenderer(Fonts.getBold(40)));
        font25 = classProvider.wrapFontRenderer(new GameFontRenderer(Fonts.getSFUI(25)));
        fontBold180 = classProvider.wrapFontRenderer(new GameFontRenderer(Fonts.getFont("Roboto-Bold.ttf", 180)));
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
                    Font font = Fonts.getFont(fontObject.get("fontFile").getAsString(), fontObject.get("fontSize").getAsInt());
                    CUSTOM_FONT_RENDERERS.put(new FontInfo(font), classProvider.wrapFontRenderer(new GameFontRenderer(font)));
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

    private static Font gett(int size) {
        Font font;
        try {
            InputStream is = Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation("liquidbounce/font/tenacitybold.ttf")).func_110527_b();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("Posterama", 0, size);
        }
        return font;
    }

    private static Font getneverlose500(int size) {
        Font font;
        try {
            InputStream is = Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation("nobility/font/neverlose500.ttf")).func_110527_b();
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

    private static Font getSFUI(int size) {
        Font font;
        try {
            InputStream is = Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation("liquidbounce/font/sfuidisplayregular.ttf")).func_110527_b();
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

    private static Font getchinese(int size) {
        Font font;
        try {
            InputStream is = Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation("liquidbounce/font/fz.ttf")).func_110527_b();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("Posterama", 0, size);
        }
        return font;
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

    private static Font getsfbold(int size) {
        Font font;
        try {
            InputStream is = Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation("liquidbounce/font/fz.ttf")).func_110527_b();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("Posterama", 0, size);
        }
        return font;
    }

    private static Font gets(int size) {
        Font font;
        try {
            InputStream is = Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation("liquidbounce/font/icon.ttf")).func_110527_b();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("Posterama", 0, size);
        }
        return font;
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
        return CUSTOM_FONT_RENDERERS.getOrDefault(new FontInfo(name, size), minecraftFont);
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
        for (Map.Entry entry : CUSTOM_FONT_RENDERERS.entrySet()) {
            if (entry.getValue() != fontRenderer) continue;
            return (FontInfo)entry.getKey();
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
        fonts.addAll(CUSTOM_FONT_RENDERERS.values());
        return fonts;
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

    private static Font getBold(int size) {
        Font font;
        try {
            InputStream is = Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation("liquidbounce/font/bold.ttf")).func_110527_b();
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

