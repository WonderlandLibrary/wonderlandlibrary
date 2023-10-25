/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontFileReader;
import com.sun.javafx.font.FontFileWriter;
import java.util.Arrays;
import java.util.zip.Inflater;

class WoffDecoder
extends FontFileWriter {
    WoffHeader woffHeader;
    WoffDirectoryEntry[] woffTableDirectory;

    public void decode(FontFileReader fontFileReader) throws Exception {
        fontFileReader.reset();
        this.initWoffTables(fontFileReader);
        if (this.woffHeader == null || this.woffTableDirectory == null) {
            throw new Exception("WoffDecoder: failure reading header");
        }
        int n = this.woffHeader.flavor;
        if (n != 65536 && n != 1953658213 && n != 0x4F54544F) {
            throw new Exception("WoffDecoder: invalid flavor");
        }
        short s = this.woffHeader.numTables;
        this.setLength(this.woffHeader.totalSfntSize);
        this.writeHeader(n, s);
        Arrays.sort(this.woffTableDirectory, (woffDirectoryEntry, woffDirectoryEntry2) -> woffDirectoryEntry.offset - woffDirectoryEntry2.offset);
        Inflater inflater = new Inflater();
        int n2 = 12 + s * 16;
        for (int i = 0; i < this.woffTableDirectory.length; ++i) {
            WoffDirectoryEntry woffDirectoryEntry3 = this.woffTableDirectory[i];
            this.writeDirectoryEntry(woffDirectoryEntry3.index, woffDirectoryEntry3.tag, woffDirectoryEntry3.origChecksum, n2, woffDirectoryEntry3.origLength);
            FontFileReader.Buffer buffer = fontFileReader.readBlock(woffDirectoryEntry3.offset, woffDirectoryEntry3.comLength);
            byte[] arrby = new byte[woffDirectoryEntry3.comLength];
            buffer.get(0, arrby, 0, woffDirectoryEntry3.comLength);
            if (woffDirectoryEntry3.comLength != woffDirectoryEntry3.origLength) {
                inflater.setInput(arrby);
                byte[] arrby2 = new byte[woffDirectoryEntry3.origLength];
                int n3 = inflater.inflate(arrby2);
                if (n3 != woffDirectoryEntry3.origLength) {
                    throw new Exception("WoffDecoder: failure expanding table");
                }
                inflater.reset();
                arrby = arrby2;
            }
            this.seek(n2);
            this.writeBytes(arrby);
            n2 += woffDirectoryEntry3.origLength + 3 & 0xFFFFFFFC;
        }
        inflater.end();
    }

    void initWoffTables(FontFileReader fontFileReader) throws Exception {
        long l = fontFileReader.getLength();
        if (l < 44L) {
            throw new Exception("WoffDecoder: invalid filesize");
        }
        FontFileReader.Buffer buffer = fontFileReader.readBlock(0, 44);
        WoffHeader woffHeader = new WoffHeader(buffer);
        int n = woffHeader.numTables;
        if (woffHeader.signature != 2001684038) {
            throw new Exception("WoffDecoder: invalid signature");
        }
        if (woffHeader.reserved != 0) {
            throw new Exception("WoffDecoder: invalid reserved != 0");
        }
        if (l < (long)(44 + n * 20)) {
            throw new Exception("WoffDecoder: invalid filesize");
        }
        WoffDirectoryEntry[] arrwoffDirectoryEntry = new WoffDirectoryEntry[n];
        int n2 = 44 + n * 20;
        int n3 = 12 + n * 16;
        buffer = fontFileReader.readBlock(44, n * 20);
        int n4 = 0;
        for (int i = 0; i < n; ++i) {
            WoffDirectoryEntry woffDirectoryEntry;
            arrwoffDirectoryEntry[i] = woffDirectoryEntry = new WoffDirectoryEntry(buffer, i);
            if (woffDirectoryEntry.tag <= n4) {
                throw new Exception("WoffDecoder: table directory not ordered by tag");
            }
            int n5 = woffDirectoryEntry.offset;
            int n6 = woffDirectoryEntry.offset + woffDirectoryEntry.comLength;
            if (n2 > n5 || (long)n5 > l) {
                throw new Exception("WoffDecoder: invalid table offset");
            }
            if (n5 > n6 || (long)n6 > l) {
                throw new Exception("WoffDecoder: invalid table offset");
            }
            if (woffDirectoryEntry.comLength > woffDirectoryEntry.origLength) {
                throw new Exception("WoffDecoder: invalid compressed length");
            }
            if ((n3 += woffDirectoryEntry.origLength + 3 & 0xFFFFFFFC) <= woffHeader.totalSfntSize) continue;
            throw new Exception("WoffDecoder: invalid totalSfntSize");
        }
        if (n3 != woffHeader.totalSfntSize) {
            throw new Exception("WoffDecoder: invalid totalSfntSize");
        }
        this.woffHeader = woffHeader;
        this.woffTableDirectory = arrwoffDirectoryEntry;
    }

    static class WoffHeader {
        int signature;
        int flavor;
        int length;
        short numTables;
        short reserved;
        int totalSfntSize;
        short majorVersion;
        short minorVersion;
        int metaOffset;
        int metaLength;
        int metaOrigLength;
        int privateOffset;
        int privateLength;

        WoffHeader(FontFileReader.Buffer buffer) {
            this.signature = buffer.getInt();
            this.flavor = buffer.getInt();
            this.length = buffer.getInt();
            this.numTables = buffer.getShort();
            this.reserved = buffer.getShort();
            this.totalSfntSize = buffer.getInt();
            this.majorVersion = buffer.getShort();
            this.minorVersion = buffer.getShort();
            this.metaOffset = buffer.getInt();
            this.metaLength = buffer.getInt();
            this.metaOrigLength = buffer.getInt();
            this.privateOffset = buffer.getInt();
            this.privateLength = buffer.getInt();
        }
    }

    static class WoffDirectoryEntry {
        int tag;
        int offset;
        int comLength;
        int origLength;
        int origChecksum;
        int index;

        WoffDirectoryEntry(FontFileReader.Buffer buffer, int n) {
            this.tag = buffer.getInt();
            this.offset = buffer.getInt();
            this.comLength = buffer.getInt();
            this.origLength = buffer.getInt();
            this.origChecksum = buffer.getInt();
            this.index = n;
        }
    }
}

