/*
 * Decompiled with CFR 0.152.
 */
package liying.fonts.api;

import liying.fonts.api.FontFamily;
import liying.fonts.api.FontRenderer;
import liying.fonts.api.FontType;

@FunctionalInterface
public interface FontManager {
    public FontFamily fontFamily(FontType var1);

    default public FontRenderer font(FontType fontType, int n) {
        return this.fontFamily(fontType).ofSize(n);
    }
}

