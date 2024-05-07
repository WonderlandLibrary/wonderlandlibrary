package com.masterof13fps.utils.fonts;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import java.awt.*;
import java.io.InputStream;

public class FontManager {

    public UnicodeFontRenderer mainFont;
    public UnicodeFontRenderer arrayFont;
    public UnicodeFontRenderer testarrayFont;

    public UnicodeFontRenderer comfortaa18;
    public UnicodeFontRenderer comfortaa20;
    public UnicodeFontRenderer comfortaa22;
    public UnicodeFontRenderer comfortaa40;
    public UnicodeFontRenderer comfortaa45;
    public UnicodeFontRenderer comfortaa50;


    public UnicodeFontRenderer notificationFont;

    public UnicodeFontRenderer americanBig;
    public UnicodeFontRenderer americanSmall;

    public UnicodeFontRenderer cInfoFont;

    /*
     * TabGui Fonts
     */
    public UnicodeFontRenderer playerWorldFont;
    public UnicodeFontRenderer eyeFont;
    public UnicodeFontRenderer movementFont;
//	public static UnicodeFontRenderer settingsFont = new UnicodeFontRenderer(getFont("Breezi", 22), true, 8);


    public UnicodeFontRenderer centuryFont;
    public UnicodeFontRenderer licenseFont;
    public UnicodeFontRenderer smallLicenseFont;
    public UnicodeFontRenderer statusFont;

    public UnicodeFontRenderer blockoverlayFont;


    public UnicodeFontRenderer guiFont;
    public UnicodeFontRenderer frameFont;
    public UnicodeFontRenderer altFont;
    public UnicodeFontRenderer bigKeyFont;


    public UnicodeFontRenderer sliderFont;

    public void initFonts() {
        this.mainFont = new UnicodeFontRenderer(getFont("Rainbow Veins", 50, FontExtension.TTF), true, 8);
        this.arrayFont = new UnicodeFontRenderer(getFont("Verdana", 19, FontExtension.TTF), true, 8);
        this.testarrayFont = new UnicodeFontRenderer(getFont("Cabin", 19, FontExtension.TTF), true, 8);

        this.notificationFont = new UnicodeFontRenderer(getFont("Verdana", 19, FontExtension.TTF), true, 8);
        this.blockoverlayFont = new UnicodeFontRenderer(getFont("Arial Bold", 15, FontExtension.TTF), true, 8);

        this.cInfoFont = new UnicodeFontRenderer(getFont("Verdana", 23, FontExtension.TTF), true, 8);

        /*
         * TabGui Fonts
         */
        this.playerWorldFont = new UnicodeFontRenderer(getFont("Icons South St", 14, FontExtension.TTF), true, 8);
        this.eyeFont = new UnicodeFontRenderer(getFont("modernpics", 22, FontExtension.OTF), true, 8);
        this.movementFont = new UnicodeFontRenderer(getFont("GlyphyxOneNF", 19, FontExtension.TTF), true, 8);
//		public static UnicodeFontRenderer settingsFont = new UnicodeFontRenderer(getFont("Breezi", 22), true, 8);

        this.centuryFont = new UnicodeFontRenderer(getFont("Century Gothic", 45, FontExtension.TTF), true, 8);
        this.licenseFont = new UnicodeFontRenderer(getFont("Century Gothic", 30, FontExtension.TTF), true, 8);
        this.smallLicenseFont = new UnicodeFontRenderer(getFont("Century Gothic", 20, FontExtension.TTF), true, 8);
        this.statusFont = new UnicodeFontRenderer(getFont("Century Gothic", 20, FontExtension.TTF), true, 8);

        this.americanBig = new UnicodeFontRenderer(getFont("American", 100, FontExtension.TTF), true, 8);
        this.americanSmall = new UnicodeFontRenderer(getFont("American", 66, FontExtension.TTF), true, 8);

        this.comfortaa18 = new UnicodeFontRenderer(getFont("Comfortaa", 18, FontExtension.TTF), true, 8);
        this.comfortaa20 = new UnicodeFontRenderer(getFont("Comfortaa", 20, FontExtension.TTF), true, 8);
        this.comfortaa22 = new UnicodeFontRenderer(getFont("Comfortaa", 22, FontExtension.TTF), true, 8);
        this.comfortaa40 = new UnicodeFontRenderer(getFont("Comfortaa", 40, FontExtension.TTF), true, 8);
        this.comfortaa45 = new UnicodeFontRenderer(getFont("Comfortaa", 45, FontExtension.TTF), true, 8);
        this.comfortaa50 = new UnicodeFontRenderer(getFont("Comfortaa", 50, FontExtension.TTF), true, 8);

        /*
         * CLickGui Font
         */
        this.guiFont = new UnicodeFontRenderer(getFont("Cabin", 18, FontExtension.TTF), true, 8);
        this.frameFont = new UnicodeFontRenderer(getFont("Cabin", 23, FontExtension.TTF), true, 8);
        this.altFont = new UnicodeFontRenderer(getFont("Cabin", 35, FontExtension.TTF), true, 8);
        this.bigKeyFont = new UnicodeFontRenderer(getFont("Cabin", 35, FontExtension.TTF), true, 8);
        this.sliderFont = new UnicodeFontRenderer(getFont("Verdana", 17, FontExtension.TTF), true, 8);
    }

    private static Font getFont(String name, int size, FontExtension fe) {
        Font font = null;
        try {
            InputStream ex = Minecraft.mc().getResourceManager()
                    .getResource(new ResourceLocation("Client/fonts/" + name + "." + fe.name().toLowerCase())).getInputStream();
            font = Font.createFont(0, ex);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Font not loaded.  Using serif font.");
            font = new Font("default", 0, size);
        }
        return font;
    }

    public enum FontExtension {
        TTF, OTF;
    }
}
