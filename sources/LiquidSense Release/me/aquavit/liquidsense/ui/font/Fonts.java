package me.aquavit.liquidsense.ui.font;

import com.google.gson.*;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.misc.HttpUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static me.aquavit.liquidsense.LiquidSense.CLIENT_RESOURCE;

@SideOnly(Side.CLIENT)
public class Fonts {

    @FontDetails(fontName = "Museo-Sans", fontSize = 14)
    public static GameFontRenderer font14;

    @FontDetails(fontName = "Museo-Sans", fontSize = 15)
    public static GameFontRenderer font15;

    @FontDetails(fontName = "Museo-Sans", fontSize = 16)
    public static GameFontRenderer font16;

    @FontDetails(fontName = "Museo-Sans", fontSize = 17)
    public static GameFontRenderer font17;

    @FontDetails(fontName = "Museo-Sans", fontSize = 18)
    public static GameFontRenderer font18;

    @FontDetails(fontName = "Museo-Sans", fontSize = 20)
    public static GameFontRenderer font20;

    @FontDetails(fontName = "Museo-Sans", fontSize = 22)
    public static GameFontRenderer font22;

    @FontDetails(fontName = "Museo-Sans", fontSize = 25)
    public static GameFontRenderer font25;

    @FontDetails(fontName = "Museo-Sans", fontSize = 26)
    public static GameFontRenderer font26;

    @FontDetails(fontName = "Museo-Sans", fontSize = 27)
    public static GameFontRenderer font27;

    @FontDetails(fontName = "Museo-Sans", fontSize = 28)
    public static GameFontRenderer font28;

    @FontDetails(fontName = "Museo-Sans", fontSize = 29)
    public static GameFontRenderer font29;

    @FontDetails(fontName = "Museo-Sans", fontSize = 30)
    public static GameFontRenderer font30;

    @FontDetails(fontName = "Museo-Sans", fontSize = 60)
    public static GameFontRenderer font60;

    @FontDetails(fontName = "Roboto Bold", fontSize = 60)
    public static GameFontRenderer fontBold30;

    @FontDetails(fontName = "Roboto Bold", fontSize = 90)
    public static GameFontRenderer fontBold180;

    @FontDetails(fontName = "Logo", fontSize = 90)
    public static GameFontRenderer logo;

    @FontDetails(fontName = "MuseoSans Bold", fontSize = 25)
    public static GameFontRenderer bold25;

    @FontDetails(fontName = "MuseoSans Bold", fontSize = 26)
    public static GameFontRenderer bold26;

    @FontDetails(fontName = "MuseoSans Bold", fontSize = 30)
    public static GameFontRenderer bold30;

    @FontDetails(fontName = "MuseoSans Bold", fontSize = 60)
    public static GameFontRenderer bold60;

    @FontDetails(fontName = "Cs Go", fontSize = 40)
    public static GameFontRenderer csgo40;

    @FontDetails(fontName = "Cs Go", fontSize = 35)
    public static GameFontRenderer csgo35;

    @FontDetails(fontName = "stylesicons", fontSize = 30)
    public static GameFontRenderer icon30;

    @FontDetails(fontName = "Minecraft Font")
    public static final FontRenderer minecraftFont = Minecraft.getMinecraft().fontRendererObj;

    private static final List<GameFontRenderer> CUSTOM_FONT_RENDERERS = new ArrayList<>();

