/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontFileReader;
import com.sun.javafx.font.PrismFontFile;

abstract class CMap {
    static final char noSuchChar = '\ufffd';
    static final int SHORTMASK = 65535;
    static final int INTMASK = -1;
    private static final int MAX_CODE_POINTS = 0x10FFFF;
    public static final NullCMapClass theNullCmap = new NullCMapClass();

    CMap() {
    }

    static CMap initialize(PrismFontFile prismFontFile) {
        CMap cMap = null;
        int n = -1;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        boolean bl = false;
        boolean bl2 = false;
        FontFileReader.Buffer buffer = prismFontFile.readTable(1668112752);
        int n6 = buffer.getShort(2);
        block5: for (int i = 0; i < n6; ++i) {
            buffer.position(i * 8 + 4);
            short s = buffer.getShort();
            if (s == 0) {
                bl = true;
                n = buffer.getShort();
                n5 = buffer.getInt();
                continue;
            }
            if (s != 3) continue;
            bl2 = true;
            n = buffer.getShort();
            int n7 = buffer.getInt();
            switch (n) {
                case 0: {
                    n2 = n7;
                    continue block5;
                }
                case 1: {
                    n3 = n7;
                    continue block5;
                }
                case 10: {
                    n4 = n7;
                }
            }
        }
        if (bl2) {
            if (n4 != 0) {
                cMap = CMap.createCMap(buffer, n4);
            } else if (n2 != 0) {
                cMap = CMap.createCMap(buffer, n2);
            } else if (n3 != 0) {
                cMap = CMap.createCMap(buffer, n3);
            }
        } else {
            cMap = bl && n5 != 0 ? CMap.createCMap(buffer, n5) : CMap.createCMap(buffer, buffer.getInt(8));
        }
        return cMap;
    }

    static CMap createCMap(FontFileReader.Buffer buffer, int n) {
        char c = buffer.getChar(n);
        switch (c) {
            case '\u0000': {
                return new CMapFormat0(buffer, n);
            }
            case '\u0002': {
                return new CMapFormat2(buffer, n);
            }
            case '\u0004': {
                return new CMapFormat4(buffer, n);
            }
            case '\u0006': {
                return new CMapFormat6(buffer, n);
            }
            case '\b': {
                return new CMapFormat8(buffer, n);
            }
            case '\n': {
                return new CMapFormat10(buffer, n);
            }
            case '\f': {
                return new CMapFormat12(buffer, n);
            }
        }
        throw new RuntimeException("Cmap format unimplemented: " + buffer.getChar(n));
    }

    abstract char getGlyph(int var1);

    final int getControlCodeGlyph(int n, boolean bl) {
        if (n < 16) {
            switch (n) {
                case 9: 
                case 10: 
                case 13: {
                    return 65535;
                }
            }
        } else if (n >= 8204) {
            if (n <= 8207 || n >= 8232 && n <= 8238 || n >= 8298 && n <= 8303) {
                return 65535;
            }
            if (bl && n >= 65535) {
                return 0;
            }
        }
        return -1;
    }

    static class CMapFormat0
    extends CMap {
        byte[] cmap;

        CMapFormat0(FontFileReader.Buffer buffer, int n) {
            char c = buffer.getChar(n + 2);
            this.cmap = new byte[c - 6];
            buffer.get(n + 6, this.cmap, 0, c - 6);
        }

        @Override
        char getGlyph(int n) {
            if (n < 256) {
                if (n < 16) {
                    switch (n) {
                        case 9: 
                        case 10: 
                        case 13: {
                            return '\uffff';
                        }
                    }
                }
                return (char)(0xFF & this.cmap[n]);
            }
            return '\u0000';
        }
    }

