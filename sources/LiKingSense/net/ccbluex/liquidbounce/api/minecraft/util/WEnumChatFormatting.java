/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\f\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\"\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u001f\b\u0012\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bB\u001f\b\u0012\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bB'\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\fJ\b\u0010\u0015\u001a\u00020\u0003H\u0016R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014j\u0002\b\u0016j\u0002\b\u0017j\u0002\b\u0018j\u0002\b\u0019j\u0002\b\u001aj\u0002\b\u001bj\u0002\b\u001cj\u0002\b\u001dj\u0002\b\u001ej\u0002\b\u001fj\u0002\b j\u0002\b!j\u0002\b\"j\u0002\b#j\u0002\b$j\u0002\b%j\u0002\b&j\u0002\b'j\u0002\b(j\u0002\b)j\u0002\b*j\u0002\b+\u00a8\u0006,"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/WEnumChatFormatting;", "", "formattingName", "", "formattingCodeIn", "", "colorIndex", "", "(Ljava/lang/String;ILjava/lang/String;CI)V", "fancyStylingIn", "", "(Ljava/lang/String;ILjava/lang/String;CZ)V", "(Ljava/lang/String;ILjava/lang/String;CZI)V", "getColorIndex", "()I", "getFancyStylingIn", "()Z", "getFormattingCodeIn", "()C", "getFormattingName", "()Ljava/lang/String;", "toString", "BLACK", "DARK_BLUE", "DARK_GREEN", "DARK_AQUA", "DARK_RED", "DARK_PURPLE", "GOLD", "GRAY", "DARK_GRAY", "BLUE", "GREEN", "AQUA", "RED", "LIGHT_PURPLE", "YELLOW", "WHITE", "OBFUSCATED", "BOLD", "STRIKETHROUGH", "UNDERLINE", "ITALIC", "RESET", "LiKingSense"})
public final class WEnumChatFormatting
extends Enum<WEnumChatFormatting> {
    public static final /* enum */ WEnumChatFormatting BLACK;
    public static final /* enum */ WEnumChatFormatting DARK_BLUE;
    public static final /* enum */ WEnumChatFormatting DARK_GREEN;
    public static final /* enum */ WEnumChatFormatting DARK_AQUA;
    public static final /* enum */ WEnumChatFormatting DARK_RED;
    public static final /* enum */ WEnumChatFormatting DARK_PURPLE;
    public static final /* enum */ WEnumChatFormatting GOLD;
    public static final /* enum */ WEnumChatFormatting GRAY;
    public static final /* enum */ WEnumChatFormatting DARK_GRAY;
    public static final /* enum */ WEnumChatFormatting BLUE;
    public static final /* enum */ WEnumChatFormatting GREEN;
    public static final /* enum */ WEnumChatFormatting AQUA;
    public static final /* enum */ WEnumChatFormatting RED;
    public static final /* enum */ WEnumChatFormatting LIGHT_PURPLE;
    public static final /* enum */ WEnumChatFormatting YELLOW;
    public static final /* enum */ WEnumChatFormatting WHITE;
    public static final /* enum */ WEnumChatFormatting OBFUSCATED;
    public static final /* enum */ WEnumChatFormatting BOLD;
    public static final /* enum */ WEnumChatFormatting STRIKETHROUGH;
    public static final /* enum */ WEnumChatFormatting UNDERLINE;
    public static final /* enum */ WEnumChatFormatting ITALIC;
    public static final /* enum */ WEnumChatFormatting RESET;
    private static final /* synthetic */ WEnumChatFormatting[] $VALUES;
    @NotNull
    private final String formattingName;
    private final char formattingCodeIn;
    private final boolean fancyStylingIn;
    private final int colorIndex;

    static {
        WEnumChatFormatting wEnumChatFormatting;
        WEnumChatFormatting wEnumChatFormatting2;
        WEnumChatFormatting wEnumChatFormatting3;
        WEnumChatFormatting wEnumChatFormatting4;
        WEnumChatFormatting wEnumChatFormatting5;
        WEnumChatFormatting wEnumChatFormatting6;
        WEnumChatFormatting wEnumChatFormatting7;
        WEnumChatFormatting wEnumChatFormatting8;
        WEnumChatFormatting wEnumChatFormatting9;
        WEnumChatFormatting wEnumChatFormatting10;
        WEnumChatFormatting wEnumChatFormatting11;
        WEnumChatFormatting wEnumChatFormatting12;
        WEnumChatFormatting wEnumChatFormatting13;
        WEnumChatFormatting wEnumChatFormatting14;
        WEnumChatFormatting wEnumChatFormatting15;
        WEnumChatFormatting wEnumChatFormatting16;
        WEnumChatFormatting wEnumChatFormatting17;
        WEnumChatFormatting wEnumChatFormatting18;
        WEnumChatFormatting wEnumChatFormatting19;
        WEnumChatFormatting wEnumChatFormatting20;
        WEnumChatFormatting wEnumChatFormatting21;
        WEnumChatFormatting wEnumChatFormatting22;
        WEnumChatFormatting[] wEnumChatFormattingArray = new WEnumChatFormatting[22];
        WEnumChatFormatting[] wEnumChatFormattingArray2 = wEnumChatFormattingArray;
        WEnumChatFormatting[] wEnumChatFormattingArray3 = wEnumChatFormattingArray;
        WEnumChatFormatting[] wEnumChatFormattingArray4 = wEnumChatFormattingArray;
        boolean bl = false;
        WEnumChatFormatting wEnumChatFormatting23 = wEnumChatFormatting22;
        WEnumChatFormatting wEnumChatFormatting24 = wEnumChatFormatting22;
        BLACK = (WEnumChatFormatting)0;
        false["BLACK"] = 0;
        String string = "BLACK";
        String string2 = "BLACK";
        boolean bl2 = true;
        WEnumChatFormatting wEnumChatFormatting25 = wEnumChatFormatting21;
        WEnumChatFormatting wEnumChatFormatting26 = wEnumChatFormatting21;
        DARK_BLUE = (WEnumChatFormatting)0;
        true["DARK_BLUE"] = 0;
        String string3 = "DARK_BLUE";
        String string4 = "DARK_BLUE";
        int n = 2;
        WEnumChatFormatting wEnumChatFormatting27 = wEnumChatFormatting20;
        WEnumChatFormatting wEnumChatFormatting28 = wEnumChatFormatting20;
        DARK_GREEN = (WEnumChatFormatting)0;
        2["DARK_GREEN"] = 0;
        String string5 = "DARK_GREEN";
        String string6 = "DARK_GREEN";
        int n2 = 3;
        WEnumChatFormatting wEnumChatFormatting29 = wEnumChatFormatting19;
        WEnumChatFormatting wEnumChatFormatting30 = wEnumChatFormatting19;
        DARK_AQUA = (WEnumChatFormatting)0;
        3["DARK_AQUA"] = 0;
        String string7 = "DARK_AQUA";
        String string8 = "DARK_AQUA";
        int n3 = 4;
        WEnumChatFormatting wEnumChatFormatting31 = wEnumChatFormatting18;
        WEnumChatFormatting wEnumChatFormatting32 = wEnumChatFormatting18;
        DARK_RED = (WEnumChatFormatting)0;
        4["DARK_RED"] = 0;
        String string9 = "DARK_RED";
        String string10 = "DARK_RED";
        int n4 = 5;
        WEnumChatFormatting wEnumChatFormatting33 = wEnumChatFormatting17;
        WEnumChatFormatting wEnumChatFormatting34 = wEnumChatFormatting17;
        DARK_PURPLE = (WEnumChatFormatting)0;
        5["DARK_PURPLE"] = 0;
        String string11 = "DARK_PURPLE";
        String string12 = "DARK_PURPLE";
        int n5 = 6;
        WEnumChatFormatting wEnumChatFormatting35 = wEnumChatFormatting16;
        WEnumChatFormatting wEnumChatFormatting36 = wEnumChatFormatting16;
        GOLD = (WEnumChatFormatting)0;
        6["GOLD"] = 0;
        String string13 = "GOLD";
        String string14 = "GOLD";
        int n6 = 7;
        WEnumChatFormatting wEnumChatFormatting37 = wEnumChatFormatting15;
        WEnumChatFormatting wEnumChatFormatting38 = wEnumChatFormatting15;
        GRAY = (WEnumChatFormatting)0;
        7["GRAY"] = 0;
        String string15 = "GRAY";
        String string16 = "GRAY";
        int n7 = 8;
        WEnumChatFormatting wEnumChatFormatting39 = wEnumChatFormatting14;
        WEnumChatFormatting wEnumChatFormatting40 = wEnumChatFormatting14;
        DARK_GRAY = (WEnumChatFormatting)0;
        8["DARK_GRAY"] = 0;
        String string17 = "DARK_GRAY";
        String string18 = "DARK_GRAY";
        int n8 = 9;
        WEnumChatFormatting wEnumChatFormatting41 = wEnumChatFormatting13;
        WEnumChatFormatting wEnumChatFormatting42 = wEnumChatFormatting13;
        BLUE = (WEnumChatFormatting)0;
        9["BLUE"] = 0;
        String string19 = "BLUE";
        String string20 = "BLUE";
        int n9 = 10;
        WEnumChatFormatting wEnumChatFormatting43 = wEnumChatFormatting12;
        WEnumChatFormatting wEnumChatFormatting44 = wEnumChatFormatting12;
        GREEN = (WEnumChatFormatting)0;
        10["GREEN"] = 0;
        String string21 = "GREEN";
        String string22 = "GREEN";
        int n10 = 11;
        WEnumChatFormatting wEnumChatFormatting45 = wEnumChatFormatting11;
        WEnumChatFormatting wEnumChatFormatting46 = wEnumChatFormatting11;
        AQUA = (WEnumChatFormatting)0;
        11["AQUA"] = 0;
        String string23 = "AQUA";
        String string24 = "AQUA";
        int n11 = 12;
        WEnumChatFormatting wEnumChatFormatting47 = wEnumChatFormatting10;
        WEnumChatFormatting wEnumChatFormatting48 = wEnumChatFormatting10;
        RED = (WEnumChatFormatting)0;
        12["RED"] = 0;
        String string25 = "RED";
        String string26 = "RED";
        int n12 = 13;
        WEnumChatFormatting wEnumChatFormatting49 = wEnumChatFormatting9;
        WEnumChatFormatting wEnumChatFormatting50 = wEnumChatFormatting9;
        LIGHT_PURPLE = (WEnumChatFormatting)0;
        13["LIGHT_PURPLE"] = 0;
        String string27 = "LIGHT_PURPLE";
        String string28 = "LIGHT_PURPLE";
        int n13 = 14;
        WEnumChatFormatting wEnumChatFormatting51 = wEnumChatFormatting8;
        WEnumChatFormatting wEnumChatFormatting52 = wEnumChatFormatting8;
        YELLOW = (WEnumChatFormatting)0;
        14["YELLOW"] = 0;
        String string29 = "YELLOW";
        String string30 = "YELLOW";
        int n14 = 15;
        WEnumChatFormatting wEnumChatFormatting53 = wEnumChatFormatting7;
        WEnumChatFormatting wEnumChatFormatting54 = wEnumChatFormatting7;
        WHITE = (WEnumChatFormatting)0;
        15["WHITE"] = 0;
        String string31 = "WHITE";
        String string32 = "WHITE";
        int n15 = 16;
        WEnumChatFormatting wEnumChatFormatting55 = wEnumChatFormatting6;
        WEnumChatFormatting wEnumChatFormatting56 = wEnumChatFormatting6;
        OBFUSCATED = (WEnumChatFormatting)0;
        16["OBFUSCATED"] = 0;
        String string33 = "OBFUSCATED";
        String string34 = "OBFUSCATED";
        int n16 = 17;
        WEnumChatFormatting wEnumChatFormatting57 = wEnumChatFormatting5;
        WEnumChatFormatting wEnumChatFormatting58 = wEnumChatFormatting5;
        BOLD = (WEnumChatFormatting)0;
        17["BOLD"] = 0;
        String string35 = "BOLD";
        String string36 = "BOLD";
        int n17 = 18;
        WEnumChatFormatting wEnumChatFormatting59 = wEnumChatFormatting4;
        WEnumChatFormatting wEnumChatFormatting60 = wEnumChatFormatting4;
        STRIKETHROUGH = (WEnumChatFormatting)0;
        18["STRIKETHROUGH"] = 0;
        String string37 = "STRIKETHROUGH";
        String string38 = "STRIKETHROUGH";
        int n18 = 19;
        WEnumChatFormatting wEnumChatFormatting61 = wEnumChatFormatting3;
        WEnumChatFormatting wEnumChatFormatting62 = wEnumChatFormatting3;
        UNDERLINE = (WEnumChatFormatting)0;
        19["UNDERLINE"] = 0;
        String string39 = "UNDERLINE";
        String string40 = "UNDERLINE";
        int n19 = 20;
        WEnumChatFormatting wEnumChatFormatting63 = wEnumChatFormatting2;
        WEnumChatFormatting wEnumChatFormatting64 = wEnumChatFormatting2;
        ITALIC = (WEnumChatFormatting)0;
        20["ITALIC"] = 0;
        String string41 = "ITALIC";
        String string42 = "ITALIC";
        int n20 = 21;
        WEnumChatFormatting wEnumChatFormatting65 = wEnumChatFormatting;
        WEnumChatFormatting wEnumChatFormatting66 = wEnumChatFormatting;
        RESET = (WEnumChatFormatting)0;
        21["RESET"] = 0;
        $VALUES = "RESET";
    }

    @NotNull
    public String toString() {
        return "" + '\u00a7' + this.formattingCodeIn;
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

