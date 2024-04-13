package dev.tenacity.util.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontUtil {
    public static volatile int completed;
    //These are for the icon font for ease of access
    public final static String
            BUG = "a",
            LIST = "b",
            BOMB = "c",
            EYE = "d",
            PERSON = "e",
            WHEELCHAIR = "f",
            SCRIPT = "g",
            SKIP_LEFT = "h",
            PAUSE = "i",
            PLAY = "j",
            SKIP_RIGHT = "k",
            SHUFFLE = "l",
            INFO = "m",
            SETTINGS = "n",
            CHECKMARK = "o",
            XMARK = "p",
            TRASH = "q",
            WARNING = "r",
            FOLDER = "s",
            LOAD = "t",
    SAVE = "u";


    public static MinecraftFontRenderer tenacityFont14, tenacityFont16,  tenacityFont18, tenacityBoldFont18, tenacityFont20, tenacityBoldFont20, tenacityFont22, tenacityBoldFont22, tenacityFont24, tenacityBoldFont26, tenacityBoldFont32, tenacityFont40, tenacityBoldFont40, iconFont16, iconFont20, iconFont26, iconFont35, iconFont40;
    public static Font tenacityBoldFont40_;


    public FontUtil() {


    }

    public static Font getFont(Map<String, Font> locationMap, String location, int size) {
        Font font;
        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(Font.PLAIN, size);
            } else {
                InputStream is = Minecraft.getMinecraft().getResourceManager()
                        .getResource(new ResourceLocation("Tenacity/Fonts/" + location)).getInputStream();
                locationMap.put(location, font = Font.createFont(0, is));
                font = font.deriveFont(Font.PLAIN, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, +10);
        }
        return font;
    }

    public static boolean hasLoaded() {
        return completed >= 3;
    }
}