    static class CMapFormat2
    extends CMap {
        char[] subHeaderKey = new char[256];
        char[] firstCodeArray;
        char[] entryCountArray;
        short[] idDeltaArray;
        char[] idRangeOffSetArray;
        char[] glyphIndexArray;

        CMapFormat2(FontFileReader.Buffer buffer, int n) {
            int n2;
            int n3;
            char c = buffer.getChar(n + 2);
            buffer.position(n + 6);
            char c2 = '\u0000';
            for (n3 = 0; n3 < 256; ++n3) {
                this.subHeaderKey[n3] = buffer.getChar();
                if (this.subHeaderKey[n3] <= c2) continue;
                c2 = this.subHeaderKey[n3];
            }
            n3 = (c2 >> 3) + 1;
            this.firstCodeArray = new char[n3];
            this.entryCountArray = new char[n3];
            this.idDeltaArray = new short[n3];
            this.idRangeOffSetArray = new char[n3];
            for (n2 = 0; n2 < n3; ++n2) {
                this.firstCodeArray[n2] = buffer.getChar();
                this.entryCountArray[n2] = buffer.getChar();
                this.idDeltaArray[n2] = (short)buffer.getChar();
                this.idRangeOffSetArray[n2] = buffer.getChar();
            }
            n2 = (c - 518 - n3 * 8) / 2;
            this.glyphIndexArray = new char[n2];
            for (int i = 0; i < n2; ++i) {
                this.glyphIndexArray[i] = buffer.getChar();
            }
        }

        @Override
        char getGlyph(int n) {
            int n2;
            int n3;
            char c;
            char c2;
            int n4 = this.getControlCodeGlyph(n, true);
            if (n4 >= 0) {
                return (char)n4;
            }
            char c3 = (char)(n >> 8);
            char c4 = (char)(n & 0xFF);
            int n5 = this.subHeaderKey[c3] >> 3;
            if (n5 != 0) {
                c2 = c4;
            } else {
                c2 = c3;
                if (c2 == '\u0000') {
                    c2 = c4;
                }
            }
            char c5 = this.firstCodeArray[n5];
            if (c2 < c5) {
                return '\u0000';
            }
            if ((c2 = (char)(c2 - c5)) < this.entryCountArray[n5] && (c = this.glyphIndexArray[(n3 = (this.idRangeOffSetArray[n5] - (n2 = (this.idRangeOffSetArray.length - n5) * 8 - 6)) / 2) + c2]) != '\u0000') {
                c = (char)(c + this.idDeltaArray[n5]);
                return c;
            }
            return '\u0000';
        }
    }

    static class CMapFormat4
    extends CMap {
        int segCount;
        int entrySelector;
        int rangeShift;
        char[] endCount;
        char[] startCount;
        short[] idDelta;
        char[] idRangeOffset;
        char[] glyphIds;

        CMapFormat4(FontFileReader.Buffer buffer, int n) {
            int n2;
            int n3;
            buffer.position(n);
            buffer.getChar();
            int n4 = buffer.getChar();
            if (n + n4 > buffer.capacity()) {
                n4 = buffer.capacity() - n;
            }
            buffer.getChar();
            this.segCount = buffer.getChar() / 2;
            buffer.getChar();
            this.entrySelector = buffer.getChar();
            this.rangeShift = buffer.getChar() / 2;
            this.startCount = new char[this.segCount];
            this.endCount = new char[this.segCount];
            this.idDelta = new short[this.segCount];
            this.idRangeOffset = new char[this.segCount];
            for (n3 = 0; n3 < this.segCount; ++n3) {
                this.endCount[n3] = buffer.getChar();
            }
            buffer.getChar();
            for (n3 = 0; n3 < this.segCount; ++n3) {
                this.startCount[n3] = buffer.getChar();
            }
            for (n3 = 0; n3 < this.segCount; ++n3) {
                this.idDelta[n3] = (short)buffer.getChar();
            }
            for (n3 = 0; n3 < this.segCount; ++n3) {
                n2 = buffer.getChar();
                this.idRangeOffset[n3] = (char)(n2 >> 1 & 0xFFFF);
            }
            n3 = (this.segCount * 8 + 16) / 2;
            buffer.position(n3 * 2 + n);
            n2 = n4 / 2 - n3;
            this.glyphIds = new char[n2];
            for (int i = 0; i < n2; ++i) {
                this.glyphIds[i] = buffer.getChar();
            }
        }

        @Override
        char getGlyph(int n) {
            int n2 = 0;
            char c = '\u0000';
            int n3 = this.getControlCodeGlyph(n, true);
            if (n3 >= 0) {
                return (char)n3;
            }
            int n4 = 0;
            int n5 = this.startCount.length;
            n2 = this.startCount.length >> 1;
            while (n4 < n5) {
                if (this.endCount[n2] < n) {
                    n4 = n2 + 1;
                } else {
                    n5 = n2;
                }
                n2 = n4 + n5 >> 1;
            }
            if (n >= this.startCount[n2] && n <= this.endCount[n2]) {
                char c2 = this.idRangeOffset[n2];
                if (c2 == '\u0000') {
                    c = (char)(n + this.idDelta[n2]);
                } else {
                    int n6 = c2 - this.segCount + n2 + (n - this.startCount[n2]);
                    c = this.glyphIds[n6];
                    if (c != '\u0000') {
                        c = (char)(c + this.idDelta[n2]);
                    }
                }
            }
            return c;
        }
    }

