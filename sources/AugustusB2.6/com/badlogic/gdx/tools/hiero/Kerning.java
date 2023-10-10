// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.tools.hiero;

import java.io.EOFException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import com.badlogic.gdx.utils.IntArray;
import java.io.IOException;
import java.io.InputStream;
import com.badlogic.gdx.utils.IntIntMap;

public final class Kerning
{
    private TTFInputStream input;
    private float scale;
    private int headOffset;
    private int kernOffset;
    private int gposOffset;
    private IntIntMap kernings;
    
    public Kerning() {
        this.headOffset = -1;
        this.kernOffset = -1;
        this.gposOffset = -1;
        this.kernings = new IntIntMap();
    }
    
    public final void load(final InputStream inputStream, final int fontSize) throws IOException {
        this.input = new TTFInputStream(inputStream);
        inputStream.close();
        this.input.skip(4L);
        final int unsignedShort = this.input.readUnsignedShort();
        this.input.skip(6L);
        final byte[] bytes = new byte[4];
        for (int i = 0; i < unsignedShort; ++i) {
            bytes[0] = (byte)this.input.readUnsignedByte();
            bytes[1] = (byte)this.input.readUnsignedByte();
            bytes[2] = (byte)this.input.readUnsignedByte();
            bytes[3] = (byte)this.input.readUnsignedByte();
            this.input.skip(4L);
            final int gposOffset = (int)this.input.readUnsignedLong();
            this.input.skip(4L);
            final String s;
            if ((s = new String(bytes, "ISO-8859-1")).equals("head")) {
                this.headOffset = gposOffset;
            }
            else if (s.equals("kern")) {
                this.kernOffset = gposOffset;
            }
            else if (s.equals("GPOS")) {
                this.gposOffset = gposOffset;
            }
        }
        if (this.headOffset == -1) {
            throw new IOException("HEAD table not found.");
        }
        this.input.seek(this.headOffset + 8 + 8 + 2);
        this.scale = fontSize / (float)this.input.readUnsignedShort();
        if (this.gposOffset != -1) {
            this.input.seek(this.gposOffset);
            this.readGPOS();
        }
        if (this.kernOffset != -1) {
            this.input.seek(this.kernOffset);
            this.readKERN();
        }
        this.input.close();
        this.input = null;
    }
    
    public final IntIntMap getKernings() {
        return this.kernings;
    }
    
    private void storeKerningOffset(int firstGlyphCode, final int secondGlyphCode, int offset) {
        if ((offset = Math.round(offset * this.scale)) == 0) {
            return;
        }
        firstGlyphCode = (firstGlyphCode << 16 | secondGlyphCode);
        this.kernings.put(firstGlyphCode, offset);
    }
    
    private void readKERN() throws IOException {
        this.input.seek(this.kernOffset + 2);
        for (int subTableCount = this.input.readUnsignedShort(); subTableCount > 0; --subTableCount) {
            this.input.skip(4L);
            final int tupleIndex;
            if (((tupleIndex = this.input.readUnsignedShort()) & 0x1) == 0x0 || (tupleIndex & 0x2) != 0x0 || (tupleIndex & 0x4) != 0x0) {
                return;
            }
            if (tupleIndex >> 8 == 0) {
                int kerningCount = this.input.readUnsignedShort();
                this.input.skip(6L);
                while (kerningCount-- > 0) {
                    final int firstGlyphCode = this.input.readUnsignedShort();
                    final int secondGlyphCode = this.input.readUnsignedShort();
                    final int offset = (short)this.input.readUnsignedShort();
                    this.storeKerningOffset(firstGlyphCode, secondGlyphCode, offset);
                }
            }
        }
    }
    
    private void readGPOS() throws IOException {
        this.input.seek(this.gposOffset + 4 + 2 + 2);
        final int lookupListOffset = this.input.readUnsignedShort();
        this.input.seek(this.gposOffset + lookupListOffset);
        final int lookupListPosition = this.input.getPosition();
        final int lookupCount = this.input.readUnsignedShort();
        final int[] lookupOffsets = this.input.readUnsignedShortArray(lookupCount);
        for (int i = 0; i < lookupCount; ++i) {
            final int lookupPosition = lookupListPosition + lookupOffsets[i];
            this.input.seek(lookupPosition);
            final int unsignedShort;
            final int type = unsignedShort = this.input.readUnsignedShort();
            final int n = lookupPosition;
            final int type2 = unsignedShort;
            this.input.skip(2L);
            final int unsignedShort2 = this.input.readUnsignedShort();
            final int[] unsignedShortArray = this.input.readUnsignedShortArray(unsignedShort2);
            for (int j = 0; j < unsignedShort2; ++j) {
                this.readSubtable(type2, n + unsignedShortArray[j]);
            }
        }
    }
    
