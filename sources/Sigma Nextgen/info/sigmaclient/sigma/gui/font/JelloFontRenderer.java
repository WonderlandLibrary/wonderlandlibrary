package info.sigmaclient.sigma.gui.font;

import java.awt.*;


public abstract class JelloFontRenderer {
    public static JelloFontRenderer createFontRenderer(Font font) {
        return new MinecraftFontRenderer(font, true, true);
    }

    public int drawStringFixed(String text, double d, double y2, int color) {
        return 0;
    }

    public int drawStringNoScale(String text, double d, double y2, int color) {
        return 0;
    }
    public int drawString(String text, double d, double y2, int color) {
        return 0;
    }
    
    public int drawPassword(String text, double d, float y2, int color) {
        return 0;
    }

    public int drawNoBSStringWithBloom(String text, double x2, double y2, int color, float alpha2) {
        return 0;
    }
    public int drawNoBSString(String text, double d, float y2, int color) {
        return 0;
    }
    
    public int drawSmoothString(String text, double d, float y2, int color) {
        return 0;
    }

    public int drawStringWithShadow(String text, double d, float y2, int color) {
        return 0;
    }

    public float drawNoBSCenteredString(String text, float x2, float y2, int color) {
        return 0;
    }
    public float drawCenteredString(String text, float x2, float y2, int color) {
        return 0;
    }
    public float drawCenteredStringWithShadow(String text, float x2, float y2, int color) {
        return 0;
    }

    public int drawStringNoScaleSB(String text, double x2, double y2, int color,boolean c){return 0;}

    public double getStringWidth(String text) {
        return 0;
    }
    public double getStringWidthNoScale(String text) {
        return 0;
    }
    
    public double getPasswordWidth(String text) {
        return 0;
    }

    public int getHeightNoScale() {
        return 0;
    }
    public int getHeight() {
        return 0;
    }
    public static Font createFontFromFile(String name, int size) {
        Font f2;
        try {
            f2 = Font.createFont(Font.TRUETYPE_FONT, new Object().getClass().getResourceAsStream("/" + name + ".ttf"));
        }
        catch (Exception e2) {
            return null;
        }
        f2 = f2.deriveFont(Font.PLAIN, size);
        return f2;
    }


    public String trimStringToWidth(String p_78262_1_, int p_78262_2_, boolean p_78262_3_) {
    	p_78262_1_ = p_78262_1_.replaceAll("\u00c3\u201a", "");
        return "";
    }
    
    public String trimStringToWidth(String p_78262_1_, int p_78262_2_) {
    	p_78262_1_ = p_78262_1_.replaceAll("\u00c3\u201a", "");
        return "";
    }
    
    public String trimStringToWidthPassword(String p_78262_1_, int p_78262_2_, boolean custom) {
    	p_78262_1_ = p_78262_1_.replaceAll("\u00c3\u201a", "");
        return "";
    }

}

