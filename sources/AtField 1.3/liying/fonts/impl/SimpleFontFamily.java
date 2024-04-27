/*
 * Decompiled with CFR 0.152.
 */
package liying.fonts.impl;

import java.awt.Font;
import liying.fonts.api.FontFamily;
import liying.fonts.api.FontRenderer;
import liying.fonts.api.FontType;
import liying.fonts.impl.SimpleFontRenderer;

final class SimpleFontFamily
implements FontFamily {
    private final FontType fontType;
    private final Font awtFont;

    @Override
    public FontRenderer ofSize(int n) {
        return SimpleFontRenderer.create(this.awtFont.deriveFont(0, n));
    }

    private SimpleFontFamily(FontType fontType, Font font) {
        this.fontType = fontType;
        this.awtFont = font;
    }

    static FontFamily create(FontType fontType, Font font) {
        return new SimpleFontFamily(fontType, font);
    }

    @Override
    public FontType font() {
        return this.fontType;
    }
}

