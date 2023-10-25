/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.CompositeFontResource;
import java.util.HashMap;

public class CompositeGlyphMapper
extends CharToGlyphMapper {
    public static final int SLOTMASK = -16777216;
    public static final int GLYPHMASK = 0xFFFFFF;
    public static final int NBLOCKS = 216;
    public static final int BLOCKSZ = 256;
    public static final int MAXUNICODE = 55296;
    private static final int SIMPLE_ASCII_MASK_START = 32;
    private static final int SIMPLE_ASCII_MASK_END = 126;
    private static final int ASCII_COUNT = 95;
    private boolean asciiCacheOK;
    private char[] charToGlyph;
    CompositeFontResource font;
    CharToGlyphMapper[] slotMappers;
    HashMap<Integer, Integer> glyphMap;

    public CompositeGlyphMapper(CompositeFontResource compositeFontResource) {
        this.font = compositeFontResource;
        this.missingGlyph = 0;
        this.glyphMap = new HashMap();
        this.slotMappers = new CharToGlyphMapper[compositeFontResource.getNumSlots()];
        this.asciiCacheOK = true;
    }

    private final CharToGlyphMapper getSlotMapper(int n) {
        Object object;
        if (n >= this.slotMappers.length) {
            object = new CharToGlyphMapper[this.font.getNumSlots()];
            System.arraycopy(this.slotMappers, 0, object, 0, this.slotMappers.length);
            this.slotMappers = object;
        }
        if ((object = this.slotMappers[n]) == null) {
            this.slotMappers[n] = object = this.font.getSlotResource(n).getGlyphMapper();
        }
        return object;
    }

    @Override
    public int getMissingGlyphCode() {
        return this.missingGlyph;
    }

    public final int compositeGlyphCode(int n, int n2) {
        return n << 24 | n2 & 0xFFFFFF;
    }

    private final int convertToGlyph(int n) {
        for (int i = 0; i < this.font.getNumSlots(); ++i) {
            if (i >= 255) {
                return this.missingGlyph;
            }
            CharToGlyphMapper charToGlyphMapper = this.getSlotMapper(i);
            int n2 = charToGlyphMapper.charToGlyph(n);
            if (n2 == charToGlyphMapper.getMissingGlyphCode()) continue;
            n2 = this.compositeGlyphCode(i, n2);
            this.glyphMap.put(n, n2);
            return n2;
        }
        return this.missingGlyph;
    }

    private int getAsciiGlyphCode(int n) {
        if (!this.asciiCacheOK || n > 126 || n < 32) {
            return -1;
        }
        if (this.charToGlyph == null) {
            char[] arrc = new char[95];
            CharToGlyphMapper charToGlyphMapper = this.getSlotMapper(0);
            int n2 = charToGlyphMapper.getMissingGlyphCode();
            for (int i = 0; i < 95; ++i) {
                int n3 = charToGlyphMapper.charToGlyph(32 + i);
                if (n3 == n2) {
                    this.charToGlyph = null;
                    this.asciiCacheOK = false;
                    return -1;
                }
                arrc[i] = (char)n3;
            }
            this.charToGlyph = arrc;
        }
        int n4 = n - 32;
        return this.charToGlyph[n4];
    }

    @Override
    public int getGlyphCode(int n) {
        int n2 = this.getAsciiGlyphCode(n);
        if (n2 >= 0) {
            return n2;
        }
        Integer n3 = this.glyphMap.get(n);
        if (n3 != null) {
            return n3;
        }
        return this.convertToGlyph(n);
    }
}

