// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.font;

import java.awt.Font;

public class FontUtil
{
    public static Font getFontFromTTF(final nf loc, final float fontSize, final int fontType) {
        try {
            Font output = Font.createFont(fontType, bib.z().O().a(loc).b());
            output = output.deriveFont(fontSize);
            return output;
        }
        catch (Exception e) {
            return null;
        }
    }
}
