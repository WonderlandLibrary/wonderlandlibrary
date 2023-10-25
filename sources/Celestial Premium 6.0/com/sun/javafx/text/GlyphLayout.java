/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.text;

import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.PrismTextLayout;
import com.sun.javafx.text.ScriptMapper;
import com.sun.javafx.text.TextRun;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Bidi;

public abstract class GlyphLayout {
    public static final int CANONICAL_SUBSTITUTION = 0x40000000;
    public static final int LAYOUT_LEFT_TO_RIGHT = 1;
    public static final int LAYOUT_RIGHT_TO_LEFT = 2;
    public static final int LAYOUT_NO_START_CONTEXT = 4;
    public static final int LAYOUT_NO_LIMIT_CONTEXT = 8;
    public static final int HINTING = 16;
    private static Method isIdeographicMethod = null;
    private static GlyphLayout reusableGL;
    private static boolean inUse;

    protected TextRun addTextRun(PrismTextLayout prismTextLayout, char[] arrc, int n, int n2, PGFont pGFont, TextSpan textSpan, byte by) {
        TextRun textRun = new TextRun(n, n2, by, true, 0, textSpan, 0, false);
        prismTextLayout.addTextRun(textRun);
        return textRun;
    }

    private TextRun addTextRun(PrismTextLayout prismTextLayout, char[] arrc, int n, int n2, PGFont pGFont, TextSpan textSpan, byte by, boolean bl) {
        if (bl || (by & 1) != 0) {
            return this.addTextRun(prismTextLayout, arrc, n, n2, pGFont, textSpan, by);
        }
        TextRun textRun = new TextRun(n, n2, by, false, 0, textSpan, 0, false);
        prismTextLayout.addTextRun(textRun);
        return textRun;
    }

