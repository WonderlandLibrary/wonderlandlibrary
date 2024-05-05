/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.cn.Insane.Module.fonts.api;

public enum FontType {
    DM("diramight.ttf"),
    FIXEDSYS("tahoma.ttf"),
    ICONFONT("stylesicons.ttf"),
    FluxICONFONT("flux.ttf"),
    Check("check.ttf"),
    TenacityBold("Tenacity.ttf"),
    SF("SF.ttf"),
    GENSHIN("GENSHIN.ttf"),
    MAINMENU("mainmenu.ttf"),
    SFBOLD("SFBOLD.ttf"),
    CHINESE("black.ttf"),
    Tahoma("Tahoma.ttf"),
    TahomaBold("Tahoma-Bold.ttf"),
    SFTHIN("SFREGULAR.ttf"),
    OXIDE("oxide.ttf");

    private final String fileName;

    private FontType(String fileName) {
        this.fileName = fileName;
    }

    public String fileName() {
        return this.fileName;
    }
}

