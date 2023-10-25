package tech.drainwalk.font;

public class FontManager {
    public static WFontRenderer
            ICONS_26,
            ICONS_16,
            ICONS_12,
            ICONS,
            ICONS_21,
            REGULAR_20,
            REGULAR_22,
            REGULAR_18,
            REGULAR_14,
            REGULAR_16,
            SEMI_BOLD_16,
            SEMI_BOLD_15,
            SEMI_BOLD_14,
            SEMI_BOLD_12,
            SEMI_BOLD_10,
            SEMI_BOLD_18,
            SEMI_BOLD_28,
            MEDIUM_20,

            BOLD_16,

            BOLD_20;

    public static void initFonts() {
        ICONS_26 = new WFontRenderer(WFont.readFontFromFile("icons.ttf", 26), true, true);
        ICONS_16 = new WFontRenderer(WFont.readFontFromFile("icons.ttf", 16), true, true);
        ICONS_12 = new WFontRenderer(WFont.readFontFromFile("icons.ttf", 12), true, true);
        ICONS = new WFontRenderer(WFont.readFontFromFile("icons.ttf", 21), true, true);
        ICONS_21 = new WFontRenderer(WFont.readFontFromFile("icons.ttf", 21), true, true);
        BOLD_20 = new WFontRenderer(WFont.readFontFromFile("sf_pro_display_bold.ttf", 20), true, true);
        BOLD_16 = new WFontRenderer(WFont.readFontFromFile("sf_pro_display_bold.ttf", 20), true, true);
        REGULAR_20 = new WFontRenderer(WFont.readFontFromFile("regular.ttf", 20), true, true);
        REGULAR_18 = new WFontRenderer(WFont.readFontFromFile("regular.ttf", 18), true, true);
        REGULAR_22 = new WFontRenderer(WFont.readFontFromFile("regular.ttf", 22), true, true);
        REGULAR_14 = new WFontRenderer(WFont.readFontFromFile("regular.ttf", 10), true, true);
        REGULAR_16 = new WFontRenderer(WFont.readFontFromFile("regular.ttf", 16), true, true);
        SEMI_BOLD_16 = new WFontRenderer(WFont.readFontFromFile("sf_pro_display_semibold.otf", 16), true, true);
        SEMI_BOLD_15 = new WFontRenderer(WFont.readFontFromFile("sf_pro_display_semibold.otf", 15), true, true);
        SEMI_BOLD_14 = new WFontRenderer(WFont.readFontFromFile("sf_pro_display_semibold.otf", 14), true, true);
        SEMI_BOLD_12 = new WFontRenderer(WFont.readFontFromFile("sf_pro_display_semibold.otf", 12), true, true);
        SEMI_BOLD_10 = new WFontRenderer(WFont.readFontFromFile("sf_pro_display_semibold.otf", 10), true, true);
        SEMI_BOLD_18 = new WFontRenderer(WFont.readFontFromFile("sf_pro_display_semibold.otf", 18), true, true);
        SEMI_BOLD_28 = new WFontRenderer(WFont.readFontFromFile("sf_pro_display_semibold.otf", 28), true, true);
        MEDIUM_20 = new WFontRenderer(WFont.readFontFromFile("medium.ttf", 20), true, true);
    }

}
