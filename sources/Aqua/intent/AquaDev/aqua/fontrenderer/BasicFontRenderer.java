package intent.AquaDev.aqua.fontrenderer;

import java.util.List;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public interface BasicFontRenderer
extends IResourceManagerReloadListener {
    public static String getFormatFromString(String text) {
        String s = "";
        int i = -1;
        int j = text.length();
        while ((i = text.indexOf(167, i + 1)) != -1) {
            if (i >= j - 1) continue;
            char c0 = text.charAt(i + 1);
            if (BasicFontRenderer.isFormatColor(c0)) {
                s = "\u00a7" + c0;
                continue;
            }
            if (!BasicFontRenderer.isFormatSpecial(c0)) continue;
            s = s + "\u00a7" + c0;
        }
        return s;
    }

    public static boolean isFormatColor(char colorChar) {
        return colorChar >= '0' && colorChar <= '9' || colorChar >= 'a' && colorChar <= 'f' || colorChar >= 'A' && colorChar <= 'F';
    }

    public static boolean isFormatSpecial(char formatChar) {
        return formatChar >= 'k' && formatChar <= 'o' || formatChar >= 'K' && formatChar <= 'O' || formatChar == 'r' || formatChar == 'R';
    }

    public int getFontHeight();

    public int drawStringWithShadow(String var1, float var2, float var3, int var4);

    public int drawString(String var1, float var2, float var3, int var4);

    public int drawString(String var1, float var2, float var3, int var4, boolean var5);

    public int drawCenteredString(String var1, float var2, float var3, int var4, boolean var5);

    public int getStringWidth(String var1);

    public int getCharWidth(char var1);

    public boolean getBidiFlag();

    public void setBidiFlag(boolean var1);

    public String wrapFormattedStringToWidth(String var1, int var2);

    public List listFormattedStringToWidth(String var1, int var2);

    public String trimStringToWidth(String var1, int var2, boolean var3);

    public String trimStringToWidth(String var1, int var2);

    public int getColorCode(char var1);

    public boolean isEnabled();

    public boolean setEnabled(boolean var1);

    public void setFontRandomSeed(long var1);

    public void drawSplitString(String var1, int var2, int var3, int var4, int var5);

    public int splitStringWidth(String var1, int var2);

    public boolean getUnicodeFlag();

    public void setUnicodeFlag(boolean var1);
}