    private void readSubtable(final int type, final int subTablePosition) throws IOException {
        this.input.seek(subTablePosition);
        if (type == 2) {
            this.readPairAdjustmentSubtable(subTablePosition);
            return;
        }
        if (type == 9) {
            this.readExtensionPositioningSubtable(subTablePosition);
        }
    }
    
    private void readPairAdjustmentSubtable(final int subTablePosition) throws IOException {
        final int type;
        if ((type = this.input.readUnsignedShort()) == 1) {
            this.readPairPositioningAdjustmentFormat1(subTablePosition);
            return;
        }
        if (type == 2) {
            this.readPairPositioningAdjustmentFormat2(subTablePosition);
        }
    }
    
    private void readExtensionPositioningSubtable(final int subTablePosition) throws IOException {
        if (this.input.readUnsignedShort() == 1) {
            this.readExtensionPositioningFormat1(subTablePosition);
        }
    }
    
    private void readPairPositioningAdjustmentFormat1(final long subTablePosition) throws IOException {
        final int coverageOffset = this.input.readUnsignedShort();
        final int valueFormat1 = this.input.readUnsignedShort();
        final int valueFormat2 = this.input.readUnsignedShort();
        int pairSetCount = this.input.readUnsignedShort();
        final int[] pairSetOffsets = this.input.readUnsignedShortArray(pairSetCount);
        this.input.seek((int)(subTablePosition + coverageOffset));
        final int[] coverage = this.readCoverageTable();
        pairSetCount = Math.min(pairSetCount, coverage.length);
        for (int i = 0; i < pairSetCount; ++i) {
            final int firstGlyph = coverage[i];
            this.input.seek((int)(subTablePosition + pairSetOffsets[i]));
            for (int pairValueCount = this.input.readUnsignedShort(), j = 0; j < pairValueCount; ++j) {
                final int secondGlyph = this.input.readUnsignedShort();
                final int xAdvance1 = this.readXAdvanceFromValueRecord(valueFormat1);
                this.readXAdvanceFromValueRecord(valueFormat2);
                if (xAdvance1 != 0) {
                    this.storeKerningOffset(firstGlyph, secondGlyph, xAdvance1);
                }
            }
        }
    }
    
    private void readPairPositioningAdjustmentFormat2(final int subTablePosition) throws IOException {
        final int coverageOffset = this.input.readUnsignedShort();
        final int valueFormat1 = this.input.readUnsignedShort();
        final int valueFormat2 = this.input.readUnsignedShort();
        final int classDefOffset1 = this.input.readUnsignedShort();
        final int classDefOffset2 = this.input.readUnsignedShort();
        final int class1Count = this.input.readUnsignedShort();
        final int class2Count = this.input.readUnsignedShort();
        final int position = this.input.getPosition();
        this.input.seek(subTablePosition + coverageOffset);
        final int[] coverage = this.readCoverageTable();
        this.input.seek(position);
        final IntArray[] glyphsByClass1 = this.readClassDefinition(subTablePosition + classDefOffset1, class1Count);
        final IntArray[] glyphsByClass2 = this.readClassDefinition(subTablePosition + classDefOffset2, class2Count);
        this.input.seek(position);
        for (int i = 0; i < coverage.length; ++i) {
            final int glyph = coverage[i];
            boolean found = false;
            for (int j = 1; j < class1Count && !found; found = glyphsByClass1[j].contains(glyph), ++j) {}
            if (!found) {
                glyphsByClass1[0].add(glyph);
            }
        }
        for (int i = 0; i < class1Count; ++i) {
            for (int k = 0; k < class2Count; ++k) {
                final int xAdvance1 = this.readXAdvanceFromValueRecord(valueFormat1);
                this.readXAdvanceFromValueRecord(valueFormat2);
                if (xAdvance1 != 0) {
                    for (int l = 0; l < glyphsByClass1[i].size; ++l) {
                        final int glyph2 = glyphsByClass1[i].items[l];
                        for (int m = 0; m < glyphsByClass2[k].size; ++m) {
                            final int glyph3 = glyphsByClass2[k].items[m];
                            this.storeKerningOffset(glyph2, glyph3, xAdvance1);
                        }
                    }
                }
            }
        }
    }
    
    private void readExtensionPositioningFormat1(int subTablePosition) throws IOException {
        final int lookupType = this.input.readUnsignedShort();
        subTablePosition += (int)this.input.readUnsignedLong();
        this.readSubtable(lookupType, subTablePosition);
    }
    