    static class CMapFormat6
    extends CMap {
        char firstCode;
        char entryCount;
        char[] glyphIdArray;

        CMapFormat6(FontFileReader.Buffer buffer, int n) {
            buffer.position(n + 6);
            this.firstCode = buffer.getChar();
            this.entryCount = buffer.getChar();
            this.glyphIdArray = new char[this.entryCount];
            for (int i = 0; i < this.entryCount; ++i) {
                this.glyphIdArray[i] = buffer.getChar();
            }
        }

        @Override
        char getGlyph(int n) {
            int n2 = this.getControlCodeGlyph(n, true);
            if (n2 >= 0) {
                return (char)n2;
            }
            if ((n -= this.firstCode) < 0 || n >= this.entryCount) {
                return '\u0000';
            }
            return this.glyphIdArray[n];
        }
    }

    static class CMapFormat8
    extends CMap {
        CMapFormat8(FontFileReader.Buffer buffer, int n) {
        }

        @Override
        char getGlyph(int n) {
            return '\u0000';
        }
    }

    static class CMapFormat10
    extends CMap {
        long startCharCode;
        int numChars;
        char[] glyphIdArray;

        CMapFormat10(FontFileReader.Buffer buffer, int n) {
            buffer.position(n + 12);
            this.startCharCode = buffer.getInt() & 0xFFFFFFFF;
            this.numChars = buffer.getInt() & 0xFFFFFFFF;
            if (this.numChars <= 0 || this.numChars > 0x10FFFF || n > buffer.capacity() - this.numChars * 2 - 12 - 8) {
                throw new RuntimeException("Invalid cmap subtable");
            }
            this.glyphIdArray = new char[this.numChars];
            for (int i = 0; i < this.numChars; ++i) {
                this.glyphIdArray[i] = buffer.getChar();
            }
        }

        @Override
        char getGlyph(int n) {
            int n2 = (int)((long)n - this.startCharCode);
            if (n2 < 0 || n2 >= this.numChars) {
                return '\u0000';
            }
            return this.glyphIdArray[n2];
        }
    }

    static class CMapFormat12
    extends CMap {
        int numGroups;
        int highBit = 0;
        int power;
        int extra;
        long[] startCharCode;
        long[] endCharCode;
        int[] startGlyphID;

        CMapFormat12(FontFileReader.Buffer buffer, int n) {
            int n2;
            this.numGroups = buffer.getInt(n + 12);
            if (this.numGroups <= 0 || this.numGroups > 0x10FFFF || n > buffer.capacity() - this.numGroups * 12 - 12 - 4) {
                throw new RuntimeException("Invalid cmap subtable");
            }
            this.startCharCode = new long[this.numGroups];
            this.endCharCode = new long[this.numGroups];
            this.startGlyphID = new int[this.numGroups];
            buffer.position(n + 16);
            for (n2 = 0; n2 < this.numGroups; ++n2) {
                this.startCharCode[n2] = buffer.getInt() & 0xFFFFFFFF;
                this.endCharCode[n2] = buffer.getInt() & 0xFFFFFFFF;
                this.startGlyphID[n2] = buffer.getInt() & 0xFFFFFFFF;
            }
            n2 = this.numGroups;
            if (n2 >= 65536) {
                n2 >>= 16;
                this.highBit += 16;
            }
            if (n2 >= 256) {
                n2 >>= 8;
                this.highBit += 8;
            }
            if (n2 >= 16) {
                n2 >>= 4;
                this.highBit += 4;
            }
            if (n2 >= 4) {
                n2 >>= 2;
                this.highBit += 2;
            }
            if (n2 >= 2) {
                n2 >>= 1;
                ++this.highBit;
            }
            this.power = 1 << this.highBit;
            this.extra = this.numGroups - this.power;
        }

        @Override
        char getGlyph(int n) {
            int n2 = this.getControlCodeGlyph(n, false);
            if (n2 >= 0) {
                return (char)n2;
            }
            int n3 = this.power;
            int n4 = 0;
            if (this.startCharCode[this.extra] <= (long)n) {
                n4 = this.extra;
            }
            while (n3 > 1) {
                if (this.startCharCode[n4 + (n3 >>= 1)] > (long)n) continue;
                n4 += n3;
            }
            if (this.startCharCode[n4] <= (long)n && this.endCharCode[n4] >= (long)n) {
                return (char)((long)this.startGlyphID[n4] + ((long)n - this.startCharCode[n4]));
            }
            return '\u0000';
        }
    }

    static class NullCMapClass
    extends CMap {
        NullCMapClass() {
        }

        @Override
        char getGlyph(int n) {
            return '\u0000';
        }
    }
}

