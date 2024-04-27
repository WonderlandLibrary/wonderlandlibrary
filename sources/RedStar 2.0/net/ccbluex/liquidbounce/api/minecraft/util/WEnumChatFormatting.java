package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\n\u0000\n\n\u0000\n\f\n\u0000\n\b\n\b\n\n\b\"\b\u00002\b0\u00000B\b000¢\bB\b00\t0\n¢B'\b00\t0\n0¢\fJ\b0HR0¢\b\n\u0000\b\rR\t0\n¢\b\n\u0000\bR0¢\b\n\u0000\bR0¢\b\n\u0000\bj\bj\bj\bj\bj\bj\bj\bj\bj\bj\bj\b j\b!j\b\"j\b#j\b$j\b%j\b&j\b'j\b(j\b)j\b*j\b+¨,"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/WEnumChatFormatting;", "", "formattingName", "", "formattingCodeIn", "", "colorIndex", "", "(Ljava/lang/String;ILjava/lang/String;CI)V", "fancyStylingIn", "", "(Ljava/lang/String;ILjava/lang/String;CZ)V", "(Ljava/lang/String;ILjava/lang/String;CZI)V", "getColorIndex", "()I", "getFancyStylingIn", "()Z", "getFormattingCodeIn", "()C", "getFormattingName", "()Ljava/lang/String;", "toString", "BLACK", "DARK_BLUE", "DARK_GREEN", "DARK_AQUA", "DARK_RED", "DARK_PURPLE", "GOLD", "GRAY", "DARK_GRAY", "BLUE", "GREEN", "AQUA", "RED", "LIGHT_PURPLE", "YELLOW", "WHITE", "OBFUSCATED", "BOLD", "STRIKETHROUGH", "UNDERLINE", "ITALIC", "RESET", "Pride"})
public final class WEnumChatFormatting
extends Enum<WEnumChatFormatting> {
    public static final WEnumChatFormatting BLACK;
    public static final WEnumChatFormatting DARK_BLUE;
    public static final WEnumChatFormatting DARK_GREEN;
    public static final WEnumChatFormatting DARK_AQUA;
    public static final WEnumChatFormatting DARK_RED;
    public static final WEnumChatFormatting DARK_PURPLE;
    public static final WEnumChatFormatting GOLD;
    public static final WEnumChatFormatting GRAY;
    public static final WEnumChatFormatting DARK_GRAY;
    public static final WEnumChatFormatting BLUE;
    public static final WEnumChatFormatting GREEN;
    public static final WEnumChatFormatting AQUA;
    public static final WEnumChatFormatting RED;
    public static final WEnumChatFormatting LIGHT_PURPLE;
    public static final WEnumChatFormatting YELLOW;
    public static final WEnumChatFormatting WHITE;
    public static final WEnumChatFormatting OBFUSCATED;
    public static final WEnumChatFormatting BOLD;
    public static final WEnumChatFormatting STRIKETHROUGH;
    public static final WEnumChatFormatting UNDERLINE;
    public static final WEnumChatFormatting ITALIC;
    public static final WEnumChatFormatting RESET;
    private static final WEnumChatFormatting[] $VALUES;
    @NotNull
    private final String formattingName;
    private final char formattingCodeIn;
    private final boolean fancyStylingIn;
    private final int colorIndex;

    static {
        WEnumChatFormatting[] wEnumChatFormattingArray = new WEnumChatFormatting[22];
        WEnumChatFormatting[] wEnumChatFormattingArray2 = wEnumChatFormattingArray;
        wEnumChatFormattingArray[0] = BLACK = new WEnumChatFormatting("BLACK", '0', 0);
        wEnumChatFormattingArray[1] = DARK_BLUE = new WEnumChatFormatting("DARK_BLUE", '1', 1);
        wEnumChatFormattingArray[2] = DARK_GREEN = new WEnumChatFormatting("DARK_GREEN", '2', 2);
        wEnumChatFormattingArray[3] = DARK_AQUA = new WEnumChatFormatting("DARK_AQUA", '3', 3);
        wEnumChatFormattingArray[4] = DARK_RED = new WEnumChatFormatting("DARK_RED", '4', 4);
        wEnumChatFormattingArray[5] = DARK_PURPLE = new WEnumChatFormatting("DARK_PURPLE", '5', 5);
        wEnumChatFormattingArray[6] = GOLD = new WEnumChatFormatting("GOLD", '6', 6);
        wEnumChatFormattingArray[7] = GRAY = new WEnumChatFormatting("GRAY", '7', 7);
        wEnumChatFormattingArray[8] = DARK_GRAY = new WEnumChatFormatting("DARK_GRAY", '8', 8);
        wEnumChatFormattingArray[9] = BLUE = new WEnumChatFormatting("BLUE", '9', 9);
        wEnumChatFormattingArray[10] = GREEN = new WEnumChatFormatting("GREEN", 'a', 10);
        wEnumChatFormattingArray[11] = AQUA = new WEnumChatFormatting("AQUA", 'b', 11);
        wEnumChatFormattingArray[12] = RED = new WEnumChatFormatting("RED", 'c', 12);
        wEnumChatFormattingArray[13] = LIGHT_PURPLE = new WEnumChatFormatting("LIGHT_PURPLE", 'd', 13);
        wEnumChatFormattingArray[14] = YELLOW = new WEnumChatFormatting("YELLOW", 'e', 14);
        wEnumChatFormattingArray[15] = WHITE = new WEnumChatFormatting("WHITE", 'f', 15);
        wEnumChatFormattingArray[16] = OBFUSCATED = new WEnumChatFormatting("OBFUSCATED", 'k', true);
        wEnumChatFormattingArray[17] = BOLD = new WEnumChatFormatting("BOLD", 'l', true);
        wEnumChatFormattingArray[18] = STRIKETHROUGH = new WEnumChatFormatting("STRIKETHROUGH", 'm', true);
        wEnumChatFormattingArray[19] = UNDERLINE = new WEnumChatFormatting("UNDERLINE", 'n', true);
        wEnumChatFormattingArray[20] = ITALIC = new WEnumChatFormatting("ITALIC", 'o', true);
        wEnumChatFormattingArray[21] = RESET = new WEnumChatFormatting("RESET", 'r', -1);
        $VALUES = wEnumChatFormattingArray;
    }

    @NotNull
    public String toString() {
        return "" + '§' + this.formattingCodeIn;
    }

    @NotNull
    public final String getFormattingName() {
        return this.formattingName;
    }

    public final char getFormattingCodeIn() {
        return this.formattingCodeIn;
    }

    public final boolean getFancyStylingIn() {
        return this.fancyStylingIn;
    }

    public final int getColorIndex() {
        return this.colorIndex;
    }

    private WEnumChatFormatting(String formattingName, char formattingCodeIn, boolean fancyStylingIn, int colorIndex) {
        this.formattingName = formattingName;
        this.formattingCodeIn = formattingCodeIn;
        this.fancyStylingIn = fancyStylingIn;
        this.colorIndex = colorIndex;
    }

    private WEnumChatFormatting(String formattingName, char formattingCodeIn, int colorIndex) {
        this(formattingName, formattingCodeIn, false, colorIndex);
    }

    private WEnumChatFormatting(String formattingName, char formattingCodeIn, boolean fancyStylingIn) {
        this(formattingName, formattingCodeIn, fancyStylingIn, -1);
    }

    public static WEnumChatFormatting[] values() {
        return (WEnumChatFormatting[])$VALUES.clone();
    }

    public static WEnumChatFormatting valueOf(String string) {
        return Enum.valueOf(WEnumChatFormatting.class, string);
    }
}
