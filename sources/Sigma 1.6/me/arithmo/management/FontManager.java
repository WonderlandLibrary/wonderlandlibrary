/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import me.arithmo.util.render.TTFFontRenderer;
import net.minecraft.util.ResourceLocation;

public class FontManager {
    private ResourceLocation darrow = new ResourceLocation("SF-UI-Display-Regular.otf");
    private TTFFontRenderer defaultFont;
    private FontManager instance;
    private HashMap<String, TTFFontRenderer> fonts = new HashMap();

    public FontManager getInstance() {
        return this.instance;
    }

    public TTFFontRenderer getFont(String key) {
        return this.fonts.getOrDefault(key, this.defaultFont);
    }

    public FontManager() {
        this.instance = this;
        this.defaultFont = new TTFFontRenderer(new Font("Verdana", 0, 18));
        try {
            int i;
            InputStream istream;
            Font myFont;
            for (i = 4; i <= 16; ++i) {
                istream = this.getClass().getResourceAsStream("/assets/minecraft/SF-UI-Display-Regular.otf");
                myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(0, i);
                this.fonts.put("SFR " + i, new TTFFontRenderer(myFont));
            }
            for (i = 4; i <= 16; ++i) {
                istream = this.getClass().getResourceAsStream("/assets/minecraft/SF-UI-Display-Bold.otf");
                myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(0, i);
                this.fonts.put("SFB " + i, new TTFFontRenderer(myFont));
            }
            for (i = 4; i <= 16; ++i) {
                istream = this.getClass().getResourceAsStream("/assets/minecraft/SF-UI-Display-Semibold.otf");
                myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(0, i);
                this.fonts.put("SFSB " + i, new TTFFontRenderer(myFont));
            }
            for (i = 4; i <= 16; ++i) {
                istream = this.getClass().getResourceAsStream("/assets/minecraft/SF-UI-Display-Medium.otf");
                myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(0, i);
                this.fonts.put("SFM " + i, new TTFFontRenderer(myFont));
            }
            for (i = 4; i <= 16; ++i) {
                istream = this.getClass().getResourceAsStream("/assets/minecraft/SF-UI-Display-Thin.otf");
                myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(0, i);
                this.fonts.put("SFT " + i, new TTFFontRenderer(myFont));
            }
            for (i = 4; i <= 16; ++i) {
                istream = this.getClass().getResourceAsStream("/assets/minecraft/SF-UI-Display-Light.otf");
                myFont = Font.createFont(0, istream);
                myFont = myFont.deriveFont(0, i);
                this.fonts.put("SFL " + i, new TTFFontRenderer(myFont));
            }
            for (i = 4; i <= 24; ++i) {
                this.fonts.put("Verdana " + i, new TTFFontRenderer(new Font("Verdana", 0, i)));
            }
            for (i = 4; i < 24; ++i) {
                this.fonts.put("Verdana Bold " + i, new TTFFontRenderer(new Font("Verdana", 1, i)));
            }
        }
        catch (Exception i) {
            // empty catch block
        }
    }
}

