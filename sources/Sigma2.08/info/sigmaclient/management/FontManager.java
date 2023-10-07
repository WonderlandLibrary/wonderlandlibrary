package info.sigmaclient.management;

import info.sigmaclient.util.render.TTFFontRenderer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

public class FontManager {

    private ResourceLocation darrow = new ResourceLocation("SF-UI-Display-Regular.otf");

    private TTFFontRenderer defaultFont;

    public FontManager getInstance() {
        return instance;
    }

    public TTFFontRenderer getFont(String key) {
        return fonts.getOrDefault(key, defaultFont);
    }

    private FontManager instance;

    private HashMap<String, TTFFontRenderer> fonts = new HashMap<>();

    public FontManager() {
        instance = this;
        defaultFont = new TTFFontRenderer(new Font("Verdana", Font.PLAIN, 18));
        try {
            for (int i : new int[]{6, 7, 8, 10, 11, 12, 14}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/SF-UI-Display-Regular.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("SFR " + i, new TTFFontRenderer(myFont));
            }
            for (int i : new int[]{7, 8, 11}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/SF-UI-Display-Bold.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("SFB " + i, new TTFFontRenderer(myFont));
            }
            for (int i : new int[]{6, 7, 8, 9, 12}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/SF-UI-Display-Medium.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("SFM " + i, new TTFFontRenderer(myFont));
            }
            for (int i : new int[]{10, 8}) {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/SF-UI-Display-Light.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, i);
                fonts.put("SFL " + i, new TTFFontRenderer(myFont));
            }
            fonts.put("Verdana 12", new TTFFontRenderer(new Font("Verdana", Font.PLAIN, 12)));
            fonts.put("Verdana Bold 16", new TTFFontRenderer(new Font("Verdana Bold", Font.PLAIN, 16)));
        } catch (Exception ignored) {

        }
    }
}
