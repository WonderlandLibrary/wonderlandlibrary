/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.enums;

public final class WDefaultVertexFormats
extends Enum<WDefaultVertexFormats> {
    public static final /* enum */ WDefaultVertexFormats POSITION;
    public static final /* enum */ WDefaultVertexFormats POSITION_TEX;
    public static final /* enum */ WDefaultVertexFormats POSITION_COLOR;
    private static final /* synthetic */ WDefaultVertexFormats[] $VALUES;

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

