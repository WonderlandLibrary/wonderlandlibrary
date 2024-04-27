/*
 * Decompiled with CFR 0.152.
 */
package ad.novoline.font;

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
    posterama("posterama.ttf"),
    csgoicon("icomoon.ttf"),
    Tahoma("tahoma.ttf"),
    NeverLoserf("neverlose500.ttf"),
    Novoicon("iconnovo.ttf"),
    Neverlose_icon("neverlose_icon.ttf"),
    Debug_Icon("Icon.ttf"),
    Notification("notification-icon.ttf"),
    Novo2("novogui.ttf"),
    tenacity("tenacity.ttf"),
    tenacityBlod("tenacityblod.ttf"),
    tenacityCheck("check.ttf");

    private final String fileName;

    private FontType(String fileName) {
        this.fileName = fileName;
    }

    public String fileName() {
        return this.fileName;
    }
}

