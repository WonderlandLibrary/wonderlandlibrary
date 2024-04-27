package net.ccbluex.liquidbounce.api.enums;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\f\n\n\n\b\b\u00002\b0\u00000B\b¢j\bj\bj\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/enums/WDefaultVertexFormats;", "", "(Ljava/lang/String;I)V", "POSITION", "POSITION_TEX", "POSITION_COLOR", "Pride"})
public final class WDefaultVertexFormats
extends Enum<WDefaultVertexFormats> {
    public static final WDefaultVertexFormats POSITION;
    public static final WDefaultVertexFormats POSITION_TEX;
    public static final WDefaultVertexFormats POSITION_COLOR;
    private static final WDefaultVertexFormats[] $VALUES;

    static {
        WDefaultVertexFormats[] wDefaultVertexFormatsArray = new WDefaultVertexFormats[3];
        WDefaultVertexFormats[] wDefaultVertexFormatsArray2 = wDefaultVertexFormatsArray;
        wDefaultVertexFormatsArray[0] = POSITION = new WDefaultVertexFormats();
        wDefaultVertexFormatsArray[1] = POSITION_TEX = new WDefaultVertexFormats();
        wDefaultVertexFormatsArray[2] = POSITION_COLOR = new WDefaultVertexFormats();
        $VALUES = wDefaultVertexFormatsArray;
    }

    public static WDefaultVertexFormats[] values() {
        return (WDefaultVertexFormats[])$VALUES.clone();
    }

    public static WDefaultVertexFormats valueOf(String string) {
        return Enum.valueOf(WDefaultVertexFormats.class, string);
    }
}
