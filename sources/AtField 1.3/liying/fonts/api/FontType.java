/*
 * Decompiled with CFR 0.152.
 */
package liying.fonts.api;

public enum FontType {
    FIXEDSYS("tahoma.ttf"),
    ICONFONT("stylesicons.ttf"),
    Check("check.ttf"),
    SF("SF.ttf"),
    SFBOLD("SFBOLD.ttf"),
    JelloMedium("jellomedium.ttf"),
    Tahoma("tahoma.ttf"),
    JelloLight("jellolight.ttf"),
    csgoicon("icomoon.ttf");

    private final String fileName;

    /*
     * WARNING - Possible parameter corruption
     * WARNING - void declaration
     */
    private FontType() {
        void var3_1;
        this.fileName = var3_1;
    }

    public String fileName() {
        return this.fileName;
    }
}

