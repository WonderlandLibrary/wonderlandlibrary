/*
 * Decompiled with CFR 0.152.
 */
package jx.utils.novoline.api;

import jx.utils.novoline.api.FontFamily;
import jx.utils.novoline.api.FontRenderer;
import jx.utils.novoline.api.FontType;

@FunctionalInterface
public interface FontManager {
    public FontFamily fontFamily(FontType var1);

    default public FontRenderer font(FontType fontType, int size) {
        return this.fontFamily(fontType).ofSize(size);
    }
}

