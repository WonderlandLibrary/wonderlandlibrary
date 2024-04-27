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
package net.dev.important.gui.font;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.dev.important.Client;
import net.dev.important.gui.font.FontDetails;
import net.dev.important.gui.font.GameFontRenderer;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.misc.HttpUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class Fonts {
    @FontDetails(fontName="Roboto Medium", fontSize=35)
    public static GameFontRenderer font35;
    @FontDetails(fontName="Roboto Medium", fontSize=40)
    public static GameFontRenderer font40;
    @FontDetails(fontName="Roboto Medium", fontSize=72)
    public static GameFontRenderer font72;
    @FontDetails(fontName="Roboto Medium", fontSize=30)
    public static GameFontRenderer fontSmall;
    @FontDetails(fontName="SFUI Regular", fontSize=35)
    public static GameFontRenderer fontSFUI35;
    @FontDetails(fontName="SFUI Regular", fontSize=40)
    public static GameFontRenderer fontSFUI40;
    @FontDetails(fontName="Roboto Bold", fontSize=180)
    public static GameFontRenderer fontBold180;
    @FontDetails(fontName="Font aa", fontSize=180)
    public static GameFontRenderer fontaa;
    @FontDetails(fontName="Flux", fontSize=35)
    public static GameFontRenderer flux35;
    @FontDetails(fontName="Poppins", fontSize=35)
    public static GameFontRenderer Poppins;
    @FontDetails(fontName="Font Axi", fontSize=35)
    public static GameFontRenderer axi35;
    @FontDetails(fontName="Font Axi", fontSize=100)
    public static GameFontRenderer axi100;
    @FontDetails(fontName="Jello", fontSize=100)
    public static GameFontRenderer jello40;
    @FontDetails(fontName="Minecraft Font")
    public static final FontRenderer minecraftFont;
    private static final List<GameFontRenderer> CUSTOM_FONT_RENDERERS;

    public static void loadFonts() {
        long l = System.currentTimeMillis();
        Fonts.download();
        font35 = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 35));
        font40 = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 40));
        font72 = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 72));
        fontSmall = new GameFontRenderer(Fonts.getFont("Roboto-Medium.ttf", 30));
        fontSFUI35 = new GameFontRenderer(Fonts.getFont("sfui.ttf", 35));
        fontSFUI40 = new GameFontRenderer(Fonts.getFont("sfui.ttf", 40));
        fontBold180 = new GameFontRenderer(Fonts.getFont("Roboto-Bold.ttf", 180));
        fontaa = new GameFontRenderer(Fonts.getFont("Roboto-Bold.ttf", 38));
        flux35 = new GameFontRenderer(Fonts.superget(35, "liquidplus/fonts/flux.ttf"));
        Poppins = new GameFontRenderer(Fonts.superget(35, "liquidplus/fonts/pop.ttf"));
        axi35 = new GameFontRenderer(Fonts.superget(35, "liquidplus/fonts/axi.ttf"));
        axi100 = new GameFontRenderer(Fonts.superget(100, "liquidplus/fonts/axi.ttf"));
        jello40 = new GameFontRenderer(Fonts.superget(40, "liquidplus/fonts/jelloregular.ttf"));
        try {
            CUSTOM_FONT_RENDERERS.clear();
            File fontsFile = new File(Client.fileManager.fontsDir, "fonts.json");
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

    private static Font superget(int size, String url) {
        Font font;
        try {
            InputStream is = Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation(url)).func_110527_b();
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

    private static void download() {
        try {
            File outputFile = new File(Client.fileManager.fontsDir, "roboto.zip");
            if (!outputFile.exists()) {
                ClientUtils.getLogger().info("Downloading fonts...");
                HttpUtils.download("https://wysi-foundation.github.io/LiquidCloud/LiquidBounce/fonts/fonts.zip", outputFile);
                ClientUtils.getLogger().info("Extract fonts...");
                Fonts.extractZip(outputFile.getPath(), Client.fileManager.fontsDir.getPath());
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

    private static Font getFont(String fontName, int size) {
        try {
            InputStream inputStream = Files.newInputStream(new File(Client.fileManager.fontsDir, fontName).toPath(), new OpenOption[0]);
            Font awtClientFont = Font.createFont(0, inputStream);
            awtClientFont = awtClientFont.deriveFont(0, size);
            inputStream.close();
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
            ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(Paths.get(zipFile, new String[0]), new OpenOption[0]));
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

    static {
        minecraftFont = Minecraft.func_71410_x().field_71466_p;
        CUSTOM_FONT_RENDERERS = new ArrayList<GameFontRenderer>();
    }
}