    private IntArray[] readClassDefinition(final int position, final int classCount) throws IOException {
        this.input.seek(position);
        final IntArray[] glyphsByClass = new IntArray[classCount];
        for (int i = 0; i < classCount; ++i) {
            glyphsByClass[i] = new IntArray();
        }
        final int classFormat;
        if ((classFormat = this.input.readUnsignedShort()) == 1) {
            this.readClassDefinitionFormat1(glyphsByClass);
        }
        else {
            if (classFormat != 2) {
                throw new IOException("Unknown class definition table type " + classFormat);
            }
            this.readClassDefinitionFormat2(glyphsByClass);
        }
        return glyphsByClass;
    }
    
    private void readClassDefinitionFormat1(final IntArray[] glyphsByClass) throws IOException {
        final int startGlyph = this.input.readUnsignedShort();
        final int glyphCount = this.input.readUnsignedShort();
        final int[] classValueArray = this.input.readUnsignedShortArray(glyphCount);
        for (int i = 0; i < glyphCount; ++i) {
            final int glyph = startGlyph + i;
            final int glyphClass;
            if ((glyphClass = classValueArray[i]) < glyphsByClass.length) {
                glyphsByClass[glyphClass].add(glyph);
            }
        }
    }
    
    private void readClassDefinitionFormat2(final IntArray[] glyphsByClass) throws IOException {
        for (int classRangeCount = this.input.readUnsignedShort(), i = 0; i < classRangeCount; ++i) {
            final int start = this.input.readUnsignedShort();
            final int end = this.input.readUnsignedShort();
            final int glyphClass;
            if ((glyphClass = this.input.readUnsignedShort()) < glyphsByClass.length) {
                for (int glyph = start; glyph <= end; ++glyph) {
                    glyphsByClass[glyphClass].add(glyph);
                }
            }
        }
    }
    
    private int[] readCoverageTable() throws IOException {
        final int unsignedShort;
        if ((unsignedShort = this.input.readUnsignedShort()) == 1) {
            final int glyphCount = this.input.readUnsignedShort();
            return this.input.readUnsignedShortArray(glyphCount);
        }
        if (unsignedShort == 2) {
            final int rangeCount = this.input.readUnsignedShort();
            final IntArray glyphArray = new IntArray();
            for (int i = 0; i < rangeCount; ++i) {
                final int start = this.input.readUnsignedShort();
                final int end = this.input.readUnsignedShort();
                this.input.skip(2L);
                for (int glyph = start; glyph <= end; ++glyph) {
                    glyphArray.add(glyph);
                }
            }
            return glyphArray.shrink();
        }
        throw new IOException("Unknown coverage table format " + unsignedShort);
    }
    
    private int readXAdvanceFromValueRecord(final int valueFormat) throws IOException {
        int xAdvance = 0;
        for (int mask = 1; mask <= 32768 && mask <= valueFormat; mask <<= 1) {
            if ((valueFormat & mask) != 0x0) {
                final int value = (short)this.input.readUnsignedShort();
                if (mask == 4) {
                    xAdvance = value;
                }
            }
        }
        return xAdvance;
    }
    
    static final class TTFInputStream extends ByteArrayInputStream
    {
        public TTFInputStream(final InputStream input) throws IOException {
            super(readAllBytes(input));
        }
        
        private static byte[] readAllBytes(final InputStream input) throws IOException {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final byte[] buffer = new byte[16384];
            int numRead;
            while ((numRead = input.read(buffer, 0, 16384)) != -1) {
                out.write(buffer, 0, numRead);
            }
            return out.toByteArray();
        }
        
        public final int getPosition() {
            return this.pos;
        }
        
        public final void seek(final int position) {
            this.pos = position;
        }
        
        public final int readUnsignedByte() throws IOException {
            final int b;
            if ((b = this.read()) == -1) {
                throw new EOFException("Unexpected end of file.");
            }
            return b;
        }
        
        public final int readUnsignedShort() throws IOException {
            return (this.readUnsignedByte() << 8) + this.readUnsignedByte();
        }
        
        public final long readUnsignedLong() throws IOException {
            return ((((long)this.readUnsignedByte() << 8) + this.readUnsignedByte() << 8) + this.readUnsignedByte() << 8) + this.readUnsignedByte();
        }
        
        public final int[] readUnsignedShortArray(final int count) throws IOException {
            final int[] shorts = new int[count];
            for (int i = 0; i < count; ++i) {
                shorts[i] = this.readUnsignedShort();
            }
            return shorts;
        }
    }
}
