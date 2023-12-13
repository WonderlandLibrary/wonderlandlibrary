package vip.radium.gui.font;

import vip.radium.utils.render.TTFUtils;

import java.awt.*;

public final class FontManager {

    public static final TrueTypeFontRenderer FR =
            new TrueTypeFontRenderer(TTFUtils.getFontFromLocation("font.ttf", 21), true, true);
    public static final TrueTypeFontRenderer MEDIUM_FR =
            new TrueTypeFontRenderer(TTFUtils.getFontFromLocation("font.ttf", 20), true, true);
    public static final TrueTypeFontRenderer SMALL_FR =
            new TrueTypeFontRenderer(TTFUtils.getFontFromLocation("font.ttf", 18), true, true);
    public static final TrueTypeFontRenderer CSGO_FR = new TrueTypeFontRenderer(
            new Font("Tahoma", Font.BOLD, 11), true, false);
    public static final TrueTypeFontRenderer FN_FR = new TrueTypeFontRenderer(
            TTFUtils.getFontFromLocation("Burbank.ttf", 36), true, false);
    public static final TrueTypeFontRenderer SP_FR = new TrueTypeFontRenderer(
            TTFUtils.getFontFromLocation("smallest_pixel.ttf", 16), true, true);

    private FontManager() {}

    public static void initTextures() {
        FR.generateTextures();
        SP_FR.generateTextures();
        MEDIUM_FR.generateTextures();
        SMALL_FR.generateTextures();

        CSGO_FR.generateTextures();
        FN_FR.generateTextures();
    }
}