    public static void loadFonts() {
        long l = System.currentTimeMillis();

        ClientUtils.getLogger().info("Loading Fonts.");

        downloadFonts();
        font14 = new GameFontRenderer(getFont("Museo-Sans.ttf", 14));
	  font15 = new GameFontRenderer(getFont("Museo-Sans.ttf", 15));
        font16 = new GameFontRenderer(getFont("Museo-Sans.ttf", 16));
        font17 = new GameFontRenderer(getFont("Museo-Sans.ttf", 17));
        font18 = new GameFontRenderer(getFont("Museo-Sans.ttf", 18));
        font20 = new GameFontRenderer(getFont("Museo-Sans.ttf", 20));
        font22 = new GameFontRenderer(getFont("Museo-Sans.ttf", 22));
        font25 = new GameFontRenderer(getFont("Museo-Sans.ttf", 25));
        font26 = new GameFontRenderer(getFont("Museo-Sans.ttf", 26));
        font27 = new GameFontRenderer(getFont("Museo-Sans.ttf", 27));
        font28 = new GameFontRenderer(getFont("Museo-Sans.ttf", 28));
        font29 = new GameFontRenderer(getFont("Museo-Sans.ttf", 29));
        font30 = new GameFontRenderer(getFont("Museo-Sans.ttf", 30));
        font60 = new GameFontRenderer(getFont("Museo-Sans.ttf", 60));
        bold25 = new GameFontRenderer(getFont("MuseoSans-Bold.ttf", 25));
        bold26 = new GameFontRenderer(getFont("MuseoSans-Bold.ttf", 26));
        bold30 = new GameFontRenderer(getFont("MuseoSans-Bold.ttf", 30));
        bold60 = new GameFontRenderer(getFont("MuseoSans-Bold.ttf", 60));
        csgo35 = new GameFontRenderer(getFont("LiquidSense.ttf", 18));
        csgo40 = new GameFontRenderer(getFont("LiquidSense.ttf", 20));
	  fontBold30 = new GameFontRenderer(getFont("Roboto-Bold.ttf", 28));
	  fontBold180 = new GameFontRenderer(getFont("Roboto-Bold.ttf", 60));
        logo = new GameFontRenderer(getFont("Facon.ttf", 60));
        icon30 = new GameFontRenderer(getFont("Stylesicons.ttf", 30));

        try {
            CUSTOM_FONT_RENDERERS.clear();

            final File fontsFile = new File(LiquidSense.fileManager.fontsDir, "fonts.json");

            if (fontsFile.exists()) {
                final JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(fontsFile)));

                if (jsonElement instanceof JsonNull)
                    return;

                final JsonArray jsonArray = (JsonArray) jsonElement;

                for (final JsonElement element : jsonArray) {
                    if (element instanceof JsonNull)
                        return;

                    final JsonObject fontObject = (JsonObject) element;

                    CUSTOM_FONT_RENDERERS.add(new GameFontRenderer(getFont(fontObject.get("fontFile").getAsString(), fontObject.get("fontSize").getAsInt())));
                }
            } else {
                fontsFile.createNewFile();

                final PrintWriter printWriter = new PrintWriter(new FileWriter(fontsFile));
                printWriter.println(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonArray()));
                printWriter.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        ClientUtils.getLogger().info("Loaded Fonts. (" + (System.currentTimeMillis() - l) + "ms)");
    }

    private static void downloadFonts() {
        try {
            final File outputFile = new File(LiquidSense.fileManager.fontsDir, "font.zip");

            if (!outputFile.exists()) {
                ClientUtils.getLogger().info("Downloading fonts...");
                HttpUtils.download(CLIENT_RESOURCE + "Font.zip", outputFile);
                ClientUtils.getLogger().info("Extract fonts...");
                extractZip(outputFile.getPath(), LiquidSense.fileManager.fontsDir.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FontRenderer getFontRenderer(final String name, final int size) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                final Object o = field.get(null);

                if (o instanceof FontRenderer) {
                    final FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    if (fontDetails.fontName().equals(name) && fontDetails.fontSize() == size)
                        return (FontRenderer) o;
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (final GameFontRenderer liquidFontRenderer : CUSTOM_FONT_RENDERERS) {
            final Font font = liquidFontRenderer.getDefaultFont().getFont();

            if (font.getName().equals(name) && font.getSize() == size)
                return liquidFontRenderer;
        }

        return minecraftFont;
    }

    public static Object[] getFontDetails(final FontRenderer fontRenderer) {
        for (final Field field : Fonts.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                final Object o = field.get(null);

                if (o.equals(fontRenderer)) {
                    final FontDetails fontDetails = field.getAnnotation(FontDetails.class);

                    return new Object[]{fontDetails.fontName(), fontDetails.fontSize()};
                }
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (fontRenderer instanceof GameFontRenderer) {
            final Font font = ((GameFontRenderer) fontRenderer).getDefaultFont().getFont();

            return new Object[]{font.getName(), font.getSize()};
        }

        return null;
    }

    public static List<FontRenderer> getFonts() {
        final List<FontRenderer> fonts = new ArrayList<>();

        for (final Field fontField : Fonts.class.getDeclaredFields()) {
            try {
                fontField.setAccessible(true);

                final Object fontObj = fontField.get(null);

                if (fontObj instanceof FontRenderer) fonts.add((FontRenderer) fontObj);
            } catch (final IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        fonts.addAll(Fonts.CUSTOM_FONT_RENDERERS);

        return fonts;
    }

    private static Font getFont(final String fontName, final int size) {
        try {
            final InputStream inputStream = new FileInputStream(new File(LiquidSense.fileManager.fontsDir, fontName));
            Font awtClientFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtClientFont = awtClientFont.deriveFont(Font.PLAIN, size);
            inputStream.close();
            return awtClientFont;
        } catch (final Exception e) {
            e.printStackTrace();

            return new Font("default", Font.PLAIN, size);
        }
    }

    private static void extractZip(final String zipFile, final String outputFolder) {
        final byte[] buffer = new byte[1024];

        try {
            final File folder = new File(outputFolder);

            if (!folder.exists()) folder.mkdir();

            final ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));

            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(outputFolder + File.separator + zipEntry.getName());
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fileOutputStream = new FileOutputStream(newFile);

                int i;
                while ((i = zipInputStream.read(buffer)) > 0)
                    fileOutputStream.write(buffer, 0, i);

                fileOutputStream.close();
                zipEntry = zipInputStream.getNextEntry();
            }

            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}