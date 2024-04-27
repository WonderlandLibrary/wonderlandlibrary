/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.api.enums;

public final class WDefaultVertexFormats
extends Enum {
    public static final /* enum */ WDefaultVertexFormats POSITION_COLOR;
    public static final /* enum */ WDefaultVertexFormats POSITION_TEX;
    public static final /* enum */ WDefaultVertexFormats POSITION;
    private static final WDefaultVertexFormats[] $VALUES;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private WDefaultVertexFormats() {
        void var2_-1;
        void var1_-1;
    }

    public static WDefaultVertexFormats valueOf(String string) {
        return Enum.valueOf(WDefaultVertexFormats.class, string);
    }

    static {
        WDefaultVertexFormats[] wDefaultVertexFormatsArray = new WDefaultVertexFormats[3];
        WDefaultVertexFormats[] wDefaultVertexFormatsArray2 = wDefaultVertexFormatsArray;
        wDefaultVertexFormatsArray[0] = POSITION = new WDefaultVertexFormats("POSITION", 0);
        wDefaultVertexFormatsArray[1] = POSITION_TEX = new WDefaultVertexFormats("POSITION_TEX", 1);
        wDefaultVertexFormatsArray[2] = POSITION_COLOR = new WDefaultVertexFormats("POSITION_COLOR", 2);
        $VALUES = wDefaultVertexFormatsArray;
    }

    public static WDefaultVertexFormats[] values() {
        return (WDefaultVertexFormats[])$VALUES.clone();
    }
}

