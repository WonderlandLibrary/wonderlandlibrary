/*
 * Decompiled with CFR 0.152.
 */
package ad.novoline.font;

import ad.novoline.font.FontFamily;
import ad.novoline.font.FontRenderer;
import ad.novoline.font.FontType;
import ad.novoline.font.SimpleFontRenderer;
import java.awt.Font;

final class SimpleFontFamily
implements FontFamily {
    private final FontType fontType;
    private final Font awtFont;

    private SimpleFontFamily(FontType fontType, Font awtFont) {
        this.fontType = fontType;
        this.awtFont = awtFont;
    }

    static FontFamily create(FontType fontType, Font awtFont) {
        return new SimpleFontFamily(fontType, awtFont);
    }

    @Override
    public FontRenderer ofSize(int size) {
        return SimpleFontRenderer.create(this.awtFont.deriveFont(0, size));
    }

    @Override
    public FontType font() {
        return this.fontType;
    }
}

