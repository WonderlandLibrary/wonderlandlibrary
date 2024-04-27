/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.minecraft.util;

public final class WEnumChatFormatting
extends Enum {
    public static final /* enum */ WEnumChatFormatting DARK_PURPLE;
    public static final /* enum */ WEnumChatFormatting DARK_AQUA;
    public static final /* enum */ WEnumChatFormatting RED;
    public static final /* enum */ WEnumChatFormatting GREEN;
    private final char formattingCodeIn;
    public static final /* enum */ WEnumChatFormatting ITALIC;
    public static final /* enum */ WEnumChatFormatting WHITE;
    public static final /* enum */ WEnumChatFormatting YELLOW;
    public static final /* enum */ WEnumChatFormatting RESET;
    public static final /* enum */ WEnumChatFormatting AQUA;
    private static final WEnumChatFormatting[] $VALUES;
    public static final /* enum */ WEnumChatFormatting GOLD;
    public static final /* enum */ WEnumChatFormatting STRIKETHROUGH;
    private final int colorIndex;
    public static final /* enum */ WEnumChatFormatting DARK_RED;
    public static final /* enum */ WEnumChatFormatting DARK_BLUE;
    public static final /* enum */ WEnumChatFormatting DARK_GREEN;
    public static final /* enum */ WEnumChatFormatting BOLD;
    public static final /* enum */ WEnumChatFormatting DARK_GRAY;
    public static final /* enum */ WEnumChatFormatting BLACK;
    public static final /* enum */ WEnumChatFormatting BLUE;
    private final boolean fancyStylingIn;
    private final String formattingName;
    public static final /* enum */ WEnumChatFormatting UNDERLINE;
    public static final /* enum */ WEnumChatFormatting GRAY;
    public static final /* enum */ WEnumChatFormatting LIGHT_PURPLE;
    public static final /* enum */ WEnumChatFormatting OBFUSCATED;

    public final boolean getFancyStylingIn() {
        return this.fancyStylingIn;
    }

    public final String getFormattingName() {
        return this.formattingName;
    }

    public static WEnumChatFormatting valueOf(String string) {
        return Enum.valueOf(WEnumChatFormatting.class, string);
    }

    static {
        WEnumChatFormatting[] wEnumChatFormattingArray = new WEnumChatFormatting[22];
        WEnumChatFormatting[] wEnumChatFormattingArray2 = wEnumChatFormattingArray;
        wEnumChatFormattingArray[0] = BLACK = new WEnumChatFormatting("BLACK", 0, "BLACK", '0', 0);
        wEnumChatFormattingArray[1] = DARK_BLUE = new WEnumChatFormatting("DARK_BLUE", 1, "DARK_BLUE", '1', 1);
        wEnumChatFormattingArray[2] = DARK_GREEN = new WEnumChatFormatting("DARK_GREEN", 2, "DARK_GREEN", '2', 2);
        wEnumChatFormattingArray[3] = DARK_AQUA = new WEnumChatFormatting("DARK_AQUA", 3, "DARK_AQUA", '3', 3);
        wEnumChatFormattingArray[4] = DARK_RED = new WEnumChatFormatting("DARK_RED", 4, "DARK_RED", '4', 4);
        wEnumChatFormattingArray[5] = DARK_PURPLE = new WEnumChatFormatting("DARK_PURPLE", 5, "DARK_PURPLE", '5', 5);
        wEnumChatFormattingArray[6] = GOLD = new WEnumChatFormatting("GOLD", 6, "GOLD", '6', 6);
        wEnumChatFormattingArray[7] = GRAY = new WEnumChatFormatting("GRAY", 7, "GRAY", '7', 7);
        wEnumChatFormattingArray[8] = DARK_GRAY = new WEnumChatFormatting("DARK_GRAY", 8, "DARK_GRAY", '8', 8);
        wEnumChatFormattingArray[9] = BLUE = new WEnumChatFormatting("BLUE", 9, "BLUE", '9', 9);
        wEnumChatFormattingArray[10] = GREEN = new WEnumChatFormatting("GREEN", 10, "GREEN", 'a', 10);
        wEnumChatFormattingArray[11] = AQUA = new WEnumChatFormatting("AQUA", 11, "AQUA", 'b', 11);
        wEnumChatFormattingArray[12] = RED = new WEnumChatFormatting("RED", 12, "RED", 'c', 12);
        wEnumChatFormattingArray[13] = LIGHT_PURPLE = new WEnumChatFormatting("LIGHT_PURPLE", 13, "LIGHT_PURPLE", 'd', 13);
        wEnumChatFormattingArray[14] = YELLOW = new WEnumChatFormatting("YELLOW", 14, "YELLOW", 'e', 14);
        wEnumChatFormattingArray[15] = WHITE = new WEnumChatFormatting("WHITE", 15, "WHITE", 'f', 15);
        wEnumChatFormattingArray[16] = OBFUSCATED = new WEnumChatFormatting("OBFUSCATED", 16, "OBFUSCATED", 'k', true);
        wEnumChatFormattingArray[17] = BOLD = new WEnumChatFormatting("BOLD", 17, "BOLD", 'l', true);
        wEnumChatFormattingArray[18] = STRIKETHROUGH = new WEnumChatFormatting("STRIKETHROUGH", 18, "STRIKETHROUGH", 'm', true);
        wEnumChatFormattingArray[19] = UNDERLINE = new WEnumChatFormatting("UNDERLINE", 19, "UNDERLINE", 'n', true);
        wEnumChatFormattingArray[20] = ITALIC = new WEnumChatFormatting("ITALIC", 20, "ITALIC", 'o', true);
        wEnumChatFormattingArray[21] = RESET = new WEnumChatFormatting("RESET", 21, "RESET", 'r', -1);
        $VALUES = wEnumChatFormattingArray;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private WEnumChatFormatting(int n) {
        this((String)var1_-1, (int)var2_-1, (String)n, (char)var4_2, false, (int)var5_3);
        void var5_3;
        void var4_2;
        void var2_-1;
        void var1_-1;
    }

    public static WEnumChatFormatting[] values() {
        return (WEnumChatFormatting[])$VALUES.clone();
    }

    public String toString() {
        return "" + '\u00a7' + this.formattingCodeIn;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private WEnumChatFormatting(boolean bl) {
        this((String)var1_-1, (int)var2_-1, (String)bl, (char)var4_2, (boolean)var5_3, -1);
        void var5_3;
        void var4_2;
        void var2_-1;
        void var1_-1;
    }

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private WEnumChatFormatting(boolean bl, int n) {
        void var6_4;
        void var5_3;
        void var2_-1;
        void var1_-1;
        this.formattingName = (String)bl;
        this.formattingCodeIn = (char)n;
        this.fancyStylingIn = var5_3;
        this.colorIndex = var6_4;
    }

    public final char getFormattingCodeIn() {
        return this.formattingCodeIn;
    }

    public final int getColorIndex() {
        return this.colorIndex;
    }
}