    public int breakRuns(PrismTextLayout prismTextLayout, char[] arrc, int n) {
        int n2;
        int n3;
        int n4 = arrc.length;
        boolean bl = false;
        boolean bl2 = false;
        int n5 = 0;
        int n6 = 0;
        boolean bl3 = true;
        boolean bl4 = true;
        if ((n & 2) != 0) {
            bl3 = (n & 0x10) != 0;
            bl4 = (n & 8) != 0;
        }
        TextRun textRun = null;
        Bidi bidi = null;
        byte by = 0;
        int n7 = n4;
        int n8 = 0;
        int n9 = 0;
        TextSpan textSpan = null;
        int n10 = n4;
        PGFont pGFont = null;
        TextSpan[] arrtextSpan = prismTextLayout.getTextSpans();
        if (arrtextSpan != null) {
            if (arrtextSpan.length > 0) {
                textSpan = arrtextSpan[n9];
                n10 = textSpan.getText().length();
                pGFont = (PGFont)textSpan.getFont();
                if (pGFont == null) {
                    n |= 0x20;
                }
            }
        } else {
            pGFont = prismTextLayout.getFont();
        }
        if (pGFont != null) {
            FontResource fontResource = pGFont.getFontResource();
            n3 = pGFont.getFeatures();
            boolean bl5 = bl2 = (n3 & (n2 = fontResource.getFeatures())) != 0;
        }
        if (bl4 && n4 > 0) {
            int n11 = prismTextLayout.getDirection();
            bidi = new Bidi(arrc, 0, null, 0, n4, n11);
            by = (byte)bidi.getLevelAt(bidi.getRunStart(n8));
            n7 = bidi.getRunLimit(n8);
            if ((by & 1) != 0) {
                n |= 0x18;
            }
        }
        int n12 = 0;
        n3 = 0;
        while (n3 < n4) {
            boolean bl6;
            int n13 = n2 = arrc[n3];
            boolean bl7 = bl6 = n2 == 9 || n2 == 10 || n2 == 13;
            if (bl6 && n3 != n12) {
                textRun = this.addTextRun(prismTextLayout, arrc, n12, n3 - n12, pGFont, textSpan, by, bl);
                if (bl) {
                    n |= 0x10;
                    bl = false;
                }
                n12 = n3;
            }
            boolean bl8 = n3 >= n10 && n3 < n4;
            boolean bl9 = n3 >= n7 && n3 < n4;
            boolean bl10 = false;
            if (!bl6) {
                boolean bl11 = bl;
                if (bl3) {
                    if (Character.isHighSurrogate((char)n2) && n3 + 1 < n10 && Character.isLowSurrogate(arrc[n3 + 1])) {
                        n13 = Character.toCodePoint((char)n2, arrc[++n3]);
                    }
                    if (GlyphLayout.isIdeographic(n13)) {
                        n |= 0x40;
                    }
                    n6 = ScriptMapper.getScript(n13);
                    if (n5 > 1 && n6 > 1 && n6 != n5) {
                        bl10 = true;
                    }
                    if (!bl) {
                        boolean bl12 = bl = bl2 || ScriptMapper.isComplexCharCode(n13);
                    }
                }
                if ((bl8 || bl9 || bl10) && n12 != n3) {
                    textRun = this.addTextRun(prismTextLayout, arrc, n12, n3 - n12, pGFont, textSpan, by, bl11);
                    if (bl) {
                        n |= 0x10;
                        bl = false;
                    }
                    n12 = n3;
                }
                ++n3;
            }
            if (bl8) {
                textSpan = arrtextSpan[++n9];
                n10 += textSpan.getText().length();
                pGFont = (PGFont)textSpan.getFont();
                if (pGFont == null) {
                    n |= 0x20;
                } else {
                    int n14;
                    FontResource fontResource = pGFont.getFontResource();
                    int n15 = pGFont.getFeatures();
                    boolean bl13 = bl2 = (n15 & (n14 = fontResource.getFeatures())) != 0;
                }
            }
            if (bl9) {
                by = (byte)bidi.getLevelAt(bidi.getRunStart(++n8));
                n7 = bidi.getRunLimit(n8);
                if ((by & 1) != 0) {
                    n |= 0x18;
                }
            }
            if (bl10) {
                n5 = n6;
            }
            if (!bl6) continue;
            if (n2 == 13 && ++n3 < n10 && arrc[n3] == '\n') {
                ++n3;
            }
            textRun = new TextRun(n12, n3 - n12, by, false, 0, textSpan, 0, false);
            if (n2 == 9) {
                textRun.setTab();
                n |= 4;
            } else {
                textRun.setLinebreak();
            }
            prismTextLayout.addTextRun(textRun);
            n12 = n3;
        }
        if (n12 < n4) {
            this.addTextRun(prismTextLayout, arrc, n12, n4 - n12, pGFont, textSpan, by, bl);
            if (bl) {
                n |= 0x10;
            }
        } else if (textRun == null || textRun.isLinebreak()) {
            textRun = new TextRun(n12, 0, 0, false, 0, textSpan, 0, false);
            prismTextLayout.addTextRun(textRun);
        }
        if (bidi != null && !bidi.baseIsLeftToRight()) {
            n |= 0x100;
        }
        return n |= 2;
    }

    public abstract void layout(TextRun var1, PGFont var2, FontStrike var3, char[] var4);

    protected int getInitialSlot(FontResource fontResource) {
        if (PrismFontFactory.isJreFont(fontResource)) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("Avoiding JRE Font: " + fontResource.getFullName());
            }
            return 1;
        }
        return 0;
    }

    private static GlyphLayout newInstance() {
        PrismFontFactory prismFontFactory = PrismFontFactory.getFontFactory();
        return prismFontFactory.createGlyphLayout();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static GlyphLayout getInstance() {
        if (inUse) {
            return GlyphLayout.newInstance();
        }
        Class<GlyphLayout> class_ = GlyphLayout.class;
        synchronized (GlyphLayout.class) {
            if (inUse) {
                // ** MonitorExit[var0] (shouldn't be in output)
                return GlyphLayout.newInstance();
            }
            inUse = true;
            // ** MonitorExit[var0] (shouldn't be in output)
            return reusableGL;
        }
    }

    public void dispose() {
        if (this == reusableGL) {
            inUse = false;
        }
    }

    private static boolean isIdeographic(int n) {
        if (isIdeographicMethod != null) {
            try {
                return (Boolean)isIdeographicMethod.invoke(null, n);
            }
            catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
                return false;
            }
        }
        return false;
    }

    static {
        try {
            isIdeographicMethod = Character.class.getMethod("isIdeographic", Integer.TYPE);
        }
        catch (NoSuchMethodException | SecurityException exception) {
            isIdeographicMethod = null;
        }
        reusableGL = GlyphLayout.newInstance();
    }
}

