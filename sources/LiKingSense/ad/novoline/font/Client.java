/*
 * Decompiled with CFR 0.152.
 */
package ad.novoline.font;

import ad.novoline.font.FontManager;
import ad.novoline.font.SimpleFontManager;

public class Client {
    public static double fontScaleOffset = 1.0;
    public static FontManager fontManager = SimpleFontManager.create();

    public static FontManager getFontManager() {
        return fontManager;
    }
}

