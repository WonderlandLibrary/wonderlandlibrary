/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.coretext;

import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.coretext.CTFontFile;
import com.sun.javafx.font.coretext.CTGlyphLayout;
import com.sun.javafx.text.GlyphLayout;

public class CTFactory
extends PrismFontFactory {
    public static PrismFontFactory getFactory() {
        return new CTFactory();
    }

    private CTFactory() {
    }

    @Override
    protected PrismFontFile createFontFile(String string, String string2, int n, boolean bl, boolean bl2, boolean bl3, boolean bl4) throws Exception {
        return new CTFontFile(string, string2, n, bl, bl2, bl3, bl4);
    }

    @Override
    public GlyphLayout createGlyphLayout() {
        return new CTGlyphLayout();
    }

    @Override
    protected boolean registerEmbeddedFont(String string) {
        boolean bl = CTFontFile.registerFont(string);
        if (debugFonts) {
            if (bl) {
                System.err.println("[CoreText] Font registration succeeded:" + string);
            } else {
                System.err.println("[CoreText] Font registration failed:" + string);
            }
        }
        return bl;
    }
}

