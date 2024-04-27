/*
 * Decompiled with CFR 0.152.
 */
package jx.utils.novoline.api;

public enum FontType {
    Jello_Light("jellolight.ttf"),
    Jello_Medium("jellomedium.ttf"),
    Jello_Regular("jelloregular.ttf"),
    SF("sf.ttf"),
    SFBOLD("sfbold.ttf"),
    SFTHIN("SFREGULAR.ttf"),
    Check("check.ttf"),
    ICONFONT("stylesicons.ttf"),
    flux("flux.ttf"),
    csgoicon("icomoon.ttf"),
    Tahoma("tahoma.ttf"),
    NeverLoserf("neverlose500.ttf"),
    Neverlose_icon("neverlose_icon.ttf"),
    Debug_Icon("Icon.ttf");

    private final String fileName;

    private FontType(String fileName) {
        this.fileName = fileName;
    }

    public String fileName() {
        return this.fileName;
    }
}

