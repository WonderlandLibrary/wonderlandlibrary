/*
 * Decompiled with CFR 0.152.
 */
package ad.novoline.font;

import ad.novoline.font.FontFamily;
import ad.novoline.font.FontRenderer;
import ad.novoline.font.FontType;

@FunctionalInterface
public interface FontManager {
    public FontFamily fontFamily(FontType var1);

    default public FontRenderer font(FontType fontType, int size) {
        return this.fontFamily(fontType).ofSize(size);
    }
}

