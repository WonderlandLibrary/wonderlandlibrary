/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.freetype;

import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.freetype.OSPango;
import com.sun.javafx.font.freetype.PangoGlyphString;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.TextRun;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

class PangoGlyphLayout
extends GlyphLayout {
    private static final long fontmap = OSPango.pango_ft2_font_map_new();
    private Map<TextRun, Long> runUtf8 = new LinkedHashMap<TextRun, Long>();

    PangoGlyphLayout() {
    }

    private int getSlot(PGFont pGFont, PangoGlyphString pangoGlyphString) {
        CompositeFontResource compositeFontResource = (CompositeFontResource)pGFont.getFontResource();
        long l = pangoGlyphString.font;
        long l2 = OSPango.pango_font_describe(l);
        String string = OSPango.pango_font_description_get_family(l2);
        int n = OSPango.pango_font_description_get_style(l2);
        int n2 = OSPango.pango_font_description_get_weight(l2);
        OSPango.pango_font_description_free(l2);
        boolean bl = n2 == 700;
        boolean bl2 = n != 0;
        PrismFontFactory prismFontFactory = PrismFontFactory.getFontFactory();
        PGFont pGFont2 = prismFontFactory.createFont(string, bl, bl2, pGFont.getSize());
        String string2 = pGFont2.getFullName();
        String string3 = compositeFontResource.getSlotResource(0).getFullName();
        int n3 = 0;
        if (!string2.equalsIgnoreCase(string3)) {
            n3 = compositeFontResource.getSlotForFont(string2);
            if (PrismFontFactory.debugFonts) {
                System.err.println("\tFallback font= " + string2 + " slot=" + (n3 >> 24));
            }
        }
        return n3;
    }

    private boolean check(long l, String string, long l2, long l3, long l4) {
        if (l != 0L) {
            return false;
        }
        if (string != null && PrismFontFactory.debugFonts) {
            System.err.println(string);
        }
        if (l4 != 0L) {
            OSPango.pango_attr_list_unref(l4);
        }
        if (l3 != 0L) {
            OSPango.pango_font_description_free(l3);
        }
        if (l2 != 0L) {
            OSPango.g_object_unref(l2);
        }
        return true;
    }

    @Override
    public void layout(TextRun textRun, PGFont pGFont, FontStrike fontStrike, char[] arrc) {
        Long l;
        boolean bl;
        FontResource fontResource = pGFont.getFontResource();
        boolean bl2 = fontResource instanceof CompositeFontResource;
        if (bl2) {
            fontResource = ((CompositeFontResource)fontResource).getSlotResource(0);
        }
        if (this.check(fontmap, "Failed allocating PangoFontMap.", 0L, 0L, 0L)) {
            return;
        }
        long l2 = OSPango.pango_font_map_create_context(fontmap);
        if (this.check(l2, "Failed allocating PangoContext.", 0L, 0L, 0L)) {
            return;
        }
        boolean bl3 = bl = (textRun.getLevel() & 1) != 0;
        if (bl) {
            OSPango.pango_context_set_base_dir(l2, 1);
        }
        float f = pGFont.getSize();
        int n = fontResource.isItalic() ? 2 : 0;
        int n2 = fontResource.isBold() ? 700 : 400;
        long l3 = OSPango.pango_font_description_new();
        if (this.check(l3, "Failed allocating FontDescription.", l2, 0L, 0L)) {
            return;
        }
        OSPango.pango_font_description_set_family(l3, fontResource.getFamilyName());
        OSPango.pango_font_description_set_absolute_size(l3, f * 1024.0f);
        OSPango.pango_font_description_set_stretch(l3, 4);
        OSPango.pango_font_description_set_style(l3, n);
        OSPango.pango_font_description_set_weight(l3, n2);
        long l4 = OSPango.pango_attr_list_new();
        if (this.check(l4, "Failed allocating PangoAttributeList.", l2, l3, 0L)) {
            return;
        }
        long l5 = OSPango.pango_attr_font_desc_new(l3);
        if (this.check(l5, "Failed allocating PangoAttribute.", l2, l3, l4)) {
            return;
        }
        OSPango.pango_attr_list_insert(l4, l5);
        if (!bl2) {
            l5 = OSPango.pango_attr_fallback_new(false);
            OSPango.pango_attr_list_insert(l4, l5);
        }
        if ((l = this.runUtf8.get(textRun)) == null) {
            char[] arrc2 = Arrays.copyOfRange(arrc, textRun.getStart(), textRun.getEnd());
            l = OSPango.g_utf16_to_utf8(arrc2);
            if (this.check(l, "Failed allocating UTF-8 buffer.", l2, l3, l4)) {
                return;
            }
            this.runUtf8.put(textRun, l);
        }
        long l6 = OSPango.g_utf8_strlen(l, -1L);
        long l7 = OSPango.g_utf8_offset_to_pointer(l, l6);
        long l8 = OSPango.pango_itemize(l2, l, 0, (int)(l7 - l), l4, 0L);
        if (l8 != 0L) {
            int n3;
            int n4 = OSPango.g_list_length(l8);
            PangoGlyphString[] arrpangoGlyphString = new PangoGlyphString[n4];
            for (n3 = 0; n3 < n4; ++n3) {
                long l9 = OSPango.g_list_nth_data(l8, n3);
                if (l9 == 0L) continue;
                arrpangoGlyphString[n3] = OSPango.pango_shape(l, l9);
                OSPango.pango_item_free(l9);
            }
            OSPango.g_list_free(l8);
            n3 = 0;
            for (PangoGlyphString n5 : arrpangoGlyphString) {
                if (n5 == null) continue;
                n3 += n5.num_glyphs;
            }
            int[] arrn = new int[n3];
            float[] arrf = new float[n3 * 2 + 2];
            int[] arrn2 = new int[n3];
            int n5 = 0;
            int n6 = bl ? textRun.getLength() : 0;
            int n7 = 0;
            for (PangoGlyphString pangoGlyphString : arrpangoGlyphString) {
                int n8;
                if (pangoGlyphString == null) continue;
                int n9 = n8 = bl2 ? this.getSlot(pGFont, pangoGlyphString) : 0;
                if (bl) {
                    n6 -= pangoGlyphString.num_chars;
                }
                for (int i = 0; i < pangoGlyphString.num_glyphs; ++i) {
                    int n10;
                    int n11 = n5 + i;
                    if (n8 != -1 && 0 <= (n10 = pangoGlyphString.glyphs[i]) && n10 <= 0xFFFFFF) {
                        arrn[n11] = n8 << 24 | n10;
                    }
                    if (f != 0.0f) {
                        arrf[2 + (n11 << 1)] = (float)(n7 += pangoGlyphString.widths[i]) / 1024.0f;
                    }
                    arrn2[n11] = pangoGlyphString.log_clusters[i] + n6;
                }
                if (!bl) {
                    n6 += pangoGlyphString.num_chars;
                }
                n5 += pangoGlyphString.num_glyphs;
            }
            textRun.shape(n3, arrn, arrf, arrn2);
        }
        this.check(0L, null, l2, l3, l4);
    }

    @Override
    public void dispose() {
        super.dispose();
        for (Long l : this.runUtf8.values()) {
            OSPango.g_free(l);
        }
        this.runUtf8.clear();
    }
}

